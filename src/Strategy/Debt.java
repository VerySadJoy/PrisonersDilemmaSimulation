package Strategy;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

// (보복형, 협력형) 원한을 품은 채권자, 공정한 복수자  
//  
// Debt 전략은 상대가 배신하면 ‘빚’으로 기록하고, 빚이 갚히기 전까지는 배신으로 응징하는 보복형 전략이다.  
// 반대로, 상대가 협력하면 점진적으로 빚을 탕감해주며 다시 협력으로 돌아간다.  
//  
// 이 전략의 핵심은 "배신의 빚은 갚아야 한다"는 원칙을 따르며,  
// 상대의 행동에 따라 협력과 보복을 조정하는 점진적인 복수 시스템을 구축하는 것이다.  
//  
// 장점:  
// - 상대가 실수로 배신한 경우에도 빚을 갚을 기회를 주기 때문에 협력 관계를 유지할 가능성이 크다.  
// - 보복형이지만 너무 가혹하지 않아서, 협력 전략과도 어느 정도 공존할 수 있다.  
// - 상대가 협력으로 돌아서면 다시 협력을 선택하므로, 신뢰 회복이 가능하다.  
//  
// 단점:  
// - 상대가 배신을 반복적으로 하면 빚이 계속 쌓이고, 결국 완전한 배신자로 변할 위험이 있다.  
// - 보복을 반드시 실행해야 하기 때문에, 유연성이 부족할 수 있다.  
// - 상대가 의도적으로 한 번씩 협력하며 빚을 최소한으로 유지하면, 예상보다 낮은 보복 효과를 가질 수도 있다.  
//  
// 실제 사회에서 이 전략을 따르는 인간 유형은 "원한을 품은 채권자" 또는 "공정한 복수자"이다.  
// 한 번 신뢰를 저버린 상대에게 반드시 대가를 치르게 하지만,  
// 동시에 상대가 충분한 보상을 제공하면 다시 신뢰를 회복할 여지를 남겨둔다.  
// 이들은 단순한 감정적 보복이 아니라, 채무 관계처럼 공정한 원칙을 기반으로 신뢰를 관리한다.  
// 하지만, 상대가 완전히 신뢰를 회복하기 전까지는 절대적으로 경계를 풀지 않는다.  

public class Debt implements Strategy {
    private final ConcurrentHashMap<Player, Integer> debt = new ConcurrentHashMap<>(); // 상대가 쌓은 배신 빚

    @Override
    public boolean choose(Player self, Player opponent, List<Boolean> opponentHistory) {
        debt.putIfAbsent(opponent, 0);

        if (opponentHistory.isEmpty()) {
            return true; // 첫 턴은 협력
        }

        // 상대가 이전 턴에 배신했으면 빚을 추가
        if (!opponentHistory.get(opponentHistory.size() - 1)) {
            debt.put(opponent, debt.get(opponent) + 1);
        } else {
            // 상대가 협력하면 빚을 하나 갚아줌
            debt.put(opponent, Math.max(0, debt.get(opponent) - 1));
        }

        // 빚이 남아 있으면 배신으로 갚음
        return debt.get(opponent) == 0;
    }

    @Override
    public Strategy cloneStrategy() {
        return new Debt();
    }
}