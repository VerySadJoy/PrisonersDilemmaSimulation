package Strategy;
import java.util.List;

// (착취형, 배신형) 기회주의적 아첨꾼, 계산적인 굴종자  
//  
// Flatterer 전략은 자신의 점수와 상대의 점수를 비교하여 행동을 결정하는 철저히 기회주의적인 전략이다.  
// 상대의 평균 점수가 자신의 점수보다 높으면 협력(C)하고, 자신의 점수가 더 높다면 배신(D)한다.  
//  
// 이 전략의 핵심은 ‘강자에게 굴종하고, 약자에게 가차 없이 배신하는’ 태도를 유지하는 것이다.  
// 즉, 강한 상대에게는 유리한 협력 관계를 유지하면서 살아남고,  
// 자신보다 약한 상대에게는 가차 없이 배신하여 이득을 극대화한다.  
//  
// 장점:  
// - 점수가 낮을 때는 협력을 유지하여 높은 점수를 가진 플레이어들과 좋은 관계를 형성할 수 있다.  
// - 점수가 높아지면 배신을 통해 추가적인 이득을 취할 수 있다.  
// - 적절한 순간까지 협력을 유지하면서 착취할 타이밍을 노릴 수 있어, 기회주의적 플레이에 적합하다.  
//  
// 단점:  
// - 강한 상대가 배신을 감지하면 보복형 전략(Tit-for-Tat, Grim Trigger)에게 크게 손해를 볼 수 있다.  
// - 협력 관계를 유지하는 듯하다가 배신하기 때문에, 신뢰를 구축하기 어려우며 장기적인 연합 전략에 약하다.  
// - 상대의 점수를 기준으로 하기 때문에, 게임 환경이 달라질 경우 예측 가능성이 높아져 공략당할 수 있다.  
//  
// 실제 사회에서 이 전략을 따르는 인간 유형은 "기회주의적 아첨꾼" 또는 "계산적인 굴종자"이다.  
// 이들은 강자에게는 협조적인 태도를 보이며 살아남지만,  
// 약자를 만나면 주저 없이 배신하고 자신의 이익을 극대화하려 한다.  
// 그러나 이러한 행동이 반복되면 결국 강한 상대에게도 신뢰를 잃고,  
// 장기적으로는 모든 플레이어에게 외면받을 가능성이 크다.  

public class Flatterer implements Strategy {
    @Override
    public boolean choose(Player self, Player opponent, List<Boolean> opponentHistory) {
        double myScore = (double) self.getScore() / self.getBattleCount();
        double opponentScore = (double) opponent.getScore() / opponent.getBattleCount();

        return myScore < opponentScore;  // 내가 높으면 배신, 내가 낮거나 같으면 협력
    }

    @Override
    public Strategy cloneStrategy() {
        return new Flatterer();
    }
}