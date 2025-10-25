import javax.swing.*;
import java.awt.*;

public class Grandma extends JButton {
    private Image scaledImage;
    private int upgradeCount;
    private int factoryCPS = 1;
    private int cost = 20;
    private int costMultiplier = 2;

    public Grandma(String imagePath) {
        ImageIcon upgradeIcon = new ImageIcon(getClass().getResource(imagePath));
        scaledImage = upgradeIcon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);

        setIcon(new ImageIcon(scaledImage));
        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);
        setOpaque(false);

        setSize(150, 150); // size for absolute layout
    }

    public void setUpgradeCount(int upgradeCount) {
        this.upgradeCount = upgradeCount;
    }

    public int getUpgradeCount() {
        return this.upgradeCount;
    }

    public void setGrandmaCPS(int factoryCPS) {
        this.factoryCPS = factoryCPS;
    }

    public int getGrandmaCPS() {
        return this.factoryCPS;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public int getCost() {
        return this.cost;
    }

    public int getMultiplier() {
        return this.costMultiplier;
    }
}
