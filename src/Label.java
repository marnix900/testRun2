import javax.swing.*;
import java.awt.*;

// Custom label with rounded corners and transparency
class Label extends JLabel {
    private Color backgroundColor;
    private int cornerRadius = 20;

    public Label(String text, Color bgColor) {
        super(text, SwingConstants.CENTER);
        this.backgroundColor = bgColor;
        setOpaque(false); // important for transparency
        setForeground(Color.WHITE);
        setFont(new Font("Arial", Font.BOLD, 18));
    }

    @Override //not necessary but conventional
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // semi-transparent gray
        g2.setColor(new Color(
                backgroundColor.getRed(),
                backgroundColor.getGreen(),
                backgroundColor.getBlue(),
                150 // alpha transparency (0–255)
        ));
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius);
        super.paintComponent(g);
        g2.dispose();
    }
}
