package Strategy;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

// Probabilistic Tit-for-Tat 전략: 
// 상대의 협력 비율을 계산하여 해당 확률만큼 협력(C)하고, 나머지는 배신(D)하는 전략.
public class ProbabilisticTitForTat implements Strategy {
    private final Random random = new Random(); // 확률 기반 행동 결정을 위한 랜덤 객체
    private final Map<Player, List<Boolean>> opponentHistories = new ConcurrentHashMap<>(); // 안전한 기록 저장

    @Override
    public boolean choose(Player self, Player opponent, List<Boolean> opponentHistory) {
        // 리스트를 안전하게 저장하기 위해 `CopyOnWriteArrayList` 사용
        opponentHistories.putIfAbsent(opponent, new CopyOnWriteArrayList<>(opponentHistory));
        List<Boolean> history = opponentHistories.get(opponent);

        // 첫 번째 라운드는 기본적으로 협력(C)
        if (history.isEmpty()) {
            return true;
        }

        // 동기화된 블록에서 안전하게 접근
        double cooperationRate;
        synchronized (history) {
            long cooperationCount = history.stream().filter(b -> b).count();
            cooperationRate = (double) cooperationCount / history.size();
        }

        // 상대의 협력 비율만큼 확률적으로 협력(C)
        return random.nextDouble() < cooperationRate;
    }
    
    @Override
    public Strategy cloneStrategy() {
        return new ProbabilisticTitForTat();
    }
}