package Strategy;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 전략 이름: MutualDestruction
 * 전략 개요: 상대의 배신률이 일정 수준을 넘으면 '공멸 모드'로 전환해 끝까지 따라가는 전략
 *
 * 전략 구조:
 * - 초기 행동: 10라운드 동안 랜덤 협력/배신 (탐색)
 * - 행동 로직:
 *   - 상대의 배신 비율이 40% 이상이면 공멸 모드 활성화
 *   - 공멸 모드에서는:
 *     - 상대가 배신하면 → 무조건 배신
 *     - 상대가 협력하면 → 50% 확률로 배신
 *   - 공멸 모드 이전에는 항상 협력
 * - 반응성: 중간 (초기 탐색 이후에는 매우 강한 반응)
 * - 기억 활용: 있음 (상대의 배신 횟수, 총 라운드 수 기록)
 * - 랜덤 요소: 있음 (초기 탐색, 공멸 모드 내 행동 일부 랜덤)
 *
 * 전략 성향 분류:
 * - 협력 전략인가? → X (탐색 후 협력, 그러나 조건부)
 * - 배신 전략인가? → △ (공멸 모드 이후는 거의 배신)
 * - 패턴 기반인가? → X (고정 패턴은 아님)
 * - 상대 반응형인가? → O (상대의 누적 배신 비율을 기준으로 반응)
 * - 예측 가능성 → 낮음 (초기 랜덤, 이후 모드 전환 기준 존재)
 *
 * 인간 유형 대응:
 * - 극단적 복수자 혹은 공멸을 불사하는 파괴자
 * - 상대의 배신을 몇 번은 참지만, 일정 임계점을 넘으면
 *   더 이상 협력을 시도하지 않고 보복에 집중한다.
 * - 스스로 손해를 감수하더라도 배신자를 끝까지 응징하려는
 *   강한 복수심 기반의 행동 패턴을 보임
 * - 이러한 성향은 억제 효과가 크지만, 신뢰 회복이 거의 불가능하다는 단점도 동반함
*/

public class MutualDestruction implements Strategy {
    private final ConcurrentHashMap<Player, Boolean> suicideMode = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<Player, Integer> betrayCount = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<Player, Integer> totalRounds = new ConcurrentHashMap<>();
    private final Random random = new Random();

    @Override
    public boolean choose(Player self, Player opponent, List<Boolean> opponentHistory) {
        suicideMode.putIfAbsent(opponent, false);
        betrayCount.putIfAbsent(opponent, 0);
        totalRounds.putIfAbsent(opponent, 0);

        int rounds = totalRounds.get(opponent);
        totalRounds.put(opponent, rounds + 1);

        // 첫 10라운드는 랜덤하게 협력/배신을 섞음
        if (rounds < 10) {
            boolean move = random.nextBoolean();
            if (!move) betrayCount.put(opponent, betrayCount.get(opponent) + 1);
            return move;
        }

        // 상대가 40% 이상 배신하면 함께 죽자 모드 활성화
        double betrayalRate = (double) betrayCount.get(opponent) / rounds;
        if (betrayalRate >= 0.4) {
            suicideMode.put(opponent, true);
        }

        // 함께 죽자 모드: 상대 배신하면 나도 배신, 상대 협력하면 50% 확률로 배신
        if (suicideMode.get(opponent)) {
            return opponentHistory.get(opponentHistory.size() - 1) || random.nextBoolean();
        }

        return true; // 평소엔 협력
    }

    @Override
    public Strategy cloneStrategy() {
        return new MutualDestruction();
    }
}