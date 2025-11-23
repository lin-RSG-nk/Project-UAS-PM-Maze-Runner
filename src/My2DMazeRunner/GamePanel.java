package My2DMazeRunner;

import ENTITY.Player;
import LEVEL.levelManager;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;

public class GamePanel extends JPanel implements Runnable{
    //Screen Setting
    final int originalTileSize = 16;
    final int scale = 2;

    public final int tileSize = originalTileSize  * scale;
    public final int maxScreenCol = 40;
    public final int maxScreenRow = 24;

    public final int screenWidth = 1280; //80 asset
    public final int screenHeight = 720; //45 asset

    //Game State
    public final int MENU_STATE = 0;
    public final int LEVEL_SELECTION_STATE = 1;
    public final int SETTING_STATE = 2;
    public final int PLAYING_STATE = 3;
    public final int LEVEL_COMPLETE_STATE = 4;
    public final int GAME_OVER_STATE = 5;
    public int gameState = MENU_STATE;
    
    // Level Complete message
    long levelCompleteTime = 0;
    final long LEVEL_COMPLETE_DURATION = 2000; // 2 seconds in milliseconds

    //FPS
    int fps = 60;

    KeyHandler keyH = new KeyHandler();

    Thread gameThread;
    
    //Menu
    BufferedImage menuBackground;
    int menuOption = 0; // 0: Level, 1: Setting, 2: Exit
    int levelOption = 0; // 0: Level 1, 1: Level 2, 2: Level 3
    Font menuFont;
    int currentLevel = 1; // Current selected level (1, 2, or 3)

    public levelManager levelM = new levelManager(this);
    Player player = new Player(this, keyH);

    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);

        //Load menu background
        getMenuImage();

        //Initialize fonts - Pixel art style
        menuFont = new Font("Courier New", Font.BOLD, 48);
    }

    public void getMenuImage() {
        try {
            menuBackground = ImageIO.read(getClass().getResourceAsStream("/Player/MainMenu.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void startThread(){
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {


        double drawInterval = 1000000000/fps;
        double nextDrawtime = System.nanoTime() + drawInterval;

        while(gameThread != null){

            //Update : perubahan setiap inputan akann di tampilkan melaluli ini
            update();
            //Draw : dimana setelah data di updat maka akan di munculkan di interface perubahannya
            repaint();


            try {
                double remainingTime = nextDrawtime - System.nanoTime();
                remainingTime = remainingTime / 1000000;

                if (remainingTime < 0){
                    remainingTime = 0;
                }

                Thread.sleep((long)remainingTime);

                nextDrawtime += drawInterval;

            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void update(){
        if (gameState == MENU_STATE) {
            updateMenu();
        } else if (gameState == LEVEL_SELECTION_STATE) {
            updateLevelSelection();
        } else if (gameState == SETTING_STATE) {
            updateSetting();
        } else if (gameState == PLAYING_STATE) {
            //Check if ESC is pressed to return to menu
            if (keyH.escPressed && !keyH.escWasPressed) {
                gameState = MENU_STATE;
                keyH.escWasPressed = true;
            } else if (!keyH.escPressed) {
                keyH.escWasPressed = false;
            }
            
            player.update();
            
            // Check if player reached finish point
            checkFinishPoint();
        } else if (gameState == LEVEL_COMPLETE_STATE) {
            updateLevelComplete();
        } else if (gameState == GAME_OVER_STATE) {
            //Handle game over screen
            if (keyH.enterPressed && !keyH.enterWasPressed) {
                gameState = MENU_STATE;
                currentLevel = 1;
                keyH.enterWasPressed = true;
            } else if (!keyH.enterPressed) {
                keyH.enterWasPressed = false;
            }
        }
    }

    public void updateMenu() {
        //Handle menu navigation
        if (keyH.upPressed && !keyH.upWasPressed) {
            menuOption--;
            if (menuOption < 0) {
                menuOption = 2;
            }
            keyH.upWasPressed = true;
        } else if (!keyH.upPressed) {
            keyH.upWasPressed = false;
        }

        if (keyH.downPressed && !keyH.downWasPressed) {
            menuOption++;
            if (menuOption > 2) {
                menuOption = 0;
            }
            keyH.downWasPressed = true;
        } else if (!keyH.downPressed) {
            keyH.downWasPressed = false;
        }

        //Handle menu selection
        if (keyH.enterPressed && !keyH.enterWasPressed) {
            selectMenuOption();
            keyH.enterWasPressed = true;
        } else if (!keyH.enterPressed) {
            keyH.enterWasPressed = false;
        }
    }

    public void selectMenuOption() {
        switch (menuOption) {
            case 0: // Level
                gameState = LEVEL_SELECTION_STATE;
                levelOption = 0; // Reset to first level option
                break;
            case 1: // Setting
                gameState = SETTING_STATE;
                break;
            case 2: // Exit
                System.exit(0);
                break;
        }
    }

    public void updateLevelSelection() {
        //Handle level selection navigation
        if (keyH.upPressed && !keyH.upWasPressed) {
            levelOption--;
            if (levelOption < 0) {
                levelOption = 2;
            }
            keyH.upWasPressed = true;
        } else if (!keyH.upPressed) {
            keyH.upWasPressed = false;
        }

        if (keyH.downPressed && !keyH.downWasPressed) {
            levelOption++;
            if (levelOption > 2) {
                levelOption = 0;
            }
            keyH.downWasPressed = true;
        } else if (!keyH.downPressed) {
            keyH.downWasPressed = false;
        }

        //Handle level selection
        if (keyH.enterPressed && !keyH.enterWasPressed) {
            currentLevel = levelOption + 1; // Level 1, 2, or 3
            startLevel(currentLevel);
            keyH.enterWasPressed = true;
        } else if (!keyH.enterPressed) {
            keyH.enterWasPressed = false;
        }

        //Handle back to main menu
        if (keyH.escPressed && !keyH.escWasPressed) {
            gameState = MENU_STATE;
            keyH.escWasPressed = true;
        } else if (!keyH.escPressed) {
            keyH.escWasPressed = false;
        }
    }

    public void updateSetting() {
        //Handle back to main menu
        if (keyH.escPressed && !keyH.escWasPressed) {
            gameState = MENU_STATE;
            keyH.escWasPressed = true;
        } else if (!keyH.escPressed) {
            keyH.escWasPressed = false;
        }
    }

    public void startLevel(int level) {
        currentLevel = level;
        levelM.loadMap(level); // Load the selected level first to get start position
        // Update player position to start position after map is loaded
        if (levelM != null) {
            player.x = levelM.startCol * tileSize;
            player.y = levelM.startRow * tileSize;
        }
        gameState = PLAYING_STATE;
    }
    
    public void checkFinishPoint() {
        // Check if player is on finish point
        int playerCenterX = player.x + (tileSize / 2);
        int playerCenterY = player.y + (tileSize / 2);
        
        if (levelM.isFinishPoint(playerCenterX, playerCenterY) && gameState == PLAYING_STATE) {
            // Player reached finish point - show "Level Complete!" message
            gameState = LEVEL_COMPLETE_STATE;
            levelCompleteTime = System.currentTimeMillis();
        }
    }
    
    public void updateLevelComplete() {
        // Check if enough time has passed
        if (System.currentTimeMillis() - levelCompleteTime >= LEVEL_COMPLETE_DURATION) {
            // Move to next level or game over
            if (currentLevel == 1) {
                // Go to level 2
                startLevel(2);
            } else if (currentLevel == 2) {
                // Go to level 3
                startLevel(3);
            } else if (currentLevel == 3) {
                // Game completed!
                gameState = GAME_OVER_STATE;
            }
        }
    }
    public void paintComponent(Graphics g){
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;

        if (gameState == MENU_STATE) {
            drawMenu(g2);
        } else if (gameState == LEVEL_SELECTION_STATE) {
            drawLevelSelection(g2);
        } else if (gameState == SETTING_STATE) {
            drawSetting(g2);
        } else if (gameState == PLAYING_STATE) {
            levelM.draw(g2);
            player.draw(g2);
        } else if (gameState == LEVEL_COMPLETE_STATE) {
            levelM.draw(g2);
            player.draw(g2);
            drawLevelComplete(g2);
        } else if (gameState == GAME_OVER_STATE) {
            drawGameOver(g2);
        }

        g2.dispose();
    }

    public void drawMenu(Graphics2D g2) {
        //Enable anti-aliasing for smoother rendering
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
        
        //Draw background (MainMenu.jpg already has the title)
        if (menuBackground != null) {
            g2.drawImage(menuBackground, 0, 0, screenWidth, screenHeight, null);
        } else {
            g2.setColor(Color.BLACK);
            g2.fillRect(0, 0, screenWidth, screenHeight);
        }

        //Draw menu options - Pixel art style
        g2.setFont(menuFont);
        String[] menuOptions = {"Level", "Setting", "Exit"};
        int menuY = 420; // Adjusted position for better placement on background
        int menuSpacing = 75;

        for (int i = 0; i < menuOptions.length; i++) {
            String option = menuOptions[i];
            FontMetrics menuFm = g2.getFontMetrics(menuFont);
            int menuX = (screenWidth - menuFm.stringWidth(option)) / 2;
            int currentY = menuY + (i * menuSpacing);

            //Highlight selected option
            if (i == menuOption) {
                //Draw selection indicator (pixel art style arrow - simple triangle)
                g2.setColor(new Color(255, 215, 0)); // Gold color for pixel art feel
                int arrowSize = 12;
                int arrowX = menuX - 35;
                int arrowY = currentY - 20;
                
                //Simple pixel-style arrow (triangle)
                g2.fillPolygon(new int[]{arrowX, arrowX + arrowSize, arrowX + arrowSize}, 
                              new int[]{arrowY, arrowY - arrowSize, arrowY + arrowSize}, 3);
                
                //Draw text with highlight (bright gold/yellow for pixel art)
                g2.setColor(new Color(255, 255, 150)); // Bright yellow
                g2.drawString(option, menuX, currentY);
                
                //Add subtle glow effect (shadow)
                g2.setColor(new Color(255, 215, 0, 100)); // Semi-transparent gold
                g2.drawString(option, menuX + 1, currentY + 1);
            } else {
                //Draw normal text (white with slight transparency for pixel art style)
                g2.setColor(new Color(255, 255, 255, 220)); // Slightly transparent white
                g2.drawString(option, menuX, currentY);
            }
        }
    }

    public void drawLevelSelection(Graphics2D g2) {
        //Enable anti-aliasing for smoother rendering
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
        
        //Draw background
        if (menuBackground != null) {
            g2.drawImage(menuBackground, 0, 0, screenWidth, screenHeight, null);
        } else {
            g2.setColor(Color.BLACK);
            g2.fillRect(0, 0, screenWidth, screenHeight);
        }

        //Draw level selection title
        g2.setFont(new Font("Courier New", Font.BOLD, 56));
        String title = "SELECT LEVEL";
        FontMetrics titleFm = g2.getFontMetrics();
        int titleX = (screenWidth - titleFm.stringWidth(title)) / 2;
        g2.setColor(new Color(255, 255, 150));
        g2.drawString(title, titleX, 350);

        //Draw level options
        g2.setFont(menuFont);
        String[] levelOptions = {"Level 1", "Level 2", "Level 3"};
        int menuY = 480;
        int menuSpacing = 75;

        for (int i = 0; i < levelOptions.length; i++) {
            String option = levelOptions[i];
            FontMetrics menuFm = g2.getFontMetrics(menuFont);
            int menuX = (screenWidth - menuFm.stringWidth(option)) / 2;
            int currentY = menuY + (i * menuSpacing);

            //Highlight selected option
            if (i == levelOption) {
                //Draw selection indicator
                g2.setColor(new Color(255, 215, 0));
                int arrowSize = 12;
                int arrowX = menuX - 35;
                int arrowY = currentY - 20;
                
                g2.fillPolygon(new int[]{arrowX, arrowX + arrowSize, arrowX + arrowSize}, 
                              new int[]{arrowY, arrowY - arrowSize, arrowY + arrowSize}, 3);
                
                //Draw text with highlight
                g2.setColor(new Color(255, 255, 150));
                g2.drawString(option, menuX, currentY);
                
                //Add subtle glow effect
                g2.setColor(new Color(255, 215, 0, 100));
                g2.drawString(option, menuX + 1, currentY + 1);
            } else {
                //Draw normal text
                g2.setColor(new Color(255, 255, 255, 220));
                g2.drawString(option, menuX, currentY);
            }
        }

        //Draw back instruction
        g2.setFont(new Font("Courier New", Font.PLAIN, 24));
        String backText = "Press ESC to go back";
        FontMetrics backFm = g2.getFontMetrics();
        int backX = (screenWidth - backFm.stringWidth(backText)) / 2;
        g2.setColor(new Color(255, 255, 255, 180));
        g2.drawString(backText, backX, 680);
    }

    public void drawSetting(Graphics2D g2) {
        //Enable anti-aliasing for smoother rendering
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
        
        //Draw background
        if (menuBackground != null) {
            g2.drawImage(menuBackground, 0, 0, screenWidth, screenHeight, null);
        } else {
            g2.setColor(Color.BLACK);
            g2.fillRect(0, 0, screenWidth, screenHeight);
        }

        //Draw setting title
        g2.setFont(new Font("Courier New", Font.BOLD, 56));
        String title = "SETTINGS";
        FontMetrics titleFm = g2.getFontMetrics();
        int titleX = (screenWidth - titleFm.stringWidth(title)) / 2;
        g2.setColor(new Color(255, 255, 150));
        g2.drawString(title, titleX, 400);

        //Draw placeholder text
        g2.setFont(new Font("Courier New", Font.PLAIN, 32));
        String placeholder = "Settings menu coming soon...";
        FontMetrics placeholderFm = g2.getFontMetrics();
        int placeholderX = (screenWidth - placeholderFm.stringWidth(placeholder)) / 2;
        g2.setColor(new Color(255, 255, 255, 200));
        g2.drawString(placeholder, placeholderX, 500);

        //Draw back instruction
        g2.setFont(new Font("Courier New", Font.PLAIN, 24));
        String backText = "Press ESC to go back";
        FontMetrics backFm = g2.getFontMetrics();
        int backX = (screenWidth - backFm.stringWidth(backText)) / 2;
        g2.setColor(new Color(255, 255, 255, 180));
        g2.drawString(backText, backX, 680);
    }

    public void drawGameOver(Graphics2D g2) {
        //Enable anti-aliasing for smoother rendering
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
        
        //Draw background
        if (menuBackground != null) {
            g2.drawImage(menuBackground, 0, 0, screenWidth, screenHeight, null);
        } else {
            g2.setColor(Color.BLACK);
            g2.fillRect(0, 0, screenWidth, screenHeight);
        }

        //Draw victory title
        g2.setFont(new Font("Courier New", Font.BOLD, 64));
        String title = "CONGRATULATIONS!";
        FontMetrics titleFm = g2.getFontMetrics();
        int titleX = (screenWidth - titleFm.stringWidth(title)) / 2;
        g2.setColor(new Color(255, 215, 0)); // Gold color
        g2.drawString(title, titleX, 300);

        //Draw victory message
        g2.setFont(new Font("Courier New", Font.BOLD, 36));
        String message = "You Completed All Levels!";
        FontMetrics messageFm = g2.getFontMetrics();
        int messageX = (screenWidth - messageFm.stringWidth(message)) / 2;
        g2.setColor(new Color(255, 255, 150));
        g2.drawString(message, messageX, 380);

        //Draw instruction
        g2.setFont(new Font("Courier New", Font.PLAIN, 28));
        String instruction = "Press ENTER to return to main menu";
        FontMetrics instructionFm = g2.getFontMetrics();
        int instructionX = (screenWidth - instructionFm.stringWidth(instruction)) / 2;
        g2.setColor(new Color(255, 255, 255, 200));
        g2.drawString(instruction, instructionX, 500);
    }

    public void drawLevelComplete(Graphics2D g2) {
        //Enable anti-aliasing for smoother rendering
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
        
        //Draw semi-transparent overlay
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.7f));
        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, screenWidth, screenHeight);
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));

        //Draw "Level Complete!" message
        g2.setFont(new Font("Courier New", Font.BOLD, 72));
        String message = "Level Complete!";
        FontMetrics messageFm = g2.getFontMetrics();
        int messageX = (screenWidth - messageFm.stringWidth(message)) / 2;
        int messageY = screenHeight / 2;
        
        //Draw shadow
        g2.setColor(new Color(0, 0, 0, 150));
        g2.drawString(message, messageX + 3, messageY + 3);
        
        //Draw main text
        g2.setColor(new Color(255, 215, 0)); // Gold color
        g2.drawString(message, messageX, messageY);
        
        //Draw level number
        g2.setFont(new Font("Courier New", Font.BOLD, 36));
        String levelText = "Level " + currentLevel + " Completed!";
        FontMetrics levelFm = g2.getFontMetrics();
        int levelX = (screenWidth - levelFm.stringWidth(levelText)) / 2;
        g2.setColor(new Color(255, 255, 150));
        g2.drawString(levelText, levelX, messageY + 60);
    }
}
