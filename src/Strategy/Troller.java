package Strategy;

import java.util.List;
import java.util.Random;

// (랜덤형, 협력형) 감정적인 기분파, 예측할 수 없는 장난꾸러기  
//  
// Troller 전략은 상대의 행동에 따라 협력과 배신을 선택하지만,  
// 확률적으로 변칙적인 반응을 보이는 감정적이고 예측 불가능한 전략이다.  
//  
// 이 전략의 핵심은 ‘상대가 협력하면 높은 확률로 협력하지만,  
// 상대가 배신하면 낮은 확률로 배신하며, 전반적으로 랜덤한 요소를 유지하는 것’이다.  
// 즉, 상대의 행동을 어느 정도 따르지만 완전히 일관되게 반응하지는 않는다.  
//  
// 전략의 작동 방식:  
// - 첫 번째 라운드는 랜덤하게 협력(C) 또는 배신(D) 선택.  
// - 상대가 협력하면 75% 확률로 협력(C), 25% 확률로 배신(D).  
// - 상대가 배신하면 25% 확률로 협력(C), 75% 확률로 배신(D).  
//  
// 장점:  
// - 완전한 보복형 전략(Tit-for-Tat, Grim Trigger)과 만나면,  
//   확률적으로 협력을 유지할 가능성이 있어 극단적인 싸움으로 치닫지 않을 수 있다.  
// - 랜덤성이 포함되어 있어 상대가 패턴을 분석하기 어렵다.  
// - 협력 성향이 어느 정도 있어, 순수한 협력가(Always Cooperate)와도 협력 가능성이 존재.  
//  
// 단점:  
// - Forgiving Tit-for-Tat 같은 전략과 만나면, 불필요한 배신으로 인해 협력 관계를 망칠 수 있다.  
// - 보복형 전략과 만나면 상대가 지속적으로 배신을 선택할 가능성이 높아져 손해를 볼 수 있다.  
// - 장기적인 관점에서 신뢰를 얻기 어려우며, 랜덤성이 강하기 때문에 안정적인 협력 관계를 구축하기 힘들다.  
//  
// 실제 사회에서 이 전략을 따르는 인간 유형은 "감정적인 기분파" 또는 "예측할 수 없는 장난꾸러기"이다.  
// 이들은 상대의 행동에 영향을 받지만, 기분이나 상황에 따라 일정하지 않은 반응을 보인다.  
// 이러한 성향은 때때로 유리하게 작용할 수 있지만,  
// 협력 관계를 맺기 어렵고, 신뢰를 쌓기 어려운 문제점을 초래할 수도 있다.  

public class Troller implements Strategy {
    private final Random random = new Random();

    @Override
    public boolean choose(Player self, Player opponent, List<Boolean> opponentHistory) {
        if (opponentHistory.isEmpty()) {
            return random.nextBoolean(); // 첫 라운드는 랜덤
        }

        boolean opponentLastMove = opponentHistory.get(opponentHistory.size() - 1);

        if (opponentLastMove) {
            return random.nextDouble() < 0.75; // 상대가 협력하면 75% 확률로 협력
        } else {
            return random.nextDouble() < 0.25; // 상대가 배신하면 25% 확률로 배신
        }
    }

    @Override
    public Strategy cloneStrategy() {
        return new Troller();
    }
}