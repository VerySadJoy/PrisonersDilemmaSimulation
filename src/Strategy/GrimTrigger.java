package Strategy;

import java.util.List;

// Grim Trigger 전략: 처음에는 협력(C)하지만, 
// 상대가 단 한 번이라도 배신(D)하면 이후로 무조건 배신하는 전략.
public class GrimTrigger implements Strategy {

    @Override
    public boolean choose(Player self, Player opponent, List<Boolean> opponentHistory) {
        // 첫 번째 라운드는 기본적으로 협력(C)
        if (opponentHistory.isEmpty()) {
            return true;
        }

        // 상대가 한 번이라도 배신(D)한 적이 있으면 이후로 계속 배신(D)
        return !opponentHistory.contains(false);
    }
    
    @Override
    public Strategy cloneStrategy() {
        return new GrimTrigger();
    }
}