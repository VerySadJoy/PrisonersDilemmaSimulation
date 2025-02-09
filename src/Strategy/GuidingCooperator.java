package Strategy;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class GuidingCooperator implements Strategy {
    private final Map<Player, Integer> opponentDefectionStreak = new ConcurrentHashMap<>(); // 연속 배신 횟수 기록
    private final Map<Player, Integer> totalRounds = new ConcurrentHashMap<>(); // 상대별 총 경기 횟수

    @Override
    public boolean choose(Player self, Player opponent, List<Boolean> opponentHistory) {
        totalRounds.computeIfAbsent(opponent, k -> 0);
        opponentDefectionStreak.computeIfAbsent(opponent, k -> 0);

        // ✅ 동기화하여 안전한 값 증가
        synchronized (totalRounds) {
            totalRounds.put(opponent, totalRounds.get(opponent) + 1);
        }

        int rounds = totalRounds.get(opponent);

        // 첫 5라운드는 무조건 협력 (상대를 협력 모드로 유도)
        if (rounds <= 5) {
            return true;
        }

        // ✅ 안전한 상대 배신 여부 확인
        boolean lastMoveWasDefection = !opponentHistory.isEmpty() && !opponentHistory.get(opponentHistory.size() - 1);

        // ✅ 동기화하여 안전한 배신 연속 기록
        synchronized (opponentDefectionStreak) {
            int defectionStreak = opponentDefectionStreak.getOrDefault(opponent, 0);
            if (lastMoveWasDefection) {
                opponentDefectionStreak.put(opponent, defectionStreak + 1);
            } else {
                opponentDefectionStreak.put(opponent, 0); // 협력하면 다시 초기화
            }
        }

        int defectionStreak = opponentDefectionStreak.get(opponent);

        // **유도 전략 로직**
        return switch (defectionStreak) {
            case 0 -> true;
            case 1 -> true;
            case 2 -> false;
            default -> rounds % 2 == 0;
        }; // 상대가 협력하거나, 한 번 배신했으면 협력 유지
        // 두 번째 배신까지는 참고 협력 (상대를 테스트)
        // 세 번째 배신부터 반격 시작
        // 이후에는 교대로 보복하여 상대를 협력으로 유도
    }
    
    @Override
    public Strategy cloneStrategy() {
        return new GuidingCooperator();
    }
}