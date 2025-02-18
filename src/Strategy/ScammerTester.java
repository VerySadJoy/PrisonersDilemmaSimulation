package Strategy;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

// (착취형, 보복형) 첫인상으로 상대를 판단하는 직관적 사기꾼, 빠른 적응형 분석가  
//  
// ScammerTester 전략은 처음에는 무조건 배신(D)을 실행한 후,  
// 상대의 반응을 보고 협력할지, 보복할지, 혹은 변칙적인 전략을 사용할지를 결정하는 탐색형 전략이다.  
//  
// 이 전략의 핵심은 첫인상으로 상대의 성향을 판단한 뒤,  
// 가장 유리한 방식으로 즉시 전략을 조정하는 것이다.  
//  
// 전략의 작동 방식:  
// - 첫 번째 라운드는 무조건 배신(D)하여 상대의 반응을 탐색.  
// - 2라운드에서 상대가 보복(배신)했다면 → Tit-for-Tat 모드로 전환하여 보복 중심의 플레이 유지.  
// - 2라운드에서 상대가 협력했다면 → 이후 협력/배신을 번갈아가며 반복하는 패턴 플레이 시작.  
// - 어떤 조건에도 해당하지 않는 경우 → 기본적으로 배신을 유지.  
//  
// 장점:  
// - 상대의 성향을 빠르게 파악하고, 즉시 최적의 대응 전략으로 전환할 수 있다.  
// - 협력형 전략(Always Cooperate)과 만나면 협력/배신을 번갈아 반복하여 착취할 수 있다.  
// - 보복형 전략(Tit-for-Tat, Grim Trigger)과 만나면 보복 모드로 들어가 손해를 최소화할 수 있다.  
//  
// 단점:  
// - 첫 턴에서 무조건 배신하기 때문에, Forgiving Tit-for-Tat 같은 전략에게도 불필요한 적대감을 살 가능성이 있다.  
// - 상대가 교묘한 변칙 전략을 사용하면, 탐색 후 선택한 모드가 무력화될 수도 있다.  
// - 랜덤형 전략(Probabilistic Tit-for-Tat, RandomStrategy)과 만나면 정확한 판단이 어려워질 수 있다.  
//  
// 실제 사회에서 이 전략을 따르는 인간 유형은 "첫인상으로 상대를 판단하는 직관적 사기꾼" 또는 "빠른 적응형 분석가"이다.  
// 이들은 처음부터 상대를 시험한 후, 그 결과에 따라 즉시 태도를 바꾸는 빠른 적응력을 가진다.  
// 이러한 성향은 초반에는 유리하게 작용할 수 있지만,  
// 지나치게 직관적 판단에 의존하면 상대가 이를 역이용하여 패턴을 깨트릴 위험도 존재한다.  

public class ScammerTester implements Strategy {
    private final Map<Player, Boolean> switchedToTitForTat = new HashMap<>(); // 상대별 팃포탯 전환 여부
    private final Map<Player, Boolean> alternatingMode = new HashMap<>(); // 상대별 협력/배신 반복 모드
    private final Map<Player, Integer> roundCount = new HashMap<>(); // 상대별 라운드 번호

    @Override
    public boolean choose(Player self, Player opponent, List<Boolean> opponentHistory) {
        // 상대방과의 라운드 진행 횟수 증가
        int currentRound = roundCount.getOrDefault(opponent, 0) + 1;
        roundCount.put(opponent, currentRound);

        // 첫 라운드에서는 무조건 배신(D)
        if (opponentHistory.isEmpty()) {
            return false;
        }

        // 2라운드에서 상대가 보복(배신)했다면, 팃포탯 모드로 전환
        if (currentRound == 2 && !opponentHistory.get(opponentHistory.size() - 1)) {
            switchedToTitForTat.put(opponent, true);
        }

        // 팃포탯 모드일 경우 상대의 마지막 행동을 따라감
        if (switchedToTitForTat.getOrDefault(opponent, false)) {
            return opponentHistory.get(opponentHistory.size() - 1);
        }

        // 2라운드에서 상대가 협력했다면 협력/배신 반복 모드로 전환
        if (currentRound == 2 && opponentHistory.get(opponentHistory.size() - 1)) {
            alternatingMode.put(opponent, true);
        }

        // 협력/배신 반복 모드 (짝수 라운드에서는 협력, 홀수 라운드에서는 배신)
        if (alternatingMode.getOrDefault(opponent, false)) {
            return currentRound % 2 == 0;
        }

        // 기본적으로 배신 유지 (실제로 이 경로로 가는 경우는 없음)
        return false;
    }

    @Override
    public Strategy cloneStrategy() {
        return new ScammerTester();
    }
}