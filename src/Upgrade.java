import javax.swing.*;
import java.awt.*;

public class Upgrade extends JButton {
    private Image scaledImage;
    private Boolean upgraded = false;

    public Upgrade(String imagePath) {

        ImageIcon upgradeIcon = new ImageIcon(getClass().getResource(imagePath));
        scaledImage = upgradeIcon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);

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

    public Boolean getUpgraded() {
        return upgraded;
    }

    public void setUpgraded(Boolean upgraded) {
        this.upgraded = upgraded;
    }
}
