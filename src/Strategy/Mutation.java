package Strategy;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * 전략 이름: Mutation
 * 전략 개요: 평가 기반 협력-보복 최적화를 통해 진화적으로 적응하는 전략
 *
 * 전략 구조:
 * - 초기 행동: 무작위 (협력 또는 배신)
 * - 행동 로직:
 *   - 상대가 협력했을 때 → 평가값이 개선되면 협력 유지, 아니면 보복
 *   - 상대가 배신했을 때 → 평가값이 개선되면 협력, 아니면 보복
 *   - 보복 시에는 강한 변이, 협력 시에는 부드러운 탐색 수행
 * - 반응성: 높음 (상대의 직전 행동 + 평가 변화 모두 반영)
 * - 기억 활용: 있음 (이전 평가값, 위치값, 탐색/변이 계수 유지)
 * - 랜덤 요소: 있음 (초기 선택, 탐색 및 변이 방향)
 *
 * 전략 성향 분류:
 * - 협력 전략인가? → △ 협력도 하지만 목적함수 기준
 * - 배신 전략인가? → △ 배신도 하지만 성능 기반
 * - 패턴 기반인가? → X (명확한 반복 구조는 없음)
 * - 상대 반응형인가? → O (상대 행동과 평가값에 적응)
 * - 예측 가능성 → 낮음 (상대 평가와 확률에 따라 다르게 반응)
 *
 * 인간 유형 대응:
 * - 최적의 해를 찾아가는 실용주의자 혹은 진화론적 탐험가
 * - 자신이 기대하는 방향으로 상대가 반응했을 때는 온화하고 협력하지만,
 *   그렇지 않으면 날카롭게 방향을 틀어버리는 이중적 성향
 * - 결과 중심적이며, 도덕보다 효율을 우선시하는 타입
 * - 관계를 쌓는다기보다 상대를 통해 자신의 목표를 달성하려는 사고방식
 * - 실험, 관찰, 보복, 수정의 루틴을 통해 더 나은 결과를 추구하는 연구자형 플레이어
*/

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