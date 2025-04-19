package Strategy;

import java.util.List;
import java.util.Random;
/**
 * 전략 이름: BadPerson
 * 전략 개요: 각 라운드에서 75% 확률로 배신(D), 25% 확률로 협력(C)을 선택하는 확률 기반 전략
 *
 * 전략 구조:
 * - 초기 행동: 무작위 (첫 턴부터 확률에 따라 결정)
 * - 행동 로직: 매 라운드마다 25% 확률로 협력(C), 75% 확률로 배신(D)
 * - 반응성: 없음 (상대의 행동과 무관)
 * - 기억 활용: 없음 (과거 이력 고려하지 않음)
 * - 랜덤 요소: 있음 (매 턴 확률적 결정)
 *
 * 전략 성향 분류:
 * - 협력 전략인가? → X (협력은 드물고 일관되지 않음)
 * - 배신 전략인가? → O (배신 확률이 높고, 기본 성향이 배신에 가까움)
 * - 패턴 기반인가? → X (패턴 없음, 순수 확률 기반)
 * - 상대 반응형인가? → X (상대에 대한 고려 없이 행동)
 * - 예측 가능성 → 낮음 (확률 기반으로 일관된 예측 어려움)
 *
 * 인간 유형 대응:
 * - 겉으론 가끔 친절하지만, 본질적으로는 신뢰할 수 없는 사람
 * - 기본적으로는 이용할 마음이지만, 너무 노골적이면 의심받으니 가끔 협력도 섞는 유형
 * - 관계 초반엔 헷갈리게 만들지만, 시간이 지나면 점차 본색이 드러남
 * - 장기적인 신뢰를 쌓기 어렵고, 결국은 모두에게 경계당하는 타입
 * - 사회적으로는 은근히 남 이용하는데, 티는 잘 안 나는 사람의 전형
*/

public class BadPerson implements Strategy {
    private final Random random = new Random();

    @Override
    public boolean choose(Player self, Player opponent, List<Boolean> opponentHistory) {
        // 75% 확률로 배신(false), 25% 확률로 협력(true)
        return random.nextDouble() < 0.25;
    }

    @Override
    public Strategy cloneStrategy() {
        return new BadPerson();
    }
}
