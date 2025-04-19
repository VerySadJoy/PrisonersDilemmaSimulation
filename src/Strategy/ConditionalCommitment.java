package Strategy;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * 전략 이름: ConditionalCommitment
 * 전략 개요: 상대의 연속된 행동을 기준으로 무조건 협력 또는 무조건 배신 모드로 전환하는 전략
 *
 * 전략 구조:
 * - 초기 행동: 랜덤 (협력 또는 배신)
 * - 행동 로직:
 *     - 상대가 3연속 협력(C, C, C) → 무조건 협력 모드로 전환
 *     - 상대가 3연속 배신(D, D, D) → 무조건 배신 모드로 전환
 *     - 그 외의 경우에는 랜덤 선택 유지
 * - 반응성: 있음 (상대의 연속된 패턴에 민감하게 반응)
 * - 기억 활용: 있음 (상대별 최근 행동 기록과 전환 여부 저장)
 * - 랜덤 요소: 있음 (전환 전에는 무작위 행동)
 *
 * 전략 성향 분류:
 * - 협력 전략인가? → X (조건 충족 시에만)
 * - 배신 전략인가? → X (조건 충족 시에만)
 * - 패턴 기반인가? → X (고정 패턴은 없지만 연속성 탐지 기반)
 * - 상대 반응형인가? → O (상대의 행동 흐름을 분석해 반응)
 * - 예측 가능성 → 중간 (모드 전환 전까지는 불확실, 이후에는 완전히 고정)
 *
 * 인간 유형 대응:
 * - 사람을 일정 기준 아래에서 판단하려는 확신 기반 인간
 * - 초반엔 유동적이지만, 상대가 일정한 태도를 보이면 그것을 본질로 간주하고 입장을 고정시킴
 * - 실수를 반복한 상대에겐 기회를 주지 않고, 신뢰를 쌓은 상대에겐 끝까지 믿음
 * - 융통성보다는 일단 정해지면 끝까지 가는 경향이 있어, 상대의 변화 가능성을 무시할 수 있음
 * - 한 번 믿으면 끝까지 가지만, 반대로 신뢰를 잃으면 다시 회복시키기 매우 어려움
*/

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