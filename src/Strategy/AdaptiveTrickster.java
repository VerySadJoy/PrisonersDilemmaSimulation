package Strategy;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

// Adaptive Trickster 전략: 
// 상대의 협력 비율을 계산하여 확률적으로 협력/배신을 결정하는 전략.
public class AdaptiveTrickster implements Strategy {
    private final Map<Player, Integer> totalRounds = new ConcurrentHashMap<>();
    private final Map<Player, List<Boolean>> opponentHistories = new ConcurrentHashMap<>(); // 안전한 기록 저장
    private final Random random = new Random();

    @Override
    public boolean choose(Player self, Player opponent, List<Boolean> opponentHistory) {
        // 안전한 리스트 관리
        opponentHistories.putIfAbsent(opponent, new CopyOnWriteArrayList<>(opponentHistory));
        List<Boolean> history = opponentHistories.get(opponent);

        // 총 라운드 수 증가 (원자적 업데이트)
        totalRounds.compute(opponent, (k, v) -> (v == null) ? 1 : v + 1);
        int rounds = totalRounds.get(opponent);

        // 초반 5라운드는 무조건 협력하여 상대의 성향을 탐색
        if (rounds <= 5) {
            return true;
        }

        // 상대의 협력 비율 계산 (동기화된 블록)
        int coopCount;
        synchronized (history) {
            coopCount = (int) history.stream().filter(b -> b).count();
        }

        double coopRate = (double) coopCount / rounds;

        // **제어된 랜덤성 적용**
        if (coopRate > 0.8) {
            return random.nextDouble() > 0.2; // 상대가 협력 위주라면 80% 협력, 20% 배신
        } else if (coopRate > 0.5) {
            return random.nextDouble() > 0.4; // 상대가 보복형 전략이면 60% 협력, 40% 배신
        } else {
            return random.nextDouble() > 0.6; // 상대가 배신 위주라면 40% 협력, 60% 배신
        }
    }
    
    @Override
    public Strategy cloneStrategy() {
        return new AdaptiveTrickster();
    }
}