package ENTITY;

import My2DMazeRunner.GamePanel;
import My2DMazeRunner.KeyHandler;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Player extends Entity{
    GamePanel gp;
    KeyHandler keyH;

    public Player (GamePanel gp, KeyHandler keyH){

        this.gp  = gp;
        this.keyH = keyH;

        setDefaultValues();
        getPlayerimage();

    }
    public void setDefaultValues(){
        // Start position at S position from level manager
        // If levelM is not initialized yet, use default position
        if (gp.levelM != null) {
            x = gp.levelM.startCol * gp.tileSize;
            y = gp.levelM.startRow * gp.tileSize;
        } else {
            // Fallback to default position (row 1, col 1)
            x = gp.tileSize;
            y = gp.tileSize;
        }
        speed = 4;
        direction = "down";
    }
    public void getPlayerimage(){
        try {
            down1 = ImageIO.read(getClass().getResourceAsStream("/Player/boy_down_1.png"));
            down2 = ImageIO.read(getClass().getResourceAsStream("/Player/boy_down_2.png"));
            up1 = ImageIO.read(getClass().getResourceAsStream("/Player/boy_up_1.png"));
            up2 = ImageIO.read(getClass().getResourceAsStream("/Player/boy_up_2.png"));
            left1 = ImageIO.read(getClass().getResourceAsStream("/Player/boy_left_1.png"));
            left2 = ImageIO.read(getClass().getResourceAsStream("/Player/boy_left_2.png"));
            right1 = ImageIO.read(getClass().getResourceAsStream("/Player/boy_right_1.png"));
            right2 = ImageIO.read(getClass().getResourceAsStream("/Player/boy_right_2.png"));


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void update(){
        int newX = x;
        int newY = y;

        if (keyH.downPressed || keyH.leftPressed || keyH.upPressed || keyH.rightPressed){
            if (keyH.upPressed){
                direction = "up";
                newY -= speed;
            } else if (keyH.downPressed) {
                direction = "down";
                newY += speed;
            } else if (keyH.leftPressed) {
                direction = "left";
                newX -= speed;
            } else if (keyH.rightPressed) {
                direction = "right";
                newX += speed;
            }
            
            // Check collision before moving
            if (!checkCollision(newX, newY)) {
                x = newX;
                y = newY;
            }
            
            spriteCounter ++;
            if (spriteCounter > 8){
                if (spriteNumber ==1){
                    spriteNumber = 2;
                } else if (spriteNumber == 2) {
                    spriteNumber = 1;
                }
                spriteCounter = 0;
            }
        }
    }
    
    public boolean checkCollision(int newX, int newY) {
        // Check collision with walls
        int leftCol = newX / gp.tileSize;
        int rightCol = (newX + gp.tileSize - 1) / gp.tileSize;
        int topRow = newY / gp.tileSize;
        int bottomRow = (newY + gp.tileSize - 1) / gp.tileSize;
        
        int tileNum1, tileNum2;
        
        // Check top-left and top-right corners
        tileNum1 = gp.levelM.getTileType(newX, newY);
        tileNum2 = gp.levelM.getTileType(newX + gp.tileSize - 1, newY);
        if (tileNum1 == 1 || tileNum2 == 1) {
            return true; // Collision with wall
        }
        
        // Check bottom-left and bottom-right corners
        tileNum1 = gp.levelM.getTileType(newX, newY + gp.tileSize - 1);
        tileNum2 = gp.levelM.getTileType(newX + gp.tileSize - 1, newY + gp.tileSize - 1);
        if (tileNum1 == 1 || tileNum2 == 1) {
            return true; // Collision with wall
        }
        
        return false; // No collision
    }
    public  void draw(Graphics2D g2){
//        g2.setColor(Color.white);
//        g2.fillRect(x,y, gp.tileSize, gp.tileSize);
        BufferedImage image = null;

        switch (direction){
            case "up":
                if (spriteNumber == 1){
                    image = up1;
                }else {
                    image = up2;
                }
                break;
            case "down":
                if (spriteNumber == 1){
                    image = down1;
                }else {
                    image = down2;
                }                break;
            case "left":
                if (spriteNumber == 1){
                    image = left1;
                }else {
                    image = left2;
                }
                break;
            case "right":
                if (spriteNumber == 1){
                    image = right1;
                }else {
                    image = right2;
                }
                break;
        }
        g2.drawImage(image, x, y ,gp.tileSize, gp.tileSize, null);
    }
}
