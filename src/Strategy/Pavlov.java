package Strategy;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Pavlov implements Strategy {
    private final Map<Player, List<Boolean>> moveHistory = new ConcurrentHashMap<>();

    @Override
    public boolean choose(Player self, Player opponent, List<Boolean> opponentHistory) {
        // 첫 번째 라운드는 무조건 협력
        if (!moveHistory.containsKey(opponent)) {
            moveHistory.put(opponent, new java.util.ArrayList<>());
            return true;
        }

        List<Boolean> myHistory = moveHistory.get(opponent);

        // 첫 번째 라운드는 무조건 협력
        if (myHistory.isEmpty()) {
            myHistory.add(true);
            return true;
        }

        // 직전 라운드에서 선택한 행동
        boolean myLastMove = myHistory.get(myHistory.size() - 1);
        boolean opponentLastMove = opponentHistory.get(opponentHistory.size() - 1);

        // Pavlov 전략: 성공하면 유지, 실패하면 변경
        boolean nextMove;
        if ((myLastMove && opponentLastMove) || (!myLastMove && !opponentLastMove)) {
            nextMove = myLastMove; // 성공했으면 유지
        } else {
            nextMove = !myLastMove; // 실패했으면 변경
        }

        // 히스토리 업데이트
        myHistory.add(nextMove);

        return nextMove;
    }

    @Override
    public Strategy cloneStrategy() {
        return new Pavlov();
    }
}