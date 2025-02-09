package Strategy;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
// (보복형, 협력형) 온화하지만 단호한 인도자, 조용한 복수자  
//  
// Shepherd 전략은 기본적으로 협력적인 태도를 유지하지만,  
// 상대가 배신할 경우 일정량의 보복을 가한 뒤 다시 협력을 시도하는 전략이다.  
//  
// 이 전략의 핵심은 ‘상대가 배신하면 즉각적인 강한 보복을 실행하지만,  
// 보복이 끝나면 다시 협력 모드로 복귀하는 것’이다.  
//  
// 전략의 작동 방식:  
// - 첫 번째 라운드는 무조건 협력(C).  
// - 상대가 협력하면 계속 협력(C) 유지.  
// - 상대가 배신하면 복수 카운트를 2 증가시킨 후, 다음 2턴 동안 배신(D)으로 응징.  
// - 이후 복수 카운트가 0이 되면 다시 협력(C)으로 복귀.  
//  
// 장점:  
// - 상대가 실수로 배신했을 경우, 과도한 보복 없이 적절한 응징을 한 후 관계를 회복할 수 있다.  
// - 보복형 전략이지만 극단적인 Tit-for-Tat이나 Grim Trigger보다 유연한 대응이 가능하다.  
// - 완전한 협력가(Always Cooperate)와 만나면 장기적인 협력을 유지할 수 있다.  
//  
// 단점:  
// - 상대가 패턴을 간파하면, 주기적으로 1번씩 배신을 해서 보복을 최소화할 수도 있다.  
// - Forgiving Tit-for-Tat과 비슷한 문제를 가지며, 교활한 배신자에게 착취당할 가능성이 있다.  
// - 랜덤 배신 전략(Probabilistic Tit-for-Tat)과 만나면 보복이 제대로 작동하지 않을 수 있다.  
//  
// 실제 사회에서 이 전략을 따르는 인간 유형은 "온화하지만 단호한 인도자" 또는 "조용한 복수자"이다.  
// 이들은 협력적이지만, 상대가 배신하면 감정적 반응 없이 철저한 응징을 실행하며,  
// 보복이 끝난 후에는 다시 협력하려는 성향을 가진다.  
// 이러한 태도는 협력 관계를 지속할 수 있도록 만들지만,  
// 상대가 이 패턴을 인지하면 이를 역이용하여 보복을 최소화할 방법을 모색할 수도 있다.  

public class Shepherd implements Strategy {
    private final Map<Player, Integer> betrayalCount = new ConcurrentHashMap<>(); // 상대가 배신했을 때 복수 카운트
    
    @Override
    public boolean choose(Player self, Player opponent, List<Boolean> opponentHistory) {
        // 만약 상대와 처음 만나는 거라면 기본적으로 협력
        if (opponentHistory.isEmpty()) {
            return true;
        }
        
        // 상대의 마지막 행동 가져오기
        boolean opponentLastMove = opponentHistory.get(opponentHistory.size() - 1);
        
        // 상대가 배신했다면 복수 카운트를 2 증가
        if (!opponentLastMove) {
            betrayalCount.put(opponent, betrayalCount.getOrDefault(opponent, 0) + 2);
        }
        
        // 현재 복수 중이라면 배신 (복수 카운트를 줄여가면서)
        if (betrayalCount.getOrDefault(opponent, 0) > 0) {
            betrayalCount.put(opponent, betrayalCount.get(opponent) - 1);
            return false; // 배신
        }
        
        // 기본적으로 협력
        return true;
    }

    @Override
    public Strategy cloneStrategy() {
        return new Shepherd();
    }
}