/**
 * Cookie Clicker
 *
 *
 *
 * @author Alex Aichimoaie
 * @id 2339730
 * @author Marnix van den Bosch
 * @id 2293781
 */

import java.awt.event.*;
import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;

public class CookieClicker {

    private CookieCounter cookie;
    private static final int maxCookies = 3;
    private static final int fallSpeed = 5;
    private static final int CPS = 0; //cookies per second
    private static long g;
    private static final int popUpSpeed = 3;
    private static float scale = 1.0f;
    private static boolean growing = true;
    private static boolean startClicked = false;
    private static boolean miniGameStarted = false;
    public static boolean miniGameFinished = false;
    private static final int miniGameTime = 10;
    public static int milkCount = 0;
    private static boolean cursorMilkUpgradable = false;
    private static boolean cursorMilkUpgraded = false;

    public static void startTutorial() {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1062, 706);
        frame.setLocation(100, 100);
        frame.setVisible(true);

        ImagePanel tutorialPanel = new ImagePanel("/images/gray.png", Color.BLACK, 1062, 706);
        frame.add(tutorialPanel);
        frame.setVisible(true);

        Label welcomeMessage = new Label("<html>Welcome to our game: Cookie Clicker.<br><br>Your goal is to collect as many cookies as possible. Click on the falling cookies to collect them.<br>Use them to buy upgrades that will help you to collect your cookies even faster.<br><br>Created by Marnix van den Bosch and Alex Aichimoaie.</html>", Color.RED, 0, 20);
        tutorialPanel.add(welcomeMessage);
        welcomeMessage.setBounds(100, -200, 862, 706);


        JButton startButton = new JButton("Press to Start");
        startButton.setSize(200, 100);
        startButton.setLocation((frame.getWidth()-startButton.getWidth())/2, (frame.getHeight()-startButton.getHeight())/2);
        startButton.setBackground(Color.GREEN);
        tutorialPanel.add(startButton);
        startButton.addActionListener(e -> {
            frame.remove(tutorialPanel);
            frame.dispose();
            runGame();
            startClicked = true;
        });

        Timer pulseTimer = new Timer(50, new ActionListener() {
            private final float SCALE_SPEED = 0.02f;
            private final float MIN_SCALE = 1.0f;
            private final float MAX_SCALE = 1.2f;

            @Override
            public void actionPerformed(ActionEvent e) {
                if (growing) {
                    scale += SCALE_SPEED;
                    if (scale >= MAX_SCALE) growing = false;
                } else {
                    scale -= SCALE_SPEED;
                    if (scale <= MIN_SCALE) growing = true;
                }
                startButton.revalidate();
                startButton.repaint();
            }
        });
        if (startClicked == true) {
            pulseTimer.stop();
        }
        pulseTimer.start();
    }

    private static void runGame() {
        JFrame frame = new JFrame("Cookie Clicker");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1062, 706);
        frame.setLocation(100, 100);

        ImagePanel panel = new ImagePanel("/images/blueSkyBackground.jpeg", Color.BLACK, 662, 706);
        frame.add(panel, BorderLayout.WEST);

        Label label = new Label("<html>Cookie count: 0<br>CPS: 0</html>", Color.BLACK, 255, 20);
        label.setBounds(0, 0, 300, 40);
        panel.add(label);

        Label label2 = new Label("<html>Milk Storm: 0<br>Milk: 0</html>", Color.BLACK, 255, 20);
        label2.setBounds(300, 0, 500, 40);
        panel.add(label2);

        int spacing = 640 / 10;//hardcoded
        ArrayList<Integer> spawningPoint = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            spawningPoint.add(i*spacing);
        }

        ImagePanel storePanel = new ImagePanel("/images/wood.jpg", Color.BLACK, 400, 706);
        frame.add(storePanel, BorderLayout.EAST);

        Upgrade cursorUpgrade = new Upgrade("/images/CursorUpgrade.png");
        cursorUpgrade.setLocation(100, 50);
        storePanel.add(cursorUpgrade);

        Label costCursor = new Label("" + cursorUpgrade.getCost(), Color.BLACK, 255, 20);
        costCursor.setBounds(250, 100, 40, 40);
        storePanel.add(costCursor);

        Label multiplierCursor = new Label("x2", Color.BLACK, 255, 20);
        multiplierCursor.setBounds(250, 150, 40, 40);
        storePanel.add(multiplierCursor);

        Grandma grandma = new Grandma("/images/grandma.png");
        grandma.setLocation(100, 250);
        storePanel.add(grandma);

        Label grandmaLabel = new Label("0", Color.BLACK, 255, 20);
        grandmaLabel.setBounds(250, 250, 40, 40);
        storePanel.add(grandmaLabel);

        Label costGrandmaLabel = new Label("" + grandma.getCost(), Color.BLACK, 255, 20);
        costGrandmaLabel.setBounds(250, 300, 40, 40);
        storePanel.add(costGrandmaLabel);

        Label cpsGrandmaLabel = new Label("" + grandma.getGrandmaCPS(), Color.BLACK, 255, 20);
        cpsGrandmaLabel.setBounds(250, 350, 40, 40);
        storePanel.add(cpsGrandmaLabel);

        Factory factory = new Factory("/images/Factory.png");
        factory.setLocation(100, 450);
        storePanel.add(factory);

        Label factoryLabel = new Label("0", Color.BLACK, 255, 20);
        factoryLabel.setBounds(250, 450, 40, 40);
        storePanel.add(factoryLabel);

        Label costFactoryLabel = new Label("" + factory.getCost(), Color.BLACK, 255, 20);
        costFactoryLabel.setBounds(250, 500, 40, 40);
        storePanel.add(costFactoryLabel);

        Label cpsFactoryLabel = new Label("0", Color.BLACK, 255, 20);
        cpsFactoryLabel.setBounds(250, 550, 40, 40);
        storePanel.add(cpsFactoryLabel);

        ArrayList<Label> labels = new ArrayList<>();

        Milk milk = new Milk();

        CookieCounter counter = new CookieCounter(0);
        CPS cps = new CPS(0);
        CountDown time = new CountDown(miniGameTime);

        ActionListener upgradeListener = e -> {
            if ((counter.getCookie() >= cursorUpgrade.getCost()) && (!cursorUpgrade.getUpgraded())) { //requirements for upgrade
                counter.setCookie(counter.getCookie() - cursorUpgrade.getCost()); //cost
                counter.setIncrement(counter.getIncrement()*2); //upgrade
                label.setText("<html>Cookie count: " + counter.getCookie() + "<br>CPS: " + cps.getCps() + "</html>");
                label2.setText("<html>Milk Storm: " + time.getTime() + "<br>Milk: " + milk.getMilkCount() + "</html>");
                Label upgradeLabel = new Label("Upgrade purchased!", Color.BLACK, 155, 20);
                showPopUp(upgradeLabel, panel, labels);
                cursorUpgrade.setUpgraded(true);
                storePanel.remove(costCursor);
                multiplierCursor.setText("x5");
                storePanel.repaint();
                cursorUpgrade.setMilkCost(10);
                cursorUpgrade.setCost(0);
            } else if ((milk.getMilkCount() >= cursorUpgrade.getMilkCost()) && (cursorMilkUpgradable) && (!cursorMilkUpgraded) && (cursorUpgrade.getUpgraded())) {
                milk.setMilkCount(milk.getMilkCount() - cursorUpgrade.getMilkCost());
                counter.setIncrement(counter.getIncrement()*5);
                label.setText("<html>Cookie count: " + counter.getCookie() + "<br>CPS: " + cps.getCps() + "</html>");
                label2.setText("<html>Milk Storm: " + time.getTime() + "<br>Milk: " + milk.getMilkCount() + "</html>");
                cursorMilkUpgraded = true;
                Label upgradeLabel = new Label("Upgrade purchased!", Color.BLACK, 155, 20);
                showPopUp(upgradeLabel, panel, labels);
            } else if ((counter.getCookie() < cursorUpgrade.getCost()) && (!cursorUpgrade.getUpgraded())) {
                Label upgradeFailedLabel = new Label("Not enough cookies!", Color.BLACK, 155, 20);
                showPopUp(upgradeFailedLabel, panel, labels);
            } else if (cursorUpgrade.getUpgraded() && !cursorMilkUpgradable) {
                Label upgradeFailedLabel = new Label("Upgrade already purchased!", Color.BLACK, 155, 20);
                showPopUp(upgradeFailedLabel, panel, labels);
            } else if (cursorMilkUpgradable && (milk.getMilkCount() < cursorUpgrade.getMilkCost())) {
                Label upgradeFailedLabel = new Label("Not enough milk!", Color.BLACK, 155, 20);
                showPopUp(upgradeFailedLabel, panel, labels);
            } else {
                Label upgradeFailedLabel = new Label("Upgrade already purchased!", Color.BLACK, 155, 20);
                showPopUp(upgradeFailedLabel, panel, labels);
            }
        };

        ActionListener upgradeFactoryListener = e -> {
            if (counter.getCookie() >= factory.getCost()) { //requirements for upgrade
                counter.setCookie(counter.getCookie() - factory.getCost()); //cost
                cps.setCps(cps.getCps()+factory.getFactoryCPS()); //upgrade
                factory.setCost(factory.getCost()*factory.getMultiplier());
                costFactoryLabel.setText("" + factory.getCost());
                label.setText("<html>Cookie count: " + counter.getCookie() + "<br>CPS: " + cps.getCps() + "</html>");
                label2.setText("<html>Milk Storm: " + time.getTime() + "<br>Milk: " + milk.getMilkCount() + "</html>");
                factory.setUpgradeCount(factory.getUpgradeCount()+1);
                factoryLabel.setText("" + factory.getUpgradeCount());
                Label upgradeLabel = new Label("Upgrade purchased!", Color.BLACK, 155, 20);
                showPopUp(upgradeLabel, panel, labels);
            } else {
                Label upgradeLabel = new Label("Not enough cookies!", Color.BLACK, 155, 20);
                showPopUp(upgradeLabel, panel, labels);
            }
        };

        ActionListener grandmaListener = e -> {
            if (counter.getCookie() >= grandma.getCost()) { //requirements for upgrade
                counter.setCookie(counter.getCookie() - grandma.getCost()); //cost
                cps.setCps(cps.getCps()+grandma.getGrandmaCPS()); //upgrade
                grandma.setCost(grandma.getCost()*grandma.getMultiplier());
                costGrandmaLabel.setText("" + grandma.getCost());
                label.setText("<html>Cookie count: " + counter.getCookie() + "<br>CPS: " + cps.getCps() + "</html>");
                label2.setText("<html>Milk Storm: " + time.getTime() + "<br>Milk: " + milk.getMilkCount() + "</html>");
                grandma.setUpgradeCount(grandma.getUpgradeCount()+1);
                grandmaLabel.setText("" + grandma.getUpgradeCount());
                Label upgradeLabel = new Label("Upgrade purchased!", Color.BLACK, 155, 20);
                showPopUp(upgradeLabel, panel, labels);
            } else {
                Label upgradeLabel = new Label("Not enough cookies!", Color.BLACK, 155, 20);
                showPopUp(upgradeLabel, panel, labels);
            }
        };

        cursorUpgrade.addActionListener(upgradeListener);
        grandma.addActionListener(grandmaListener);
        factory.addActionListener(upgradeFactoryListener);

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
                label.setText("<html>Cookie count: " + counter.getCookie() + "<br>CPS: " + cps.getCps() + "</html>");
                label2.setText("<html>Milk Storm: " + time.getTime() + "<br>Milk: " + milk.getMilkCount() + "</html>");
                panel.repaint();
                spawningPoint.add(clicked.getX());
                spawnCookie(cookies, panel, counter, label, spawningPoint, cps, time, label2, milk); // ✅ recursive call
            });
        }

        MilkCatcherGame game = new MilkCatcherGame();

        Timer fallTimer = new Timer(20, e -> {
            if ((g*20)%1000 == 0) { //is true in intervals of 1 second
                time.setTime(time.getTime() - 1);
                counter.setCookie(counter.getCookie() + cps.getCps());
            }
            g = g + 1;
            label.setText("<html>Cookie count: " + counter.getCookie() + "<br>CPS: " + cps.getCps() + "</html>");
            label2.setText("<html>Milk Storm: " + time.getTime() + "<br>Milk: " + milk.getMilkCount() + "</html>");
            ArrayList<Cookie> toRemove = new ArrayList<>();

            if (time.getTime() <= 0 && !miniGameStarted) {
                game.startGame();
                cursorMilkUpgradable = true;
                miniGameStarted = true;
                frame.remove(panel);
                frame.add(game);
                System.out.println("game is running");
                frame.revalidate();
                frame.repaint();
                game.setFocusable(true);
                game.requestFocusInWindow(); //so that you dont have to click on the window
            }

            if (milkCount != 0) {
                milk.setMilkCount(milk.getMilkCount() + milkCount);
                milkCount = 0;
            }

            if (miniGameFinished) {
                miniGameStarted = false;
                miniGameFinished = false;
                time.setTime(miniGameTime);
                frame.remove(game);
                frame.add(panel);
                frame.revalidate();
                frame.repaint();
            }

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
                spawnCookie(cookies, panel, counter, label, spawningPoint, cps, time, label2, milk);
            }

            ArrayList<Label> toRemove2 = new ArrayList<>();
            for (Label l : labels) {
                l.setLocation(l.getX(), l.getY() - popUpSpeed);
                l.setTransparency(l.getTransparency()-1);
                if (l.getY() < 100) {
                    panel.remove(l);
                    toRemove2.add(l);
                }
            }
            labels.removeAll(toRemove2);



            panel.repaint();
        });
        fallTimer.start();
        frame.setVisible(true);
    }

    private static void showPopUp(Label label, ImagePanel panel, ArrayList<Label> labels) {
        //JOptionPane.showMessageDialog(null, message, title, JOptionPane.INFORMATION_MESSAGE);
        label.setBounds(panel.getWidth()/2-75, 200, 150, 60);
        labels.add(label);
        panel.add(label);
    }

    private static void spawnCookie(
            ArrayList<Cookie> cookies, JPanel panel, CookieCounter counter, Label label, ArrayList<Integer> spawningPoint,  CPS cps, CountDown time, Label label2, Milk milk) {
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
                label.setText("<html>Cookie count: " + counter.getCookie() + "<br>CPS: " + cps.getCps() + "</html>");
                label2.setText("<html>Milk Storm: " + time.getTime() + "<br>Milk: " + milk.getMilkCount() + "</html>");
                panel.repaint();
                spawningPoint.add(cookie.getX());
                spawnCookie(cookies, panel, counter, label, spawningPoint, cps, time, label2, milk); // ✅ recursive call
            });

            cookies.add(cookie);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> startTutorial()); //required for swing
    }
}