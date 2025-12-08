package My2DMazeRunner;

import ENTITY.Player;
import LEVEL.levelManager;
import State.GameStateManager;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;

public class GamePanel extends JPanel implements Runnable {
    // Screen Settings
    public final int tileSize = 32;
    public final int maxScreenCol = 40;
    public final int maxScreenRow = 24;
    public final int screenWidth = 1280;
    public final int screenHeight = 720;

    // Game States
    public final int MENU_STATE = 0;
    public final int LEVEL_SELECTION_STATE = 1;
    public final int INFORMATION_STATE = 2;
    public final int PLAYING_STATE = 3;
    public final int LEVEL_COMPLETE_STATE = 4;
    public final int GAME_OVER_STATE = 5;

    // Game Components
    public KeyHandler keyH = new KeyHandler();
    public GameStateManager gameStateManager;
    public levelManager levelM;
    public Player player;
    public BufferedImage menuBackground;
    public SoundManager soundManager;

    // Tambahkan variabel currentLevel yang missing
    public int currentLevel = 1; // Current selected level (1, 2, or 3)

    private Thread gameThread;
    private int fps = 60;

    public GamePanel() {
        initializePanel();
        initializeGameComponents();
        loadMenuImage();
    }

    private void initializePanel() {
        setPreferredSize(new Dimension(screenWidth, screenHeight));
        setBackground(Color.black);
        setDoubleBuffered(true);
        addKeyListener(keyH);
        setFocusable(true);
    }

    private void initializeGameComponents() {
        levelM = new levelManager(this);
        player = new Player(this, keyH);
        soundManager = new SoundManager();
        gameStateManager = new GameStateManager(this);
    }

    private void loadMenuImage() {
        try {
            menuBackground = ImageIO.read(getClass().getResourceAsStream("/Menu/MainMenu.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Tambahkan method startLevel yang missing
    public void startLevel(int level) {
        currentLevel = level;
        levelM.loadMap(level);
        // Update player position to start position
        if (levelM != null) {
            player.x = levelM.startCol * tileSize;
            player.y = levelM.startRow * tileSize;

            // --- PERBAIKAN DI SINI ---
            // Reset status Player (matikan AI dan hapus path lama)
            player.resetState();
            // -------------------------
        }
        // Stop menu music when starting game
        if (soundManager != null) {
            soundManager.stopMenuMusic();
        }
        gameStateManager.setState(PLAYING_STATE);
    }

    public void startThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        double drawInterval = 1000000000 / fps;
        double nextDrawTime = System.nanoTime() + drawInterval;

        while (gameThread != null) {
            update();
            repaint();

            try {
                double remainingTime = (nextDrawTime - System.nanoTime()) / 1000000;
                if (remainingTime < 0) remainingTime = 0;
                Thread.sleep((long) remainingTime);
                nextDrawTime += drawInterval;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void update() {
        gameStateManager.update();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        gameStateManager.draw(g2);
        g2.dispose();
    }
}