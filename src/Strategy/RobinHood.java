package Strategy;
import java.util.List;
// (착취형, 보복형) 불평등한 구조를 뒤엎는 의적, 강자에게 배신하는 역설적 정의주의자  
//  
// RobinHood 전략은 상대와 자신의 평균 점수를 비교하여,  
// 상대보다 점수가 낮으면 배신(D)하고, 높거나 같으면 협력(C)하는 전략이다.  
//  
// 이 전략의 핵심은 ‘자신이 불리한 위치에 있을 때는 적극적으로 상대를 착취하고,  
// 자신이 유리한 위치에 있으면 협력을 유지하려는 것’이다.  
//  
// 전략의 작동 방식:  
// - 상대보다 점수가 낮으면 배신(D)을 선택하여 상대를 약화시키거나 점수를 따라잡음.  
// - 상대보다 점수가 높거나 같으면 협력(C)하여 현재 상태를 유지하려 함.  
//  
// 장점:  
// - 상대보다 낮은 점수일 때 공격적으로 배신하여 점수를 따라잡을 수 있다.  
// - 강한 상대(점수가 높은 플레이어)에게는 배신을 통해 균형을 맞추려 하므로,  
//   특정한 환경에서 생존 가능성이 높아진다.  
// - 점수가 높을 때 협력을 유지하면, 협력 전략과의 공존 가능성이 생긴다.  
//  
// 단점:  
// - 상대가 보복형 전략(Tit-for-Tat, Grim Trigger)일 경우, 배신이 감지되면 지속적인 보복을 받을 가능성이 크다.  
// - 상대가 이 전략을 인지하면, 인위적으로 점수를 조정하여 배신을 유도할 수 있다.  
// - 점수가 낮을 때 배신을 반복하기 때문에, 협력 관계를 형성하기 어렵다.  
//  
// 실제 사회에서 이 전략을 따르는 인간 유형은 "불평등한 구조를 뒤엎는 의적" 또는 "강자에게 배신하는 역설적 정의주의자"이다.  
// 이들은 자신보다 강한 상대에게는 공격적으로 배신하면서도,  
// 자신과 대등하거나 약한 상대에게는 협력적인 태도를 유지하려 한다.  
// 그러나 이러한 태도는 협력 관계의 불안정을 초래할 수 있으며,  
// 보복을 당할 경우 생존이 어려워질 수도 있다.  

public class RobinHood implements Strategy {
    @Override
    public boolean choose(Player self, Player opponent, List<Boolean> opponentHistory) {
        double myScore = (double) self.getScore() / self.getBattleCount();
        double opponentScore = (double) opponent.getScore() / opponent.getBattleCount();

        return myScore >= opponentScore;  // 내가 높거나 같으면 협력, 내가 낮으면 배신
    }

    @Override
    public Strategy cloneStrategy() {
        return new RobinHood();
    }
}