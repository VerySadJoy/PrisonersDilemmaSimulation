package Strategy;

import java.util.List;

// (기회주의형) 계산된 배신자, 상황을 보고 움직이는 교활한 협력자
//  
// Opportunist 전략은 기본적으로 협력을 유지하지만,  
// 상대가 협력적인 태도를 보일 때 예상치 못한 순간에 배신하여 이득을 취하는 전략이다.  
//  
// 전략의 작동 방식:  
// - 첫 번째 라운드는 무조건 협력 (C).  
// - 직전 라운드에서 나와 상대가 모두 협력(C)했다면, 배신(D).  
// - 그 외의 경우에는 모두 협력(C).  
//  
// 장점:  
// - 상대가 지속적으로 협력하는 경우, 적절한 순간에 배신하여 최대한의 이득을 취할 수 있다.  
// - 보복형 전략(Tit-for-Tat, Grim Trigger)과 만나도 초반에는 협력하며 신뢰를 유지할 수 있다.  
// - 상대가 배신 성향이라면, 지속적으로 협력하여 불필요한 손해를 피할 수 있다.  
//  
// 단점:  
// - 상대가 빠르게 패턴을 분석하면, 기회주의적 배신이 반복될 경우 협력 관계가 깨질 수 있다.  
// - Forgiving Tit-for-Tat 같은 전략과 만나면 불필요한 배신이 반복될 수도 있다.  
//  
// 실제 사회에서 이 전략을 따르는 인간 유형은 "계산된 배신자" 또는 "상황을 보고 움직이는 교활한 협력자"이다.  
// 이들은 협력하는 척하다가 결정적인 순간에 배신하여 이득을 극대화하려 한다.  
// 하지만 너무 빈번하게 배신하면 신뢰를 잃고 협력 관계가 깨질 위험이 있다.  

public class Opportunist implements Strategy {

    @Override
    public boolean choose(Player self, Player opponent, List<Boolean> opponentHistory) {
        // 첫 번째 라운드는 무조건 협력 (C)
        if (opponentHistory.isEmpty()) {
            return true;
        }

        // 직전 라운드 확인 (내 행동은 리스트 마지막, 상대 행동은 리스트에서 마지막-1)
        int lastRound = opponentHistory.size() - 1;
        // 이전 라운드에서 나와 상대가 둘 다 협력(C)했다면 배신(D)
        // 그 외의 경우 모두 협력 (C)
        return !(lastRound > 0 && opponentHistory.get(lastRound) && self.getMyLastHistory(opponent));
    }

    @Override
    public Strategy cloneStrategy() {
        return new Opportunist();
    }
}