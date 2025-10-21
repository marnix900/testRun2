import java.awt.event.*;
import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;

public class testClass {

    private CookieCounter cookie;
    private static final int maxCookies = 3;
    private static final int fallSpeed = 5;
    private static final int CPS = 0; //cookies per second
    private static long g;

    public testClass(CookieCounter cookie) {
        this.cookie = cookie;
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("Cookie Clicker");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1062, 706);
        frame.setLocation(100, 100);

        ImagePanel panel = new ImagePanel("/images/blueSkyBackground.jpeg", Color.BLACK, 662, 706);
        frame.add(panel, BorderLayout.WEST);

        Label label = new Label(
                "<html>Cookie count: 0<br>CPS: 0<br>Countdown: 200</html>",
                Color.GRAY
        );
        label.setBounds(231, 20, 200, 60);
        panel.add(label);

        int spacing = 640 / 10;//hardcoded
        ArrayList<Integer> spawningPoint = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            spawningPoint.add(i*spacing);
        }

        System.out.println("spawning point: " + spawningPoint);

        ImagePanel storePanel = new ImagePanel("/images/wood.jpg", Color.BLACK, 400, 706);
        frame.add(storePanel, BorderLayout.EAST);
        Upgrade cursorUpgrade = new Upgrade("/images/CursorUpgrade.png");
        cursorUpgrade.setLocation(100, 100);
        storePanel.add(cursorUpgrade);

        CookieCounter counter = new CookieCounter(0);
        CPS cps = new CPS(0);
        CountDown time = new CountDown(60);

        ActionListener upgradeListener = e -> {
            Upgrade upgradeClicked = (Upgrade) e.getSource();
            if ((counter.getCookie() >= 10) && (!cursorUpgrade.getUpgraded())) {
                counter.setCookie(counter.getCookie() - 10);
                counter.setIncrement(counter.getIncrement()*2);
                label.setText("<html>Cookie count: " + counter.getCookie() + "<br>CPS: " + cps.getCps() + "<br>Countdown: " + time.getTime() + "</html>");
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
            int randomNumber = cookie.random(spawningPoint);
            int XCord = spawningPoint.get(randomNumber);
            spawningPoint.remove(randomNumber);
            cookie.setX(XCord);
            cookie.setLocation(XCord, -200);
            cookies.add(cookie);
            panel.add(cookie);
            cookie.addActionListener(e -> {
                Cookie clicked = (Cookie) e.getSource();
                panel.remove(clicked);
                cookies.remove(clicked);
                counter.addCookie();
                label.setText("<html>Cookie count: " + counter.getCookie() + "<br>CPS: " + cps.getCps() + "<br>Countdown: " + time.getTime() + "</html>");
                panel.repaint();
                spawningPoint.add(clicked.getX());
                spawnCookie(cookies, panel, counter, label, spawningPoint, cps, time); // ✅ recursive call
            });
        }

        Timer fallTimer = new Timer(20, e -> {
            if ((g*20)%1000 == 0) {
                time.setTime(time.getTime() - 1);
            }
            g = g + 1;
            label.setText("<html>Cookie count: " + counter.getCookie() + "<br>CPS: " + cps.getCps() + "<br>Countdown: " + time.getTime() + "</html>");
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
                spawningPoint.add(c.getX());
                spawnCookie(cookies, panel, counter, label, spawningPoint, cps, time);
            }

            panel.repaint();
        });
        fallTimer.start();

        frame.setVisible(true);
    }

    private static void spawnCookie(
            ArrayList<Cookie> cookies, JPanel panel, CookieCounter counter, JLabel label, ArrayList<Integer> spawningPoint,  CPS cps, CountDown time
    ) {
        if (cookies.size() < maxCookies) {
            Cookie cookie = new Cookie("/images/cookie.png", "Cookie" + (cookies.size() + 1));
            int randomNumber = cookie.random(spawningPoint);
            int XCord = spawningPoint.get(randomNumber);
            spawningPoint.remove(randomNumber);
            cookie.setX(XCord);
            cookie.setLocation(XCord, -200);
            panel.add(cookie);
            panel.repaint();

            cookie.addActionListener(e -> {
                Cookie clicked = (Cookie) e.getSource();
                panel.remove(clicked);
                cookies.remove(clicked);
                counter.addCookie();
                label.setText("<html>Cookie count: " + counter.getCookie() + "<br>CPS: " + cps.getCps() + "<br>Countdown: " + time.getTime() + "</html>");
                panel.repaint();
                spawningPoint.add(cookie.getX());
                spawnCookie(cookies, panel, counter, label, spawningPoint, cps, time); // ✅ recursive call
            });

            cookies.add(cookie);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> createAndShowGUI());
    }
}