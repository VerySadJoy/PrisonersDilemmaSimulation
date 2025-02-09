package Strategy;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class RobinHood implements Strategy {
    private final ConcurrentHashMap<Player, Integer> scores = new ConcurrentHashMap<>();

    @Override
    public boolean choose(Player self, Player opponent, List<Boolean> opponentHistory) {
        int myScore = self.getScore();
        int opponentScore = opponent.getScore();

        return myScore >= opponentScore;  // 내가 높거나 같으면 협력, 내가 낮으면 배신
    }

    @Override
    public Strategy cloneStrategy() {
        return new RobinHood();
    }
}