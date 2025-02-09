package Strategy;

import java.util.List;

// AlwaysCooperate 전략: 상대의 행동과 관계없이 항상 협력(C)하는 전략
public class AlwaysCooperate implements Strategy {
    @Override
    public boolean choose(Player self, Player opponent, List<Boolean> opponentHistory) {
        // 항상 협력 (C)을 선택함
        return true;
    }
    
    @Override
    public Strategy cloneStrategy() {
        return new AlwaysCooperate();
    }
}