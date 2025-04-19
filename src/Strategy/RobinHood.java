package Strategy;
import java.util.List;

/**
 * 전략 이름: RobinHood  
 * 전략 유형: 착취형 + 보복형 (불평등한 구조를 뒤엎는 의적, 역설적 정의주의자)
 *
 * 전략 개요:  
 * - 자신의 평균 점수가 상대보다 낮을 경우 → 배신(D)  
 * - 자신의 평균 점수가 높거나 같을 경우 → 협력(C)  
 * - 점수 기반의 정의 추구형 전략으로, 불리한 상황에서는 공격적으로, 유리한 상황에서는 안정적으로 플레이
 *
 * 작동 방식:  
 * - 비교 대상: 내 평균 점수 vs. 상대 평균 점수  
 * - 점수가 낮으면 → 배신 (점수 격차를 좁히려는 행동)  
 * - 점수가 높거나 같으면 → 협력 (현상 유지 시도)
 *
 * 장점:  
 * - 게임 초반 손해를 봤을 때 공격적으로 따라잡을 수 있는 기회 제공  
 * - 점수가 높을 때는 협력을 유지하며 이득을 안정적으로 관리  
 * - 강한 플레이어를 견제할 수 있기 때문에 일부 환경에선 긍정적 역할 가능
 *
 * 단점:  
 * - 보복 전략(Tit-for-Tat, Grim Trigger)에게 단발성 배신이 큰 손해로 이어질 수 있음  
 * - 점수 차만으로 행동을 결정하기 때문에, 문맥 없는 배신으로 협력을 깨기 쉬움  
 * - 상대가 이 전략을 간파하면 점수 조작을 통해 유리하게 유도당할 수 있음  
 *
 * 인간 대응 유형:  
 * - 강자에게는 배신, 약자에게는 협력
 * - 결과적으로 공정해지기를 바라지만, 그 방식은 공격적  
 * - 이상주의를 기반으로 하면서도 현실적인 이득을 챙기려는 정의 추구형 파이터  
 * - 협력보다는 균형 회복과 생존이 우선이며, 신뢰보다 점수가 우선인 사람
*/ 

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