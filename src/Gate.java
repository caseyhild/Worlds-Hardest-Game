import java.awt.*;

public class Gate {
    public enum Orientation { HORIZONTAL, VERTICAL }

    private final double x, y;        // top-left X, Y in pixels
    private final double thickness;   // thickness in pixels (height for horizontal, width for vertical)
    private final double baseLength;  // closed length in pixels (width for horizontal, height for vertical)
    private final Orientation orientation;
    private final Key key;            // associated key

    private double length;            // current length in pixels — retracts toward 0 when opening
    private final double openSpeed;   // pixels per update to retract when opening

    private final Color fillColor;
    private final Color borderColor;

    public Gate(double x, double y, double baseLength, double thickness, double openSpeed, Orientation orientation, Key key, Color fillColor, Color borderColor) {
        this.x = x;
        this.y = y;
        this.baseLength = Math.max(0, baseLength);
        this.thickness = Math.max(1, thickness);
        this.orientation = orientation;
        this.key = key;
        this.length = this.baseLength;
        this.openSpeed = Math.max(0.0, openSpeed);
        this.fillColor = fillColor;
        this.borderColor = borderColor;
    }

    public void update() {
        // Open if key is collected
        if (key != null && key.isCollected()) {
            if (length > 0.0) {
                length -= openSpeed;
                if (length < 0.0)
                    length = 0.0;
            }
        }
        else {
            length = baseLength;
        }
    }

    public Wall getCollisionWall() {
        // Only produce a collision wall when gate is closed or while closing
        if (orientation == Orientation.HORIZONTAL) {
            int pxLength = (int) Math.round(length);
            if (pxLength == 0) return null;
            return new Wall(x, y, pxLength, thickness, fillColor, borderColor);
        } else {
            int pxLength = (int) Math.round(length);
            if (pxLength == 0) return null;
            return new Wall(x, y, thickness, pxLength, fillColor, borderColor);
        }
    }

    public void render(Graphics2D g) {
        // Draw gate as a rectangle with the correct orientation
        int strokeWidth = 2;
        if (orientation == Orientation.HORIZONTAL) {
            int pxLength = (int) Math.round(length);
            if (pxLength <= 0) return;

            g.setColor(fillColor);
            g.fillRect((int) Math.round(x), (int) Math.round(y), pxLength - strokeWidth, (int) Math.round(thickness));

            Stroke old = g.getStroke();
            g.setStroke(new BasicStroke(strokeWidth));
            g.setColor(borderColor);
            g.drawRect((int) Math.round(x), (int) Math.round(y), pxLength - strokeWidth, (int) Math.round(thickness));
            g.setStroke(old);
        } else {
            int pxLength = (int) Math.round(length);
            if (pxLength <= 0) return;

            g.setColor(fillColor);
            g.fillRect((int) Math.round(x), (int) Math.round(y), (int) Math.round(thickness), pxLength - strokeWidth);

            Stroke old = g.getStroke();
            g.setStroke(new BasicStroke(strokeWidth));
            g.setColor(borderColor);
            g.drawRect((int) Math.round(x), (int) Math.round(y), (int) Math.round(thickness), pxLength - strokeWidth);
            g.setStroke(old);
        }
    }
}