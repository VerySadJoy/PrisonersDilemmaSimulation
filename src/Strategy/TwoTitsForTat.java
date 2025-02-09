package Strategy;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Two-Tits-for-Tat 전략: 상대가 배신하면 두 번 보복하는 전략
public class TwoTitsForTat implements Strategy {

    private final Map<Player, Integer> opponentPunishmentCount = new HashMap<>(); // 각 상대별 보복 횟수 저장

    @Override
    public boolean choose(Player self, Player opponent, List<Boolean> opponentHistory) {
        // 첫 번째 라운드는 기본적으로 협력(C)
        if (opponentHistory.isEmpty()) {
            return true;
        }

        // 상대의 이전 행동 확인
        boolean lastMove = opponentHistory.get(opponentHistory.size() - 1);

        // 상대가 직전 라운드에서 배신(D)했다면, 두 번 연속으로 배신하도록 설정
        if (!lastMove) {
            opponentPunishmentCount.put(opponent, 2); // 보복 횟수 2회 설정
        }

        // 보복 횟수가 남아 있다면, 배신(D) 실행
        int remainingPunishments = opponentPunishmentCount.getOrDefault(opponent, 0);
        if (remainingPunishments > 0) {
            opponentPunishmentCount.put(opponent, remainingPunishments - 1); // 1 감소
            return false; // 배신 실행
        }

        // 상대의 마지막 행동을 그대로 따라감 (기본 Tit-for-Tat)
        return lastMove;
    }
    
    @Override
    public Strategy cloneStrategy() {
        return new TwoTitsForTat();
    }
}