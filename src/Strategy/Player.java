package Strategy;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Player {
    private final String name;
    private final Strategy strategy;
    private int score = 0;
    private int battleCount = 1;
    private final Map<Player, List<Boolean>> history = new ConcurrentHashMap<>(); // 상대별 기록 유지

    public Player(String name, Strategy strategy) {
        this.name = name;
        this.strategy = strategy;
    }

    public boolean makeMove(Player opponent) {
        List<Boolean> opponentHistory = history.getOrDefault(opponent, new ArrayList<>());
        return strategy.choose(this, opponent, opponentHistory); // 상대별 기록 전달
    }

    public void updateHistory(Player opponent, boolean myMove, boolean opponentMove) {
        history.computeIfAbsent(opponent, k -> Collections.synchronizedList(new ArrayList<>())).add(opponentMove);
    }    

    public int getScore() {
        return score;
    }

    public int getBattleCount() {
        return battleCount;
    }

    public void addScore(int points) {
        this.score += points;
        battleCount++;
    }

    public String getName() {
        return name;
    }

    public Strategy getStrategy() {
        return this.strategy;
    }

    public boolean getMyLastHistory(Player opponent) {
        return history.get(opponent).get(history.get(opponent).size() - 1);
    }

    public Player cloneWithNewStrategy() {
        return new Player(this.name, this.strategy.cloneStrategy());  
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Player player = (Player) obj;
        return Objects.equals(name, player.name);  // 이름이 같으면 같은 플레이어로 간주
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);  // 이름 기반으로 해시코드 생성
    }
}