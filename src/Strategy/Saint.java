package Strategy;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
// (협력형, 보복형) 성인군자, 한계를 넘어선 인내자  
//  
// Saint 전략은 상대가 배신을 반복해도 일정 횟수(10번)까지는 무조건 참으며,  
// 그 이후부터는 보복을 시작하는 극단적으로 관대한 협력 전략이다.  
//  
// 이 전략의 핵심은 ‘상대에게 충분한 기회를 주고,  
// 인내할 수 있는 한계를 넘어서면 보복형 전략으로 전환하는 것’이다.  
//  
// 전략의 작동 방식:  
// - 초반에는 무조건 협력(C)을 유지하며, 상대의 배신을 참고 넘어감.  
// - 상대의 배신 횟수가 10번을 넘어서면 Tit-for-Tat 방식으로 전환하여 보복을 시작.  
// - 즉, 상대가 협력하면 협력하고, 배신하면 배신하는 형태로 변함.  
//  
// 장점:  
// - Forgiving Tit-for-Tat보다 훨씬 더 참을성이 강하여, 실수로 배신한 상대와 협력을 유지할 가능성이 크다.  
// - 협력 관계를 유지하려는 전략과 만나면 최상의 결과를 얻을 수 있다.  
// - 초반에 공격적인 전략과 만나도 즉시 보복하지 않기 때문에 상대의 태도를 바꿀 기회를 제공할 수 있다.  
//  
// 단점:  
// - 상대가 완전한 배신자(Always Defect)일 경우, 10번이나 손해를 보고 나서야 대응하기 때문에 상당한 점수 손실이 발생한다.  
// - 랜덤 배신 전략(Probabilistic Tit-for-Tat)과 만나면, 필요 이상으로 손해를 볼 가능성이 있다.  
// - 상대가 이 전략을 간파하면, 10번까지는 배신하고 이후에 협력하는 방식으로 악용할 수도 있다.  
//  
// 실제 사회에서 이 전략을 따르는 인간 유형은 "성인군자" 또는 "한계를 넘어선 인내자"이다.  
// 이들은 상대가 배신하더라도 오랫동안 참으며 협력적인 태도를 유지하려 하지만,  
// 참을 수 있는 한계를 넘어서면 그때부터 냉정하게 대응한다.  
// 그러나 지나치게 참을성이 강한 태도는 때때로 악용될 가능성이 있으며,  
// 상대가 이를 이용하면 상당한 손해를 감수할 수도 있다.  

public class Saint implements Strategy {
    private final ConcurrentHashMap<Player, Integer> betrayals = new ConcurrentHashMap<>();
    private final int TOLERANCE = 10; // 배신을 10번까지는 참음

    @Override
    public boolean choose(Player self, Player opponent, List<Boolean> opponentHistory) {
        betrayals.putIfAbsent(opponent, 0);

        if (opponentHistory.isEmpty()) {
            return true; // 처음엔 무조건 협력
        }

        if (!opponentHistory.get(opponentHistory.size() - 1)) {
            betrayals.put(opponent, betrayals.get(opponent) + 1);
        }

        // 상대가 10번 이상 배신했으면 Tit-for-Tat 모드 돌입
        if (betrayals.get(opponent) >= TOLERANCE) {
            return opponentHistory.get(opponentHistory.size() - 1);
        }

        return true; // 평소에는 무조건 협력
    }

    @Override
    public Strategy cloneStrategy() {
        return new Saint();
    }
}