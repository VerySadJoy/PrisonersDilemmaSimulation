package Strategy;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 전략 이름: OmegaTitForTat
 * 전략 개요: Tit-for-Tat에 교착 해소와 랜덤성 감지를 추가한 스마트 보복형 전략
 *
 * 전략 구조:
 * - 초기 행동: 협력(C)
 * - 행동 로직:
 *   - 기본적으로는 Tit-for-Tat처럼 상대의 마지막 행동을 따라함
 *   - 교착 패턴 (C,D)-(D,C) 반복 시 deadlockCounter 증가
 *     - 임계치(3회) 이상이면 강제 협력(C)로 루프 탈출
 *   - 상대 행동의 변동성이 높으면 randomnessCounter 증가
 *     - 임계치(8회) 이상이면 무조건 배신 모드로 전환
 * - 반응성: 높음 (교착 탐지, 랜덤성 감지 모두 포함)
 * - 기억 활용: 있음 (최소 3턴까지 분석)
 * - 랜덤 요소: 없음 (완전 결정적)
 *
 * 전략 성향 분류:
 * - 협력 전략인가? → O (협력을 기반으로 함)
 * - 배신 전략인가? → X (특수 조건에서만 전환)
 * - 패턴 기반인가? → O (반복 패턴 및 랜덤성 감지)
 * - 상대 반응형인가? → O (상대 패턴에 민감하게 반응)
 * - 예측 가능성 → 중간 (기본은 예측 가능, 전환 조건은 예외적)
 *
 * 인간 유형 대응:
 * - 최적화를 추구하는 스마트한 대응자
 * - 상대의 실수는 관대하게 보지만, 불성실한 태도나 혼란을 유발하는 전략에는 단호하게 대응
 * - 반복되는 싸움에선 "이쯤에서 그만 싸우자"며 먼저 손 내밀 줄 아는 타입
 * - 그러나 너무 예측 불가능한 행동을 보이는 상대에겐 신뢰를 거두고 단호해짐
 * - 말 잘 통하면 오래 가고, 말 안 통하면 매몰차게 돌아서는 지혜로운 전략가
*/

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