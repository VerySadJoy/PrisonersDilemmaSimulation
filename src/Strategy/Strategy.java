package Strategy;
import java.util.List;

public interface Strategy {
    boolean choose(Player self, Player opponent, List<Boolean> opponentHistory); // 상대별로 다른 행동 가능
    Strategy cloneStrategy();
}
