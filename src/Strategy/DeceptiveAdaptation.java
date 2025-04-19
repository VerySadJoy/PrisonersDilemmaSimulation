package Strategy;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 전략 이름: DeceptiveAdaptation
 * 전략 개요: 초반엔 협력하며 상대의 성향을 파악하고, 이후 상대의 협력 비율에 따라 확률적으로 배신을 섞는 적응형 기만 전략
 *
 * 전략 구조:
 * - 초기 행동: 첫 5라운드는 무조건 협력(C)
 * - 행동 로직:
 *     - 이후부터는 상대의 협력 비율을 기반으로 확률 조정
 *     - 협력률 > 80% → 80% 협력, 20% 배신
 *     - 협력률 > 50% → 60% 협력, 40% 배신
 *     - 협력률 ≤ 50% → 40% 협력, 60% 배신
 * - 반응성: 있음 (상대 성향 분석 및 행동 조정)
 * - 기억 활용: 있음 (상대별 협력 비율 기록)
 * - 랜덤 요소: 있음 (확률 기반 의사결정)
 *
 * 전략 성향 분류:
 * - 협력 전략인가? → X (협력을 유지하되, 지속적으로 배신을 섞음)
 * - 배신 전략인가? → X (전략적으로 협력을 유지함)
 * - 패턴 기반인가? → X (고정 패턴 없음)
 * - 상대 반응형인가? → O (상대 협력률 기반 적응형)
 * - 예측 가능성 → 낮음 (확률 기반이기 때문에 불규칙)
 *
 * 인간 유형 대응:
 * - 협력을 가장한 전략적 기회주의자
 * - 초반에는 신뢰를 쌓고 친근하게 다가오지만, 상대의 성향을 분석한 뒤 유리한 순간에만 배신을 시도함
 * - 대화에선 "난 믿음 중요시해"라면서도, 뒤에선 계산기 두드리는 타입
 * - 사회적으로는 성공 지향적이지만, 진심이 없다고 느껴질 수 있으며 장기적으로는 신뢰를 잃을 위험이 있음
 * - 다만, 적응력이 뛰어나기 때문에 단기적 성과는 매우 높을 수 있음
*/

public class DeceptiveAdaptation implements Strategy {
    private final Map<Player, Integer> totalRounds = new ConcurrentHashMap<>();
    private final Map<Player, List<Boolean>> opponentHistories = new ConcurrentHashMap<>(); // 안전한 기록 저장
    private final Random random = new Random();

    @Override
    public boolean choose(Player self, Player opponent, List<Boolean> opponentHistory) {
        // 안전한 리스트 관리
        opponentHistories.putIfAbsent(opponent, new CopyOnWriteArrayList<>(opponentHistory));
        List<Boolean> history = opponentHistories.get(opponent);

        // 총 라운드 수 증가 (원자적 업데이트)
        totalRounds.compute(opponent, (k, v) -> (v == null) ? 1 : v + 1);
        int rounds = totalRounds.get(opponent);

        // 초반 5라운드는 무조건 협력하여 상대의 성향을 탐색
        if (rounds <= 5) {
            return true;
        }

        // 상대의 협력 비율 계산 (동기화된 블록)
        int coopCount;
        synchronized (history) {
            coopCount = (int) history.stream().filter(b -> b).count();
        }

        double coopRate = (double) coopCount / rounds;

        // 제어된 랜덤성 적용
        if (coopRate > 0.8) {
            return random.nextDouble() > 0.2; // 상대가 협력 위주라면 80% 협력, 20% 배신
        }
        else if (coopRate > 0.5) {
            return random.nextDouble() > 0.4; // 상대가 보복형 전략이면 60% 협력, 40% 배신
        }
        else {
            return random.nextDouble() > 0.6; // 상대가 배신 위주라면 40% 협력, 60% 배신
        }
    }
    
    @Override
    public Strategy cloneStrategy() {
        return new DeceptiveAdaptation();
    }
}