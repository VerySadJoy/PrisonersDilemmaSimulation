package Strategy;

import java.util.List;

// (보복형, 협력형) 신뢰와 복수의 균형자, 가장 유명한 공정한 대응자  
//  
// Tit-for-Tat 전략은 상대의 지난 행동을 그대로 따라가는 가장 단순하면서도 강력한 전략 중 하나이다.  
//  
// 이 전략의 핵심은 ‘상대가 협력하면 협력하고, 상대가 배신하면 배신하는 것’으로,  
// 상대가 협력적인 태도를 유지하면 지속적으로 협력할 수 있지만,  
// 상대가 배신을 선택하면 즉시 보복하여 추가적인 배신을 막는다.  
//  
// 전략의 작동 방식:  
// - 첫 번째 라운드는 무조건 협력(C) 하여 상대가 협력할 기회를 준다.  
// - 이후부터는 상대의 마지막 행동을 그대로 따라간다.  
//  
// 장점:  
// - 협력 기반의 환경에서 매우 강력하며, 신뢰할 수 있는 상대와 만나면 높은 점수를 유지할 수 있다.  
// - 완전한 배신자(Always Defect)와 만나면 최소한의 손해로 빠르게 적응할 수 있다.  
// - 상대가 실수로 한 번 배신했더라도, 다시 협력을 유지하면 곧바로 회복할 수 있다.  
//  
// 단점:  
// - Forgiving Tit-for-Tat보다 유연성이 부족하여, 랜덤 배신 전략(Probabilistic Tit-for-Tat)과 만나면 불필요한 배신을 반복할 수 있다.  
// - 보복 전략이기 때문에, 상대가 지속적으로 협력하지 않으면 협력 관계를 지속하기 어렵다.  
// - 상대가 C-D-C-D 같은 패턴을 사용하면, 끝없이 반복되는 비효율적인 싸움이 발생할 수 있다.  
//  
// 실제 사회에서 이 전략을 따르는 인간 유형은 "신뢰와 복수의 균형자" 또는 "가장 유명한 공정한 대응자"이다.  
// 이들은 상대가 협력하면 협력하지만, 배신하면 즉시 응징하는 공정한 태도를 유지한다.  
// 이러한 성향은 협력적인 관계를 구축하는 데 매우 유용하지만,  
// 너무 기계적으로 반응하여 유연성이 부족할 수도 있다.  

public class TitForTat implements Strategy {

    @Override
    public boolean choose(Player self, Player opponent, List<Boolean> opponentHistory) {
        // 첫 번째 라운드는 기본적으로 협력(C)
        if (opponentHistory.isEmpty()) {
            return true;
        }

        // 상대의 마지막 행동을 따라감 (협력(C) 또는 배신(D))
        return opponentHistory.get(opponentHistory.size() - 1);
    }
    
    @Override
    public Strategy cloneStrategy() {
        return new TitForTat();
    }
}