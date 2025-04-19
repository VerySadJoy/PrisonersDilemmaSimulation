package Strategy;

import java.util.List;
import java.util.Random;

/**
 * 전략 이름: GoodPerson
 * 전략 개요: 75% 확률로 협력하고 25% 확률로 배신하는 확률 기반 전략
 *
 * 전략 구조:
 * - 초기 행동: 없음 (랜덤)
 * - 행동 로직: 매 라운드마다 75% 확률로 협력(C), 25% 확률로 배신(D)
 * - 반응성: 없음 (상대의 행동과 무관)
 * - 기억 활용: 없음
 * - 랜덤 요소: 있음 (확률적 결정, 고정된 비율)
 *
 * 전략 성향 분류:
 * - 협력 전략인가? → O (확률적 협력 중심)
 * - 배신 전략인가? → X (비우선)
 * - 패턴 기반인가? → X (패턴 없음)
 * - 상대 반응형인가? → X
 * - 예측 가능성 → 낮음 (확률 기반이지만 일정한 성향은 있음)
 *
 * 인간 유형 대응:
 * - 선의를 가진 평범한 사람, 대체로 믿을 수 있지만 완전히 믿기엔 불안한 성격
 * - 평소엔 협조적이며 사회적 관계를 중요시하지만, 가끔 뜬금없는 이기적인 행동을 하기도 함
 * - 감정이나 상황에 따라 의외의 선택을 할 수 있어 상대를 당황시키는 경우도 있음
 * - "얘는 착하긴 한데, 가끔 진짜 왜 저러지?" 싶은 타입
 * - 전체적으로는 협력적인 분위기를 유도하지만, 완벽한 신뢰가 필요한 상황에서는 아쉬움을 남길 수 있다
*/ 

public class GoodPerson implements Strategy {
    private final Random random = new Random();

    @Override
    public boolean choose(Player self, Player opponent, List<Boolean> opponentHistory) {
        // 75% 확률로 협력(true), 25% 확률로 배신(false)
        return random.nextDouble() < 0.75;
    }

    @Override
    public Strategy cloneStrategy() {
        return new GoodPerson();
    }
}