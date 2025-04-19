package Strategy;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 전략 이름: GlassMind
 * 전략 개요: 초반에는 협력적으로 시작하지만, 배신당하면 5라운드 동안 무조건 배신 후 다시 신뢰를 회복하려는 감정 기반 전략
 *
 * 전략 구조:
 * - 초기 행동: 첫 10라운드 동안 80% 확률로 협력(C)
 * - 행동 로직:
 *     - 배신을 당하면 → 5라운드 동안 무조건 배신(D)
 *     - 그 이후 → 상대가 협력하면 다시 신뢰하고 협력(C)
 * - 반응성: 있음 (직전 배신에 민감하게 반응)
 * - 기억 활용: 있음 (배신 횟수 및 신뢰 회복 상태 추적)
 * - 랜덤 요소: 초반 10라운드 동안 80% 확률 협력
 *
 * 전략 성향 분류:
 * - 협력 전략인가? → O (회복을 전제로 함)
 * - 배신 전략인가? → X (지속적인 배신을 의도하진 않음)
 * - 패턴 기반인가? → O (5턴 보복 후 회복이라는 감정 주기 패턴)
 * - 상대 반응형인가? → O (상대의 직전 행동에 즉각 반응)
 * - 예측 가능성 → 중간 (초반 랜덤, 이후에는 상태 기반 결정)
 *
 * 인간 유형 대응:
 * - 상처받기 쉬운 신뢰자, 감정적으로 반응하는 방어자
 * - 누군가를 신뢰할 때는 깊이 신뢰하지만, 배신당하면 꽤 오랫동안 마음을 닫는다
 * - 단, 시간이 지나거나 상대가 성의를 보이면 다시 마음을 연다
 * - 감정 기복이 있기에 다루기 어렵지만, 관계 회복의 여지를 남기는 점에서 인간미가 있다
 * - 협력 관계를 유지하려면 조심스럽게 대해야 하며, 무심코 배신하면 큰 보복이 따른다
*/

public class GlassMind implements Strategy {
    private final ConcurrentHashMap<Player, Integer> betrayalCount = new ConcurrentHashMap<>(); // 상대에게 배신당한 횟수
    private final ConcurrentHashMap<Player, Integer> trustRecovery = new ConcurrentHashMap<>(); // 신뢰 회복 상태 (양수: 협력, 음수: 배신)
    private final int RECOVERY_PERIOD = 5; // 배신 후 5라운드 동안은 배신 모드
    private final int INITIAL_COOP_PERCENTAGE = 80; // 초반 10라운드 동안 협력 확률

    @Override
    public boolean choose(Player self, Player opponent, List<Boolean> opponentHistory) {
        int roundsPlayed = opponentHistory.size();

        // 초반 10라운드 동안 기본적으로 협력 (80%) 하지만 가끔 배신 (20%)
        if (roundsPlayed < 10) {
            return Math.random() < (INITIAL_COOP_PERCENTAGE / 100.0);
        }

        // 상대에게 배신당한 횟수 확인
        betrayalCount.putIfAbsent(opponent, 0);
        trustRecovery.putIfAbsent(opponent, 0);

        // 이전 라운드에서 상대방이 배신했는지 확인
        boolean opponentLastMove = opponentHistory.get(roundsPlayed - 1);
        if (!opponentLastMove) { // 상대가 배신한 경우
            betrayalCount.put(opponent, betrayalCount.get(opponent) + 1);
            trustRecovery.put(opponent, -RECOVERY_PERIOD); // 배신 모드 ON
        }

        // 배신 모드 (배신당한 후 5라운드 동안은 무조건 배신)
        if (trustRecovery.get(opponent) < 0) {
            trustRecovery.put(opponent, trustRecovery.get(opponent) + 1); // 회복 모드 증가
            return false; // 배신 지속
        }

        // 신뢰 회복 모드 (상대가 협력하면 다시 협력)
        if (opponentLastMove) {
            trustRecovery.put(opponent, RECOVERY_PERIOD); // 신뢰 회복 모드 ON
        }

        // 신뢰 회복이 끝난 상태면 협력
        return true;
    }

    @Override
    public Strategy cloneStrategy() {
        return new GlassMind();
    }
}