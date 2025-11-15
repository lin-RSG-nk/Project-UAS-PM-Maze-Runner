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
        x = 100;
        y = 100;
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

        if (keyH.downPressed == true || keyH.leftPressed == true || keyH.upPressed == true || keyH.rightPressed == true){
            if (keyH.upPressed == true){
                direction = "up";
                y -= speed;


            } else if (keyH.downPressed == true) {
                direction = "down";
                y += speed;
            } else if (keyH.leftPressed == true) {
                direction = "left";
                x -= speed;

            } else if (keyH.rightPressed == true) {
                direction = "right";
                x += speed;
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
