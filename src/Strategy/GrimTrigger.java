package Strategy;

import java.util.List;

// (보복형) 절대적인 응징자, 신뢰를 잃으면 끝장내는 복수자  
//  
// Grim Trigger 전략은 초반에는 협력(C)하지만,  
// 상대가 단 한 번이라도 배신(D)하면 이후로 무조건 배신하는 가혹한 보복형 전략이다.  
//  
// 이 전략의 핵심은 신뢰는 한 번 깨지면 절대 회복되지 않는다는 원칙을 따르는 것이다.  
// 즉, 상대가 실수든 의도적이든 단 한 번이라도 배신하면,  
// 이후로는 절대 용서하지 않고 지속적으로 배신을 유지한다.  
//  
// 장점:  
// - 상대가 전략을 알고 있다면, 배신을 두려워하여 끝까지 협력을 유지할 가능성이 높다.  
// - 협력적인 상대와 만나면 최상의 결과를 얻을 수 있다.  
// - 보복형 전략 중에서도 가장 강력한 억제력을 가지고 있어, 협력적인 환경에서는 매우 효과적이다.  
//  
// 단점:  
// - 상대가 실수로 한 번 배신해도 영원히 협력을 잃어버리기 때문에, 지나치게 가혹할 수 있다.  
// - 랜덤한 행동을 하는 전략(예: Random, Adaptive Trickster)과 만나면 쉽게 협력을 깨뜨릴 위험이 있다.  
// - Forgiving Tit-for-Tat 같은 유연한 전략보다 지나치게 극단적이어서, 상대를 완전히 배척할 가능성이 크다.  
//  
// 실제 사회에서 이 전략을 따르는 인간 유형은 "절대적인 응징자" 또는 "신뢰를 잃으면 끝장내는 복수자"이다.  
// 이들은 처음에는 협력적이지만, 상대가 한 번이라도 신뢰를 깨면  
// 그 이후로는 절대 용서하지 않고 강경한 태도를 유지한다.  
// 이러한 성향은 상대에게 강한 경고 효과를 줄 수 있지만,  
// 너무 가혹한 대응으로 인해 때로는 손해를 볼 수도 있다.  

public class GrimTrigger implements Strategy {

    @Override
    public boolean choose(Player self, Player opponent, List<Boolean> opponentHistory) {
        // 첫 번째 라운드는 기본적으로 협력(C)
        if (opponentHistory.isEmpty()) {
            return true;
        }

        // 상대가 한 번이라도 배신(D)한 적이 있으면 이후로 계속 배신(D)
        return !opponentHistory.contains(false);
    }
    
    @Override
    public Strategy cloneStrategy() {
        return new GrimTrigger();
    }
}