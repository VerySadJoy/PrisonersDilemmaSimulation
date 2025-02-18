package Strategy;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
// (착취형, 배신형) 강자를 피하고 약자를 사냥하는 포식자  
//  
// Predator 전략은 상대가 협력적인 성향을 보이면 이를 착취하고,  
// 보복 가능성이 높은 상대에게는 협력하는 기회주의적인 전략이다.  
//  
// 이 전략의 핵심은 상대가 순수 협력가라면 계속 배신하여 이득을 취하고,  
// 보복 가능성이 있는 상대에게는 조용히 협력하는 것이다.  
//  
// 전략의 작동 방식:  
// - 첫 번째 라운드는 무조건 배신(D)하여 상대의 반응을 본다.  
// - 상대의 협력 비율이 80% 이상이면 순수 협력가로 간주하고 계속 배신(D).  
// - 상대가 보복형 전략이라면 협력(C)하여 공격을 피한다.  
//  
// 장점:  
// - 완전한 협력가(Always Cooperate)와 만나면 지속적으로 높은 이득을 취할 수 있다.  
// - 보복형 전략(Tit-for-Tat, Grim Trigger)과 만나도 먼저 공격을 하지 않으므로, 강한 보복을 피할 수 있다.  
// - 상대의 성향을 탐색한 후, 위험 부담 없이 착취할 대상을 선별하는 방식이므로 안정적인 점수 획득이 가능하다.  
//  
// 단점:  
// - 협력가를 착취하는 전략이므로, 협력 관계를 형성할 가능성이 거의 없다.  
// - Forgiving Tit-for-Tat 같은 전략과 만나면 상대가 점차 배신을 섞기 시작하여 손해를 볼 수도 있다.  
// - 패턴이 단순하여 상대가 이를 간파하면, 협력 비율을 조정하면서 전략을 무력화할 수도 있다.  
//  
// 실제 사회에서 이 전략을 따르는 인간 유형은 "강자를 피하고 약자를 사냥하는 포식자"이다.  
// 이들은 순수하게 협력적인 상대를 이용하여 최대한의 이득을 얻으려 하지만,  
// 보복할 가능성이 있는 상대에게는 조용히 협력하여 문제를 피하는 성향을 가진다.  
// 단기적인 이득을 얻는 데 효과적이지만,  
// 장기적으로는 신뢰를 얻기 어려워 사회적 관계에서 불리할 수도 있다.  

public class Predator implements Strategy {
    private final Map<Player, List<Boolean>> opponentHistories = new ConcurrentHashMap<>(); // 안전한 기록 저장
    private final Map<Player, Boolean> isCooperatorMap = new ConcurrentHashMap<>(); // 협력가 여부 저장

    @Override
    public boolean choose(Player self, Player opponent, List<Boolean> opponentHistory) {
        // 안전한 리스트 관리
        opponentHistories.putIfAbsent(opponent, new CopyOnWriteArrayList<>(opponentHistory));
        List<Boolean> history = opponentHistories.get(opponent);

        // 첫 번째 턴은 무조건 배신(D)
        if (history.isEmpty()) {
            return false;
        }

        long betrayals;
        synchronized (history) {
            betrayals = history.stream().filter(b -> !b).count();
        }

        boolean isCooperator = betrayals < history.size() * 0.2; // 80% 이상 협력하면 순수 협력가로 간주
        isCooperatorMap.put(opponent, isCooperator);

        return !isCooperatorMap.get(opponent); // 협력가면 계속 배신, 보복형이면 협력
    }
    
    @Override
    public Strategy cloneStrategy() {
        return new Predator();
    }
}