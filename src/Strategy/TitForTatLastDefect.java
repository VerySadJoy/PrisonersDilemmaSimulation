package Strategy;

import java.util.List;

// (보복형, 배신형)  
//  
// TitForTatLastDefect 전략은 일반적인 Tit-for-Tat 전략과 동일하지만,  
// 마지막 라운드에는 무조건 배신(D)하는 특징을 갖는다.  
//  
// 이 전략은 협력적인 대응을 유지하면서도,  
// 게임이 종료되는 시점에서는 상대에게 협력을 기대하지 않고 이득을 극대화하려 한다.  
//  
// 전략의 작동 방식:  
// - 첫 번째 라운드는 무조건 협력(C)하여 상대가 협력할 기회를 준다.  
// - 이후부터는 상대의 마지막 행동을 그대로 따라간다.  
// - 마지막 라운드에서는 무조건 배신(D)하여 이득을 극대화한다.  
//  
// 장점:  
// - 협력적인 환경에서도 안정적인 성과를 낼 수 있다.  
// - 마지막 라운드에서 상대가 협력할 경우, 배신으로 추가 점수를 획득할 수 있다.  
//  
// 단점:  
// - 마지막 라운드에서 무조건 배신하기 때문에, 상대가 이를 예측하면 협력을 포기할 수도 있다.  
// - 일반적인 Tit-for-Tat보다 덜 신뢰받을 가능성이 있다.  
//  
// 실제 사회에서 이 전략을 따르는 인간 유형은 "협력하지만 마지막에는 이득을 취하는 현실주의자"이다.  
//  

public class TitForTatLastDefect implements Strategy {

    @Override
    public boolean choose(Player self, Player opponent, List<Boolean> opponentHistory) {
        // 마지막 라운드라면 무조건 배신(D)
        if (opponentHistory.size() >= 499) {
            return false;
        }

        // 첫 번째 라운드는 기본적으로 협력(C)
        if (opponentHistory.isEmpty()) {
            return true;
        }

        // 상대의 마지막 행동을 따라감 (협력(C) 또는 배신(D))
        return opponentHistory.get(opponentHistory.size() - 1);
    }

    @Override
    public Strategy cloneStrategy() {
        return new TitForTatLastDefect();
    }
}