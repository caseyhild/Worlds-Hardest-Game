import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public class Level2Scene extends LevelScene implements Scene {
    public Level2Scene(WorldsHardestGame game) {
        super(game);

        // Spawn player at bottom-left checkpoint
        playerColor = new Color(0, 255, 255);
        player = new Player(game, 3 * gridBoxSizeX, 17 * gridBoxSizeY, playerSize, playerSpeed, playerColor);

        // Enemies
        createEnemies();

        // Walls and color barriers
        wallFillColor = new Color(192, 128, 255);
        wallBorderColor = new Color(128, 0, 255);
        barrierPassColor = new Color(255, 255, 0);
        wallMatrix = new int[][] {
                {1,1,1,1,1,1,0,1,1,1,1,1,1,0,1,1,1,1,1,1},
                {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
                {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
                {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
                {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
                {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
                {1,0,0,0,0,0,1,0,0,0,0,0,0,1,0,0,0,0,0,1},
                {1,0,0,0,0,0,1,0,0,0,0,0,0,1,0,0,0,0,0,1},
                {1,0,0,0,0,0,1,0,0,0,0,0,0,1,0,0,0,0,0,1},
                {1,0,0,0,0,0,1,0,0,0,0,0,0,1,0,0,0,0,0,1},
                {1,0,0,0,0,0,1,0,0,0,0,0,0,1,0,0,0,0,0,1},
                {1,0,0,0,0,0,1,0,0,0,0,0,0,1,0,0,0,0,0,1},
                {1,0,0,0,0,0,1,0,0,0,0,0,0,1,0,0,0,0,0,1},
                {1,0,0,0,0,0,1,0,0,0,0,0,0,1,0,0,0,0,0,1},
                {1,0,0,0,0,0,1,0,0,0,0,0,0,1,0,0,0,0,0,1},
                {0,0,0,0,0,0,1,0,0,0,0,0,0,1,1,2,2,2,2,1},
                {0,0,0,0,0,0,1,0,0,0,0,0,0,1,1,0,0,0,0,1},
                {0,0,0,0,0,0,1,0,0,0,0,0,0,1,1,0,0,0,0,1},
                {0,0,0,0,0,0,1,0,0,0,0,0,0,1,1,0,0,0,0,1},
                {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,0,0,0,0,1}
        };
        buildStaticGeometry();
        addWall(6 * gridBoxSizeX, -gridBoxSizeY, gridBoxSizeX, gridBoxSizeY);
        addWall(13 * gridBoxSizeX, -gridBoxSizeY, gridBoxSizeX, gridBoxSizeY);

        // Paint splotch
        paintSplotchColor = barrierPassColor;
        paintSplotches.add(new PaintSplotch(10 * gridBoxSizeX, 17 * gridBoxSizeY, 1.25 * Math.min(gridBoxSizeX, gridBoxSizeY), paintSplotchColor));

        // Checkpoints
        checkpoints.add(new Checkpoint(3 * gridBoxSizeX, 17 * gridBoxSizeY, 6 * gridBoxSizeX, 4 * gridBoxSizeY, checkpointColor));
        checkpoints.add(new Checkpoint(6.5 * gridBoxSizeX, 0.5 * gridBoxSizeY, gridBoxSizeX, gridBoxSizeY, checkpointColor));
        checkpoints.add(new Checkpoint(13.5 * gridBoxSizeX, 0.5 * gridBoxSizeY, gridBoxSizeX, gridBoxSizeY, checkpointColor));
        checkpoints.add(new Checkpoint(17 * gridBoxSizeX, 18 * gridBoxSizeY, 4 * gridBoxSizeX, 4 * gridBoxSizeY, checkpointColor));
    }

    // Create enemies
    @Override
    public void createEnemies() {
        enemies.clear();
        enemySpeed = 0.08 * Math.min(gridBoxSizeX, gridBoxSizeY);
        double gridBoxSize = Math.min(gridBoxSizeX, gridBoxSizeY);

        // Windmill enemies
        double[] cxs = new double[] {3.5 * gridBoxSizeX, 3.5 * gridBoxSizeX, 16.5 * gridBoxSizeX, 16.5 * gridBoxSizeX};
        double[] cys = new double[] {12.5 * gridBoxSizeY, 3.5 * gridBoxSizeY, 3.5 * gridBoxSizeX, 12.5 * gridBoxSizeY};
        for (double cx : cxs) {
            for (double cy : cys) {
                enemies.add(new Enemy(Enemy.MotionType.STATIC, cx, cy, enemySize, enemyColor));
                for (int i = 0; i < 4; i++)
                    enemies.add(new Enemy(Enemy.MotionType.ORBIT, cx, cy, gridBoxSize, Math.toRadians(90 * i), 0.03, enemySize, enemyColor));
                for (int i = 0; i < 4; i++)
                    enemies.add(new Enemy(Enemy.MotionType.ORBIT, cx, cy, 2 * gridBoxSize, Math.toRadians(90 * i), 0.03, enemySize, enemyColor));
            }
        }

        // Side linear enemies
        for (int i = 0; i < 4; i++) {
            enemies.add(new Enemy(Enemy.MotionType.LINEAR,
                    (1.5 + i % 2 * 4) * gridBoxSizeX,
                    (6.5 + i) * gridBoxSizeY,
                    enemySpeed,
                    new ArrayList<>(Arrays.asList((1.5 + i % 2 * 4) * gridBoxSizeX, (1.5 + (1 - i % 2) * 4) * gridBoxSizeX)),
                    new ArrayList<>(Arrays.asList((6.5 + i) * gridBoxSizeY, (6.5 + i) * gridBoxSizeY)),
                    enemySize,
                    enemyColor)
            );
        }
        for (int i = 0; i < 4; i++) {
            enemies.add(new Enemy(Enemy.MotionType.LINEAR,
                    (18.5 - i % 2 * 4) * gridBoxSizeX,
                    (6.5 + i) * gridBoxSizeY,
                    enemySpeed,
                    new ArrayList<>(Arrays.asList((18.5 - i % 2 * 4) * gridBoxSizeX, (18.5 - (1 - i % 2) * 4) * gridBoxSizeX)),
                    new ArrayList<>(Arrays.asList((6.5 + i) * gridBoxSizeY, (6.5 + i) * gridBoxSizeY)),
                    enemySize,
                    enemyColor)
            );
        }

        // Top linear enemies
        for (int i = 0; i < 2; i++) {
            enemies.add(new Enemy(Enemy.MotionType.LINEAR,
                    (6.5 + 7 * i) * gridBoxSizeX,
                    1.5 * gridBoxSizeY,
                    enemySpeed,
                    new ArrayList<>(Arrays.asList((6.5 + 7 * i) * gridBoxSizeX, (6.5 + 7 * i) * gridBoxSizeX)),
                    new ArrayList<>(Arrays.asList(1.5 * gridBoxSizeY, 5.5 * gridBoxSizeY)),
                    enemySize,
                    enemyColor)
            );
        }

        // Middle linear enemies
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 2; j++) {
                enemies.add(new Enemy(Enemy.MotionType.LINEAR,
                        (7.5 + j % 2 * 5) * gridBoxSizeX,
                        (1.5 + 2 * i) * gridBoxSizeY,
                        enemySpeed,
                        new ArrayList<>(Arrays.asList((7.5 + j % 2 * 5) * gridBoxSizeX, (7.5 + (1 - j % 2) * 5) * gridBoxSizeX)),
                        new ArrayList<>(Arrays.asList((1.5 + 2 * i) * gridBoxSizeY, (2.5 + 2 * i) * gridBoxSizeY)),
                        enemySize,
                        enemyColor)
                );
            }
        }

        // Middle box enemies
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                enemies.add(new Enemy(Enemy.MotionType.LINEAR,
                        (7.5 + 2 * j) * gridBoxSizeX,
                        (9.5 + 2 * i) * gridBoxSizeY,
                        enemySpeed/2,
                        new ArrayList<>(Arrays.asList((7.5 + 2 * j) * gridBoxSizeX, (8.5 + 2 * j) * gridBoxSizeX, (8.5 + 2 * j) * gridBoxSizeX, (7.5 + 2 * j) * gridBoxSizeX)),
                        new ArrayList<>(Arrays.asList((9.5 + 2 * i) * gridBoxSizeY, (9.5 + 2 * i) * gridBoxSizeY, (10.5 + 2 * i) * gridBoxSizeY, (10.5 + 2 * i) * gridBoxSizeY)),
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

        // go to level 1 if player goes off the left of the screen
        if (player.getX() < 0) {
            player.setPosition(w, player.getY());
            for (Checkpoint cp : checkpoints)
                cp.playerInsideLastFrame = false;
            game.setScene(game.level1, player);
        }
        // go to level 3 if player goes off the bottom of the screen
        if (player.getY() > h) {
            player.setPosition(player.getX(), 0);
            for (Checkpoint cp : checkpoints)
                cp.playerInsideLastFrame = false;
            game.setScene(game.level3, player);
        }
    }
}