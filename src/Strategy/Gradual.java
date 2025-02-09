package Strategy;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Gradual 전략: 상대의 배신 횟수에 따라 보복 강도를 점점 증가시키는 전략
public class Gradual implements Strategy {
    private final Map<Player, Integer> pendingDefections = new HashMap<>(); // 각 상대별 보복 횟수 기록

    @Override
    public boolean choose(Player self, Player opponent, List<Boolean> opponentHistory) {
        // 상대방의 기록이 없으면 (첫 라운드라면) 무조건 협력(C)
        if (opponentHistory.isEmpty()) {
            return true;
        }

        // 상대의 마지막 행동이 배신(D)이라면, 보복 예약
        if (!opponentHistory.get(opponentHistory.size() - 1)) {
            pendingDefections.put(opponent, pendingDefections.getOrDefault(opponent, 0) + 1);
        }

        // 상대방에 대한 예약된 보복이 남아 있다면 배신(D) 실행
        if (pendingDefections.getOrDefault(opponent, 0) > 0) {
            pendingDefections.put(opponent, pendingDefections.get(opponent) - 1);
            return false; // 배신
        }

        // 기본적으로 협력 유지 (C)
        return true;
    }
    
    @Override
    public Strategy cloneStrategy() {
        return new Gradual();
    }
}