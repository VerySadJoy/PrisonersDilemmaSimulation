package Strategy;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 전략 이름: Tranquilizer  
 * 전략 유형: 착취형 + 협력형 (조용히 다가와 배신하는 유혹자)  
 * 
 * 전략 개요:  
 * - 초반 협력으로 상대의 신뢰를 얻고,  
 * - 상대가 협력적이면 배신을 천천히 삽입해 착취함  
 * - 단, 배신 비율은 25%를 넘지 않도록 조절하여 눈치 못 채게 유지  
 * - 상대가 배신하면 즉시 응징하고 배신 모드 해제
 * 
 * 작동 방식:  
 * - 첫 라운드: 무조건 협력  
 * - 상대 협력률이 80% 이상 → 배신 모드 진입  
 * - 배신 모드에서는 전체 게임의 25% 비율 이하로만 배신 수행  
 * - 상대가 배신하면 즉시 배신 모드 해제 + 즉시 보복  
 * - 나머지는 기본적으로 협력 유지
 * 
 * 장점:  
 * - 협력 기반 상대에겐 신뢰를 쌓은 뒤 고효율 착취 가능  
 * - 배신 비율을 억제해 보복형 전략에게도 적당히 협력 유지 가능  
 * - 예측하기 어려운 점진적 배신으로 인해 상대가 쉽게 대응 못함
 * 
 * 단점:  
 * - Forgiving 전략에게는 반복 배신이 누적되어 결국 보복당할 수 있음  
 * - 착취 패턴이 들키면 협력 붕괴 가능성 있음  
 * - 완전한 배신자나 랜덤 전략과 만나면 초반 협력이 독이 될 수 있음
 * 
 * 인간 유형 대응:  
 * - 처음엔 친절하게 다가오고, 나중엔 슬쩍 이용하는 타입
 * - 마치 마약처럼 상대를 안심시키며 자신에게 유리한 구조를 만든다  
 * - 신뢰를 악용하는 형태지만, 드러나지 않도록 세심하게 설계된 전략
*/

public class Tranquilizer implements Strategy {
    private final Map<Player, Boolean> betrayalPhase = new ConcurrentHashMap<>(); // 상대별 배신 모드 활성화 여부
    private final Map<Player, Integer> betrayalCount = new ConcurrentHashMap<>(); // 상대별 배신 횟수
    private final Map<Player, Integer> totalRounds = new ConcurrentHashMap<>(); // 상대별 총 게임 횟수
    private final Map<Player, List<Boolean>> opponentHistories = new ConcurrentHashMap<>(); // 상대별 기록 저장

    @Override
    public boolean choose(Player self, Player opponent, List<Boolean> opponentHistory) {
        // 리스트를 CopyOnWriteArrayList로 관리하여 동시 수정 방지
        opponentHistories.putIfAbsent(opponent, new CopyOnWriteArrayList<>(opponentHistory));
        List<Boolean> history = opponentHistories.get(opponent);

        // 상대와의 총 라운드 수 증가 (동기화된 업데이트)
        totalRounds.compute(opponent, (k, v) -> (v == null) ? 1 : v + 1);
        int rounds = totalRounds.get(opponent);

        // 첫 번째 라운드는 무조건 협력(C)
        if (history.isEmpty()) {
            return true;
        }

        // 상대의 협력 비율 계산
        long cooperationCount = history.stream().filter(b -> b).count();
        double cooperationRate = (double) cooperationCount / history.size();

        // 상대가 배신하면 즉시 보복(배신)하고 배신 모드 해제
        if (!history.get(history.size() - 1)) {
            betrayalPhase.put(opponent, false); // 배신 모드 해제
            return false; // 즉시 응징 (배신)
        }

        // 상대별 배신 횟수 가져오기 (Atomic 수정)
        betrayalCount.putIfAbsent(opponent, 0);
        double betrayalRate = (double) betrayalCount.get(opponent) / rounds;

        // 배신 비율이 25%를 넘지 않도록 제한
        if (betrayalRate >= 0.25) {
            return true; // 배신 비율이 너무 높으면 협력 유지
        }

        // 상대가 계속 협력하면 일정 시점에서 배신 모드 활성화
        betrayalPhase.putIfAbsent(opponent, false);
        if (!betrayalPhase.get(opponent) && cooperationRate > 0.8) {
            betrayalPhase.put(opponent, true); // 배신 모드 활성화
        }

        // 배신 모드에서는 점진적으로 배신 횟수를 증가시켜 착취
        if (betrayalPhase.get(opponent)) {
            if (betrayalCount.get(opponent) < rounds / 4) { // 배신 비율이 전체 게임의 1/4 이하로 유지
                betrayalCount.compute(opponent, (k, v) -> v + 1); // Atomic 업데이트
                return false; // 배신
            } else {
                betrayalPhase.put(opponent, false); // 다시 협력 모드로 전환
            }
        }

        // 기본적으로 협력 유지 (C)
        return true;
    }
    
    @Override
    public Strategy cloneStrategy() {
        return new Tranquilizer();
    }
}