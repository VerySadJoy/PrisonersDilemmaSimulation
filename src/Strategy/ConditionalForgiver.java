package Strategy;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

// (배신형, 조건부 협력) 냉혹한 현실주의자, 제한적인 용서자
//
// ConditionalForgiver 전략은 기본적으로 항상 배신(D)하지만,  
// 직전 라운드에서 상대와 내가 모두 배신(D, D)했을 경우 단 한 번 협력(C) 한다.
//
// 전략의 작동 방식:
// - 기본적으로 항상 배신(D).
// - 단, 직전 라운드에서 나와 상대가 둘 다 배신(D, D)했으면 한 번 협력(C).
// - 협력 후에는 다시 기본 배신(D) 모드로 돌아감.
//
// 장점:
// - 완전한 배신 전략(Always Defect)보다는 유연한 대응 가능.
// - 상대가 Tit-for-Tat 같은 전략이라면, 계속되는 배신에서 벗어나 협력 기회를 가질 수 있음.
// - 멀티 환경에서 각 상대별로 독립적인 상태를 유지할 수 있음.  
//
// 단점:
// - Forgiving Tit-for-Tat 같은 유연한 전략과 만나도 제한적인 협력만 가능하여 완전한 신뢰를 얻기 어려움.
// - 상대가 협력 중심 전략이라면, 결국 배신을 반복하여 관계가 깨질 가능성이 높음.
//

public class ConditionalForgiver implements Strategy {
    // 상대별로 이전 라운드에서 내가 했던 행동을 저장 (true = 협력, false = 배신)
    private final Map<Player, Boolean> lastMyAction = new HashMap<>();

    @Override
    public boolean choose(Player self, Player opponent, List<Boolean> opponentHistory) {
        int rounds = opponentHistory.size();

        // 첫 번째 라운드는 기본적으로 배신(D)
        if (rounds == 0) {
            lastMyAction.put(opponent, false); // 초기값: 배신(D)
            return false;
        }

        // 상대의 마지막 행동을 가져옴
        boolean lastOpponentMove = opponentHistory.get(rounds - 1);
        boolean lastMyMove = lastMyAction.getOrDefault(opponent, false); // 기본값: 배신(D)

        // 직전 라운드에서 나와 상대가 둘 다 배신(D, D)했다면 → 한 번 협력(C)
        if (!lastOpponentMove && !lastMyMove) {
            lastMyAction.put(opponent, true); // 이번 라운드에서는 협력(C)
            return true;
        }

        // 기본적으로 배신(D)
        lastMyAction.put(opponent, false); // 이번 라운드도 배신(D)
        return false;
    }

    @Override
    public Strategy cloneStrategy() {
        return new ConditionalForgiver();
    }
}