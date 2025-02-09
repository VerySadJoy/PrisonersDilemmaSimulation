package Strategy;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

// (보복형) 장기적 복수자, 점진적 응징자  
//  
// Gradual 전략은 상대가 배신한 시점에 따라 보복의 강도가 달라지는 비선형적인 보복형 전략이다.  
// 초반에 여러 번 배신을 당해도 보복의 총량이 적지만,  
// 후반부에 단 한 번이라도 배신하면 해당 시점까지의 모든 라운드를 반영한 강한 보복을 실행한다.  
//  
// 이 전략의 핵심은 ‘초반 배신은 관대하게 용서할 수 있지만,  
// 후반부 배신은 절대 용납하지 않고 강력한 응징을 가하는 것’이다.  
//  
// 예를 들어, 초반 5라운드 동안 연속으로 배신을 당하면 5 + 4 + 3 + 2 + 1 = 15번만 배신으로 응징하지만,  
// 99라운드까지 협력하다가 마지막 100라운드에서 배신당하면,  
// 보복으로 100번 연속 배신을 실행하여 상대를 완전히 무너뜨린다.  
//  
// 장점:  
// - 초반의 실수는 크게 보복하지 않기 때문에 협력을 유지할 가능성이 크다.  
// - 장기적인 관계에서 신뢰를 형성할 수 있으며, 상대가 보복을 예상하고 조심하게 만든다.  
// - 후반 배신을 극도로 억제할 수 있어, 상대가 끝까지 협력하도록 유도하는 효과가 있다.  
//  
// 단점:  
// - 상대가 전략을 간파하면, 초반에는 배신을 반복하고 후반에는 협력하여 이득을 볼 수 있다.  
// - 초반에 실수를 유도하는 전략(예: 탐색형 랜덤 배신 전략)과 만나면 불리할 수도 있다.  
// - 보복이 점진적으로 누적되기 때문에, 어떤 상황에서도 상대에게 적당한 수준의 압박을 유지하기 어렵다.  
//  
// 실제 사회에서 이 전략을 따르는 인간 유형은 "장기적 복수자" 또는 "점진적 응징자"이다.  
// 신뢰를 중요시하며 협력적인 태도를 유지하지만,  
// 신뢰를 저버리는 순간, 과거의 모든 관계를 반영한 강력한 응징을 가한다.  
// 이는 상대에게 신중한 행동을 유도하지만,  
// 지나치게 가혹한 응징으로 인해 긴장 관계를 초래할 위험도 있다.  

public class Gradual implements Strategy {
    private final Map<Player, Integer> pendingDefections = new HashMap<>(); // 각 상대별 보복 횟수 기록
    private int round = 0; // 현재 라운드

    @Override
    public boolean choose(Player self, Player opponent, List<Boolean> opponentHistory) {
        round++; // 매 호출 시 라운드 증가

        // 상대방의 기록이 없으면 (첫 라운드라면) 무조건 협력(C)
        if (opponentHistory.isEmpty()) {
            return true;
        }

        // 상대의 마지막 행동이 배신(D)이라면, 해당 라운드 수만큼 보복 예약
        if (!opponentHistory.get(opponentHistory.size() - 1)) {
            pendingDefections.put(opponent, pendingDefections.getOrDefault(opponent, 0) + round);
        }

        // 예약된 보복이 남아 있다면 배신(D) 실행
        if (pendingDefections.getOrDefault(opponent, 0) > 0) {
            pendingDefections.put(opponent, pendingDefections.get(opponent) - 1);
            return false; // 배신
        }

        // 기본적으로 협력 유지 (C)
        return true;
    }

    @Override
    public Strategy cloneStrategy() {
        return new Gradual();
    }
}