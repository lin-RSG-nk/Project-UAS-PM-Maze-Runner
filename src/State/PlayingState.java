package State;

import My2DMazeRunner.GamePanel;

import java.awt.*;

public class PlayingState implements GameState {
    private GamePanel gp;

    public PlayingState(GamePanel gp) {
        this.gp = gp;
    }

    @Override
    public void update() {
        handlePause();
        gp.player.update(); // Ini yang memanggil update animasi
        checkFinishPoint();
    }

    private void handlePause() {
        if (gp.keyH.escPressed && !gp.keyH.escWasPressed) {
            // Return to level selection menu instead of main menu
            gp.gameStateManager.setState(gp.LEVEL_SELECTION_STATE);
            gp.keyH.escWasPressed = true;
        } else if (!gp.keyH.escPressed) {
            gp.keyH.escWasPressed = false;
        }
    }

    @Override
    public void draw(Graphics2D g2) {
        gp.levelM.draw(g2);
        gp.player.draw(g2); // Ini yang memanggil draw player dengan animasi
    }
    private void checkFinishPoint() {
        // Gunakan pusat collision box untuk deteksi finish point
        int playerCenterX = gp.player.collisionX + (gp.player.collisionWidth / 2);
        int playerCenterY = gp.player.collisionY + (gp.player.collisionHeight / 2);

        if (gp.levelM.isFinishPoint(playerCenterX, playerCenterY)) {
            gp.gameStateManager.setState(gp.LEVEL_COMPLETE_STATE);
        }
    }
}