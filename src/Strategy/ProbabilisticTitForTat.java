package Strategy;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 전략 이름: ProbabilisticTitForTat  
 * 전략 유형: 보복형 + 랜덤형 (변덕스러운 확률적 대응자, 적응형 분석가)  
 * 
 * 전략 개요:  
 * - 상대의 협력 비율을 실시간으로 계산하여,  
 *   그 확률만큼 협력하고 나머지는 배신하는 확률 기반 Tit-for-Tat  
 * 
 * 작동 방식:  
 * - 첫 라운드는 무조건 협력 (C)  
 * - 이후 상대의 협력 비율(coopRate)을 기반으로 협력 확률 결정  
 *   - ex: coopRate가 0.7이라면 → 70% 확률 협력, 30% 확률 배신  
 * 
 * 장점:  
 * - 상대가 협력적일수록 높은 확률로 협력 유지 → 보복형 전략과 안정적 관계 가능  
 * - 약간의 배신이 섞여 있어 단순한 Tit-for-Tat보다 예측 불가능성 있음  
 * - 랜덤성으로 인해 조작/계산이 어려운 전략에게도 대처 가능  
 * 
 * 단점:  
 * - 상대가 배신을 반복하면 협력 확률이 급락하여 갈등이 심화될 수 있음  
 * - Forgiving 계열 전략과 만나면 예상치 못한 배신으로 관계 악화 위험  
 * - 확률적 판단이라 오차가 발생할 수 있어 신뢰 기반 유지가 어려움  
 * 
 * 인간 대응 유형:  
 * - "조건부로 믿는 사람", 또는 "조금은 의심 많은 전략적 협력자"  
 * - 항상 협력도, 항상 보복도 아닌, 통계를 믿고 행동하는 분석형 플레이어  
 * - 장기적으로는 실용적이지만, 신뢰 형성에는 시간이 걸리는 스타일  
 */

public class ProbabilisticTitForTat implements Strategy {
    private final Random random = new Random(); // 확률 기반 행동 결정을 위한 랜덤 객체
    private final Map<Player, List<Boolean>> opponentHistories = new ConcurrentHashMap<>(); // 안전한 기록 저장

    @Override
    public boolean choose(Player self, Player opponent, List<Boolean> opponentHistory) {
        // 리스트를 안전하게 저장하기 위해 CopyOnWriteArrayList 사용
        opponentHistories.putIfAbsent(opponent, new CopyOnWriteArrayList<>(opponentHistory));
        List<Boolean> history = opponentHistories.get(opponent);

        // 첫 번째 라운드는 기본적으로 협력(C)
        if (history.isEmpty()) {
            return true;
        }

        // 동기화된 블록에서 안전하게 접근
        double cooperationRate;
        synchronized (history) {
            long cooperationCount = history.stream().filter(b -> b).count();
            cooperationRate = (double) cooperationCount / history.size();
        }

        // 상대의 협력 비율만큼 확률적으로 협력(C)
        return random.nextDouble() < cooperationRate;
    }
    
    @Override
    public Strategy cloneStrategy() {
        return new ProbabilisticTitForTat();
    }
}