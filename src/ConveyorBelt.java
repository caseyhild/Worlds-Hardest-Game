import java.awt.*;
import java.util.List;

public class ConveyorBelt {
    public enum Orientation { LEFT, RIGHT, UP, DOWN }
    private final Orientation orientation;
    private final double x, y, w, h;
    private final double speed;
    private double offset;
    private double zigSize;
    private final Color color;

    public ConveyorBelt(Orientation orientation, double x, double y, double w, double h, double speed, Color color) {
        this.orientation = orientation;
        this.x = x;
        this.y = y;
        this.w = Math.max(1, w);
        this.h = Math.max(1, h);
        this.speed = speed;
        this.offset = 0;
        this.color = color;
    }

    public boolean intersectsPlayer(Player p) {
        double px = p.getX();
        double py = p.getY();
        int pSize = (int) Math.round(p.getSize());
        double tlx = px - pSize / 2.0;
        double tly = py - pSize / 2.0;
        Rectangle playerRect = new Rectangle((int)Math.round(tlx), (int)Math.round(tly), pSize, pSize);
        return playerRect.intersects(new Rectangle((int) Math.round(x + pSize/2.0), (int) Math.round(y + pSize/2.0), (int) Math.round(w - pSize), (int) Math.round(h - pSize)));
    }

    public void update(Player p, List<Wall> walls, int gridBoxSize) {
        zigSize = gridBoxSize/2.0;
        offset = (offset + speed) % gridBoxSize;
        applyToPlayer(p, walls);
    }

    private void applyToPlayer(Player p, List<Wall> walls) {
        if (!intersectsPlayer(p)) return;

        double dx, dy;
        dy = switch (orientation) {
            case LEFT -> {
                dx = -speed;
                yield 0;
            }
            case RIGHT -> {
                dx = speed;
                yield 0;
            }
            case UP -> {
                dx = 0;
                yield -speed;
            }
            case DOWN -> {
                dx = 0;
                yield speed;
            }
        };

        double newCenterX = p.getX() + dx;
        double newCenterY = p.getY() + dy;
        int pSize = (int) Math.round(p.getSize());
        Rectangle candidate = new Rectangle((int)Math.round(newCenterX - pSize/2.0),
                (int)Math.round(newCenterY - pSize/2.0),
                pSize, pSize);

        // check collision with walls: fallback axis attempts like Player.update
        for (Wall w : walls) {
            if (candidate.intersects(w.getBounds())) {
                Rectangle candX = new Rectangle((int)Math.round(p.getX() + dx - pSize/2.0),
                        (int)Math.round(p.getY() - pSize/2.0),
                        pSize, pSize);
                boolean collX = false;
                for (Wall ww : walls) { if (candX.intersects(ww.getBounds())) { collX = true; break; } }
                Rectangle candY = new Rectangle((int)Math.round(p.getX() - pSize/2.0),
                        (int)Math.round(p.getY() + dy - pSize/2.0),
                        pSize, pSize);
                boolean collY = false;
                for (Wall ww : walls) { if (candY.intersects(ww.getBounds())) { collY = true; break; } }

                if (!collX && collY) {
                    p.setPosition(p.getX() + dx, p.getY());
                } else if (collX && !collY) {
                    p.setPosition(p.getX(), p.getY() + dy);
                }
                return;
            }
        }

        // free move
        p.setPosition(newCenterX, newCenterY);
    }

    public void render(Graphics2D g) {
        // Save old clip
        Shape oldClip = g.getClip();

        // Set clip to the conveyor belt rectangle
        g.setClip(new Rectangle((int) Math.round(x), (int) Math.round(y), (int) Math.round(w), (int) Math.round(h)));

        // base fill
        g.setColor(color);
        g.fillRect((int) Math.round(x), (int) Math.round(y), (int) Math.round(w), (int) Math.round(h));

        g.setStroke(new BasicStroke(2));
        g.setColor(color.darker());

        int rows = (int) ((orientation == Orientation.LEFT || orientation == Orientation.RIGHT) ? w / zigSize / 2 : h / zigSize / 2) * 2;
        double movementShift = offset;
        switch (orientation) {
            case Orientation.LEFT, Orientation.UP:
                break;
            case Orientation.RIGHT, Orientation.DOWN:
                movementShift -= h/2;
        }

        if (orientation == Orientation.UP || orientation == Orientation.DOWN) {
            // vertical zigzag stripes
            for (int row = 0; row < rows; row++) {
                double startY = y + 2 * (row * zigSize) + zigSize / 2;
                double xLeft = x;
                int steps = (int) (w / zigSize) + 1;
                int[] xs = new int[2 * steps];
                int[] ys = new int[2 * steps];
                for (int i = 0; i < steps; i++) {
                    xs[i] = (int) Math.round(xLeft + i * zigSize + 1 - 2.0 * i/steps);
                    ys[i] = (int) Math.round(startY + (i % 2 == 0 ? -zigSize / 2 : zigSize / 2) + movementShift);
                }
                for (int i = 0; i < steps; i++) {
                    xs[2 * steps - 1 - i] = (int) Math.round(xLeft + i * zigSize + 1 - 2.0 * i/steps);
                    ys[2 * steps - 1 - i] = (int) Math.round(startY + (i % 2 == 0 ? -zigSize / 2 : zigSize / 2) + zigSize + movementShift);
                }
                if (orientation == Orientation.UP) {
                    for (int i = 0; i < 2 * steps; i++) {
                        ys[i] = (int) Math.round(2 * startY - ys[i] + zigSize);
                    }
                }
                g.fillPolygon(xs, ys, 2 * steps);
                g.drawPolygon(xs, ys, 2 * steps);
            }
        } else {
            // horizontal zigzag stripes
            for (int col = 0; col < rows; col++) {
                double startX = x + 2 * (col * zigSize) + zigSize / 2;
                double yTop = y;
                int steps = (int) (h / zigSize) + 1;
                int[] xs = new int[2 * steps];
                int[] ys = new int[2 * steps];
                for (int i = 0; i < steps; i++) {
                    xs[i] = (int) Math.round(startX + (i % 2 == 0 ? -zigSize / 2 : zigSize / 2) + movementShift);
                    ys[i] = (int) Math.round(yTop + i * zigSize + 1 - 2.0 * i/steps);
                }
                for (int i = 0; i < steps; i++) {
                    xs[2 * steps - 1 - i] = (int) Math.round(startX + (i % 2 == 0 ? -zigSize / 2 : zigSize / 2) + zigSize + movementShift);
                    ys[2 * steps - 1 - i] = (int) Math.round(yTop + i * zigSize + 1 - 2.0 * i/steps);
                }
                if (orientation == Orientation.LEFT) {
                    for (int i = 0; i < 2 * steps; i++) {
                        xs[i] = (int) Math.round(2 * startX - xs[i] + zigSize);
                    }
                }
                g.fillPolygon(xs, ys, 2 * steps);
                g.drawPolygon(xs, ys, 2 * steps);
            }
        }

        // Restore old clip after drawing
        g.setClip(oldClip);
    }
}
