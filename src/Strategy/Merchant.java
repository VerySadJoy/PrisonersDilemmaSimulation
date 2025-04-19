package Strategy;

import java.util.List;

/**
 * 전략 이름: Merchant
 * 전략 개요: 상대의 마지막 행동과 반대되는 선택을 하여 이득을 추구하는 반응적 균형 전략
 *
 * 전략 구조:
 * - 초기 행동: 배신(D)으로 시작
 * - 행동 로직: 상대가 협력(C)했으면 배신(D), 배신(D)했으면 협력(C)
 * - 반응성: 있음 (직전 행동에 반응)
 * - 기억 활용: 낮음 (직전 1턴만 기억)
 * - 랜덤 요소: 없음 (결정적, 상대 행동에 기반)
 *
 * 전략 성향 분류:
 * - 협력 전략인가? → X (협력은 상대 배신 시에만, 순수 협력 아님)
 * - 배신 전략인가? → O (상대 협력 시에는 항상 배신)
 * - 패턴 기반인가? → O (상대와 반대되는 패턴 반복)
 * - 상대 반응형인가? → O (상대 직전 행동에 따라 결정)
 * - 예측 가능성 → 보통 (패턴 파악 후엔 쉽게 예측됨)
 *
 * 인간 유형 대응:
 * - 교활한 중개인 혹은 끊임없이 균형을 맞추려는 거래자
 * - 상대가 주는 행동에 즉각 반응하면서, 자신은 언제나 반대되는 방향으로 대응
 * - 신뢰를 직접 쌓기보다는 손익을 고려해 역으로 움직이는 경향이 있음
 * - 협력자에겐 이익을 취하고, 배신자에겐 교묘히 달래려는 태도로 인해
 *   장기적 관계는 어렵고, 결국엔 상대에게 간파당할 위험이 있음
*/

public class Merchant implements Strategy {
    private boolean lastMove; // 이전에 내가 했던 선택 (true = 협력, false = 배신)

    @Override
    public boolean choose(Player self, Player opponent, List<Boolean> opponentHistory) {
        if (opponentHistory.isEmpty()) {
            return false; // 첫 턴에는 초기값 사용
        }

        boolean opponentLastMove = opponentHistory.get(opponentHistory.size() - 1);
        lastMove = !opponentLastMove; // 상대가 협력하면 배신, 배신하면 협력
        return lastMove;
    }

    @Override
    public Strategy cloneStrategy() {
        return new Merchant(); // 새 전략 인스턴스 생성
    }
}