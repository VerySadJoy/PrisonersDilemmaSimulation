package Strategy;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * 전략 이름: Cheater  
 * 전략 유형: 메타 전략, 비정상적 전지적 판단자  
 *  
 * 전략 개요:  
 * - 상대의 전략 클래스명을 직접 확인하여 가장 적절한 대응을 선택  
 * - 상대의 전략을 모른다는 기본 전제를 강제로 깨뜨린 전략  
 * - 실제 시뮬레이션에서는 사용할 수 없지만, 재미와 분석용으로 강력한 성능을 발휘  
 *  
 * 작동 방식:  
 * - 상대의 `getStrategy().getClass().getSimpleName()`을 이용해 전략명을 파악  
 * - 전략마다 대응 패턴을 커스터마이징  
 *   - ex) AlwaysDefect → 계속 배신, TitForTat → 팃포탯 대응, Tranquilizer → 초반 배신 등  
 * - 배신 카운트, 라운드 추적 등을 통해 세밀한 대응 수행  
 *  
 * 장점:  
 * - 이론상 거의 모든 전략에 맞춤형으로 이기기 위한 응답이 가능 
 * - 분석 도구로 활용하면, 특정 전략의 약점을 파악할 수 있음  
 * - 시뮬레이션에서 가장 강한 대응을 테스트해볼 수 있는 강력한 수단  
 *  
 * 단점:  
 * - 상대 전략을 코드 수준에서 확인하는 건 현실적이지 않으며 공정하지 않음
 * - 전략 자체의 사고방식이나 패턴을 반영하지 않고, 단순히 이름에 의존  
 * - 다른 전략들과의 윤리적 협력 가능성이 거의 없음
 *  
 * 인간 유형 대응:  
 * - 상대를 판단하지 않고는 시작도 못 한다는 초직관적 전략가  
 * - 겉으로는 분석적이지만, 실상은 편견과 꼼수를 전략화한 치트성 인간  
 * - 공정성보다 결과와 효율만을 따지는 냉정한 승부주의자
 * - 게임의 규칙을 따르기보단, 규칙 바깥에서 방법을 찾으려는 타입
*/

public class Cheater implements Strategy {
    private final Map<Player, Integer> roundTracker = new HashMap<>(); // 상대별 라운드 카운트
    private final Map<Player, Integer> betrayalTracker = new HashMap<>();
    @Override
    public boolean choose(Player self, Player opponent, List<Boolean> opponentHistory) {
        roundTracker.put(opponent, roundTracker.getOrDefault(opponent, 0) + 1); // 라운드 카운트 증가

        return switch (opponent.getStrategy().getClass().getSimpleName()) {
            case "ReverseGrimTrigger", "Predator", "ScammerTester", "StereoType" -> onlyCooperateFirstRound(opponent);
            case "OmegaTitForTat", "SlowTitForTat" -> patternDCDC(opponent);
            case "Tranquilizer" -> earlyDefect5ThenDCDC(opponent);
            case "AlwaysDefect", "Flatterer", "ConditionalForgiver", 
                "RobinHood", "CollectiveResponsibility", "AlternateDefect", 
                "BadPerson", "Troller", "RandomStrategy", "Gambler", "DeceptiveAdaptation", 
                "AlternateCooperate", "SuspiciousTitForTat", "PatternBreaker", 
                "Opportunist", "Merchant", "GoodPerson", "GrimTrigger", "PeacefulTitForTat",
                "AlwaysCooperate", "HandOfGod" -> defect();
            case "TitForTat", "ContriteTitForTat", "ProbabilisticTitForTat",
                 "Grasshopper", "NoisyTitForTat", "GlassMind", "ShadowDefect",
                 "ProbabilisticWeightedTitForTat", "Predictor", "Shepherd", 
                 "DynamicTitForTat", "DiscountFactor" -> titForTat(opponentHistory);
            case "OpponentTester" -> againstOpponentTester(opponent);
            case "BinaryThinking" -> againstBinary(opponent, opponentHistory);
            case "Mutation" -> defectTitForTat(self, opponentHistory);
            case "ConditionalCommitment" -> againstConditionalCommitment(opponent);
            case "MutualDestruction" -> againstMutualDestruction(opponent);
            case "GuidingCooperator" -> againstGuidingCooperator(opponentHistory);
            case "EnhancedTitForTat" -> againstEnhancedTitForTat(opponentHistory);
            case "ForgivingTitForTat", "Pavlov" -> onlyCooperateFirstRoundDCDC(opponentHistory);
            case "Saint" -> againstSaint(opponentHistory);
            case "TitForTatLastDefect" -> titForTatLastTwoDefects(opponentHistory);
            case "GreedyTitForTat", "Gradual", "Debt", "TwoTitsForTat", "HardTitForTat" -> grimTrigger(opponentHistory);
            default -> { 
                System.out.println(opponent.getStrategy().getClass().getSimpleName());
                yield defect();
            }
        };
    }

    private boolean onlyCooperateFirstRound(Player opponent) {
        int round = roundTracker.getOrDefault(opponent, 0);
        return round == 1; // 첫 라운드 협력, 이후 계속 배신
    }

    private Boolean onlyCooperateFirstRoundDCDC(List<Boolean> opponentHistory) {
        int round = opponentHistory.size();
        if (round == 0) {
            return true; // 첫 라운드는 협력
        }

        return round % 2 == 1;
    }

    private boolean patternDCDC(Player opponent) {
        int round = roundTracker.getOrDefault(opponent, 0);
        return round % 2 == 0;
    }

    private boolean earlyDefect5ThenDCDC(Player opponent) {
        int round = roundTracker.getOrDefault(opponent, 0);
        return round < 5 ? false : round % 2 == 0;
    }

    private boolean grimTrigger(List<Boolean> opponentHistory) {
        // 첫 번째 라운드는 기본적으로 협력(C)
        if (opponentHistory.isEmpty()) {
            return true;
        }

        // 상대가 한 번이라도 배신(D)한 적이 있으면 이후로 계속 배신(D)
        return !opponentHistory.contains(false);
    }

    private boolean againstBinary(Player opponent, List<Boolean> opponentHistory) {
        int rounds = opponentHistory.size();
        int betrayals = betrayalTracker.getOrDefault(opponent, 0);
        if (rounds < 5) {
            return true;
        }
        // 배신 비율을 계산하여 이번에 배신하면 50%를 초과하는지 확인
        double newBetrayalRate = (double) (betrayals + 1) / (rounds + 1);
        if (newBetrayalRate > 0.49) {
            return true;
        }
        else {
            betrayalTracker.put(opponent, betrayals + 1);
            return false;
        }
    }

    private boolean defect() {
        return false;
    }

    private boolean againstOpponentTester(Player opponent) {
        int round = roundTracker.getOrDefault(opponent, 0);
        return round == 1 || (round - 1) % 6 == 0; 
    }

    private boolean titForTat(List<Boolean> opponentHistory) {
        if (opponentHistory.size() >= 499) {
            return false;
        }

        if (opponentHistory.isEmpty()) {
            return true;
        }

        return opponentHistory.get(opponentHistory.size() - 1);
    }

    private Boolean titForTatLastTwoDefects(List<Boolean> opponentHistory) {
        if (opponentHistory.size() >= 498) {
            return false;
        }

        if (opponentHistory.isEmpty()) {
            return true;
        }

        return opponentHistory.get(opponentHistory.size() - 1);
    }

    private boolean defectTitForTat(Player self, List<Boolean> opponentHistory) {
        if (self.getBattleCount() >= 499 || opponentHistory.isEmpty()) {
            return false;
        }

        return opponentHistory.get(opponentHistory.size() - 1);
    }

    private boolean againstConditionalCommitment(Player opponent) {
        int round = roundTracker.getOrDefault(opponent, 0);
        return round <= 3;
    }

    private boolean againstMutualDestruction(Player opponent) {
        int totalRounds = roundTracker.getOrDefault(opponent, 0);
        int betrayals = betrayalTracker.getOrDefault(opponent, 0);
    
        // 배신 비율을 계산하여 이번에 배신하면 40%를 초과하는지 확인
        double newBetrayalRate = (double) (betrayals + 1) / (totalRounds + 1);
    
        // 만약 이번 배신이 40%를 넘는다면 협력, 그렇지 않다면 배신
        if (newBetrayalRate > 0.40) {
            return true; // 협력
        }
        else {
            betrayalTracker.put(opponent, betrayals + 1);
            return false; // 배신
        }
    }

    private Boolean againstGuidingCooperator(List<Boolean> opponentHistory) {
        int rounds = opponentHistory.size();

        if (rounds < 5) {
            return false; // 첫 5라운드 배신 (Guiding Cooperator는 무조건 협력)
        }

        if (rounds == 6 || rounds == 7) {
            return true; // 6~7라운드 협력 (Guiding Cooperator는 보복 안 함)
        }

        return rounds % 2 == 0; // 이후 교대로 협력과 배신 섞기 (보복 모드 발동 방지)
    }

    private Boolean againstEnhancedTitForTat(List<Boolean> opponentHistory) {
        int rounds = opponentHistory.size();

        if (rounds < 3) {
            return false; // 초반 3라운드 배신 → ETFT의 협력 비율 낮추기
        }
        // 기본적으로 협력 → ETFT가 신뢰를 다시 높이게 유도

        return rounds % 5 != 0; 
    }

    private Boolean againstSaint(List<Boolean> opponentHistory) {
        int rounds = opponentHistory.size();
        return rounds >= 9;
    }
    
    @Override
    public Strategy cloneStrategy() {
        return new Cheater();
    }
}