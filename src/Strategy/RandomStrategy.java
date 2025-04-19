package Strategy;

import java.util.List;
import java.util.Random;

/**
 * 전략 이름: RandomStrategy  
 * 전략 유형: 랜덤형 (예측 불가능한 광인, 무작위적 혼돈의 창조자)
 *
 * 전략 개요:  
 * - 모든 선택이 50% 확률로 결정되는 완전 무작위 전략  
 * - 상대의 행동과 무관하게 의도도 일관성도 없는 혼돈의 플레이를 실행
 *
 * 작동 방식:  
 * - 매 라운드: 무조건 50% 확률로 협력(C) 또는 배신(D) 중 하나 선택  
 * - 상대 기록, 내 행동 기록, 전략적 분석 등 일절 고려하지 않음  
 * - 고정된 규칙 없이 오직 랜덤 값으로만 전략이 구성됨
 *
 * 장점:  
 * - 분석 불가 → 상대가 예측하거나 공략하기 매우 어려움  
 * - 패턴 기반 전략(Tit-for-Tat, Pavlov 등)에게 혼란을 줄 수 있음  
 * - 상황에 따라 예기치 않은 이득을 가져올 수도 있음
 *
 * 단점:  
 * - 협력 유지가 불가능함 → 장기적 신뢰 관계 형성이 불가  
 * - 보복형 전략에게 지속적 배신으로 간주되어 강한 손해 가능  
 * - 전략이 없기 때문에 전략 간 상성과 최적화 구조를 고려하지 않음
 *
 * 인간 대응 유형:  
 * - 예측할 수 없는 광기
 * - 이성보다 직감을 따르고, 어떤 논리로도 행동을 설명할 수 없음  
 * - 혼돈이 곧 전략인 사람, 스스로도 왜 그렇게 했는지 모를 수 있음  
 * - 흥미롭지만 신뢰하기 어려운, 카오스 그 자체
 */ 

public class RandomStrategy implements Strategy {
    private final Random random = new Random();

    @Override
    public boolean choose(Player self, Player opponent, List<Boolean> opponentHistory) {
        // 무조건 50% 확률로 협력(C) 또는 배신(D)
        return random.nextBoolean();
    }

    @Override
    public Strategy cloneStrategy() {
        return new RandomStrategy();
    }
}