import java.awt.event.*;
import java.awt.*;
import javax.swing.*;

public class testClass {

    private CookieCounter cookie;

    public testClass(CookieCounter cookie) {
        this.cookie = cookie;
    }

    public Point point() {
        int x = (int)(Math.random()*101);
        int y = (int)(Math.random()*101);
        System.out.println(x + ' ' + y);
        return new Point(100, 100);
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

        ImagePanel storePanel = new ImagePanel("/images/wood.jpg", Color.BLACK, 400, 706);
        frame.add(storePanel, BorderLayout.EAST);
        Upgrade cursorUpgrade = new Upgrade("/images/CursorUpgrade.png");
        cursorUpgrade.setLocation(100, 100);
        storePanel.add(cursorUpgrade);

        CookieCounter counter = new CookieCounter(0);

        // Create multiple cookies
        Cookie cookie1 = new Cookie("/images/cookie.png", "Cookie1");
        cookie1.setLocation(cookie1.random(), cookie1.random());

        Cookie cookie2 = new Cookie("/images/cookie.png", "Cookie2");
        cookie2.setLocation(cookie2.random(), cookie2.random());

        Cookie cookie3 = new Cookie("/images/cookie.png", "Cookie3");
        cookie3.setLocation(cookie3.random(), cookie3.random());

        panel.add(cookie1);
        panel.add(cookie2);
        panel.add(cookie3);

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

        // Shared listener logic
        ActionListener clickAction = e -> {
            Cookie clicked = (Cookie) e.getSource();
            panel.remove(clicked);
            panel.revalidate();
            panel.repaint();
            counter.addCookie();
            label.setText("Cookie count: " + counter.getCookie());
            panel.add(clicked);
            clicked.setLocation(clicked.random(), clicked.random());
        };

        // Add listeners
        for (Cookie cookie : new Cookie[]{cookie1, cookie2, cookie3}) {
            cookie.addActionListener(clickAction);
            cookie.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    cookie.shrink();
                }
            });
        }

        frame.setVisible(true);
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> createAndShowGUI());
    }
}