package Strategy;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

// (보복형, 협력형) 느리게 반응하는 신중한 추적자, 반복적 패턴에 반응하는 전략적 대응자  
//  
// Slow Tit For Tat (STFT2) 전략은 일반적인 Tit For Tat (TFT)보다 더 느리게 반응하며,  
// 상대가 연속적으로 같은 행동을 할 때만 그 행동을 따라가는 전략이다.  
//  
// 이 전략의 핵심은 상대의 행동이 반복되기 전까지 반응을 지연시키고,  
// 같은 행동이 반복될 때만 그 행동을 따라가며, 그렇지 않으면 자신의 행동을 유지하는 것이다.  
//  
// 전략의 작동 방식:  
// - 첫 두 라운드는 무조건 협력(C).  
// - 이후 상대의 최근 두 번의 행동을 비교하여, 같은 행동이 연속되면 그 행동을 그대로 따라감.  
// - 상대의 최근 두 행동이 다르면, 자신이 마지막에 했던 행동을 반복.  
//  
// 장점:  
// - 빠른 반응을 피하고, 상대의 행동 패턴을 신중하게 확인한 후 반응한다.  
// - 상대가 자주 행동을 바꾸는 경우, 불필요한 보복을 줄여서 협력의 기회를 유지할 수 있다.  
// - 안정적이고 반복적인 패턴을 가진 상대에게 강력한 대응을 할 수 있다.  
//  
// 단점:  
// - 상대가 변동성이 큰 전략(랜덤성 높은 전략)을 사용할 경우, 반응이 늦어져 협력적인 관계를 맺는 데 어려움이 있을 수 있다.  
// - Forgiving Tit-for-Tat과 같은 보다 관대하게 대응하는 전략과 만날 때, 손해를 볼 가능성이 있다.  
// - 상대가 일관된 행동을 하지 않는 경우, 지속적으로 자신의 행동을 반복하여 협력의 기회를 놓칠 수 있다.  
//  
// 실제 사회에서 이 전략을 따르는 인간 유형은 "느리게 반응하는 신중한 추적자" 또는 "반복적 패턴에 반응하는 전략적 대응자"이다.  
// 이들은 상대의 성향을 파악하기 전에 급하게 반응하지 않고,  
// 상대가 일관된 행동을 보일 때만 이에 반응하여 효율적으로 대응하려 한다.  
// 하지만 너무 늦은 반응은 상대에게 기회를 줄 수 있어, 신속하게 대응할 필요가 있을 수 있다.  

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