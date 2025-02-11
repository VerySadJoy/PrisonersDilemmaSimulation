package Strategy;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

// (보복형, 확률형) 최근 행동에 가중치를 둔 Probabilistic Tit-for-Tat 전략
//
// 전략의 작동 방식:
// - 첫 번째 라운드는 무조건 협력 (C)
// - 상대의 최근 5번의 행동을 각각 25%, 20%, 15%, 10%, 5%의 가중치로 반영
// - 그 이전의 선택들은 남은 25%를 균등하게 분배하여 협력 확률 계산
// - 확률적으로 협력/배신 결정
//
// 장점:
// - 상대의 최근 행동을 중요하게 고려하여 빠르게 반응 가능
// - 확률적 요소를 추가하여 상대가 예측하기 어려움
// - 멀티 플레이어 환경에서도 유연하게 적용 가능
//
// 단점:
// - 상대의 장기적인 성향을 덜 반영할 수도 있음
// - Forgiving Tit-for-Tat과 만나면 예측 불가능성이 협력에 방해가 될 수도 있음

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