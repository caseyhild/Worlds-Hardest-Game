import java.awt.*;
import java.awt.geom.*;

public class Key {
    private final double x;
    private final double y;
    private final double size;
    private boolean collected;
    private final Color color;

    public Key(double x, double y, double size, Color color) {
        this.x = x;
        this.y = y;
        this.size = size;
        this.collected = false;
        this.color = color;
    }

    public boolean isCollected() { return collected; }
    public void setCollected(boolean c) { collected = c; }
    public void collect() { collected = true; }

    public void update(Player player) {
        if (!collected && player.getBounds().intersects(getBounds())) {
            collect();
        }
    }

    public void render(Graphics2D g) {
        // Draw if not already collected
        if (collected) return;

        Color stroke = color.darker().darker();

        // Circle fill
        double r = size / 2.0;
        Ellipse2D.Double circle = new Ellipse2D.Double(x - r, y, r, r);
        g.setColor(color);
        g.fill(circle);

        // Tooth fill
        GeneralPath tooth = getKeyToothPath(r);
        g.fill(tooth);

        // Tooth outline
        g.setStroke(new BasicStroke((float)(size * 0.125), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g.setColor(stroke);
        g.draw(tooth);

        // Circle outline
        Arc2D.Double arc = new Arc2D.Double(
                x - r, y, r, r,
                50, 290, Arc2D.OPEN
        );
        g.setStroke(new BasicStroke((float)(size * 0.1)));
        g.draw(arc);

        // Inner dot
        double dotSize = size * 0.2;
        Ellipse2D.Double dot = new Ellipse2D.Double(x - 0.35 * size, y + 0.15 * size, dotSize, dotSize);
        g.fill(dot);
    }

    private GeneralPath getKeyToothPath(double r) {
        GeneralPath tooth = new GeneralPath();
        tooth.moveTo(x, y + r / 2);
        tooth.lineTo(x + r, y - r / 2);
        tooth.lineTo(x + r / 2, y - r);
        tooth.lineTo(x + 0.24 * size, y - 0.37 * size);
        tooth.lineTo(x + 0.15 * size, y - 0.38 * size);
        tooth.lineTo(x + 0.245 * size, y - 0.245 * size);
        tooth.lineTo(x + 0.155 * size, y - 0.155 * size);
        tooth.lineTo(x + 0.03 * size, y - 0.28 * size);
        tooth.lineTo(x - 0.1 * size, y - 0.15 * size);
        tooth.lineTo(x + 0.025 * size, y - 0.025 * size);
        double n = 0.125 * size * Math.sqrt(2.0);
        tooth.lineTo(x + n - 0.25 * size, y - n + 0.25 * size);
        return tooth;
    }

    public Rectangle getBounds() {
        double width = 7 * size / 6;
        return new Rectangle((int) Math.round(x - width/2), (int) Math.round(y - width/2), (int) Math.round(width), (int) Math.round(width));
    }
}
