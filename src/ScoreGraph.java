import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.awt.*;
import java.util.Map;
import java.util.TreeSet;
import Strategy.*;

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
}