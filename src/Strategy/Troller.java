package Strategy;

import java.util.List;
import java.util.Random;

/**
 * 전략 이름: Troller  
 * 전략 유형: 랜덤형 + 협력형 (감정적인 기분파, 예측 불가능한 장난꾸러기)
 * 
 * 전략 개요:  
 * - 상대의 행동에 따라 협력 또는 배신을 선택하지만,  
 *   확률적으로 감정적인 듯한 랜덤 반응을 보임  
 * - 협력에는 비교적 우호적이며, 배신에도 유연하게 대응함  
 * 
 * 작동 방식:  
 * - 첫 번째 라운드: 랜덤하게 협력 또는 배신  
 * - 이후:  
 *   - 상대가 협력 → 75% 확률로 협력, 25% 확률로 배신  
 *   - 상대가 배신 → 25% 확률로 협력, 75% 확률로 배신  
 * 
 * 장점:  
 * - 보복형 전략과 만나도 완전한 파국을 피할 수 있음  
 * - 확률적 반응으로 인해 패턴 분석이 어려워서 예측 회피 가능  
 * - 상황에 따라 협력 유지 가능성 있음 (특히 초반 협력가 상대 시)  
 * 
 * 단점:  
 * - 신뢰 기반 협력 관계 형성이 매우 어려움  
 * - 랜덤성과 감정적 반응 때문에 전략적 일관성 부족  
 * - Forgiving 계열 전략에게도 불필요한 배신을 유발할 수 있음  
 * 
 * 인간 유형 대응:  
 * - 감정적인 기분파, 예측할 수 없는 장난꾸러기
 * - 감정 따라 행동하는 듯한 타입, 하지만 계산된 랜덤함이 숨어 있음  
 * - 트롤이라는 이름과 달리, 혼란스러울 뿐 악의적이지는 않은 전략  
*/

public class Troller implements Strategy {
    private final Random random = new Random();

    @Override
    public boolean choose(Player self, Player opponent, List<Boolean> opponentHistory) {
        if (opponentHistory.isEmpty()) {
            return random.nextBoolean(); // 첫 라운드는 랜덤
        }

        boolean opponentLastMove = opponentHistory.get(opponentHistory.size() - 1);

        if (opponentLastMove) {
            return random.nextDouble() < 0.75; // 상대가 협력하면 75% 확률로 협력
        } else {
            return random.nextDouble() < 0.25; // 상대가 배신하면 25% 확률로 배신
        }
    }

    @Override
    public Strategy cloneStrategy() {
        return new Troller();
    }
}