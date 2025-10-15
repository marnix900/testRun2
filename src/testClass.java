import java.awt.event.*;
import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;

public class testClass {

    private CookieCounter cookie;
    private static final int maxCookies = 3;
    private static final int fallSpeed = 5;

    public testClass(CookieCounter cookie) {
        this.cookie = cookie;
    }

    private static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("Cookie Clicker");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1062, 706);
        frame.setLocation(100, 100);

        JLabel label = new JLabel("Cookie Clicker");
        frame.getContentPane().add(label, BorderLayout.NORTH);

        ImagePanel panel = new ImagePanel("/images/blueSkyBackground.jpeg", Color.BLACK, 662, 706);
        frame.add(panel, BorderLayout.WEST);
        panel.setLayout(null);

        ImagePanel storePanel = new ImagePanel("/images/wood.jpg", Color.BLACK, 400, 706);
        frame.add(storePanel, BorderLayout.EAST);
        Upgrade cursorUpgrade = new Upgrade("/images/CursorUpgrade.png");
        cursorUpgrade.setLocation(100, 100);
        storePanel.add(cursorUpgrade);

        CookieCounter counter = new CookieCounter(0);

        ActionListener upgradeListener = e -> {
            Upgrade upgradeClicked = (Upgrade) e.getSource();
            if ((counter.getCookie() >= 10) && (!cursorUpgrade.getUpgraded())) {
                counter.setCookie(counter.getCookie() - 10);
                counter.setIncrement(counter.getIncrement()*2);
                label.setText("Cookie count: " + counter.getCookie());
                System.out.println("Upgrade purchased!");
                cursorUpgrade.setUpgraded(true);
            } if (cursorUpgrade.getUpgraded()) {
                System.out.println("Upgrade already purchased!");
            } else {
                System.out.println("Upgrade failed!");
            }
        };

        cursorUpgrade.addActionListener(upgradeListener);

        //initial cookies
        ArrayList<Cookie> cookies = new ArrayList<>();
        while (cookies.size() < maxCookies) {
            Cookie cookie = new Cookie("/images/cookie.png", "Cookie1");
            cookie.setLocation(cookie.random(), 0);
            cookies.add(cookie);
            panel.add(cookie);
            cookie.addActionListener(e -> {
                Cookie clicked = (Cookie) e.getSource();
                panel.remove(clicked);
                cookies.remove(clicked);
                counter.addCookie();
                label.setText("Cookie count: " + counter.getCookie());
                panel.repaint();
                spawnCookie(cookies, panel, counter, label); // ✅ recursive call
            });
        }

        Timer fallTimer = new Timer(20, e -> {
            ArrayList<Cookie> toRemove = new ArrayList<>();

            for (Cookie cookie : new ArrayList<>(cookies)) {
                Point p = cookie.getLocation();
                p.y += fallSpeed;
                cookie.setLocation(p);

                if (p.y > panel.getHeight()) {
                    toRemove.add(cookie);
                }
            }

            for (Cookie c : toRemove) {
                panel.remove(c);
                cookies.remove(c);
                spawnCookie(cookies, panel, counter, label);
            }

            panel.repaint();
        });
        fallTimer.start();

        frame.setVisible(true);
    }

    private static void spawnCookie(
            ArrayList<Cookie> cookies, JPanel panel, CookieCounter counter, JLabel label
    ) {
        if (cookies.size() < maxCookies) {
            Cookie cookie = new Cookie("/images/cookie.png", "Cookie" + (cookies.size() + 1));
            int startX = (int)(Math.random() * (panel.getWidth() - 150));
            cookie.setLocation(startX, 0);
            panel.add(cookie);
            panel.repaint();

            cookie.addActionListener(e -> {
                Cookie clicked = (Cookie) e.getSource();
                panel.remove(clicked);
                cookies.remove(clicked);
                counter.addCookie();
                label.setText("Cookie count: " + counter.getCookie());
                panel.repaint();
                spawnCookie(cookies, panel, counter, label); // ✅ recursive call
            });

            cookies.add(cookie);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> createAndShowGUI());
    }
}