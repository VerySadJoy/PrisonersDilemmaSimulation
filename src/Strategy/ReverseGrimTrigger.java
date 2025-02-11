package Strategy;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

// (협력형) 반전된 그림 트리거, 기회주의적 신뢰자
//
// ReverseGrimTrigger 전략은 기본적으로 항상 배신(D)하지만,  
// 단 한 번이라도 상대가 협력(C)하면 무조건적인 협력 모드로 전환한다.
//
// 전략의 작동 방식:
// - 기본적으로 항상 배신(D).
// - 단, 상대가 한 번이라도 협력(C)하면 무한 협력 모드로 변경.
// - 한 번 협력 모드가 되면 끝까지 협력.
//
// 장점:
// - 상대가 협력적인 경우 빠르게 적응하여 높은 점수를 얻을 수 있음.
// - Tit-for-Tat 같은 전략과 만나면 쉽게 협력 루프에 진입 가능.
// - 멀티 환경에서 각 상대별로 독립적인 상태를 유지할 수 있음.  
//
// 단점:
// - 상대가 배신만 하면 무조건 배신하므로, Always Defect 같은 전략에겐 약함.
// - Forgiving Tit-for-Tat 같은 전략과 만나도 초기에는 비협력적으로 대응하여 신뢰를 얻기 어려울 수 있음.
//

public class ReverseGrimTrigger implements Strategy {
    // 상대별로 협력 모드 여부를 저장 (true = 무한 협력, false = 기본 배신)
    private final Map<Player, Boolean> alwaysCooperate = new HashMap<>();

    @Override
    public boolean choose(Player self, Player opponent, List<Boolean> opponentHistory) {
        // 만약 특정 상대와 이미 무한 협력 모드라면 계속 협력
        if (alwaysCooperate.getOrDefault(opponent, false)) {
            return true;
        }

        // 상대의 행동 히스토리를 확인
        for (boolean move : opponentHistory) {
            if (move) { // 상대가 한 번이라도 협력(C)한 경우
                alwaysCooperate.put(opponent, true); // 무한 협력 모드로 전환
                return true;
            }
        }

        // 기본적으로 배신(D)
        return false;
    }

    @Override
    public Strategy cloneStrategy() {
        return new ReverseGrimTrigger();
    }
}