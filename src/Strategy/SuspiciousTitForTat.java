package Strategy;

import java.util.List;

// (보복형) 경계심이 강한 의심쟁이, 신뢰를 테스트하는 현실주의자  
//  
// Suspicious Tit-for-Tat 전략은 기본적으로 Tit-for-Tat과 유사하지만,  
// 첫 번째 라운드에서 무조건 배신(D)을 선택하여 상대의 반응을 탐색하는 변형된 전략이다.  
//  
// 이 전략의 핵심은 ‘상대가 신뢰할 수 있는 존재인지 먼저 시험하고,  
// 이후에는 상대의 행동을 따라가는 것’이다.  
//  
// 전략의 작동 방식:  
// - 첫 번째 라운드는 무조건 배신(D) 하여 상대의 반응을 본다.  
// - 이후 상대의 마지막 행동을 그대로 따라가는 Tit-for-Tat 원칙을 따른다.  
//  
// 장점:  
// - 첫 턴에 상대가 보복형 전략인지 확인할 수 있어, 빠르게 정보를 얻을 수 있다.  
// - 완전한 협력가(Always Cooperate)와 만나면, 초반 손해를 제외하면 일반적인 Tit-for-Tat과 비슷하게 작동한다.  
// - 보복형 전략(Tit-for-Tat, Grim Trigger)과 만나면 초반 적대감을 불러일으킬 가능성이 있지만,  
//   이후 패턴을 맞추면서 균형을 유지할 수 있다.  
//  
// 단점:  
// - Forgiving Tit-for-Tat과 같은 전략과 만나면 불필요한 배신을 유발하여 협력 관계를 망칠 수도 있다.  
// - 첫 턴에서 배신을 선택하기 때문에, 협력 기반 전략과 만나면 신뢰를 잃고 불리한 관계를 형성할 수 있다.  
// - 상대가 이 전략을 인지하면, 첫 턴에만 협력하고 이후 보복형 패턴을 유지하는 방식으로 대응할 수도 있다.  
//  
// 실제 사회에서 이 전략을 따르는 인간 유형은 "경계심이 강한 의심쟁이" 또는 "신뢰를 테스트하는 현실주의자"이다.  
// 이들은 처음에는 상대를 믿지 않고,  
// 먼저 상대가 신뢰할 만한 존재인지 시험한 후,  
// 상대가 협력적이라면 협력을 지속하는 성향을 가진다.  
// 그러나 첫 턴 배신이 관계를 악화시킬 수 있어,  
// 불필요한 적을 만들 위험이 있다.  

public class SuspiciousTitForTat implements Strategy {

    @Override
    public boolean choose(Player self, Player opponent, List<Boolean> opponentHistory) {
        // 상대방의 행동 기록이 비어 있다면 (즉, 첫 라운드라면) 무조건 배신(D) 선택
        if (opponentHistory.isEmpty()) {
            return false;
        }

        // 이후부터는 상대방의 마지막 행동을 따라감 (팃포탯 원칙)
        return opponentHistory.get(opponentHistory.size() - 1);
    }
    
    @Override
    public Strategy cloneStrategy() {
        return new SuspiciousTitForTat();
    }
}