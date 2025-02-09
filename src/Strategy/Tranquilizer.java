package Strategy;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

// (착취형, 협력형) 조용히 다가와 신뢰를 쌓고 배신하는 기만적인 착취자, 마약과 같은 유혹자  
//  
// Tranquilizer 전략은 초반에는 협력적인 태도를 유지하여 상대를 안심시키지만,  
// 점진적으로 배신을 증가시키면서도 배신 비율이 25%를 넘지 않도록 조절하는 전략이다.  
//  
// 이 전략의 핵심은 상대의 신뢰를 얻은 후,  
// 지나치게 배신하지 않으면서도 점진적으로 착취하는 것이다.  
// 상대가 협력적일수록 더욱 교묘하게 배신을 삽입하여 상대가 이를 눈치채지 못하도록 만든다.  
//  
// 전략의 작동 방식:  
// - 초반에는 무조건 협력(C) 하여 상대의 신뢰를 쌓는다.  
// - 상대의 협력 비율이 80%를 넘으면, "배신 모드"를 활성화하여 배신을 점진적으로 삽입.  
// - 단, 배신 비율이 전체 게임의 25%를 넘지 않도록 조절하여, 상대가 큰 의심을 가지지 않도록 함.  
// - 상대가 배신하면 즉시 배신 모드를 해제하고 보복(D)을 실행하여 신뢰를 회복할 기회를 제공.  
//  
// 장점:  
// - 상대가 협력적인 전략일 경우, 최대한의 신뢰를 얻은 후 착취할 수 있다.  
// - 배신을 무작위로 섞는 것이 아니라 점진적으로 증가시키므로,  
//   상대가 눈치채기 어려우며 즉각적인 반격을 받을 가능성이 낮다.  
// - 배신 비율을 25%로 제한하여, 보복형 전략(Tit-for-Tat, Grim Trigger)과도 어느 정도 협력 관계를 유지할 수 있다.  
//  
// 단점:  
// - 상대가 패턴을 감지하면, Forgiving Tit-for-Tat 같은 전략이 배신 빈도를 점진적으로 높여 반격할 가능성이 있다.  
// - 상대가 보복을 실행하면, 배신 모드를 해제해야 하므로 전략적 유연성이 제한될 수 있다.  
// - 배신을 삽입하는 패턴이 일정 부분 예측 가능해지면, 숙련된 상대에게 쉽게 간파될 위험이 있다.  
//  
// 실제 사회에서 이 전략을 따르는 인간 유형은 "조용히 다가와 신뢰를 쌓고 배신하는 기만적인 착취자" 또는 "마약과 같은 유혹자"이다.  
// 이들은 상대가 자신을 신뢰하도록 만들고,  
// 상대가 경계심을 풀었을 때 점진적으로 이용하는 성향을 가진다.  
// 그러나 지나치게 착취에 집중하면, 결국 상대가 눈치채고 보복을 실행할 가능성이 커진다.  

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