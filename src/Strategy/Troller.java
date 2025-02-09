package Strategy;

import java.util.List;
import java.util.Random;

public class Troller implements Strategy {
    private final Random random = new Random();

    @Override
    public boolean choose(Player self, Player opponent, List<Boolean> opponentHistory) {
        if (opponentHistory.isEmpty()) {
            return random.nextBoolean(); // 첫 라운드는 랜덤
        }

        boolean opponentLastMove = opponentHistory.get(opponentHistory.size() - 1);

        if (opponentLastMove) {
            return random.nextDouble() < 0.9; // 상대가 협력하면 90% 확률로 협력 (호구)
        } else {
            return random.nextDouble() < 0.1; // 상대가 배신하면 90% 확률로 배신 (트롤)
        }
    }

    @Override
    public Strategy cloneStrategy() {
        return new Troller();
    }
}