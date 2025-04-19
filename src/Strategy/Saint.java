package Strategy;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 전략 이름: Saint  
 * 전략 유형: 협력형 + 보복형 (성인군자, 한계를 넘어선 인내자)
 *
 * 전략 개요:  
 * - 초반에는 무조건 협력(C)을 유지  
 * - 상대가 10번 이상 배신하면 → Tit-for-Tat 보복 모드로 전환  
 * - 한계를 넘어선 인내 후 냉정한 대응으로 전환하는, 극도로 관대한 전략
 *
 * 작동 방식:  
 * - 상대의 배신 횟수 < 10 → 무조건 협력  
 * - 상대의 배신 횟수 ≥ 10 → Tit-for-Tat 적용 (상대가 배신하면 나도 배신)
 *
 * 장점:  
 * - Forgiving Tit-for-Tat보다 더 너그러움 (10번 참음!)  
 * - 실수 많은 전략이나 비협력적 상대에게도 회복 기회를 줌  
 * - 협력 유지력이 매우 높아 장기적 관계에 강함
 *
 * 단점:  
 * - Always Defect 전략에게는 10번이나 손해 본 후 대응하므로 손실이 큼  
 * - 상대가 이 전략을 파악하면 10회 배신 후 협력으로 착취 가능  
 * - 랜덤형 전략에게는 헛된 인내가 될 수도 있음
 *
 * 인간 유형 대응:  
 * - 무한 인내는 아니지만 거의 부처님급
 * - 상대가 실수하거나 악의가 있더라도, 마지막 순간까지 참고 기다리는 인물  
 * - 하지만 한계를 넘어서면 놀라운 냉정함으로 전환  
 * - 관계 지향적이지만, 너무 참다가 이용당할 수 있는 리스크 있는 성향
*/

public class Saint implements Strategy {
    private final ConcurrentHashMap<Player, Integer> betrayals = new ConcurrentHashMap<>();
    private final int TOLERANCE = 10; // 배신을 10번까지는 참음

    @Override
    public boolean choose(Player self, Player opponent, List<Boolean> opponentHistory) {
        betrayals.putIfAbsent(opponent, 0);

        if (opponentHistory.isEmpty()) {
            return true; // 처음엔 무조건 협력
        }

        if (!opponentHistory.get(opponentHistory.size() - 1)) {
            betrayals.put(opponent, betrayals.get(opponent) + 1);
        }

        // 상대가 10번 이상 배신했으면 Tit-for-Tat 모드 돌입
        if (betrayals.get(opponent) >= TOLERANCE) {
            return opponentHistory.get(opponentHistory.size() - 1);
        }

        return true; // 평소에는 무조건 협력
    }

    @Override
    public Strategy cloneStrategy() {
        return new Saint();
    }
}