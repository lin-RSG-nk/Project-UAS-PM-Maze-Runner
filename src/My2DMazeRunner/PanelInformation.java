package My2DMazeRunner;

import java.awt.*;

public class PanelInformation {
    private GamePanel gp;
    private boolean isActive;

    // Panel dimensions and positioning
    private int panelWidth = 850;
    private int panelHeight = 600;
    private int panelX;
    private int panelY;

    // Key visual settings
    private int keyBoxSize = 70;
    private Font keyFont = new Font("Courier New", Font.BOLD, 36);

    // Colors matching the existing theme
    private Color panelBackgroundColor = new Color(0, 0, 0, 220); // Semi-transparent black
    private Color borderColor = new Color(255, 255, 150);
    private Color titleColor = new Color(255, 255, 150);
    private Color textColor = new Color(255, 255, 255, 240);
    private Color instructionColor = new Color(255, 255, 200);

    // Fonts matching the existing theme
    private Font titleFont = new Font("Courier New", Font.BOLD, 44);
    private Font instructionFont = new Font("Courier New", Font.PLAIN, 26);
    private Font backInstructionFont = new Font("Courier New", Font.PLAIN, 24);

    public PanelInformation(GamePanel gp) {
        this.gp = gp;
        this.isActive = false;
        // Center the panel on screen
        this.panelX = (gp.screenWidth - panelWidth) / 2;
        this.panelY = (gp.screenHeight - panelHeight) / 2;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        this.isActive = active;
    }

    public void update() {
        // Update logic if needed in the future
    }

    public void draw(Graphics2D g2) {
        if (!isActive) return;

        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw panel background
        g2.setColor(panelBackgroundColor);
        g2.fillRoundRect(panelX, panelY, panelWidth, panelHeight, 20, 20);

        // Draw panel border
        g2.setStroke(new BasicStroke(3));
        g2.setColor(borderColor);
        g2.drawRoundRect(panelX, panelY, panelWidth, panelHeight, 20, 20);

        // Draw title "How to Play – Maze Runner" (split into two lines)
        g2.setFont(titleFont);
        String titleLine1 = "How to Play";
        String titleLine2 = "Maze Runner";
        FontMetrics titleFm = g2.getFontMetrics();
        
        // Center both lines
        int titleLine1X = panelX + (panelWidth - titleFm.stringWidth(titleLine1)) / 2;
        int titleLine2X = panelX + (panelWidth - titleFm.stringWidth(titleLine2)) / 2;
        int titleY1 = panelY + 60;
        int titleY2 = titleY1 + titleFm.getHeight() + 8;
        
        g2.setColor(titleColor);
        g2.drawString(titleLine1, titleLine1X, titleY1);
        g2.drawString(titleLine2, titleLine2X, titleY2);

        // Draw control instructions with button images
        g2.setFont(instructionFont);
        g2.setColor(instructionColor);
        
        int startY = titleY2 + 60;
        int lineSpacing = 85;
        int leftPadding = 100;
        int buttonTextGap = 35;
        FontMetrics fm = g2.getFontMetrics();
        int textVerticalCenter = keyBoxSize / 2;
        
        drawKeyInstruction(g2, "W", "Move the runner upward", panelX + leftPadding,
                startY, buttonTextGap, fm, textVerticalCenter);
        drawKeyInstruction(g2, "A", "Move the runner to the left", panelX + leftPadding,
                startY + lineSpacing, buttonTextGap, fm, textVerticalCenter);
        drawKeyInstruction(g2, "S", "Move the runner downward", panelX + leftPadding,
                startY + (lineSpacing * 2), buttonTextGap, fm, textVerticalCenter);
        drawKeyInstruction(g2, "D", "Move the runner to the right", panelX + leftPadding,
                startY + (lineSpacing * 3), buttonTextGap, fm, textVerticalCenter);
        
        // Alternative control hint
        int altHintY = startY + (lineSpacing * 4) + 15;
        g2.setFont(new Font("Courier New", Font.PLAIN, 22));
        g2.setColor(new Color(255, 255, 255, 200));
        String altHint = "You can also use the Arrow Keys (↑ ↓ ← →)";
        FontMetrics altFm = g2.getFontMetrics();
        int altHintX = panelX + (panelWidth - altFm.stringWidth(altHint)) / 2;
        g2.drawString(altHint, altHintX, altHintY);

        // Draw back instruction at the bottom
        g2.setFont(backInstructionFont);
        String backText = "Press ESC to go back";
        FontMetrics backFm = g2.getFontMetrics();
        int backX = panelX + (panelWidth - backFm.stringWidth(backText)) / 2;
        int backY = panelY + panelHeight - 40;
        g2.setColor(new Color(255, 255, 255, 180));
        g2.drawString(backText, backX, backY);
    }

    private void drawKeyInstruction(Graphics2D g2, String keyLabel, String description,
                                    int keyX, int keyY, int gap, FontMetrics textMetrics, int textOffset) {
        // Draw key box
        g2.setColor(new Color(30, 30, 30, 220));
        g2.fillRoundRect(keyX, keyY, keyBoxSize, keyBoxSize, 12, 12);
        g2.setColor(new Color(255, 255, 200));
        g2.setStroke(new BasicStroke(2));
        g2.drawRoundRect(keyX, keyY, keyBoxSize, keyBoxSize, 12, 12);

        // Draw key label
        g2.setFont(keyFont);
        FontMetrics keyFm = g2.getFontMetrics();
        int letterX = keyX + (keyBoxSize - keyFm.stringWidth(keyLabel)) / 2;
        int letterY = keyY + ((keyBoxSize - keyFm.getHeight()) / 2) + keyFm.getAscent();
        g2.drawString(keyLabel, letterX, letterY);

        // Draw description text
        g2.setFont(instructionFont);
        g2.setColor(instructionColor);
        int textX = keyX + keyBoxSize + gap;
        int textY = keyY + textOffset + (textMetrics.getHeight() / 3);
        g2.drawString(description, textX, textY);
    }
}

