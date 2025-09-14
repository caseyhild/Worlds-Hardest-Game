import java.awt.*;

public class WalkthroughEndScene implements Scene {
    private final WorldsHardestGame game;
    private final Button menuButton;
    private final Button playGameButton;

    public WalkthroughEndScene(WorldsHardestGame game) {
        this.game = game;
        int w = game.getScreenWidth();
        int h = game.getScreenHeight();

        menuButton = new Button(game, "Main Menu",
                3 * w/8, 2 * h/3, w/4, h/16,
                new Color(70, 160, 220), new Color(40, 110, 180),
                Color.BLACK, Color.WHITE);

        playGameButton = new Button(game, "Play the Game",
                3 * w/8, 2 * h/3 + h/10, w/4, h/16,
                new Color(90, 200, 100), new Color(60, 150, 70),
                Color.BLACK, Color.WHITE);
    }

    @Override
    public void update() {
        if (menuButton.isClicked())
            game.setScene(new MenuScene(game));
        if (playGameButton.isClicked()) {
            game.level1 = new Level1Scene(game);
            game.level2 = new Level2Scene(game);
            game.level3 = new Level3Scene(game);
            game.level4 = new Level4Scene(game);
            game.setScene(game.level1);
            game.resetDeaths();
        }
    }

    @Override
    public void render(Graphics2D g) {
        int w = game.getScreenWidth();
        int h = game.getScreenHeight();

        g.setColor(new Color(20, 20, 20));
        g.fillRect(0, 0, w, h);

        String title = "Walkthrough";
        Font titleFont = new Font("SansSerif", Font.BOLD, h/10);
        Color titleColor = new Color(255, 200, 100);
        Color shadowColor = new Color(0, 0, 0, 180);
        drawTextWithShadow(g, title, titleFont, w/2, h/4-h/8, titleColor, shadowColor);
        title = "Finished";
        drawTextWithShadow(g, title, titleFont, w/2, h/4, titleColor, shadowColor);

        String msg = "Now go and see if you can";
        Font msgFont = new Font("SansSerif", Font.BOLD, h/28);
        drawTextWithShadow(g, msg, msgFont, w/2, h/2-h/16, Color.WHITE, shadowColor);
        msg = "complete the game yourself!";
        drawTextWithShadow(g, msg, msgFont, w/2, h/2, Color.WHITE, shadowColor);

        menuButton.render(g);
        playGameButton.render(g);
    }

    private void drawTextWithShadow(Graphics2D g, String text, Font font, int x, int y,
                                    Color mainColor, Color shadowColor) {
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