import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferStrategy;

public class WorldsHardestGame extends JFrame implements Runnable, MouseListener, MouseMotionListener, KeyListener {
    private final int width;
    private final int height;
    private final Thread thread;
    private boolean running;

    // mouse state
    private int mouseX, mouseY;
    private boolean mousePressed;

    // keys pressed
    public final boolean[] keys = new boolean[256];

    // last KeyEvent
    public KeyEvent key = new KeyEvent(new JFrame(), 0, 0, 0, 0, KeyEvent.CHAR_UNDEFINED);

    // scene
    private Scene currentScene;
    public Level1Scene level1;
    public Level2Scene level2;
    public Level3Scene level3;
    public Level4Scene level4;
    private int deaths = 0;

    public WorldsHardestGame() {
        width = height = 800;
        setTitle("World's Hardest Game");
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // inputs
        addMouseListener(this);
        addMouseMotionListener(this);
        addKeyListener(this);
        setFocusable(true);

        setSize(width, height + 28);
        setResizable(true);
        setLocationRelativeTo(null);
        setVisible(true);

        // initial scene
        setScene(new TitleScene(this));

        thread = new Thread(this);
        start();
    }

    // preferred accessors used by scenes:
    public int getScreenWidth() { return width; }
    public int getScreenHeight() { return height; }

    // mouse helpers for scenes
    public int getMouseX() { return mouseX; }
    public int getMouseY() { return mouseY; }
    public boolean isMousePressed() { return mousePressed; }

    public Scene getScene() {
        return currentScene;
    }

    public void setScene(Scene s, Player p) {
        currentScene = s;
        if (s instanceof Level1Scene)
            ((Level1Scene) currentScene).setPlayer(p);
        if (s instanceof Level2Scene)
            ((Level2Scene) currentScene).setPlayer(p);
        if (s instanceof Level3Scene)
            ((Level3Scene) currentScene).setPlayer(p);
        if (s instanceof Level4Scene)
            ((Level4Scene) currentScene).setPlayer(p);
    }

    public void setScene(Scene s) {
        this.currentScene = s;
    }

    // deaths counter access
    public void addDeath() { deaths++; }
    public int getDeaths() { return deaths; }
    public void resetDeaths() { deaths = 0; }

    private synchronized void start() {
        running = true;
        thread.start();
    }

    private void update() {
        if (currentScene != null)
            currentScene.update();
    }

    private void render() {
        BufferStrategy bs = getBufferStrategy();
        if (bs == null) {
            createBufferStrategy(3);
            return;
        }

        Graphics g = bs.getDrawGraphics();
        Graphics2D g2 = (Graphics2D) g.create();
        g2.translate(0, 28);

        // clear full canvas
        g2.setColor(new Color(28, 28, 30));
        g2.fillRect(0, 0, width, height);

        // draw current scene
//        System.out.println(currentScene.getClass().getName());
        if (currentScene != null)
            currentScene.render(g2);

        bs.show();
    }

    public void run() {
        long lastTime = System.nanoTime();
        final double ns = 1000000000.0 / 60.0;
        double delta = 0;
        requestFocusInWindow();
        while (running) {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            while (delta >= 1) {
                update();
                delta--;
            }
            render();
        }
    }

    // mouse events
    public void mouseClicked(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}

    public void mousePressed(MouseEvent e) {
        mousePressed = true;
        mouseX = e.getX() - 1;
        mouseY = e.getY() - 31;
    }

    public void mouseReleased(MouseEvent e) {
        mousePressed = false;
        mouseX = e.getX() - 1;
        mouseY = e.getY() - 31;
    }

    public void mouseDragged(MouseEvent e) {
        mousePressed = true;
        mouseX = e.getX() - 1;
        mouseY = e.getY() - 31;
    }

    public void mouseMoved(MouseEvent e) {
        mouseX = e.getX() - 1;
        mouseY = e.getY() - 31;
    }

    // keyboard events
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();
        if (code >= 0 && code < keys.length) keys[code] = true;
        key = e;
    }

    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();
        if (code >= 0 && code < keys.length) keys[code] = false;
        key = e;
    }

    public void keyTyped(KeyEvent e) {
        key = e;
    }

    public static void main(String[] args) {
        new WorldsHardestGame();
    }
}