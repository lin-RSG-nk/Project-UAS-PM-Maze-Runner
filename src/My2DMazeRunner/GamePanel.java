package My2DMazeRunner;

import ENTITY.Player;
import LEVEL.Tingkatan;
import LEVEL.levelManager;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class GamePanel extends JPanel implements Runnable{
    //Screen Setting
    final int originalTileSize = 16;
    final int scale = 2;

    public final int tileSize = originalTileSize  * scale;
    public final int screenWidth = 1280; //80 asset
    public final int screenHeight = 720; //45 asset

    //Game State
    public final int MENU_STATE = 0;
    public final int PLAYING_STATE = 1;
    public int gameState = MENU_STATE;

    //FPS
    int fps = 60;

    KeyHandler keyH = new KeyHandler();

    Thread gameThread;
    Player player = new Player(this, keyH);

    //Menu
    BufferedImage menuBackground;
    int menuOption = 0; // 0: New Game, 1: Continue, 2: Exit
    Font menuFont;

    levelManager levelM = new levelManager(this);

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
        } else if (gameState == PLAYING_STATE) {
            //Check if ESC is pressed to return to menu
            if (keyH.escPressed && !keyH.escWasPressed) {
                gameState = MENU_STATE;
                keyH.escWasPressed = true;
            } else if (!keyH.escPressed) {
                keyH.escWasPressed = false;
            }
            
            player.update();
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
            case 0: // New Game
                startNewGame();
                break;
            case 1: // Continue
                continueGame();
                break;
            case 2: // Exit
                System.exit(0);
                break;
        }
    }

    public void startNewGame() {
        gameState = PLAYING_STATE;
        player.setDefaultValues();
        // Reset game state here if needed

    }

    public void continueGame() {
        gameState = PLAYING_STATE;
        // Load saved game state here if needed
        // For now, it's the same as new game
    }
    public void paintComponent(Graphics g){
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;

        if (gameState == MENU_STATE) {
            drawMenu(g2);
        } else if (gameState == PLAYING_STATE) {
            levelM.draw(g2);
            player.draw(g2);
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
        String[] menuOptions = {"New Game", "Continue", "Exit"};
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
}
