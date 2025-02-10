import Strategy.*;
import java.util.*;
import java.util.concurrent.*;
import javax.swing.SwingUtilities;

public class PrisonersDilemmaSimulation {
    private static final int TOTAL_GAMES = 100; // 총 반복 횟수
    private static final int ROUNDS_PER_GAME = 500; // 한 게임당 라운드 수

    private static List<Player> players;
    private static final Map<Player, Map<Player, Integer>> allGameResults = new ConcurrentHashMap<>();
    @SuppressWarnings({ "unchecked", "rawtypes" })
    private static final List<Map<Integer, Map<Player, Integer>>> roundScoresList = Collections.synchronizedList(new ArrayList());

    public static void main(String[] args) {
        runSimulation(TOTAL_GAMES);
        Map<Player, Map<Integer, Double>> averageRoundScores = calculateAverageRoundScores();
        //  그래프 실행 (여기서 ScoreGraph 호출)
        SwingUtilities.invokeLater(() -> {
            @SuppressWarnings("unused")
            ScoreGraph graph = new ScoreGraph(averageRoundScores, ROUNDS_PER_GAME);
            //graph.displayGraph();
        });
    }

    @SuppressWarnings("CallToPrintStackTrace")
    public static void runSimulation(int numGames) {
        createPlayers();
        int availableCores = Runtime.getRuntime().availableProcessors();
        int threadPoolSize = Math.min(availableCores * 2, 100); // 최대 100개 제한
        System.out.println("너의 코어 수: " + availableCores);
        ExecutorService executor = Executors.newFixedThreadPool(threadPoolSize);
        List<Future<Game>> futures = new ArrayList<>();

        // 각 게임을 비동기적으로 실행
        for (int gameIndex = 0; gameIndex < numGames; gameIndex++) {
            final int currentGame = gameIndex;
            futures.add(executor.submit(() -> {
                System.out.println("Game: " + currentGame + " 실행 중...");
        
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

        executor.shutdown(); // 스레드 풀 종료

        //  최종 결과 출력
        displayResult(allGameResults);
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

    private static void createPlayers() {
        List<Player> tempPlayers = new ArrayList<>();

        tempPlayers.add(new Player("교대 배신자 이로하", new AlternateDefect())); //35위
        tempPlayers.add(new Player("순수한 협력가 카나에", new AlwaysCooperate())); //13위
        tempPlayers.add(new Player("냉혈한 배신자 아카네", new AlwaysDefect())); //28위
        tempPlayers.add(new Player("이분법자 츠바사", new BinaryThinking())); //26위
        tempPlayers.add(new Player("사과할줄 아는 루나", new ContriteTitForTat())); //15위
        tempPlayers.add(new Player("빚을 잊지 않는 아이코", new Debt())); //4위
        tempPlayers.add(new Player("정제된 광기 히카리", new DeceptiveAdaptation())); //30위
        tempPlayers.add(new Player("자애로운 협력가 리나", new DynamicTitForTat())); //17위
        tempPlayers.add(new Player("약육강식 노엘", new Flatterer())); //19위
        tempPlayers.add(new Player("용서하는 자 모모", new ForgivingTitForTat())); //12위
        tempPlayers.add(new Player("도박사 키리코", new Gambler())); //25위
        tempPlayers.add(new Player("감정적인 유리멘탈 치카코", new GlassMind())); //20위
        tempPlayers.add(new Player("신중한 보복가 미카", new Gradual())); //7위
        tempPlayers.add(new Player("즉흥적인 배짱이 하루카", new Grasshopper())); //29위
        tempPlayers.add(new Player("탐욕적인 기회주의자 레이나", new GreedyTitForTat())); //39위
        tempPlayers.add(new Player("복수의 사도 카오리", new GrimTrigger())); //8위
        tempPlayers.add(new Player("협력 유도자 스즈메", new GuidingCooperator())); //11위
        tempPlayers.add(new Player("철저한 분석가 나나세", new HandOfGod())); //40위 ㅠㅜ
        tempPlayers.add(new Player("뒷끝있는 협력자 키누에", new HardTitForTat())); //2위
        tempPlayers.add(new Player("반동분자 유리", new Merchant())); //21위
        tempPlayers.add(new Player("분탕종자 레이", new MutualDestruction())); //22위
        tempPlayers.add(new Player("전략적 판단가 니코", new OmegaTitForTat())); //32위
        tempPlayers.add(new Player("패턴 파괴자 우이", new PatternBreaker())); //23위
        tempPlayers.add(new Player("과거를 기억하는 미나", new Pavlov())); //16위
        tempPlayers.add(new Player("화해를 원하는 모네", new PeacefulTitForTat())); //6위
        tempPlayers.add(new Player("영악한 착취자 아스나", new Predator())); //27위
        tempPlayers.add(new Player("위대한 예언가 유키카", new Predictor())); //9위
        tempPlayers.add(new Player("확률적 협력가 유즈키", new ProbabilisticTitForTat())); //14위
        tempPlayers.add(new Player("미친 변덕쟁이 미유", new RandomStrategy())); //36위
        tempPlayers.add(new Player("붉은 혁명 루카리", new RobinHood())); //34위
        tempPlayers.add(new Player("인내하는 카렌", new Saint())); //10위
        tempPlayers.add(new Player("첫 인상만 보는 하츠네", new ScammerTester())); //24위
        tempPlayers.add(new Player("지능형 배신자 사오리", new ShadowDefect())); //18위
        tempPlayers.add(new Player("가차없는 코노하", new Shepherd())); //1위
        tempPlayers.add(new Player("고뇌하는 협력가 마리", new SlowTitForTat())); //33위
        tempPlayers.add(new Player("교활한 협력가 나츠키", new SuspiciousTitForTat())); //37위
        tempPlayers.add(new Player("정석적인 인간 아야메", new TitForTat())); //5위
        tempPlayers.add(new Player("비열한 사기꾼 히나코", new Tranquilizer())); //31위
        tempPlayers.add(new Player("멍텅구리 치히로", new Troller())); //38위
        tempPlayers.add(new Player("두 배로 응징하는 미호", new TwoTitsForTat())); //3위
        tempPlayers.add(new Player("나쁜 사람 미루", new BadPerson()));
        tempPlayers.add(new Player("좋은 사람 세리", new GoodPerson()));
        tempPlayers.add(new Player("성향 시험자 아리스", new OpponentTester()));
        players = Collections.unmodifiableList(tempPlayers);
    }

    private static void displayResult(Map<Player, Map<Player, Integer>> allGameResults) {
        Map<Player, Double> avgGameScores = new HashMap<>();

        //System.out.println("\n=== 상대전적 ===");
        for (Player p1 : allGameResults.keySet()) {
            int playerTotalScore = 0;

            //System.out.println(p1.getName());

            for (Player p2 : allGameResults.get(p1).keySet()) {
                int score1 = allGameResults.get(p1).getOrDefault(p2, 0);
                //int score2 = allGameResults.get(p2).getOrDefault(p1, 0);
                //int scoreDifference = score1 - score2;
                //System.out.printf("  vs %-10s: %.1f%n", p2.getName(), (double)(scoreDifference) / (double)TOTAL_GAMES);

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
    
        // //  디버깅 로그: 0번 플레이어 점수 확인
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