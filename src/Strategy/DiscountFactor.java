package Strategy;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 전략 이름: DiscountFactor
 * 전략 개요: 상대의 협력 행동을 시간 가중 평균(Discount Factor)으로 계산하여 협력할지 결정하는 수학 기반 적응형 전략
 *
 * 전략 구조:
 * - 초기 행동: 협력(C)
 * - 행동 로직:
 *     - 매 턴마다 상대의 협력 여부를 할인율(0.9)을 적용해 누적 계산
 *     - 최근 행동은 더 큰 영향을 미치고, 오래된 행동은 점점 영향력이 감소
 *     - Discount Factor가 0.75 이상이면 협력(C), 그렇지 않으면 배신(D)
 * - 반응성: 있음 (상대의 협력 경향을 반영)
 * - 기억 활용: 있음 (이전 협력의 축적된 영향 계산)
 * - 랜덤 요소: 없음 (완전 결정적)
 *
 * 전략 성향 분류:
 * - 협력 전략인가? → O (협력 중심이며, 기준 충족 시 언제나 협력)
 * - 배신 전략인가? → X (상대가 협력적이지 않으면 협력하지 않음)
 * - 패턴 기반인가? → X (단순 주기 아님)
 * - 상대 반응형인가? → O (상대의 행동 빈도에 따라 조절)
 * - 예측 가능성 → 중간 (공식은 있지만 외부에서 예측은 어려움)
 *
 * 인간 유형 대응:
 * - 감정보다는 확률을 믿는 신중한 관찰자
 * - 상대의 협력도에 따라 점진적으로 신뢰를 쌓아가며, 일정 기준 이상이면 전폭적인 협력자로 전환
 * - 한 번의 협력이나 배신으로 판단하지 않고, 전체적인 경향을 통계적으로 파악하는 성향
 * - 사회적으로는 쉽게 친해지지 않지만, 꾸준히 잘해주면 결국 믿고 함께할 수 있는 사람
 * - 단, 상대가 일관되지 않으면 언제든지 신뢰를 철회할 준비가 되어 있는 타입
*/


public class DiscountFactor implements Strategy {
    // 각 상대별 Discount Factor 저장
    private final Map<Player, Double> discountFactors = new HashMap<>();

    // 각 상대별 협력 횟수 및 총 라운드 횟수 저장
    private final Map<Player, Integer> cooperationCount = new HashMap<>();
    private final Map<Player, Integer> totalRounds = new HashMap<>();

    // Discount Factor의 감쇠율 (이전 협력 행동의 중요도 조절)
    private final double discountRate = 0.9; // 0.9로 설정하여 최근 행동이 더 큰 영향을 미침

    @Override
    public boolean choose(Player self, Player opponent, List<Boolean> opponentHistory) {
        int rounds = opponentHistory.size();

        // 첫 번째 라운드는 무조건 협력
        if (rounds == 0) {
            discountFactors.put(opponent, 1.0); // 초반에는 최대 협력 가정
            cooperationCount.put(opponent, 0);
            totalRounds.put(opponent, 0);
            return true;
        }

        // 상대의 총 플레이 횟수 증가
        totalRounds.put(opponent, totalRounds.getOrDefault(opponent, 0) + 1);

        // 상대가 협력했으면 협력 횟수 증가
        if (opponentHistory.get(rounds - 1)) {
            cooperationCount.put(opponent, cooperationCount.getOrDefault(opponent, 0) + 1);
        }

        // Discount Factor 계산 (할인율 적용)
        double prevDiscountFactor = discountFactors.getOrDefault(opponent, 1.0);
        double newDiscountFactor = (discountRate * prevDiscountFactor) + (1 - discountRate) * (opponentHistory.get(rounds - 1) ? 1.0 : 0.0);
        discountFactors.put(opponent, newDiscountFactor);

        // 75% 이상이면 협력, 아니면 배신
        return newDiscountFactor >= 0.75;
    }

    @Override
    public Strategy cloneStrategy() {
        return new DiscountFactor();
    }
}