package Strategy;

import java.util.List;

// AlwaysDefect 전략: 상대의 행동과 관계없이 항상 배신(D)하는 전략
public class AlwaysDefect implements Strategy {

    @Override
    public boolean choose(Player self, Player opponent, List<Boolean> opponentHistory) {
        // 항상 배신 (D)을 선택함
        return false;
    }
    
    @Override
    public Strategy cloneStrategy() {
        return new AlwaysDefect();
    }
}