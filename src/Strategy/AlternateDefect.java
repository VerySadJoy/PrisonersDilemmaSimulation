package Strategy;

import java.util.List;

/**
 * 전략 이름: AlternateDefect
 * 전략 개요: 배신(D)과 협력(C)을 번갈아 반복하는 고정 루틴 전략
 *
 * 전략 구조:
 * - 초기 행동: 첫 라운드는 배신(D)
 * - 행동 로직: D → C → D → C 순서로 반복
 * - 반응성: 없음 (상대의 행동과 무관)
 * - 기억 활용: 있음 (자신의 턴 수를 기억)
 * - 랜덤 요소: 없음 (완전히 결정적)
 *
 * 전략 성향 분류:
 * - 협력 전략인가? → X (절반은 협력이지만, 배신이 시작이기 때문에 더 적대적으로 보임)
 * - 배신 전략인가? → X (완전한 배신 전략은 아님, 주기적으로 협력함)
 * - 패턴 기반인가? → O (라운드 수 기반의 주기적 행동)
 * - 상대 반응형인가? → X (모든 상대에게 동일한 행동 반복)
 * - 예측 가능성 → 완전히 예측 가능
 *
 * 인간 유형 대응:
 * - 고집스러운 독립주의자 또는 관계 회피형 몽상가
 * - "내 방식이 곧 정의다"라는 태도를 지니며, 상대의 반응이나 신뢰보다 자기 규칙에 더 충실
 * - 협력과 배신이 오가지만, 그것은 타인과의 관계 맥락 없이 혼자 결정한 흐름
 * - 사회적 관계에서 오해받기 쉬움 — 때론 도와주지만, 이유를 묻기 어려운 사람
 * - 예측 가능성은 있지만, 감정적 신뢰는 형성되지 않는 유형
 * - 일종의 고독한 플레이어, 자기 안의 세계를 사는 타입
*/

public class AlternateDefect implements Strategy {
    @Override
    public boolean choose(Player self, Player opponent, List<Boolean> opponentHistory) {
        // 첫 번째 라운드는 무조건 배신 (D)
        if (opponentHistory.isEmpty()) {
            return false;
        }
        return opponentHistory.size() % 2 != 0;
    }
    
    @Override
    public Strategy cloneStrategy() {
        return new AlternateDefect();
    }
}