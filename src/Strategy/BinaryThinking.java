package Strategy;

import java.util.List;

/**
 * 전략 이름: BinaryThinking
 * 전략 개요: 상대의 협력 비율이 50%를 초과하면 무조건 협력(C), 50% 이하이면 무조건 배신(D)하는 기준 기반 전략
 *
 * 전략 구조:
 * - 초기 행동: 배신(D)
 * - 행동 로직: 상대의 협력 비율이 50% 초과면 협력(C), 그렇지 않으면 배신(D)
 * - 반응성: 있음 (협력률에 따라 즉각적인 전략 변화 발생)
 * - 기억 활용: 있음 (전체 협력 비율을 계산함)
 * - 랜덤 요소: 없음 (결정적 조건 판단)
 *
 * 전략 성향 분류:
 * - 협력 전략인가? → X (조건부 협력)
 * - 배신 전략인가? → X (조건부 배신)
 * - 패턴 기반인가? → X (고정된 반복은 없음)
 * - 상대 반응형인가? → O (상대의 전체 협력 성향을 평가)
 * - 예측 가능성 → 중간 (협력 비율을 알면 예측 가능하지만, 모르면 모호함)
 *
 * 인간 유형 대응:
 * - 타인을 철저히 두 부류로 나누는 흑백논리 성향
 * - 한 번 신뢰를 잃은 사람에겐 쉽게 마음을 돌리지 않으며, 초기 인상을 중시함
 * - 단순하고 빠른 판단을 선호하며, 복잡한 맥락보다는 수치적 기준에 의존함
 * - 협력적 관계에서도 상대의 일관된 태도를 중시하기 때문에, 가끔은 지나치게 단호하고 융통성 없는 태도를 보이기도 함
 * - 자신의 기준에는 일관되지만, 사회적 관계에선 경직된 판단으로 인해 오해를 받기 쉬움
*/

public class BinaryThinking implements Strategy {
    @Override
    public boolean choose(Player self, Player opponent, List<Boolean> opponentHistory) {
        // 상대방의 행동 기록이 비어 있다면 (즉, 첫 라운드라면) 무조건 배신(D) 선택
        if (opponentHistory.isEmpty()) {
            return false;
        }

        // 동기화된 블록에서 안전하게 접근
        double cooperationRate;

        long cooperationCount = opponentHistory.stream().filter(b -> b).count();
        cooperationRate = (double) cooperationCount / opponentHistory.size();
        

        // 협력 비율이 50% 이하라면 무조건 배신(D)
        // 협력 비율이 50%를 초과하면 무조건 협력(C)
        return cooperationRate > 0.5;
    }
    
    @Override
    public Strategy cloneStrategy() {
        return new BinaryThinking();
    }
}