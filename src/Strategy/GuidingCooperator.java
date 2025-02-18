package Strategy;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

// (협력형, 보복형) 인내심 있는 유도자, 점진적 교육자  
//  
// Guiding Cooperator 전략은 상대를 협력하도록 유도하는 교육적 성향을 가진 전략이다.  
// 상대가 처음에는 배신하더라도 쉽게 보복하지 않고,  
// 단계적으로 반응하여 상대를 협력적인 행동으로 이끌어내려고 한다.  
//  
// 이 전략의 핵심은 ‘너그럽게 상대를 인내하며, 점진적으로 협력을 가르치는 것’이다.  
// 초반 5라운드 동안은 무조건 협력하여 상대가 협력을 유지할 기회를 제공하며,  
// 상대가 지속적으로 배신하면 점진적으로 보복을 시작한다.  
//  
// 전략의 작동 방식:  
// - 첫 5라운드는 무조건 협력하여 상대가 협력할 기회를 제공.  
// - 상대가 한 번 배신하면 여전히 협력하여 실수를 용서.  
// - 두 번 연속 배신하면 강한 보복(배신) 시작.  
// - 세 번째 배신부터는 한 라운드씩 교대로 보복하며 협력 유도를 시도.  
//  
// 장점:  
// - 상대가 실수로 배신했을 경우, 협력적인 관계를 지속할 수 있다.  
// - 완전한 배신 전략(Always Defect)과 만나도 일정 부분 협력을 이끌어낼 가능성이 있다.  
// - 무조건적인 보복보다 유연한 대응을 하여, 협력 유지 가능성이 높다.  
//  
// 단점:  
// - 상대가 보복을 무시하고 배신을 반복하면 결국 손해를 볼 가능성이 크다.  
// - Forgiving Tit-for-Tat보다 관대한 성향이므로, 일부 착취 전략에게 쉽게 이용당할 수 있다.  
// - 상대가 교묘하게 배신을 섞는다면, 이를 간파하고 대응하는 것이 어렵다.  
//  
// 실제 사회에서 이 전략을 따르는 인간 유형은 "인내심 있는 유도자" 또는 "점진적 교육자"이다.  
// 이들은 상대가 실수하더라도 쉽게 보복하지 않고,  
// 꾸준한 협력과 점진적인 보복을 통해 상대를 협력적인 방향으로 유도하려 한다.  
// 하지만 이러한 전략이 지나치게 관대하면,  
// 교활한 상대에게 이용당하거나 배신을 당할 위험이 존재한다.  

public class GuidingCooperator implements Strategy {
    private final Map<Player, Integer> opponentDefectionStreak = new ConcurrentHashMap<>(); // 연속 배신 횟수 기록
    private final Map<Player, Integer> totalRounds = new ConcurrentHashMap<>(); // 상대별 총 경기 횟수

    @Override
    public boolean choose(Player self, Player opponent, List<Boolean> opponentHistory) {
        totalRounds.computeIfAbsent(opponent, k -> 0);
        opponentDefectionStreak.computeIfAbsent(opponent, k -> 0);

        // 동기화하여 안전한 값 증가
        synchronized (totalRounds) {
            totalRounds.put(opponent, totalRounds.get(opponent) + 1);
        }

        int rounds = totalRounds.get(opponent);

        // 첫 5라운드는 무조건 협력 (상대를 협력 모드로 유도)
        if (rounds <= 5) {
            return true;
        }

        // 안전한 상대 배신 여부 확인
        boolean lastMoveWasDefection = !opponentHistory.isEmpty() && !opponentHistory.get(opponentHistory.size() - 1);

        // 동기화하여 안전한 배신 연속 기록
        synchronized (opponentDefectionStreak) {
            int defectionStreak = opponentDefectionStreak.getOrDefault(opponent, 0);
            if (lastMoveWasDefection) {
                opponentDefectionStreak.put(opponent, defectionStreak + 1);
            } else {
                opponentDefectionStreak.put(opponent, 0); // 협력하면 다시 초기화
            }
        }

        int defectionStreak = opponentDefectionStreak.get(opponent);

        // 유도 전략 로직
        return switch (defectionStreak) {
            case 0 -> true;
            case 1 -> true;
            case 2 -> false;
            default -> rounds % 2 == 0;
        }; // 상대가 협력하거나, 한 번 배신했으면 협력 유지
        // 두 번째 배신까지는 참고 협력 (상대를 테스트)
        // 세 번째 배신부터 반격 시작
        // 이후에는 교대로 보복하여 상대를 협력으로 유도
    }
    
    @Override
    public Strategy cloneStrategy() {
        return new GuidingCooperator();
    }
}