package State;

import My2DMazeRunner.GamePanel;

import java.awt.*;

public class SettingState implements GameState {
    private GamePanel gp;

    public SettingState(GamePanel gp) {
        this.gp = gp;
    }

    @Override
    public void update() {
        handleBackToMenu();
    }

    private void handleBackToMenu() {
        if (gp.keyH.escPressed && !gp.keyH.escWasPressed) {
            gp.gameStateManager.setState(gp.MENU_STATE);
            gp.keyH.escWasPressed = true;
        } else if (!gp.keyH.escPressed) {
            gp.keyH.escWasPressed = false;
        }
    }

    @Override
    public void draw(Graphics2D g2) {
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);

        // Draw background
        if (gp.menuBackground != null) {
            g2.drawImage(gp.menuBackground, 0, 0, gp.screenWidth, gp.screenHeight, null);
        } else {
            g2.setColor(Color.BLACK);
            g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);
        }

        // Draw title
        g2.setFont(new Font("Courier New", Font.BOLD, 56));
        String title = "SETTINGS";
        FontMetrics titleFm = g2.getFontMetrics();
        int titleX = (gp.screenWidth - titleFm.stringWidth(title)) / 2;
        g2.setColor(new Color(255, 255, 150));
        g2.drawString(title, titleX, 400);

        // Draw placeholder
        g2.setFont(new Font("Courier New", Font.PLAIN, 32));
        String placeholder = "Settings menu coming soon...";
        FontMetrics placeholderFm = g2.getFontMetrics();
        int placeholderX = (gp.screenWidth - placeholderFm.stringWidth(placeholder)) / 2;
        g2.setColor(new Color(255, 255, 255, 200));
        g2.drawString(placeholder, placeholderX, 500);

        // Draw back instruction
        g2.setFont(new Font("Courier New", Font.PLAIN, 24));
        String backText = "Press ESC to go back";
        FontMetrics backFm = g2.getFontMetrics();
        int backX = (gp.screenWidth - backFm.stringWidth(backText)) / 2;
        g2.setColor(new Color(255, 255, 255, 180));
        g2.drawString(backText, backX, 680);
    }
}