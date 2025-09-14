import java.awt.*;
import java.awt.geom.*;

public class PaintSplotch {
    private final double x;
    private final double y;
    private final double size;
    private Color color;
    private boolean collected;

    public PaintSplotch(double x, double y, double size, Color color) {
        this.x = x;
        this.y = y;
        this.color = color;
        this.size = size;
        this.collected = false;
    }

    public void collect() {
        collected = true;
    }

    public boolean isCollected() {
        return collected;
    }

    public void update(Player player) {
        if (!collected && player.getBounds().intersects(getBounds())) {
            collect();
            player.setColor(color);
        }
    }

    public void render(Graphics2D g) {
        // Draw paint splotch if not already collected
        if (collected)
            return;

        AffineTransform oldTx = g.getTransform();
        g.translate(x, y);

        // Original points scaled relative to size / 40
        double scale = size / 40.0;
        double[][] pts = new double[][] {
                {7.0, 7.0}, {7.0, 13.0}, {3.0, 14.5}, {-1.0, 9.0}, {-7.0, 11.0},
                {-10.5, 11.5}, {-13.5, 10.0}, {-14.0, 5.5}, {-8.5, 2.0}, {-8.5, -1.0},
                {-13.5, -6.0}, {-13.0, -11.5}, {-9.5, -13.0}, {-4.0, -9.5}, {2.0, -14.0},
                {6.5, -14.0}, {7.5, -9.5}, {5.0, -1.5}, {14.5, 2.5}, {13.5, 7.0},
                {7.0, 7.0}, {7.0, 13.0}, {3.0, 14.5}
        };

        for (int i = 0; i < pts.length; i++) {
            pts[i][0] *= scale;
            pts[i][1] *= scale;
        }

        // Draw the paint splotch
        GeneralPath path = getPaintSplotchPath(pts);

        g.setColor(color);
        g.fill(path);

        Color strokeColor = new Color(
                Math.max(color.getRed() / 2, 0),
                Math.max(color.getGreen() / 2, 0),
                Math.max(color.getBlue() / 2, 0)
        );
        g.setStroke(new BasicStroke((float)(3.0 * scale), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g.setColor(strokeColor);
        g.draw(path);

        // Small ellipse scaled
        double ellW = 6.5 * scale;
        double ellH = 6.5 * scale;
        double ellCx = (12.5 - 6.5/2.0) * scale;
        double ellCy = (-5.0 - 6.5/2.0) * scale;
        Ellipse2D.Double bubble = new Ellipse2D.Double(ellCx, ellCy, ellW, ellH);
        g.setColor(color);
        g.fill(bubble);
        g.setColor(strokeColor);
        g.draw(bubble);

        g.setTransform(oldTx);
    }

    private static GeneralPath getPaintSplotchPath(double[][] pts) {
        final int samplesPerSeg = 18;
        GeneralPath path = new GeneralPath();
        boolean started = false;

        for (int i = 0; i <= pts.length - 4; i++) {
            double[] p0 = pts[i];
            double[] p1 = pts[i + 1];
            double[] p2 = pts[i + 2];
            double[] p3 = pts[i + 3];

            for (int s = 0; s <= samplesPerSeg; s++) {
                double t = (double) s / samplesPerSeg;
                double t2 = t * t;
                double t3 = t2 * t;

                double xPos = 0.5 * (
                        (2.0 * p1[0]) +
                                (-p0[0] + p2[0]) * t +
                                (2.0*p0[0] - 5.0*p1[0] + 4.0*p2[0] - p3[0]) * t2 +
                                (-p0[0] + 3.0*p1[0] - 3.0*p2[0] + p3[0]) * t3
                );

                double yPos = 0.5 * (
                        (2.0 * p1[1]) +
                                (-p0[1] + p2[1]) * t +
                                (2.0*p0[1] - 5.0*p1[1] + 4.0*p2[1] - p3[1]) * t2 +
                                (-p0[1] + 3.0*p1[1] - 3.0*p2[1] + p3[1]) * t3
                );

                if (!started) {
                    path.moveTo(xPos, yPos);
                    started = true;
                } else {
                    path.lineTo(xPos, yPos);
                }
            }
        }
        path.closePath();
        return path;
    }

    public Rectangle getBounds() {
        double width = 3 * size / 4;
        return new Rectangle((int) Math.round(x - width/2), (int) Math.round(y - width/2), (int) Math.round(width), (int) Math.round(width));
    }
    
    public void setColor(Color color) {
        this.color = color;
    }
}
