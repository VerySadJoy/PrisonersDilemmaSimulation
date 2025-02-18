package Strategy;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

// (랜덤형, 착취형) 충동적인 도약자, 감정적 반응형 플레이어  
//  
// Grasshopper 전략은 직전 라운드에서 얻은 점수를 기반으로 협력과 배신을 결정하는 변칙적인 전략이다.  
// 전략명 메뚜기는 일정한 패턴 없이 여기저기 도약하는 듯한 랜덤성과 변덕스러움을 반영한다.  
//  
// 이 전략의 핵심은 단기적인 이득에 따라 즉흥적으로 반응하는 것이다.  
// 특정한 장기적인 패턴 없이, 이전 라운드에서 얻은 점수를 분석하고  
// 그 결과에 따라 협력과 배신을 결정하는 경향을 보인다.  
//  
// 전략의 작동 방식:  
// - (C, C)로 6점을 얻으면 → 랜덤한 선택 (상황을 지속할지, 변화를 줄지 고민하는 성향)  
// - (C, D) 또는 (D, C)로 5점을 얻으면 → 상대의 행동에 따라 다름 (상황을 바꾸려 하거나 유지하려 함)  
// - (D, D)로 2점을 얻으면 → 확실한 배신 (손해를 봤다고 판단)  
//  
// 장점:  
// - 특정한 규칙 없이 변칙적으로 움직이기 때문에 상대가 예측하기 어렵다.  
// - 협력과 배신을 적절히 섞으며 상대에 대한 즉각적인 피드백을 제공할 수 있다.  
// - 보복형 전략과 만났을 때, (C, C)를 유지하면 추가적인 이득을 볼 가능성이 높다.  
//  
// 단점:  
// - 장기적인 관계를 고려하지 않고 즉흥적으로 판단하기 때문에 신뢰를 형성하기 어렵다.  
// - 보복형 전략(Tit-for-Tat, Grim Trigger)과 싸울 경우 쉽게 배신을 반복하여 협력을 이끌어내기 어렵다.  
// - 랜덤성이 포함되어 있어, 특정 상황에서 불리한 선택을 할 수도 있다.  
//  
// 실제 사회에서 이 전략을 따르는 인간 유형은 "충동적인 도약자" 또는 "감정적 반응형 플레이어"이다.  
// 이들은 장기적인 계획 없이 순간적인 이득과 손해에 민감하게 반응하며 전략을 조정한다.  
// 때로는 예측 불가능한 행동으로 상대를 혼란스럽게 만들지만,  
// 지나치게 충동적인 선택이 누적될 경우 신뢰를 잃거나 장기적인 손해를 볼 수도 있다.  

public class Grasshopper implements Strategy {
    private final Map<Player, Integer> lastRoundScores = new ConcurrentHashMap<>();
    private final Random random = new Random();

    @Override
    public boolean choose(Player self, Player opponent, List<Boolean> opponentHistory) {
        if (!lastRoundScores.containsKey(opponent)) {
            return random.nextBoolean(); // 첫 라운드는 랜덤
        }

        int lastScore = lastRoundScores.get(opponent);

        switch (lastScore) {
            case 6 -> {
                // (C, C) → 3 + 3
                return random.nextBoolean(); // 랜덤하게 협력 or 배신
            }
            case 5 -> {
                // (C, D) or (D, C)
                return lastScore != 0; // (C, D) → 협력 / (D, C) → 배신
            }
            case 2 -> {
                // (D, D) → 1 + 1
                return false; // 배신
            }
            default -> {
            }
        }

        return true; // 기본적으로 협력 (이론상 도달할 수 없는 경우지만 대비)
    }

    // 상대 플레이어별 점수를 저장 (게임이 끝날 때 호출해야 함)
    public void updateScore(Player opponent, int score) {
        lastRoundScores.put(opponent, score);
    }

    @Override
    public Strategy cloneStrategy() {
        return new Grasshopper();
    }
}