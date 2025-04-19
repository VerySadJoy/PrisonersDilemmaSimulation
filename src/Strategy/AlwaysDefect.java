package Strategy;

import java.util.List;

/**
 * 전략 이름: AlwaysDefect
 * 전략 개요: 어떤 상황에서도 무조건 배신(D)만 선택하는 절대 배신 전략
 *
 * 전략 구조:
 * - 초기 행동: 배신(D)
 * - 행동 로직: 항상 배신(D) — 상대가 무엇을 하든 변하지 않음
 * - 반응성: 없음 (상대의 협력에도 반응하지 않음)
 * - 기억 활용: 없음 (과거 기록과 무관)
 * - 랜덤 요소: 없음 (완전 결정적)
 *
 * 전략 성향 분류:
 * - 협력 전략인가? → X 없음
 * - 배신 전략인가? → O 절대 배신형
 * - 패턴 기반인가? → X (패턴조차 없음, 오직 배신)
 * - 상대 반응형인가? → X (모든 상대에게 동일한 행동)
 * - 예측 가능성 → 완전히 예측 가능
 *
 * 인간 유형 대응:
 * - 냉혈한 기회주의자 혹은 탐욕스러운 생존주의자
 * - 언제나 자신의 이익을 최우선으로 생각하며, 타인을 이용하는 데 거리낌이 없음
 * - 신뢰를 주지 않으며, 협력을 기반으로 한 관계 형성에는 관심이 없음
 * - 단기적 성과에는 능하지만, 공동체 내에서는 고립되거나 배척될 가능성이 큼
 * - 사회적 규범보다 생존과 이익을 우선시하는 이기적인 성향을 지님
*/

public class AlwaysDefect implements Strategy {

    @Override
    public boolean choose(Player self, Player opponent, List<Boolean> opponentHistory) {
        // 항상 배신 (D)을 선택함
        return false;
    }
    
    @Override
    public Strategy cloneStrategy() {
        return new AlwaysDefect();
    }
}