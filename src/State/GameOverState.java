package State;

import My2DMazeRunner.GamePanel;

import java.awt.*;

public class GameOverState implements GameState {
    private GamePanel gp;

    public GameOverState(GamePanel gp) {
        this.gp = gp;
    }

    @Override
    public void update() {
        if (gp.keyH.enterPressed && !gp.keyH.enterWasPressed) {
            gp.gameStateManager.setState(gp.MENU_STATE);
            gp.currentLevel = 1;
            gp.keyH.enterWasPressed = true;
        } else if (!gp.keyH.enterPressed) {
            gp.keyH.enterWasPressed = false;
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
        g2.setFont(new Font("Courier New", Font.BOLD, 64));
        String title = "CONGRATULATIONS!";
        FontMetrics titleFm = g2.getFontMetrics();
        int titleX = (gp.screenWidth - titleFm.stringWidth(title)) / 2;
        g2.setColor(new Color(255, 215, 0));
        g2.drawString(title, titleX, 300);

        // Draw message
        g2.setFont(new Font("Courier New", Font.BOLD, 36));
        String message = "You Completed All Levels!";
        FontMetrics messageFm = g2.getFontMetrics();
        int messageX = (gp.screenWidth - messageFm.stringWidth(message)) / 2;
        g2.setColor(new Color(255, 255, 150));
        g2.drawString(message, messageX, 380);

        // Draw instruction
        g2.setFont(new Font("Courier New", Font.PLAIN, 28));
        String instruction = "Press ENTER to return to main menu";
        FontMetrics instructionFm = g2.getFontMetrics();
        int instructionX = (gp.screenWidth - instructionFm.stringWidth(instruction)) / 2;
        g2.setColor(new Color(255, 255, 255, 200));
        g2.drawString(instruction, instructionX, 500);
    }
}