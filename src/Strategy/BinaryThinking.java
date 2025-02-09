package Strategy;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

// Downing 전략: 상대의 협력 비율이 50% 이하면 무조건 배신(D), 
// 50%를 초과하면 무조건 협력(C)하는 전략
public class BinaryThinking implements Strategy {
    private final Map<Player, List<Boolean>> opponentHistories = new ConcurrentHashMap<>(); // 안전한 기록 저장

    @Override
    public boolean choose(Player self, Player opponent, List<Boolean> opponentHistory) {
        // 안전한 리스트 관리
        opponentHistories.putIfAbsent(opponent, new CopyOnWriteArrayList<>(opponentHistory));
        List<Boolean> history = opponentHistories.get(opponent);

        // 상대방의 행동 기록이 비어 있다면 (즉, 첫 라운드라면) 무조건 배신(D) 선택
        if (history.isEmpty()) {
            return false;
        }

        // 동기화된 블록에서 안전하게 접근
        double cooperationRate;
        synchronized (history) {
            long cooperationCount = history.stream().filter(b -> b).count();
            cooperationRate = (double) cooperationCount / history.size();
        }

        // 협력 비율이 50% 이하라면 무조건 배신(D)
        // 협력 비율이 50%를 초과하면 무조건 협력(C)
        return cooperationRate > 0.5;
    }
    
    @Override
    public Strategy cloneStrategy() {
        return new BinaryThinking();
    }
}