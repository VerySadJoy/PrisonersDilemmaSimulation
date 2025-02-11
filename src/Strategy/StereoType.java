package Strategy;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

// (보복형) 선입견에 입각한 사람
//
// StereoType 전략은 첫 번째 라운드에서는 무작위로 협력 또는 배신을 선택하고,  
// 이후 상대의 첫 반응에 따라 협력 또는 배신을 고정하는 방식으로 작동한다.  
//
// - 첫 라운드에는 랜덤하게 협력(C) 또는 배신(D).  
// - 상대가 첫 번째 반응에서 협력(C)했다면 → 이후 항상 협력(C).  
// - 상대가 첫 번째 반응에서 배신(D)했다면 → 이후 항상 배신(D).  
//
// 장점:
// - 상대의 성향을 초반에 빠르게 결정하고, 복잡한 계산 없이 간단하게 행동 가능.  
// - Tit-for-Tat과 같은 협력형 전략과 만나면 완전히 협력적인 플레이가 가능.  
//
// 단점:
// - 상대가 배신하는 전략(Always Defect)과 만나면 처음부터 끝까지 손해를 볼 가능성이 큼.  
// - Forgiving Tit-for-Tat 같은 유연한 전략에게는 약간의 불리함이 있을 수 있음.  
//
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