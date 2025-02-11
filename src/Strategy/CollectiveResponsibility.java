package Strategy;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

// (보복형) 네트워크 전체 협력/배신 비율을 고려하여 행동을 결정하는 전략
//
// CollectiveResponsibility 전략은 상대의 개별 행동이 아니라,
// 네트워크 전체의 협력/배신 비율을 기반으로 행동을 결정함.
//
// 멀티 환경 지원: 전체 플레이어의 데이터를 공유하여 분석
// 연대책임 적용: 네트워크의 협력 비율이 높으면 협력, 배신 비율이 높으면 배신
//
// 전략의 작동 방식:
// - 초기 상태에서는 협력 (C)
// - 네트워크에서 기록된 모든 플레이어의 협력/배신 비율을 계산
// - 배신 비율이 더 높으면 배신 (D), 협력 비율이 같거나 많으면 협력 (C)
//
// 장점:
// - 네트워크 전체 협력을 장려하고 배신자가 많아지면 자동으로 대응 가능
// - 특정 상대가 아니라 네트워크 전반적인 분위기를 반영하여 반응
// - 새로운 플레이어도 기존 네트워크 분위기에 맞춰 참여 가능
//
// 단점:
// - 네트워크에서 배신자가 많아지면 협력을 유지하기 어려움
// - 특정 개인에게 직접적인 보복을 하지 않기 때문에 개별 학습이 어렵다

public class CollectiveResponsibility implements Strategy {
    private final Map<Player, Integer> cooperationCount = new HashMap<>(); // 플레이어별 협력 횟수
    private final Map<Player, Integer> totalRounds = new HashMap<>(); // 플레이어별 총 라운드 수

    @Override
    public boolean choose(Player self, Player opponent, List<Boolean> opponentHistory) {
        // 상대 플레이어의 전체 플레이 횟수 증가
        totalRounds.put(opponent, totalRounds.getOrDefault(opponent, 0) + 1);

        // 상대가 협력한 경우 협력 횟수 증가
        if (!opponentHistory.isEmpty() && opponentHistory.get(opponentHistory.size() - 1)) {
            cooperationCount.put(opponent, cooperationCount.getOrDefault(opponent, 0) + 1);
        }

        // 네트워크 전체 협력/배신 비율 계산
        int totalCooperation = 0;
        int totalInteractions = 0;

        for (Player p : totalRounds.keySet()) {
            totalInteractions += totalRounds.get(p);
            totalCooperation += cooperationCount.getOrDefault(p, 0);
        }

        // 데이터가 없으면 기본적으로 협력 (C)
        if (totalInteractions == 0) {
            return true;
        }

        // 협력 비율 계산
        double cooperationRate = (double) totalCooperation / totalInteractions;

        // 배신 비율이 더 높으면 배신 (D), 협력 비율이 같거나 많으면 협력 (C)
        return cooperationRate >= 0.5;
    }

    @Override
    public Strategy cloneStrategy() {
        return new CollectiveResponsibility();
    }
}