package Strategy;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 전략 이름: ConditionalForgiver
 * 전략 개요: 기본적으로 배신하지만, 서로가 동시에 배신했을 경우에만 단 한 번 협력(C)을 시도하는 전략
 *
 * 전략 구조:
 * - 초기 행동: 배신(D)
 * - 행동 로직:
 *     - 기본적으로는 항상 배신(D)
 *     - 단, 직전 라운드에서 나와 상대가 모두 배신(D, D)했다면 → 한 번 협력(C)
 *     - 협력 후에는 다시 배신 모드로 돌아감
 * - 반응성: 있음 (직전 라운드 결과에 따라 반응)
 * - 기억 활용: 있음 (자신의 직전 선택을 상대별로 저장)
 * - 랜덤 요소: 없음 (결정적 조건 기반 판단)
 *
 * 전략 성향 분류:
 * - 협력 전략인가? → X (협력은 예외적 상황에만 등장)
 * - 배신 전략인가? → O (기본 행동이 지속적 배신)
 * - 패턴 기반인가? → X (고정된 순서 없음, 조건부 반응)
 * - 상대 반응형인가? → O (직전 상대 행동과 내 행동을 함께 고려)
 * - 예측 가능성 → 중간 (패턴은 명확하지만 협력 조건이 드물어 예측 어려움)
 *
 * 인간 유형 대응:
 * - 원칙적으로는 타인을 믿지 않으며, 쉽게 협력하지 않는 냉소적인 현실주의자
 * - 하지만 아주 드물게, 서로가 잘못을 범한 순간에만 기회를 주려는 제한적 용서자
 * - 자신의 실수는 인정하지 않지만, 상대도 똑같이 잘못했을 때만 "그래, 한 번쯤은" 하고 손 내미는 타입
 * - 이런 성향은 반복적인 갈등을 막기 위한 최소한의 배려일 수도 있고, 자신의 안전을 위한 전략적 움직임일 수도 있음
 * - 전형적인 조건부 신뢰자이며, 잘못된 관계를 회복하려 하기보단 갈등의 피로감에서 벗어나려는 경향
*/

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