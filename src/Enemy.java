import java.awt.*;
import java.util.ArrayList;

public class Enemy {
    private final MotionType motion;
    // position / velocity / speed
    public double x, y, vx, vy;
    private double speed;
    // set points along a movement path
    private ArrayList<Double> pathX;
    private ArrayList<Double> pathY;
    // orbit params
    private double cx, cy, orbitRadius, angle, angularSpeed;
    private final double size;
    private final Color color;

    public enum MotionType { LINEAR, ORBIT, STATIC }

    // linear constructor
    public Enemy(MotionType motion, double x, double y, double speed, ArrayList<Double> pathX, ArrayList<Double> pathY, double size, Color color) {
        if (motion != MotionType.LINEAR)
            throw new IllegalArgumentException("Motion type must be LINEAR");
        if (pathX.size() != pathY.size())
            throw new IllegalArgumentException("pathX and pathY much be of same length");
        if (pathX.size() < 2)
            throw new IllegalArgumentException("Path must contain at least 2 points");
        this.motion = motion;
        this.x = x;
        this.y = y;
        this.speed = speed;
        double dx = pathX.get(1) - pathX.get(0);
        double dy = pathY.get(1) - pathY.get(0);
        double len = Math.sqrt(dx * dx + dy * dy);
        this.vx = speed * dx/len;
        this.vy = speed * dy/len;
        this.pathX = pathX;
        this.pathY = pathY;
        this.size = size;
        this.color = color;
    }

    // orbit constructor
    public Enemy(MotionType motion, double centerX, double centerY, double orbitRadius, double startAngle, double angularSpeed, double size, Color color) {
        if (motion != MotionType.ORBIT)
            throw new IllegalArgumentException("Motion type must be ORBIT");
        this.motion = motion;
        this.cx = centerX;
        this.cy = centerY;
        this.orbitRadius = orbitRadius;
        this.angle = startAngle;
        this.angularSpeed = angularSpeed;
        this.x = cx + Math.cos(angle) * orbitRadius;
        this.y = cy + Math.sin(angle) * orbitRadius;
        this.size = size;
        this.color = color;
    }

    // static constructor
    public Enemy(MotionType motion, double x, double y, double size, Color color) {
        if (motion != MotionType.STATIC)
            throw new IllegalArgumentException("Motion type must be STATIC");
        this.motion = motion;
        this.x = x;
        this.y = y;
        this.size = size;
        this.color = color;
    }

    public void update(int gridBoxSize) {
        if (motion == MotionType.LINEAR) {
            x += vx;
            y += vy;

            // change velocities at points on path
            double threshold = gridBoxSize/10.0;
            for (int i = 0; i < pathX.size(); i++) {
                double px = pathX.get(i);
                double px1 = pathX.get((i + 1) % pathX.size());
                double py = pathY.get(i);
                double py1 = pathY.get((i + 1) % pathY.size());
                if (Math.abs(x - px) < threshold && Math.abs(y - py) < threshold) {
                    double dx = px1 - px;
                    double dy = py1 - py;
                    double len = Math.sqrt(dx * dx + dy * dy);
                    vx = speed * dx/len;
                    vy = speed * dy/len;
                    break;
                }
            }
        } else if (motion == MotionType.ORBIT) {
            angle += angularSpeed;
            x = cx + Math.cos(angle) * orbitRadius;
            y = cy + Math.sin(angle) * orbitRadius;
        }
    }

    public void render(Graphics2D g) {
        // inner fill
        g.setColor(color);
        g.fillOval((int) Math.round(x - size/2 + size * 0.1), (int) Math.round(y - size/2 + size * 0.1), (int) Math.round(size * 0.8), (int) Math.round(size * 0.8));
        // outer ring
        g.setStroke(new BasicStroke(3.0f));
        g.setColor(color.darker());
        g.drawOval((int) Math.round(x - size/2 + size * 0.1), (int) Math.round(y - size/2 + size * 0.1), (int) Math.round(size * 0.8), (int) Math.round(size * 0.8));
    }

    public boolean collidesWith(Player player) {
        double dist = Math.hypot(player.getX() - x, player.getY() - y);
        return dist <= (player.getSize() + size)/2;
    }

    public double getSize() { return size; }
    public double getX() { return x; }
    public double getY() { return y; }
}
