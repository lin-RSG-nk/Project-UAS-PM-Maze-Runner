package State;

import My2DMazeRunner.GamePanel;

import java.awt.Graphics2D;

public class GameStateManager {
    private GamePanel gp;
    private GameState currentState;
    private int previousState = -1; // Track previous state for ESC navigation

    // State instances
    private MenuState menuState;
    private LevelSelectionState levelSelectionState;
    private InformationState informationState;
    private PlayingState playingState;
    private LevelCompleteState levelCompleteState;
    private GameOverState gameOverState;

    public GameStateManager(GamePanel gp) {
        this.gp = gp;
        initializeStates();
        setState(gp.MENU_STATE); // Start with menu state
    }

    private void initializeStates() {
        menuState = new MenuState(gp);
        levelSelectionState = new LevelSelectionState(gp);
        informationState = new InformationState(gp);
        playingState = new PlayingState(gp);
        levelCompleteState = new LevelCompleteState(gp);
        gameOverState = new GameOverState(gp);
    }

    public void update() {
        if (currentState != null) {
            currentState.update();
        }
    }

    public void draw(Graphics2D g2) {
        if (currentState != null) {
            currentState.draw(g2);
        }
    }

    public void setState(int state) {
        // Save current state as previous before switching (except first time)
        if (currentState != null) {
            int currentStateId = getCurrentStateId();
            if (currentStateId != -1 && state != currentStateId) {
                previousState = currentStateId;
            }
        }

        // Hentikan musik yang sesuai sebelum berpindah state
        switch (state) {
            case 0: // MENU_STATE
                //Musik game mati
                if (gp.soundManager != null) {
                    gp.soundManager.stopGameMusic();
                    gp.soundManager.playMenuMusic();
                }
                currentState = menuState;
                break;
            case 1: // LEVEL_SELECTION_STATE
                //Musik game mati
                if (gp.soundManager != null) {
                    gp.soundManager.stopGameMusic();
                    gp.soundManager.playMenuMusic();
                }
                currentState = levelSelectionState;
                break;
            case 2: // INFORMATION_STATE
                //Musik game mati
                if (gp.soundManager != null) {
                    gp.soundManager.stopGameMusic();
                }
                currentState = informationState;
                break;
            case 3: // PLAYING_STATE
                // Hentikan musik menu, musik game akan diputar oleh player
                if (gp.soundManager != null) {
                    gp.soundManager.stopMenuMusic();
                }
                currentState = playingState;
                break;
            case 4: // LEVEL_COMPLETE_STATE
                //Musik game mati
                if (gp.soundManager != null) {
                    gp.soundManager.stopGameMusic();
                }
                currentState = levelCompleteState;
                break;
            case 5: // GAME_OVER_STATE
                //Musik game mati
                if (gp.soundManager != null) {
                    gp.soundManager.stopGameMusic();
                    gp.soundManager.playMenuMusic(); // Mainkan musik menu di game over
                }
                currentState = gameOverState;
                break;
        }
        resetKeyStates();
    }
    
    private int getCurrentStateId() {
        if (currentState == menuState) return gp.MENU_STATE;
        if (currentState == levelSelectionState) return gp.LEVEL_SELECTION_STATE;
        if (currentState == informationState) return gp.INFORMATION_STATE;
        if (currentState == playingState) return gp.PLAYING_STATE;
        if (currentState == levelCompleteState) return gp.LEVEL_COMPLETE_STATE;
        if (currentState == gameOverState) return gp.GAME_OVER_STATE;
        return -1;
    }
    
    public int getPreviousState() {
        return previousState;
    }
    
    public void goBackToPreviousState() {
        if (previousState != -1) {
            setState(previousState);
        }
    }

    private void resetKeyStates() {
        gp.keyH.enterWasPressed = false;
        gp.keyH.escWasPressed = false;
        gp.keyH.upWasPressed = false;
        gp.keyH.downWasPressed = false;
    }
}