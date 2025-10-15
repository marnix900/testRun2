import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import javax.imageio.ImageIO;

public class MilkCatcherGame extends JPanel implements ActionListener, KeyListener {

    private final int WIDTH = 500;
    private final int HEIGHT = 700;
    private final int SPEED = 5;

    private int cookieX = WIDTH / 2 - 40;
    private int score = 0;
    private int missed = 0;

    private Timer timer;
    private Random rand = new Random();

    private java.util.List<Point> milkDrops = new ArrayList<>();

    // Images
    private BufferedImage cookieImg;
    private BufferedImage milkImg;
    private BufferedImage backgroundImg;

    public MilkCatcherGame() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setFocusable(true);
        addKeyListener(this);

        try {
            // Load images using BufferedImage
            cookieImg = ImageIO.read(new File("cookie.png"));
            milkImg = ImageIO.read(new File("milk.png"));
            backgroundImg = ImageIO.read(new File("background.png"));

            // Scale images
            cookieImg = resize(cookieImg, 80, 50);
            milkImg = resize(milkImg, 25, 25);
            backgroundImg = resize(backgroundImg, WIDTH, HEIGHT);

        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        timer = new Timer(30, this);
        timer.start();
    }

    private BufferedImage resize(BufferedImage img, int w, int h) {
        Image tmp = img.getScaledInstance(w, h, Image.SCALE_SMOOTH);
        BufferedImage resized = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = resized.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();
        return resized;
    }

    public void actionPerformed(ActionEvent e) {
        // Add milk drops
        if (rand.nextInt(15) == 0) {
            milkDrops.add(new Point(rand.nextInt(WIDTH - milkImg.getWidth()), 0));
        }

        // Move drops and check collisions
        Iterator<Point> it = milkDrops.iterator();
        while (it.hasNext()) {
            Point p = it.next();
            p.y += SPEED;

            Rectangle dropRect = new Rectangle(p.x, p.y, milkImg.getWidth(), milkImg.getHeight());
            Rectangle cookieRect = new Rectangle(cookieX, HEIGHT - cookieImg.getHeight() - 10,
                    cookieImg.getWidth(), cookieImg.getHeight());

            if (dropRect.intersects(cookieRect)) {
                score++;
                it.remove();
            } else if (p.y > HEIGHT) {
                missed++;
                it.remove();
            }
        }

        repaint();
    }

    protected void paintComponent(Graphics g) {
        // Draw the background first
        g.drawImage(backgroundImg, 0, 0, null);

        // Draw milk drops
        for (Point p : milkDrops) {
            g.drawImage(milkImg, p.x, p.y, null);
        }

        // Draw cookie
        g.drawImage(cookieImg, cookieX, HEIGHT - cookieImg.getHeight() - 10, null);

        // Draw score
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("Score: " + score, 20, 30);
        g.drawString("Missed: " + missed, 20, 60);

        // Game over
        if (missed >= 10) {
            g.setFont(new Font("Arial", Font.BOLD, 40));
            g.drawString("Game Over!", WIDTH / 2 - 120, HEIGHT / 2);
            timer.stop();
        }
    }

    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_LEFT && cookieX > 0) {
            cookieX -= 20;
        }
        if (key == KeyEvent.VK_RIGHT && cookieX < WIDTH - cookieImg.getWidth()) {
            cookieX += 20;
        }

        if (key == KeyEvent.VK_SPACE && missed >= 10) {
            score = 0;
            missed = 0;
            milkDrops.clear();
            timer.start();
        }
    }

    public void keyReleased(KeyEvent e) {}
    public void keyTyped(KeyEvent e) {}

    public static void main(String[] args) {
        JFrame frame = new JFrame("Cookie Catcher 🍪🥛");
        MilkCatcherGame game = new MilkCatcherGame();
        frame.add(game);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setResizable(false);
    }
}
