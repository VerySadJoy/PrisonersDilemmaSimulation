package Strategy;
import java.util.List;

/**
 * 전략 이름: Flatterer
 * 전략 개요: 상대의 평균 점수가 자신보다 높으면 협력하고, 낮으면 배신하는 점수 기반 기회주의 전략
 *
 * 전략 구조:
 * - 초기 행동: 없음 (점수 비교는 항상 가능)
 * - 행동 로직:
 *     - 상대의 평균 점수가 자신보다 높으면 협력(C)
 *     - 상대의 평균 점수가 자신보다 낮으면 배신(D)
 * - 반응성: 있음 (상대의 점수에 반응)
 * - 기억 활용: 부분적 (전략 자체는 누적 점수 기반)
 * - 랜덤 요소: 없음 (완전 결정적)
 *
 * 전략 성향 분류:
 * - 협력 전략인가? → X (조건부 협력)
 * - 배신 전략인가? → O (점수가 높아지는 순간 자동 배신 전환)
 * - 패턴 기반인가? → X (점수 비교 기반)
 * - 상대 반응형인가? → O (상대 점수 기준)
 * - 예측 가능성 → 중간 (상대가 점수 알고 있다면 예측 가능)
 *
 * 인간 유형 대응:
 * - 기회주의적 아첨꾼, 계산적인 굴종자
 * - 강자 앞에서는 비굴하게 협력하며 관계 유지
 * - 약자를 만나면 거리낌 없이 배신하여 이득 취득
 * - 생존과 효율을 최우선으로 삼는 사람
 * - 단기적으로는 성과를 내지만, 반복적인 배신과 아첨은 결국 신뢰를 무너뜨려 고립을 초래할 수 있음
*/

public class Flatterer implements Strategy {
    @Override
    public boolean choose(Player self, Player opponent, List<Boolean> opponentHistory) {
        double myScore = (double) self.getScore() / self.getBattleCount();
        double opponentScore = (double) opponent.getScore() / opponent.getBattleCount();

        return myScore < opponentScore;  // 내가 높으면 배신, 내가 낮거나 같으면 협력
    }

    @Override
    public Strategy cloneStrategy() {
        return new Flatterer();
    }
}