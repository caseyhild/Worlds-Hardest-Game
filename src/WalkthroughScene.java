import java.awt.*;
import java.util.List;
import java.util.ArrayList;

public class WalkthroughScene implements Scene {
    private final WorldsHardestGame game;
    private LevelScene level;
    private final Player player;
    private List<String> movementDirections;
    private List<Integer> movementDurations;
    private int moveIndex;
    private int moveTimeRemaining;

    public WalkthroughScene(WorldsHardestGame game, LevelScene level) {
        this.game = game;
        this.level = level;
        player = level.getPlayer();
        if (level instanceof Level1Scene)
            player.setPosition(3 * level.gridBoxSizeX, 3 * level.gridBoxSizeY);
        if (level instanceof Level2Scene)
            player.setPosition(0, 17 * level.gridBoxSizeY);
        if (level instanceof Level3Scene)
            player.setPosition(17 * level.gridBoxSizeX, 0);
        if (level instanceof Level4Scene)
            player.setPosition(level.w, 17 * level.gridBoxSizeY);

        createMovementPattern();
        moveIndex = 0;
        moveTimeRemaining = movementDurations.getFirst();
    }

    private void upLeft(int duration) {
        movementDirections.add("up left");
        movementDurations.add(duration);
    }
    private void up(int duration) {
        movementDirections.add("up");
        movementDurations.add(duration);
    }
    private void upRight(int duration) {
        movementDirections.add("up right");
        movementDurations.add(duration);
    }
    private void left(int duration) {
        movementDirections.add("left");
        movementDurations.add(duration);
    }
    private void stay(int duration) {
        movementDirections.add("stay");
        movementDurations.add(duration);
    }
    private void right(int duration) {
        movementDirections.add("right");
        movementDurations.add(duration);
    }
    private void downLeft(int duration) {
        movementDirections.add("down left");
        movementDurations.add(duration);
    }
    private void down(int duration) {
        movementDirections.add("down");
        movementDurations.add(duration);
    }
    private void downRight(int duration) {
        movementDirections.add("down right");
        movementDurations.add(duration);
    }

    private void createMovementPattern() {
        movementDirections = new ArrayList<>();
        movementDurations = new ArrayList<>();
        switch (level) {
            case Level1Scene ignored -> {
                createMovementLevel1();
                createMovementLevel2();
                createMovementLevel3();
                createMovementLevel4();
            }
            case Level2Scene ignored -> {
                createMovementLevel2();
                createMovementLevel3();
                createMovementLevel4();
            }
            case Level3Scene ignored -> {
                createMovementLevel3();
                createMovementLevel4();
            }
            case Level4Scene ignored -> createMovementLevel4();
            default -> {}
        }
    }

    private void createMovementLevel1() {
        downRight(25);
        down(20);
        stay(45);
        down(20);
        stay(30);
        right(20);
        stay(30);
        right(20);
        down(10);
        stay(20);
        left(20);
        stay(30);
        left(30);
        stay(70);
        down(60);
        left(10);
        right(10);
        up(40);
        stay(80);
        up(30);
        stay(20);
        up(50);
        right(30);
        stay(60);
        right(30);
        stay(10);
        right(60);
        up(10);
        down(10);
        left(60);
        down(10);
        stay(70);
        down(20);
        stay(30);
        down(30);
        stay(20);
        down(20);
        stay(30);
        down(30);
        stay(60);
        right(50);
        downRight(15);
        right(31);
    }

    private void createMovementLevel2() {
        right(15);
        up(25);
        stay(24);
        up(50);
        stay(10);
        up(10);
        stay(30);
        up(10);
        stay(25);
        up(60);
        stay(55);
        right(50);
        up(10);
        stay(30);
        down(10);
        right(10);
        for (int i = 0; i < 8; i++) {
            stay(50);
            down(10);
        }
        for (int i = 0; i < 2; i++) {
            right(10);
            stay(10);
            down(10);
            stay(10);
            left(10);
            stay(10);
            down(10);
        }
        right(10);
        stay(10);
        down(30);
        right(30);
        up(10);
        stay(50);
        up(20);
        stay(10);
        for (int i = 0; i < 2; i++) {
            stay(10);
            right(10);
            stay(10);
            up(10);
            stay(10);
            left(10);
            stay(10);
            up(10);
        }
        right(10);
        up(20);
        for (int i = 0; i < 6; i++) {
            stay(50);
            up(10);
        }
        right(10);
        up(10);
        stay(50);
        down(10);
        stay(30);
        right(50);
        stay(20);
        down(60);
        for(int i = 0; i < 4; i++) {
            stay(30);
            down(10);
        }
        down(50);
        downLeft(15);
        down(21);
    }

    private void createMovementLevel3() {
        down(20);
        left(30);
        stay(50);
        up(20);
        stay(20);
        up(20);
        stay(80);
        down(20);
        stay(100);
        right(30);
        down(30);
        stay(30);
        left(20);
        down(20);
        stay(140);
        left(10);
        stay(300);
        right(5);
        down(20);
        right(30);
        right(80);
        up(10);
        down(10);
        left(10);
        left(60);
        stay(5);
        left(40);
        up(15);
        left(80);
        upLeft(20);
        up(120);
        upRight(20);
        right(80);
        downRight(20);
        down(50);
        downRight(10);
        right(50);
        stay(20);
        right(20);
        upRight(10);
        up(30);
        stay(10);
        up(20);
        upRight(20);
        right(50);
        stay(10);
        right(20);
        downRight(10);
        down(50);
        for (int i = 0; i < 2; i++) {
            stay(30);
            down(30);
        }
        downRight(10);
        right(50);
        stay(30);
        right(31);
        upRight(10);
        up(49);
        upRight(8);
        right(27);
        downRight(11);
        stay(10);
        down(50);
        stay(45);
        down(80);
        downLeft(20);
        stay(60);
        left(150);
        upLeft(15);
        left(1);
    }

    private void createMovementLevel4() {
        upLeft(5);
        left(60);
        if (!(level instanceof Level4Scene)) {
            stay(20);
            left(30);
            stay(20);
        } else {
            left(30);
        }
        left(70);
        stay(30);
        up(70);
        stay(30);
        right(70);
        stay(30);
        down(10);
        downRight(30);
        right(30);
        left(30);
        if (!(level instanceof Level4Scene))
            stay(30);
        left(10);
        down(10);
        downLeft(20);
        if (level instanceof Level4Scene)
            stay(30);
        left(70);
        stay(30);
        up(70);
        stay(30);
        right(70);
        stay(30);
        if (level instanceof Level4Scene)
            stay(20);
        upRight(10);
        up(30);
    }

    @Override
    public void update() {
        level.update();

        double px = player.getX();
        double py = player.getY();

        if (moveIndex < movementDirections.size()) {
            String direction = movementDirections.get(moveIndex);

            // apply movement for this frame
            switch (direction) {
                case "up left":
                    px -= level.playerSpeed;
                    py -= level.playerSpeed;
                    break;
                case "up":
                    py -= level.playerSpeed;
                    break;
                case "up right":
                    px += level.playerSpeed;
                    py -= level.playerSpeed;
                    break;
                case "left":
                    px -= level.playerSpeed;
                    break;
                case "stay":
                    // do nothing
                    break;
                case "right":
                    px += level.playerSpeed;
                    break;
                case "down left":
                    px -= level.playerSpeed;
                    py += level.playerSpeed;
                    break;
                case "down":
                    py += level.playerSpeed;
                    break;
                case "down right":
                    px += level.playerSpeed;
                    py += level.playerSpeed;
                    break;
            }

            // decrement timer for this move
            moveTimeRemaining--;

            // if this move is done, go to the next one
            if (moveTimeRemaining <= 0) {
                moveIndex++;
                if (moveIndex < movementDurations.size())
                    moveTimeRemaining = movementDurations.get(moveIndex);
            }
        }

        // finally update the player's position
        player.setPosition(px, py);

        // switch to next level if needed
        checkSwitchLevel();
    }

    public void checkSwitchLevel() {
        int w = game.getScreenWidth();
        int h = game.getScreenHeight();

        // go to level 2 if player goes off the right of the screen
        if (level instanceof Level1Scene && player.getX() > w) {
            player.setPosition(0, player.getY());
            level = game.level2;
            game.level2.setPlayer(player);
        }
        // go to level 1 if player goes off the left of the screen
        if (level instanceof Level2Scene && player.getX() < 0) {
            player.setPosition(w, player.getY());
            level = game.level1;
            game.level1.setPlayer(player);
        }
        // go to level 3 if player goes off the bottom of the screen
        if (level instanceof Level2Scene && player.getY() > h) {
            player.setPosition(player.getX(), 0);
            level = game.level3;
            game.level3.setPlayer(player);
        }
        // go to level 2 if player goes off the top of the screen
        if (level instanceof Level3Scene && player.getY() < 0) {
            player.setPosition(player.getX(), h);
            level = game.level2;
            game.level2.setPlayer(player);
        }
        // go to level 4 if player goes off the left of the screen
        if (level instanceof Level3Scene && player.getX() < 0) {
            player.setPosition(w, player.getY());
            level = game.level4;
            game.level4.setPlayer(player);
        }
        // go to level 3 if player goes off the right of the screen
        if (level instanceof Level4Scene && player.getX() > w) {
            player.setPosition(0, player.getY());
            level = game.level3;
            game.level3.setPlayer(player);
        }
        // end walkthrough if last checkpoint is reached
        if (level instanceof Level4Scene && player.getX() < 0.65 * w && player.getY() < 0.3 * h)
            game.setScene(new WalkthroughEndScene(game));
    }

    @Override
    public void render(Graphics2D g) {
        level.render(g);
    }
}