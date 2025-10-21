import javax.swing.*;
import java.awt.*;

public class ImagePanel extends JPanel {
    private Image backgroundImage;
    private Color borderColor;

    // Constructor
    public ImagePanel(String imagePath, Color borderColor, int width, int height) {
        this.borderColor = borderColor;

        // Try to load image
        ImageIcon icon = new ImageIcon(getClass().getResource(imagePath));
        backgroundImage = icon.getImage();

        // Optional: set panel size or layout
        setPreferredSize(new Dimension(width, height));
        setLayout(null); // for absolute positioning of the cookies
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); // important for proper repainting

        // Draw the background image scaled to panel size
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }

        // Draw the borderline with custom color
        g.setColor(borderColor);
        g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
    }
}
