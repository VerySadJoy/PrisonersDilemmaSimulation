package Strategy;

import java.util.List;

/**
 * 전략 이름: AlternateCooperate
 * 전략 개요: 협력(C)과 배신(D)을 번갈아 반복하는 고정 루틴 전략
 *
 * 전략 구조:
 * - 초기 행동: 첫 라운드는 협력(C)
 * - 행동 로직: C → D → C → D 순서로 반복
 * - 반응성: 없음 (상대 행동과 무관)
 * - 기억 활용: 있음 (자신의 턴 수를 기억)
 * - 랜덤 요소: 없음 (완전히 결정적)
 *
 * 전략 성향 분류:
 * - 협력 전략인가? → X (50%만 협력, 완전 협력은 아님)
 * - 배신 전략인가? → X (50%만 배신, 완전 배신도 아님)
 * - 패턴 기반인가? → O (라운드 수 기반의 주기적 행동)
 * - 상대 반응형인가? → X (어떤 전략과 만나든 동일한 행동)
 * - 예측 가능성 → 완전히 예측 가능
 *
 * 인간 유형 대응:
 * - 스스로의 규칙을 철저히 따르는 자기 원칙주의자
 * - 인간관계에서 상대의 반응에 크게 동요하지 않음. 누가 어떻게 대하든 자신의 방식대로 일관되게 행동함
 * - 예를 들어, "한 번은 양보, 한 번은 나를 위해" 식의 균형 감각을 신념처럼 여기는 사람
 * - 타인 입장에선 이해하기 쉽지만, 감정적 유대가 약한 사람으로 보일 수 있음
 * - 갈등보다는 루틴을 선호하며, 변화보다 반복에서 안정을 추구하는 경향
 * - 팀워크보다 개인적 리듬을 중시하고, 예측 가능성은 높지만 융통성은 부족
*/

public class AlternateCooperate implements Strategy {
    @Override
    public boolean choose(Player self, Player opponent, List<Boolean> opponentHistory) {
        // 첫 번째 라운드는 무조건 협력 (C)
        if (opponentHistory.isEmpty()) {
            return true;
        }
        // 짝수 번째 선택에서는 협력 (C), 홀수 번째 선택에서는 배신 (D)
        return opponentHistory.size() % 2 == 0;
    }

    @Override
    public Strategy cloneStrategy() {
        return new AlternateCooperate();
    }
}