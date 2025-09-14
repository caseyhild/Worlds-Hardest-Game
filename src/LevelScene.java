import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public abstract class LevelScene implements Scene {
    protected final WorldsHardestGame game;
    protected int w;
    protected int h;
    protected boolean previewMode = false;

    protected final int gridSizeX = 20;
    protected final int gridSizeY = 20;
    protected final int gridBoxSizeX;
    protected final int gridBoxSizeY;

    // player
    protected Player player;
    protected final double playerSize;
    protected double playerSpeed;
    protected Color playerColor = new Color(255, 0, 0);

    // enemies
    protected final List<Enemy> enemies = new ArrayList<>();
    protected final double enemySize;
    protected double enemySpeed;
    protected final Color enemyColor = new Color(0, 0, 255);

    // walls
    protected int[][] wallMatrix = new int[20][20];
    protected final List<Wall> walls = new ArrayList<>();
    protected Color wallFillColor = new Color(192, 128, 255);
    protected Color wallBorderColor = new Color(128, 0, 255);

    // color barriers
    protected final List<ColorBarrier> colorBarriers = new ArrayList<>();
    protected Color barrierPassColor = new Color(0, 255, 255);
    protected float rainbowProgress = 0f;
    protected final Color[] rainbowStops = {
            new Color(255, 0, 0),     // Red
            new Color(255, 255, 0),   // Yellow
            new Color(0, 255, 0),     // Green
            new Color(0, 255, 255),   // Cyan
            new Color(0, 0, 255),     // Blue
            new Color(255, 0, 255),   // Magenta
    };

    // keys
    protected final List<Key> keys = new ArrayList<>();
    protected final Color keyColor = new Color(170, 170, 170);

    // gates
    protected final List<Gate> gates = new ArrayList<>();
    protected final double gateOpenSpeed = 1.6;
    protected final Color gateFillColor = new Color(128, 128, 128);
    protected final Color gateBorderColor = new Color(64, 64, 64);

    // paint splotches
    protected final List<PaintSplotch> paintSplotches = new ArrayList<>();
    protected Color paintSplotchColor = new Color(0,255,255);

    // checkpoints
    protected final List<Checkpoint> checkpoints = new ArrayList<>();
    protected final Color checkpointColor = new Color(128, 255, 128);

    // conveyor belts
    protected final List<ConveyorBelt> conveyorBelts = new ArrayList<>();
    protected final Color conveyorBeltColor = new Color(160, 160, 160);
    protected double conveyorBeltSpeed;

    private final Button menuButton;

    public LevelScene(WorldsHardestGame game) {
        this.game = game;
        w = game.getScreenWidth();
        h = game.getScreenHeight();
        gridBoxSizeX = w / gridSizeX;
        gridBoxSizeY = h / gridSizeY;
        playerSize = 0.75 * Math.min(gridBoxSizeX, gridBoxSizeY);
        playerSpeed = 0.1 * Math.min(gridBoxSizeX, gridBoxSizeY);
        enemySize = 0.5 * Math.min(gridBoxSizeX, gridBoxSizeY);

        int btnW = 4 * gridBoxSizeX;
        int btnH = gridBoxSizeY - 4;
        Color wallMiddleColor = new Color((wallFillColor.getRGB() + wallBorderColor.getRGB())/2);
        menuButton = new Button(game, "Back to Menu",
                w/2 - btnW/2, h - gridBoxSizeY/2 - btnH/2, btnW, btnH,
                wallFillColor, wallMiddleColor,
                wallBorderColor, wallBorderColor);
    }

    // Create walls
    protected void buildStaticGeometry() {
        walls.clear();
        colorBarriers.clear();

        for (int row = 0; row < wallMatrix.length; row++) {
            for (int col = 0; col < wallMatrix[row].length; col++) {
                if (wallMatrix[row][col] == 1)
                    addWall(col * gridBoxSizeX, row * gridBoxSizeY, gridBoxSizeX, gridBoxSizeY);
                if (wallMatrix[row][col] == 2)
                    colorBarriers.add(new ColorBarrier(col * gridBoxSizeX, row * gridBoxSizeY, gridBoxSizeX, gridBoxSizeY, barrierPassColor));
            }
        }
    }

    protected void addWall(int x, int y, int width, int height) {
        walls.add(new Wall(x, y, width, height, wallFillColor, wallBorderColor));
    }

    // Snap to nearest grid center (grid box size = 20, centers at 10,30,50,...)
    protected double snapToGridX(double x) {
        return Math.round((x - gridBoxSizeX/2.0) / gridBoxSizeX) * gridBoxSizeX + gridBoxSizeX/2.0;
    }
    protected double snapToGridY(double y) {
        return Math.round((y - gridBoxSizeY/2.0) / gridBoxSizeY) * gridBoxSizeY + gridBoxSizeY/2.0;
    }

    // Create enemies
    protected void createEnemies() {
        enemies.clear();
    }

    // handle death
    protected void handlePlayerDeath() {
        // add a death to the counter and respawn the player at the last checkpoint
        game.addDeath();

        // reset items
        for (Checkpoint cp : checkpoints)
            cp.restore(keys);

        // reset gates
        for (Gate g : gates)
            g.update();
    }

    // update everything
    @Override
    public void update() {
        for (Gate g : gates)
            g.update();

        // dynamic gate walls based on current length
        List<Wall> dynamicWalls = new ArrayList<>();
        for (Gate g : gates) {
            Wall gw = g.getCollisionWall();
            if (gw != null)
                dynamicWalls.add(gw);
        }

        List<Wall> combinedWalls = new ArrayList<>(walls);
        combinedWalls.addAll(dynamicWalls);
        for (ColorBarrier cb : colorBarriers) {
            if (!cb.canPass(player))
                combinedWalls.add(cb.getCollisionWall());
        }

        // update player
        if (!previewMode)
            player.update(combinedWalls);

        // update enemies
        for (Enemy e : enemies)
            e.update(Math.min(gridBoxSizeX, gridBoxSizeY));

        // Check collision with enemies
        for (Enemy e : enemies) {
            if (e.collidesWith(player) && !player.isDying()) {
                player.die();
                break;
            }
        }

        // After player update, check if respawn just happened
        if (player.hasJustRespawned())
            handlePlayerDeath();

        // update checkpoints
        if (!previewMode)
            for (Checkpoint cp : checkpoints)
                cp.update(player, keys);

        // apply conveyor belt effects to player
        for (ConveyorBelt c : conveyorBelts)
            c.update(player, combinedWalls, Math.min(gridBoxSizeX, gridBoxSizeY));

        // update keys
        for (Key k : keys)
            k.update(player);

        // update paintSplotches
        for (PaintSplotch p : paintSplotches)
            p.update(player);

        // update rainbow color in case player or items are that color
        updateRainbow();

        // automatically go to another level if player goes off the screen in that direction
        checkSetNewLevel();

        // return to the main menu if button is clicked
        if (menuButton.isClicked())
            game.setScene(new MenuScene(game));
    }

    protected void checkSetNewLevel() {
        // i.e. go to the next level if the player moves
        // off the screen following a path to that level
    }

    protected void updateRainbow() {
        // set rainbowColor
        rainbowProgress += 0.02f;
        rainbowProgress %= rainbowStops.length;
        int i = (int) Math.floor(rainbowProgress);
        float t = rainbowProgress - i;
        Color rainbowColor = interpolate(rainbowStops[i], rainbowStops[(i+1) % rainbowStops.length], t);
        if(player.isRainbowColor())
            player.setRainbowColor(rainbowColor);
    }

    protected Color interpolate(Color c1, Color c2, float t) {
        int r = (int)(c1.getRed()   + t * (c2.getRed()   - c1.getRed()));
        int g = (int)(c1.getGreen() + t * (c2.getGreen() - c1.getGreen()));
        int b = (int)(c1.getBlue()  + t * (c2.getBlue()  - c1.getBlue()));
        return new Color(r, g, b);
    }

    @Override
    public void render(Graphics2D g) {
        // checkerboard floor
        drawCheckerboard(g, new Color(245,245,245), new Color(225,225,225));

        // checkpoints
        for (Checkpoint cp : checkpoints)
            cp.render(g);

        // conveyor belts
        for (ConveyorBelt b : conveyorBelts)
            b.render(g);

        // walls
        for (Wall wall : walls)
            wall.render(g, walls);

        // color barriers
        for (ColorBarrier cb : colorBarriers)
            cb.render(g, colorBarriers);

        // gates
        for (Gate gate : gates)
            gate.render(g);

        // keys
        for (Key k : keys)
            k.render(g);

        // paint splotches
        for (PaintSplotch p : paintSplotches)
            p.render(g);

        // enemies
        for (Enemy e : enemies)
            e.render(g);

        // player
        if (!previewMode)
            player.render(g);

        String stageText = "Stage " + getStage();
        Font buttonFont = new Font("SansSerif", Font.BOLD, gridBoxSizeY / 2);
        Color shadowColor = new Color(0, 0, 0, 180);
        drawTextWithShadow(g, stageText, buttonFont, w / 2, gridBoxSizeY / 2, wallBorderColor, shadowColor);


        if (!previewMode) {
            // death counter
            if (!(game.getScene() instanceof WalkthroughScene)) {
                String deathText = "Deaths: " + game.getDeaths();
                drawTextWithShadow(g, deathText, buttonFont, 5 * gridBoxSizeX / 2, gridBoxSizeY / 2, wallBorderColor, shadowColor);
            }

            // menu button
            menuButton.render(g);
        }
    }

    private int getStage() {
        return switch (this) {
            case Level1Scene ignored -> 1;
            case Level2Scene ignored -> 2;
            case Level3Scene ignored -> 3;
            case Level4Scene ignored -> 4;
            default -> 0;
        };
    }

    private void drawCheckerboard(Graphics2D g, Color c1, Color c2) {
        // background
        g.setColor(new Color(200,205,210));
        g.fillRect(0,0, game.getScreenWidth(), game.getScreenHeight());

        // checkerboard pattern
        for (int gx = 0; gx * gridBoxSizeX < game.getScreenWidth(); gx++) {
            for (int gy = 0; gy * gridBoxSizeY < game.getScreenHeight(); gy++) {
                boolean light = ((gx + gy) % 2 == 0);
                g.setColor(light ? c1 : c2);
                g.fillRect(gx * gridBoxSizeX, gy * gridBoxSizeY, gridBoxSizeX, gridBoxSizeY);
            }
        }
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

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player p) {
        this.player = p;
    }

    public void setPreviewMode(boolean preview) {
        this.previewMode = preview;
    }
}
