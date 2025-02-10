package Strategy;

import java.util.List;
import java.util.Random;

// (확률적 배신형) 나쁜 사람
//  
// 이 전략은 75% 확률로 배신(D), 25% 확률로 협력(C)을 선택하는 방식으로 동작한다.  
// 이는 기본적인 랜덤 전략에서 배신 확률을 높인 형태로,  
// 상대방을 주로 이용하려는 성향이 강하다.  
//  
// 전략의 작동 방식:  
// - 매 라운드마다 75% 확률로 배신(D), 25% 확률로 협력(C)을 선택.  
// - 상대의 과거 행동이나 전략을 전혀 고려하지 않음.  
// - 기본적으로 배신을 선호하지만, 가끔 협력하여 예측 불가능성을 유지.  
//  
// 장점:  
// - 순진한 협력형 전략(All-C, Tit-for-Tat)과 만나면 높은 보상을 얻을 가능성이 있다.  
// - 상대가 신뢰를 주기 어렵기 때문에, 장기적으로 공격적인 플레이를 할 수 있다.  
// - 완전한 배신 전략(All-D)보다 가끔 협력하기 때문에, 상대를 더 혼란스럽게 만들 수 있다.  
//  
// 단점:  
// - 협력을 기반으로 한 전략(Tit-for-Tat, Pavlov)과 만나면 상대의 반격을 받을 수 있다.  
// - 한 번이라도 협력한 상대는 이후 배신을 예상하고 방어적인 전략을 취할 가능성이 높다.  
// - 신뢰를 구축하기 어렵고, 장기적인 협력을 유지하기 힘들다.  
//  
// 실제 사회에서 이 전략을 따르는 인간 유형은 "기본적으로 배신하지만 가끔 친절한 유형"이다.  
// 이들은 주로 기회주의적으로 행동하지만, 가끔 협력을 선택함으로써 상대를 방심하게 만들 수 있다.  
// 이러한 성향은 단기적으로 이득을 볼 수 있지만, 장기적으로 신뢰를 얻기 어려운 단점이 있다.  

public class BadPerson implements Strategy {
    private final Random random = new Random();

    @Override
    public boolean choose(Player self, Player opponent, List<Boolean> opponentHistory) {
        // 75% 확률로 배신(false), 25% 확률로 협력(true)
        return random.nextDouble() < 0.25;
    }

    @Override
    public Strategy cloneStrategy() {
        return new BadPerson();
    }
}
