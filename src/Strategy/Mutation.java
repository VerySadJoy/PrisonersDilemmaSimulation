package Strategy;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

// (적응형, 협력 & 보복 기반 최적화) 여러 플레이어와 협력과 보복을 수행하는 최적화 전략
//
// Mutation 전략은 Tit-for-Tat 원리를 최적화 문제에 적용하여,
// 여러 플레이어가 협력과 보복을 반복하면서 점진적으로 최적해를 찾아가는 방식으로 동작한다.
//
// 전략의 작동 방식:
// - 첫 번째 라운드는 랜덤한 선택을 함.
// - 이후에는 상대의 마지막 행동을 따라감 (협력 or 보복).
// - 상대가 좋은 결과(낮은 점수)를 얻었다면, 부드러운 탐색(협력).
// - 상대가 나쁜 결과(높은 점수)를 얻었다면, 강한 변이(보복).
//
// 장점:
// - 여러 플레이어가 동시에 최적화를 수행할 수 있음.
// - 협력과 보복을 통해 더 나은 해를 찾을 가능성이 높음.
// - 지역 최적해(local optimum)에 빠지지 않고 탐색을 지속할 수 있음.
//
// 단점:
// - 초기 탐색이 랜덤이므로 최적해를 찾는 데 시간이 걸릴 수 있음.
// - 보복 전략이 강하면 탐색이 불안정해질 가능성이 있음.
//

public class Mutation implements Strategy {
    private final Random random = new Random();
    
    // 각 플레이어별 평가값 저장 (최적화 성능 평가)
    private final Map<Player, Double> lastEvaluation = new HashMap<>();
    
    // 각 플레이어별 현재 위치 값 저장 (자신이 관리하는 최적화 변수)
    private final Map<Player, Double> currentValue = new HashMap<>();
    
    // 각 플레이어별 탐색 강도 (협력 시 부드러운 탐색)
    private final Map<Player, Double> explorationFactor = new HashMap<>();
    
    // 각 플레이어별 변이 강도 (보복 시 강한 변이)
    private final Map<Player, Double> mutationRate = new HashMap<>();

    @Override
    public boolean choose(Player self, Player opponent, List<Boolean> opponentHistory) {
        int rounds = opponentHistory.size();

        // 상대 플레이어의 currentValue를 초기화 (최초 만남 시)
        if (!currentValue.containsKey(opponent)) {
            currentValue.put(opponent, random.nextDouble() * 20 - 10); // -10 ~ 10 범위에서 랜덤 초기화
        }

        // 첫 번째 라운드는 랜덤 선택 & 탐색/변이 계수 초기화
        if (rounds == 0) {
            boolean firstAction = random.nextBoolean();

            // 탐색 및 변이 계수를 무작위로 초기화
            explorationFactor.put(opponent, 0.05 + random.nextDouble() * 0.1); // 0.05 ~ 0.15
            mutationRate.put(opponent, 0.1 + random.nextDouble() * 0.2); // 0.1 ~ 0.3

            return firstAction;
        }

        // 상대의 마지막 행동 가져오기
        boolean lastOpponentMove = opponentHistory.get(rounds - 1);

        // 현재 평가값 가져오기
        double currentEval = evaluate(opponent);

        // 이전 평가값이 없는 경우, 현재 평가값 저장 후 협력
        if (!lastEvaluation.containsKey(opponent)) {
            lastEvaluation.put(opponent, currentEval);
            return true;
        }

        // 이전 평가값과 현재 평가값 비교
        double previousEval = lastEvaluation.get(opponent);
        boolean shouldCooperate = currentEval < previousEval; // 성능이 개선되었으면 협력

        // 탐색 조절: 협력(부드러운 탐색) vs. 보복(강한 변이)
        boolean finalAction;
        if (shouldCooperate) {
            // 협력: 부드러운 탐색 (Exploration)
            finalAction = lastOpponentMove; // 상대의 행동을 따라감
            double newValue = currentValue.get(opponent) + explorationFactor.get(opponent) * (random.nextDouble() * 2 - 1);
            currentValue.put(opponent, newValue);
        }
        else {
            // 보복: 강한 변이 (Mutation)
            finalAction = !lastOpponentMove; // 상대의 행동을 반대로 함
            double newValue = currentValue.get(opponent) + mutationRate.get(opponent) * (random.nextDouble() * 2 - 1);
            currentValue.put(opponent, newValue);
        }

        // 현재 평가값 업데이트
        lastEvaluation.put(opponent, currentEval);

        return finalAction;
    }

    @Override
    public Strategy cloneStrategy() {
        return new Mutation();
    }

    // 평가 함수: 특정 목적 함수 기반으로 플레이어의 성능을 평가
    private double evaluate(Player opponent) {
        double x = currentValue.get(opponent); // 전략에서 관리하는 플레이어의 위치 값
        return objectiveFunction(x);
    }

    // 최적화할 목표 함수
    private double objectiveFunction(double x) {
        return x * x + 5 * Math.sin(x); // f(x) = x^2 + 5sin(x)
    }
}