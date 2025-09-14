import java.awt.*;
import java.awt.image.BufferedImage;

public class TitleScene implements Scene {
    private final WorldsHardestGame game;
    private final Level1Scene preview1;
    private final Level2Scene preview2;
    private final Level3Scene preview3;
    private final Level4Scene preview4;
    private boolean pressedInside = false;

    public TitleScene(WorldsHardestGame game) {
        this.game = game;
        preview1 = new Level1Scene(game);
        preview1.setPreviewMode(true);

        preview2 = new Level2Scene(game);
        preview2.setPreviewMode(true);

        preview3 = new Level3Scene(game);
        preview3.setPreviewMode(true);

        preview4 = new Level4Scene(game);
        preview4.setPreviewMode(true);
    }

    @Override
    public void update() {
        int mx = game.getMouseX();
        int my = game.getMouseY();
        boolean inside = mx >= 0 && my >= 0 && mx < game.getScreenWidth() && my < game.getScreenHeight();

        if (game.isMousePressed()) {
            if (inside) pressedInside = true;
        } else {
            if (pressedInside && inside) {
                game.setScene(new MenuScene(game));
            }
            pressedInside = false;
        }

        preview1.update();
        preview2.update();
        preview3.update();
        preview4.update();
    }

    @Override
    public void render(Graphics2D g) {
        int w = game.getScreenWidth();
        int h = game.getScreenHeight();

        // Background
        g.setColor(new Color(32, 32, 32));
        g.fillRect(0, 0, w, h);

        // Title text
        String title = "World's Hardest Game";
        Font titleFont = new Font("SansSerif", Font.BOLD, h/12);
        Color titleColor = new Color(240, 240, 240);
        Color titleShadowColor = new Color(0, 0, 0, 180);
        drawTextWithShadow(g, title, titleFont, w/2, h/16, titleColor, titleShadowColor);

        // Subtitle text
        String subtitle = "Click anywhere to continue";
        Font subtitleFont = new Font("SansSerif", Font.PLAIN, h/40);
        Color subtitleColor = new Color(200, 200, 200);
        Color subtitleShadowColor = new Color(0, 0, 0, 180);
        drawTextWithShadow(g, subtitle, subtitleFont, w/2, h/8, subtitleColor, subtitleShadowColor);

        // Preview
        drawLevelPreview(g, preview1, w/8, 3 * h/16, 3 * w/8, 3 * h/8);
        drawLevelPreview(g, preview2, w/2, 3 * h/16, 3 * w/8, 3 * h/8);
        drawLevelPreview(g, preview3, w/2, 9 * h/16, 3 * w/8, 3 * h/8);
        drawLevelPreview(g, preview4, w/8, 9 * h/16, 3 * w/8, 3 * h/8);
    }

    private void drawLevelPreview(Graphics2D g, Scene level, int x, int y, int width, int height) {
        if (level == null)
            return;

        BufferedImage preview = new BufferedImage(Math.max(1, width), Math.max(1, height), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = preview.createGraphics();

        // Improve visual quality
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        // Clear (transparent)
        g2.setComposite(AlphaComposite.Clear);
        g2.fillRect(0, 0, width, height);
        g2.setComposite(AlphaComposite.SrcOver);

        // Scale down the game's coordinate space to fit the preview
        double sx = width / (double) game.getScreenWidth();
        double sy = height / (double) game.getScreenHeight();
        g2.scale(sx, sy);

        // Clip to game viewport so rendering doesn't bleed
        g2.setClip(0, 0, game.getScreenWidth(), game.getScreenHeight());

        // Render the level into the screen
        level.render(g2);

        g2.dispose();

        // Draw preview onto title screen (top-left at x,y)
        g.drawImage(preview, x, y, null);

        // Optional border around preview
        g.setColor(new Color(0, 0, 0, 120));
        g.setStroke(new BasicStroke(2));
        g.drawRect(x, y, width, height);
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
