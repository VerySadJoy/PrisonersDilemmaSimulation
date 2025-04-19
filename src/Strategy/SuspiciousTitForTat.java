package Strategy;

import java.util.List;

/**
 * 전략 이름: SuspiciousTitForTat  
 * 전략 유형: 보복형 (경계심이 강한 의심쟁이, 신뢰를 테스트하는 현실주의자)
 * 
 * 전략 개요:  
 * - 일반적인 Tit-for-Tat의 변형 버전  
 * - 첫 턴에서 무조건 배신(D)하여 상대의 성향을 탐색한 후  
 * - 이후에는 상대의 마지막 행동을 그대로 따라가는 구조
 * 
 * 작동 방식:  
 * - 1라운드: 무조건 배신(D)  
 * - 2라운드~: 상대의 마지막 행동을 따라감 (Tit-for-Tat)
 * 
 * 장점:  
 * - 상대가 협력가인지 보복형인지 빠르게 판단 가능  
 * - 보복형과도 빠르게 동기화 가능하여, 불필요한 루프 없이 균형 유지  
 * - Always Cooperate 같은 전략을 초반에 착취 가능
 * 
 * 단점:  
 * - 협력 지향 전략과 만나면 초반 배신으로 신뢰를 깨뜨릴 수 있음  
 * - Forgiving 전략에게는 괜히 한 번 쏘아붙인 결과로 불리해질 수 있음  
 * - 첫 인상이 너무 나빠서 협력 관계 형성이 늦어질 수 있음
 * 
 * 인간 유형 대응:  
 * - 처음엔 믿지 않아, 증명해봐라는 신중하고 방어적인 인간상  
 * - 진심이 통하면 끝까지 같이 가지만,  
 * - 그 전에 한 번은 찔러보는 타입. 말하자면 초반 테스트형 현실주의자
 * - 경계심이 높아서 실수하면 복구하기 힘든 관계가 되기도 함
*/  

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