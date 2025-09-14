import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public class Level3Scene extends LevelScene implements Scene {
    public Level3Scene(WorldsHardestGame game) {
        super(game);

        // Spawn player at top left checkpoint
        playerColor = new Color(255, 255, 0);
        player = new Player(game, 17 * gridBoxSizeX, 2 * gridBoxSizeY, playerSize, playerSpeed, playerColor);

        // Enemies
        createEnemies();

        // Walls and color barriers
        wallFillColor = new Color(192, 128, 255);
        wallBorderColor = new Color(128, 0, 255);
        barrierPassColor = new Color(255, 0, 255);
        wallMatrix = new int[][] {
                {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,0,0,0,0,1},
                {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
                {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
                {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
                {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
                {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
                {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
                {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
                {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
                {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
                {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
                {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
                {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
                {1,1,1,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
                {1,1,1,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
                {0,0,0,0,2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
                {0,0,0,0,2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
                {0,0,0,0,2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
                {0,0,0,0,2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
                {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
        };
        buildStaticGeometry();

        // Paint splotch
        paintSplotchColor = barrierPassColor;
        paintSplotches.add(new PaintSplotch(13 * gridBoxSizeX, 10 * gridBoxSizeY, Math.min(gridBoxSizeX, gridBoxSizeY) * 1.25, paintSplotchColor));

        // Checkpoints
        checkpoints.add(new Checkpoint(17 * gridBoxSizeX, 2 * gridBoxSizeY, 4 * gridBoxSizeX, 4 * gridBoxSizeY, checkpointColor));
        checkpoints.add(new Checkpoint(2 * gridBoxSizeX, 17 * gridBoxSizeY, 4 * gridBoxSizeX, 4 * gridBoxSizeY, checkpointColor));

        // Conveyor belts
        conveyorBeltSpeed = 0.05 * Math.min(gridBoxSizeX, gridBoxSizeY);
        conveyorBelts.add(new ConveyorBelt(ConveyorBelt.Orientation.DOWN, 13 * gridBoxSizeX, gridBoxSizeY, 2 * gridBoxSizeX, 4.5 * gridBoxSizeY, conveyorBeltSpeed, conveyorBeltColor));
        conveyorBelts.add(new ConveyorBelt(ConveyorBelt.Orientation.LEFT, 10.5 * gridBoxSizeX, 5 * gridBoxSizeY, 4.5 * gridBoxSizeX, 2 * gridBoxSizeY, conveyorBeltSpeed, conveyorBeltColor));
        conveyorBelts.add(new ConveyorBelt(ConveyorBelt.Orientation.UP, 9 * gridBoxSizeX, 2.5 * gridBoxSizeY, 2 * gridBoxSizeX, 4.5 * gridBoxSizeY, conveyorBeltSpeed, conveyorBeltColor));
        conveyorBelts.add(new ConveyorBelt(ConveyorBelt.Orientation.LEFT, 6.5 * gridBoxSizeX, gridBoxSizeY, 4.5 * gridBoxSizeX, 2 * gridBoxSizeY, conveyorBeltSpeed, conveyorBeltColor));
        conveyorBelts.add(new ConveyorBelt(ConveyorBelt.Orientation.DOWN, 5 * gridBoxSizeX, gridBoxSizeY, 2 * gridBoxSizeX, 4.5 * gridBoxSizeY, conveyorBeltSpeed, conveyorBeltColor));
        conveyorBelts.add(new ConveyorBelt(ConveyorBelt.Orientation.LEFT, 2.5 * gridBoxSizeX, 5 * gridBoxSizeY, 4.5 * gridBoxSizeX, 2 * gridBoxSizeY, conveyorBeltSpeed, conveyorBeltColor));
        conveyorBelts.add(new ConveyorBelt(ConveyorBelt.Orientation.UP, gridBoxSizeX, 2.5 * gridBoxSizeY, 2 * gridBoxSizeX, 4.5 * gridBoxSizeY, conveyorBeltSpeed, conveyorBeltColor));
        conveyorBelts.add(new ConveyorBelt(ConveyorBelt.Orientation.LEFT, -gridBoxSizeX, gridBoxSizeY, 4 * gridBoxSizeX, 2 * gridBoxSizeY, conveyorBeltSpeed, conveyorBeltColor));
        conveyorBelts.add(new ConveyorBelt(ConveyorBelt.Orientation.RIGHT, -gridBoxSizeX, 9 * gridBoxSizeY, 2 * gridBoxSizeX, 2 * gridBoxSizeY, conveyorBeltSpeed, conveyorBeltColor));
    }

    // Create enemies
    @Override
    public void createEnemies() {
        enemies.clear();
        enemySpeed = 0.08 * Math.min(gridBoxSizeX, gridBoxSizeY);

        // Static enemies
        for (int x = 0; x < 2; x++) {
            for (int y = 0; y < 4; y++) {
                enemies.add(new Enemy(Enemy.MotionType.STATIC, (3.5 + x) * gridBoxSizeX, (1.5 + y) * gridBoxSizeY, enemySize, enemyColor));
                enemies.add(new Enemy(Enemy.MotionType.STATIC, (7.5 + x) * gridBoxSizeX, (3.5 + y) * gridBoxSizeY, enemySize, enemyColor));
                enemies.add(new Enemy(Enemy.MotionType.STATIC, (11.5 + x) * gridBoxSizeX, (1.5 + y) * gridBoxSizeY, enemySize, enemyColor));
            }
        }
        for (int y = 0; y < 9; y++) {
            enemies.add(new Enemy(Enemy.MotionType.STATIC, 15.5 * gridBoxSizeX, (4.5 + y) * gridBoxSizeY, enemySize, enemyColor));
        }
        for (int x = 0; x < 14; x++) {
            enemies.add(new Enemy(Enemy.MotionType.STATIC, (1.5 + x) * gridBoxSizeX, 7.5 * gridBoxSizeY, enemySize, enemyColor));
            enemies.add(new Enemy(Enemy.MotionType.STATIC, (1.5 + x) * gridBoxSizeX, 12.5 * gridBoxSizeY, enemySize, enemyColor));
        }
        for (int x = 0; x < 7; x++) {
            for (int y = 0; y < 2; y++) {
                enemies.add(new Enemy(Enemy.MotionType.STATIC, (7.5 + x) * gridBoxSizeX, (15.5 + y) * gridBoxSizeY, enemySize, enemyColor));
            }
        }

        // Linear enemies
        for (int y = 0; y < 3; y++) {
            enemies.add(new Enemy(Enemy.MotionType.LINEAR,
                    (1.5 + 13 * (y % 2)) * gridBoxSizeX,
                    (1.5 + 2 * y) * gridBoxSizeY,
                    enemySpeed,
                    new ArrayList<>(Arrays.asList((1.5 + 13 * (y % 2)) * gridBoxSizeX, (14.5 - 13 * (y % 2)) * gridBoxSizeX)),
                    new ArrayList<>(Arrays.asList((1.5 + 2 * y) * gridBoxSizeY, (1.5 + 2 * y) * gridBoxSizeY)),
                    enemySize,
                    enemyColor)
            );
            enemies.add(new Enemy(Enemy.MotionType.LINEAR,
                    (1.5 + 13 * (y % 2)) * gridBoxSizeX,
                    (2.5 + 2 * y) * gridBoxSizeY,
                    enemySpeed,
                    new ArrayList<>(Arrays.asList((1.5 + 13 * (y % 2)) * gridBoxSizeX, (14.5 - 13 * (y % 2)) * gridBoxSizeX)),
                    new ArrayList<>(Arrays.asList((2.5 + 2 * y) * gridBoxSizeY, (2.5 + 2 * y) * gridBoxSizeY)),
                    enemySize,
                    enemyColor)
            );
        }
        for (int x = 0; x < 9; x++) {
            enemies.add(new Enemy(Enemy.MotionType.LINEAR,
                    (2.5 + x) * gridBoxSizeX,
                    (8.5 + 3 * (x/3 % 2)) * gridBoxSizeY,
                    enemySpeed,
                    new ArrayList<>(Arrays.asList((2.5 + x) * gridBoxSizeX, (2.5 + x) * gridBoxSizeX)),
                    new ArrayList<>(Arrays.asList((8.5 + 3 * (x/3 % 2)) * gridBoxSizeY, (11.5 - 3 * (x/3 % 2)) * gridBoxSizeY)),
                    enemySize,
                    enemyColor)
            );
        }
        for (int y = 0; y < 9; y++) {
            if (y == 4)
                continue;
            enemies.add(new Enemy(Enemy.MotionType.LINEAR,
                    (16.5 + 2 * (y/5 % 2)) * gridBoxSizeX,
                    (4.5 + y) * gridBoxSizeY,
                    0.75 * enemySpeed,
                    new ArrayList<>(Arrays.asList((16.5 + 2 * (y/5 % 2)) * gridBoxSizeX, (18.5 - 2 * (y/5 % 2)) * gridBoxSizeX)),
                    new ArrayList<>(Arrays.asList((4.5 + y) * gridBoxSizeY, (4.5 + y) * gridBoxSizeY)),
                    enemySize,
                    enemyColor)
            );
        }

        // Box enemies
        for (int y = 0; y < 2; y++) {
            for (int x = 0; x < 2; x++) {
                enemies.add(new Enemy(Enemy.MotionType.LINEAR,
                        (5.5 + x) * gridBoxSizeX,
                        (13.5 + y) * gridBoxSizeY,
                        2 * enemySpeed,
                        new ArrayList<>(Arrays.asList((5.5 + x) * gridBoxSizeX, (14.5 + x) * gridBoxSizeX, (14.5 + x) * gridBoxSizeX, (5.5 + x) * gridBoxSizeX)),
                        new ArrayList<>(Arrays.asList((13.5 + y) * gridBoxSizeY, (13.5 + y) * gridBoxSizeY, (17.5 + y) * gridBoxSizeY, (17.5 + y) * gridBoxSizeY)),
                        enemySize,
                        enemyColor)
                );
                enemies.add(new Enemy(Enemy.MotionType.LINEAR,
                        (14.5 + x) * gridBoxSizeX,
                        (17.5 + y) * gridBoxSizeY,
                        2 * enemySpeed,
                        new ArrayList<>(Arrays.asList((14.5 + x) * gridBoxSizeX, (5.5 + x) * gridBoxSizeX, (5.5 + x) * gridBoxSizeX, (14.5 + x) * gridBoxSizeX)),
                        new ArrayList<>(Arrays.asList((17.5 + y) * gridBoxSizeY, (17.5 + y) * gridBoxSizeY, (13.5 + y) * gridBoxSizeY, (13.5 + y) * gridBoxSizeY)),
                        enemySize,
                        enemyColor)
                );
            }
        }
    }

    @Override
    protected void checkSetNewLevel() {
        // if performing walkthrough, let it control scene management
        if (game.getScene() instanceof WalkthroughScene)
            return;

        // go to level 2 if player goes off the top of the screen
        if (player.getY() < 0) {
            player.setPosition(player.getX(), h);
            for (Checkpoint cp : checkpoints)
                cp.playerInsideLastFrame = false;
            game.setScene(game.level2, player);
        }
        // go to level 4 if player goes off the left of the screen
        if (player.getX() < 0) {
            player.setPosition(w, player.getY());
            for (Checkpoint cp : checkpoints)
                cp.playerInsideLastFrame = false;
            game.setScene(game.level4, player);
        }
    }
}
