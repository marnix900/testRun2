import java.awt.event.*;
import java.awt.*;
import javax.swing.*;

public class testClass {
    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */

    private Cookie cookie;

    public testClass(Cookie cookie) {
        this.cookie = cookie;
    }

    private static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("HelloWorldSwing");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Add the ubiquitous "Hello World" label.
        JLabel label = new JLabel("Hello World");
        frame.getContentPane().add(label);

        //Display the window.
        frame.pack();
        frame.setVisible(true);
        frame.setSize(500,400);
        frame.setLocation(2000, 500);

        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createLineBorder(Color.black));
        panel.setBackground(Color.red);

        frame.add(panel);

        JButton button = new JButton("Cookie");
        panel.add(button);
        button.setBorder(BorderFactory.createLineBorder(Color.black));
        button.setBackground(Color.blue);

        Cookie c = new Cookie(0);
        button.addMouseListener(c.addCookie(c.getCookie()));
        label.setText("Cookie count: " + c.getCookie());
    }

    /*
    public static void createWindow() {
        JWindow window = new JWindow();
        window.setSize(100,100);
        window.setLocation(100, 100);
        window.setVisible(true);
    }
    */

    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                Cookie c = new Cookie(0);
                testClass testClass = new testClass(c);
                createAndShowGUI();
                //createWindow();
            }
        });
    }
}