package Strategy;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

public class MutualDestruction implements Strategy {
    private final ConcurrentHashMap<Player, Boolean> suicideMode = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<Player, Integer> betrayCount = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<Player, Integer> totalRounds = new ConcurrentHashMap<>();
    private final Random random = new Random();

    @Override
    public boolean choose(Player self, Player opponent, List<Boolean> opponentHistory) {
        suicideMode.putIfAbsent(opponent, false);
        betrayCount.putIfAbsent(opponent, 0);
        totalRounds.putIfAbsent(opponent, 0);

        int rounds = totalRounds.get(opponent);
        totalRounds.put(opponent, rounds + 1);

        // 첫 10라운드는 랜덤하게 협력/배신을 섞음
        if (rounds < 10) {
            boolean move = random.nextBoolean();
            if (!move) betrayCount.put(opponent, betrayCount.get(opponent) + 1);
            return move;
        }

        // 상대가 40% 이상 배신하면 함께 죽자 모드 활성화
        double betrayalRate = (double) betrayCount.get(opponent) / rounds;
        if (betrayalRate >= 0.4) {
            suicideMode.put(opponent, true);
        }

        // 함께 죽자 모드: 상대 배신하면 나도 배신, 상대 협력하면 50% 확률로 배신
        if (suicideMode.get(opponent)) {
            return opponentHistory.get(opponentHistory.size() - 1) || random.nextBoolean();
        }

        return true; // 평소엔 협력
    }

    @Override
    public Strategy cloneStrategy() {
        return new MutualDestruction();
    }
}