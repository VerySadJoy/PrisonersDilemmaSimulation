package Strategy;

import java.util.List;
// (랜덤형, 착취형) 교활한 중개인, 끊임없이 균형을 맞추려는 거래자  
//  
// Merchant 전략은 상대의 직전 행동과 반대되는 행동을 취하는 변칙적인 전략이다.  
// 상대가 협력하면 배신하고, 상대가 배신하면 협력하여  
// 협력과 배신 사이에서 균형을 맞추려는 듯한 태도를 보인다.  
//  
// 이 전략의 핵심은 ‘상대와 동일한 선택을 피하면서,  
// 상대가 협력할 경우 이익을 극대화하고,  
// 상대가 배신할 경우 최소한의 피해를 입는 것’이다.  
//  
// 전략의 작동 방식:  
// - 첫 번째 라운드는 무조건 배신(D).  
// - 상대가 협력하면 즉시 배신하여 이득을 취함.  
// - 상대가 배신하면 협력하여 신뢰를 줄 듯한 모습을 보임.  
// - 결과적으로 상대와 항상 반대되는 선택을 하게 되어 일정한 예측 가능성을 가짐.  
//  
// 장점:  
// - 협력형 전략과 만나면 지속적으로 높은 이득을 얻을 가능성이 크다.  
// - 상대가 보복형 전략이라면, 일정한 균형을 유지하면서 협력과 배신을 조정할 수 있다.  
// - 보복형 전략이 아닌 랜덤형 전략과도 비교적 유리한 성과를 낼 수 있다.  
//  
// 단점:  
// - Tit-for-Tat과 같은 보복 전략과 만나면, 끊임없이 배신과 협력을 반복하며 제자리걸음을 하게 된다.  
// - 상대가 자신의 패턴을 분석하면, 일정한 주기로 협력과 배신이 반복되는 점을 이용해 조작당할 가능성이 있다.  
// - 완전한 배신 전략(Always Defect)과 만나면 협력을 유지하려다 오히려 손해를 볼 수도 있다.  
//  
// 실제 사회에서 이 전략을 따르는 인간 유형은 "교활한 중개인" 또는 "끊임없이 균형을 맞추려는 거래자"이다.  
// 이들은 상대의 행동에 반응하여 거래를 조정하는 듯한 모습을 보이지만,  
// 실질적으로는 신뢰를 형성하기 어렵고, 장기적인 관계보다는 단기적인 이득을 우선시한다.  
// 그러나 이러한 패턴이 반복되면 결국 상대에게 간파당하여 공략당할 가능성이 높다.  

public class Merchant implements Strategy {
    private boolean lastMove; // 이전에 내가 했던 선택 (true = 협력, false = 배신)

    @Override
    public boolean choose(Player self, Player opponent, List<Boolean> opponentHistory) {
        if (opponentHistory.isEmpty()) {
            return false; // 첫 턴에는 초기값 사용
        }

        boolean opponentLastMove = opponentHistory.get(opponentHistory.size() - 1);
        lastMove = !opponentLastMove; // 상대가 협력하면 배신, 배신하면 협력
        return lastMove;
    }

    @Override
    public Strategy cloneStrategy() {
        return new Merchant(); // 새 전략 인스턴스 생성
    }
}