import Strategy.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

class Game {
    private final List<Player> players;
    private final int rounds;
    private final Map<Player, Map<Player, Integer>> scoreBoard = new HashMap<>();
    private final Map<Integer, Map<Player, Integer>> roundScores = new ConcurrentHashMap<>();

    public Game(List<Player> players, int rounds) {
        this.players = players;
        this.rounds = rounds;

        // 초기화: 상대별 점수 저장소
        for (Player p1 : players) {
            scoreBoard.put(p1, new HashMap<>());
            for (Player p2 : players) {
                if (p1 != p2) {
                    scoreBoard.get(p1).put(p2, 0);
                }
            }
        }
    }

    private void playRound(Player p1, Player p2, int round) {
        boolean move1 = p1.makeMove(p2);
        boolean move2 = p2.makeMove(p1);

        int p1Points, p2Points;

        if (move1 && move2) { // (C, C)
            p1Points = 3;
            p2Points = 3;
        } else if (move1 && !move2) { // (C, D)
            p1Points = 0;
            p2Points = 5;
        } else if (!move1 && move2) { // (D, C)
            p1Points = 5;
            p2Points = 0;
        } else { // (D, D)
            p1Points = 1;
            p2Points = 1;
        }

        p1.addScore(p1Points);
        p2.addScore(p2Points);

        // ✅ 라운드별 총점 기록 (플레이어 정보 없이 저장)
        roundScores.computeIfAbsent(round, k -> new ConcurrentHashMap<>()).merge(p1, p1Points, Integer::sum);
        roundScores.computeIfAbsent(round, k -> new ConcurrentHashMap<>()).merge(p2, p2Points, Integer::sum);
        //System.out.printf("[DEBUG] Round %d -> %s: %d, %s: %d\n", round, p1.getName(), p1Points, p2.getName(), p2Points);
        // 상대별 점수 누적
        scoreBoard.computeIfAbsent(p1, k -> new HashMap<>()).merge(p2, p1Points, Integer::sum);
        scoreBoard.computeIfAbsent(p2, k -> new HashMap<>()).merge(p1, p2Points, Integer::sum);

        p1.updateHistory(p2, move1, move2);
        p2.updateHistory(p1, move2, move1);
    }

    public void playAndGetResults() {
        for (int round = 1; round <= rounds; round++) {
            for (int j = 0; j < players.size(); j++) {
                for (int k = j + 1; k < players.size(); k++) {
                    playRound(players.get(j), players.get(k), round);
                }
            }
        }
    }

    public Map<Integer, Map<Player, Integer>> getRoundScores() {
        return roundScores;
    }
    public Map<Player, Map<Player, Integer>> getScoreBoard() {
        return scoreBoard;
    }
}