package Strategy;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 전략 이름: ReverseGrimTrigger  
 * 전략 유형: 협력형 (기회주의적 신뢰자, 반전된 Grim Trigger)
 *
 * 전략 개요:  
 * - 처음엔 무조건 배신(D)으로 시작하지만, 상대가 단 한 번이라도 협력(C)하면 그 즉시 "무조건 협력 모드"로 전환  
 * - Grim Trigger의 반대 작동 구조: 상대가 한 번 배신하면 끝이 아니라, 협력하면 영원히 협력
 *
 * 작동 방식:  
 * - 기본 모드: 무조건 배신  
 * - 조건 전환: 상대가 한 번이라도 협력 → 이후 무한 협력  
 * - 전환 후에는 아무 조건 없이 끝까지 협력 유지
 *
 * 장점:  
 * - 협력 유도에 성공한 경우, 매우 안정적인 협력 루프 형성 가능  
 * - Tit-for-Tat, Pavlov 등 협력 성향 전략과 만나면 높은 점수 가능  
 * - 다중 플레이어 환경에서 독립적 협력 관계를 유지할 수 있음
 *
 * 단점:  
 * - Always Defect 같은 완전한 배신자에겐 영영 협력 진입 기회가 없어 불리  
 * - Forgiving Tit-for-Tat 같은 전략에겐 오해를 사기 쉬움 → 첫 인상이 너무 나쁨  
 * - 상대가 협력 안 하면 그냥 맨날 꼴찌함 (전략적 호불호가 매우 극단적임)
 *
 * 인간 대응 유형:  
 * - "기회주의적 신뢰자"  
 * - 처음에는 의심 많고 날카롭지만, 한 번 따뜻하게 대해주면 무한히 베푸는 타입  
 * - 불신으로 시작하지만, 일단 마음 열면 완전한 협력자로 전환  
 * - 누군가에게는 든든한 아군이 될 수 있고, 누군가에게는 절대 설득 불가능한 고집쟁이로 보일 수 있음
 */

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