import javax.swing.*;
import java.awt.*;

public class ImagePanel extends JPanel {
    private Image backgroundImage;
    private Color borderColor;

    // Constructor
    public ImagePanel(String imagePath, Color borderColor, int width, int height) {
        this.borderColor = borderColor;

        ImageIcon icon = new ImageIcon(getClass().getResource(imagePath));
        backgroundImage = icon.getImage();

        setPreferredSize(new Dimension(width, height));
        setLayout(null);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }

        g.setColor(borderColor);
        g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
    }
}
