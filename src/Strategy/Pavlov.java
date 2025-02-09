package Strategy;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
// (협력형, 보복형) 조건 반사적 학습자, 성공 유지 본능자  
//  
// Pavlov 전략은 자신의 이전 행동이 성공적이었으면 유지하고,  
// 실패했으면 변경하는 학습형 전략이다.  
//  
// 이 전략의 핵심은 ‘조건 반사적으로 과거의 결과를 학습하여,  
// 성공했던 행동을 지속하고 실패했던 행동을 변경하는 것’이다.  
//  
// 전략의 작동 방식:  
// - 첫 번째 라운드는 무조건 협력(C)한다.  
// - 이전 라운드에서 협력-협력(C, C) 또는 배신-배신(D, D)이 발생하면 기존 행동 유지.  
// - 이전 라운드에서 협력-배신(C, D) 또는 배신-협력(D, C)이 발생하면 행동 변경.  
// - 즉, 성공적인 경험을 반복하려 하며, 실패한 경우에는 반대로 행동한다.  
//  
// 장점:  
// - 상대가 협력 전략을 유지하면 자연스럽게 협력 관계가 형성된다.  
// - 상대가 보복형 전략(Tit-for-Tat)일 경우, 협력을 유지할 가능성이 높다.  
// - 상대가 배신을 반복하면 점점 배신으로 전환하며 손해를 줄일 수 있다.  
//  
// 단점:  
// - 랜덤 전략과 만나면 행동이 계속 변하면서 불안정해질 수 있다.  
// - 상대가 의도적으로 C-D-C-D 같은 방식으로 플레이하면 조작당할 가능성이 있다.  
// - 보복형 전략과 만나면 예측 가능한 패턴을 보이기 때문에 상대가 이를 악용할 수 있다.  
//  
// 실제 사회에서 이 전략을 따르는 인간 유형은 "조건 반사적 학습자" 또는 "성공 유지 본능자"이다.  
// 이들은 과거의 경험을 기반으로 자신의 행동을 조정하며,  
// 자신이 이득을 본 선택을 반복하려 하고, 불이익을 경험하면 행동을 수정하는 성향을 가진다.  
// 이러한 특성은 실용적이고 효율적일 수 있지만,  
// 지나치게 단순한 패턴에 의존하면 쉽게 예측당할 위험이 있다.  

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