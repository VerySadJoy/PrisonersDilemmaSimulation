package Strategy;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

// (보복형, 랜덤형) 극단적 복수자, 공멸을 불사하는 파괴자  
//  
// Mutual Destruction 전략은 초반에는 협력적인 모습을 보이다가,  
// 상대가 일정 수준 이상 배신하면 "함께 죽자" 모드로 전환하여 상대를 끝까지 따라가며 파괴하는 전략이다.  
//  
// 이 전략의 핵심은 상대가 일정 기준 이상 배신하면,  
// 나도 손해를 감수하고 끝까지 보복하여 상대도 살아남지 못하게 만드는 것이다.  
//  
// 전략의 작동 방식:  
// - 첫 10라운드 동안은 랜덤하게 협력과 배신을 섞어 탐색한다.  
// - 상대의 배신 비율이 40%를 초과하면 "함께 죽자" 모드(자폭 모드) 를 발동한다.  
// - 자폭 모드에서는 상대가 배신하면 무조건 배신하고,  
//   상대가 협력해도 50% 확률로 배신을 선택하여 신뢰 회복을 어렵게 만든다.  
//  
// 장점:  
// - 배신이 많은 상대에게 극단적인 보복을 가하며, 장기적으로 상대를 압박할 수 있다.  
// - 초반에는 협력을 섞기 때문에 상대가 방심하도록 만들 수 있다.  
// - 보복형 전략이지만 예측이 어렵고, 상대가 쉽게 조작할 수 없는 방식으로 운영된다.  
//  
// 단점:  
// - 한 번 자폭 모드가 발동되면 상대가 협력을 하더라도 관계를 회복하기 어렵다.  
// - Forgiving Tit-for-Tat 같은 전략과 만나면, 협력 기회를 날려버릴 위험이 있다.  
// - 상대가 처음부터 강한 배신 전략(Always Defect)일 경우, 오히려 손해를 볼 수도 있다.  
//  
// 실제 사회에서 이 전략을 따르는 인간 유형은 "극단적 복수자" 또는 "공멸을 불사하는 파괴자"이다.  
// 이들은 상대가 배신하면 일정 수준까지는 참고 보지만,  
// 그 기준을 넘어서면 자신도 손해를 감수하며 상대를 끝까지 끌고 내려가려 한다.  
// 이런 태도는 강한 경고 효과를 줄 수 있지만,  
// 때로는 불필요한 파국을 초래하여 서로에게 악영향을 미칠 수도 있다.  

public class MutualDestruction implements Strategy {
    private final ConcurrentHashMap<Player, Boolean> suicideMode = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<Player, Integer> betrayCount = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<Player, Integer> totalRounds = new ConcurrentHashMap<>();
    private final Random random = new Random();

    @Override
    public boolean choose(Player self, Player opponent, List<Boolean> opponentHistory) {
        suicideMode.putIfAbsent(opponent, false);
        betrayCount.putIfAbsent(opponent, 0);
        totalRounds.putIfAbsent(opponent, 0);

        int rounds = totalRounds.get(opponent);
        totalRounds.put(opponent, rounds + 1);

        // 첫 10라운드는 랜덤하게 협력/배신을 섞음
        if (rounds < 10) {
            boolean move = random.nextBoolean();
            if (!move) betrayCount.put(opponent, betrayCount.get(opponent) + 1);
            return move;
        }

        // 상대가 40% 이상 배신하면 함께 죽자 모드 활성화
        double betrayalRate = (double) betrayCount.get(opponent) / rounds;
        if (betrayalRate >= 0.4) {
            suicideMode.put(opponent, true);
        }

        // 함께 죽자 모드: 상대 배신하면 나도 배신, 상대 협력하면 50% 확률로 배신
        if (suicideMode.get(opponent)) {
            return opponentHistory.get(opponentHistory.size() - 1) || random.nextBoolean();
        }

        return true; // 평소엔 협력
    }

    @Override
    public Strategy cloneStrategy() {
        return new MutualDestruction();
    }
}