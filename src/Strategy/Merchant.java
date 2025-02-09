package Strategy;

import java.util.List;

public class Merchant implements Strategy {
    private boolean lastMove; // 이전에 내가 했던 선택 (true = 협력, false = 배신)

    @Override
    public boolean choose(Player self, Player opponent, List<Boolean> opponentHistory) {
        if (opponentHistory.isEmpty()) {
            return false; // 첫 턴에는 초기값 사용
        }

        boolean opponentLastMove = opponentHistory.get(opponentHistory.size() - 1);
        lastMove = !opponentLastMove; // 상대가 협력하면 배신, 배신하면 협력
        return lastMove;
    }

    @Override
    public Strategy cloneStrategy() {
        return new Merchant(); // 새 전략 인스턴스 생성
    }
}