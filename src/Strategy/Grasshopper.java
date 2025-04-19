package Strategy;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 전략 이름: Grasshopper
 * 전략 개요: 직전 라운드 점수에 따라 즉흥적으로 협력 또는 배신을 선택하는 반응형 전략
 *
 * 전략 구조:
 * - 초기 행동: 랜덤 (50% 협력/배신)
 * - 행동 로직:
 *   - 직전 라운드 점수가 6 (C, C) → 랜덤 선택 (협력 or 배신)
 *   - 점수 5 (C, D or D, C) → 협력/배신 구분 없이 default 협력
 *   - 점수 2 (D, D) → 무조건 배신
 * - 반응성: 높음 (점수 기반 즉각 반응)
 * - 기억 활용: 1턴 (직전 점수만 사용)
 * - 랜덤 요소: 있음 (6점일 경우 랜덤)
 *
 * 전략 성향 분류:
 * - 협력 전략인가? → △ 랜덤과 반응에 따라 다름
 * - 배신 전략인가? → △ 불리할 때는 확실히 배신
 * - 패턴 기반인가? → O (점수 기반 반응형)
 * - 상대 반응형인가? → X (직접적 반응은 아님, 점수에 따라 간접 반응)
 * - 예측 가능성 → 낮음 (랜덤성과 점수 기반이 결합된 혼합형)
 *
 * 인간 유형 대응:
 * - 충동적인 도약자 혹은 감정적 반응형 플레이어
 * - 자신의 상황(이득/손해)에 민감하게 반응하며 즉흥적으로 결정함
 * - 장기적인 계획 없이 순간의 손익에 따라 협력과 배신을 오간다
 * - 때로는 예측 불가능한 행동으로 상대에게 혼란을 주지만,
 *   반복될 경우 신뢰를 잃고 장기적으로 불리해질 수 있다
 * - 협력 유지는 가능하지만, 안정적 관계 구축은 어려운 유형
*/

public class Grasshopper implements Strategy {
    private final Map<Player, Integer> lastRoundScores = new ConcurrentHashMap<>();
    private final Random random = new Random();

    @Override
    public boolean choose(Player self, Player opponent, List<Boolean> opponentHistory) {
        if (!lastRoundScores.containsKey(opponent)) {
            return random.nextBoolean(); // 첫 라운드는 랜덤
        }

        int lastScore = lastRoundScores.get(opponent);

        switch (lastScore) {
            case 6 -> {
                // (C, C) → 3 + 3
                return random.nextBoolean(); // 랜덤하게 협력 or 배신
            }
            case 5 -> {
                // (C, D) or (D, C)
                return lastScore != 0; // (C, D) → 협력 / (D, C) → 배신
            }
            case 2 -> {
                // (D, D) → 1 + 1
                return false; // 배신
            }
            default -> {
            }
        }

        return true; // 기본적으로 협력 (이론상 도달할 수 없는 경우지만 대비)
    }

    // 상대 플레이어별 점수를 저장 (게임이 끝날 때 호출해야 함)
    public void updateScore(Player opponent, int score) {
        lastRoundScores.put(opponent, score);
    }

    @Override
    public Strategy cloneStrategy() {
        return new Grasshopper();
    }
}