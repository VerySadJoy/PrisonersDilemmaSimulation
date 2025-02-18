package Strategy;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

// (랜덤형, 착취형) 음흉한 협상가, 유동적인 생존주의자  
//  
// Deceptive Adaptation 전략은 상대의 협력 비율을 분석하여 유리한 방향으로 자신의 행동을 조정하는 변칙적이고 기회주의적인 전략이다.  
// 초반 5라운드 동안은 무조건 협력하여 상대의 성향을 파악하고, 이후 상대의 협력 비율에 따라 확률적으로 협력과 배신을 조절한다.  
//  
// 이 전략의 핵심은 신뢰를 구축하는 듯하다가, 예상치 못한 순간에 배신을 섞어 상대를 혼란스럽게 만드는 것이다.  
// 협력적인 상대에게는 협력을 유지하면서도 배신을 적절히 활용해 이득을 취하고,  
// 배신적인 상대에게는 보다 강하게 대응하여 손해를 최소화한다.  
//  
// 장점:  
// - 상대가 협력적일수록 더 많은 이득을 취할 수 있으며, 완전한 협력보다는 약간의 배신을 섞어 최대한의 보상을 노린다.  
// - 상대의 패턴을 분석하고 거기에 맞춰 대응하므로, 일관된 전략보다 더 높은 적응력을 갖춘다.  
// - 상대가 랜덤한 전략을 사용하더라도 일정한 확률을 유지하므로 크게 손해 보지 않는다.  
//  
// 단점:  
// - 상대가 보복형 전략(Tit-for-Tat, Grim Trigger)일 경우 배신이 감지되면 협력 관계가 깨지고 불리해질 수 있다.  
// - 협력과 배신이 혼합되어 있어, 상대가 패턴을 읽고 대응하면 효과가 약해질 수도 있다.  
// - 단기적으로는 효과적이지만, 장기적인 신뢰를 형성하기 어려울 수 있다.  
//  
// 실제 사회에서 이 전략을 따르는 인간 유형은 "음흉한 협상가" 또는 "유동적인 생존주의자"이다.  
// 사람들과 신뢰 관계를 유지하는 듯하면서도, 기회가 오면 언제든 배신을 선택할 수 있는 성향을 가진다.  
// 이들은 상대를 완전히 속이는 것이 아니라, 적절한 순간을 노려 약간의 기만을 활용하여 최상의 결과를 도출하려 한다.  
// 하지만, 이러한 전략이 들통나면 신뢰를 잃고 장기적으로 불리해질 위험이 크다.  

public class DeceptiveAdaptation implements Strategy {
    private final Map<Player, Integer> totalRounds = new ConcurrentHashMap<>();
    private final Map<Player, List<Boolean>> opponentHistories = new ConcurrentHashMap<>(); // 안전한 기록 저장
    private final Random random = new Random();

    @Override
    public boolean choose(Player self, Player opponent, List<Boolean> opponentHistory) {
        // 안전한 리스트 관리
        opponentHistories.putIfAbsent(opponent, new CopyOnWriteArrayList<>(opponentHistory));
        List<Boolean> history = opponentHistories.get(opponent);

        // 총 라운드 수 증가 (원자적 업데이트)
        totalRounds.compute(opponent, (k, v) -> (v == null) ? 1 : v + 1);
        int rounds = totalRounds.get(opponent);

        // 초반 5라운드는 무조건 협력하여 상대의 성향을 탐색
        if (rounds <= 5) {
            return true;
        }

        // 상대의 협력 비율 계산 (동기화된 블록)
        int coopCount;
        synchronized (history) {
            coopCount = (int) history.stream().filter(b -> b).count();
        }

        double coopRate = (double) coopCount / rounds;

        // 제어된 랜덤성 적용
        if (coopRate > 0.8) {
            return random.nextDouble() > 0.2; // 상대가 협력 위주라면 80% 협력, 20% 배신
        }
        else if (coopRate > 0.5) {
            return random.nextDouble() > 0.4; // 상대가 보복형 전략이면 60% 협력, 40% 배신
        }
        else {
            return random.nextDouble() > 0.6; // 상대가 배신 위주라면 40% 협력, 60% 배신
        }
    }
    
    @Override
    public Strategy cloneStrategy() {
        return new DeceptiveAdaptation();
    }
}