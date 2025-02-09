package Strategy;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Shepherd implements Strategy {
    private final Map<Player, Integer> betrayalCount = new ConcurrentHashMap<>(); // 상대가 배신했을 때 복수 카운트
    
    @Override
    public boolean choose(Player self, Player opponent, List<Boolean> opponentHistory) {
        // 만약 상대와 처음 만나는 거라면 기본적으로 협력
        if (opponentHistory.isEmpty()) {
            return true;
        }
        
        // 상대의 마지막 행동 가져오기
        boolean opponentLastMove = opponentHistory.get(opponentHistory.size() - 1);
        
        // 상대가 배신했다면 복수 카운트를 2 증가
        if (!opponentLastMove) {
            betrayalCount.put(opponent, betrayalCount.getOrDefault(opponent, 0) + 2);
        }
        
        // 현재 복수 중이라면 배신 (복수 카운트를 줄여가면서)
        if (betrayalCount.getOrDefault(opponent, 0) > 0) {
            betrayalCount.put(opponent, betrayalCount.get(opponent) - 1);
            return false; // 배신
        }
        
        // 기본적으로 협력
        return true;
    }

    @Override
    public Strategy cloneStrategy() {
        return new Shepherd();
    }
}