package Strategy;

import java.util.List;

// (보복형, 배신형) 흑백논리 신봉자, 극단적 계산가  
//  
// BinaryThinking 전략은 상대의 협력 비율이 50%를 넘으면 무조건 협력(C),  
// 50% 이하이면 무조건 배신(D)하는 단순하지만 강력한 논리를 따르는 전략이다.  
//  
// 이 전략의 핵심은 절대적인 기준(50%)을 통해 상대를 협력할 가치가 있는지 아닌지 판단하는 것으로,  
// 판단이 내려지면 더 이상 상대의 개별적인 행동을 고려하지 않는다.  
//  
// 장점:  
// - 상대가 협력적이라면 끝까지 협력하여 높은 보상을 유지할 수 있다.  
// - 배신하는 상대에게는 단호하게 맞배신하여 손해를 최소화한다.  
// - 연산이 간단하고, 협력과 배신을 명확히 구분하기 때문에 예측 가능성이 높다.  
//  
// 단점:  
// - 상대의 협력 비율이 50% 근처에서 변동하면, 전략이 불안정하게 작동할 가능성이 크다.  
// - 상대가 일부러 50% 근처에서 협력과 배신을 조정하면 쉽게 조종당할 수 있다.  
// - 상대의 전략적 변화를 반영하지 않기 때문에, 상황 변화에 둔감하다.  
//  
// 실제 사회에서 이 전략을 따르는 인간 유형은 "흑백논리 신봉자" 또는 "극단적 계산가"이다.  
// 사람을 철저히 "믿을 수 있는 자"와 "믿을 수 없는 자"로 나눈다.
// 또한, 협력을 하더라도 상대의 성향에 의해 결정될 뿐, 자신의 판단을 수정하지 않는다.  
// 이러한 성향은 일관성이 있지만, 융통성이 부족하고 상대에게 쉽게 간파당할 위험이 있다.  

public class BinaryThinking implements Strategy {
    @Override
    public boolean choose(Player self, Player opponent, List<Boolean> opponentHistory) {
        // 상대방의 행동 기록이 비어 있다면 (즉, 첫 라운드라면) 무조건 배신(D) 선택
        if (opponentHistory.isEmpty()) {
            return false;
        }

        // 동기화된 블록에서 안전하게 접근
        double cooperationRate;

        long cooperationCount = opponentHistory.stream().filter(b -> b).count();
        cooperationRate = (double) cooperationCount / opponentHistory.size();
        

        // 협력 비율이 50% 이하라면 무조건 배신(D)
        // 협력 비율이 50%를 초과하면 무조건 협력(C)
        return cooperationRate > 0.5;
    }
    
    @Override
    public Strategy cloneStrategy() {
        return new BinaryThinking();
    }
}