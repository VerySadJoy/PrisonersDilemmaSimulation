package Strategy;

import java.util.List;
import java.util.Random;

// Greedy Tit-for-Tat 전략: 기본적으로 팃포탯과 동일하게 작동하지만, 
// 협력을 해야 할 때 10% 확률로 배신하는 전략
public class GreedyTitForTat implements Strategy {
    private final Random random = new Random(); // 배신할 확률 계산을 위한 랜덤 객체

    @Override
    public boolean choose(Player self, Player opponent, List<Boolean> opponentHistory) {
        // 첫 번째 라운드는 기본적으로 협력(C)
        if (opponentHistory.isEmpty()) {
            return true;
        }

        // 상대의 마지막 행동을 따라감 (기본적으로 팃포탯 방식)
        boolean shouldCooperate = opponentHistory.get(opponentHistory.size() - 1);

        // 협력을 하려는 상황이라면 10% 확률로 배신(D)
        if (shouldCooperate && random.nextDouble() < 0.1) {
            return false;
        }

        // 기본적으로 상대의 마지막 행동을 따라감
        return shouldCooperate;
    }
    
    @Override
    public Strategy cloneStrategy() {
        return new GreedyTitForTat();
    }
}