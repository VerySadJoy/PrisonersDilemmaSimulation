package Strategy;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

// Tranquilizer 전략: 협력하면서 신뢰를 쌓다가 갑자기 배신하는 전략.
// 상대가 협력적인 태도를 유지하면 배신 횟수를 점점 늘려서 착취하지만,
// 배신 비율이 전체 게임의 25%를 넘지 않도록 조절함.
public class Tranquilizer implements Strategy {
    private final Map<Player, Boolean> betrayalPhase = new ConcurrentHashMap<>(); // 상대별 배신 모드 활성화 여부
    private final Map<Player, Integer> betrayalCount = new ConcurrentHashMap<>(); // 상대별 배신 횟수
    private final Map<Player, Integer> totalRounds = new ConcurrentHashMap<>(); // 상대별 총 게임 횟수
    private final Map<Player, List<Boolean>> opponentHistories = new ConcurrentHashMap<>(); // 상대별 기록 저장

    @Override
    public boolean choose(Player self, Player opponent, List<Boolean> opponentHistory) {
        // 리스트를 `CopyOnWriteArrayList`로 관리하여 동시 수정 방지
        opponentHistories.putIfAbsent(opponent, new CopyOnWriteArrayList<>(opponentHistory));
        List<Boolean> history = opponentHistories.get(opponent);

        // 상대와의 총 라운드 수 증가 (동기화된 업데이트)
        totalRounds.compute(opponent, (k, v) -> (v == null) ? 1 : v + 1);
        int rounds = totalRounds.get(opponent);

        // 첫 번째 라운드는 무조건 협력(C)
        if (history.isEmpty()) {
            return true;
        }

        // 상대의 협력 비율 계산
        long cooperationCount = history.stream().filter(b -> b).count();
        double cooperationRate = (double) cooperationCount / history.size();

        // 상대가 배신하면 즉시 보복(배신)하고 배신 모드 해제
        if (!history.get(history.size() - 1)) {
            betrayalPhase.put(opponent, false); // 배신 모드 해제
            return false; // 즉시 응징 (배신)
        }

        // 상대별 배신 횟수 가져오기 (Atomic 수정)
        betrayalCount.putIfAbsent(opponent, 0);
        double betrayalRate = (double) betrayalCount.get(opponent) / rounds;

        // 배신 비율이 25%를 넘지 않도록 제한
        if (betrayalRate >= 0.25) {
            return true; // 배신 비율이 너무 높으면 협력 유지
        }

        // 상대가 계속 협력하면 일정 시점에서 배신 모드 활성화
        betrayalPhase.putIfAbsent(opponent, false);
        if (!betrayalPhase.get(opponent) && cooperationRate > 0.8) {
            betrayalPhase.put(opponent, true); // 배신 모드 활성화
        }

        // 배신 모드에서는 점진적으로 배신 횟수를 증가시켜 착취
        if (betrayalPhase.get(opponent)) {
            if (betrayalCount.get(opponent) < rounds / 4) { // 배신 비율이 전체 게임의 1/4 이하로 유지
                betrayalCount.compute(opponent, (k, v) -> v + 1); // Atomic 업데이트
                return false; // 배신
            } else {
                betrayalPhase.put(opponent, false); // 다시 협력 모드로 전환
            }
        }

        // 기본적으로 협력 유지 (C)
        return true;
    }
    
    @Override
    public Strategy cloneStrategy() {
        return new Tranquilizer();
    }
}