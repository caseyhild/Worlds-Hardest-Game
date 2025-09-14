import java.awt.*;
import java.util.List;

public class Checkpoint {
    private final Rectangle bounds;
    private long flashStart = 0;
    private static final int FLASH_DURATION = 200; // in ms
    public boolean playerInsideLastFrame = false;
    private boolean[] savedKeys;
    private final Color color;

    public Checkpoint(double centerX, double centerY, double sizeX, double sizeY, Color color) {
        this.bounds = new Rectangle((int) Math.round(centerX - sizeX/2), (int) Math.round(centerY - sizeY/2), (int) Math.round(sizeX), (int) Math.round(sizeY));
        this.color = color;
    }

    public void update(Player player, List<Key> keys) {
        boolean playerInsideNow = bounds.intersects(player.getBounds());

        if (playerInsideNow && !playerInsideLastFrame) {
            activate(player, keys);
        }

        playerInsideLastFrame = playerInsideNow;
    }

    private void activate(Player player, List<Key> keys) {
        flashStart = System.currentTimeMillis();
        player.setRespawn(bounds.x + bounds.width / 2.0, bounds.y + bounds.height / 2.0);

        // snapshot collected states
        savedKeys = new boolean[keys.size()];
        for (int i = 0; i < keys.size(); i++) {
            savedKeys[i] = keys.get(i).isCollected();
        }
    }

    public void restore(List<Key> keys) {
        // Restore snapshot when player respawns
        if (savedKeys != null) {
            for (int i = 0; i < keys.size(); i++) {
                keys.get(i).setCollected(savedKeys[i]);
            }
        }
    }

    public double getX() {
        return bounds.getCenterX();
    }

    public double getY() {
        return bounds.getCenterY();
    }

    public void render(Graphics2D g) {
        Color base = color;
        Color flash = color.darker();

        // Set color for flash when reaching checkpoint
        Color current = base;
        long elapsed = System.currentTimeMillis() - flashStart;
        if (elapsed < FLASH_DURATION) {
            float t = elapsed / (float) FLASH_DURATION;
            int r = Math.round(flash.getRed() * (1 - t) + base.getRed() * t);
            int gCol = Math.round(flash.getGreen() * (1 - t) + base.getGreen() * t);
            int b = Math.round(flash.getBlue() * (1 - t) + base.getBlue() * t);
            current = new Color(r, gCol, b);
        }

        // Draw checkpoint
        g.setColor(current);
        g.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);
    }
}
