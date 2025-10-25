import javax.swing.*;
import java.awt.*;

public class Upgrade extends JButton {
    private Image scaledImage;
    private Boolean upgraded = false;
    private int cost = 10;
    private int milkCost = 0;

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

    public Boolean getUpgraded() {
        return upgraded;
    }

    public void setUpgraded(Boolean upgraded) {
        this.upgraded = upgraded;
    }

    public int getCost() {
        return this.cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public int getMilkCost() {
        return this.milkCost;
    }

    public void setMilkCost(int milkCost) {
        this.milkCost = milkCost;
    }
}
