package Strategy;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

// (적응형, 협력형) 네트워크 노이즈에 강한 개선된 Tit-for-Tat 전략
//
// EnhancedTitForTat (ETFT) 전략은 기본 Tit-for-Tat 전략을 개선하여,
// 네트워크 오류, 랜덤 실패 등으로 인해 협력이 무너지는 것을 방지함.
//
// 전략의 작동 방식:
// - 기본적으로 Tit-for-Tat처럼 상대의 마지막 행동을 따라감
// - 단, 협력 비율이 감소했을 때 "즉시 보복"하지 않고 점진적으로 협력 수준을 복구
// - 특정한 비율 incrementRate로 협력 비율을 증가시킴
//
// 장점:
// - 네트워크 노이즈에도 안정적인 협력 가능
// - 상대가 실수로 배신해도 점진적으로 협력으로 복구
// - 장기적으로 협력을 유도할 수 있음
//
// 단점:
// - 배신자를 너무 쉽게 용서할 수도 있음 (협력 남용 가능성)
// - 악의적인 적에게 일부 취약할 가능성 있음

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
