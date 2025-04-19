package Strategy;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 전략 이름: PeacefulTitForTat
 * 전략 유형: 보복형 + 협력형 (참을성 있는 화해자, 신중한 평화주의자)

 * 전략 개요:
 * - 기본적으로 Tit-for-Tat을 따르되, 상대가 5번 연속 배신하면 화해를 시도하는 온화한 변형
 * - "끝없는 보복보다는 화해의 기회를 주는 신중한 대응자"

 * 작동 방식:
 * - 첫 라운드: 무조건 협력 (C)
 * - 이후:
 *   - 상대가 협력하면 협력, 배신하면 배신 (기본 Tit-for-Tat 로직)
 *   - 상대가 5번 연속 배신하면, 한 번 협력(C) 시도 (신뢰 회복 유도)
 *   - 이후 다시 상대 행동에 따라 반응 (협력이면 협력, 배신이면 보복 재개)

 * 장점:
 * - 실수로 반복 배신한 상대도 다시 협력 관계로 복귀 가능
 * - 완전한 협력가보단 방어적이고, 완전한 보복가보단 유연함
 * - 보복과 용서의 균형이 잘 잡혀 있음

 * 단점:
 * - 교활한 전략이 의도적으로 5번 배신 후 협력을 유도하면 착취당할 수 있음
 * - ForgivingTitForTat보다도 관대한 편이라 악용 가능성 존재
 * - 반복적 배신자와의 대결에선 다소 손해를 감수할 수 있음

 * 인간 대응 유형:
 * - 참을성 있는 화해자
 * - 일정 수준의 배신도 포용하려는 평화주의 성향
 * - 강경하게는 나가지 않지만, 너무 관대한 게 약점이 될 수 있음
*/ 

public class PeacefulTitForTat implements Strategy {
    private final ConcurrentHashMap<Player, Integer> consecutiveDefects = new ConcurrentHashMap<>(); // 연속 배신 횟수
    private final int FORGIVENESS_THRESHOLD = 5; // 5번 연속 배신 시 화해 시도

    @Override
    public boolean choose(Player self, Player opponent, List<Boolean> opponentHistory) {
        int roundsPlayed = opponentHistory.size();

        // 상대방 히스토리가 없으면 기본적으로 협력 (첫 턴)
        if (roundsPlayed == 0) {
            return true;
        }

        // 상대방의 마지막 행동
        boolean opponentLastMove = opponentHistory.get(roundsPlayed - 1);

        // 연속 배신 횟수 체크
        consecutiveDefects.putIfAbsent(opponent, 0);
        if (!opponentLastMove) { // 상대가 배신했다면
            consecutiveDefects.put(opponent, consecutiveDefects.get(opponent) + 1);
        } else { // 상대가 협력하면 연속 배신 횟수 리셋
            consecutiveDefects.put(opponent, 0);
        }

        // 만약 내가 5번 연속 배신했다면, 한 번 협력 시도
        if (consecutiveDefects.get(opponent) >= FORGIVENESS_THRESHOLD) {
            consecutiveDefects.put(opponent, 0); // 배신 카운트 리셋
            return true; // 화해의 손길!
        }

        // 기본적으로 Tit-for-Tat 방식 (상대가 협력하면 협력, 배신하면 배신)
        return opponentLastMove;
    }

    @Override
    public Strategy cloneStrategy() {
        return new PeacefulTitForTat();
    }
}