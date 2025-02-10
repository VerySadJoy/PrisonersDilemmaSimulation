package Strategy;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
// (착취형, 보복형) 신의 손길을 가장한 기만자, 서서히 배신하는 위선자  
//  
// HandOfGod 전략은 초반에는 협력적인 태도를 보이지만,  
// 상대가 충분히 협력적이라고 판단되면 점진적으로 착취하는 변칙적인 전략이다.  
//  
// 이 전략의 핵심은 ‘상대를 속여 협력하게 만든 뒤,  
// 일정 시점 이후부터 배신을 섞어 착취하는 것’이다.  
//  
// 초반 10라운드 동안 무조건 협력하며 상대를 안심시키고,  
// 상대의 협력 비율이 80% 이상을 일정 기간 유지하면 착취 모드에 돌입한다.  
// 착취 모드에서는 4라운드마다 배신하며, 상대가 눈치채지 못하도록 점진적으로 배신 비율을 증가시킨다.  
//  
// 또한, 보복형 전략에 대해서는 30% 확률로 배신을 섞어 탐색하며,  
// 완전한 배신 전략(Always Defect)에게는 가끔 협력하여 상대를 교란하려 한다.  
//  
// 장점:  
// - 상대가 착취 모드를 눈치채지 못하면, 장기적인 협력 속에서 높은 이득을 취할 수 있다.  
// - 완전한 배신이 아니므로 보복형 전략과도 일정 수준에서 협력을 유지할 가능성이 있다.  
// - 보복형이지만 너무 가혹하지 않아서, 협력 유지가 가능한 상대에게 유리하다.  
//  
// 단점:  
// - 상대가 착취 패턴을 인지하면 강한 보복을 받을 가능성이 크다.  
// - Tit-for-Tat처럼 상대가 배신을 용인하지 않는 전략과 만나면 협력 관계가 깨질 수 있다.  
// - 완전한 배신 전략과 만나면 협력 기회가 줄어들어 효과적인 착취가 어려울 수 있다.  
//  
// 실제 사회에서 이 전략을 따르는 인간 유형은 "신의 손길을 가장한 기만자" 또는 "서서히 배신하는 위선자"이다.  
// 이들은 겉으로는 협력적인 태도를 보이며 신뢰를 구축하지만,  
// 일정 시점이 지나면 서서히 배신을 섞어 상대를 착취한다.  
// 상대가 이를 눈치채지 않도록 점진적으로 배신을 늘리는 것이 특징이며,  
// 상대의 성향을 탐색하면서 적절한 시점에 배신을 가하는 교활한 전략을 취한다.  

public class HandOfGod implements Strategy {
    private final Map<Player, Integer> opponentCooperationCount = new ConcurrentHashMap<>(); // 협력 횟수 기록
    private final Map<Player, Integer> totalRounds = new ConcurrentHashMap<>(); // 총 경기 수 기록
    private final Map<Player, Boolean> exploitMode = new ConcurrentHashMap<>(); // 착취 모드 여부
    private final Map<Player, Integer> consecutiveHighCooperation = new ConcurrentHashMap<>(); // 연속 협력 횟수 기록
    private final Random random = new Random(); // 랜덤한 행동을 위한 변수

    @Override
    public boolean choose(Player self, Player opponent, List<Boolean> opponentHistory) {
        // 값 초기화
        totalRounds.putIfAbsent(opponent, 0);
        opponentCooperationCount.putIfAbsent(opponent, 0);
        exploitMode.putIfAbsent(opponent, false);
        consecutiveHighCooperation.putIfAbsent(opponent, 0);

        // 라운드 수 증가
        totalRounds.put(opponent, totalRounds.get(opponent) + 1);
        int rounds = totalRounds.get(opponent);

        // 첫 10라운드는 무조건 협력(C)
        if (rounds <= 10) {
            return true;
        }

        // 상대의 협력 횟수를 먼저 업데이트
        if (!opponentHistory.isEmpty() && opponentHistory.get(opponentHistory.size() - 1)) {
            opponentCooperationCount.put(opponent, opponentCooperationCount.get(opponent) + 1);
        }

        int cooperationCount = opponentCooperationCount.get(opponent);
        double cooperationRate = (double) cooperationCount / rounds;

        // 착취 모드 진입 조건
        if (cooperationRate >= 0.8) {
            consecutiveHighCooperation.put(opponent, consecutiveHighCooperation.get(opponent) + 1);
            if (consecutiveHighCooperation.get(opponent) >= 5) {
                exploitMode.put(opponent, true);
            }
        } else {
            consecutiveHighCooperation.put(opponent, 0);
        }

        boolean isExploiting = exploitMode.get(opponent);

        // 착취 모드 적용
        if (isExploiting) {
            return rounds % 4 != 0; // 4라운드마다 배신 (점진적 착취)
        }

        // 보복형 전략 대응 (60% 이상 협력)
        if (cooperationRate > 0.6) {
            return random.nextDouble() > 0.3; // 30% 확률로 배신
        }

        // 배신자가 협력할 가능성도 테스트
        if (cooperationRate < 0.3) {
            return random.nextDouble() < 0.2; // 20% 확률로 협력 (상대가 협력할 가능성 테스트)
        }

        // 애매한 상대(협력-배신 혼합형) 대응
        return !opponentHistory.get(opponentHistory.size() - 1); // 기본적으로 상대 행동을 따라감
    }

    @Override
    public Strategy cloneStrategy() {
        return new HandOfGod();
    }
}