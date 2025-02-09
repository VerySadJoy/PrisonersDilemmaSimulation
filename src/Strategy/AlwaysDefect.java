package Strategy;

import java.util.List;
// (배신형, 착취형) 냉혈한 기회주의자, 탐욕스러운 생존주의자  
//  
// AlwaysDefect 전략은 상대가 협력하든 배신하든, 어떤 상황에서도 항상 배신(D)만 선택하는 전략이다.  
// 어떠한 신뢰도 형성하지 않으며, 상대를 무조건적으로 이용하는 방식으로 작동한다.  
//  
// 장점:  
// - 협력형 전략(AlwaysCooperate, Generous Tit-for-Tat)과 만나면 지속적으로 이득을 취할 수 있다.  
// - 보복형 전략과 만나더라도 처음에는 이득을 보며, 상대가 대응하기 전까지 최대한 착취할 수 있다.  
// - 짧은 기간 동안 플레이할 경우, 빠르게 높은 점수를 획득할 가능성이 크다.  
//  
// 단점:  
// - 보복형 전략(Tit-for-Tat, Grim Trigger)과 만나면 빠르게 패배한다.  
// - 협력적인 사회에서는 왕따를 당하며, 장기적으로는 생존하기 어려울 수 있다.  
// - 협력 관계가 중요한 게임 환경에서는 오히려 손해를 볼 가능성이 높다.  
//  
// 실제 사회에서 이 전략을 따르는 인간 유형은 "냉혈한 기회주의자" 또는 "탐욕스러운 생존주의자"이다.  
// 이들은 단기적인 이익을 극대화하는 데 집중하며, 자신의 이득을 위해 타인을 거리낌 없이 이용한다.  
// 그러나 신뢰를 기반으로 한 장기적인 관계 형성이 불가능하며, 결국 공동체에서 배척될 가능성이 크다.  
// 사회적 규범이나 도덕을 고려하지 않는 이기적인 성향을 지닌 경우가 많다.  

public class AlwaysDefect implements Strategy {

    @Override
    public boolean choose(Player self, Player opponent, List<Boolean> opponentHistory) {
        // 항상 배신 (D)을 선택함
        return false;
    }
    
    @Override
    public Strategy cloneStrategy() {
        return new AlwaysDefect();
    }
}