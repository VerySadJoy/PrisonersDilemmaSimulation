package Strategy;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 전략 이름: ProbabilisticWeightedTitForTat  
 * 전략 유형: 보복형 + 확률형 (최근 행동 중심의 확률적 대응자)
 *
 * 전략 개요:  
 * - 상대의 최근 행동에 높은 가중치를 두고 협력 확률을 계산하는 진화형 Tit-for-Tat  
 * - 과거 전체가 아닌 "최근 5턴"에 더 민감하게 반응하며,  
 *   확률적으로 협력 또는 배신을 결정하는 유연한 대응 전략
 *
 * 작동 방식:  
 * - 첫 라운드: 무조건 협력 (C)  
 * - 이후:  
 *   - 최근 5턴: 각각 25%, 20%, 15%, 10%, 5% 가중치 적용  
 *   - 그 이전 턴들: 남은 25% 가중치를 균등 분배  
 *   - 전체 협력 가중치를 합산하여 확률적 협력(C)/배신(D) 결정
 *
 * 장점:  
 * - 최근 행동에 즉각적으로 반응 → 빠른 적응  
 * - 확률 기반이라 상대가 예측하기 어려움  
 * - 멀티플레이 환경에서 전략 다변화 가능
 *
 * 단점:  
 * - 장기적 협력 관계 형성에는 불안정할 수 있음  
 * - Forgiving 계열 전략에게 오해받아 관계가 악화될 가능성  
 * - 극단적인 협력 or 배신자에겐 비효율적으로 작동할 수 있음
 *
 * 인간 대응 유형:  
 * - 최근 감정에 따라 사람을 판단하는 즉각 반응형 인간
 * - 장기보단 최근 분위기를 우선시하는 신중한 감각파  
 * - 협력도, 배신도 즉흥적일 수 있으나, 그 나름의 논리는 갖춘 분석형 반응자  
 */

public class ProbabilisticWeightedTitForTat implements Strategy {
    private final Random random = new Random(); // 확률 기반 행동 결정을 위한 랜덤 객체
    private final Map<Player, List<Boolean>> opponentHistories = new ConcurrentHashMap<>(); // 상대별 기록 저장

    @Override
    public boolean choose(Player self, Player opponent, List<Boolean> opponentHistory) {
        // 상대의 기록이 없으면 새로 생성하여 저장
        opponentHistories.putIfAbsent(opponent, new CopyOnWriteArrayList<>(opponentHistory));
        List<Boolean> history = opponentHistories.get(opponent);

        // 첫 번째 라운드는 기본적으로 협력(C)
        if (history.isEmpty()) {
            return true;
        }

        int size = history.size();
        double weightedSum = 0.0;
        double totalWeight = 0.0;

        // 최근 5개의 행동에 대한 가중치
        double[] weights = {0.25, 0.20, 0.15, 0.10, 0.05};

        // 최근 행동부터 거꾸로 계산 (최대 5개까지)
        for (int i = 0; i < Math.min(5, size); i++) {
            boolean action = history.get(size - 1 - i);
            double weight = weights[i];
            totalWeight += weight;
            if (action) weightedSum += weight;
        }

        // 나머지 오래된 행동들에 대한 가중치 계산
        if (size > 5) {
            double remainingWeight = 1.0 - totalWeight; // 남은 25%
            double oldWeight = remainingWeight / (size - 5); // 남은 행동들의 평균 가중치
            for (int i = 5; i < size; i++) {
                boolean action = history.get(size - 1 - i);
                totalWeight += oldWeight;
                if (action) weightedSum += oldWeight;
            }
        }

        // 최종 협력 확률 계산
        double cooperationProbability = weightedSum / totalWeight;

        // 확률적으로 협력 or 배신 결정
        return random.nextDouble() < cooperationProbability;
    }

    @Override
    public Strategy cloneStrategy() {
        return new ProbabilisticWeightedTitForTat();
    }
}