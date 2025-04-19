package Strategy;
import java.util.List;

/**
 * 전략 이름: Gambler
 * 전략 개요: 자신의 평균 점수가 2.25를 넘는지 여부에 따라 협력(C) 또는 배신(D)을 결정하는 확률 기반 전략
 *
 * 전략 구조:
 * - 초기 행동: 협력 또는 배신 (자기 점수에 따라 다름)
 * - 행동 로직:
 *     - 평균 점수 > 2.25 → 배신(D)
 *     - 평균 점수 ≤ 2.25 → 협력(C)
 * - 반응성: 없음 (상대의 행동에 반응하지 않음)
 * - 기억 활용: 있음 (자신의 누적 점수 및 라운드 수 기반)
 * - 랜덤 요소: 없음 (점수 기반 결정적 행동)
 *
 * 전략 성향 분류:
 * - 협력 전략인가? → X (상황에 따라 협력하지만 협력 자체를 목표로 하지 않음)
 * - 배신 전략인가? → O (조건 만족 시 배신을 선택)
 * - 패턴 기반인가? → X
 * - 상대 반응형인가? → X (상대에 대한 고려 없이 자기 점수만 평가)
 * - 예측 가능성 → 중간 (점수 확인하면 예측 가능하지만 표면상은 불규칙)
 *
 * 인간 유형 대응:
 * - 위험 감수형 도박사, 확률적 계산가
 * - “지금 이익이 괜찮다면 더 크게 먹자”라는 마인드로 협력을 버리고 배신을 택함
 * - 반대로, 상황이 불리할 땐 굴욕을 감수하고 협력을 유지해 생존을 꾀함
 * - 협력과 배신의 기준이 상대가 아니라 ‘자신의 이득’이기 때문에,
 *   타인의 신뢰를 얻기는 어렵고, 공감 능력이 떨어지는 인상을 줄 수 있음
 * - 하지만 이성적인 환경에서는 수익을 극대화할 수 있는 계산형 전략이기도 함
*/

public class Gambler implements Strategy {
    private int roundsPlayed = 0;

    @Override
    public boolean choose(Player self, Player opponent, List<Boolean> opponentHistory) {
        roundsPlayed++;
        int averageScore = (roundsPlayed > 0) ? (self.getScore() / roundsPlayed) : 0;

        return averageScore > 2.25; // (5, 3, 1, 0)의 기댓값이 2.25
    }

    @Override
    public Gambler cloneStrategy() {
        return new Gambler();
    }
}