package Strategy;

import java.util.List;
import java.util.Random;

// (확률적 협력형) 대체로 선한 자
//  
// 이 전략은 75% 확률로 협력(C), 25% 확률로 배신(D)을 선택하는 방식으로 동작한다.  
// 이는 기본적인 랜덤 전략에서 협력 확률을 높인 형태로,  
// 상대와의 협력을 유지하려는 성향이 강하다.  
//  
// 전략의 작동 방식:  
// - 매 라운드마다 75% 확률로 협력(C), 25% 확률로 배신(D)을 선택.  
// - 상대의 과거 행동이나 전략을 전혀 고려하지 않음.  
// - 기본적으로 협력을 선호하지만, 예측 불가능성을 약간 유지.  
//  
// 장점:  
// - 협력을 기반으로 하는 전략(Tit-for-Tat, Pavlov)과 만나면 좋은 결과를 얻을 가능성이 높다.  
// - 완전한 랜덤 전략보다 협력 가능성이 높아, 신뢰를 형성할 여지가 있다.  
// - 일정 수준의 예측 불가능성을 유지하여 상대가 완벽하게 조작하기 어려움.  
//  
// 단점:  
// - 특정 전략(Tit-for-Tat, Grim Trigger)과 만나면, 한 번의 배신이 지속적인 손해로 이어질 수도 있다.  
// - 장기적인 전략적 사고가 부족하여, 최적의 플레이를 보장하지 않는다.  
//  
// 실제 사회에서 이 전략을 따르는 인간 유형은 "기본적으로 선하지만 때때로 배신하는 유형"이다.  
// 이들은 협력을 선호하지만, 가끔 예측할 수 없는 배신을 통해 상대를 당황하게 만들 수 있다.  
// 이러한 성향은 상대와 신뢰를 쌓을 가능성이 있으나, 100% 신뢰받기는 어려울 수도 있다.  

public class GoodPerson implements Strategy {
    private final Random random = new Random();

    @Override
    public boolean choose(Player self, Player opponent, List<Boolean> opponentHistory) {
        // 75% 확률로 협력(true), 25% 확률로 배신(false)
        return random.nextDouble() < 0.75;
    }

    @Override
    public Strategy cloneStrategy() {
        return new GoodPerson();
    }
}