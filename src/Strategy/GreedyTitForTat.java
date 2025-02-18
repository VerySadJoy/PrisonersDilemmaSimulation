package Strategy;

import java.util.List;
import java.util.Random;

// (보복형, 착취형) 교활한 탐욕가, 신뢰를 배신하는 계산가  
//  
// Greedy Tit-for-Tat 전략은 기본적으로 Tit-for-Tat과 유사하게 작동하지만,  
// 협력을 선택해야 할 때 10% 확률로 배신하는 변형된 팃포탯 전략이다.  
//  
// 이 전략의 핵심은 기본적인 신뢰 관계를 유지하면서도,  
// 가끔 예상치 못한 배신을 섞어 상대를 착취하는 것이다.  
//  
// 보통의 Tit-for-Tat과 달리, 상대가 협력했더라도 일정 확률로 배신을 시도하여  
// 장기적인 협력 관계를 유지하면서도 이득을 극대화하려는 성향을 가진다.  
//  
// 장점:  
// - 협력 기반의 플레이를 유지하면서도, 가끔씩 배신하여 추가적인 이득을 볼 수 있다.  
// - 상대가 단순한 Tit-for-Tat 전략이라면, 배신을 섞어도 보복을 당하지 않고 협력 관계를 유지할 수 있다.  
// - 보복형 전략(Tit-for-Tat, Grim Trigger)과 대결할 때도 협력을 유지하면서 간헐적인 이득을 취할 수 있다.  
//  
// 단점:  
// - 상대가 배신을 감지하면 신뢰를 잃고 보복형 전략에 의해 쉽게 응징당할 수 있다.  
// - 장기적인 관계에서 상대가 신뢰를 거두면 협력 관계를 유지하기 어려워질 수 있다.  
// - 상대가 확률적 배신을 눈치채면, 더 강한 보복 전략을 사용하여 압박할 가능성이 있다.  
//  
// 실제 사회에서 이 전략을 따르는 인간 유형은 "교활한 탐욕가" 또는 "신뢰를 배신하는 계산가"이다.  
// 이들은 기본적으로 신뢰를 형성하려 하지만,  
// 틈을 노려 가끔씩 배신하여 자신이 얻을 수 있는 이득을 극대화하려 한다.  
// 하지만 이러한 행동이 반복되면 상대는 이를 간파하고,  
// 결국 더 큰 보복을 받거나 신뢰를 잃을 위험이 커진다.  

public class GreedyTitForTat implements Strategy {
    private final Random random = new Random(); // 배신할 확률 계산을 위한 랜덤 객체

    @Override
    public boolean choose(Player self, Player opponent, List<Boolean> opponentHistory) {
        // 첫 번째 라운드는 기본적으로 협력(C)
        if (opponentHistory.isEmpty()) {
            return true;
        }

        // 상대의 마지막 행동을 따라감 (기본적으로 팃포탯 방식)
        boolean shouldCooperate = opponentHistory.get(opponentHistory.size() - 1);

        // 협력을 하려는 상황이라면 10% 확률로 배신(D)
        if (shouldCooperate && random.nextDouble() < 0.1) {
            return false;
        }

        // 기본적으로 상대의 마지막 행동을 따라감
        return shouldCooperate;
    }
    
    @Override
    public Strategy cloneStrategy() {
        return new GreedyTitForTat();
    }
}