package Strategy;

import java.util.List;
import java.util.Random;

/**
 * 전략 이름: GreedyTitForTat
 * 전략 개요: 기본 Tit-for-Tat을 따르되, 협력 시 10% 확률로 배신을 시도하는 탐욕적 변형 전략
 *
 * 전략 구조:
 * - 초기 행동: 협력(C)
 * - 행동 로직:
 *   - 상대가 직전에 협력했다면 → 협력하되 10% 확률로 배신
 *   - 상대가 배신했다면 → 그대로 맞배신
 * - 반응성: 있음 (상대의 마지막 행동에 반응)
 * - 기억 활용: 1턴 (직전 라운드)
 * - 랜덤 요소: 있음 (협력 상황에서 10% 확률로 배신)
 *
 * 전략 성향 분류:
 * - 협력 전략인가? → O (기본적으로 협력적이나, 배신을 삽입)
 * - 배신 전략인가? → △ (조건부 삽입 배신)
 * - 패턴 기반인가? → O (Tit-for-Tat 기반)
 * - 상대 반응형인가? → O (Tit-for-Tat 계열)
 * - 예측 가능성 → 보통 이하 (10% 랜덤 배신으로 인해 완전한 예측은 어려움)
 *
 * 인간 유형 대응:
 * - 교활한 탐욕가 혹은 신뢰를 배신하는 계산가
 * - 겉으로는 신뢰를 유지하고 협력하는 모습을 보이지만,
 *   기회가 오면 약간의 배신을 통해 이득을 취하려는 성향
 * - 협력 관계를 유지하고 싶어 하면서도, 자신만의 이익을 조금 더 얻고 싶어 하는 타입
 * - 그러나 이런 행동이 반복되면 결국 상대에게 들켜서,
 *   신뢰를 잃거나 강한 보복을 당할 위험이 커진다
*/ 

public class GreedyTitForTat implements Strategy {
    private final Random random = new Random(); // 배신할 확률 계산을 위한 랜덤 객체

    @Override
    public boolean choose(Player self, Player opponent, List<Boolean> opponentHistory) {
        // 첫 번째 라운드는 기본적으로 협력(C)
        if (opponentHistory.isEmpty()) {
            return true;
        }

        // 상대의 마지막 행동을 따라감 (기본적으로 팃포탯 방식)
        boolean shouldCooperate = opponentHistory.get(opponentHistory.size() - 1);

        // 협력을 하려는 상황이라면 10% 확률로 배신(D)
        if (shouldCooperate && random.nextDouble() < 0.1) {
            return false;
        }

        // 기본적으로 상대의 마지막 행동을 따라감
        return shouldCooperate;
    }
    
    @Override
    public Strategy cloneStrategy() {
        return new GreedyTitForTat();
    }
}