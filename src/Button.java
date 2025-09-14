import java.awt.*;

public class Button {
    private final WorldsHardestGame game;
    private final String text;
    private final int x, y, width, height;
    private final Color baseColor;
    private final Color hoverColor;
    private final Color borderColor;
    private final Color textColor;
    private boolean pressedInside = false;

    public Button(WorldsHardestGame game, String text, int x, int y, int width, int height,
                  Color baseColor, Color hoverColor, Color borderColor, Color textColor) {
        this.game = game;
        this.text = text;
        this.x = x; this.y = y;
        this.width = width;
        this.height = height;
        this.baseColor = baseColor;
        this.hoverColor = hoverColor;
        this.borderColor = borderColor;
        this.textColor = textColor;
    }

    public boolean isClicked() {
        int mx = game.getMouseX();
        int my = game.getMouseY();
        boolean inside = mx >= x && mx <= x + width && my >= y && my <= y + height;

        if (game.isMousePressed()) {
            if (inside) pressedInside = true;
        } else {
            if (pressedInside && inside) {
                pressedInside = false;
                return true;
            }
            pressedInside = false;
        }
        return false;
    }

    public void render(Graphics2D g) {
        int mx = game.getMouseX();
        int my = game.getMouseY();
        boolean inside = mx >= x && mx <= x + width && my >= y && my <= y + height;

        // Background
        g.setColor(inside ? hoverColor : baseColor);
        g.fillRoundRect(x, y, width, height, 20, 20);

        // Border
        g.setColor(borderColor);
        g.drawRoundRect(x, y, width, height, 20, 20);

        // Text
        Font buttonFont = new Font("SansSerif", Font.BOLD, height/2);
        Color shadowColor = new Color(0, 0, 0, 180);
        drawTextWithShadow(g, text, buttonFont, x + width/2, y + height/2, textColor, shadowColor);
    }

    private void drawTextWithShadow(Graphics2D g, String text, Font font, int x, int y, Color mainColor, Color shadowColor) {
        g.setFont(font);
        FontMetrics fm = g.getFontMetrics();
        x = x - fm.stringWidth(text) / 2;
        y = y - fm.getHeight() / 2 + fm.getAscent();

        g.setColor(shadowColor);
        g.drawString(text, x + font.getSize()/12, y + font.getSize()/12);
        g.setColor(mainColor);
        g.drawString(text, x, y);
    }
}