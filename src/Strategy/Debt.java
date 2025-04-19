package Strategy;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 전략 이름: Debt
 * 전략 개요: 상대의 배신을 '빚'으로 기록하고, 갚기 전까지는 보복하며, 협력으로 빚을 갚으면 다시 협력하는 보복-회복형 전략
 *
 * 전략 구조:
 * - 초기 행동: 협력(C)
 * - 행동 로직:
 *     - 상대가 배신(D)하면 빚을 +1
 *     - 상대가 협력(C)하면 빚을 -1 (최소 0까지)
 *     - 빚이 0일 경우에만 협력(C), 빚이 남아있으면 배신(D)
 * - 반응성: 있음 (상대의 이전 행동에 따라 실시간 조정)
 * - 기억 활용: 있음 (상대별 누적 빚 상태 저장)
 * - 랜덤 요소: 없음 (결정적 보복 시스템)
 *
 * 전략 성향 분류:
 * - 협력 전략인가? → O (상대가 갚으면 협력 회복 가능)
 * - 배신 전략인가? → X (보복은 조건부로 실행됨)
 * - 패턴 기반인가? → X (고정된 반복 없음)
 * - 상대 반응형인가? → O (행동에 따라 빚을 쌓고 갚음)
 * - 예측 가능성 → 높음 (빚 구조를 알면 완전 예측 가능)
 *
 * 인간 유형 대응:
 * - 원한을 품되 이성적으로 갚게 만드는 채권자
 * - 신뢰를 잃으면 반드시 대가를 요구하지만, 보상이 충분하면 다시 신뢰를 회복해주는 공정한 복수자
 * - 감정적인 복수보다는 정확한 상호작용 이력에 따라 보복과 용서를 계산함
 * - 상대가 회복 의지를 보인다면 받아들이지만, 빚이 남아있는 한 절대로 협력을 허용하지 않음
 * - 사회적으로는 일관된 원칙을 가진 관계 관리형이며, 얕보면 안 되는 타입
*/

public class Debt implements Strategy {
    private final ConcurrentHashMap<Player, Integer> debt = new ConcurrentHashMap<>(); // 상대가 쌓은 배신 빚

    @Override
    public boolean choose(Player self, Player opponent, List<Boolean> opponentHistory) {
        debt.putIfAbsent(opponent, 0);

        if (opponentHistory.isEmpty()) {
            return true; // 첫 턴은 협력
        }

        // 상대가 이전 턴에 배신했으면 빚을 추가
        if (!opponentHistory.get(opponentHistory.size() - 1)) {
            debt.put(opponent, debt.get(opponent) + 1);
        } else {
            // 상대가 협력하면 빚을 하나 갚아줌
            debt.put(opponent, Math.max(0, debt.get(opponent) - 1));
        }

        // 빚이 남아 있으면 배신으로 갚음
        return debt.get(opponent) == 0;
    }

    @Override
    public Strategy cloneStrategy() {
        return new Debt();
    }
}