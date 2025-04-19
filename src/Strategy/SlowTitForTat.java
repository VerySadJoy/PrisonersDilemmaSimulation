package Strategy;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 전략 이름: SlowTitForTat (STFT2)  
 * 전략 유형: 보복형 + 협력형 (느리게 반응하는 신중한 대응자)
 * 
 * 전략 개요:  
 * - 일반적인 Tit-for-Tat보다 반응이 느린 전략  
 * - 상대가 두 번 연속 같은 행동(C 또는 D)을 할 때만 따라감  
 * - 그렇지 않으면 자신의 이전 행동을 유지
 * 
 * 작동 방식:  
 * - 1~2라운드: 무조건 협력(C)  
 * - 이후:  
 *   - 상대가 두 번 연속 같은 행동 → 그 행동을 따라함  
 *   - 그렇지 않으면 이전 라운드의 내 행동을 반복함
 * 
 * 장점:  
 * - 협력과 배신이 뒤섞인 불안정한 상대에 대해 즉각 반응하지 않음  
 * - 상대가 반복적인 성향이면 효과적으로 동기화 가능  
 * - 불필요한 보복을 줄이고, 협력 기회는 열어두는 전략
 * 
 * 단점:  
 * - 변동성이 높은 랜덤형 전략에게는 무력하게 보일 수 있음  
 * - 상대가 너무 유연하거나 Forgiving한 경우, 내가 너무 느려서 손해 볼 수 있음  
 * - 반응이 느리기 때문에 초기 몇 턴 동안 손해를 감수할 수도 있음
 * 
 * 인간 유형 대응:  
 * - 신중한 관찰자, 상대를 급히 평가하지 않고, 패턴이 보일 때까지 기다림  
 * - 한 번 배신했다고 바로 되갚지 않지만, 반복되면 차분하게 대응  
 * - 대화나 관계에서도 느리게 반응하지만, 한 번 판단하면 그 기준을 오래 유지하는 사람
*/ 

public class SlowTitForTat implements Strategy {
    private final Map<Player, List<Boolean>> opponentHistories = new ConcurrentHashMap<>(); // 상대 기록 저장
    private final Map<Player, List<Boolean>> selfHistories = new ConcurrentHashMap<>(); // 본인의 기록 저장

    @Override
    public boolean choose(Player self, Player opponent, List<Boolean> opponentHistory) {
        // 상대 기록 관리
        opponentHistories.putIfAbsent(opponent, new CopyOnWriteArrayList<>(opponentHistory));
        List<Boolean> history = opponentHistories.get(opponent);

        // 본인의 기록도 관리
        selfHistories.putIfAbsent(opponent, new CopyOnWriteArrayList<>());
        List<Boolean> selfHistory = selfHistories.get(opponent);

        // 첫 두 라운드는 무조건 협력(C)
        if (history.size() < 2) {
            selfHistory.add(true);
            return true;
        }

        // 상대의 마지막 두 행동 가져오기
        int historySize = history.size();
        boolean lastMove = history.get(historySize - 1);
        boolean secondLastMove = history.get(historySize - 2);

        // 상대가 최근 두 번 연속 같은 행동을 했으면, 그 행동을 따라감
        if (lastMove == secondLastMove) {
            selfHistory.add(lastMove);
            return lastMove;
        }

        // 그렇지 않다면, 이전 라운드에서 자신이 했던 행동을 반복
        boolean myLastMove = selfHistory.get(selfHistory.size() - 1);
        selfHistory.add(myLastMove);
        return myLastMove;
    }

    @Override
    public Strategy cloneStrategy() {
        return new SlowTitForTat();
    }
}