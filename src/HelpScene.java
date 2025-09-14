import java.awt.*;

public class HelpScene implements Scene {
    private final WorldsHardestGame game;
    private final Button menuButton;

    public HelpScene(WorldsHardestGame game) {
        this.game = game;
        int w = game.getScreenWidth();
        int h = game.getScreenHeight();

        int btnW = 3 * w/10;
        int btnH = 13 * h/200;

        menuButton = new Button(game, "Back to Menu",
                w/2 - btnW/2, 3 * h/4, btnW, btnH,
                new Color(60, 120, 200), new Color(30, 90, 160),
                Color.BLACK, Color.WHITE);
    }

    @Override
    public void update() {
        if (menuButton.isClicked())
            game.setScene(new MenuScene(game));
    }

    @Override
    public void render(Graphics2D g) {
        int w = game.getScreenWidth();
        int h = game.getScreenHeight();

        // Background
        g.setColor(new Color(32, 32, 32));
        g.fillRect(0, 0, w, h);

        String[] helpText = new String[] {
                "1. Use the arrow keys to move.",
                "",
                "2. Avoid enemies.",
                "",
                "3. Collect keys to unlock gates.",
                "",
                "4. Reach checkpoints to save your progress.",
                "",
                "5. Unlock new paint colors to move to the next level."
        };
        int y = h/8;
        for (String text : helpText) {
            Font helpFont = new Font("SansSerif", Font.BOLD, h/30);
            Color helpColor = new Color(240, 240, 240);
            Color helpShadowColor = new Color(0, 0, 0, 180);
            drawTextWithShadow(g, text, helpFont, w/16, y, helpColor, helpShadowColor);
            y += h/20;
        }

        menuButton.render(g);
    }

    private void drawTextWithShadow(Graphics2D g, String text, Font font, int x, int y, Color mainColor, Color shadowColor) {
        g.setFont(font);
        FontMetrics fm = g.getFontMetrics();
        y = y - fm.getHeight() / 2 + fm.getAscent();

        g.setColor(shadowColor);
        g.drawString(text, x + font.getSize()/12, y + font.getSize()/12);
        g.setColor(mainColor);
        g.drawString(text, x, y);
    }
}