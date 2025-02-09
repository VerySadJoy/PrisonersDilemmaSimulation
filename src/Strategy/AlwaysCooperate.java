package Strategy;

import java.util.List;

// (협력형) 이상주의적 성인군자, 순진한 낙천주의자  
//  
// AlwaysCooperate 전략은 상대가 배신하든 협력하든, 어떤 상황에서도 항상 협력(C)만 선택하는 전략이다.  
// 어떠한 보복도 하지 않으며, 상대에게 무조건적인 신뢰를 보내는 방식으로 작동한다.  
//  
// 장점:  
// - 두 AlwaysCooperate 전략이 만나면 최고의 결과(최대 보상)를 얻을 수 있다.  
// - 상대가 협력적인 성향이라면 장기적인 관계에서 신뢰를 구축할 수 있다.  
// - 보복형 전략(Tit-for-Tat)과도 안정적인 협력 관계를 유지할 가능성이 크다.  
//  
// 단점:  
// - 배신형 전략(Always Defect, Greedy Tit-for-Tat)과 만나면 일방적으로 착취당한다.  
// - 상대가 악의적인 경우에도 무조건 협력하기 때문에 쉽게 희생양이 될 수 있다.  
// - 현실 세계에서는 순진하거나 이용당하기 쉬운 성향으로 평가될 수 있다.  
//  
// 실제 사회에서 이 전략을 따르는 인간 유형은 "이상주의적 성인군자" 또는 "순진한 낙천주의자"이다.  
// 인간관계를 중요시하며 누구에게나 선의를 베풀지만, 세상의 냉혹한 현실 앞에서  
// 쉽게 상처받거나 이용당할 가능성이 크다.  
// 하지만, 그들의 존재 자체가 협력을 촉진하고, 이상적인 사회를 가능하게 만드는 힘이 될 수도 있다.  

public class AlwaysCooperate implements Strategy {
    @Override
    public boolean choose(Player self, Player opponent, List<Boolean> opponentHistory) {
        // 항상 협력 (C)을 선택함
        return true;
    }
    
    @Override
    public Strategy cloneStrategy() {
        return new AlwaysCooperate();
    }
}