package Strategy;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

// (보복형, 협력형) 상처받기 쉬운 신뢰자, 감정적으로 반응하는 방어자  
//  
// GlassMind 전략은 상대에게 배신당하면 5라운드 동안 무조건 배신하지만,  
// 이후 상대가 협력하면 다시 신뢰를 회복하는 감정적이고 변덕스러운 보복형 전략이다.  
//  
// 초반 10라운드는 80% 확률로 협력하며 상대의 성향을 탐색하지만,  
// 한번 배신을 당하면 5라운드 동안 강한 보복을 가하며,  
// 이후 상대가 협력하면 다시 신뢰를 회복하는 방식으로 작동한다.  
//  
// 이 전략의 핵심은 배신당하면 쉽게 깨지지만, 일정 시간이 지나면 다시 신뢰를 회복하는 유리 같은 심리 상태이다.  
//  
// 장점:  
// - 상대가 지속적으로 협력한다면 협력적인 플레이를 유지할 수 있다.  
// - 보복형 전략이지만 완전히 단절되지 않으며, 시간이 지나면 관계를 회복할 수 있다.  
// - 상대가 실수로 배신한 경우에도 다시 협력으로 돌아갈 기회가 있다.  
//  
// 단점:  
// - 배신당하면 5라운드 동안 무조건 배신하므로, 보복형 전략과 만나면 불리해질 수 있다.  
// - 감정적으로 반응하는 듯한 방식이기 때문에, 상대가 이를 이용하면 착취당할 가능성이 있다.  
// - 강한 보복을 하지만 지속성이 부족하여, 교활한 전략에 의해 조종될 위험이 있다.  
//  
// 실제 사회에서 이 전략을 따르는 인간 유형은 "상처받기 쉬운 신뢰자" 또는 "감정적으로 반응하는 방어자"이다.  
// 한 번 신뢰하면 계속 협력하지만, 배신을 당하면 쉽게 깨지고 일정 기간 동안 강하게 반응한다.  
// 하지만 시간이 지나면 다시 신뢰를 회복하고 협력하려 하기 때문에,  
// 장기적인 관계에서는 신중하게 다뤄야 하는 민감한 성향을 가진다.  

public class GlassMind implements Strategy {
    private final ConcurrentHashMap<Player, Integer> betrayalCount = new ConcurrentHashMap<>(); // 상대에게 배신당한 횟수
    private final ConcurrentHashMap<Player, Integer> trustRecovery = new ConcurrentHashMap<>(); // 신뢰 회복 상태 (양수: 협력, 음수: 배신)
    private final int RECOVERY_PERIOD = 5; // 배신 후 5라운드 동안은 배신 모드
    private final int INITIAL_COOP_PERCENTAGE = 80; // 초반 10라운드 동안 협력 확률

    @Override
    public boolean choose(Player self, Player opponent, List<Boolean> opponentHistory) {
        int roundsPlayed = opponentHistory.size();

        // 초반 10라운드 동안 기본적으로 협력 (80%) 하지만 가끔 배신 (20%)
        if (roundsPlayed < 10) {
            return Math.random() < (INITIAL_COOP_PERCENTAGE / 100.0);
        }

        // 상대에게 배신당한 횟수 확인
        betrayalCount.putIfAbsent(opponent, 0);
        trustRecovery.putIfAbsent(opponent, 0);

        // 이전 라운드에서 상대방이 배신했는지 확인
        boolean opponentLastMove = opponentHistory.get(roundsPlayed - 1);
        if (!opponentLastMove) { // 상대가 배신한 경우
            betrayalCount.put(opponent, betrayalCount.get(opponent) + 1);
            trustRecovery.put(opponent, -RECOVERY_PERIOD); // 배신 모드 ON
        }

        // 배신 모드 (배신당한 후 5라운드 동안은 무조건 배신)
        if (trustRecovery.get(opponent) < 0) {
            trustRecovery.put(opponent, trustRecovery.get(opponent) + 1); // 회복 모드 증가
            return false; // 배신 지속
        }

        // 신뢰 회복 모드 (상대가 협력하면 다시 협력)
        if (opponentLastMove) {
            trustRecovery.put(opponent, RECOVERY_PERIOD); // 신뢰 회복 모드 ON
        }

        // 신뢰 회복이 끝난 상태면 협력
        return true;
    }

    @Override
    public Strategy cloneStrategy() {
        return new GlassMind();
    }
}