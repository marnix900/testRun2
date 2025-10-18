import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Cookie extends JButton {
    private String name;
    private Image scaledImage;
    private int X;


    public Cookie(String imagePath, String name) {
        this.name = name;

        ImageIcon cookieIcon = new ImageIcon(getClass().getResource(imagePath));
        scaledImage = cookieIcon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);

        setIcon(new ImageIcon(scaledImage));
        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);
        setOpaque(false);

        setSize(150, 150); // size for absolute layout
    }

    public void shrink() {
        setIcon(new ImageIcon(scaledImage.getScaledInstance(140, 140, Image.SCALE_SMOOTH)));
    }

    public void grow() {
        setIcon(new ImageIcon(scaledImage.getScaledInstance(150, 150, Image.SCALE_SMOOTH)));
    }

    public int random(ArrayList<Integer> spawningPoint) {
        return (int)(Math.random()*spawningPoint.size()); //prevents out of bound error
    }

    public void setX(int x) {
        this.X = x;
    }

    public int getX() {
        return this.X;
    }
}
