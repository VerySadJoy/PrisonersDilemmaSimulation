package Strategy;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

// (착취형, 보복형) 그림자 속의 배신자, 신뢰를 시험하는 교활한 적응자  
//  
// ShadowDefect 전략은 초반에는 협력적인 모습을 보이지만,  
// 상대의 협력 비율을 분석한 후 특정 시점에 예측 불가능한 방식으로 배신하는 전략이다.  
//  
// 이 전략의 핵심은 상대가 협력적인 성향을 보일수록,  
// 불규칙적으로 배신을 섞어 신뢰를 시험하며 착취하는 것이다.  
//  
// 전략의 작동 방식:  
// - 첫 5라운드는 무조건 협력(C)하여 상대를 방심하게 만든다.  
// - 이후 상대의 협력 비율을 분석하여 반응을 조정.  
// - 상대의 협력 비율이 75% 이상이면, 20~50% 확률로 배신하여 신뢰를 이용하려 한다.  
// - 상대의 협력 비율이 40% 이하이면, 맞배신(D)하되 20% 확률로 협력하여 변칙적인 움직임을 보인다.  
// - 그 외의 경우, 기본적으로 협력을 유지하지만, 10라운드마다 30% 확률로 배신을 섞어 예측을 어렵게 만든다.  
//  
// 장점:  
// - 협력하는 상대에게 착취적인 플레이를 하면서도, 한 번에 완전한 적으로 돌리지 않는다.  
// - 보복형 전략(Tit-for-Tat, Grim Trigger)과 싸울 경우, 처음에는 협력하다가 적절한 시점에서 배신하여 균형을 조절할 수 있다.  
// - 불규칙한 배신 패턴을 통해 상대가 쉽게 예측하지 못하도록 만든다.  
//  
// 단점:  
// - Forgiving Tit-for-Tat 같은 전략에게는 일정 부분 용인되지만, 반복적인 배신이 감지되면 결국 보복을 받을 수 있다.  
// - 상대가 확률적 배신을 간파하면, 신뢰를 회복하기 어려워질 수 있다.  
// - 완전한 배신자(Always Defect)와 만나면 초반 협력으로 인해 손해를 볼 가능성이 크다.  
//  
// 실제 사회에서 이 전략을 따르는 인간 유형은 그림자 속의 배신자 또는 신뢰를 시험하는 교활한 적응자이다.  
// 이들은 처음에는 협력적인 모습을 보이지만, 상대가 신뢰를 보이면 이를 이용하여 예상치 못한 배신을 실행한다.  
// 상대가 신뢰를 주면 이를 시험하고, 상대가 강경한 태도를 보이면 신중하게 대응하며  
// 최적의 순간을 찾아 변칙적인 패턴을 통해 이득을 극대화하려 한다.  
// 하지만 이러한 태도는 장기적인 협력 관계를 유지하기 어렵게 만들 수 있다.  

public class ShadowDefect implements Strategy {
    private final Map<Player, Integer> opponentCooperationCount = new HashMap<>(); // 협력 횟수 기록
    private final Map<Player, Integer> totalRounds = new HashMap<>(); // 총 경기 수 기록
    private final Random random = new Random();

    @Override
    public boolean choose(Player self, Player opponent, List<Boolean> opponentHistory) {
        totalRounds.put(opponent, totalRounds.getOrDefault(opponent, 0) + 1);
        int rounds = totalRounds.get(opponent);
        
        // 상대의 협력 횟수를 먼저 업데이트
        if (!opponentHistory.isEmpty() && opponentHistory.get(opponentHistory.size() - 1)) {
            opponentCooperationCount.put(opponent, opponentCooperationCount.getOrDefault(opponent, 0) + 1);
        }

        // 상대의 협력 비율 계산
        int cooperationCount = opponentCooperationCount.getOrDefault(opponent, 0);
        double cooperationRate = (double) cooperationCount / rounds;

        // 첫 5라운드는 무조건 협력(C) -> 상대가 방심하게 만들기
        if (rounds <= 5) {
            return true;
        }

        // 배신 타이밍 조정 (더 예측 불가능하게)
        if (cooperationRate > 0.75) {
            // 상대가 협력 비율이 높다면 20~50% 확률로 배신
            return random.nextDouble() > 0.3; 
        } 
        else if (cooperationRate < 0.4) {
            // 상대가 원래 배신을 많이 하면 맞배신하되, 가끔은 협력할 수도 있음 (20% 확률 협력)
            return random.nextDouble() < 0.2;
        } 
        else {
            // 기본적으로 협력 유지하되, 10라운드마다 30% 확률로 배신
            return rounds % 10 == 0 ? random.nextDouble() > 0.7 : true;
        }
    }

    @Override
    public Strategy cloneStrategy() {
        return new ShadowDefect();
    }
}