import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public class Level4Scene extends LevelScene implements Scene {
    public Level4Scene(WorldsHardestGame game) {
        super(game);

        // Spawn player at top left checkpoint
        playerColor = new Color(255, 0, 255);
        player = new Player(game, 18 * gridBoxSizeX, 17 * gridBoxSizeY, playerSize, playerSpeed, playerColor);

        // Enemies
        createEnemies();

        // Walls and color barriers
        wallFillColor = new Color(192, 128, 255);
        wallBorderColor = new Color(128, 0, 255);
        barrierPassColor = new Color(0, 255, 0);
        wallMatrix = new int[][] {
                {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
                {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
                {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
                {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
                {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
                {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
                {1,2,2,2,2,2,2,2,2,2,2,2,2,0,0,0,0,0,0,1},
                {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
                {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
                {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
                {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
                {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
                {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
                {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
                {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
                {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
                {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
                {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
                {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
                {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
        };
        buildStaticGeometry();

        // Paint splotch
        paintSplotchColor = barrierPassColor;
        paintSplotches.add(new PaintSplotch(17 * gridBoxSizeX, 13 * gridBoxSizeY, Math.min(gridBoxSizeX, gridBoxSizeY) * 1.25, paintSplotchColor));

        // Checkpoints
        checkpoints.add(new Checkpoint(18 * gridBoxSizeX, 17 * gridBoxSizeY, 4 * gridBoxSizeX, 4 * gridBoxSizeY, checkpointColor));
        checkpoints.add(new Checkpoint(7 * gridBoxSizeX, 3.5 * gridBoxSizeY, 12 * gridBoxSizeX, 5 * gridBoxSizeY, checkpointColor));

        // Conveyor Belts
        conveyorBeltSpeed = 0.05 * Math.min(gridBoxSizeX, gridBoxSizeY);
        conveyorBelts.add(new ConveyorBelt(ConveyorBelt.Orientation.LEFT, 16.5 * gridBoxSizeX, gridBoxSizeY, 4.5 * gridBoxSizeX, 2 * gridBoxSizeY, conveyorBeltSpeed, conveyorBeltColor));
        conveyorBelts.add(new ConveyorBelt(ConveyorBelt.Orientation.DOWN, 15 * gridBoxSizeX, gridBoxSizeY, 2 * gridBoxSizeX, 8.5 * gridBoxSizeY, conveyorBeltSpeed, conveyorBeltColor));
        conveyorBelts.add(new ConveyorBelt(ConveyorBelt.Orientation.RIGHT, 15 * gridBoxSizeX, 9 * gridBoxSizeY, 6 * gridBoxSizeX, 2 * gridBoxSizeY, conveyorBeltSpeed, conveyorBeltColor));
    }

    // Create enemies
    @Override
    public void createEnemies() {
        enemies.clear();
        enemySpeed = 0.08 * Math.min(gridBoxSizeX, gridBoxSizeY);
        double gridBoxSize = Math.min(gridBoxSizeX, gridBoxSizeY);

        // Static enemies
        for (int x = 0; x < 2; x++) {
            for (int y = 0; y < 6; y++) {
                enemies.add(new Enemy(Enemy.MotionType.STATIC, (17.5 + x) * gridBoxSizeX, (3.5 + y) * gridBoxSizeY, enemySize, enemyColor));
            }
        }
        for (int x = 0; x < 2; x++) {
            for (int y = 0; y < 10; y++) {
                enemies.add(new Enemy(Enemy.MotionType.STATIC, (13.5 + x) * gridBoxSizeX, (1.5 + y) * gridBoxSizeY, enemySize, enemyColor));
            }
        }
        for (int x = 0; x < 6; x++) {
            enemies.add(new Enemy(Enemy.MotionType.STATIC, (13.5 + x) * gridBoxSizeX, 11.5 * gridBoxSizeY, enemySize, enemyColor));
            enemies.add(new Enemy(Enemy.MotionType.STATIC, (13.5 + x) * gridBoxSizeX, 14.5 * gridBoxSizeY, enemySize, enemyColor));
        }

        // Box enemies
        for (int y = 0; y < 2; y++) {
            for (int x = 0; x < 2; x++) {
                enemies.add(new Enemy(Enemy.MotionType.LINEAR,
                        (1.5 + x) * gridBoxSizeX,
                        (7.5 + y) * gridBoxSizeY,
                        2 * enemySpeed,
                        new ArrayList<>(Arrays.asList((1.5 + x) * gridBoxSizeX, (11.5 + x) * gridBoxSizeX, (11.5 + x) * gridBoxSizeX, (1.5 + x) * gridBoxSizeX)),
                        new ArrayList<>(Arrays.asList((7.5 + y) * gridBoxSizeY, (7.5 + y) * gridBoxSizeY, (17.5 + y) * gridBoxSizeY, (17.5 + y) * gridBoxSizeY)),
                        enemySize,
                        enemyColor)
                );
                enemies.add(new Enemy(Enemy.MotionType.LINEAR,
                        (11.5 + x) * gridBoxSizeX,
                        (7.5 + y) * gridBoxSizeY,
                        2 * enemySpeed,
                        new ArrayList<>(Arrays.asList((11.5 + x) * gridBoxSizeX, (11.5 + x) * gridBoxSizeX, (1.5 + x) * gridBoxSizeX, (1.5 + x) * gridBoxSizeX)),
                        new ArrayList<>(Arrays.asList((7.5 + y) * gridBoxSizeY, (17.5 + y) * gridBoxSizeY, (17.5 + y) * gridBoxSizeY, (7.5 + y) * gridBoxSizeY)),
                        enemySize,
                        enemyColor)
                );
                enemies.add(new Enemy(Enemy.MotionType.LINEAR,
                        (11.5 + x) * gridBoxSizeX,
                        (17.5 + y) * gridBoxSizeY,
                        2 * enemySpeed,
                        new ArrayList<>(Arrays.asList((11.5 + x) * gridBoxSizeX, (1.5 + x) * gridBoxSizeX, (1.5 + x) * gridBoxSizeX, (11.5 + x) * gridBoxSizeX)),
                        new ArrayList<>(Arrays.asList((17.5 + y) * gridBoxSizeY, (17.5 + y) * gridBoxSizeY, (7.5 + y) * gridBoxSizeY, (7.5 + y) * gridBoxSizeY)),
                        enemySize,
                        enemyColor)
                );
                enemies.add(new Enemy(Enemy.MotionType.LINEAR,
                        (1.5 + x) * gridBoxSizeX,
                        (17.5 + y) * gridBoxSizeY,
                        2 * enemySpeed,
                        new ArrayList<>(Arrays.asList((1.5 + x) * gridBoxSizeX, (1.5 + x) * gridBoxSizeX, (11.5 + x) * gridBoxSizeX, (11.5 + x) * gridBoxSizeX)),
                        new ArrayList<>(Arrays.asList((17.5 + y) * gridBoxSizeY, (7.5 + y) * gridBoxSizeY, (7.5 + y) * gridBoxSizeY, (17.5 + y) * gridBoxSizeY)),
                        enemySize,
                        enemyColor)
                );
            }
        }

        // Windmill enemies
        for (int i = 0; i < 4; i++)
            enemies.add(new Enemy(Enemy.MotionType.ORBIT, 7 * gridBoxSizeX, 13 * gridBoxSizeY, 0.5 * gridBoxSize, Math.toRadians(90 * i), 0.03, enemySize, enemyColor));
        for (int i = 0; i < 4; i++)
            enemies.add(new Enemy(Enemy.MotionType.ORBIT, 7 * gridBoxSizeX, 13 * gridBoxSizeY, 1.5 * gridBoxSize, Math.toRadians(90 * i), 0.03, enemySize, enemyColor));
        for (int i = 0; i < 4; i++)
            enemies.add(new Enemy(Enemy.MotionType.ORBIT, 7 * gridBoxSizeX, 13 * gridBoxSizeY, 2.5 * gridBoxSize, Math.toRadians(90 * i), 0.03, enemySize, enemyColor));
        for (int i = 0; i < 4; i++)
            enemies.add(new Enemy(Enemy.MotionType.ORBIT, 7 * gridBoxSizeX, 13 * gridBoxSizeY, 3.5 * gridBoxSize, Math.toRadians(90 * i), 0.03, enemySize, enemyColor));
    }

    @Override
    protected void checkSetNewLevel() {
        // if performing walkthrough, let it control scene management
        if (game.getScene() instanceof WalkthroughScene)
            return;

        // go to level 3 if player goes off the right of the screen
        if (player.getX() > w) {
            player.setPosition(0, player.getY());
            for (Checkpoint cp : checkpoints)
                cp.playerInsideLastFrame = false;
            game.setScene(game.level3, player);
        }
        // win if last checkpoint is reached
        if (checkpoints.get(1).playerInsideLastFrame)
            game.setScene(new WinScene(game));
    }

    protected void updateRainbow() {
        // set color barrier, paint splotch, and player to rainbow color if needed
        rainbowProgress += 0.02f;
        rainbowProgress %= rainbowStops.length;
        int i = (int) Math.floor(rainbowProgress);
        float t = rainbowProgress - i;
        Color rainbowColor = interpolate(rainbowStops[i], rainbowStops[(i+1) % rainbowStops.length], t);

        barrierPassColor = rainbowColor;
        paintSplotchColor = rainbowColor;
        for (ColorBarrier cb : colorBarriers)
            cb.setColor(rainbowColor);
        paintSplotches.getFirst().setColor(rainbowColor);
        if (paintSplotches.getFirst().isCollected() || player.isRainbowColor())
            player.setRainbowColor(rainbowColor);
    }
}
