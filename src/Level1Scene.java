import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public class Level1Scene extends LevelScene implements Scene {
    public Level1Scene(WorldsHardestGame game) {
        super(game);

        // Spawn player at top left checkpoint
        playerColor = new Color(255, 0, 0);
        player = new Player(game, 3 * gridBoxSizeX, 3 * gridBoxSizeY, playerSize, playerSpeed, playerColor);

        // Enemies
        createEnemies();

        // Walls and color barriers
        wallFillColor = new Color(192, 128, 255);
        wallBorderColor = new Color(128, 0, 255);
        barrierPassColor = new Color(0, 255, 255);
        wallMatrix = new int[][] {
                {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
                {1,0,0,0,0,1,1,1,1,1,1,1,1,1,0,0,0,0,0,1},
                {1,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,1},
                {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
                {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
                {1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
                {1,1,0,0,0,0,0,0,0,0,0,0,0,1,1,1,1,1,1,1},
                {1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1},
                {1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1},
                {1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1},
                {1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1},
                {1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1},
                {1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1},
                {1,1,1,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,1,1},
                {1,0,0,0,0,0,1,0,0,0,0,0,0,0,2,2,2,2,1,1},
                {1,0,0,0,0,0,1,0,0,0,0,0,0,0,2,0,0,0,0,0},
                {1,0,0,0,0,0,1,0,0,0,0,0,0,0,2,0,0,0,0,0},
                {1,0,0,0,0,0,1,0,0,0,0,0,0,0,2,0,0,0,0,0},
                {1,0,0,0,0,0,1,1,1,1,1,1,1,1,1,0,0,0,0,0},
                {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
        };
        buildStaticGeometry();

        // Keys
        keys.add(new Key(10 * gridBoxSizeX, 10 * gridBoxSizeY, 0.75 * Math.min(gridBoxSizeX, gridBoxSizeY), keyColor));
        keys.add(new Key(3.5 * gridBoxSizeX, 16.5 * gridBoxSizeY, 0.75 * Math.min(gridBoxSizeX, gridBoxSizeY), keyColor));

        // Gates
        gates.add(new Gate(3 * gridBoxSizeX + 1, 13 * gridBoxSizeY, 3 * gridBoxSizeX - 1, gridBoxSizeY, gateOpenSpeed,
                Gate.Orientation.HORIZONTAL, keys.get(0), gateFillColor, gateBorderColor));
        gates.add(new Gate(13 * gridBoxSizeX, 3 * gridBoxSizeY + 1, 3 * gridBoxSizeY - 1, gridBoxSizeX, gateOpenSpeed,
                Gate.Orientation.VERTICAL, keys.get(1), gateFillColor, gateBorderColor));

        // Paint splotch
        paintSplotchColor = barrierPassColor;
        paintSplotches.add(new PaintSplotch(16.5 * gridBoxSizeX, 3.5 * gridBoxSizeY, 1.25 * Math.min(gridBoxSizeX, gridBoxSizeY), paintSplotchColor));

        // Checkpoints
        checkpoints.add(new Checkpoint(3 * gridBoxSizeX, 3 * gridBoxSizeY, 4 * gridBoxSizeX, 4 * gridBoxSizeY, checkpointColor));
        checkpoints.add(new Checkpoint(17.5 * gridBoxSizeX, 17 * gridBoxSizeY, 5 * gridBoxSizeX, 4 * gridBoxSizeY, checkpointColor));
    }

    // Create enemies
    @Override
    protected void createEnemies() {
        // Enemies: 5 groups in a plus shape
        enemies.clear();
        enemySpeed = 0.1 * Math.min(gridBoxSizeX, gridBoxSizeY);

        int[][] groupCenters = {
                {10 * gridBoxSizeX, 5 * gridBoxSizeY},
                {5 * gridBoxSizeX, 10 * gridBoxSizeY},
                {10 * gridBoxSizeX, 10 * gridBoxSizeY},
                {15 * gridBoxSizeX, 10 * gridBoxSizeY},
                {10 * gridBoxSizeX, 15 * gridBoxSizeY}
        };

        // box sizes
        double halfPathX = 2.5 * gridBoxSizeX;
        double halfPathY = 2.5 * gridBoxSizeY;

        for (int[] gc : groupCenters) {
            double cx = gc[0];
            double cy = gc[1];

            double topY = snapToGridY(cy - 1.5 * gridBoxSizeY);
            double bottomY = snapToGridY(cy + 1.5 * gridBoxSizeY);
            double leftX = snapToGridX(cx - 1.5 * gridBoxSizeX);
            double rightX = snapToGridX(cx + 1.5 * gridBoxSizeX);

            double pathStartX = snapToGridX(cx - halfPathX);
            double pathEndX   = snapToGridX(cx + halfPathX);
            double pathStartY = snapToGridY(cy - halfPathY);
            double pathEndY   = snapToGridY(cy + halfPathY);

            enemies.add(new Enemy(Enemy.MotionType.LINEAR,
                    pathStartX,
                    topY,
                    enemySpeed,
                    new ArrayList<>(Arrays.asList(pathStartX, pathEndX)),
                    new ArrayList<>(Arrays.asList(topY, topY)),
                    enemySize,
                    enemyColor)
            );
            enemies.add(new Enemy(Enemy.MotionType.LINEAR,
                    rightX,
                    pathStartY,
                    enemySpeed,
                    new ArrayList<>(Arrays.asList(rightX, rightX)),
                    new ArrayList<>(Arrays.asList(pathStartY, pathEndY)),
                    enemySize,
                    enemyColor)
            );
            enemies.add(new Enemy(Enemy.MotionType.LINEAR,
                    pathEndX,
                    bottomY,
                    enemySpeed,
                    new ArrayList<>(Arrays.asList(pathEndX, pathStartX)),
                    new ArrayList<>(Arrays.asList(bottomY, bottomY)),
                    enemySize,
                    enemyColor)
            );
            enemies.add(new Enemy(Enemy.MotionType.LINEAR,
                    leftX,
                    pathEndY,
                    enemySpeed,
                    new ArrayList<>(Arrays.asList(leftX, leftX)),
                    new ArrayList<>(Arrays.asList(pathEndY, pathStartY)),
                    enemySize,
                    enemyColor)
            );
        }
    }

    @Override
    protected void checkSetNewLevel() {
        // if performing walkthrough, let it control scene management
        if (game.getScene() instanceof WalkthroughScene)
            return;

        // go to level 2 if player goes off the right of the screen
        if (player.getX() > w) {
            player.setPosition(0, player.getY());
            for (Checkpoint cp : checkpoints)
                cp.playerInsideLastFrame = false;
            game.setScene(game.level2, player);
        }
    }
}
