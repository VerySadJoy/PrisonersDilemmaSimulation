package Strategy;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

// (보복형, 랜덤형) 변덕스러운 확률적 대응자, 적응형 분석가  
//  
// Probabilistic Tit-for-Tat 전략은 상대의 협력 비율을 계산하여,  
// 해당 확률만큼 협력하고 나머지는 배신하는 변형된 Tit-for-Tat 전략이다.  
//  
// 이 전략의 핵심은 상대가 협력적일수록 나도 협력할 확률을 높이고,  
// 상대가 배신적일수록 협력 확률을 줄이는 것이다.  
// 즉, 단순한 Tit-for-Tat처럼 무조건 상대를 따라가는 것이 아니라,  
// 확률적으로 협력과 배신을 조정하여 변칙적인 패턴을 만든다.  
//  
// 전략의 작동 방식:  
// - 첫 번째 라운드는 무조건 협력(C).  
// - 이후 상대의 협력 비율을 계산하여 그 확률만큼 협력(C) 하고, 나머지는 배신(D).  
// - 예를 들어, 상대의 협력 비율이 80%라면 80% 확률로 협력(C), 20% 확률로 배신(D).  
//  
// 장점:  
// - 상대가 완전한 협력가(Always Cooperate)일 경우, 높은 확률로 협력을 유지할 수 있다.  
// - 상대가 보복형 전략(Tit-for-Tat, Grim Trigger)일 경우, 협력 기반의 관계를 유지하면서도 가끔 배신을 섞어 이득을 볼 수 있다.  
// - 확률적인 배신이 섞여 있기 때문에, 상대가 쉽게 예측하고 조작하기 어렵다.  
//  
// 단점:  
// - 상대의 협력 비율이 낮아지면, 협력 확률이 줄어들어 관계가 급격히 악화될 수 있다.  
// - Forgiving Tit-for-Tat 같은 전략과 만나면, 불필요한 배신으로 인해 협력 관계를 유지하기 어려울 수도 있다.  
// - 상대가 완전한 배신자(Always Defect)일 경우, 초반 협력으로 인해 큰 손해를 입을 수 있다.  
//  
// 실제 사회에서 이 전략을 따르는 인간 유형은 "변덕스러운 확률적 대응자" 또는 "적응형 분석가"이다.  
// 이들은 상대의 행동 패턴을 분석한 뒤,  
// 완벽한 협력이나 배신을 선택하는 것이 아니라 적절한 비율로 섞어 대응하려 한다.  
// 이러한 성향은 예측 불가능성을 증가시키지만,  
// 지나친 변동성이 상대에게 불신을 초래할 수도 있다.  

public class ProbabilisticTitForTat implements Strategy {
    private final Random random = new Random(); // 확률 기반 행동 결정을 위한 랜덤 객체
    private final Map<Player, List<Boolean>> opponentHistories = new ConcurrentHashMap<>(); // 안전한 기록 저장

    @Override
    public boolean choose(Player self, Player opponent, List<Boolean> opponentHistory) {
        // 리스트를 안전하게 저장하기 위해 CopyOnWriteArrayList 사용
        opponentHistories.putIfAbsent(opponent, new CopyOnWriteArrayList<>(opponentHistory));
        List<Boolean> history = opponentHistories.get(opponent);

        // 첫 번째 라운드는 기본적으로 협력(C)
        if (history.isEmpty()) {
            return true;
        }

        // 동기화된 블록에서 안전하게 접근
        double cooperationRate;
        synchronized (history) {
            long cooperationCount = history.stream().filter(b -> b).count();
            cooperationRate = (double) cooperationCount / history.size();
        }

        // 상대의 협력 비율만큼 확률적으로 협력(C)
        return random.nextDouble() < cooperationRate;
    }
    
    @Override
    public Strategy cloneStrategy() {
        return new ProbabilisticTitForTat();
    }
}