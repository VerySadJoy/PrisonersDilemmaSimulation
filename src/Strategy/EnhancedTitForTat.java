package Strategy;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * 전략 이름: EnhancedTitForTat
 * 전략 개요: 상대의 마지막 행동을 따르되, 배신 시에도 점진적으로 협력 수준을 회복하는 유연한 대응형 전략
 *
 * 전략 구조:
 * - 초기 행동: 협력(C)
 * - 행동 로직:
 *     - 상대가 협력(C)했으면 협력 비율을 1.0으로 회복하고 무조건 협력
 *     - 상대가 배신(D)했더라도 즉시 배신하지 않고, 협력 비율을 점진적으로 감소시킴
 *     - 현재 협력 비율에 따라 확률적으로 협력 결정
 *     - 협력 비율은 0.2씩 감소하고, 협력 시 즉시 1.0으로 회복
 * - 반응성: 있음 (상대의 직전 행동 반영)
 * - 기억 활용: 있음 (상대별 협력 비율 저장)
 * - 랜덤 요소: 있음 (협력 확률 기반 선택)
 *
 * 전략 성향 분류:
 * - 협력 전략인가? → O (가능한 협력을 유지하려 함)
 * - 배신 전략인가? → X (단호한 배신은 없음)
 * - 패턴 기반인가? → X (비결정적 확률 기반)
 * - 상대 반응형인가? → O (상대의 행동에 따라 협력률 조절)
 * - 예측 가능성 → 낮음 (협력률에 기반한 확률적 결정으로 인해 외부에서 예측 어려움)
 *
 * 인간 유형 대응:
 * - 실수에도 관대한 신중한 중재자
 * - 처음부터 상대를 믿지만, 상대가 신뢰를 저버리면 천천히 경계심을 키워감
 * - 단번에 화를 내지 않고, 몇 번은 이해해주는 사람
 * - 관계의 회복 가능성을 항상 열어두고, 상대가 협력으로 돌아오면 빠르게 용서하고 협력 복원
 * - 사회적 인간관계에서도 완벽주의보다 신뢰 유지와 회복에 집중하는 사람
*/

public class EnhancedTitForTat implements Strategy {
    private final Random random = new Random();

    // 상대별로 협력 비율 저장 (0.0 ~ 1.0)
    private final Map<Player, Double> cooperationRate = new HashMap<>();

    // 협력 비율 증가 속도 (Proportion Increment)
    private final double incrementRate = 0.2; // 협력 회복 속도 (20%)

    @Override
    public boolean choose(Player self, Player opponent, List<Boolean> opponentHistory) {
        int rounds = opponentHistory.size();

        // 첫 번째 라운드는 무조건 협력 (C)
        if (rounds == 0) {
            cooperationRate.put(opponent, 1.0); // 초기 협력 비율 = 1.0 (완전 협력)
            return true;
        }

        // 상대의 마지막 행동 가져오기
        boolean lastOpponentMove = opponentHistory.get(rounds - 1);

        // 현재 협력 비율 가져오기 (기본값 = 1.0)
        double currentCooperation = cooperationRate.getOrDefault(opponent, 1.0);

        // 상대가 협력했으면, 협력 유지
        if (lastOpponentMove) {
            cooperationRate.put(opponent, 1.0); // 다시 완전 협력으로 회복
            return true;
        }

        // 상대가 배신한 경우 → 즉시 배신하지 않고, 점진적으로 협력 수준 조절
        double newCooperationRate = Math.max(0.0, currentCooperation - incrementRate);
        cooperationRate.put(opponent, newCooperationRate);

        // 현재 협력 비율에 따라 행동 결정 (확률적으로 협력 수행)
        return random.nextDouble() < newCooperationRate;
    }

    @Override
    public Strategy cloneStrategy() {
        return new EnhancedTitForTat();
    }
}
