package Strategy;

import java.util.List;

/**
 * 전략 이름: TitForTatLastDefect  
 * 전략 유형: 보복형 + 배신형 (협력하지만 마지막엔 이득을 취하는 현실주의자)
 * 
 * 전략 개요:  
 * - 기본적으로는 Tit-for-Tat과 동일하게 작동하지만,  
 * - 마지막 라운드에서 무조건 배신(D)하여 이득을 극대화하는 현실주의적 전략  
 * 
 * 작동 방식:  
 * - 1라운드: 무조건 협력 (C)  
 * - 2~499라운드: 상대의 직전 행동을 그대로 따라함  
 * - 500라운드(마지막): 무조건 배신 (D)로 마무리
 * 
 * 장점:  
 * - 대부분의 라운드에서 협력 유지로 관계 안정화 가능  
 * - 마지막 턴에서 상대가 협력할 경우, 일방적 이득 확보 가능  
 * - 현실적인 손익 계산을 반영하여 효율적 플레이 가능
 * 
 * 단점:  
 * - 일반 Tit-for-Tat과 동일하게 반복 갈등 구조에 취약  
 * 
 * 인간 유형 대응:  
 * - 대부분은 공정하게 대하지만, 마지막 순간엔 실리부터 본다는 현실적 실용주의자  
 * - 전체적인 협력은 유지하지만, 끝이 아름답지 않은 관계를 만드는 타입  
 * - 사회적 평판보단 최종 이득을 우선시하는 성향
*/  

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