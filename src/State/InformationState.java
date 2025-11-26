package State;

import My2DMazeRunner.GamePanel;
import My2DMazeRunner.PanelInformation;

import java.awt.*;

public class InformationState implements GameState {
    private GamePanel gp;
    private PanelInformation panelInformation;

    public InformationState(GamePanel gp) {
        this.gp = gp;
        panelInformation = new PanelInformation(gp);
        panelInformation.setActive(false); // Disabled by default
    }

    @Override
    public void update() {
        // Activate panel when entering this state
        if (!panelInformation.isActive()) {
            panelInformation.setActive(true);
        }
        panelInformation.update();
        handleBackToMenu();
    }

    private void handleBackToMenu() {
        if (gp.keyH.escPressed && !gp.keyH.escWasPressed) {
            panelInformation.setActive(false);
            gp.gameStateManager.setState(gp.MENU_STATE);
            gp.keyH.escWasPressed = true;
        } else if (!gp.keyH.escPressed) {
            gp.keyH.escWasPressed = false;
        }
    }

    @Override
    public void draw(Graphics2D g2) {
        // Draw background
        if (gp.menuBackground != null) {
            g2.drawImage(gp.menuBackground, 0, 0, gp.screenWidth, gp.screenHeight, null);
        } else {
            g2.setColor(Color.BLACK);
            g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);
        }

        // Draw the information panel if active
        if (panelInformation.isActive()) {
            panelInformation.draw(g2);
        }
    }
}

