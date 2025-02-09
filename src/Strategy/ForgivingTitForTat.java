package Strategy;

import java.util.List;

// (보복형, 협력형) 신중한 용서자, 현실적인 화해주의자  
//  
// Forgiving Tit-for-Tat 전략은 상대가 한 번 배신하는 것은 용서하지만,  
// 두 번 연속으로 배신하면 보복하는 보다 관대한 형태의 Tit-for-Tat 전략이다.  
//  
// 이 전략의 핵심은 ‘신뢰와 보복의 균형’으로, 협력 관계를 유지하려 하지만  
// 반복적인 배신에는 강경하게 대응하는 것이다.  
// 기본 Tit-for-Tat 전략보다 상대가 실수로 배신했을 때 관계를 쉽게 회복할 수 있다.  
//  
// 장점:  
// - 실수로 한 번 배신한 상대와도 협력 관계를 지속할 수 있어, 협력 유지력이 강하다.  
// - 보복형 전략이지만 너무 가혹하지 않아, 보복과 협력의 균형을 유지한다.  
// - 협력적인 환경에서는 높은 점수를 얻을 가능성이 크다.  
//  
// 단점:  
// - 상대가 의도적으로 한 번씩 배신(D, C, D, C)하면 계속 협력할 수밖에 없어서 착취당할 위험이 있다.  
// - 완전한 보복형 전략(Tit-for-Tat, Grim Trigger)보다 배신자를 억제하는 힘이 약할 수 있다.  
// - 상대가 배신을 반복적으로 하지만 한 번씩 협력을 섞으면 대응이 어렵다.  
//  
// 실제 사회에서 이 전략을 따르는 인간 유형은 "신중한 용서자" 또는 "현실적인 화해주의자"이다.  
// 이들은 한 번의 실수는 받아들이고 상대에게 기회를 주지만,  
// 반복적인 배신에는 단호하게 보복하는 신뢰와 현실주의가 섞인 성향을 가진다.  
// 그러나 지나치게 관대할 경우 교활한 사람에게 이용당할 위험이 있다.  

public class ForgivingTitForTat implements Strategy {

    @Override
    public boolean choose(Player self, Player opponent, List<Boolean> opponentHistory) {
        // 첫 번째 라운드는 기본적으로 협력(C)
        if (opponentHistory.isEmpty()) {
            return true;
        }

        int size = opponentHistory.size();

        // 상대가 단 한 번 배신했을 경우 → 용서하고 협력
        if (size == 1) {
            return true;
        }
        // 상대가 직전 두 번 연속 배신(D, D) 했다면 → 보복 (배신)
        // 기본적으로 협력 유지

        return !(!opponentHistory.get(size - 1) && !opponentHistory.get(size - 2));
    }
    
    @Override
    public Strategy cloneStrategy() {
        return new ForgivingTitForTat();
    }
}