package State;

import My2DMazeRunner.GamePanel;

import java.awt.*;

public class LevelSelectionState implements GameState {
    private GamePanel gp;
    private int levelOption = 0;

    public LevelSelectionState(GamePanel gp) {
        this.gp = gp;
    }

    @Override
    public void update() {
        handleLevelNavigation();
        handleLevelSelection();
        handleBackToMenu();
    }

    private void handleLevelNavigation() {
        if (gp.keyH.upPressed && !gp.keyH.upWasPressed) {
            levelOption--;
            if (levelOption < 0) levelOption = 2;
            gp.keyH.upWasPressed = true;
        } else if (!gp.keyH.upPressed) {
            gp.keyH.upWasPressed = false;
        }

        if (gp.keyH.downPressed && !gp.keyH.downWasPressed) {
            levelOption++;
            if (levelOption > 2) levelOption = 0;
            gp.keyH.downWasPressed = true;
        } else if (!gp.keyH.downPressed) {
            gp.keyH.downWasPressed = false;
        }
    }

    private void handleLevelSelection() {
        if (gp.keyH.enterPressed && !gp.keyH.enterWasPressed) {
            // Play click sound when level is selected
            if (gp.soundManager != null) {
                gp.soundManager.playClickSound();
            }
            
            // Gunakan gp.currentLevel bukan variabel lokal
            gp.currentLevel = levelOption + 1;
            gp.startLevel(gp.currentLevel);
            gp.keyH.enterWasPressed = true;
        } else if (!gp.keyH.enterPressed) {
            gp.keyH.enterWasPressed = false;
        }
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

        if (gp.menuBackground != null) {
            g2.drawImage(gp.menuBackground, 0, 0, gp.screenWidth, gp.screenHeight, null);
        } else {
            g2.setColor(Color.BLACK);
            g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);
        }

        g2.setFont(new Font("Courier New", Font.BOLD, 56));
        String title = "SELECT LEVEL";
        FontMetrics titleFm = g2.getFontMetrics();
        int titleX = (gp.screenWidth - titleFm.stringWidth(title)) / 2;
        g2.setColor(new Color(255, 255, 150));
        g2.drawString(title, titleX, 350);

        g2.setFont(new Font("Courier New", Font.BOLD, 48));
        String[] levelOptions = {"Level 1", "Level 2", "Level 3"};
        int menuY = 480;
        int menuSpacing = 75;

        for (int i = 0; i < levelOptions.length; i++) {
            String option = levelOptions[i];
            FontMetrics menuFm = g2.getFontMetrics();
            int menuX = (gp.screenWidth - menuFm.stringWidth(option)) / 2;
            int currentY = menuY + (i * menuSpacing);

            if (i == levelOption) {
                g2.setColor(new Color(255, 215, 0));
                int arrowSize = 12;
                int arrowX = menuX - 35;
                int arrowY = currentY - 20;

                g2.fillPolygon(new int[]{arrowX, arrowX + arrowSize, arrowX + arrowSize},
                        new int[]{arrowY, arrowY - arrowSize, arrowY + arrowSize}, 3);

                g2.setColor(new Color(255, 255, 150));
                g2.drawString(option, menuX, currentY);

                g2.setColor(new Color(255, 215, 0, 100));
                g2.drawString(option, menuX + 1, currentY + 1);
            } else {
                g2.setColor(new Color(255, 255, 255, 220));
                g2.drawString(option, menuX, currentY);
            }
        }

        g2.setFont(new Font("Courier New", Font.PLAIN, 24));
        String backText = "Press ESC to go back";
        FontMetrics backFm = g2.getFontMetrics();
        int backX = (gp.screenWidth - backFm.stringWidth(backText)) / 2;
        g2.setColor(new Color(255, 255, 255, 180));
        g2.drawString(backText, backX, 680);
    }
}