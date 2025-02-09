package Strategy;

import java.util.List;
// (보복형) 신뢰를 쉽게 주지 않는 강경한 복수자, 기억에 의존하는 응징자  
//  
// Hard Tit-for-Tat 전략은 일반적인 Tit-for-Tat보다 더 강한 보복 성향을 가지며,  
// 최근 3라운드 동안 단 한 번이라도 배신이 있었다면 즉시 배신하는 변형된 전략이다.  
//  
// 이 전략의 핵심은 ‘한 번의 배신을 즉각 보복하는 것이 아니라,  
// 상대가 일정 기간 동안 지속적으로 협력하는지를 확인한 후 협력 여부를 결정하는 것’이다.  
//  
// 전략의 작동 방식:  
// - 첫 번째 라운드는 무조건 협력(C).  
// - 최근 3라운드를 분석하여, 그중 하나라도 배신(D)이 포함되어 있으면 무조건 배신(D).  
// - 최근 3라운드가 모두 협력이면 협력(C) 유지.  
//  
// 장점:  
// - 일반적인 Tit-for-Tat보다 상대의 실수(한 번의 배신)를 어느 정도 용인할 수 있다.  
// - 상대가 협력형 전략이라면, 일정 기간 협력을 유지해야 신뢰를 얻을 수 있기 때문에  
//   상대가 쉽게 배신을 반복할 수 없게 만든다.  
// - 완전한 배신 전략(Always Defect)과 만나면 강력하게 보복하여 최소한의 손해로 대응할 수 있다.  
//  
// 단점:  
// - 상대가 보복형 전략(Tit-for-Tat, Forgiving Tit-for-Tat)일 경우,  
//   불필요한 배신이 반복되면서 손해를 볼 수도 있다.  
// - 상대가 협력을 하다가 실수로 한 번 배신했을 경우,  
//   3라운드 동안 배신해야 하기 때문에 협력 관계가 깨질 위험이 크다.  
// - 상대가 랜덤한 패턴(Probabilistic Tit-for-Tat, RandomStrategy)을 사용할 경우,  
//   필요 이상으로 배신할 가능성이 높아진다.  
//  
// 실제 사회에서 이 전략을 따르는 인간 유형은 "신뢰를 쉽게 주지 않는 강경한 복수자" 또는 "기억에 의존하는 응징자"이다.  
// 이들은 상대가 협력을 유지해야만 신뢰를 보내며,  
// 상대가 일정 기간 동안 배신을 하지 않아야만 협력 관계를 지속하려 한다.  
// 하지만 너무 보복적인 태도를 유지하면,  
// 상대가 실수했을 때도 관계를 복구하기 어렵게 만드는 단점이 있다.  

public class HardTitForTat implements Strategy {

    private static final int MEMORY_DEPTH = 3; // 최근 3라운드 기억

    @Override
    public boolean choose(Player self, Player opponent, List<Boolean> opponentHistory) {
        // 첫 번째 라운드에서는 무조건 협력 (C)
        if (opponentHistory.isEmpty()) {
            return true;
        }

        // 최근 3개의 행동을 확인
        int historySize = opponentHistory.size();
        int startIndex = Math.max(0, historySize - MEMORY_DEPTH);

        for (int i = startIndex; i < historySize; i++) {
            if (!opponentHistory.get(i)) { // 배신(D)이 하나라도 포함되면
                return false; // 배신 (D)
            }
        }

        // 최근 3번 모두 협력이면 협력 (C)
        return true;
    }

    @Override
    public Strategy cloneStrategy() {
        return new HardTitForTat();
    }
}