package Strategy;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

// Predator 전략: 상대의 협력 비율이 80% 이상이면 계속 배신하는 전략.
public class Predator implements Strategy {
    private final Map<Player, List<Boolean>> opponentHistories = new ConcurrentHashMap<>(); // 안전한 기록 저장
    private final Map<Player, Boolean> isCooperatorMap = new ConcurrentHashMap<>(); // 협력가 여부 저장

    @Override
    public boolean choose(Player self, Player opponent, List<Boolean> opponentHistory) {
        // 안전한 리스트 관리
        opponentHistories.putIfAbsent(opponent, new CopyOnWriteArrayList<>(opponentHistory));
        List<Boolean> history = opponentHistories.get(opponent);

        // 첫 번째 턴은 무조건 배신(D)
        if (history.isEmpty()) {
            return false;
        }

        // 동기화된 블록에서 안전하게 접근
        long betrayals;
        synchronized (history) {
            betrayals = history.stream().filter(b -> !b).count();
        }

        boolean isCooperator = betrayals < history.size() * 0.2; // 80% 이상 협력하면 순수 협력가로 간주
        isCooperatorMap.put(opponent, isCooperator);

        return !isCooperatorMap.get(opponent); // 협력가면 계속 배신, 보복형이면 협력
    }
    
    @Override
    public Strategy cloneStrategy() {
        return new Predator();
    }
}