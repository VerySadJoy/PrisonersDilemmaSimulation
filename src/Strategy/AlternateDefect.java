package Strategy;

import java.util.List;

// (랜덤형, 배신형) 고집스러운 반항아, 고독한 몽상가  
//  
// Alternate Defect 전략은 상대의 행동에 상관없이 D → C → D → C 패턴을 무한히 반복하는 전략이다.  
// 즉, 상대가 협력하든 배신하든 자신의 고유한 방식을 절대 바꾸지 않는다.  
//  
// 이 전략은 상대의 성향이나 과거의 행동을 고려하지 않으며,  
// 단순한 주기적 패턴에 따라 작동하기 때문에 보복형 전략을 무력화할 수 있다.  
// 예를 들어, Tit-for-Tat(맞대응 전략)과 대결할 경우 일정한 협력을 유도할 수도 있지만,  
// 장기적으로 신뢰를 형성하기 어렵고, 항상 절반의 라운드에서 손해를 볼 가능성이 있다.  
//  
// 또한, 예측이 쉽기 때문에 상대가 패턴을 학습하면 쉽게 공략당할 수 있다.  
// 하지만 상대가 랜덤한 전략을 사용하면 일정한 수익을 보장받을 수도 있다.  
//  
// 실제 사회에서 이 전략을 사용하는 인간 유형은 "고집스러운 반항아" 또는 "고독한 몽상가"에 가깝다.  
// 남들이 뭐라고 하든 자신의 길을 가며, 협력과 배신을 반복하지만  
// 그것이 상대와의 관계 때문이 아니라 그냥 자신의 원칙일 뿐이다.  
// 타인의 신뢰를 얻기는 어렵지만, 때로는 예측 불가능한 매력을 가질 수도 있다.  

public class AlternateDefect implements Strategy {
    @Override
    public boolean choose(Player self, Player opponent, List<Boolean> opponentHistory) {
        // 첫 번째 라운드는 무조건 배신 (D)
        if (opponentHistory.isEmpty()) {
            return false;
        }
        return opponentHistory.size() % 2 != 0;
    }
    
    @Override
    public Strategy cloneStrategy() {
        return new AlternateDefect();
    }
}