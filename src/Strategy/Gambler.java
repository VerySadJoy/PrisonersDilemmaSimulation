package Strategy;
import java.util.List;

public class Gambler implements Strategy {
    private int roundsPlayed = 0;

    @Override
    public boolean choose(Player self, Player opponent, List<Boolean> opponentHistory) {
        roundsPlayed++;
        int averageScore = (roundsPlayed > 0) ? (self.getScore() / roundsPlayed) : 0;

        return averageScore > 2.25;
    }

    @Override
    public Gambler cloneStrategy() {
        return new Gambler();
    }
}