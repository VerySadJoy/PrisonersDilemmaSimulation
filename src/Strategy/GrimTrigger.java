package Strategy;

import java.util.List;

/**
 * 전략 이름: GrimTrigger
 * 전략 개요: 상대가 단 한 번이라도 배신하면 이후 무조건 배신만 하는 극단적 보복 전략
 *
 * 전략 구조:
 * - 초기 행동: 협력(C)
 * - 행동 로직:
 *   - 상대가 한 번도 배신하지 않았다면 계속 협력(C)
 *   - 단 한 번이라도 배신(D)을 했다면 → 이후 전 라운드에서 배신(D) 고정
 * - 반응성: 있음 (상대의 과거 전체를 기억)
 * - 기억 활용: 무한 (상대의 모든 과거 행동 추적)
 * - 랜덤 요소: 없음 (결정적)
 *
 * 전략 성향 분류:
 * - 협력 전략인가? → X (협력은 단 한 번의 배신 이후 종료됨)
 * - 배신 전략인가? → △ (조건부 극단적 배신)
 * - 패턴 기반인가? → X (단일 조건 기반)
 * - 상대 반응형인가? → O (상대의 과거 행동에 민감)
 * - 예측 가능성 → 매우 높음 (단 한 번 배신이 기준)
 *
 * 인간 유형 대응:
 * - 절대적인 응징자 또는 신뢰를 잃으면 끝장내는 복수자
 * - 처음에는 믿고 협력하지만, 한 번이라도 신뢰를 깨뜨리면
 *   이후에는 어떤 해명이나 회복의 여지도 없이 단절을 선택함
 * - 강력한 경고를 주는 전략으로, 얌체 전략이나 기회주의자들을 억제할 수 있으나
 *   단 한 번의 실수조차 용납하지 않는 가혹함이 단점
 * - 사회적으로는 상처를 잘 못 잊는 사람, 용서를 모르는 사람으로 비춰질 수 있음
*/

public class GrimTrigger implements Strategy {

    @Override
    public boolean choose(Player self, Player opponent, List<Boolean> opponentHistory) {
        // 첫 번째 라운드는 기본적으로 협력(C)
        if (opponentHistory.isEmpty()) {
            return true;
        }

        // 상대가 한 번이라도 배신(D)한 적이 있으면 이후로 계속 배신(D)
        return !opponentHistory.contains(false);
    }
    
    @Override
    public Strategy cloneStrategy() {
        return new GrimTrigger();
    }
}