package ENTITY;

import java.awt.image.BufferedImage;

public class Entity {
    public int x, y;
    public int speed;
    public BufferedImage up1, up2, down1, down2, left1, left2, right1, right2;
    public String direction;
    public int spriteCounter = 0;
    public int spriteNumber = 1;

    // Collision box variables
    public int collisionX, collisionY;
    public int collisionWidth, collisionHeight;
    public int collisionDefaultX, collisionDefaultY;
}