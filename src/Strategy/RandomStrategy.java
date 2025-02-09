package Strategy;

import java.util.List;
import java.util.Random;

// RandomStrategy 전략: 상대의 행동과 관계없이 
// 50% 확률로 협력(C) 또는 배신(D)을 무작위로 결정하는 전략.
public class RandomStrategy implements Strategy {
    private final Random random = new Random(); // 랜덤 객체 (무작위 결정)

    @Override
    public boolean choose(Player self, Player opponent, List<Boolean> opponentHistory) {
        // 무조건 50% 확률로 협력(C) 또는 배신(D)
        return random.nextBoolean();
    }

    
    @Override
    public Strategy cloneStrategy() {
        return new RandomStrategy();
    }
}