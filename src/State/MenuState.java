package State;

import My2DMazeRunner.GamePanel;
import My2DMazeRunner.MenuRenderer;

import java.awt.*;

public class MenuState implements GameState {
    private GamePanel gp;
    private int menuOption = 0;

    public MenuState(GamePanel gp) {
        this.gp = gp;
    }

    @Override
    public void update() {
        handleMenuNavigation();
        handleMenuSelection();
    }

    private void handleMenuNavigation() {
        if (gp.keyH.upPressed && !gp.keyH.upWasPressed) {
            menuOption = (menuOption - 1 + 3) % 3;
            gp.keyH.upWasPressed = true;
        } else if (!gp.keyH.upPressed) {
            gp.keyH.upWasPressed = false;
        }

        if (gp.keyH.downPressed && !gp.keyH.downWasPressed) {
            menuOption = (menuOption + 1) % 3;
            gp.keyH.downWasPressed = true;
        } else if (!gp.keyH.downPressed) {
            gp.keyH.downWasPressed = false;
        }
    }

    private void handleMenuSelection() {
        if (gp.keyH.enterPressed && !gp.keyH.enterWasPressed) {
            switch (menuOption) {
                case 0: // Level
                    gp.gameStateManager.setState(gp.LEVEL_SELECTION_STATE);
                    break;
                case 1: // Information
                    gp.gameStateManager.setState(gp.INFORMATION_STATE);
                    break;
                case 2: // Exit
                    System.exit(0);
                    break;
            }
            gp.keyH.enterWasPressed = true;
        } else if (!gp.keyH.enterPressed) {
            gp.keyH.enterWasPressed = false;
        }
    }

    @Override
    public void draw(Graphics2D g2) {
        MenuRenderer.render(g2, gp, menuOption);
    }
}