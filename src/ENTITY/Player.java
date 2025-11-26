package ENTITY;

import My2DMazeRunner.GamePanel;
import My2DMazeRunner.KeyHandler;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Player extends Entity {
    GamePanel gp;
    KeyHandler keyH;

    public Player(GamePanel gp, KeyHandler keyH) {
        this.gp = gp;
        this.keyH = keyH;
        setDefaultValues();
        getPlayerImage();
        setCollisionBox();
    }

    public void setDefaultValues() {
        if (gp.levelM != null) {
            x = gp.levelM.startCol * gp.tileSize;
            y = gp.levelM.startRow * gp.tileSize;
        } else {
            x = gp.tileSize;
            y = gp.tileSize;
        }
        speed = 4;
        direction = "down";
    }

    // Set collision box yang lebih kecil dari karakter
    public void setCollisionBox() {
        // Ukuran collision box (lebih kecil dari tileSize)
        collisionWidth = 17;  // Lebih kecil dari 32 (tileSize)
        collisionHeight = 17; // Lebih kecil dari 32 (tileSize)

        // Posisi collision box (di tengah karakter)
        collisionDefaultX = (gp.tileSize - collisionWidth) / 2;
        collisionDefaultY = (gp.tileSize - collisionHeight) / 2;

        collisionX = x + collisionDefaultX;
        collisionY = y + collisionDefaultY;
    }

    public void getPlayerImage() {
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

    public void update() {
        // Update collision box position
        collisionX = x + collisionDefaultX;
        collisionY = y + collisionDefaultY;

        // Reset movement flags
        boolean isMoving = false;
        int newX = x;
        int newY = y;

        // Handle movement input
        if (keyH.upPressed) {
            direction = "up";
            newY -= speed;
            isMoving = true;
        } else if (keyH.downPressed) {
            direction = "down";
            newY += speed;
            isMoving = true;
        } else if (keyH.leftPressed) {
            direction = "left";
            newX -= speed;
            isMoving = true;
        } else if (keyH.rightPressed) {
            direction = "right";
            newX += speed;
            isMoving = true;
        }

        // Check collision sebelum bergerak (menggunakan collision box)
        if (!checkCollision(newX, newY)) {
            x = newX;
            y = newY;
        }

        // Handle animasi hanya ketika bergerak
        if (isMoving) {
            spriteCounter++;
            if (spriteCounter > 10) {
                if (spriteNumber == 1) {
                    spriteNumber = 2;
                } else if (spriteNumber == 2) {
                    spriteNumber = 1;
                }
                spriteCounter = 0;
            }
        } else {
            // reset ke gambar 1 kalo ga gerak
            spriteNumber = 1;
            spriteCounter = 0;
        }
    }

    public boolean checkCollision(int newX, int newY) {
        // Hitung posisi collision box yang baru
        int newCollisionX = newX + collisionDefaultX;
        int newCollisionY = newY + collisionDefaultY;

        // Check collision dengan collision box yang lebih kecil
        int leftCol = newCollisionX / gp.tileSize;
        int rightCol = (newCollisionX + collisionWidth - 1) / gp.tileSize;
        int topRow = newCollisionY / gp.tileSize;
        int bottomRow = (newCollisionY + collisionHeight - 1) / gp.tileSize;

        int tileNum1, tileNum2;

        // cek sekeliling menggunakan collision box
        tileNum1 = gp.levelM.getTileType(newCollisionX, newCollisionY);
        tileNum2 = gp.levelM.getTileType(newCollisionX + collisionWidth - 1, newCollisionY);
        if (tileNum1 == 1 || tileNum2 == 1) {
            return true;
        }

        tileNum1 = gp.levelM.getTileType(newCollisionX, newCollisionY + collisionHeight - 1);
        tileNum2 = gp.levelM.getTileType(newCollisionX + collisionWidth - 1, newCollisionY + collisionHeight - 1);
        if (tileNum1 == 1 || tileNum2 == 1) {
            return true;
        }

        return false;
    }

    public void draw(Graphics2D g2) {
        BufferedImage image = null;

        switch (direction) {
            case "up":
                if (spriteNumber == 1) {
                    image = up1;
                } else {
                    image = up2;
                }
                break;
            case "down":
                if (spriteNumber == 1) {
                    image = down1;
                } else {
                    image = down2;
                }
                break;
            case "left":
                if (spriteNumber == 1) {
                    image = left1;
                } else {
                    image = left2;
                }
                break;
            case "right":
                if (spriteNumber == 1) {
                    image = right1;
                } else {
                    image = right2;
                }
                break;
        }
        g2.drawImage(image, x, y, gp.tileSize, gp.tileSize, null);

        // DEBUG: Gambar collision box (bisa di-comment setelah testing)
        // drawCollisionBox(g2);
    }

    // Method untuk debugging - gambar collision box
    public void drawCollisionBox(Graphics2D g2) {
        g2.setColor(Color.RED);
        g2.drawRect(collisionX, collisionY, collisionWidth, collisionHeight);
    }
}