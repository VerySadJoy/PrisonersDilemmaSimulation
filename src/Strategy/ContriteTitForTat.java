package Strategy;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

// (보복형, 협력형) 실수를 인정하는 신중한 조정자, 사과할 줄 아는 지능적 협력자  
//  
// Contrite Tit-for-Tat (CTFT) 전략은 일반적인 Tit-for-Tat과 유사하지만,  
// 실수(Noise)로 인해 배신(D)했을 경우, 상대가 여전히 협력(C)했다면 이를 인식하고 사과(C)를 실행하는 변형된 전략이다.  
//  
// 이 전략의 핵심은 ‘의도치 않은 실수(배신)를 인지하고,  
// 상대가 용서해 줄 기회를 준다면 즉시 협력(C)하여 관계를 회복하는 것’이다.  
//  
// 전략의 작동 방식:  
// - 첫 번째 라운드는 무조건 협력(C).  
// - 기본적으로 Tit-for-Tat처럼 상대의 마지막 행동을 따라감.  
// - Noise(예기치 않은 배신)가 발생했을 경우,  
//   상대가 여전히 협력(C)을 유지하고 있으면 Contrite 모드(사과 모드)를 활성화.  
// - Contrite 모드에서는 무조건 협력(C)하여 사과, 이후 다시 일반적인 TFT 패턴으로 돌아감.  
//  
// 장점:  
// - 일반적인 Tit-for-Tat보다 부드러운 대응을 하여,  
//   실수로 인해 협력 관계가 완전히 깨지는 것을 방지할 수 있다.  
// - Forgiving Tit-for-Tat보다 더 정교한 판단을 통해 사과 여부를 결정하기 때문에,  
//   불필요한 협력을 줄이고 보복형 전략과도 일정 부분 균형을 맞출 수 있다.  
// - Noise(랜덤 배신)이 존재하는 환경에서 협력 관계를 유지하기 용이하다.  
//  
// 단점:  
// - 완전한 배신 전략(Always Defect)과 만나면,  
//   불필요한 사과를 반복할 가능성이 있어 손해를 볼 수 있다.  
// - 상대가 이 전략을 간파하면, 일부러 배신을 유도한 후 다시 협력하여  
//   사과를 강요하는 방식으로 착취할 가능성이 있다.  
// - 보복 성향이 약해지는 만큼, Two-Tits-for-Tat이나 Hard Tit-for-Tat과 비교하면  
//   억제력이 약할 수 있다.  
//  
// 실제 사회에서 이 전략을 따르는 인간 유형은 "실수를 인정하는 신중한 조정자" 또는 "사과할 줄 아는 지능적 협력자"이다.  
// 이들은 자신의 실수를 인정하고 즉시 관계를 회복하려는 태도를 유지하지만,  
// 너무 자주 사과하면 상대에게 이용당할 위험이 있다.  

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