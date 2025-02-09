package Strategy;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class Saint implements Strategy {
    private final ConcurrentHashMap<Player, Integer> betrayals = new ConcurrentHashMap<>();
    private final int TOLERANCE = 10; // 배신을 10번까지는 참음

    @Override
    public boolean choose(Player self, Player opponent, List<Boolean> opponentHistory) {
        betrayals.putIfAbsent(opponent, 0);

        if (opponentHistory.isEmpty()) {
            return true; // 처음엔 무조건 협력
        }

        if (!opponentHistory.get(opponentHistory.size() - 1)) {
            betrayals.put(opponent, betrayals.get(opponent) + 1);
        }

        // 상대가 10번 이상 배신했으면 Tit-for-Tat 모드 돌입
        if (betrayals.get(opponent) >= TOLERANCE) {
            return opponentHistory.get(opponentHistory.size() - 1);
        }

        return true; // 평소에는 무조건 협력
    }

    @Override
    public Strategy cloneStrategy() {
        return new Saint();
    }
}