package Strategy;

import java.util.List;

/**
 * 전략 이름: AlwaysCooperate
 * 전략 개요: 어떤 상황에서도 무조건 협력(C)만 선택하는 절대 협력 전략
 *
 * 전략 구조:
 * - 초기 행동: 협력(C)
 * - 행동 로직: 항상 협력(C) — 상대가 무엇을 하든 변하지 않음
 * - 반응성: 없음 (상대의 배신에도 반응하지 않음)
 * - 기억 활용: 없음 (과거 기록과 무관)
 * - 랜덤 요소: 없음 (완전 결정적)
 *
 * 전략 성향 분류:
 * - 협력 전략인가? → O 절대 협력형
 * - 배신 전략인가? → X 없음
 * - 패턴 기반인가? → X (패턴조차 없음, 오직 협력)
 * - 상대 반응형인가? → X (모든 상대에게 동일한 행동)
 * - 예측 가능성 → 완전히 예측 가능
 *
 * 인간 유형 대응:
 * - 이상주의적 성인군자 혹은 순수한 낙천주의자
 * - 누구에게나 선의를 베풀며, 상대가 자신을 해쳐도 신뢰와 협력을 거두지 않음
 * - “세상은 협력으로 돌아가야 해”라는 믿음을 행동으로 실천하는 사람
 * - 하지만 그 순수함 때문에 현실에서는 자주 이용당하거나 희생당하기도 함
 * - 다른 사람이 배신해도 변하지 않기에, 때로는 어리석어 보이지만 동시에 감동을 주는 존재
 * - 이들이 많아질수록 세상은 더 나아질 수 있지만, 이들이 살아남기 어려운 세상이 문제
*/

public class AlwaysCooperate implements Strategy {
    @Override
    public boolean choose(Player self, Player opponent, List<Boolean> opponentHistory) {
        // 항상 협력 (C)을 선택함
        return true;
    }
    
    @Override
    public Strategy cloneStrategy() {
        return new AlwaysCooperate();
    }
}