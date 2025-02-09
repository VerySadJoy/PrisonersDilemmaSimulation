package Strategy;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class GlassMind implements Strategy {
    private final ConcurrentHashMap<Player, Integer> betrayalCount = new ConcurrentHashMap<>(); // 상대에게 배신당한 횟수
    private final ConcurrentHashMap<Player, Integer> trustRecovery = new ConcurrentHashMap<>(); // 신뢰 회복 상태 (양수: 협력, 음수: 배신)
    private final int RECOVERY_PERIOD = 5; // 배신 후 5라운드 동안은 배신 모드
    private final int INITIAL_COOP_PERCENTAGE = 80; // 초반 10라운드 동안 협력 확률

    @Override
    public boolean choose(Player self, Player opponent, List<Boolean> opponentHistory) {
        int roundsPlayed = opponentHistory.size();

        // 🔹 초반 10라운드 동안 기본적으로 협력 (80%) 하지만 가끔 배신 (20%)
        if (roundsPlayed < 10) {
            return Math.random() < (INITIAL_COOP_PERCENTAGE / 100.0);
        }

        // 🔥 상대에게 배신당한 횟수 확인
        betrayalCount.putIfAbsent(opponent, 0);
        trustRecovery.putIfAbsent(opponent, 0);

        // 🔥 이전 라운드에서 상대방이 배신했는지 확인
        boolean opponentLastMove = opponentHistory.get(roundsPlayed - 1);
        if (!opponentLastMove) { // 상대가 배신한 경우
            betrayalCount.put(opponent, betrayalCount.get(opponent) + 1);
            trustRecovery.put(opponent, -RECOVERY_PERIOD); // 배신 모드 ON
        }

        // 🧊 배신 모드 (배신당한 후 5라운드 동안은 무조건 배신)
        if (trustRecovery.get(opponent) < 0) {
            trustRecovery.put(opponent, trustRecovery.get(opponent) + 1); // 회복 모드 증가
            return false; // 배신 지속
        }

        // 🥺 신뢰 회복 모드 (상대가 협력하면 다시 협력)
        if (opponentLastMove) {
            trustRecovery.put(opponent, RECOVERY_PERIOD); // 신뢰 회복 모드 ON
        }

        // 🔄 신뢰 회복이 끝난 상태면 협력
        return true;
    }

    @Override
    public Strategy cloneStrategy() {
        return new GlassMind();
    }
}