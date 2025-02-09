package Strategy;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShadowDefect implements Strategy {
    private final Map<Player, Integer> opponentCooperationCount = new HashMap<>(); // 협력 횟수 기록
    private final Map<Player, Integer> totalRounds = new HashMap<>(); // 총 경기 수 기록

    @Override
    public boolean choose(Player self, Player opponent, List<Boolean> opponentHistory) {
        totalRounds.put(opponent, totalRounds.getOrDefault(opponent, 0) + 1);
        int rounds = totalRounds.get(opponent);
        
        // 첫 5라운드는 무조건 협력(C) -> 상대가 방심하게 만들기
        if (rounds <= 5) {
            return true;
        }

        // 상대의 협력 비율 계산
        int cooperationCount = opponentCooperationCount.getOrDefault(opponent, 0);
        double cooperationRate = (double) cooperationCount / rounds;

        // 상대가 협력했으면 협력 카운트 증가
        if (!opponentHistory.isEmpty() && opponentHistory.get(opponentHistory.size() - 1)) {
            opponentCooperationCount.put(opponent, cooperationCount + 1);
        }

        // **배신 타이밍 결정**
        if (cooperationRate > 0.8 && rounds % 4 == 0) {
            // 상대가 80% 이상 협력하는 경우, 4라운드마다 배신 한 번 삽입
            return false;
        }
        else if (cooperationRate < 0.4) {
            // 상대가 원래 배신을 많이 하는 경우 → 맞배신
            return false;
        }
        else return rounds % 10 != 0; // 주기적으로 한번씩 배신 (상대가 예상하기 어렵도록)
        // 기본적으로 협력 유지 (상대가 보복형이면 협력)
        
    }

    
    @Override
    public Strategy cloneStrategy() {
        return new ShadowDefect();
    }
}