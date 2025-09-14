import java.awt.*;
import java.util.List;

public class ColorBarrier {
    private final int x;
    private final int y;
    private final int width;
    private final int height;
    private Color color;
    private boolean activePass; // true if barrier is in passable state

    public ColorBarrier(int x, int y, int w, int h, Color color) {
        this.x = x; this.y = y; this.width = w; this.height = h;
        this.color = color;
        this.activePass = false;
    }

    public boolean canPass(Player player) {
        // allow pass if player color matches barrier color
        activePass = player.getColor().equals(color);
        return activePass;
    }

    public Wall getCollisionWall() {
        return new Wall(x, y, width, height, color, color.darker());
    }

    public void render(Graphics2D g, java.util.List<ColorBarrier> allColorBarriers) {
        renderColorBarriers(g);
        renderBorders(g, allColorBarriers);
    }

    public void renderColorBarriers(Graphics2D g) {
        g.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 80));
        g.fillRect(x, y, width, height);
    }
    public void renderBorders(Graphics2D g, List<ColorBarrier> allColorBarriers) {
        // Draw border only if no adjacent wall
        boolean top = true, bottom = true, left = true, right = true;

        for (ColorBarrier other: allColorBarriers) {
            if (other == this) continue;

            // Check adjacency
            if (other.y + other.height == y && other.x < x + width && other.x + other.width > x) top = false;
            if (y + height == other.y && other.x < x + width && other.x + other.width > x) bottom = false;
            if (other.x + other.width == x && other.y < y + height && other.y + other.height > y) left = false;
            if (x + width == other.x && other.y < y + height && other.y + other.height > y) right = false;
        }

        // Draw only exposed sides
        g.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 80).darker());
        g.setStroke(new BasicStroke(6.0f));
        if (top) g.drawLine(x, y, x + width, y);
        if (bottom) g.drawLine(x, y + height, x + width, y + height);
        if (left) g.drawLine(x, y, x, y + height);
        if (right) g.drawLine(x + width, y, x + width, y + height);
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
