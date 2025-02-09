import Strategy.*;
import java.util.*;
import java.util.concurrent.*;
import javax.swing.SwingUtilities;

public class PrisonersDilemmaSimulation {
    private static final int TOTAL_GAMES = 10; // 총 반복 횟수
    private static final int ROUNDS_PER_GAME = 500; // 한 게임당 라운드 수

    private static List<Player> players;
    private static final Map<Player, Map<Player, Integer>> allGameResults = new ConcurrentHashMap<>();
    @SuppressWarnings({ "unchecked", "rawtypes" })
    private static final List<Map<Integer, Map<Player, Integer>>> roundScoresList = Collections.synchronizedList(new ArrayList());
    public static void main(String[] args) {
        runSimulation(TOTAL_GAMES);
        Map<Player, Map<Integer, Double>> averageRoundScores = calculateAverageRoundScores();
        // ✅ 그래프 실행 (여기서 ScoreGraph 호출)
        SwingUtilities.invokeLater(() -> {
            ScoreGraph graph = new ScoreGraph(averageRoundScores, ROUNDS_PER_GAME);
            //graph.displayGraph();
        });
    }

    @SuppressWarnings("CallToPrintStackTrace")
    public static void runSimulation(int numGames) {
        createPlayers();
        int availableCores = Runtime.getRuntime().availableProcessors();
        int threadPoolSize = Math.min(availableCores * 2, 100); // 최대 100개 제한
        ExecutorService executor = Executors.newFixedThreadPool(threadPoolSize);
        List<Future<Game>> futures = new ArrayList<>();

        // 각 게임을 비동기적으로 실행
        for (int gameIndex = 0; gameIndex < numGames; gameIndex++) {
            final int currentGame = gameIndex;
            futures.add(executor.submit(() -> {
                System.out.println("Game: " + currentGame + " 실행 중...");
        
                // 🔥 각 게임마다 독립적인 Player 리스트 생성 (클론 사용)
                List<Player> clonedPlayers = players.stream()
                    .map(Player::cloneWithNewStrategy)  // ✅ 독립적인 전략을 가진 새로운 플레이어 사용
                    .toList();
        
                Game game = new Game(clonedPlayers, ROUNDS_PER_GAME);
                game.playAndGetResults();
                return game;
            }));
        }        

        // 모든 게임이 완료될 때까지 대기하고 결과 병합
        for (Future<Game> future : futures) {
            try {
                Game game = future.get(); // 게임 실행 후 결과 가져오기
                mergeResults(game.getScoreBoard()); // 게임 결과 병합
                roundScoresList.add(game.getRoundScores()); // ✅ 각 게임의 라운드별 점수를 저장
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }

        executor.shutdown(); // 스레드 풀 종료

        // 🔥 최종 결과 출력
        displayResult(allGameResults);
    }

    private static synchronized void mergeResults(Map<Player, Map<Player, Integer>> gameResults) {
        for (Player p1 : gameResults.keySet()) {
            allGameResults.putIfAbsent(p1, new ConcurrentHashMap<>());
    
            for (Player p2 : gameResults.get(p1).keySet()) {
                int currentScore = gameResults.get(p1).get(p2);
    
                // ✅ 기존 점수 가져오기
                int previousScore = allGameResults.get(p1).getOrDefault(p2, 0);
                int newTotalScore = previousScore + (currentScore);
    
                // ✅ 업데이트된 점수 반영
                allGameResults.get(p1).put(p2, newTotalScore);
            }
        }
    }    

    private static void createPlayers() {
        List<Player> tempPlayers = new ArrayList<>();
        tempPlayers.add(new Player("보복하는 협력가 아야메", new TitForTat()));
        tempPlayers.add(new Player("교활한 협력가 나츠키", new DefectTitForTat()));
        tempPlayers.add(new Player("순수한 협력가 카나에", new AlwaysCooperate()));
        tempPlayers.add(new Player("냉혈한 배신자 아카네", new AlwaysDefect()));
        tempPlayers.add(new Player("미친 변덕쟁이 미유", new RandomStrategy()));
        tempPlayers.add(new Player("복수의 사도 카오리", new GrimTrigger()));
        tempPlayers.add(new Player("확률적 협력가 유즈키", new ProbabilisticTitForTat()));
        tempPlayers.add(new Player("용서하는 자 모모", new ForgivingTitForTat()));
        tempPlayers.add(new Player("이분법자 츠바사", new BinaryThinking()));
        tempPlayers.add(new Player("탐욕적인 기회주의자 레이나", new GreedyTitForTat()));
        tempPlayers.add(new Player("비열한 사기꾼 히나코", new Tranquilizer()));
        tempPlayers.add(new Player("신중한 보복가 미카", new Gradual()));
        tempPlayers.add(new Player("교대 배신자 이로하", new AlternateDefect()));
        tempPlayers.add(new Player("영악한 착취자 아스나", new Predator()));
        tempPlayers.add(new Player("철저한 분석가 나나세", new HandOfGod()));
        tempPlayers.add(new Player("지능형 배신자 사오리", new ShadowDefect()));
        tempPlayers.add(new Player("패턴 파괴자 우이", new PatternBreaker()));
        tempPlayers.add(new Player("협력 유도자 스즈메", new GuidingCooperator()));
        tempPlayers.add(new Player("정제된 광기 히카리", new AdaptiveTrickster()));
        tempPlayers.add(new Player("위대한 예언가 유키카", new Predictor()));
        tempPlayers.add(new Player("과거를 기억하는 미나", new Pavlov()));
        tempPlayers.add(new Player("반동분자 유리", new Merchant()));
        tempPlayers.add(new Player("가차없는 코노하", new Shepherd()));
        tempPlayers.add(new Player("즉흥적인 배짱이 하루카", new Grasshopper()));
        tempPlayers.add(new Player("멍텅구리 치히로", new Troller()));
        tempPlayers.add(new Player("도박사 키리코", new Gambler()));
        tempPlayers.add(new Player("감정적인 유리멘탈 치카코", new GlassMind()));
        tempPlayers.add(new Player("화해를 원하는 모네", new PeacefulTitForTat()));
        tempPlayers.add(new Player("인내하는 카렌", new Saint()));
        tempPlayers.add(new Player("빚을 잊지 않는 아이코", new Debt()));
        tempPlayers.add(new Player("분탕종자 레이", new MutualDestruction()));
        tempPlayers.add(new Player("정의로운 도둑 루카리", new RobinHood()));
        players = Collections.unmodifiableList(tempPlayers);
    }

    private static void displayResult(Map<Player, Map<Player, Integer>> allGameResults) {
        Map<Player, Double> avgGameScores = new HashMap<>();

        System.out.println("\n=== 상대전적 ===");
        for (Player p1 : allGameResults.keySet()) {
            int playerTotalScore = 0;

            System.out.println(p1.getName());

            for (Player p2 : allGameResults.get(p1).keySet()) {
                int score1 = allGameResults.get(p1).getOrDefault(p2, 0);
                int score2 = allGameResults.get(p2).getOrDefault(p1, 0);
                int scoreDifference = score1 - score2;
                System.out.printf("  vs %-10s: %.1f%n", p2.getName(), (double)(scoreDifference) / (double)TOTAL_GAMES);

                playerTotalScore += score1;
            }
            avgGameScores.put(p1, playerTotalScore / (double) (TOTAL_GAMES * players.size()));
        }

        List<Map.Entry<Player, Double>> sortedPlayers = new ArrayList<>(avgGameScores.entrySet());
        sortedPlayers.sort((a, b) -> Double.compare(b.getValue(), a.getValue()));

        System.out.println("\n=== 최종 순위 ===");
        int i = 0;
        for (Map.Entry<Player, Double> entry : sortedPlayers) {
            System.out.printf(++i + "위 %s, 평균 점수: %.2f%n",
                    entry.getKey().getName(), entry.getValue());
        }
    }

    private static Map<Player, Map<Integer, Double>> calculateAverageRoundScores() {
        Map<Player, Map<Integer, Double>> averageRoundScores = new ConcurrentHashMap<>();
    
        // System.out.println("\n=== [DEBUG] 저장된 roundScoresList 데이터 ===");
        // for (int i = 0; i < roundScoresList.size(); i++) {
        //     System.out.println("게임 " + (i + 1) + ": " + roundScoresList.get(i));
        // }
    
        for (Player player : players) {
            averageRoundScores.putIfAbsent(player, new ConcurrentHashMap<>());
            Map<Integer, Double> playerAverages = averageRoundScores.get(player);
    
            double cumulativeSum = 0.0; // 🔥 누적 합 저장
    
            for (int round = 1; round <= ROUNDS_PER_GAME; round++) {
                double roundTotal = 0.0;
                int count = 0;
    
                for (Map<Integer, Map<Player, Integer>> gameRoundScores : roundScoresList) {
                    if (gameRoundScores.containsKey(round) && gameRoundScores.get(round).containsKey(player)) {
                        roundTotal += gameRoundScores.get(round).get(player);
                        count++;
                    }
                }
    
                double roundAverage = (count > 0) ? (roundTotal / count) : 0.0;
                cumulativeSum += roundAverage; // 🔥 누적 점수 반영
                playerAverages.put(round, cumulativeSum / players.size());
            }
        }
    
        // // ✅ 디버깅 로그: 0번 플레이어 점수 확인
        // Player firstPlayer = players.get(0);
        // System.out.println("\n=== [테스트] 0번 플레이어의 평균 누적 점수 ===");
        // System.out.printf("%-10s %-15s\n", "라운드", "누적 평균 점수");
        // System.out.println("---------------------------------");
    
        // for (Integer round : new TreeSet<>(averageRoundScores.get(firstPlayer).keySet())) {
        //     System.out.printf("%-10d %-15.2f\n", round, averageRoundScores.get(firstPlayer).get(round));
        // }
    
        return averageRoundScores;
    }           
}