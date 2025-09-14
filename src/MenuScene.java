import java.awt.*;

public class MenuScene implements Scene {
    private final WorldsHardestGame game;
    private final Button playButton;
    private final Button helpButton;
    private final Button walkthroughButton;
    private final Button quitButton;

    public MenuScene(WorldsHardestGame game) {
        this.game = game;

        int w = game.getScreenWidth();
        int h = game.getScreenHeight();

        // Central column of buttons
        int btnW = 3 * w/10;
        int btnH = 13 * h/200;
        int centerX = w/2 - btnW/2;
        int baseY = 9 * h/20;

        playButton = new Button(
                game,
                "Play",
                centerX,
                baseY,
                btnW,
                btnH,
                new Color(50, 120, 200),
                new Color(30, 90, 160),
                new Color(18, 18, 18),
                Color.WHITE
        );

        helpButton = new Button(
                game,
                "Help",
                centerX,
                baseY + h/10,
                btnW,
                btnH,
                new Color(50, 200, 120),
                new Color(30, 160, 90),
                new Color(18, 18, 18),
                Color.WHITE
        );

        walkthroughButton = new Button(
                game,
                "Walkthrough",
                centerX,
                baseY + h/5,
                btnW,
                btnH,
                new Color(220, 180, 60),
                new Color(180, 140, 30),
                new Color(18, 18, 18),
                Color.WHITE
        );

        quitButton = new Button(
                game,
                "Quit Game",
                centerX,
                baseY + 3 * h/10,
                btnW,
                btnH,
                new Color(200, 60, 60),
                new Color(160, 30, 30),
                new Color(18, 18, 18),
                Color.WHITE
        );
    }

    @Override
    public void update() {
        // Button actions
        if (playButton.isClicked()) {
            game.level1 = new Level1Scene(game);
            game.level2 = new Level2Scene(game);
            game.level3 = new Level3Scene(game);
            game.level4 = new Level4Scene(game);
            game.setScene(game.level1);
            game.resetDeaths();
        }
        if (helpButton.isClicked())
            game.setScene(new HelpScene(game));
        if (walkthroughButton.isClicked())
            game.setScene(new WalkthroughMenuScene(game));
        if (quitButton.isClicked()) {
            game.dispose();
            System.exit(0);
        }
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
        drawTextWithShadow(g, title, titleFont, w/2, h/4, titleColor, titleShadowColor);

        // Render buttons
        playButton.render(g);
        helpButton.render(g);
        walkthroughButton.render(g);
        quitButton.render(g);
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