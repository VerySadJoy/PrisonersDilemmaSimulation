package Strategy;

import java.util.List;
import java.util.Random;

// (랜덤형, 보복형) 예측 불가능한 협력자 
//  
// NoisyTitForTat 전략은 일반적인 Tit-for-Tat 전략과 유사하지만,  
// 80% 확률로 상대의 마지막 행동을 따라가고,  
// 20% 확률로 반대 행동을 선택하는 변칙적인 성향을 가진다.  
//  
// 전략의 작동 방식:  
// - 첫 번째 라운드는 무조건 협력(C)하여 상대가 협력할 기회를 준다.  
// - 이후부터는 상대의 마지막 행동을 80% 확률로 따라가고, 20% 확률로 반대 행동을 선택한다.  
//  
// 장점:  
// - 일반적인 Tit-for-Tat보다 덜 예측 가능하여 특정 전략에게 유리할 수 있다.  
// - Forgiving Tit-for-Tat과 비슷한 성향을 가지며, 실수로 인한 배신을 용인할 가능성이 있음.  
//  
// 단점:  
// - 확률적 반응 때문에, 협력적인 상대에게 불필요한 배신을 할 가능성이 있음.  
// - 상대가 Tit-for-Tat일 경우, 예측 불가능한 노이즈로 인해 불필요한 갈등이 발생할 수도 있다.  
//  
// 실제 사회에서 이 전략을 따르는 인간 유형은 "실수를 가끔 하는 대응자" 또는 "예측 불가능한 협력자"이다.  
// 이들은 대체로 상대의 행동을 반영하지만, 가끔은 의도치 않은 변칙적인 행동을 하기도 한다.  
//  

public class NoisyTitForTat implements Strategy {
    private final Random random = new Random();

    @Override
    public boolean choose(Player self, Player opponent, List<Boolean> opponentHistory) {
        // 첫 번째 라운드는 무조건 협력(C)
        if (opponentHistory.isEmpty()) {
            return true;
        }

        // 상대의 마지막 행동을 가져옴
        boolean lastOpponentMove = opponentHistory.get(opponentHistory.size() - 1);

        // 80% 확률로 상대의 행동을 따라가고, 20% 확률로 반대 행동 선택
        return random.nextDouble() < 0.8 ? lastOpponentMove : !lastOpponentMove;
    }

    @Override
    public Strategy cloneStrategy() {
        return new NoisyTitForTat();
    }
}