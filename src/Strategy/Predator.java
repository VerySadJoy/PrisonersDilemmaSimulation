package Strategy;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 전략 이름: Predator  
 * 전략 유형: 배신형, 상황적 협력형 (강자를 피하고 약자를 사냥하는 포식자)  
 * 
 * 전략 개요:  
 * - 협력적인 성향의 상대는 지속적으로 배신하고 착취  
 * - 보복형 또는 비협력적인 상대는 협력하며 갈등 회피  
 * 
 * 작동 방식:  
 * - 첫 번째 라운드는 무조건 배신 (D)  
 * - 이후 상대의 협력률이 80% 이상 → 순수 협력가로 간주 → 계속 배신 (D)  
 * - 그 외 → 보복 가능성이 있다고 판단 → 협력 (C)  
 * 
 * 장점:  
 * - 완전한 협력가(Always Cooperate)를 완전히 이용할 수 있음  
 * - 보복형 전략에게는 협력적으로 접근하여 손해를 최소화  
 * - 계산적이고 안정적인 이득 추구 가능  
 * 
 * 단점:  
 * - 협력형 전략과의 신뢰 형성이 어렵고, 장기 협력에 불리  
 * - Forgiving Tit-for-Tat과 같은 유연한 전략에게 들키면 역으로 착취당할 가능성  
 * - 인간적인 감성 없는 철저한 계산형 전략이므로 사회적 평판은 낮을 수 있음  
 * 
 * 인간 대응 유형:  
 * - 강자에게는 순응하고, 약자에겐 공격적인 기회주의자  
 * - 계산적으로 행동하지만, 신뢰를 잃기 쉬운 냉정한 생존 전략가  
 * - “이득이 없다면 협력하지 않는다”는 이기적 현실주의자  
 */

public class Predator implements Strategy {
    private final Map<Player, List<Boolean>> opponentHistories = new ConcurrentHashMap<>(); // 안전한 기록 저장
    private final Map<Player, Boolean> isCooperatorMap = new ConcurrentHashMap<>(); // 협력가 여부 저장

    @Override
    public boolean choose(Player self, Player opponent, List<Boolean> opponentHistory) {
        // 안전한 리스트 관리
        opponentHistories.putIfAbsent(opponent, new CopyOnWriteArrayList<>(opponentHistory));
        List<Boolean> history = opponentHistories.get(opponent);

        // 첫 번째 턴은 무조건 배신(D)
        if (history.isEmpty()) {
            return false;
        }

        long betrayals;
        synchronized (history) {
            betrayals = history.stream().filter(b -> !b).count();
        }

        boolean isCooperator = betrayals < history.size() * 0.2; // 80% 이상 협력하면 순수 협력가로 간주
        isCooperatorMap.put(opponent, isCooperator);

        return !isCooperatorMap.get(opponent); // 협력가면 계속 배신, 보복형이면 협력
    }
    
    @Override
    public Strategy cloneStrategy() {
        return new Predator();
    }
}