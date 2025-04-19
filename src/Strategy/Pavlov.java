package Strategy;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 전략 이름: Pavlov
 * 전략 유형: 협력형 + 보복형 (조건 반사적 학습자, 성공 유지 본능자)

 * 전략 개요:
 * - 과거 라운드의 결과가 성공적이었는지 실패였는지 판단해 다음 행동을 결정
 * - 성공: 내가 한 행동이 상대와 같았다면 유지
 * - 실패: 내가 한 행동이 상대와 달랐다면 반대로 전환

 * 작동 방식:
 * - 첫 라운드: 무조건 협력 (C)
 * - 이후:
 *   - (C, C) or (D, D) → 성공 → 같은 행동 유지
 *   - (C, D) or (D, C) → 실패 → 행동 전환

 * 장점:
 * - 협력 전략과 만나면 협력을 유지할 수 있음
 * - 상대가 배신만 해도 점차 배신 쪽으로 동기화되며 손해를 줄임
 * - 단순하지만 효과적인 반복 학습형 전략

 * 단점:
 * - Random 전략과 만나면 쉽게 혼란스러워질 수 있음
 * - C-D 반복 전략에게는 쉽게 조작당할 수 있음
 * - 전략이 지나치게 규칙적이어서 예측당할 수 있음

 * 인간 대응 유형:
 * - 성공 경험을 반복하려는 실용주의자
 * - 결과 중심적이며, 실패에는 곧바로 수정하는 성향
 * - 단순한 규칙 기반 사고에 강하지만, 복잡한 패턴에선 약점이 생김
 */

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