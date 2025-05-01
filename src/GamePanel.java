import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.imageio.ImageIO;
import javax.swing.Timer;

public class GamePanel extends JPanel implements ActionListener, MouseMotionListener {
    Timer timer;
    ArrayList<Fruit> fruits;
    Random rand = new Random();
    int score = 0;
    int highScore = 0;
    boolean gameOver = false;
    Image background;

    public GamePanel() {
        this.setFocusable(true);
        this.addMouseMotionListener(this);
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (gameOver) {
                    int mx = e.getX();
                    int my = e.getY();
                    if (mx >= 310 && mx <= 490 && my >= 400 && my <= 440) {
                        resetGame();
                    }
                }
            }
        });

        fruits = new ArrayList<>();

        try {
            background = ImageIO.read(new File("./background.png"));
        } catch (IOException e) {
            background = null;
        }

        loadHighScore();

        timer = new Timer(30, this);
        timer.start();

        new Timer(200, _ -> {
            if (!gameOver) spawnFruit();
        }).start();
    }

    public void spawnFruit() {
        fruits.add(new Fruit(rand.nextInt(750), 0));
    }

    public void loadHighScore() {
        try (BufferedReader br = new BufferedReader(new FileReader("./highscore.txt"))) {
            highScore = Integer.parseInt(br.readLine().trim());
        } catch (Exception e) {
            highScore = 0;
        }
    }

    public void saveHighScore() {
        if (score > highScore) {
            try (PrintWriter pw = new PrintWriter("./highscore.txt")) {
                pw.println(score);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void resetGame() {
        gameOver = false;
        score = 0;
        fruits.clear();
        timer.start();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (background != null) {
            g.drawImage(background, 0, 0, getWidth(), getHeight(), null);
        } else {
            g.setColor(Color.LIGHT_GRAY);
            g.fillRect(0, 0, getWidth(), getHeight());
        }

        for (Fruit fruit : fruits) {
            fruit.draw(g);
        }

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 24));
        g.drawString("Score: " + score, 20, 30);
        g.drawString("High Score: " + highScore, 20, 60);

        if (gameOver) {
            g.setFont(new Font("Arial", Font.BOLD, 48));
            g.setColor(Color.white);
            g.fillRect(200, 240, 390, 80);
            g.drawRect(200, 240, 390, 80);
            g.setColor(Color.BLACK);
            g.drawString("GAME OVER", 250, 300);

            g.setFont(new Font("Arial", Font.BOLD, 30));
            g.setColor(Color.gray);
            g.drawString("Final Score: " + score, 300, 350);
            g.setFont(new Font("Arial", Font.BOLD, 24));
            g.setColor(Color.black);
            g.fillRect(295, 400, 220, 40);
            g.drawRect(295, 400, 220, 40);
            g.setColor(Color.white);
            g.drawString("Click to Replay", 320, 428);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!gameOver) {
            Iterator<Fruit> it = fruits.iterator();
            while (it.hasNext()) {
                Fruit fruit = it.next();
                fruit.update();
                if (fruit.y > getHeight()) {
                    it.remove();
                }
            }
            repaint();
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (gameOver) return;

        int mx = e.getX();
        int my = e.getY();

        for (Fruit fruit : fruits) {
            if (!fruit.sliced && fruit.contains(mx, my)) {
                fruit.slice();
                if (fruit.type.equals("bomb")) {
                    SoundPlayer.play("./bomb.wav");
                    repaint();

                        gameOver = true;
                        saveHighScore();
                        timer.stop();}
               else {
                    score += 10;
                    SoundPlayer.play("./slice.wav");
                }
            }
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {}
}
