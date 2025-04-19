package Strategy;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 전략 이름: GuidingCooperator
 * 전략 개요: 상대를 협력적으로 유도하기 위해 인내하고 점진적으로 반응하는 교육형 전략
 *
 * 전략 구조:
 * - 초기 행동: 첫 5라운드 무조건 협력(C)
 * - 행동 로직:
 *   - 배신 1회: 계속 협력 (실수로 간주)
 *   - 배신 2회: 첫 보복(D)
 *   - 배신 3회 이상: 교대로 협력과 배신을 반복하여 점진적 반응
 * - 반응성: 있음 (상대의 최근 행동과 연속 배신 횟수 기반)
 * - 기억 활용: 제한적 (연속 배신 카운트만 사용)
 * - 랜덤 요소: 없음 (결정적 전략)
 *
 * 전략 성향 분류:
 * - 협력 전략인가? → O (기본 성향은 협력)
 * - 배신 전략인가? → △ (조건부 배신)
 * - 패턴 기반인가? → O (단계적 반응 패턴)
 * - 상대 반응형인가? → O (상대의 배신 여부에 따라 행동)
 * - 예측 가능성 → 보통 (처음은 완전 협력, 이후 반응성 있음)
 *
 * 인간 유형 대응:
 * - 인내심 있는 유도자 또는 점진적 교육자
 * - 상대가 잘못을 저질러도 곧바로 벌하지 않고, 일정한 기회를 준 뒤 행동을 교정하려 함
 * - 실수를 이해하고 받아들이지만, 반복되면 점차 강하게 반응함
 * - 이런 사람은 이상적인 협력 환경을 조성하려 하지만,
 *   악의적이거나 교활한 상대에게는 오히려 이용당할 수 있음
*/

public class GuidingCooperator implements Strategy {
    private final Map<Player, Integer> opponentDefectionStreak = new ConcurrentHashMap<>(); // 연속 배신 횟수 기록
    private final Map<Player, Integer> totalRounds = new ConcurrentHashMap<>(); // 상대별 총 경기 횟수

    @Override
    public boolean choose(Player self, Player opponent, List<Boolean> opponentHistory) {
        totalRounds.computeIfAbsent(opponent, k -> 0);
        opponentDefectionStreak.computeIfAbsent(opponent, k -> 0);

        // 동기화하여 안전한 값 증가
        synchronized (totalRounds) {
            totalRounds.put(opponent, totalRounds.get(opponent) + 1);
        }

        int rounds = totalRounds.get(opponent);

        // 첫 5라운드는 무조건 협력 (상대를 협력 모드로 유도)
        if (rounds <= 5) {
            return true;
        }

        // 안전한 상대 배신 여부 확인
        boolean lastMoveWasDefection = !opponentHistory.isEmpty() && !opponentHistory.get(opponentHistory.size() - 1);

        // 동기화하여 안전한 배신 연속 기록
        synchronized (opponentDefectionStreak) {
            int defectionStreak = opponentDefectionStreak.getOrDefault(opponent, 0);
            if (lastMoveWasDefection) {
                opponentDefectionStreak.put(opponent, defectionStreak + 1);
            } else {
                opponentDefectionStreak.put(opponent, 0); // 협력하면 다시 초기화
            }
        }

        int defectionStreak = opponentDefectionStreak.get(opponent);

        // 유도 전략 로직
        return switch (defectionStreak) {
            case 0 -> true;
            case 1 -> true;
            case 2 -> false;
            default -> rounds % 2 == 0;
        }; // 상대가 협력하거나, 한 번 배신했으면 협력 유지
        // 두 번째 배신까지는 참고 협력 (상대를 테스트)
        // 세 번째 배신부터 반격 시작
        // 이후에는 교대로 보복하여 상대를 협력으로 유도
    }
    
    @Override
    public Strategy cloneStrategy() {
        return new GuidingCooperator();
    }
}