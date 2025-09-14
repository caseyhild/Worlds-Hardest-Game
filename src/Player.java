import java.awt.*;
import java.util.List;

public class Player {
    private final WorldsHardestGame game;
    private double x, y;
    private final double size;
    private final double speed;
    private final Rectangle bounds;
    private Color color;
    private int alpha = 255;
    private boolean dying = false;
    private boolean justRespawned = false;
    private boolean isRainbowColor;
    private double respawnX, respawnY;

    public Player(WorldsHardestGame game, double startX, double startY, double size, double speed, Color color) {
        this.game = game;
        this.x = startX;
        this.y = startY;
        this.size = size;
        this.speed = speed;
        this.bounds = new Rectangle((int) Math.round(startX - size/2), (int) Math.round(startY - size/2), (int) Math.round(size), (int) Math.round(size));
        this.color = color;

        // initial respawn at start position
        this.respawnX = startX;
        this.respawnY = startY;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color c) {
        this.color = c;
        this.isRainbowColor = false;
    }

    public boolean isRainbowColor() {
        return this.isRainbowColor;
    }

    public void setRainbowColor(Color c) {
        this.color = c;
        this.isRainbowColor = true;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getSize() {
        return size;
    }

    public Rectangle getBounds() {
        return dying ? new Rectangle(game.getScreenWidth(), game.getScreenHeight(), 0, 0) : bounds;
    }

    public void setPosition(double x, double y) {
        this.x = x;
        this.y = y;
        this.bounds.x = (int) Math.round(x - size/2);
        this.bounds.y = (int) Math.round(y - size/2);
    }

    public void setRespawn(double x, double y) {
        this.respawnX = x;
        this.respawnY = y;
    }

    public void respawn() {
        setPosition(respawnX, respawnY);
        alpha = 255;
        dying = false;
    }

    public boolean hasJustRespawned() {
        if (justRespawned) {
            justRespawned = false;
            return true;
        }
        return false;
    }

    public void die() {
        if (!dying) {
            dying = true;
            alpha = 255;
        }
    }

    public boolean isDying() {
        return dying;
    }

    public void update(List<Wall> walls) {
        if (dying) {
            int fadeSpeed = 10;
            alpha -= fadeSpeed;
            if (alpha <= 0) {
                respawn();
                justRespawned = true;
            }
            return;
        }

        double dx = 0;
        double dy = 0;
        if(!(game.getScene() instanceof WalkthroughScene)) {
            if (game.keys[java.awt.event.KeyEvent.VK_LEFT])
                dx -= speed;
            if (game.keys[java.awt.event.KeyEvent.VK_RIGHT])
                dx += speed;
            if (game.keys[java.awt.event.KeyEvent.VK_UP])
                dy -= speed;
            if (game.keys[java.awt.event.KeyEvent.VK_DOWN])
                dy += speed;
        }

        // normalize diagonal
        if (dx != 0 && dy != 0) {
            dx = Math.round(dx * 0.7071f);
            dy = Math.round(dy * 0.7071f);
        }

        // attempt X
        if (dx != 0) {
            Rectangle tryX = new Rectangle(bounds);
            tryX.x += (int) dx;
            if (collidesAny(tryX, walls)) {
                bounds.x += (int) dx;
                x += dx;
            } else {
                int step = dx > 0 ? 1 : -1;
                for (int i = 0; i < Math.abs(dx); i++) {
                    Rectangle s = new Rectangle(bounds);
                    s.x += step;
                    if (collidesAny(s, walls)) {
                        bounds.x += step;
                        x += step;
                    }
                    else break;
                }
            }
        }

        // attempt Y
        if (dy != 0) {
            Rectangle tryY = new Rectangle(bounds);
            tryY.y += (int) dy;
            if (collidesAny(tryY, walls)) {
                bounds.y += (int) dy;
                y += dy;
            } else {
                int step = dy > 0 ? 1 : -1;
                for (int i = 0; i < Math.abs(dy); i++) {
                    Rectangle s = new Rectangle(bounds);
                    s.y += step;
                    if (collidesAny(s, walls)) {
                        bounds.y += step;
                        y += step;
                    }
                    else break;
                }
            }
        }
    }

    private boolean collidesAny(Rectangle r, List<Wall> walls) {
        for (Wall w : walls)
            if (r.intersects(w.getBounds()))
                return false;
        return true;
    }

    public void render(Graphics2D g) {
        // slightly bigger border/stroke so player stands out
        g.setColor(new Color(color.darker().darker().getRed(), color.darker().darker().getGreen(), color.darker().darker().getBlue(), alpha));
        g.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);

        // body
        g.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha));
        int insetX = (int) Math.round(0.125 * bounds.width);
        int insetY = (int) Math.round(0.125 * bounds.height);
        g.fillRect(bounds.x + insetX, bounds.y + insetY, bounds.width - 2 * insetX, bounds.height - 2 * insetY);
    }
}