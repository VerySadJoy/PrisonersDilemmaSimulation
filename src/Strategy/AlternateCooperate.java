package Strategy;

import java.util.List;

// (랜덤형, 협력형) 예측 가능한 낙관주의자, 고집 있는 이상가
//
// AlternateCooperate 전략은 상대의 행동에 상관없이 C → D → C → D 패턴을 무한히 반복하는 전략이다.
// 즉, 상대가 협력하든 배신하든 일정한 주기에 따라 협력과 배신을 반복한다.
//
// 이 전략의 특징:
// - 첫 번째 라운드는 무조건 협력(C).
// - 이후 C → D → C → D 순서로 계속 반복.
//
// 장점:
// - 상대가 패턴을 이해하면 일정한 협력을 유도할 수 있음.
// - 랜덤한 전략과 만나도 안정적인 성과를 낼 가능성이 있음.
// - 특정 전략(예: Tit-for-Tat)과 만나면 반복적인 균형을 유지할 수도 있음.
//
// 단점:
// - 상대가 패턴을 분석하면 쉽게 공략당할 가능성이 큼.
// - 신뢰 기반 전략과 만나면 절반의 라운드에서 손해를 볼 수 있음.
// - 보복형 전략과 만나면 지속적인 배신을 당할 가능성이 있음.
//
// 실제 사회에서 이 전략을 따르는 인간 유형은 "예측 가능한 낙관주의자" 또는 "신념 있는 이상가"이다.
// 이들은 자신의 원칙을 고수하며, 상대가 어떻게 나오든 주어진 패턴을 따른다.
// 협력과 배신을 절반씩 반복하기 때문에 완전한 협력 전략보다는 실용적일 수 있지만,
// 특정 상황에서는 신뢰를 얻기 어렵고, 예측 가능한 패턴이 단점이 될 수도 있다.

public class AlternateCooperate implements Strategy {
    @Override
    public boolean choose(Player self, Player opponent, List<Boolean> opponentHistory) {
        // 첫 번째 라운드는 무조건 협력 (C)
        if (opponentHistory.isEmpty()) {
            return true;
        }
        // 짝수 번째 선택에서는 협력 (C), 홀수 번째 선택에서는 배신 (D)
        return opponentHistory.size() % 2 == 0;
    }

    @Override
    public Strategy cloneStrategy() {
        return new AlternateCooperate();
    }
}