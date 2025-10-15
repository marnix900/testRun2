import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class MilkCatcherGame extends JPanel implements ActionListener, KeyListener {

    private final int WIDTH = 500;
    private final int HEIGHT = 700;
    private final int SPEED = 4;

    private int cookieX = WIDTH / 2 - 40;
    private int score = 0;
    private int missed = 0;

    private Timer timer;
    private Random rand = new Random();

    private java.util.List<Point> milkDrops = new ArrayList<>();

    // Images
    private Image cookieImg;
    private Image milkImg;
    private Image backgroundImg;

    public MilkCatcherGame() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));

        // Load images
        cookieImg = new ImageIcon("cookie.png").getImage()
            .getScaledInstance(80, 50, Image.SCALE_SMOOTH);
        milkImg = new ImageIcon("milk.png").getImage()
            .getScaledInstance(25, 25, Image.SCALE_SMOOTH);

        // Load and resize background to fill window
        backgroundImg = new ImageIcon("background.png").getImage()
            .getScaledInstance(WIDTH, HEIGHT, Image.SCALE_SMOOTH);

        timer = new Timer(30, this);
        timer.start();

        addKeyListener(this);
        setFocusable(true);
    }

    public void actionPerformed(ActionEvent e) {
        if (rand.nextInt(15) == 0) {
            milkDrops.add(new Point(rand.nextInt(WIDTH - 25), 0));
        }

        Iterator<Point> it = milkDrops.iterator();
        while (it.hasNext()) {
            Point p = it.next();
            p.y += SPEED;

            Rectangle dropRect = new Rectangle(p.x, p.y, 25, 25);
            Rectangle cookieRect = new Rectangle(cookieX, HEIGHT - 60, 80, 50);

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
        super.paintComponent(g);

        // 🌄 Draw background first
        g.drawImage(backgroundImg, 0, 0, this);

        // 🥛 Draw milk drops
        for (Point p : milkDrops) {
            g.drawImage(milkImg, p.x, p.y, this);
        }

        // 🍪 Draw cookie
        g.drawImage(cookieImg, cookieX, HEIGHT - 60, this);

        // 🧾 Draw score
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("Score: " + score, 20, 30);
        g.drawString("Missed: " + missed, 20, 60);

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
        if (key == KeyEvent.VK_RIGHT && cookieX < WIDTH - 80) {
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
