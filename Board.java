import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.sound.sampled.*;

public class Board extends JPanel implements ActionListener {

    private Image apple;
    private Image dot;
    private Image head;

    private final int ALL_DOTS = 2500;
    private final int DOT_SIZE = 10;
    private final int RANDOM_POSITION = 29;

    private int apple_x;
    private int apple_y;

    private final int x[] = new int[ALL_DOTS];
    private final int y[] = new int[ALL_DOTS];

    private boolean leftDirection = false;
    private boolean rightDirection = true;
    private boolean upDirection = false;
    private boolean downDirection = false;

    private boolean inGame = true;

    private int dots;
    private Timer timer;

    private Clip gameOverSound;
    private Clip appleEatingSound;

    private int score = 0;

    Board() {
        addKeyListener(new TAdapter());

        setBackground(Color.BLACK);
        setPreferredSize(new Dimension(500, 500));
        setFocusable(true);

        loadImages();
        initialiseGame();
    }

    public void loadImages() {
        ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("icons\\apple.png"));
        apple = i1.getImage();

        ImageIcon i2 = new ImageIcon(ClassLoader.getSystemResource("icons\\dot.png"));
        dot = i2.getImage();

        ImageIcon i3 = new ImageIcon(ClassLoader.getSystemResource("icons\\head.png"));
        head = i3.getImage();
    }

    public void initialiseGame() {
        dots = 3;

        for (int i = 0; i < dots; i++) {
            y[i] = 50;
            x[i] = 50 - i * DOT_SIZE;
        }

        locateApple();

        timer = new Timer(140, this);
        timer.start();
    }

    public void locateApple() {
        int r = (int) (Math.random() * RANDOM_POSITION);
        apple_x = r * DOT_SIZE;

        r = (int) (Math.random() * RANDOM_POSITION);
        apple_y = r * DOT_SIZE;
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        if (inGame) {
            g.drawImage(apple, apple_x, apple_y, this);

            for (int i = 0; i < dots; i++) {
                if (i == 0) {
                    g.drawImage(head, x[i], y[i], this);
                } else {
                    g.drawImage(dot, x[i], y[i], this);
                }
            }

            Toolkit.getDefaultToolkit().sync();
        } else {
            gameOver(g);
            playGameOverSound();
        }
    }

    public void gameOver(Graphics g) {
        String gameOverMsg = "Game Over!";
        String scoreMsg = "Score: " + score;

        Font largeFont = new Font("Georgia", Font.BOLD, 30); // large font for the game over
        Font smallFont = new Font("Arial", Font.BOLD, 20); // Smaller font for the score
        FontMetrics largeFontMetrics = getFontMetrics(largeFont);
        FontMetrics smallFontMetrics = getFontMetrics(smallFont);

        g.setColor(Color.WHITE);

        g.setFont(largeFont);
        // Draw "Game Over!" message
        g.drawString(gameOverMsg, (500 - largeFontMetrics.stringWidth(gameOverMsg)) / 2, 400 / 2);

        g.setFont(smallFont);
        // Draw score below "Game Over!" message
        g.drawString(scoreMsg, (500 - smallFontMetrics.stringWidth(scoreMsg)) / 2, (400 / 2) + 30);
    }

    public void move() {
        for (int i = dots; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }

        if (leftDirection) {
            x[0] = x[0] - DOT_SIZE;
        }
        if (rightDirection) {
            x[0] = x[0] + DOT_SIZE;
        }
        if (upDirection) {
            y[0] = y[0] - DOT_SIZE;
        }
        if (downDirection) {
            y[0] = y[0] + DOT_SIZE;
        }
    }

    public void checkApple() {
        if ((x[0] == apple_x) && (y[0] == apple_y)) {
            dots++;
            score++;
            playAppleEatingSound();
            locateApple();
        }
    }

    public void checkCollision() {
        for (int i = dots; i > 0; i--) {
            if ((i > 4) && (x[0] == x[i]) && (y[0] == y[i])) {
                inGame = false;
            }
        }

        if (y[0] >= 500) {
            inGame = false;
        }
        if (x[0] >= 500) {
            inGame = false;
        }
        if (y[0] < 0) {
            inGame = false;
        }
        if (x[0] < 0) {
            inGame = false;
        }

        if (!inGame) {
            timer.stop();
        }
    }

    public void playGameOverSound() {
        try {
            AudioInputStream audioInputStream = AudioSystem
                    .getAudioInputStream(getClass().getResourceAsStream("music\\gameOverMusic.wav"));
            gameOverSound = AudioSystem.getClip();
            gameOverSound.open(audioInputStream);
            gameOverSound.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void playAppleEatingSound() {
        try {
            AudioInputStream audioInputStream = AudioSystem
                    .getAudioInputStream(getClass().getResourceAsStream("music\\appleEatingMusic.wav"));
            appleEatingSound = AudioSystem.getClip();
            appleEatingSound.open(audioInputStream);
            appleEatingSound.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void actionPerformed(ActionEvent ae) {
        if (inGame) {
            checkApple();
            checkCollision();
            move();
        }
        repaint();
    }

    public class TAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();

            if (key == KeyEvent.VK_LEFT && (!rightDirection)) {
                leftDirection = true;
                upDirection = false;
                downDirection = false;
            }
            if (key == KeyEvent.VK_RIGHT && (!leftDirection)) {
                rightDirection = true;
                upDirection = false;
                downDirection = false;
            }
            if (key == KeyEvent.VK_UP && (!downDirection)) {
                upDirection = true;
                leftDirection = false;
                rightDirection = false;
            }
            if (key == KeyEvent.VK_DOWN && (!upDirection)) {
                downDirection = true;
                leftDirection = false;
                rightDirection = false;
            }
        }
    }
}