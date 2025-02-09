package Strategy;

import java.util.List;
import java.util.Random;

// (랜덤형) 예측 불가능한 광인, 무작위적 혼돈의 창조자  
//  
// RandomStrategy 전략은 상대의 행동과 무관하게 50% 확률로 협력(C) 또는 배신(D)을 결정하는 완전한 랜덤 전략이다.  
//  
// 이 전략의 핵심은 어떠한 패턴도 없이 완전한 무작위성을 유지하는 것으로,  
// 상대가 어떤 전략을 사용하든 예측이 불가능한 방식으로 반응한다.  
//  
// 전략의 작동 방식:  
// - 매 라운드마다 50% 확률로 협력(C) 또는 배신(D)을 선택.  
// - 상대의 과거 행동이나 전략을 전혀 고려하지 않음.  
// - 완전한 예측 불가능성을 유지하여 상대가 공략하기 어렵게 만듦.  
//  
// 장점:  
// - 상대가 패턴을 분석하려 해도, 일정한 규칙이 없기 때문에 조작당할 가능성이 적다.  
// - 단순한 패턴 기반 전략(Tit-for-Tat, Pavlov)과 만나면 상대를 혼란스럽게 만들 수 있다.  
// - 특정 전략과 만나면, 예상치 못한 이득을 볼 수도 있다.  
//  
// 단점:  
// - 협력 관계를 유지할 방법이 없으며, 완전히 운에 의해 결정된다.  
// - 상대가 보복형 전략(Tit-for-Tat, Grim Trigger)일 경우, 지속적으로 손해를 볼 수 있다.  
// - 장기적인 전략적 사고가 전혀 없기 때문에, 안정적인 승률을 기대하기 어렵다.  
//  
// 실제 사회에서 이 전략을 따르는 인간 유형은 "예측 불가능한 광인" 또는 "무작위적 혼돈의 창조자"이다.  
// 이들은 규칙 없이 행동하며, 일관된 원칙이나 전략 없이 즉흥적인 선택을 하는 성향을 가진다.  
// 이러한 성향은 상대에게 혼란을 주고 특정 상황에서는 유리할 수도 있지만,  
// 장기적으로 신뢰를 형성하기 어렵고, 협력 관계를 구축하기 어려운 단점이 있다.  

public class RandomStrategy implements Strategy {
    private final Random random = new Random(); // 랜덤 객체 (무작위 결정)

    @Override
    public boolean choose(Player self, Player opponent, List<Boolean> opponentHistory) {
        // 무조건 50% 확률로 협력(C) 또는 배신(D)
        return random.nextBoolean();
    }

    @Override
    public Strategy cloneStrategy() {
        return new RandomStrategy();
    }
}