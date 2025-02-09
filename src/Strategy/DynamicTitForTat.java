package Strategy;

import java.util.List;
import java.util.Random;
// (보복형, 협력형) 상황에 따라 유연하게 대응하는 자비로운 응징자, 신중한 분석가  
//  
// Dynamic Tit-for-Tat 전략은 기본적으로 Tit-for-Tat보다도 더 자비롭지만,  
// 상대가 최근 두 번 중 한 번이라도 배신했다면,  
// 상대의 협력 비율을 기반으로 확률적으로 배신하는 변형된 대응 전략이다.  
//  
// 이 전략의 핵심은 ‘상대가 최근에 배신한 적이 있으면 무조건 보복하는 것이 아니라,  
// 상대가 협력적이었는지를 고려하여 유연하게 대응하는 것’이다.  
//  
// 전략의 작동 방식:  
// - 첫 번째 라운드는 무조건 협력(C).  
// - 상대가 최근 2번 중 한 번이라도 배신(D)했을 경우,  
//   상대의 협력 비율을 기반으로 확률적으로 배신(D) 또는 협력(C) 선택.  
// - 상대가 높은 협력 비율을 유지하고 있다면, 높은 확률로 협력(C) 선택.  
// - 상대의 협력 비율이 낮다면, 낮은 확률로 협력(C)하고 보복 가능성 증가.  
//  
// 장점:  
// - Tit-for-Tat보다 더욱 유연하게 대응하여,  
//   실수로 배신한 상대와도 협력 관계를 유지할 가능성이 높다.  
// - 완전한 보복형(Tit-for-Tat, Two-Tits-for-Tat)보다 상대와의 관계를 덜 악화시킨다.  
// - 상대의 협력 비율을 기반으로 행동하므로, 상대가 협력적이면 나도 협력적으로 유지 가능.  
//  
// 단점:  
// - 상대가 완전한 배신 전략(Always Defect)일 경우,  
//   초반에는 협력 비율이 높게 나오므로 불필요한 협력을 유도할 위험이 있다.  
// - 랜덤형 전략(Probabilistic Tit-for-Tat, RandomStrategy)과 만나면,  
//   예측이 어렵기 때문에 비효율적으로 대응할 가능성이 있다.  
// - 상대가 패턴을 간파하면, 협력 비율을 조작하여  
//   의도적으로 배신을 회피하는 방법을 사용할 수도 있다.  
//  
// 실제 사회에서 이 전략을 따르는 인간 유형은 "상황에 따라 유연하게 대응하는 자비로운 응징자" 또는 "신중한 분석가"이다.  
// 이들은 상대의 과거 행동을 분석하여 적절한 수준의 보복을 실행하지만,  
// 무조건적인 응징보다는 상대의 성향을 고려하며 협력 가능성을 최대한 유지하려 한다.  
// 그러나 이러한 태도는 상대가 이를 악용할 경우, 쉽게 조작당할 위험도 있다.  

public class DynamicTitForTat implements Strategy {
    private final Random random = new Random(); // 확률적 선택을 위한 랜덤 객체

    @Override
    public boolean choose(Player self, Player opponent, List<Boolean> opponentHistory) {
        // 첫 번째 라운드는 기본적으로 협력(C)
        if (opponentHistory.isEmpty()) {
            return true;
        }

        int historySize = opponentHistory.size();
        boolean lastMove = opponentHistory.get(historySize - 1); // 상대의 마지막 행동

        // 상대가 최근 2번 중 1번이라도 배신했다면
        if (historySize > 1 && (!lastMove || !opponentHistory.get(historySize - 2))) {
            // 상대의 협력 비율을 계산
            long cooperations = opponentHistory.stream().filter(b -> b).count();
            double cooperationRate = (double) cooperations / historySize;

            // 협력 비율 확률로 협력 선택
            return random.nextDouble() < cooperationRate;
        }

        // 기본적으로 협력 유지
        return true;
    }

    @Override
    public Strategy cloneStrategy() {
        return new DynamicTitForTat();
    }
}