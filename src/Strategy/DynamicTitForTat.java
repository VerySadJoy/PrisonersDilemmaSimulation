package Strategy;

import java.util.List;
import java.util.Random;
/**
 * 전략 이름: DynamicTitForTat
 * 전략 개요: 상대의 최근 배신 여부와 전체 협력 비율을 함께 고려하여 보복 여부를 확률적으로 결정하는 유연한 보복형 전략
 *
 * 전략 구조:
 * - 초기 행동: 협력(C)
 * - 행동 로직:
 *     - 최근 2번 중 1번이라도 배신했다면 상대의 협력률을 기준으로 확률적 선택
 *         - 협력률이 높으면 협력할 확률이 높아짐
 *         - 협력률이 낮으면 배신할 확률이 높아짐
 *     - 최근 2번 모두 협력이라면 무조건 협력(C)
 * - 반응성: 있음 (단순한 반응이 아니라 통계적 판단을 동반함)
 * - 기억 활용: 있음 (전체 히스토리와 최근 행동 모두 활용)
 * - 랜덤 요소: 있음 (확률 기반 협력/배신 결정)
 *
 * 전략 성향 분류:
 * - 협력 전략인가? → O (기본은 협력 유지, 상대가 악의적일 때만 조심스레 반격)
 * - 배신 전략인가? → X (보복은 있지만 무작정 하지 않음)
 * - 패턴 기반인가? → X (반복적이기보다는 분석적)
 * - 상대 반응형인가? → O (성향 판단 기반 대응)
 * - 예측 가능성 → 낮음 (상대에게는 행동이 확률적으로 보이므로 불확실성 있음)
 *
 * 인간 유형 대응:
 * - 자비로운 응징자, 신중한 분석가
 * - 상대가 나쁜 행동을 하더라도 그 사람이 본래 나쁜 사람인지, 실수인지 구별하려 함
 * - 한 번의 실수로 관계를 끊지 않고, 패턴을 지켜본 후 대응을 결정하는 성숙한 판단력 보유
 * - 협력을 우선시하지만, 계속 배신당하면 결국 대응할 준비도 되어 있음
 * - 인간관계에서 신중하고 너그러운 태도를 유지하지만, 이용당하면 깨달음도 빠름
*/

public class DynamicTitForTat implements Strategy {
    private final Random random = new Random(); // 확률적 선택을 위한 랜덤 객체

    @Override
    public boolean choose(Player self, Player opponent, List<Boolean> opponentHistory) {
        // 첫 번째 라운드는 기본적으로 협력(C)
        if (opponentHistory.isEmpty()) {
            return true;
        }

        int historySize = opponentHistory.size();
        boolean lastMove = opponentHistory.get(historySize - 1); // 상대의 마지막 행동

        // 상대가 최근 2번 중 1번이라도 배신했다면
        if (historySize > 1 && (!lastMove || !opponentHistory.get(historySize - 2))) {
            // 상대의 협력 비율을 계산
            long cooperations = opponentHistory.stream().filter(b -> b).count();
            double cooperationRate = (double) cooperations / historySize;

            // 협력 비율 확률로 협력 선택
            return random.nextDouble() < cooperationRate;
        }

        // 기본적으로 협력 유지
        return true;
    }

    @Override
    public Strategy cloneStrategy() {
        return new DynamicTitForTat();
    }
}