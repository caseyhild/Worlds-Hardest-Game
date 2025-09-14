import java.awt.*;
import java.util.List;

public class Wall {
    private final double x, y, width, height;
    private final Color fillColor, borderColor;

    public Wall(double x, double y, double w, double h, Color fillColor, Color borderColor) {
        this.x = x;
        this.y = y;
        this.width = w;
        this.height = h;
        this.fillColor = fillColor;
        this.borderColor = borderColor;
    }

    public void render(Graphics2D g, List<Wall> allWalls) {
        renderWalls(g);
        renderBorders(g, allWalls);
    }

    public void renderWalls(Graphics2D g) {
        g.setColor(fillColor);
        g.fillRect((int) Math.round(x), (int) Math.round(y), (int) width, (int) height);
    }
    public void renderBorders(Graphics2D g, List<Wall> allWalls) {
        // Draw border only if no adjacent wall
        boolean top = true, bottom = true, left = true, right = true;

        for (Wall other: allWalls) {
            if (other == this) continue;

            // Check adjacency
            if (other.y + other.height == y && other.x < x + width && other.x + other.width > x) top = false;
            if (y + height == other.y && other.x < x + width && other.x + other.width > x) bottom = false;
            if (other.x + other.width == x && other.y < y + height && other.y + other.height > y) left = false;
            if (x + width == other.x && other.y < y + height && other.y + other.height > y) right = false;
        }

        // Draw only exposed sides
        g.setColor(borderColor);
        g.setStroke(new BasicStroke(3.0f));
        if (top) g.drawLine((int) Math.round(x), (int) Math.round(y), (int) Math.round(x + width), (int) Math.round(y));
        if (bottom) g.drawLine((int) Math.round(x), (int) Math.round(y + height), (int) Math.round(x + width), (int) Math.round(y + height));
        if (left) g.drawLine((int) Math.round(x), (int) Math.round(y), (int) Math.round(x), (int) Math.round(y + height));
        if (right) g.drawLine((int) Math.round(x + width), (int) Math.round(y), (int) Math.round(x + width), (int) Math.round(y + height));
    }
    public Rectangle getBounds() {
        return new Rectangle((int) Math.round(x), (int) Math.round(y), (int) Math.round(width), (int) Math.round(height));
    }
}
