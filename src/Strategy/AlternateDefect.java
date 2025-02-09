package Strategy;

import java.util.List;

//  D -> C -> D -> C 만을 반복하는, 남이 뭐라고 하던 자기 길만 가는 존재
public class AlternateDefect implements Strategy {
    @Override
    public boolean choose(Player self, Player opponent, List<Boolean> opponentHistory) {
        // 첫 번째 라운드는 무조건 협력 (C)
        if (opponentHistory.isEmpty()) {
            return false;
        }
        // 짝수 번째 라운드 → 협력 (C), 홀수 번째 라운드 → 배신 (D)
        return opponentHistory.size() % 2 != 0;
    }
    
    @Override
    public Strategy cloneStrategy() {
        return new AlternateDefect();
    }
}