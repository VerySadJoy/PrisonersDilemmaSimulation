package Strategy;

import java.util.List;

/**
 * 전략 이름: TitForTat  
 * 전략 유형: 보복형 + 협력형 (신뢰와 복수의 균형자, 가장 유명한 공정한 대응자)
 * 
 * 전략 개요:  
 * - 상대가 협력하면 나도 협력,  
 * - 상대가 배신하면 나도 배신  
 * - 단순하지만 강력한 대응 기반 전략  
 * 
 * 작동 방식:  
 * - 1라운드: 무조건 협력 (C)  
 * - 2라운드~: 상대의 직전 행동을 따라함
 * 
 * 장점:  
 * - 협력 환경에서 뛰어난 성능 (최적의 전략 중 하나)  
 * - 배신자에겐 즉각 보복 → 최소 손해  
 * - 실수도 용서 가능 (상대가 협력만 유지하면 바로 회복)
 * 
 * 단점:  
 * - 유연성이 낮아 랜덤/불안정한 전략과 반복 갈등  
 * - Forgiving Tit-for-Tat보다 보복이 빠르고 냉정함  
 * - C-D-C-D처럼 반복 패턴일 경우 무한 충돌 가능
 * 
 * 인간 유형 대응:  
 * - 너가 잘하면 나도 잘해줄게라는 조건부 신뢰주의자  
 * - 공정함을 중시하지만, 감정적 유연성은 낮음  
 * - 응징과 화해의 밸런스를 정확히 맞추려는 정의형 플레이어  
 * - 신뢰받기 쉽지만, 실수에도 민감해 관계가 쉽게 틀어질 수도 있음
*/

public class TitForTat implements Strategy {

    @Override
    public boolean choose(Player self, Player opponent, List<Boolean> opponentHistory) {
        // 첫 번째 라운드는 기본적으로 협력(C)
        if (opponentHistory.isEmpty()) {
            return true;
        }

        // 상대의 마지막 행동을 따라감 (협력(C) 또는 배신(D))
        return opponentHistory.get(opponentHistory.size() - 1);
    }
    
    @Override
    public Strategy cloneStrategy() {
        return new TitForTat();
    }
}