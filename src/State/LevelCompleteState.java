package State;

import My2DMazeRunner.GamePanel;

import java.awt.*;

public class LevelCompleteState implements GameState {
    private GamePanel gp;
    private long levelCompleteTime;

    public LevelCompleteState(GamePanel gp) {
        this.gp = gp;
    }

    public void setLevelCompleteTime(long time) {
        this.levelCompleteTime = time;
    }

    @Override
    public void update() {
        // Auto-advance to next level after duration
        if (System.currentTimeMillis() - levelCompleteTime >= 2000) {
            if (gp.currentLevel < 3) {
                gp.currentLevel++;
                gp.startLevel(gp.currentLevel);
            } else {
                gp.gameStateManager.setState(gp.GAME_OVER_STATE);
            }
        }
    }

    @Override
    public void draw(Graphics2D g2) {
        // Draw the game behind the message
        gp.levelM.draw(g2);
        gp.player.draw(g2);

        // Draw overlay
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.7f));
        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));

        // Draw message
        g2.setFont(new Font("Courier New", Font.BOLD, 72));
        String message = "Level Complete!";
        FontMetrics messageFm = g2.getFontMetrics();
        int messageX = (gp.screenWidth - messageFm.stringWidth(message)) / 2;
        int messageY = gp.screenHeight / 2;

        // Shadow
        g2.setColor(new Color(0, 0, 0, 150));
        g2.drawString(message, messageX + 3, messageY + 3);

        // Main text
        g2.setColor(new Color(255, 215, 0));
        g2.drawString(message, messageX, messageY);

        // Level info
        g2.setFont(new Font("Courier New", Font.BOLD, 36));
        String levelText = "Level " + gp.currentLevel + " Completed!";
        FontMetrics levelFm = g2.getFontMetrics();
        int levelX = (gp.screenWidth - levelFm.stringWidth(levelText)) / 2;
        g2.setColor(new Color(255, 255, 150));
        g2.drawString(levelText, levelX, messageY + 60);
    }
}