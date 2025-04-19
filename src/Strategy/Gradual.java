package Strategy;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 전략 이름: Gradual
 * 전략 개요: 상대의 배신 시점에 따라 보복 강도를 달리하는 점진적 응징 전략
 *
 * 전략 구조:
 * - 초기 행동: 협력(C)
 * - 행동 로직:
 *   - 상대가 협력 시: 계속 협력
 *   - 상대가 배신하면 그 시점까지의 라운드 수만큼 보복(D)을 예약
 *   - 예약된 보복이 남아 있으면 그 횟수만큼 배신(D)
 * - 반응성: 있음 (상대의 마지막 행동 기반 + 보복 스택)
 * - 기억 활용: 있음 (보복 예약 스택, 라운드 기반 계산)
 * - 랜덤 요소: 없음
 *
 * 전략 성향 분류:
 * - 협력 전략인가? → O (상대가 협력하면 무한 협력)
 * - 배신 전략인가? → O (상대가 배신하면 매우 강력하게 응징)
 * - 패턴 기반인가? → X
 * - 상대 반응형인가? → O
 * - 예측 가능성 → 중간 (보복 시점과 강도가 상대에 따라 다름)
 *
 * 인간 유형 대응:
 * - 장기적 신뢰를 중요시하는 사람
 * - 초반에는 상대의 실수나 배신도 어느 정도 용서할 수 있지만,
 *   배신이 누적되거나 늦은 타이밍에 발생하면 과거를 통틀어 응징하는 성향
 * - “한 번 신뢰를 깨면 그 대가는 반드시 치르게 한다”는 태도를 가진 사람
 * - 관계를 오래 유지할수록 점점 더 민감해지며, 실망 시 그 반응도 점점 커지는 타입
 * - 사회적으로는 ‘겉으론 관대하지만 속으론 오래 기억하는 사람’으로 여겨질 수 있음
*/

public class Gradual implements Strategy {
    private final Map<Player, Integer> pendingDefections = new HashMap<>(); // 각 상대별 보복 횟수 기록
    private int round = 0; // 현재 라운드

    @Override
    public boolean choose(Player self, Player opponent, List<Boolean> opponentHistory) {
        round++; // 매 호출 시 라운드 증가

        // 상대방의 기록이 없으면 (첫 라운드라면) 무조건 협력(C)
        if (opponentHistory.isEmpty()) {
            return true;
        }

        // 상대의 마지막 행동이 배신(D)이라면, 해당 라운드 수만큼 보복 예약
        if (!opponentHistory.get(opponentHistory.size() - 1)) {
            pendingDefections.put(opponent, pendingDefections.getOrDefault(opponent, 0) + round);
        }

        // 예약된 보복이 남아 있다면 배신(D) 실행
        if (pendingDefections.getOrDefault(opponent, 0) > 0) {
            pendingDefections.put(opponent, pendingDefections.get(opponent) - 1);
            return false; // 배신
        }

        // 기본적으로 협력 유지 (C)
        return true;
    }

    @Override
    public Strategy cloneStrategy() {
        return new Gradual();
    }
}