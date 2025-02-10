package Strategy;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

// (배신형) 장기 전략 수립자  
//
// ShadowDefectV2 전략은 첫 라운드에 선제적으로 배신(D)하고,  
// 상대의 반응에 따라 5라운드 동안 고정된 행동을 하는 방식으로 작동한다.  
//
// - 첫 라운드에는 무조건 배신(D).  
// - 상대가 맞배신(D)했다면 → 5라운드 연속 협력(C).  
// - 상대가 협력(C)했다면 → 5라운드 연속 배신(D).  
// - 5라운드 동안 선택한 행동을 유지하며, 고민하지 않음.  
// - 5연속 행동이 끝나면 상대의 반응을 다시 판단하고 새로운 5연속 행동을 결정.  
//
// 장점:
// - 초반에 선제적으로 배신하여 상대의 성향을 탐색.  
// - 상대의 반응에 따라 협력 또는 적대적으로 플레이할 수 있음.  
// - 5라운드 동안 확정된 패턴을 유지하여, 일정 부분 예측 불가능성을 제공.  
//
// 단점:
// - Forgiving Tit-for-Tat 같은 유연한 전략에게는 대응이 어려울 수 있음.  
// - 5라운드 동안 고정된 패턴이기 때문에, 일부 전략에게는 약점이 될 수도 있음.  

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