package Strategy;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

// 상대를 협력자, 보복자, 배신자, 예측 불가능으로 구분하여 대응
public class HandOfGod implements Strategy {
    private final Map<Player, Integer> opponentCooperationCount = new ConcurrentHashMap<>(); // 협력 횟수 기록
    private final Map<Player, Integer> totalRounds = new ConcurrentHashMap<>(); // 총 경기 수 기록
    private final Map<Player, Boolean> exploitMode = new ConcurrentHashMap<>(); // 착취 모드 여부

    @Override
    public boolean choose(Player self, Player opponent, List<Boolean> opponentHistory) {
        // ✅ 안전한 값 초기화
        totalRounds.computeIfAbsent(opponent, k -> 0);
        opponentCooperationCount.computeIfAbsent(opponent, k -> 0);
        exploitMode.computeIfAbsent(opponent, k -> false);

        // ✅ 동기화하여 라운드 수 증가
        synchronized (totalRounds) {
            totalRounds.put(opponent, totalRounds.get(opponent) + 1);
        }

        int rounds = totalRounds.get(opponent);

        // 첫 10라운드는 무조건 협력(C)
        if (rounds <= 10) {
            return true;
        }

        // ✅ 안전한 협력 횟수 증가
        synchronized (opponentCooperationCount) {
            if (!opponentHistory.isEmpty() && opponentHistory.get(opponentHistory.size() - 1)) {
                opponentCooperationCount.put(opponent, opponentCooperationCount.get(opponent) + 1);
            }
        }

        int cooperationCount = opponentCooperationCount.get(opponent);
        double cooperationRate = (double) cooperationCount / rounds;

        // ✅ 착취 모드 여부 안전하게 가져오기
        boolean isExploiting = exploitMode.get(opponent);

        // 전략 판별
        if (cooperationRate > 0.9) {
            // 순수 협력가 → 점진적으로 착취
            if (!isExploiting) {
                synchronized (exploitMode) {
                    exploitMode.put(opponent, true);
                }
                return false; // 첫 배신 시도
            } else {
                return rounds % 3 != 0; // 가끔 배신
            }
        } else if (cooperationRate > 0.6) {
            // 보복형 전략 (T4T, Gradual 등) → 협력 유지
            return true;
        } else if (cooperationRate < 0.3) {
            // 배신자 (Always Defect, Greedy T4T) → 배신으로 맞대응
            return false;
        } else {
            // 애매한 경우 → 따라하기
            return !opponentHistory.get(opponentHistory.size() - 1);
        }
    }

    
    @Override
    public Strategy cloneStrategy() {
        return new HandOfGod();
    }
}