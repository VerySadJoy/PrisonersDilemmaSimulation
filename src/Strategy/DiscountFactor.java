package Strategy;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

// (협력형, 적응형) 상대의 Discount Factor를 기반으로 협력 또는 배신을 결정하는 전략
//
// DiscountFactorStrategy 전략은 상대의 협력 행동을 추적하고,  
// 협력의 지속성을 반영하는 Discount Factor(할인 계수)를 계산하여 판단한다.
//
// 전략의 작동 방식:
// - 첫 번째 라운드는 무조건 협력(C)하여 상대가 협력할 기회를 줌.
// - 이후 각 상대별 Discount Factor를 계산하여 협력 확률을 평가.
// - Discount Factor가 75% 이상이면 협력, 아니면 배신.
//
// 장점:
// - 상대가 협력적이라면 지속적으로 협력할 수 있음.
// - 상대가 배신적인 경우 Discount Factor가 감소하여 자연스럽게 배신 전략으로 전환됨.
// - 특정 전략(Tit-for-Tat, Pavlov)과 만나면 비교적 안정적으로 상호 작용 가능.
//
// 단점:
// - Discount Factor 계산에 시간이 걸려 초반 몇 라운드는 부정확할 수 있음.
// - Forgiving Tit-for-Tat 같은 전략에게는 완벽히 협력적이지 않아 오해를 살 수도 있음.
//

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