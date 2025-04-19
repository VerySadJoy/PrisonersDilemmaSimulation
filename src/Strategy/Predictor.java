package Strategy;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 전략 이름: Predictor  
 * 전략 유형: 협력형 + 보복형 (미래를 예측하는 분석가, 전략적 판단자)  
 * 
 * 전략 개요:  
 * - 상대의 과거 반응 데이터를 축적하여  
 *   협력 확률과 배신 확률을 계산하고, 가장 유리한 선택을 하는 전략  
 * 
 * 작동 방식:  
 * - 첫 5라운드는 협력(C)으로 시작하여 데이터 수집  
 * - 이후:  
 *   - 내가 협력했을 때 상대가 배신할 확률이 70% 이상 → 배신(D)  
 *   - 내가 배신했을 때도 상대가 배신할 확률이 60% 이상 → 배신(D)  
 *   - 그렇지 않으면 상대의 협력률이 높으면 협력(C), 낮으면 배신(D)  
 * 
 * 장점:  
 * - 상대가 협력적이면 자연스럽게 협력 유지  
 * - 배신 성향을 보이는 상대에게도 손해 없이 적응 가능  
 * - 상대 행동을 분석하는 유연하고 실용적인 접근법  
 * 
 * 단점:  
 * - 데이터 수집 기간(초반 5라운드) 동안 손해를 볼 위험 존재  
 * - 완전 랜덤 전략이나 교묘한 혼합 전략에 대응이 어려울 수 있음  
 * - 분석 대상이 되는 데이터를 조작당하면 오판 가능성 있음  
 * 
 * 인간 대응 유형:  
 * - 계산적 판단자 또는 확률적 신중가 
 * - 과거 데이터를 기반으로 미래 행동을 예측하려는 사람  
 * - 실용적이지만 너무 데이터 중심이면 예외 상황에 취약할 수 있음  
 */ 

public class Predictor implements Strategy {
    private final Map<Player, Integer> opponentCoopCount = new ConcurrentHashMap<>(); // 협력 횟수
    private final Map<Player, Integer> opponentDefectCount = new ConcurrentHashMap<>(); // 배신 횟수
    private final Map<Player, Integer> totalRounds = new ConcurrentHashMap<>(); // 전체 라운드 수
    private final Map<Player, Integer> whenICoopTheyCoop = new ConcurrentHashMap<>(); // 내가 협력했을 때 상대도 협력한 횟수
    private final Map<Player, Integer> whenIDefectTheyCoop = new ConcurrentHashMap<>(); // 내가 배신했을 때 상대가 협력한 횟수
    private final Map<Player, Integer> whenICoopTheyDefect = new ConcurrentHashMap<>(); // 내가 협력했을 때 상대가 배신한 횟수
    private final Map<Player, Integer> whenIDefectTheyDefect = new ConcurrentHashMap<>(); // 내가 배신했을 때 상대도 배신한 횟수

    @Override
    public boolean choose(Player self, Player opponent, List<Boolean> opponentHistory) {
        totalRounds.put(opponent, totalRounds.getOrDefault(opponent, 0) + 1);
        int rounds = totalRounds.get(opponent);

        // 첫 5라운드는 데이터 수집을 위해 기본적으로 협력
        if (rounds <= 5) {
            return true;
        }

        // computeIfAbsent로 안전하게 값 초기화
        opponentCoopCount.computeIfAbsent(opponent, k -> 0);
        opponentDefectCount.computeIfAbsent(opponent, k -> 0);
        whenICoopTheyCoop.computeIfAbsent(opponent, k -> 0);
        whenICoopTheyDefect.computeIfAbsent(opponent, k -> 0);
        whenIDefectTheyCoop.computeIfAbsent(opponent, k -> 0);
        whenIDefectTheyDefect.computeIfAbsent(opponent, k -> 0);

        // 상대의 협력/배신 패턴 기록
        boolean lastMove = opponentHistory.get(opponentHistory.size() - 1);
        if (lastMove) {
            opponentCoopCount.put(opponent, opponentCoopCount.get(opponent) + 1);
        } else {
            opponentDefectCount.put(opponent, opponentDefectCount.get(opponent) + 1);
        }

        // 내가 지난 턴에 무엇을 했는지 분석
        boolean myLastMove = rounds > 1 && opponentHistory.size() >= 2 
            ? Boolean.TRUE.equals(opponentHistory.get(opponentHistory.size() - 2))
            : true;


        if (myLastMove && lastMove) {
            whenICoopTheyCoop.put(opponent, whenICoopTheyCoop.get(opponent) + 1);
        } else if (myLastMove && !lastMove) {
            whenICoopTheyDefect.put(opponent, whenICoopTheyDefect.get(opponent) + 1);
        } else if (!myLastMove && lastMove) {
            whenIDefectTheyCoop.put(opponent, whenIDefectTheyCoop.get(opponent) + 1);
        } else {
            whenIDefectTheyDefect.put(opponent, whenIDefectTheyDefect.get(opponent) + 1);
        }

        // 협력 확률 계산
        double totalInteractions = (double) rounds;
        double coopRate = opponentCoopCount.get(opponent) / totalInteractions;
        double defectRate = opponentDefectCount.get(opponent) / totalInteractions;

        double whenICoopDefectRate = whenICoopTheyDefect.get(opponent) /
                (double) Math.max(1, whenICoopTheyCoop.get(opponent) + whenICoopTheyDefect.get(opponent));

        double whenIDefectDefectRate = whenIDefectTheyDefect.get(opponent) /
                (double) Math.max(1, whenIDefectTheyCoop.get(opponent) + whenIDefectTheyDefect.get(opponent));

        // 상대가 내 협력에 대해 배신할 확률이 70% 이상이면 배신
        if (whenICoopDefectRate > 0.7) {
            return false;
        }

        // 상대가 내 배신에 대해서도 배신할 확률이 높다면 나도 배신
        if (whenIDefectDefectRate > 0.6) {
            return false;
        }

        // 기본적으로 상대가 협력할 확률이 높으면 협력
        return coopRate > defectRate;
    }

    @Override
    public Strategy cloneStrategy() {
        return new Predictor();
    }
}