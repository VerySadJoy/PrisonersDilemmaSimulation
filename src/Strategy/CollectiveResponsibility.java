package Strategy;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 전략 이름: CollectiveResponsibility
 * 전략 개요: 특정 상대의 행동이 아닌, 네트워크 전체의 협력 비율을 기반으로 행동을 결정하는 전략
 *
 * 전략 구조:
 * - 초기 행동: 협력(C)
 * - 행동 로직: 모든 플레이어의 협력/배신 기록을 기반으로 전체 협력 비율을 계산함
 * - 협력 비율이 50% 이상이면 협력(C), 그렇지 않으면 배신(D)
 * - 반응성: 있음 (상대 개별 행동은 고려하지 않지만, 전체 분위기 변화에 반응)
 * - 기억 활용: 있음 (전체 플레이어의 협력/배신 기록을 누적 저장)
 * - 랜덤 요소: 없음 (결정적 조건 기반 판단)
 *
 * 전략 성향 분류:
 * - 협력 전략인가? → X (협력 가능하지만, 분위기에 따라 쉽게 배신으로 전환됨)
 * - 배신 전략인가? → X (배신 성향은 있지만, 조건부에 따라 협력 유지)
 * - 패턴 기반인가? → X (고정된 패턴은 없음)
 * - 상대 반응형인가? → X (특정 상대의 행동에는 반응하지 않음)
 * - 예측 가능성 → 낮음 (전체 분위기에 따라 유동적)
 *
 * 인간 유형 대응:
 * - 개인보다는 집단 분위기를 중시하는 사회적 동조자
 * - 모두가 선하면 나도 선하게 행동하지만, 주변이 이기적으로 변하면 금세 변하는 유형
 * - 책임을 타인과 나누려는 성향이 강하며, 개인 판단보다 군중의 흐름을 따름
 * - 사회 정의에 기여할 수도 있지만, 분위기에 휩쓸려 쉽게 책임을 회피하거나 배신할 수 있음
 * - 일관성은 부족하지만, 집단의 거울 같은 존재로 협력과 배신의 흐름을 반영함
*/

public class CollectiveResponsibility implements Strategy {
    private final Map<Player, Integer> cooperationCount = new HashMap<>(); // 플레이어별 협력 횟수
    private final Map<Player, Integer> totalRounds = new HashMap<>(); // 플레이어별 총 라운드 수

    @Override
    public boolean choose(Player self, Player opponent, List<Boolean> opponentHistory) {
        // 상대 플레이어의 전체 플레이 횟수 증가
        totalRounds.put(opponent, totalRounds.getOrDefault(opponent, 0) + 1);

        // 상대가 협력한 경우 협력 횟수 증가
        if (!opponentHistory.isEmpty() && opponentHistory.get(opponentHistory.size() - 1)) {
            cooperationCount.put(opponent, cooperationCount.getOrDefault(opponent, 0) + 1);
        }

        // 네트워크 전체 협력/배신 비율 계산
        int totalCooperation = 0;
        int totalInteractions = 0;

        for (Player p : totalRounds.keySet()) {
            totalInteractions += totalRounds.get(p);
            totalCooperation += cooperationCount.getOrDefault(p, 0);
        }

        // 데이터가 없으면 기본적으로 협력 (C)
        if (totalInteractions == 0) {
            return true;
        }

        // 협력 비율 계산
        double cooperationRate = (double) totalCooperation / totalInteractions;

        // 배신 비율이 더 높으면 배신 (D), 협력 비율이 같거나 많으면 협력 (C)
        return cooperationRate >= 0.5;
    }

    @Override
    public Strategy cloneStrategy() {
        return new CollectiveResponsibility();
    }
}