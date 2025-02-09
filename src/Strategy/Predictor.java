package Strategy;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Predictor implements Strategy {
    private final Map<Player, Integer> opponentCoopCount = new ConcurrentHashMap<>(); // 협력 횟수
    private final Map<Player, Integer> opponentDefectCount = new ConcurrentHashMap<>(); // 배신 횟수
    private final Map<Player, Integer> totalRounds = new ConcurrentHashMap<>(); // 전체 라운드 수
    private final Map<Player, Integer> whenICoopTheyCoop = new ConcurrentHashMap<>(); // 내가 협력했을 때 상대도 협력한 횟수
    private final Map<Player, Integer> whenIDefectTheyCoop = new ConcurrentHashMap<>(); // 내가 배신했을 때 상대가 협력한 횟수
    private final Map<Player, Integer> whenICoopTheyDefect = new ConcurrentHashMap<>(); // 내가 협력했을 때 상대가 배신한 횟수
    private final Map<Player, Integer> whenIDefectTheyDefect = new ConcurrentHashMap<>(); // 내가 배신했을 때 상대도 배신한 횟수

    @Override
    public boolean choose(Player self, Player opponent, List<Boolean> opponentHistory) {
        totalRounds.put(opponent, totalRounds.getOrDefault(opponent, 0) + 1);
        int rounds = totalRounds.get(opponent);

        // 첫 5라운드는 데이터 수집을 위해 기본적으로 협력
        if (rounds <= 5) {
            return true;
        }

        // ✅ `computeIfAbsent()`로 안전하게 값 초기화
        opponentCoopCount.computeIfAbsent(opponent, k -> 0);
        opponentDefectCount.computeIfAbsent(opponent, k -> 0);
        whenICoopTheyCoop.computeIfAbsent(opponent, k -> 0);
        whenICoopTheyDefect.computeIfAbsent(opponent, k -> 0);
        whenIDefectTheyCoop.computeIfAbsent(opponent, k -> 0);
        whenIDefectTheyDefect.computeIfAbsent(opponent, k -> 0);

        // 상대의 협력/배신 패턴 기록
        boolean lastMove = opponentHistory.get(opponentHistory.size() - 1);
        if (lastMove) {
            opponentCoopCount.put(opponent, opponentCoopCount.get(opponent) + 1);
        } else {
            opponentDefectCount.put(opponent, opponentDefectCount.get(opponent) + 1);
        }

        // 내가 지난 턴에 무엇을 했는지 분석
        boolean myLastMove = rounds > 1 && opponentHistory.size() >= 2 
            ? Boolean.TRUE.equals(opponentHistory.get(opponentHistory.size() - 2))
            : true;


        if (myLastMove && lastMove) {
            whenICoopTheyCoop.put(opponent, whenICoopTheyCoop.get(opponent) + 1);
        } else if (myLastMove && !lastMove) {
            whenICoopTheyDefect.put(opponent, whenICoopTheyDefect.get(opponent) + 1);
        } else if (!myLastMove && lastMove) {
            whenIDefectTheyCoop.put(opponent, whenIDefectTheyCoop.get(opponent) + 1);
        } else {
            whenIDefectTheyDefect.put(opponent, whenIDefectTheyDefect.get(opponent) + 1);
        }

        // 협력 확률 계산
        double totalInteractions = (double) rounds;
        double coopRate = opponentCoopCount.get(opponent) / totalInteractions;
        double defectRate = opponentDefectCount.get(opponent) / totalInteractions;

        double whenICoopDefectRate = whenICoopTheyDefect.get(opponent) /
                (double) Math.max(1, whenICoopTheyCoop.get(opponent) + whenICoopTheyDefect.get(opponent));

        double whenIDefectDefectRate = whenIDefectTheyDefect.get(opponent) /
                (double) Math.max(1, whenIDefectTheyCoop.get(opponent) + whenIDefectTheyDefect.get(opponent));

        // 상대가 내 협력에 대해 배신할 확률이 70% 이상이면 배신
        if (whenICoopDefectRate > 0.7) {
            return false;
        }

        // 상대가 내 배신에 대해서도 배신할 확률이 높다면 나도 배신
        if (whenIDefectDefectRate > 0.6) {
            return false;
        }

        // 기본적으로 상대가 협력할 확률이 높으면 협력
        return coopRate > defectRate;
    }

    @Override
    public Strategy cloneStrategy() {
        return new Predictor();
    }
}