package Strategy;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

// (적응형) 상황에 따라 절대적인 태도를 정하는 전략
//
// ConditionalCommitment 전략은 초반에는 랜덤하게 행동하지만,
// 상대의 연속된 행동에 따라 무조건적인 협력 또는 배신 모드로 전환하는 전략이다.
//
// 전략의 작동 방식:
// - 초반에는 랜덤하게 협력(C) 또는 배신(D).
// - 상대가 3연속 협력(C, C, C) 하면 무조건 협력 모드로 변경.
// - 상대가 3연속 배신(D, D, D) 하면 무조건 배신 모드로 변경.
// - 한 번 모드가 정해지면 끝까지 유지 (상대별로 다르게 적용됨).
//
// 장점:
// - 상대가 협력적이라면 무조건 협력하여 높은 점수를 얻을 수 있음.
// - 상대가 배신적이라면 빠르게 적응하여 손실을 최소화할 수 있음.
// - 멀티 환경에서 각 상대별로 개별적인 상태를 유지할 수 있음.
//
// 단점:
// - 초반에 랜덤하게 행동하므로 불필요한 배신을 할 수도 있음.
// - 상대의 실수(랜덤 배신)로 인해 비효율적인 모드에 갇힐 가능성이 있음.
// - Forgiving Tit-for-Tat 같은 전략에게 오해를 살 수도 있음.

public class ConditionalCommitment implements Strategy {
    private final Random random = new Random();

    // 각 상대별 무조건 협력(true) / 무조건 배신(false) 모드 저장 (null이면 아직 결정되지 않음)
    private final Map<Player, Boolean> lockedStrategy = new HashMap<>();

    @Override
    public boolean choose(Player self, Player opponent, List<Boolean> opponentHistory) {
        // 만약 특정 상대에 대해 이미 협력/배신 모드가 결정되었다면 그대로 유지
        if (lockedStrategy.containsKey(opponent)) {
            return lockedStrategy.get(opponent);
        }

        int rounds = opponentHistory.size();

        // 초반에는 랜덤한 선택
        if (rounds < 3) {
            return random.nextBoolean();
        }

        // 최근 3턴이 모두 협력(C)이라면 → 무한 협력 모드
        if (opponentHistory.get(rounds - 3) && opponentHistory.get(rounds - 2) && opponentHistory.get(rounds - 1)) {
            lockedStrategy.put(opponent, true);
            return true;
        }

        // 최근 3턴이 모두 배신(D)이라면 → 무한 배신 모드
        if (!opponentHistory.get(rounds - 3) && !opponentHistory.get(rounds - 2) && !opponentHistory.get(rounds - 1)) {
            lockedStrategy.put(opponent, false);
            return false;
        }

        // 3연속 협력/배신이 아닐 경우 랜덤 선택 유지
        return random.nextBoolean();
    }

    @Override
    public Strategy cloneStrategy() {
        return new ConditionalCommitment();
    }
}