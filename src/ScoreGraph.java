import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.StandardXYToolTipGenerator;
import org.jfree.chart.labels.XYToolTipGenerator;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;

import javax.swing.*;
import java.awt.*;
import java.util.Map;
import java.util.Optional;
import java.util.TreeSet;
import java.util.ArrayList;
import java.util.List;
import Strategy.*;
import java.util.Set;
import java.util.stream.Collectors;

public class ScoreGraph {
    private final Map<Player, Map<Integer, Double>> averageRoundScores;
    private final int totalRounds;

    public ScoreGraph(Map<Player, Map<Integer, Double>> averageRoundScores, int totalRounds) {
        this.averageRoundScores = averageRoundScores;
        this.totalRounds = totalRounds;
    }

    public void displayGraph() {
        XYSeriesCollection dataset = new XYSeriesCollection();

        for (Player player : averageRoundScores.keySet()) {
            XYSeries series = new XYSeries(player.getName());

            for (int round = 1; round <= totalRounds; round++) {
                double avgScore = averageRoundScores.get(player).getOrDefault(round, 0.0);
                series.add(round, avgScore);  // Y축: 평균 점수 그대로 사용 (누적 X)
            }
            dataset.addSeries(series);
        }

        JFreeChart chart = ChartFactory.createXYLineChart(
                "Average Score per Round",
                "Round",
                "Average Score",
                dataset,
                PlotOrientation.VERTICAL,
                false, true, false
        );

        // 한글 깨짐 방지를 위한 폰트 설정
        Font font = new Font("Malgun Gothic", Font.PLAIN, 15);
        chart.getTitle().setFont(font);
        //chart.getLegend().setItemFont(font);

        JFrame frame = new JFrame("Game Score Graph");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // 전체 화면 크기 가져오기
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setSize(screenSize.width, screenSize.height);

        frame.add(new ChartPanel(chart));
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);  // 최대화 상태로 시작
        frame.setVisible(true);
    }

    public void displayGraphPlayer(String playerName) {
        XYSeriesCollection dataset = new XYSeriesCollection();

        // 특정 플레이어 찾기
        Optional<Player> targetPlayer = averageRoundScores.keySet().stream()
                .filter(player -> player.getName().equals(playerName))
                .findFirst();

        if (targetPlayer.isPresent()) {
            Player player = targetPlayer.get();
            XYSeries series = new XYSeries(player.getName());

            for (int round = 1; round <= totalRounds; round++) {
                double avgScore = averageRoundScores.get(player).getOrDefault(round, 0.0);
                series.add(round, avgScore);
            }
            dataset.addSeries(series);
        } else {
            System.out.println("플레이어 " + playerName + "을(를) 찾을 수 없습니다.");
            return;
        }

        JFreeChart chart = ChartFactory.createXYLineChart(
                "Average Score per Round - " + playerName,
                "Round",
                "Average Score",
                dataset,
                PlotOrientation.VERTICAL,
                false, true, false
        );

        Font font = new Font("Malgun Gothic", Font.PLAIN, 15);
        chart.getTitle().setFont(font);

        JFrame frame = new JFrame("Player Score Graph: " + playerName);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setSize(screenSize.width, screenSize.height);

        frame.add(new ChartPanel(chart));
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setVisible(true);
    }
    
    public void displayGraphWithBestFit(List<String> playerNames) {
        XYSeriesCollection dataset = new XYSeriesCollection();
        Set<String> selectedPlayers = playerNames == null || playerNames.isEmpty()
                ? averageRoundScores.keySet().stream().map(Player::getName).collect(Collectors.toSet())
                : Set.copyOf(playerNames);

        for (Player player : averageRoundScores.keySet()) {
            if (!selectedPlayers.contains(player.getName())) {
                continue; // 플레이어 이름 목록에 없는 경우 건너뜀
            }

            // 곡선 근사 데이터 시리즈 (이동 평균 적용)
            XYSeries smoothSeries = new XYSeries(player.getName() + " - Smoothed");

            List<Double> scores = new ArrayList<>();
            for (int round = 1; round <= totalRounds; round++) {
                double avgScore = averageRoundScores.get(player).getOrDefault(round, 0.0);
                scores.add(avgScore);
            }

            // 이동 평균 기반 곡선 근사 적용
            int windowSize = 5; // 최근 5개의 데이터 평균을 사용
            for (int i = 0; i < scores.size(); i++) {
                double sum = 0;
                int count = 0;
                for (int j = Math.max(0, i - windowSize + 1); j <= i; j++) {
                    sum += scores.get(j);
                    count++;
                }
                smoothSeries.add(i + 1, sum / count); // 부드러운 곡선 데이터 추가
            }
            dataset.addSeries(smoothSeries); // 곡선 근사 데이터 추가
        }

        JFreeChart chart = ChartFactory.createXYLineChart(
                "Smoothed Average Score per Round",
                "Round",
                "Average Score",
                dataset,
                PlotOrientation.VERTICAL,
                false,  // 범례 (Legend) 제거
                true,   // 툴팁 활성화
                false   // URL 비활성화
        );

        XYPlot plot = chart.getXYPlot();
        NumberAxis xAxis = (NumberAxis)plot.getDomainAxis();
        xAxis.setRange(0, 100);

        // 부드러운 곡선을 그리기 위한 렌더러 설정
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        for (int i = 0; i < dataset.getSeriesCount(); i++) {
            renderer.setSeriesShapesVisible(i, false);  // 포인트 마커(세모, 네모 등) 제거
            renderer.setSeriesStroke(i, new BasicStroke(1.0f)); // 곡선은 더 두껍게
        }
        renderer.setBaseToolTipGenerator(new XYToolTipGenerator() {
            @Override
            public String generateToolTip(XYDataset dataset, int series, int item) {
                String playerName = dataset.getSeriesKey(series).toString(); // 🔥 시리즈 이름(플레이어 이름) 가져오기
                double x = dataset.getXValue(series, item);
                double y = dataset.getYValue(series, item);
                return String.format("%s: (Round: %.0f, Score: %.2f)", playerName, x, y);
            }
        });
        chart.getXYPlot().setRenderer(renderer);
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setMouseWheelEnabled(true); // 줌 기능 활성화
        chartPanel.setDisplayToolTips(true); // 🔥 툴팁 활성화

        // 한글 깨짐 방지를 위한 폰트 설정
        Font font = new Font("Malgun Gothic", Font.PLAIN, 15);
        chart.getTitle().setFont(font);

        JFrame frame = new JFrame("Smoothed Score Graph");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1200, 800);
        frame.add(chartPanel);
        frame.setVisible(true);
    }


}