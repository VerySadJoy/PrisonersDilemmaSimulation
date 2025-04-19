package Strategy;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * 전략 이름: ShadowDefect  
 * 전략 유형: 착취형 + 보복형 (그림자 속의 배신자, 신뢰를 시험하는 교활한 적응자)
 *
 * 전략 개요:  
 * - 초반에는 철저하게 협력해 신뢰를 쌓고  
 * - 이후엔 상대의 협력 성향을 분석해 예측 불가능한 방식으로 배신을 섞음  
 * - 협력가에겐 슬쩍 배신을 섞고, 배신자에겐 가끔 협력해 교란함  
 *
 * 작동 방식:  
 * - 1~5R: 무조건 협력 (상대 안심시키기)  
 * - 이후:  
 *   - 상대 협력률 75% 이상 → 20~50% 확률로 배신  
 *   - 협력률 40% 이하 → 20% 확률로만 협력 (나머지는 배신)  
 *   - 그 외 → 기본 협력 유지, 10R마다 30% 확률 배신으로 패턴 깨기  
 *
 * 장점:  
 * - 협력가에겐 기회주의적 배신, 배신자에겐 방어적 교란 가능  
 * - 불규칙한 패턴으로 예측 어렵게 만듦  
 * - 강자에겐 머리 조아리고, 약자에겐 살짝 물어뜯는 현실적 생존 전략  
 *
 * 단점:  
 * - Forgiving 전략에게도 반복적인 배신이 감지되면 보복당할 수 있음  
 * - 착취하려던 상대가 눈치채고 전환하면 오히려 역공당할 수도  
 * - 완전한 배신자 상대로는 초반 5턴이 무의미하게 낭비됨  
 *
 * 인간 유형 대응:  
 * - 신뢰를 기회로 보는 사람  
 * - 겉으론 온화하지만, 눈치 보고 순간적으로 배신할 줄 아는 성향  
 * - 내가 약할 땐 참지만, 네가 방심하면 바로 들어간다는 계산형  
 * - 장기 협력보단 순간 효율 극대화에 집중하는 똑똑한 포식자
*/ 

public class ShadowDefect implements Strategy {
    private final Map<Player, Integer> opponentCooperationCount = new HashMap<>(); // 협력 횟수 기록
    private final Map<Player, Integer> totalRounds = new HashMap<>(); // 총 경기 수 기록
    private final Random random = new Random();

    @Override
    public boolean choose(Player self, Player opponent, List<Boolean> opponentHistory) {
        totalRounds.put(opponent, totalRounds.getOrDefault(opponent, 0) + 1);
        int rounds = totalRounds.get(opponent);
        
        // 상대의 협력 횟수를 먼저 업데이트
        if (!opponentHistory.isEmpty() && opponentHistory.get(opponentHistory.size() - 1)) {
            opponentCooperationCount.put(opponent, opponentCooperationCount.getOrDefault(opponent, 0) + 1);
        }

        // 상대의 협력 비율 계산
        int cooperationCount = opponentCooperationCount.getOrDefault(opponent, 0);
        double cooperationRate = (double) cooperationCount / rounds;

        // 첫 5라운드는 무조건 협력(C) -> 상대가 방심하게 만들기
        if (rounds <= 5) {
            return true;
        }

        // 배신 타이밍 조정 (더 예측 불가능하게)
        if (cooperationRate > 0.75) {
            // 상대가 협력 비율이 높다면 20~50% 확률로 배신
            return random.nextDouble() > 0.3; 
        } 
        else if (cooperationRate < 0.4) {
            // 상대가 원래 배신을 많이 하면 맞배신하되, 가끔은 협력할 수도 있음 (20% 확률 협력)
            return random.nextDouble() < 0.2;
        } 
        else {
            // 기본적으로 협력 유지하되, 10라운드마다 30% 확률로 배신
            return rounds % 10 == 0 ? random.nextDouble() > 0.7 : true;
        }
    }

    @Override
    public Strategy cloneStrategy() {
        return new ShadowDefect();
    }
}