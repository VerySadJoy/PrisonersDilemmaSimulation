package Strategy;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
// (랜덤형, 착취형) 패턴 붕괴자, 예측 불가능한 교란자  
//  
// PatternBreaker 전략은 상대의 행동 패턴을 분석한 후,  
// 예상 가능한 패턴을 깨뜨려 상대가 적응하지 못하게 만드는 변칙적이고 기만적인 전략이다.  
//  
// 이 전략의 핵심은 ‘상대가 예측할 수 없도록 행동을 조정하는 것’으로,  
// 특정 패턴(예: C-D-C-D 반복, 랜덤한 협력/배신, 완전한 협력가 또는 배신자)을 감지하면  
// 그에 맞춰 의도적으로 패턴을 깨뜨리는 방식으로 운영된다.  
//  
// 전략의 작동 방식:  
// - 초반 10라운드는 데이터 수집을 위해 협력(C)을 유지한다.  
// - 이후 상대의 협력률과 배신률을 분석하여 특정 패턴을 감지하면,  
//   그 패턴을 예상과 다르게 깨뜨리는 방식으로 대응한다.  
//  
// 패턴 분석 후 행동:  
// - 교대 배신자 (C-D-C-D) 감지 → 3의 배수 라운드에서 예측을 깨뜨리는 행동 실행  
// - 확률적 협력가(랜덤 대응) 감지 → 자체적으로 랜덤한 대응 유지 (50% 확률 협력/배신)  
// - 완전한 협력가(Always Cooperate) 감지 → 배신(D)하여 착취  
// - 완전한 배신자(Always Defect) 감지 → 가끔(5의 배수) 협력하여 상대를 헷갈리게 만듦  
// - 보복형 협력가(Tit-for-Tat, Grim Trigger) 감지 → 주기적으로 4라운드마다 배신을 섞어 균형 유지  
//  
// 장점:  
// - 상대가 예측하지 못하도록 행동 패턴을 조정하여, 단순한 전략을 가진 상대를 쉽게 공략할 수 있다.  
// - 완전한 협력가와 만나면 지속적으로 이득을 취할 수 있다.  
// - 보복형 전략과도 어느 정도 협력 가능성을 유지하면서, 상대를 흔들어 심리적으로 교란할 수 있다.  
//  
// 단점:  
// - 상대가 랜덤 전략을 사용하면, 패턴 분석이 어려워지고 오히려 불리해질 수 있다.  
// - 상대가 분석을 피하기 위해 자신의 패턴을 계속 바꾸면, 효과적으로 대응하기 어렵다.  
// - 지나치게 변칙적인 전략으로 인해, 협력 관계를 유지하기 어려울 수 있다.  
//  
// 실제 사회에서 이 전략을 따르는 인간 유형은 "패턴 붕괴자" 또는 "예측 불가능한 교란자"이다.  
// 이들은 상대가 특정한 행동 패턴을 보이면 이를 분석하고,  
// 의도적으로 예상과 다르게 행동하여 상대를 혼란스럽게 만들려는 성향을 가진다.  
// 이러한 성향은 교활하게 작용할 수도 있지만,  
// 지나치게 예측 불가능한 행동은 장기적인 신뢰 형성을 어렵게 만들 수 있다.  

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