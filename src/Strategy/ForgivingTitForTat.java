package Strategy;

import java.util.List;

// Forgiving Tit-for-Tat 전략: 상대가 한 번 배신하면 용서하지만,
// 두 번 연속 배신하면 보복(배신)하는 전략
public class ForgivingTitForTat implements Strategy {

    @Override
    public boolean choose(Player self, Player opponent, List<Boolean> opponentHistory) {
        // 첫 번째 라운드는 기본적으로 협력(C)
        if (opponentHistory.isEmpty()) {
            return true;
        }

        int size = opponentHistory.size();

        // 상대가 단 한 번 배신했을 경우 → 용서하고 협력
        if (size == 1) {
            return true;
        }
        // 상대가 **직전 두 번 연속 배신(D, D)** 했다면 → 보복 (배신)
        // 기본적으로 협력 유지

        return !(!opponentHistory.get(size - 1) && !opponentHistory.get(size - 2));
    }
    
    @Override
    public Strategy cloneStrategy() {
        return new ForgivingTitForTat();
    }
}