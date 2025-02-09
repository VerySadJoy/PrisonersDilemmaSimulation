package Strategy;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

// ScammerTester 전략: 처음에는 배신(D) 후, 상대의 반응을 보고
// 협력/배신 전략을 다르게 조정하는 간보는 사기꾼 스타일 전략.
public class ScammerTester implements Strategy {
    private final Map<Player, Boolean> switchedToTitForTat = new HashMap<>(); // 상대별 팃포탯 전환 여부
    private final Map<Player, Boolean> alternatingMode = new HashMap<>(); // 상대별 협력/배신 반복 모드
    private final Map<Player, Integer> roundCount = new HashMap<>(); // 상대별 라운드 번호

    @Override
    public boolean choose(Player self, Player opponent, List<Boolean> opponentHistory) {
        // 상대방의 라운드 진행 카운트 증가
        roundCount.put(opponent, roundCount.getOrDefault(opponent, 0) + 1);
        int currentRound = roundCount.get(opponent);

        // 상대의 기록이 없으면 (즉, 첫 번째 라운드라면) 무조건 배신(D)
        if (opponentHistory.isEmpty()) {
            return false;
        }

        // 상대가 바로 보복(배신)했다면 → 팃포탯 모드로 전환
        if (currentRound == 2 && !opponentHistory.get(opponentHistory.size() - 1)) {
            switchedToTitForTat.put(opponent, true);
        }

        // 팃포탯 모드면 상대의 마지막 행동을 따라감
        if (switchedToTitForTat.getOrDefault(opponent, false)) {
            return opponentHistory.get(opponentHistory.size() - 1);
        }

        // 상대가 첫 배신에도 보복하지 않고 협력했다면 → 협력/배신 반복 모드로 전환
        if (currentRound == 2 && opponentHistory.get(opponentHistory.size() - 1)) {
            alternatingMode.put(opponent, true);
        }

        // 협력/배신 반복 모드
        if (alternatingMode.getOrDefault(opponent, false)) {
            return currentRound % 2 == 0; // 짝수 라운드에서는 협력(C), 홀수 라운드에서는 배신(D)
        }

        // 기본적으로 배신 유지 (이 로직에 도달할 일은 없음)
        return false;
    }
    
    @Override
    public Strategy cloneStrategy() {
        return new ScammerTester();
    }
}