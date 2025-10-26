import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;

public class Cookie extends JButton {
    private String name;
    private Image scaledImage;
    private int x;


    public Cookie(String imagePath, String name) {
        this.name = name;

        ImageIcon cookieIcon = new ImageIcon(getClass().getResource(imagePath));
        scaledImage = cookieIcon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);

        setIcon(new ImageIcon(scaledImage));
        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);
        setOpaque(false);

        setSize(150, 150);
    }

    public int random(ArrayList<Integer> spawningPoint) {
        return (int) (Math.random() * spawningPoint.size()); //prevents out of bound error
    }

    public void setX(int y) {
        this.x = y;
    }

    public int getX() {
        return this.x;
    }
}
