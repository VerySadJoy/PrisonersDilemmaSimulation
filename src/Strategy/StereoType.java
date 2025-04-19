package Strategy;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * 전략 이름: StereoType  
 * 전략 유형: 보복형 (선입견에 입각한 단순화 전략)
 *
 * 전략 개요:  
 * - 첫 라운드엔 랜덤하게 행동한 뒤  
 * - 상대의 첫 반응만 보고 "이 사람은 협력자/배신자야"라고 단정  
 * - 이후엔 무조건 그에 맞춰서 행동 (협력 or 배신 고정)
 *
 * 작동 방식:  
 * - 1R: 랜덤하게 협력(C) or 배신(D)  
 * - 2R: 상대의 1R 반응을 기록 → 고정 반응 설정  
 * - 3R~: 상대의 첫 반응을 계속 따라감 (협력이면 나도 협력, 배신이면 나도 배신)
 *
 * 장점:  
 * - 협력가와 만나면 빠르게 완전 협력으로 진입 가능  
 * - 로직이 매우 단순하고 효율적 (판단 한 번으로 끝)  
 * - Tit-for-Tat과 높은 협력 시너지 발생 가능
 *
 * 단점:  
 * - 상대의 초기 행동 하나에 모든 미래를 걸어버림  
 * - Always Defect 같은 전략 만나면 그냥 박살  
 * - Forgiving 전략처럼 초반 배신 뒤 협력하는 타입에게 쉽게 당함  
 * - 변칙형, 확률형 전략에게도 상당히 약함
 *
 * 인간 유형 대응:  
 * - 첫인상이 모든 걸 결정한다는 사람  
 * - 누가 나한테 잘해주면 끝까지 믿고,  
 *   초반에 무례하면 그 사람은 그냥 낙인 찍음  
 * - 단순하지만, 때로는 편견이 관계를 망칠 수도 있음  
 * - 인간적인 면으론 공감되지만, 전략적으로는 위험요소 많음
 */

public class StereoType implements Strategy {
    private final Map<Player, Boolean> fixedResponse = new HashMap<>(); // 고정된 반응 (true = 협력, false = 배신)
    private final Random random = new Random();

    @Override
    public boolean choose(Player self, Player opponent, List<Boolean> opponentHistory) {
        int rounds = opponentHistory.size();

        // 첫 번째 라운드: 랜덤하게 행동 결정
        if (rounds == 0) {
            boolean firstAction = random.nextBoolean(); // 랜덤하게 협력 또는 배신
            return firstAction;
        }

        // 두 번째 라운드: 상대의 첫 반응을 보고 결정
        if (rounds == 1) {
            boolean opponentFirstAction = opponentHistory.get(0); // 상대의 첫 번째 행동
            fixedResponse.put(opponent, opponentFirstAction); // 상대의 첫 반응을 저장
            return opponentFirstAction; // 이후 이 행동을 계속 유지
        }

        // 이후 모든 라운드에서는 상대의 첫 반응에 따라 고정된 행동 유지
        return fixedResponse.get(opponent);
    }

    @Override
    public Strategy cloneStrategy() {
        return new StereoType();
    }
}