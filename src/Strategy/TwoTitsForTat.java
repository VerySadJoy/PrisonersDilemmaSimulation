package Strategy;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 전략 이름: TwoTitsForTat  
 * 전략 유형: 보복형 (확장형 Tit-for-Tat, 강화된 응징자)
 *
 * 전략 개요:  
 * - 상대가 배신하면 두 번 연속 보복하는 강화형 Tit-for-Tat 전략  
 * - 한 번의 배신에 두 번의 배신으로 응징 → 더 강한 억제 효과 기대  
 *
 * 작동 방식:  
 * - 첫 라운드: 무조건 협력(C)  
 * - 이후:  
 *   - 상대가 배신 → 보복 횟수 2회로 설정  
 *   - 보복 횟수가 남아 있다면 계속 배신(D)  
 *   - 보복이 끝난 후에는 다시 상대 행동을 따라감 (기본 TFT와 유사)
 * 
 * 장점:  
 * - Tit-for-Tat보다 강한 보복으로 상대의 배신을 억제하는 효과  
 * - 실수보다 의도적 배신에 대해 강력한 메시지를 전달할 수 있음  
 * - 보복이 끝난 후엔 다시 협력 복귀가 가능함 (Grim Trigger보다 유연)
 * 
 * 단점:  
 * - Forgiving 전략과 만나면 과한 보복으로 인해 신뢰 회복이 어려울 수 있음  
 * - 상대가 랜덤성 높은 전략일 경우, 불필요한 배신의 반복이 생길 수 있음  
 * - 보복이 반복되면 협력 복구까지 시간이 길어질 수 있음
 * 
 * 인간 유형 대응:  
 * - 강하게 응징하되, 완전한 절연은 하지 않는 사람 
 * - 쉽게 용서하지 않지만, 영원히 복수하지도 않는 스타일  
 * - 상대에게 두 번은 맞을 각오해라라는 심리적 압박을 줌
*/

public class TwoTitsForTat implements Strategy {

    private final Map<Player, Integer> opponentPunishmentCount = new HashMap<>(); // 각 상대별 보복 횟수 저장

    @Override
    public boolean choose(Player self, Player opponent, List<Boolean> opponentHistory) {
        // 첫 번째 라운드는 기본적으로 협력(C)
        if (opponentHistory.isEmpty()) {
            return true;
        }

        // 상대의 이전 행동 확인
        boolean lastMove = opponentHistory.get(opponentHistory.size() - 1);

        // 상대가 직전 라운드에서 배신(D)했다면, 두 번 연속으로 배신하도록 설정
        if (!lastMove) {
            opponentPunishmentCount.put(opponent, 2); // 보복 횟수 2회 설정
        }

        // 보복 횟수가 남아 있다면, 배신(D) 실행
        int remainingPunishments = opponentPunishmentCount.getOrDefault(opponent, 0);
        if (remainingPunishments > 0) {
            opponentPunishmentCount.put(opponent, remainingPunishments - 1); // 1 감소
            return false; // 배신 실행
        }

        // 상대의 마지막 행동을 그대로 따라감 (기본 Tit-for-Tat)
        return lastMove;
    }
    
    @Override
    public Strategy cloneStrategy() {
        return new TwoTitsForTat();
    }
}