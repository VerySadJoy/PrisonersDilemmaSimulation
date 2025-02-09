package Strategy;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

// (보복형, 협력형) 최적화를 추구하는 스마트한 대응자, 교착 상태를 해결하는 전략적 판단자  
//  
// Omega TFT 전략은 일반적인 Tit-for-Tat을 개선하여,  
// 1) 상대와 교착 상태(Deadlock)에 빠지는 경우 이를 감지하여 해결하고  
// 2) 상대가 지나치게 랜덤한 행동을 할 경우 강하게 보복하는 전략이다.  
//  
// 이 전략의 핵심은 ‘Tit-for-Tat의 기본 원칙을 따르되,  
// 특정한 문제(예: 협력-배신 패턴 반복, 상대의 불규칙한 배신)를 해결할 수 있도록 보완하는 것’이다.  
//  
// 전략의 작동 방식:  
// - 첫 번째 라운드는 무조건 협력(C).  
// - 기본적으로 Tit-for-Tat처럼 상대의 마지막 행동을 따라간다.  
// - 상대가 (C, D) → (D, C) 패턴을 반복하는 경우(교착 상태),  
//   일정 횟수(3회) 반복되면 강제로 협력(C) 하여 루프에서 벗어나도록 한다.  
// - 상대가 지나치게 랜덤한 행동(불규칙한 협력/배신)을 보일 경우,  
//   랜덤성 지표를 증가시키고, 일정 횟수(8회)를 초과하면 무조건 배신(D) 모드로 전환.  
//  
// 장점:  
// - 일반적인 Tit-for-Tat보다 교착 상태를 피할 수 있어, 협력 관계를 유지할 가능성이 높다.  
// - 랜덤형 전략(Probabilistic Tit-for-Tat, RandomStrategy)과 싸울 때,  
//   일정 이상 변칙적인 행동을 감지하면 강한 보복을 실행하여 불필요한 손해를 줄인다.  
// - 협력과 배신의 균형을 조절하여, 보다 스마트한 대응이 가능하다.  
//  
// 단점:  
// - Forgiving Tit-for-Tat과 비교하면, 보복성이 강하여 협력 관계가 깨질 위험이 존재한다.  
// - 상대가 패턴을 간파하면, 교착 상태를 유도한 후 이를 이용해 자신에게 유리한 방향으로 유도할 수도 있다.  
// - 무조건 배신 모드


public class OmegaTitForTat implements Strategy {
    private final Map<Player, Integer> deadlockCounter = new ConcurrentHashMap<>(); // 교착 상태 감지용 카운터
    private final Map<Player, Integer> randomnessCounter = new ConcurrentHashMap<>(); // 랜덤성 감지용 카운터
    private final Map<Player, Integer> totalRounds = new ConcurrentHashMap<>(); // 상대별 총 라운드 수
    private final Map<Player, Boolean> defectMode = new ConcurrentHashMap<>(); // 무조건 배신 모드 활성화 여부
    private final Map<Player, List<Boolean>> opponentHistories = new ConcurrentHashMap<>(); // 상대 기록 저장

    private static final int DEADLOCK_THRESHOLD = 3; // 교착 상태 감지 임계값
    private static final int RANDOMNESS_THRESHOLD = 8; // 랜덤성 감지 임계값

    @Override
    public boolean choose(Player self, Player opponent, List<Boolean> opponentHistory) {
        // 리스트를 CopyOnWriteArrayList로 관리하여 동시 수정 방지
        opponentHistories.putIfAbsent(opponent, new CopyOnWriteArrayList<>(opponentHistory));
        List<Boolean> history = opponentHistories.get(opponent);

        // 상대와의 총 라운드 수 증가 (동기화된 업데이트)
        totalRounds.compute(opponent, (k, v) -> (v == null) ? 1 : v + 1);

        // 첫 번째 라운드는 무조건 협력(C)
        if (history.isEmpty()) {
            return true;
        }

        // 만약 무조건 배신 모드(defectMode)라면 남은 게임 동안 계속 배신(D)
        defectMode.putIfAbsent(opponent, false);
        if (defectMode.get(opponent)) {
            return false;
        }

        // 최근 2번의 상대 행동 확인
        int historySize = history.size();
        if (historySize > 1) {
            boolean lastMove = history.get(historySize - 1);
            boolean secondLastMove = history.get(historySize - 2);

            // (C, D) → (D, C) 패턴 반복 여부 확인 (Deadlock 감지)
            deadlockCounter.putIfAbsent(opponent, 0);
            if (secondLastMove != lastMove) {
                deadlockCounter.compute(opponent, (k, v) -> v + 1);
            } else {
                deadlockCounter.put(opponent, 0);
            }

            // Deadlock 감지 시, 일정 횟수 넘으면 강제 협력(C)하여 루프 탈출
            if (deadlockCounter.get(opponent) >= DEADLOCK_THRESHOLD) {
                return true;
            }

            // 랜덤한 행동 감지 (상대의 변동성이 높으면 증가)
            randomnessCounter.putIfAbsent(opponent, 0);
            if (secondLastMove != lastMove || historySize > 2 && history.get(historySize - 3) != lastMove) {
                randomnessCounter.compute(opponent, (k, v) -> v + 1);
            }

            // 만약 랜덤성이 너무 높다면, 무조건 배신 모드로 전환
            if (randomnessCounter.get(opponent) >= RANDOMNESS_THRESHOLD) {
                defectMode.put(opponent, true);
                return false; // 무조건 배신 모드 돌입
            }
        }

        // 일반적인 Tit For Tat 행동 (상대의 마지막 행동을 따라감)
        return history.get(historySize - 1);
    }

    @Override
    public Strategy cloneStrategy() {
        return new OmegaTitForTat();
    }
}