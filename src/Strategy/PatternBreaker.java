package Strategy;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 전략 이름: PatternBreaker
 * 전략 유형: 랜덤형 + 착취형 (패턴 붕괴자, 교란 전략)

 * 전략 개요:
 * - 상대의 반복적인 패턴을 분석한 뒤, 예상과는 다른 방식으로 대응하여 혼란을 유도하는 전략
 * - 단순히 반응하는 것이 아니라, 분석 후 "의도적"으로 반대로 움직임

 * 작동 방식:
 * - 초반 10라운드는 협력(C)을 유지하면서 상대 데이터 수집
 * - 이후 협력률/배신률 기반으로 패턴 분류 및 반패턴 행동 실행

 * 분류 및 반응:
 * 1. 교대형(C-D-C-D): 협력률이 45~55% → 3의 배수 라운드에서 패턴 깨기 (예측 교란)
 * 2. 랜덤형: 협력률이 30~70% → 대응도 랜덤(50:50)
 * 3. Always Cooperate: 협력률 90% 이상 → 무조건 배신 (착취)
 * 4. Always Defect: 배신률 90% 이상 → 5의 배수마다 협력 (혼란 유발)
 * 5. 보복형 협력가: 협력률 60% 이상 → 대부분 협력하지만 4의 배수에서 배신 한 번

 * 장점:
 * - 상대가 단순한 전략일 경우, 완벽하게 교란 가능
 * - Always Cooperate 같은 순진한 전략은 완전히 공략 가능
 * - 일정 부분 보복형 전략과도 밸런스를 맞출 수 있음

 * 단점:
 * - 너무 랜덤한 상대(예: NoisyTitForTat, Random 등)에게는 분석 효과가 없음
 * - 지나치게 계산적이고 예측 불가능한 전략이라 신뢰 구축이 어려움
 * - 장기적 협력이 중요한 시나리오에서는 오해를 살 가능성 있음

 * 인간 대응 유형:
 * - 항상 분석하고, 상대의 반복을 깨뜨리려는 교란 전문가
 * - 일종의 메타 전략 플레이어로, 상황을 지배하고 싶어하는 성향
 * - 하지만 신뢰를 기반으로 한 협력 사회에서는 위험 요소가 될 수 있음
*/ 

public class PatternBreaker implements Strategy {
    private final Map<Player, Integer> opponentCooperationCount = new ConcurrentHashMap<>();
    private final Map<Player, Integer> opponentDefectionCount = new ConcurrentHashMap<>();
    private final Map<Player, Integer> totalRounds = new ConcurrentHashMap<>();
    private final Random random = new Random();

    @Override
    public boolean choose(Player self, Player opponent, List<Boolean> opponentHistory) {
        // computeIfAbsent()로 안전한 초기화
        totalRounds.computeIfAbsent(opponent, k -> 0);
        opponentCooperationCount.computeIfAbsent(opponent, k -> 0);
        opponentDefectionCount.computeIfAbsent(opponent, k -> 0);

        totalRounds.put(opponent, totalRounds.get(opponent) + 1);
        int rounds = totalRounds.get(opponent);

        // 첫 10라운드는 데이터 수집을 위해 기본적으로 협력
        if (rounds <= 10) {
            return true;
        }

        // 안전한 협력/배신 횟수 기록
        int coopCount = opponentCooperationCount.get(opponent);
        int defectCount = opponentDefectionCount.get(opponent);

        if (!opponentHistory.isEmpty()) {
            boolean lastMove = opponentHistory.get(opponentHistory.size() - 1);
            if (lastMove) {
                opponentCooperationCount.put(opponent, coopCount + 1);
            } else {
                opponentDefectionCount.put(opponent, defectCount + 1);
            }
        }

        double coopRate = (double) coopCount / Math.max(1, rounds);  // 0으로 나누는 문제 방지
        double defectRate = (double) defectCount / Math.max(1, rounds);

        // 상대 패턴 분석 후 패턴 깨기

        // 패턴 깨기
        if (coopRate > 0.45 && coopRate < 0.55) {  // C-D-C-D 같은 일정 패턴 감지
            return rounds % 3 == 0;  // 주기적으로 패턴 깨뜨림 (예상과 다르게 행동)
        }

        // 확률적 협력가(랜덤 대응) 감지
        if (coopRate > 0.3 && coopRate < 0.7) {  // 너무 랜덤하게 행동하는 경우
            return random.nextBoolean();  // 랜덤으로 대응 (50% 확률)
        }

        // 완전한 협력가(Always Cooperate) 착취
        if (coopRate > 0.9) {
            return false;  // 착취 (배신)
        }

        // 완전한 배신자(Always Defect) 유인
        if (defectRate > 0.9) {
            return rounds % 5 == 0;  // 가끔 협력해서 상대를 헷갈리게 만듦
        }

        // 보복형 협력가(T4T, Grim Trigger) 조심스럽게 대응
        if (coopRate > 0.6) {
            return rounds % 4 != 0;  // 협력 유지하며 가끔 배신 (균형 유지)
        }

        // 기본적으로 협력 유지 (패턴 분석이 끝나지 않은 경우)
        return true;
    }

    
    @Override
    public Strategy cloneStrategy() {
        return new PatternBreaker();
    }
}