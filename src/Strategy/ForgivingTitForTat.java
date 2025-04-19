package Strategy;

import java.util.List;

/**
 * 전략 이름: ForgivingTitForTat
 * 전략 개요: 상대가 한 번 배신하는 것은 용서하지만, 두 번 연속 배신하면 보복하는 관대한 변형 Tit-for-Tat 전략
 *
 * 전략 구조:
 * - 초기 행동: 협력(C)
 * - 행동 로직:
 *     - 직전 두 라운드에서 모두 배신(D, D)이면 → 보복 (배신)
 *     - 그 외에는 모두 협력
 * - 반응성: 있음 (직전 두 행동 기준)
 * - 기억 활용: 짧은 메모리 (최근 2턴만 참조)
 * - 랜덤 요소: 없음 (결정적 행동)
 *
 * 전략 성향 분류:
 * - 협력 전략인가? → O (보복보다는 협력을 우선시)
 * - 배신 전략인가? → X
 * - 패턴 기반인가? → X
 * - 상대 반응형인가? → O (두 번 연속 배신에만 반응)
 * - 예측 가능성 → 중간 (상대가 패턴을 알면 착취 가능)
 *
 * 인간 유형 대응:
 * - 신중한 용서자, 현실적인 화해주의자
 * - 한 번의 실수는 "그럴 수도 있지" 하고 넘기지만,
 *   두 번째 실수가 반복되면 "그건 고의지?" 하며 단호해진다
 * - 관계 유지와 신뢰 회복을 중요시하지만, 바보처럼 계속 당하진 않음
 * - 실제로는 부드럽지만 속은 단단한 사람, “좋은 사람”이지만 절대 “호구”는 아님
 * - 하지만 교활한 상대가 타이밍을 조절하면 이용당할 가능성도 존재함
*/  

public class ForgivingTitForTat implements Strategy {

    @Override
    public boolean choose(Player self, Player opponent, List<Boolean> opponentHistory) {
        // 첫 번째 라운드는 기본적으로 협력(C)
        if (opponentHistory.isEmpty()) {
            return true;
        }

        int size = opponentHistory.size();

        // 상대가 단 한 번 배신했을 경우 → 용서하고 협력
        if (size == 1) {
            return true;
        }
        // 상대가 직전 두 번 연속 배신(D, D) 했다면 → 보복 (배신)
        // 기본적으로 협력 유지

        return !(!opponentHistory.get(size - 1) && !opponentHistory.get(size - 2));
    }
    
    @Override
    public Strategy cloneStrategy() {
        return new ForgivingTitForTat();
    }
}