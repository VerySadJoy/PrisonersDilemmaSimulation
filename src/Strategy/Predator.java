package Strategy;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 전략 이름: Predator
 * 전략 개요: 협력적인 상대는 착취하고, 보복형 상대는 피하는 기회주의적 전략
 *
 * 전략 구조:
 * - 초기 행동: 첫 라운드 무조건 배신(D)하여 상대 성향 탐색
 * - 행동 로직:
 *   - 상대 협력 비율이 80% 이상 → 착취 대상으로 간주, 계속 배신
 *   - 협력 비율이 낮거나 보복형 성향 → 조용히 협력 유지
 * - 반응성: 있음 (상대의 누적 협력 비율 기반)
 * - 기억 활용: 중간 (상대의 전체 이력 활용)
 * - 랜덤 요소: 없음 (결정적 전략)
 *
 * 전략 성향 분류:
 * - 협력 전략인가? → X
 * - 배신 전략인가? → O (조건부 지속적 배신)
 * - 패턴 기반인가? → O (협력률에 따른 조건부 분기)
 * - 상대 반응형인가? → O (상대 행동 분석 기반 선택)
 * - 예측 가능성 → 보통 (조건 파악 후에는 고정됨)
 *
 * 인간 유형 대응:
 * - 강자를 피하고 약자를 사냥하는 포식자
 * - 순진하게 협력하는 이들에게는 무자비하게 이득을 취하지만,
 *   자신에게 위협이 되는 존재에겐 얌전히 굴며 문제를 피하려는 성향
 * - 이런 사람은 계산적이고 신중하지만, 신뢰를 얻기는 어려움
*/

public class Predator implements Strategy {
    private final Map<Player, List<Boolean>> opponentHistories = new ConcurrentHashMap<>(); // 안전한 기록 저장
    private final Map<Player, Boolean> isCooperatorMap = new ConcurrentHashMap<>(); // 협력가 여부 저장

    @Override
    public boolean choose(Player self, Player opponent, List<Boolean> opponentHistory) {
        // 안전한 리스트 관리
        opponentHistories.putIfAbsent(opponent, new CopyOnWriteArrayList<>(opponentHistory));
        List<Boolean> history = opponentHistories.get(opponent);

        // 첫 번째 턴은 무조건 배신(D)
        if (history.isEmpty()) {
            return false;
        }

        long betrayals;
        synchronized (history) {
            betrayals = history.stream().filter(b -> !b).count();
        }

        boolean isCooperator = betrayals < history.size() * 0.2; // 80% 이상 협력하면 순수 협력가로 간주
        isCooperatorMap.put(opponent, isCooperator);

        return !isCooperatorMap.get(opponent); // 협력가면 계속 배신, 보복형이면 협력
    }
    
    @Override
    public Strategy cloneStrategy() {
        return new Predator();
    }
}