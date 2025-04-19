package Strategy;

import java.util.List;

/**
 * 전략 이름: Opportunist
 * 전략 유형: 기회주의형, 계산된 배신자
 *
 * 전략 개요:
 * - 협력을 기반으로 하지만, '서로 완벽히 신뢰하는 순간'에 배신하여 최대 보상을 노리는 전략
 *
 * 전략 작동 방식:
 * - 첫 라운드는 무조건 협력 (상대에게 신뢰의 출발점 제공)
 * - 직전 라운드에서 나와 상대가 모두 협력(C, C)한 경우 → 배신(D)
 * - 그 외에는 계속 협력
 *
 * 작동 의도:
 * - 상대가 협력을 지속하면, 방심한 틈을 노려 높은 점수 획득
 * - 상대가 배신 위주 전략일 경우, 굳이 맞배신하지 않고 계속 협력하여 피해 최소화
 *
 * 장점:
 * - 완전 협력형 전략에게 높은 보상을 얻을 기회가 많음
 * - 초반 신뢰를 얻기 쉬워 Tit-for-Tat류에게는 유리한 출발 가능
 * - 연속적 배신 없이도 이득을 취할 수 있음
 *
 * 단점:
 * - 행동 패턴이 매우 단순해서 쉽게 간파될 수 있음 (예: C,C 후 무조건 D)
 * - 보복 전략에게 들키면 신뢰를 빠르게 잃고 맞배신 루프에 빠짐
 * - Forgiving Tit-for-Tat 같은 전략과는 서로 오해하며 비효율적인 싸움을 할 수 있음
 *
 * 인간 대응 유형:
 * - 겉으로는 협력적인 태도를 유지하지만, 내심 계산적이며 타이밍을 노림
 * - 누군가를 100% 신뢰하지 않으며, 적절한 순간에 자기 이득을 위해 배신을 감행함
 * - 지나치게 반복되면 주위 사람들로부터 '믿을 수 없는 자'라는 평가를 받게 됨
*/

public class Opportunist implements Strategy {

    @Override
    public boolean choose(Player self, Player opponent, List<Boolean> opponentHistory) {
        // 첫 번째 라운드는 무조건 협력 (C)
        if (opponentHistory.isEmpty()) {
            return true;
        }

        // 직전 라운드 확인 (내 행동은 리스트 마지막, 상대 행동은 리스트에서 마지막-1)
        int lastRound = opponentHistory.size() - 1;
        // 이전 라운드에서 나와 상대가 둘 다 협력(C)했다면 배신(D)
        // 그 외의 경우 모두 협력 (C)
        return !(lastRound > 0 && opponentHistory.get(lastRound) && self.getMyLastHistory(opponent));
    }

    @Override
    public Strategy cloneStrategy() {
        return new Opportunist();
    }
}