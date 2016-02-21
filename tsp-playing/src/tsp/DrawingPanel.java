package tsp;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Stroke;
import java.util.Map;
import java.util.Random;
import javax.swing.JPanel;

public class DrawingPanel extends JPanel {
    private final TSP tsp;
    private final static float dash1[] = {10.0f};
    private final static BasicStroke dashed = new BasicStroke(1.0f, BasicStroke.CAP_BUTT,
                        BasicStroke.JOIN_MITER, 10.0f, dash1, 0.0f);

    public DrawingPanel(TSP tsp) {
        super.setBackground(Color.WHITE);
        this.tsp = tsp;
    }

    private Color[] prepareColors(Point[] points) {
        Color[] colors = new Color[points.length];
        Random r = new Random(System.currentTimeMillis());

        for (int i = 0; i < colors.length; i++) {
            colors[i] = new Color(r.nextFloat(), r.nextFloat(), r.nextFloat());
        }
        return colors;
    }

    private void drawSolution(Graphics2D graphics) {
        Point[] solution = tsp.solve();
        graphics.setColor(Color.BLUE);
        for (int i = 1; i < solution.length; i++) {
            graphics.drawLine(solution[i - 1].x, solution[i - 1].y, solution[i].x, solution[i].y);
        }
        graphics.drawLine(solution[0].x, solution[0].y, solution[solution.length - 1].x, solution[solution.length - 1].y);

        graphics.setColor(Color.BLACK);
        graphics.drawString("length=" + tsp.getPathLength(solution), 10, 12);
    }

    private Double findMax(Double[] distances) {
        Double max = new Double(0);
        for (Double distance : distances) {
            if (distance > max) {
                max = distance;
            }
        }
        return max;
    }

    private void drawDistancesMap(Graphics2D graphics, Color[] colors) {
        Map<Double, Point> mapSolution = tsp.solveWithDistances();
        Double[] distances = mapSolution.keySet().toArray(new Double[0]);
        int height = getPreferredSize().height;

        graphics.setColor(Color.BLACK);
        graphics.drawLine(0, height - 9, findMax(distances).intValue(), height - 9);

        for (int i = 0; i < distances.length; i++) {
            graphics.setColor(colors[i]);
            graphics.fillRect(distances[i].intValue(), height - 12, 6, 6);
        }
    }

    private void drawPoints(Graphics2D graphics, Point[] points, Color[] colors) {
        for (int i = 0; i < points.length; i++) {
            Point point = points[i];
            String msg = "[" + point.x + "," + point.y + "]";
            graphics.setColor(Color.WHITE);
            graphics.fillRect(point.x, point.y+5, graphics.getFontMetrics().stringWidth(msg), 12);
            graphics.setColor(colors[i]);
            graphics.fillOval(point.x-3, point.y-3, 6, 6);
            graphics.drawString(msg , point.x, point.y + 15);
        }
    }

    @Override
    public void paintComponent(Graphics gOld) {
        Graphics2D g = (Graphics2D)gOld;
        Point[] points = tsp.getPoints();
        Color[] colors = prepareColors(points);

        Point cog = tsp.getCenterOfGravity();
        if (cog != null) {
            Stroke oldStroke = g.getStroke();
            g.setStroke(dashed);
            for (int i = 0; i < points.length; i++) {
                Point point = points[i];
                g.setColor(colors[i]);
                g.drawLine(cog.x, cog.y, point.x, point.y);
            }
            g.setStroke(oldStroke);

            g.setColor(Color.red);
            g.fillOval(cog.x - 3, cog.y - 3, 6, 6);

            drawSolution(g);
            drawDistancesMap(g, colors);
        }
        drawPoints(g, points, colors);
    }

}
