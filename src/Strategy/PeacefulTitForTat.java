package Strategy;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
// (보복형, 협력형) 참을성 있는 화해자, 신중한 평화주의자  
//  
// Peaceful Tit-for-Tat 전략은 일반적인 Tit-for-Tat과 유사하지만,  
// 상대가 5번 연속으로 배신하면 한 번 협력하여 화해를 시도하는 보다 온화한 변형 전략이다.  
//  
// 이 전략의 핵심은 기본적으로는 보복하지만,  
// 일정 횟수 이상 상대가 배신을 지속하면 다시 협력하여 관계를 회복하려 한다는 것이다.  
//  
// 전략의 작동 방식:  
// - 첫 번째 라운드는 협력(C)한다.  
// - 상대가 협력하면 협력, 배신하면 배신하는 기본 Tit-for-Tat 로직을 따른다.  
// - 상대가 5번 연속 배신하면, 한 번 협력을 시도하여 신뢰를 회복할 기회를 준다.  
// - 이후 상대가 협력하면 다시 협력 모드로 복귀하지만,  
//   상대가 계속 배신하면 다시 보복을 이어간다.  
//  
// 장점:  
// - 실수로 배신한 상대와도 다시 협력 관계를 형성할 가능성이 크다.  
// - 완전한 보복형(Tit-for-Tat, Grim Trigger)보다 더 유연하게 상대를 대할 수 있다.  
// - 완전한 협력가(Always Cooperate)보다는 배신자를 억제하는 힘이 있다.  
//  
// 단점:  
// - 상대가 계속 배신하면 손해를 볼 가능성이 있다.  
// - Forgiving Tit-for-Tat보다 더 관대한 성향이므로, 일부 교활한 전략에게 이용당할 수도 있다.  
// - 상대가 패턴을 학습하면, 일부러 5번 배신 후 협력을 유도하는 방식으로 착취할 수 있다.  
//  
// 실제 사회에서 이 전략을 따르는 인간 유형은 "참을성 있는 화해자" 또는 "신중한 평화주의자"이다.  
// 이들은 상대가 실수로 배신했거나 적대적인 태도를 유지하더라도,  
// 일정한 시간이 지나면 화해의 손길을 내밀어 관계를 회복하려 한다.  
// 하지만 이러한 관대함이 악용될 경우, 상대에게 지속적인 손해를 입을 수도 있다.  

public class PeacefulTitForTat implements Strategy {
    private final ConcurrentHashMap<Player, Integer> consecutiveDefects = new ConcurrentHashMap<>(); // 연속 배신 횟수
    private final int FORGIVENESS_THRESHOLD = 5; // 5번 연속 배신 시 화해 시도

    @Override
    public boolean choose(Player self, Player opponent, List<Boolean> opponentHistory) {
        int roundsPlayed = opponentHistory.size();

        // 상대방 히스토리가 없으면 기본적으로 협력 (첫 턴)
        if (roundsPlayed == 0) {
            return true;
        }

        // 상대방의 마지막 행동
        boolean opponentLastMove = opponentHistory.get(roundsPlayed - 1);

        // 연속 배신 횟수 체크
        consecutiveDefects.putIfAbsent(opponent, 0);
        if (!opponentLastMove) { // 상대가 배신했다면
            consecutiveDefects.put(opponent, consecutiveDefects.get(opponent) + 1);
        } else { // 상대가 협력하면 연속 배신 횟수 리셋
            consecutiveDefects.put(opponent, 0);
        }

        // 만약 내가 5번 연속 배신했다면, 한 번 협력 시도
        if (consecutiveDefects.get(opponent) >= FORGIVENESS_THRESHOLD) {
            consecutiveDefects.put(opponent, 0); // 배신 카운트 리셋
            return true; // 화해의 손길!
        }

        // 기본적으로 Tit-for-Tat 방식 (상대가 협력하면 협력, 배신하면 배신)
        return opponentLastMove;
    }

    @Override
    public Strategy cloneStrategy() {
        return new PeacefulTitForTat();
    }
}