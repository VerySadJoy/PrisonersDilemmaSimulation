import Strategy.*;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;
import javax.swing.SwingUtilities;

public class PrisonersDilemmaSimulation {
    private static final int TOTAL_GAMES = 50; // 총 반복 횟수
    private static final int ROUNDS_PER_GAME = 100; // 한 게임당 라운드 수

    private static List<Player> players;
    private static Map<Player, Map<Player, Integer>> allGameResults = new ConcurrentHashMap<>();
    @SuppressWarnings({ "unchecked", "rawtypes" })
    private static final List<Map<Integer, Map<Player, Integer>>> roundScoresList = Collections.synchronizedList(new ArrayList());

    private static final List<Map<Player, Double>> gameScoreHistory = new ArrayList<>();
    private static final List<Player> allPlayers = new ArrayList<>(); // 고정된 전체 플레이어 목록

    public static void main(String[] args) {
        runSimulationUntilOneLeft();

        Map<Player, Map<Integer, Double>> averageRoundScores = calculateDeltaScores(); // 또는 calculateDeltaScores();
        exportGameScoresToCSV("round_ranks.csv");
        //  그래프 실행 (여기서 ScoreGraph 호출)
        SwingUtilities.invokeLater(() -> {
            @SuppressWarnings("unused")
            ScoreGraph graph = new ScoreGraph(averageRoundScores, ROUNDS_PER_GAME);
            //graph.displayGraphWithBestFit(new ArrayList<>());
        });
    }
    
    @SuppressWarnings("CallToPrintStackTrace")
    private static void exportGameScoresToCSV(String filename) {
        try (FileWriter writer = new FileWriter(filename)) {
            // 헤더
            writer.write("Game");
            for (Player p : allPlayers) {
                writer.write("," + p.getName());
            }
            writer.write("\n");
    
            // 각 게임 기록
            for (int gameIndex = 0; gameIndex < gameScoreHistory.size(); gameIndex++) {
                Map<Player, Double> scoreMap = gameScoreHistory.get(gameIndex);
                writer.write(String.valueOf(gameIndex + 1));
    
                for (Player p : allPlayers) {
                    Optional<Player> matched = scoreMap.keySet().stream()
                        .filter(player -> player.getName().equals(p.getName()))
                        .findFirst();
    
                    if (matched.isPresent()) {
                        writer.write("," + String.format("%.2f", scoreMap.get(matched.get())));
                    } else {
                        writer.write(",-");
                    }
                }
                writer.write("\n");
            }
    
            System.out.println("게임별 평균 점수 저장 완료: " + filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }    

    @SuppressWarnings("CallToPrintStackTrace")
    public static void runSimulation(int numGames) {
        roundScoresList.clear();
        allGameResults.clear();
        int availableCores = Runtime.getRuntime().availableProcessors();
        int threadPoolSize = Math.min(availableCores * 2, 100); // 최대 100개 제한
        ExecutorService executor = Executors.newFixedThreadPool(threadPoolSize);
        List<Future<Game>> futures = new ArrayList<>();

        // 각 게임을 비동기적으로 실행
        for (int gameIndex = 0; gameIndex < numGames; gameIndex++) {
            futures.add(executor.submit(() -> {
                
                //  각 게임마다 독립적인 Player 리스트 생성 (클론 사용)
                List<Player> clonedPlayers = players.stream()
                    .map(Player::cloneWithNewStrategy)  //  독립적인 전략을 가진 새로운 플레이어 사용
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
                roundScoresList.add(game.getRoundScores()); //  각 게임의 라운드별 점수를 저장
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }

        executor.shutdown();
        try {
            if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
        }
    }

    private static synchronized void mergeResults(Map<Player, Map<Player, Integer>> gameResults) {
        for (Player p1 : gameResults.keySet()) {
            allGameResults.putIfAbsent(p1, new ConcurrentHashMap<>());
    
            for (Player p2 : gameResults.get(p1).keySet()) {
                int currentScore = gameResults.get(p1).get(p2);
    
                //  기존 점수 가져오기
                int previousScore = allGameResults.get(p1).getOrDefault(p2, 0);
                int newTotalScore = previousScore + (currentScore);
    
                //  업데이트된 점수 반영
                allGameResults.get(p1).put(p2, newTotalScore);
            }
        }
    }    

    private static void createPlayers(List<Player> removeList) {
        players = null;
        List<Player> tempPlayers = new ArrayList<>();
        tempPlayers.add(new Player("교대 협력자 하루", new AlternateCooperate()));
        tempPlayers.add(new Player("교대 배신자 이로하", new AlternateDefect()));
        tempPlayers.add(new Player("순수한 협력가 카나에", new AlwaysCooperate()));
        tempPlayers.add(new Player("냉혈한 배신자 아카네", new AlwaysDefect()));
        tempPlayers.add(new Player("나쁜 사람 미루", new BadPerson()));
        tempPlayers.add(new Player("이분법자 츠바사", new BinaryThinking()));
        tempPlayers.add(new Player("사기캐 시유", new Cheater()));
        tempPlayers.add(new Player("연대책임론자 안즈", new CollectiveResponsibility()));
        tempPlayers.add(new Player("상황을 판단하려 드는 아이리스", new ConditionalCommitment()));
        tempPlayers.add(new Player("보복을 두려워하는 배신자 사라", new ConditionalForgiver()));
        tempPlayers.add(new Player("사과할줄 아는 루나", new ContriteTitForTat()));
        tempPlayers.add(new Player("빚을 잊지 않는 아이코", new Debt()));
        tempPlayers.add(new Player("정제된 광기 히카리", new DeceptiveAdaptation()));
        tempPlayers.add(new Player("계산적인 장사꾼 이치카", new DiscountFactor()));
        tempPlayers.add(new Player("자애로운 협력가 리나", new DynamicTitForTat()));
        tempPlayers.add(new Player("생각하는 보복가 카즈하", new EnhancedTitForTat()));
        tempPlayers.add(new Player("약육강식 노엘", new Flatterer()));
        tempPlayers.add(new Player("용서하는 자 모모", new ForgivingTitForTat()));
        tempPlayers.add(new Player("도박사 키리코", new Gambler()));
        tempPlayers.add(new Player("감정적인 유리멘탈 치카코", new GlassMind()));
        tempPlayers.add(new Player("좋은 사람 세리", new GoodPerson()));
        tempPlayers.add(new Player("신중한 보복가 미카", new Gradual()));
        tempPlayers.add(new Player("즉흥적인 배짱이 하루카", new Grasshopper()));
        tempPlayers.add(new Player("탐욕적인 기회주의자 레이나", new GreedyTitForTat()));
        tempPlayers.add(new Player("복수의 사도 카오리", new GrimTrigger()));
        tempPlayers.add(new Player("협력 유도자 스즈메", new GuidingCooperator()));
        tempPlayers.add(new Player("철저한 분석가 나나세", new HandOfGod()));
        tempPlayers.add(new Player("뒷끝있는 협력자 키누에", new HardTitForTat()));
        tempPlayers.add(new Player("반동분자 유리", new Merchant()));
        tempPlayers.add(new Player("진화하는 히토미", new Mutation()));
        tempPlayers.add(new Player("분탕종자 레이", new MutualDestruction()));
        tempPlayers.add(new Player("기분파 인간 아이", new NoisyTitForTat()));
        tempPlayers.add(new Player("전략적 판단가 니코", new OmegaTitForTat()));
        tempPlayers.add(new Player("성향 시험자 아리스", new OpponentTester()));
        tempPlayers.add(new Player("교활한 인간 린", new Opportunist()));
        tempPlayers.add(new Player("패턴 파괴자 우이", new PatternBreaker()));
        tempPlayers.add(new Player("과거를 기억하는 미나", new Pavlov()));
        tempPlayers.add(new Player("화해를 원하는 모네", new PeacefulTitForTat()));
        tempPlayers.add(new Player("영악한 착취자 아스나", new Predator()));
        tempPlayers.add(new Player("위대한 예언가 유키카", new Predictor()));
        tempPlayers.add(new Player("확률적 협력가 유즈키", new ProbabilisticTitForTat()));
        tempPlayers.add(new Player("최근을 더 고려하는 리츠", new ProbabilisticWeightedTitForTat()));
        tempPlayers.add(new Player("미친 변덕쟁이 미유", new RandomStrategy()));
        tempPlayers.add(new Player("의심을 거두는 아오이", new ReverseGrimTrigger()));
        tempPlayers.add(new Player("붉은 혁명 루카리", new RobinHood()));
        tempPlayers.add(new Player("인내하는 카렌", new Saint()));
        tempPlayers.add(new Player("첫 인상만 보는 하츠네", new ScammerTester()));
        tempPlayers.add(new Player("지능형 배신자 사오리", new ShadowDefect()));
        tempPlayers.add(new Player("가차없는 코노하", new Shepherd()));
        tempPlayers.add(new Player("선입견에 가득 찬 니카", new StereoType()));
        tempPlayers.add(new Player("고뇌하는 협력가 마리", new SlowTitForTat()));
        tempPlayers.add(new Player("교활한 협력가 나츠키", new SuspiciousTitForTat()));
        tempPlayers.add(new Player("정석적인 인간 아야메", new TitForTat()));
        tempPlayers.add(new Player("실리주의자 사에", new TitForTatLastDefect()));
        tempPlayers.add(new Player("비열한 사기꾼 히나코", new Tranquilizer()));
        tempPlayers.add(new Player("멍텅구리 치히로", new Troller()));
        tempPlayers.add(new Player("두 배로 응징하는 미호", new TwoTitsForTat()));
        if (allPlayers.isEmpty()) {
            allPlayers.addAll(tempPlayers);
        }
        tempPlayers.removeAll(removeList);
        players = tempPlayers;
    }

    private static void displayResult(Map<Player, Map<Player, Integer>> allGameResults) {
        Map<Player, Double> avgGameScores = new HashMap<>();

        for (Player p1 : allGameResults.keySet()) {
            int playerTotalScore = 0;

            for (Player p2 : allGameResults.get(p1).keySet()) {
                int score1 = allGameResults.get(p1).getOrDefault(p2, 0);
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
    
    private static Map<Player, Map<Integer, Double>> calculateDeltaScores() {
        Map<Player, Map<Integer, Double>> deltaScores = new ConcurrentHashMap<>();
    
        for (Player player : players) {
            deltaScores.putIfAbsent(player, new ConcurrentHashMap<>());
            Map<Integer, Double> playerAverages = deltaScores.get(player);
    
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
                playerAverages.put(round, roundAverage / players.size());
            }
        }
    
        return deltaScores;
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
    
            double cumulativeSum = 0.0; // 누적 합 저장
    
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
                cumulativeSum += roundAverage; //  누적 점수 반영
                playerAverages.put(round, cumulativeSum / players.size());
            }
        }
    
        return averageRoundScores;
    }
    
    public static void runSimulationUntilOneLeft() {    
        // 원본 players 리스트를 복사해서 수정 가능한 리스트 생성
        List<Player> remove = new ArrayList<>();
    
        // ExecutorService나 멀티쓰레딩을 사용할 때, players 리스트는 항상 복사본을 사용하여 수정합니다.
        while (remove.size() < 57) {
            createPlayers(remove);
            runSimulation(TOTAL_GAMES);
            
            // 마지막 플레이어 찾기
            Player lastPlacePlayer = findLastPlacePlayer(remove);
    
            if (lastPlacePlayer != null) {
                System.out.println(players.size() + "위: " + lastPlacePlayer.getName());
                
                // 플레이어 제거 (안전하게)
                remove.add(lastPlacePlayer);
                players.remove(lastPlacePlayer);
            }
        }
    
        // 최종 우승자 출력
        if (players.size() == 1) {
            System.out.println("최종 우승자: " + players.get(0).getName());
        }
    
        // 마지막에 다시 unmodifiableList로 설정 (멀티쓰레딩 환경에서 안전하게 읽기 전용으로 설정)
        //players = Collections.unmodifiableList(modifiablePlayers);
    }    

    private static Map<Player, Double> calculateAverageScores(List<Player> remove) {
        Map<Player, Double> avgScores = new HashMap<>();
    
        for (Player player : allGameResults.keySet()) {
            int totalScore = 0;
    
            for (int score : allGameResults.get(player).values()) {
                totalScore += score;
            }
    
            avgScores.put(player, totalScore / (double) TOTAL_GAMES);
        }
        allGameResults = new ConcurrentHashMap<>();
        return avgScores;
    }
    
    private static Player findLastPlacePlayer(List<Player> remove) {
        Map<Player, Double> avgGameScores = calculateAverageScores(remove);
        gameScoreHistory.add(avgGameScores);
        // Find the minimum average score using a stream (no need for the variable to be mutable)
        double minAverageScore = avgGameScores.values().stream()
            .min(Double::compare)
            .orElse(Double.MAX_VALUE); // Use Double.MAX_VALUE if there's no player
    
        // Check for ties in the minimum score
        long numPlayersWithMinScore = avgGameScores.values().stream()
            .filter(score -> score == minAverageScore)
            .count();
    
        if (numPlayersWithMinScore > 1) {
            System.out.println("동률");
            return null; // No removal if there's a tie for the lowest score
        }
    
        // Find the player with the minimum average score
        for (Map.Entry<Player, Double> entry : avgGameScores.entrySet()) {
            if (entry.getValue() == minAverageScore) {
                return entry.getKey();
            }
        }
        
        return null;
    }
}