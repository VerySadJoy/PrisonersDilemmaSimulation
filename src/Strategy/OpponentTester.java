package Strategy;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 전략 이름: OpponentTester
 * 전략 개요: 상대의 첫 반응을 기반으로 이후 5라운드씩 고정된 행동을 반복하는 장기적 패턴 전략
 *
 * 전략 구조:
 * - 첫 라운드에는 무조건 배신(D)
 * - 상대가 맞배신(D)했다면 → 이후 5라운드 협력(C)
 * - 상대가 협력(C)했다면 → 이후 5라운드 배신(D)
 * - 이후 매 5라운드 단위로 상대의 반응을 평가하여 행동을 전환
 *
 * 행동 원칙:
 * - 항상 5라운드 단위로 행동을 결정함
 * - 일관된 패턴을 유지하면서도, 상대의 최근 반응을 기반으로 행동 전환
 * - 탐색 후 고정된 판단을 내리는 '미니-세그먼트 전략'
 *
 * 전략 성향 분류:
 * - 협력 전략인가? → X (처음은 항상 배신, 협력도 통제됨)
 * - 배신 전략인가? → O (배신 기반 시작, 주도권을 쥐고 테스트함)
 * - 반응형인가? → O (5라운드 단위로 반응)
 * - 랜덤성? → X (완전히 결정적)
 * - 패턴형? → O (정확히 5턴 패턴을 기반으로 한 전략)
 *
 * 인간 유형 대응:
 * - 장기 전략 수립자, 심리적 테스트 후 결정하는 사람
 * - “일단 네가 어떤 타입인지 보자. 그다음 5턴 동안 결정할게” 같은 마인드
 * - 감정적이지 않고, 테스트 결과에 따라 기계적으로 반응
 * - 일단 맞배신하는 사람에겐 신뢰를 보내는 타입이고
 * - 너무 순진하게 협력만 하는 사람에겐 기회라고 생각하고 배신을 반복함
*/

public class OpponentTester implements Strategy {
    private final Map<Player, Integer> roundsPlayed = new HashMap<>(); // 경기 진행 라운드 수
    private final Map<Player, Boolean> currentAction = new HashMap<>(); // 현재 5라운드 동안 고정된 행동
    private final Map<Player, Integer> actionCounter = new HashMap<>(); // 5라운드 행동 카운트

    @Override
    public boolean choose(Player self, Player opponent, List<Boolean> opponentHistory) {
        int rounds = roundsPlayed.getOrDefault(opponent, 0) + 1;
        roundsPlayed.put(opponent, rounds);

        // 첫 라운드는 무조건 배신(D)
        if (rounds == 1) {
            currentAction.put(opponent, false); // 배신(D)
            actionCounter.put(opponent, 1);
            return false;
        }

        // 5연속 행동이 끝난 경우, 상대의 최근 반응을 분석하여 새로운 5연속 행동을 결정
        if (actionCounter.getOrDefault(opponent, 0) >= 5) {
            boolean lastMyAction = currentAction.get(opponent); // 내가 직전 5라운드 동안 했던 행동
            boolean opponentLastAction = opponentHistory.get(opponentHistory.size() - 1); // 상대의 마지막 행동

            if (lastMyAction) { // 내가 직전 5연속 협력(C)했음
                if (!opponentLastAction) { // 상대가 배신(D)했으면 → 5연속 협력
                    currentAction.put(opponent, true);
                } else { // 상대가 협력(C)했으면 → 5연속 배신
                    currentAction.put(opponent, false);
                }
            } else { // 내가 직전 5연속 배신(D)했음
                if (!opponentLastAction) { // 상대도 배신(D)했으면 → 5연속 협력
                    currentAction.put(opponent, true);
                } else { // 상대가 협력(C)했으면 → 5연속 배신
                    currentAction.put(opponent, false);
                }
            }
            actionCounter.put(opponent, 1); // 새로운 5라운드 시작
        } else {
            actionCounter.put(opponent, actionCounter.get(opponent) + 1); // 현재 5연속 행동 유지
        }

        return currentAction.get(opponent); // 5연속 동안 정한 행동을 반환
    }

    @Override
    public Strategy cloneStrategy() {
        return new OpponentTester();
    }
}