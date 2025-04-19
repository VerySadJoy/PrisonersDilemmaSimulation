package Strategy;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 전략 이름: ContriteTitForTat
 * 전략 개요: 기본적으로는 Tit-for-Tat 전략을 따르되, 실수로 배신했을 경우 상대가 여전히 협력하면 사과(C)를 실행하는 전략
 *
 * 전략 구조:
 * - 초기 행동: 협력(C)
 * - 행동 로직:
 *     - 일반적으로는 상대의 마지막 행동을 따라함 (Tit-for-Tat)
 *     - 만약 Noise로 인해 내가 배신했는데, 상대가 계속 협력했다면 → Contrite 모드로 진입
 *     - Contrite 모드에서는 무조건 협력(C)을 실행하여 사과하고, 그 후 일반 모드로 복귀
 * - 반응성: 있음 (상대의 협력 지속 여부와 본인의 행동 불일치를 탐지)
 * - 기억 활용: 있음 (상대의 행동뿐 아니라 본인의 행동도 저장하여 비교)
 * - 랜덤 요소: 없음 (결정적 조건 기반 판단)
 *
 * 전략 성향 분류:
 * - 협력 전략인가? → O (기본적으로 협력 지향적이며, 관계 회복을 중시함)
 * - 배신 전략인가? → X (보복은 하지만, 조건부이며 사과 가능)
 * - 패턴 기반인가? → X (Tit-for-Tat 기반 + Noise 대응 로직)
 * - 상대 반응형인가? → O (상대의 마지막 행동과 관계 흐름에 적극 반응)
 * - 예측 가능성 → 중간 (TFT 기반이지만 사과 조건은 읽기 어려움)
 *
 * 인간 유형 대응:
 * - 실수를 인정하고 책임지려는 신중한 조정자
 * - 자신의 잘못을 정확히 인식하고, 상대가 그럼에도 협력할 경우 사과를 통해 관계 회복을 시도함
 * - 지나친 고집이나 완고함 없이, 유연하게 행동을 조정할 수 있는 감정지능을 지님
 * - 다만, 이 사과가 반복되면 상대에게 이용당할 여지가 있으며, 상대의 악의적 행동을 감지하지 못할 수도 있음
 * - 결과적으로는 신뢰 기반 사회에서 빛을 발하는 전략이며, 협력적인 분위기를 유지하는 데 탁월함
*/

public class ContriteTitForTat implements Strategy {
    private final Map<Player, Boolean> contriteMode = new ConcurrentHashMap<>(); // Contrite 모드 활성화 여부
    private final Map<Player, List<Boolean>> opponentHistories = new ConcurrentHashMap<>(); // 상대 기록 저장
    private final Map<Player, List<Boolean>> selfHistories = new ConcurrentHashMap<>(); // 본인의 기록 저장 (Noise 검출용)

    @Override
    public boolean choose(Player self, Player opponent, List<Boolean> opponentHistory) {
        // 상대 기록 관리
        opponentHistories.putIfAbsent(opponent, new CopyOnWriteArrayList<>(opponentHistory));
        List<Boolean> history = opponentHistories.get(opponent);

        // 본인의 기록도 관리 (Noise 감지용)
        selfHistories.putIfAbsent(opponent, new CopyOnWriteArrayList<>());
        List<Boolean> selfHistory = selfHistories.get(opponent);

        // 첫 번째 라운드는 무조건 협력(C)
        if (history.isEmpty()) {
            selfHistory.add(true); // 자신의 협력 기록 추가
            return true;
        }

        int historySize = history.size();
        boolean lastOpponentMove = history.get(historySize - 1); // 상대의 마지막 행동

        // Contrite 모드 활성화 여부 가져오기 (초기값: false)
        contriteMode.putIfAbsent(opponent, false);
        boolean isContrite = contriteMode.get(opponent);

        // Contrite 모드일 경우, 협력(C)하여 사과
        if (isContrite) {
            contriteMode.put(opponent, false); // 사과 후 Contrite 모드 해제
            selfHistory.add(true);
            return true; // 무조건 협력 (사과)
        }

        // Noise 검출: 본인의 마지막 행동과 기록된 행동이 다르면 Noise 발생한 것으로 간주
        if (selfHistory.size() >= historySize) {
            boolean lastRecordedMove = selfHistory.get(historySize - 1); // 본인이 기억하는 마지막 행동
            if (lastRecordedMove != lastOpponentMove && lastRecordedMove == false && lastOpponentMove == true) {
                // 내가 Noise 때문에 배신(D)했지만, 상대는 계속 협력(C)한 경우 → Contrite 모드 활성화
                contriteMode.put(opponent, true);
                selfHistory.add(true);
                return true; // 사과 (협력)
            }
        }

        // 기본적으로 Tit For Tat 동작 유지
        selfHistory.add(lastOpponentMove);
        return lastOpponentMove; // 상대의 마지막 행동을 따라감 (TFT)
    }

    @Override
    public Strategy cloneStrategy() {
        return new ContriteTitForTat();
    }
}