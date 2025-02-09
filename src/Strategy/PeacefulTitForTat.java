package Strategy;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class PeacefulTitForTat implements Strategy {
    private final ConcurrentHashMap<Player, Integer> consecutiveDefects = new ConcurrentHashMap<>(); // 연속 배신 횟수
    private final int FORGIVENESS_THRESHOLD = 5; // 5번 연속 배신 시 화해 시도

    @Override
    public boolean choose(Player self, Player opponent, List<Boolean> opponentHistory) {
        int roundsPlayed = opponentHistory.size();

        // 상대방 히스토리가 없으면 기본적으로 협력 (첫 턴)
        if (roundsPlayed == 0) {
            return true;
        }

        // 상대방의 마지막 행동
        boolean opponentLastMove = opponentHistory.get(roundsPlayed - 1);

        // 연속 배신 횟수 체크
        consecutiveDefects.putIfAbsent(opponent, 0);
        if (!opponentLastMove) { // 상대가 배신했다면
            consecutiveDefects.put(opponent, consecutiveDefects.get(opponent) + 1);
        } else { // 상대가 협력하면 연속 배신 횟수 리셋
            consecutiveDefects.put(opponent, 0);
        }

        // 🔥 만약 내가 5번 연속 배신했다면, 한 번 협력 시도
        if (consecutiveDefects.get(opponent) >= FORGIVENESS_THRESHOLD) {
            consecutiveDefects.put(opponent, 0); // 배신 카운트 리셋
            return true; // 화해의 손길!
        }

        // ✅ 기본적으로 Tit-for-Tat 방식 (상대가 협력하면 협력, 배신하면 배신)
        return opponentLastMove;
    }

    @Override
    public Strategy cloneStrategy() {
        return new PeacefulTitForTat();
    }
}