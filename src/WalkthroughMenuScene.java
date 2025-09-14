import java.awt.*;

public class WalkthroughMenuScene implements Scene {
    private final WorldsHardestGame game;
    private final Button level1Button;
    private final Button level2Button;
    private final Button level3Button;
    private final Button level4Button;
    private final Button menuButton;

    public WalkthroughMenuScene(WorldsHardestGame game) {
        this.game = game;
        int w = game.getScreenWidth();
        int h = game.getScreenHeight();

        // Central column of buttons
        int btnW = 3 * w/10;
        int btnH = 13 * h/200;
        int baseX = w/3 - btnW/2;
        int baseY = 7 * h/16;

        level1Button = new Button(game, "Stage 1",
                baseX, baseY, btnW, btnH,
                new Color(220, 180, 60), new Color(180, 140, 30),
                new Color(18, 18, 18), Color.WHITE);

        level2Button = new Button(game, "Stage 2",
                baseX + w/3, baseY, btnW, btnH,
                new Color(220, 180, 60), new Color(180, 140, 30),
                new Color(18, 18, 18), Color.WHITE);

        level3Button = new Button(game, "Stage 3",
                baseX, baseY + h/8, btnW, btnH,
                new Color(220, 180, 60), new Color(180, 140, 30),
                new Color(18, 18, 18), Color.WHITE);

        level4Button = new Button(game, "Stage 4",
                baseX + w/3, baseY + h/8, btnW, btnH,
                new Color(220, 180, 60), new Color(180, 140, 30),
                new Color(18, 18, 18), Color.WHITE);

        menuButton = new Button(game, "Back to Menu",
                w/2 - btnW/2, 3 * h/4, btnW, btnH,
                new Color(60, 120, 200), new Color(30, 90, 160),
                Color.BLACK, Color.WHITE);
    }

    @Override
    public void update() {
        if (level1Button.isClicked()) {
            game.level1 = new Level1Scene(game);
            game.level2 = new Level2Scene(game);
            game.level3 = new Level3Scene(game);
            game.level4 = new Level4Scene(game);
            game.setScene(new WalkthroughScene(game, game.level1));
        }
        if (level2Button.isClicked()) {
            game.level1 = new Level1Scene(game);
            game.level2 = new Level2Scene(game);
            game.level3 = new Level3Scene(game);
            game.level4 = new Level4Scene(game);
            game.setScene(new WalkthroughScene(game, game.level2));
        }
        if (level3Button.isClicked()) {
            game.level1 = new Level1Scene(game);
            game.level2 = new Level2Scene(game);
            game.level3 = new Level3Scene(game);
            game.level4 = new Level4Scene(game);
            game.setScene(new WalkthroughScene(game, game.level3));
        }
        if (level4Button.isClicked()) {
            game.level1 = new Level1Scene(game);
            game.level2 = new Level2Scene(game);
            game.level3 = new Level3Scene(game);
            game.level4 = new Level4Scene(game);
            game.setScene(new WalkthroughScene(game, game.level4));
        }
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

        String title = "Walkthrough";
        Font titleFont = new Font("SansSerif", Font.BOLD, h/12);
        Color titleColor = new Color(240, 240, 240);
        Color shadowColor = new Color(0, 0, 0, 180);
        drawTextWithShadow(g, title, titleFont, w/2, h/4, titleColor, shadowColor);

        String msg = "Start from...";
        Font msgFont = new Font("SansSerif", Font.BOLD, h/28);
        drawTextWithShadow(g, msg, msgFont, w/2, h/3, Color.WHITE, shadowColor);

        level1Button.render(g);
        level2Button.render(g);
        level3Button.render(g);
        level4Button.render(g);
        menuButton.render(g);
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