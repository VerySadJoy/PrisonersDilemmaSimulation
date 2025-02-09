package Strategy;
import java.util.List;

// (랜덤형, 착취형) 위험 감수형 도박사, 확률적 계산가  
//  
// Gambler 전략은 자신의 평균 점수를 기반으로 협력과 배신을 결정하는 확률적 전략이다.  
// 플레이어가 지금까지 얻은 평균 점수가 2.25보다 높으면 배신(D),  
// 낮으면 협력(C)을 선택한다.  
//  
// 이 전략의 핵심은 ‘위험 감수와 수익 극대화’이며,  
// 상대에 대한 직접적인 분석보다 자신의 수익을 최적화하는 방식으로 작동한다.  
//  
// 장점:  
// - 상대의 전략을 직접 분석하지 않고도, 현재까지의 성과를 바탕으로 유동적인 전략을 취할 수 있다.  
// - 초반에는 협력을 유지하며 점수를 쌓다가, 점수가 충분히 높아지면 배신을 선택하는 방식으로 착취 가능.  
// - 보복형 전략과 싸울 경우, 초반 협력으로 신뢰를 얻을 수 있어 Tit-for-Tat과도 협력 가능성이 있다.  
//  
// 단점:  
// - 자신의 점수에만 의존하기 때문에 상대의 성향을 세밀하게 분석하지 못한다.  
// - 강한 보복형 전략(Grim Trigger, Tit-for-Tat 변형)과 만나면 배신 후 협력 관계가 단절될 가능성이 크다.  
// - 초반에 낮은 점수를 받으면 협력을 지속할 수밖에 없으므로, 적극적으로 배신하는 전략에게 약할 수도 있다.  
//  
// 실제 사회에서 이 전략을 따르는 인간 유형은 "위험 감수형 도박사" 또는 "확률적 계산가"이다.  
// 이들은 확률과 기대 이득을 계산하여 협력과 배신을 선택하며,  
// 현재 성과가 좋으면 더 큰 이득을 위해 배신을 감행하고,  
// 성과가 낮으면 협력을 통해 생존을 모색하는 성향을 가진다.  
// 하지만 지나친 확률적 접근이 신뢰를 해치는 결과를 초래할 수도 있다.  

public class Gambler implements Strategy {
    private int roundsPlayed = 0;

    @Override
    public boolean choose(Player self, Player opponent, List<Boolean> opponentHistory) {
        roundsPlayed++;
        int averageScore = (roundsPlayed > 0) ? (self.getScore() / roundsPlayed) : 0;

        return averageScore > 2.25; // (5, 3, 1, 0)의 기댓값이 2.25
    }

    @Override
    public Gambler cloneStrategy() {
        return new Gambler();
    }
}