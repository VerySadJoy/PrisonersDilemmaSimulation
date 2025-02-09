package Strategy;

import java.util.List;

// Tit-for-Tat 전략: 
// 상대가 협력하면 협력(C), 상대가 배신하면 배신(D)하는 전략.
// 즉, 상대의 지난 행동을 그대로 따라 하는 방식.
public class TitForTat implements Strategy {

    @Override
    public boolean choose(Player self, Player opponent, List<Boolean> opponentHistory) {
        // 첫 번째 라운드는 기본적으로 협력(C)
        if (opponentHistory.isEmpty()) {
            return true;
        }

        // 상대의 마지막 행동을 따라감 (협력(C) 또는 배신(D))
        return opponentHistory.get(opponentHistory.size() - 1);
    }
    
    @Override
    public Strategy cloneStrategy() {
        return new TitForTat();
    }
}