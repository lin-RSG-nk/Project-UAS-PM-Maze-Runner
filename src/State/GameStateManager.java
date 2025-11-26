package State;

import My2DMazeRunner.GamePanel;

import java.awt.Graphics2D;

public class GameStateManager {
    private GamePanel gp;
    private GameState currentState;

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
        switch (state) {
            case 0: // MENU_STATE
                currentState = menuState;
                break;
            case 1: // LEVEL_SELECTION_STATE
                currentState = levelSelectionState;
                break;
            case 2: // INFORMATION_STATE
                currentState = informationState;
                break;
            case 3: // PLAYING_STATE
                currentState = playingState;
                break;
            case 4: // LEVEL_COMPLETE_STATE
                currentState = levelCompleteState;
                ((LevelCompleteState) levelCompleteState).setLevelCompleteTime(System.currentTimeMillis());
                break;
            case 5: // GAME_OVER_STATE
                currentState = gameOverState;
                break;
        }
        resetKeyStates();
    }

    private void resetKeyStates() {
        gp.keyH.enterWasPressed = false;
        gp.keyH.escWasPressed = false;
        gp.keyH.upWasPressed = false;
        gp.keyH.downWasPressed = false;
    }
}