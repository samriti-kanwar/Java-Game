import java.awt.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.ImageIO;
import java.util.Random;

public class Fruit {
    int x, y;
    int speed;
    boolean sliced = false;
    String type;
    BufferedImage image, slicedImage;
    static Random rand = new Random();

    public Fruit(int x, int y) {
        this.x = x;
        this.y = y;
        this.speed = 5 + rand.nextInt(5);

        int choice = rand.nextInt(4); // 0–3 to avoid 5 types unless you have more
        switch (choice) {
            case 0 -> type = "watermelon";
            case 1 -> type = "banana";
            case 2 -> type = "orange";
            case 3 -> type = "bomb";
        }

        loadImages();
    }

    private void loadImages() {
        try {
            switch (type) {
                case "watermelon" -> {
                    image = ImageIO.read(new File("./watermelon.png"));
                    slicedImage = ImageIO.read(new File("./watermelon_sliced.png"));
                }
                case "banana" -> {
                    image = ImageIO.read(new File("./banana.png"));
                    slicedImage = ImageIO.read(new File("./banana_sliced.png"));
                }
                case "orange" -> {
                    image = ImageIO.read(new File("./orange.png"));
                    slicedImage = ImageIO.read(new File("./sliced.png"));
                }
                case "bomb" -> {
                    image = ImageIO.read(new File("./bomb.png"));
                    slicedImage = ImageIO.read(new File("./explosion.png"));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
// fruit fall downwards
    public void update() {
        y += speed;
    }

    public void draw(Graphics g) {
        if (image == null || slicedImage == null) return;
        g.drawImage(sliced ? slicedImage : image, x, y, 80,80, null);
    }

    public boolean contains(int mx, int my) {
        return new Rectangle(x, y, 80, 80).contains(mx, my);
    }

    public void slice() {
        sliced = true;
    }
}
