package Strategy;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

public class PatternBreaker implements Strategy {
    private final Map<Player, Integer> opponentCooperationCount = new ConcurrentHashMap<>();
    private final Map<Player, Integer> opponentDefectionCount = new ConcurrentHashMap<>();
    private final Map<Player, Integer> totalRounds = new ConcurrentHashMap<>();
    private final Random random = new Random();

    @Override
    public boolean choose(Player self, Player opponent, List<Boolean> opponentHistory) {
        // ✅ `computeIfAbsent()`로 안전한 초기화
        totalRounds.computeIfAbsent(opponent, k -> 0);
        opponentCooperationCount.computeIfAbsent(opponent, k -> 0);
        opponentDefectionCount.computeIfAbsent(opponent, k -> 0);

        totalRounds.put(opponent, totalRounds.get(opponent) + 1);
        int rounds = totalRounds.get(opponent);

        // 첫 10라운드는 데이터 수집을 위해 기본적으로 협력
        if (rounds <= 10) {
            return true;
        }

        // ✅ 안전한 협력/배신 횟수 기록
        int coopCount = opponentCooperationCount.get(opponent);
        int defectCount = opponentDefectionCount.get(opponent);

        if (!opponentHistory.isEmpty()) {
            boolean lastMove = opponentHistory.get(opponentHistory.size() - 1);
            if (lastMove) {
                opponentCooperationCount.put(opponent, coopCount + 1);
            } else {
                opponentDefectionCount.put(opponent, defectCount + 1);
            }
        }

        double coopRate = (double) coopCount / Math.max(1, rounds);  // 🔥 0으로 나누는 문제 방지
        double defectRate = (double) defectCount / Math.max(1, rounds);

        // 🔥 상대 패턴 분석 후 패턴 깨기 🔥

        // 1️⃣ **교대 배신자 패턴 깨기**
        if (coopRate > 0.45 && coopRate < 0.55) {  // C-D-C-D 같은 일정 패턴 감지
            return rounds % 3 == 0;  // 주기적으로 패턴 깨뜨림 (예상과 다르게 행동)
        }

        // 2️⃣ **확률적 협력가(랜덤 대응) 감지**
        if (coopRate > 0.3 && coopRate < 0.7) {  // 너무 랜덤하게 행동하는 경우
            return random.nextBoolean();  // 랜덤으로 대응 (50% 확률)
        }

        // 3️⃣ **완전한 협력가(Always Cooperate) 착취**
        if (coopRate > 0.9) {
            return false;  // 착취 (배신)
        }

        // 4️⃣ **완전한 배신자(Always Defect) 유인**
        if (defectRate > 0.9) {
            return rounds % 5 == 0;  // 가끔 협력해서 상대를 헷갈리게 만듦
        }

        // 5️⃣ **보복형 협력가(T4T, Grim Trigger) 조심스럽게 대응**
        if (coopRate > 0.6) {
            return rounds % 4 != 0;  // 협력 유지하며 가끔 배신 (균형 유지)
        }

        // 기본적으로 협력 유지 (패턴 분석이 끝나지 않은 경우)
        return true;
    }

    
    @Override
    public Strategy cloneStrategy() {
        return new PatternBreaker();
    }
}