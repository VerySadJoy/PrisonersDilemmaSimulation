package Strategy;

import java.util.List;

// DefectTitForTat 전략: 첫 번째 라운드는 무조건 배신(D),
// 이후부터는 상대의 지난 행동을 따라가는 변형된 팃포탯 전략
public class DefectTitForTat implements Strategy {

    @Override
    public boolean choose(Player self, Player opponent, List<Boolean> opponentHistory) {
        // 상대방의 행동 기록이 비어 있다면 (즉, 첫 라운드라면) 무조건 배신(D) 선택
        if (opponentHistory.isEmpty()) {
            return false;
        }

        // 이후부터는 상대방의 마지막 행동을 따라감 (팃포탯 원칙)
        return opponentHistory.get(opponentHistory.size() - 1);
    }
    
    @Override
    public Strategy cloneStrategy() {
        return new DefectTitForTat();
    }
}