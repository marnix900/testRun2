import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import javax.imageio.ImageIO;
import javax.swing.*;

public class MilkCatcherGame extends JPanel implements ActionListener, KeyListener {

    private final int width = 862;
    private final int height = 706;
    private final int speed = 5;
    private final int gameDuration = 20000; // 20 seconds

    private int cookieX = width / 2 - 40;
    private int score = 0;

    private javax.swing.Timer timer;
    private long startTime;
    private boolean gameOver = false;

    private Random rand = new Random();
    private java.util.List<Point> milkDrops = new ArrayList<>();
    private java.util.List<Point> obstacles = new ArrayList<>();
    private java.util.List<Particle> particles = new ArrayList<>();

    private BufferedImage cookieImg;
    private BufferedImage milkImg;
    private BufferedImage backgroundImg;
    private BufferedImage obstacleImg;  // obstacle image

    private boolean doublePoints = false;
    private long doublePointsEndTime = 0;

    private String[] funMessages = {
        "Yum!", "Delicious!", "Nice catch!", "Milk + Cookie = ❤️", "Cheese?! Wait, no..."
    };

    private String hitMessage = ""; // message when hitting obstacle
    private long messageEndTime = 0; // message duration

    public MilkCatcherGame() {
        setPreferredSize(new Dimension(width, height));
        setFocusable(true);
        addKeyListener(this);

        try {
            cookieImg = ImageIO.read(getClass().getResource("/images/cookie.png"));
            milkImg = ImageIO.read(getClass().getResource("/images/milk.png"));
            backgroundImg = ImageIO.read(getClass().getResource("/images/background.png"));
            obstacleImg = ImageIO.read(
            getClass().getResource("/images/obstacle.png")  // load obstacle image
            );

            cookieImg = resize(cookieImg, 80, 50);
            milkImg = resize(milkImg, 25, 25);
            backgroundImg = resize(backgroundImg, width, height);
            obstacleImg = resize(obstacleImg, 30, 30); // resize obstacle
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        timer = new javax.swing.Timer(30, this);
        startGame();
    }

    public void startGame() {
        score = 0;
        cookieX = width / 2 - 40;
        milkDrops.clear();
        obstacles.clear();
        particles.clear();
        startTime = System.currentTimeMillis();
        gameOver = false;
        doublePoints = false;
        hitMessage = "";
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
        if (gameOver) {
            return;
        }

        long elapsed = System.currentTimeMillis() - startTime;
        if (elapsed >= gameDuration) {
            gameOver = true;
            timer.stop();
            repaint();
            return;
        }

        if (rand.nextInt(15) == 0) {
            milkDrops.add(new Point(rand.nextInt(width - milkImg.getWidth()), 0));
        }

        if (rand.nextInt(80) == 0) {
            obstacles.add(new Point(rand.nextInt(width - 30), 0));
        }

        Iterator<Point> milkIt = milkDrops.iterator();
        while (milkIt.hasNext()) {
            Point p = milkIt.next();
            p.y += speed;

            Rectangle dropRect = new Rectangle(p.x, p.y, milkImg.getWidth(), milkImg.getHeight());
            Rectangle cookieRect = new Rectangle(cookieX, height - cookieImg.getHeight() - 10,
                    cookieImg.getWidth(), cookieImg.getHeight());

            if (dropRect.intersects(cookieRect)) {
                score += doublePoints ? 2 : 1;
                milkIt.remove();
                spawnParticles(p.x + milkImg.getWidth() / 2, p.y + milkImg.getHeight() / 2);

                if (rand.nextInt(5) == 0) {
                    System.out.println(funMessages[rand.nextInt(funMessages.length)]);
                }

                if (rand.nextInt(10) == 0) {
                    doublePoints = true;
                    doublePointsEndTime = System.currentTimeMillis() + 5000;
                }
            } else if (p.y > height) {
                milkIt.remove();
            }
        }

        Iterator<Point> obsIt = obstacles.iterator();
        while (obsIt.hasNext()) {
            Point o = obsIt.next();
            o.y += speed + 2;

            Rectangle obsRect = new Rectangle(o.x, o.y, 30, 30);
            Rectangle cookieRect = new Rectangle(cookieX, height - cookieImg.getHeight() - 10,
                    cookieImg.getWidth(), cookieImg.getHeight());

            if (obsRect.intersects(cookieRect)) {
                score = Math.max(0, score - 5);
                obsIt.remove();

                // Show hit message
                hitMessage = "Ouch! -5 points!";
                messageEndTime = System.currentTimeMillis() + 1500;
            } else if (o.y > height) {
                obsIt.remove();
            }
        }

        Iterator<Particle> partIt = particles.iterator();
        while (partIt.hasNext()) {
            Particle p = partIt.next();
            p.update();
            if (!p.alive) {
                partIt.remove();
            }
        }

        if (doublePoints && System.currentTimeMillis() > doublePointsEndTime) {
            doublePoints = false;
        }

        repaint();
    }

    private void spawnParticles(int x, int y) {
        for (int i = 0; i < 10; i++) {
            particles.add(new Particle(x, y));
        }
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundImg, 0, 0, null);

        for (Point p : milkDrops) {
            g.drawImage(milkImg, p.x, p.y, null);
        }

        // Obstacles as images
        for (Point o : obstacles) {
            g.drawImage(obstacleImg, o.x, o.y, null);
        }

        g.drawImage(cookieImg, cookieX, height - cookieImg.getHeight() - 10, null);

        for (Particle p : particles) {
            p.draw(g);
        }

        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("Score: " + score, 20, 30);

        if (doublePoints) {
            g.setColor(Color.RED);
            g.drawString("DOUBLE POINTS!", 20, 60);
        } else {
            g.setColor(Color.BLACK);
            long timeLeft = Math.max(
                0,
                (gameDuration - (System.currentTimeMillis() - startTime)) / 1000
            );
            g.drawString("Time: " + timeLeft + "s", 20, 60);
        }

        g.setFont(new Font("Arial", Font.PLAIN, 16));
        g.drawString("Use ← → to move", width - 200, 30);
        g.drawString("Press SPACE to restart", width - 220, 55);

        if (!hitMessage.isEmpty() && System.currentTimeMillis() < messageEndTime) {
            g.setFont(new Font("Arial", Font.BOLD, 25));
            g.setColor(Color.RED);
            g.drawString(hitMessage, width / 2 - 80, height / 2 + 100);
        } else if (System.currentTimeMillis() >= messageEndTime) {
            hitMessage = "";
        }

        if (gameOver) {
            g.setFont(new Font("Arial", Font.BOLD, 40));
            g.setColor(new Color(0, 0, 0, 180));
            g.fillRect(0, height / 2 - 60, width, 120);

            g.setColor(Color.WHITE);
            g.drawString("Time’s up!", width / 2 - 100, height / 2 - 10);
            g.setFont(new Font("Arial", Font.PLAIN, 25));
            g.drawString("Final Score: " + score, width / 2 - 90, height / 2 + 30);
            g.drawString("Press SPACE to continue...", width / 2 - 130, height / 2 + 60);
            CookieClicker.milkCount = score;
        }
    }

    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (!gameOver) {
            if (key == KeyEvent.VK_LEFT && cookieX > 0) {
                cookieX -= 20;
            }
            if (key == KeyEvent.VK_RIGHT && cookieX < width - cookieImg.getWidth()) {
                cookieX += 20;
            }
        }
        if (key == KeyEvent.VK_SPACE && gameOver) {
            CookieClicker.miniGameFinished = true;
        }
    }

    public void keyReleased(KeyEvent e) {}
    
    public void keyTyped(KeyEvent e) {}

    class Particle {
        int x;
        int y;
        int size;
        int dx;
        int dy;
        Color color;
        boolean alive = true;

        public Particle(int x, int y) {
            this.x = x;
            this.y = y;
            size = rand.nextInt(5) + 3;
            dx = rand.nextInt(7) - 3;
            dy = rand.nextInt(3) - 2;
            color = new Color(rand.nextFloat(), rand.nextFloat(), rand.nextFloat());
        }

        void update() {
            x += dx;
            y += dy;
            size--;
            if (size <= 0) {
                alive = false;
            }
        }

        void draw(Graphics g) {
            g.setColor(color);
            g.fillOval(x, y, size, size);
        }
    }
}