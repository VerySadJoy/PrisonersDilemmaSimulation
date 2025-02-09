package Strategy;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

public class Grasshopper implements Strategy {
    private final Map<Player, Integer> lastRoundScores = new ConcurrentHashMap<>();
    private final Random random = new Random();

    @Override
    public boolean choose(Player self, Player opponent, List<Boolean> opponentHistory) {
        if (!lastRoundScores.containsKey(opponent)) {
            return random.nextBoolean(); // 첫 라운드는 랜덤
        }

        int lastScore = lastRoundScores.get(opponent);

        if (lastScore == 6) { // (C, C) → 3 + 3
            return random.nextBoolean(); // 랜덤하게 협력 or 배신
        } else if (lastScore == 5) { // (C, D) or (D, C)
            return lastScore == 0 ? false : true; // (C, D) → 협력 / (D, C) → 배신
        } else if (lastScore == 2) { // (D, D) → 1 + 1
            return false; // 배신
        }

        return true; // 기본적으로 협력 (이론상 도달할 수 없는 경우지만 대비)
    }

    // ✅ 상대 플레이어별 점수를 저장 (게임이 끝날 때 호출해야 함)
    public void updateScore(Player opponent, int score) {
        lastRoundScores.put(opponent, score);
    }

    @Override
    public Strategy cloneStrategy() {
        return new Grasshopper();
    }
}