package Strategy;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class Debt implements Strategy {
    private final ConcurrentHashMap<Player, Integer> debt = new ConcurrentHashMap<>(); // 상대가 쌓은 배신 빚

    @Override
    public boolean choose(Player self, Player opponent, List<Boolean> opponentHistory) {
        debt.putIfAbsent(opponent, 0);

        if (opponentHistory.isEmpty()) {
            return true; // 첫 턴은 협력
        }

        // 상대가 이전 턴에 배신했으면 빚을 추가
        if (!opponentHistory.get(opponentHistory.size() - 1)) {
            debt.put(opponent, debt.get(opponent) + 1);
        } else {
            // 상대가 협력하면 빚을 하나 갚아줌
            debt.put(opponent, Math.max(0, debt.get(opponent) - 1));
        }

        // 빚이 남아 있으면 배신으로 갚음
        return debt.get(opponent) == 0;
    }

    @Override
    public Strategy cloneStrategy() {
        return new Debt();
    }
}