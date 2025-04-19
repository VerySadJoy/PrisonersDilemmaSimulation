package Strategy;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 전략 이름: HandOfGod
 * 전략 개요: 상대를 충분히 신뢰하게 만든 뒤, 점진적으로 배신을 섞어 착취하는 위선적 전략
 *
 * 전략 구조:
 * - 초기 행동: 첫 10라운드 무조건 협력(C)
 * - 행동 로직:
 *   - 상대의 협력 비율이 80% 이상인 상태가 5라운드 연속되면 착취 모드 진입
 *   - 착취 모드에서는 4라운드마다 배신(D) → 점진적 배신 전개
 *   - 상대가 보복형일 경우: 30% 확률로 배신을 섞어 탐색
 *   - 상대가 완전 배신형일 경우: 20% 확률로 협력하여 혼란 유도
 *   - 그 외 상황: 상대의 마지막 행동을 따라감 (TFT 기반 대응)
 * - 반응성: 있음 (상대의 협력 비율, 반응 추적)
 * - 기억 활용: 중간 (협력 횟수, 연속 협력 기록)
 * - 랜덤 요소: 있음 (탐색과 혼란을 위한 확률적 판단)
 *
 * 전략 성향 분류:
 * - 협력 전략인가? → X (초반 협력은 착취 유도 목적)
 * - 배신 전략인가? → O (장기적으로는 점진적 착취)
 * - 패턴 기반인가? → O (4턴 주기 배신, 상태 전이 기반)
 * - 상대 반응형인가? → O (협력 비율과 반응에 따라 착취 여부 결정)
 * - 예측 가능성 → 낮음 (착취 모드 전환 시점이 불투명하고, 확률 배신 존재)
 *
 * 인간 유형 대응:
 * - 신의 손길을 가장한 기만자 또는 서서히 배신하는 위선자
 * - 초반에는 완벽한 협력자처럼 행동하며 상대에게 신뢰를 쌓지만,
 *   그 신뢰가 일정 수준에 도달하면 그것을 기회로 삼아 배신을 감행함
 * - 상대가 눈치채지 못하도록 점진적으로 배신을 섞고,
 *   강한 상대에게는 신중하게 배신을 탐색하거나 협력을 시도하여 자신을 보호함
 * - 이러한 인간은 외면상 매력적일 수 있지만,
 *   내면에는 철저한 계산과 이기심이 숨어 있어 장기적으로는 신뢰를 잃을 수 있음
*/

public class HandOfGod implements Strategy {
    private final Map<Player, Integer> opponentCooperationCount = new ConcurrentHashMap<>(); // 협력 횟수 기록
    private final Map<Player, Integer> totalRounds = new ConcurrentHashMap<>(); // 총 경기 수 기록
    private final Map<Player, Boolean> exploitMode = new ConcurrentHashMap<>(); // 착취 모드 여부
    private final Map<Player, Integer> consecutiveHighCooperation = new ConcurrentHashMap<>(); // 연속 협력 횟수 기록
    private final Random random = new Random(); // 랜덤한 행동을 위한 변수

    @Override
    public boolean choose(Player self, Player opponent, List<Boolean> opponentHistory) {
        // 값 초기화
        totalRounds.putIfAbsent(opponent, 0);
        opponentCooperationCount.putIfAbsent(opponent, 0);
        exploitMode.putIfAbsent(opponent, false);
        consecutiveHighCooperation.putIfAbsent(opponent, 0);

        // 라운드 수 증가
        totalRounds.put(opponent, totalRounds.get(opponent) + 1);
        int rounds = totalRounds.get(opponent);

        // 첫 10라운드는 무조건 협력(C)
        if (rounds <= 10) {
            return true;
        }

        // 상대의 협력 횟수를 먼저 업데이트
        if (!opponentHistory.isEmpty() && opponentHistory.get(opponentHistory.size() - 1)) {
            opponentCooperationCount.put(opponent, opponentCooperationCount.get(opponent) + 1);
        }

        int cooperationCount = opponentCooperationCount.get(opponent);
        double cooperationRate = (double) cooperationCount / rounds;

        // 착취 모드 진입 조건
        if (cooperationRate >= 0.8) {
            consecutiveHighCooperation.put(opponent, consecutiveHighCooperation.get(opponent) + 1);
            if (consecutiveHighCooperation.get(opponent) >= 5) {
                exploitMode.put(opponent, true);
            }
        } else {
            consecutiveHighCooperation.put(opponent, 0);
        }

        boolean isExploiting = exploitMode.get(opponent);

        // 착취 모드 적용
        if (isExploiting) {
            return rounds % 4 != 0; // 4라운드마다 배신 (점진적 착취)
        }

        // 보복형 전략 대응 (60% 이상 협력)
        if (cooperationRate > 0.6) {
            return random.nextDouble() > 0.3; // 30% 확률로 배신
        }

        // 배신자가 협력할 가능성도 테스트
        if (cooperationRate < 0.3) {
            return random.nextDouble() < 0.2; // 20% 확률로 협력 (상대가 협력할 가능성 테스트)
        }

        // 애매한 상대(협력-배신 혼합형) 대응
        return !opponentHistory.get(opponentHistory.size() - 1); // 기본적으로 상대 행동을 따라감
    }

    @Override
    public Strategy cloneStrategy() {
        return new HandOfGod();
    }
}