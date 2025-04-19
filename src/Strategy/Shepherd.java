package Strategy;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 전략 이름: Shepherd  
 * 전략 유형: 협력형 + 보복형 (온화하지만 단호한 인도자, 조용한 복수자)
 *
 * 전략 개요:  
 * - 기본적으로는 협력을 유지하지만  
 * - 상대가 배신하면 2턴간 보복한 뒤 다시 협력으로 복귀  
 * - 감정적인 반응 없이 절제된 복수를 실행하는 전략
 *
 * 작동 방식:  
 * - 1R: 무조건 협력(C)  
 * - 이후:  
 *   - 상대가 협력(C) → 나도 계속 협력  
 *   - 상대가 배신(D) → 복수 카운트 +2  
 *   - 복수 카운트 > 0이면 → 계속 배신(D), 카운트 1씩 감소  
 *   - 복수가 끝나면 다시 협력(C)  
 *
 * 장점:  
 * - 실수로 인한 배신도 관계 회복 가능  
 * - 지나치게 강하지 않아 다양한 협력형 전략과 공존 가능  
 * - 교착에 빠지지 않고 협력 루프를 유도함
 *
 * 단점:  
 * - 주기적 배신으로 카운트 조절하는 교활한 전략에게 취약  
 * - Forgiving 전략처럼 착취당할 가능성 존재  
 * - 랜덤형 상대와는 효과적인 보복이 어려울 수 있음
 *
 * 인간 유형 대응:  
 * - 한 번은 참아주고, 두 번은 참고 보복한다는 사람  
 * - 실수를 용서하되, 반복되는 패턴에는 단호한 태도  
 * - 인간관계에서 신뢰 중심이나, 잘못된 행동은 기록하며 균형 있게 조율  
 * - 무섭진 않지만, 무시할 수도 없는 조용한 권위자
*/ 

public class Shepherd implements Strategy {
    private final Map<Player, Integer> betrayalCount = new ConcurrentHashMap<>(); // 상대가 배신했을 때 복수 카운트
    
    @Override
    public boolean choose(Player self, Player opponent, List<Boolean> opponentHistory) {
        // 만약 상대와 처음 만나는 거라면 기본적으로 협력
        if (opponentHistory.isEmpty()) {
            return true;
        }
        
        // 상대의 마지막 행동 가져오기
        boolean opponentLastMove = opponentHistory.get(opponentHistory.size() - 1);
        
        // 상대가 배신했다면 복수 카운트를 2 증가
        if (!opponentLastMove) {
            betrayalCount.put(opponent, betrayalCount.getOrDefault(opponent, 0) + 2);
        }
        
        // 현재 복수 중이라면 배신 (복수 카운트를 줄여가면서)
        if (betrayalCount.getOrDefault(opponent, 0) > 0) {
            betrayalCount.put(opponent, betrayalCount.get(opponent) - 1);
            return false; // 배신
        }
        
        // 기본적으로 협력
        return true;
    }

    @Override
    public Strategy cloneStrategy() {
        return new Shepherd();
    }
}