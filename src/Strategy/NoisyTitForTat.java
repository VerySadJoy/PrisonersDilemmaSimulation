package Strategy;

import java.util.List;
import java.util.Random;

/**
 * 전략 이름: NoisyTitForTat
 * 전략 개요: 상대의 마지막 행동을 80% 확률로 따라가는 변형된 Tit-for-Tat 전략
 *
 * 전략 구조:
 * - 초기 행동: 무조건 협력 (C)
 * - 행동 로직:
 *   - 이후 라운드부터는:
 *     - 상대가 직전에 협력했다면 → 80% 확률로 협력, 20% 확률로 배신
 *     - 상대가 직전에 배신했다면 → 80% 확률로 배신, 20% 확률로 협력
 * - 반응성: 중간 (상대 행동에 기반하되 확률적으로 반응)
 * - 기억 활용: 있음 (직전 라운드)
 * - 랜덤 요소: 있음 (20% 확률로 반대 행동)
 *
 * 전략 성향 분류:
 * - 협력 전략인가? → O (기본적으로 협력 중심)
 * - 배신 전략인가? → X (일부 확률적 배신 존재)
 * - 패턴 기반인가? → △ (패턴은 있지만 노이즈 포함)
 * - 상대 반응형인가? → O (직전 행동 기준)
 * - 예측 가능성 → 중간 (80%로 예측 가능, 20%는 불확실성)
 *
 * 인간 유형 대응:
 * - 예측 불가능한 협력자 혹은 실수를 가끔 하는 대응자
 * - 대체로 상대의 행동에 맞춰 움직이지만,
 *   때때로 불안정하거나 의외의 반응을 보이기도 함
 * - 신뢰를 쌓을 수는 있지만, 완벽하게 믿기는 어려운 존재
 * - 실수로 인해 갈등을 유발할 수 있으나, 악의는 없는 편
*/ 

public class NoisyTitForTat implements Strategy {
    private final Random random = new Random();

    @Override
    public boolean choose(Player self, Player opponent, List<Boolean> opponentHistory) {
        // 첫 번째 라운드는 무조건 협력(C)
        if (opponentHistory.isEmpty()) {
            return true;
        }

        // 상대의 마지막 행동을 가져옴
        boolean lastOpponentMove = opponentHistory.get(opponentHistory.size() - 1);

        // 80% 확률로 상대의 행동을 따라가고, 20% 확률로 반대 행동 선택
        return random.nextDouble() < 0.8 ? lastOpponentMove : !lastOpponentMove;
    }

    @Override
    public Strategy cloneStrategy() {
        return new NoisyTitForTat();
    }
}