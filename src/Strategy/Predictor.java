package Strategy;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

// (협력형, 보복형) 미래를 예측하는 분석가, 전략적 판단자  
//  
// Predictor 전략은 상대의 과거 행동 패턴을 분석하여,  
// 상대가 협력 또는 배신할 가능성을 예측하고 이에 맞춰 대응하는 전략이다.  
//  
// 이 전략의 핵심은 ‘상대의 반응 데이터를 축적하여  
// 상대가 협력할 확률과 배신할 확률을 계산한 후,  
// 가장 유리한 선택을 하는 것’이다.  
//  
// 전략의 작동 방식:  
// - 첫 5라운드는 데이터를 수집하기 위해 협력(C) 유지.  
// - 상대의 협력과 배신 비율을 계산하여 협력적 성향인지 배신적 성향인지 판단.  
// - 상대가 내가 협력했을 때 배신하는 확률이 70% 이상이면 배신(D)로 대응.  
// - 상대가 내가 배신했을 때도 배신하는 확률이 60% 이상이면 배신(D)로 지속.  
// - 기본적으로 상대의 협력 비율이 배신 비율보다 높으면 협력(C).  
//  
// 장점:  
// - 상대가 협력적이면 협력 관계를 유지하면서 높은 보상을 얻을 수 있다.  
// - 상대가 배신 성향을 보이면 배신으로 대응하여 손해를 줄인다.  
// - 단순한 보복 전략보다 유연하게 상대의 성향을 분석하며 대응할 수 있다.  
//  
// 단점:  
// - 초반 5라운드 동안 상대가 배신을 반복하면 큰 손해를 볼 수도 있다.  
// - 랜덤 전략과 만나면 예측이 어려워지고, 지속적인 적응이 필요할 수 있다.  
// - 상대가 의도적으로 협력-배신 패턴을 조정하면 쉽게 속을 가능성이 있다.  
//  
// 실제 사회에서 이 전략을 따르는 인간 유형은 "미래를 예측하는 분석가" 또는 "전략적 판단자"이다.  
// 이들은 과거 데이터를 기반으로 상대가 어떤 반응을 보일지 예측하고,  
// 가장 유리한 방식으로 행동을 조정하려 한다.  
// 이는 상당히 실용적이지만, 상대가 의도적으로 데이터를 조작하면 오판할 위험도 존재한다.  

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