package Strategy;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 전략 이름: ScammerTester  
 * 전략 유형: 착취형 + 보복형 (직관적 사기꾼, 빠른 적응형 분석가)
 *
 * 전략 개요:  
 * - 첫 라운드는 무조건 배신(D)으로 상대의 성향을 실험  
 * - 2라운드 상대 반응에 따라 모드 전환  
 *   - 상대가 배신 → 보복형(Tit-for-Tat) 모드  
 *   - 상대가 협력 → 협력/배신 반복 모드  
 *   - 그 외 → 기본 배신 유지  
 *
 * 작동 방식:  
 * - 1R: 무조건 배신  
 * - 2R: 상대 행동 관찰  
 *   - 배신이면 → 보복 중심(TFT)  
 *   - 협력이면 → C-D-C-D 반복 (최대 이득 착취 루트)  
 * - 이후 라운드: 전환된 모드에 따라 플레이
 *
 * 장점:  
 * - 단 2라운드 만에 빠르게 상대 성향 파악 가능  
 * - 협력가에겐 지속적 착취 가능  
 * - 보복형에겐 손해 줄이기 유리  
 * - 적응형으로서의 실용성 매우 높음
 *
 * 단점:  
 * - 첫 턴 배신은 Forgiving 전략에게도 신뢰 상실로 이어질 수 있음  
 * - 랜덤형이나 교란형 전략엔 판단 기준 흐려짐  
 * - 초반 실험에 실패하면 이후 전략 전환이 무력화될 수 있음
 *
 * 인간 유형 대응:  
 * - 처음에 떠보는 타입 
 * - 인간 관계에서 테스트성 행동을 먼저 하고,  
 *   이후 태도를 바로바로 바꾸는 계산적인 사람  
 * - 유연하고 실용적이지만, 관계 형성보다는 이득 극대화에 집중하는 성향  
 * - 사람에 따라선 불쾌하거나, 반대로 센스있다고 느낄 수 있음
*/  

public class ScammerTester implements Strategy {
    private final Map<Player, Boolean> switchedToTitForTat = new HashMap<>(); // 상대별 팃포탯 전환 여부
    private final Map<Player, Boolean> alternatingMode = new HashMap<>(); // 상대별 협력/배신 반복 모드
    private final Map<Player, Integer> roundCount = new HashMap<>(); // 상대별 라운드 번호

    @Override
    public boolean choose(Player self, Player opponent, List<Boolean> opponentHistory) {
        // 상대방과의 라운드 진행 횟수 증가
        int currentRound = roundCount.getOrDefault(opponent, 0) + 1;
        roundCount.put(opponent, currentRound);

        // 첫 라운드에서는 무조건 배신(D)
        if (opponentHistory.isEmpty()) {
            return false;
        }

        // 2라운드에서 상대가 보복(배신)했다면, 팃포탯 모드로 전환
        if (currentRound == 2 && !opponentHistory.get(opponentHistory.size() - 1)) {
            switchedToTitForTat.put(opponent, true);
        }

        // 팃포탯 모드일 경우 상대의 마지막 행동을 따라감
        if (switchedToTitForTat.getOrDefault(opponent, false)) {
            return opponentHistory.get(opponentHistory.size() - 1);
        }

        // 2라운드에서 상대가 협력했다면 협력/배신 반복 모드로 전환
        if (currentRound == 2 && opponentHistory.get(opponentHistory.size() - 1)) {
            alternatingMode.put(opponent, true);
        }

        // 협력/배신 반복 모드 (짝수 라운드에서는 협력, 홀수 라운드에서는 배신)
        if (alternatingMode.getOrDefault(opponent, false)) {
            return currentRound % 2 == 0;
        }

        // 기본적으로 배신 유지 (실제로 이 경로로 가는 경우는 없음)
        return false;
    }

    @Override
    public Strategy cloneStrategy() {
        return new ScammerTester();
    }
}