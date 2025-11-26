package ENTITY;

import java.awt.image.BufferedImage;

public class PlayerAnimation {

    public void updateSpriteCounter() {
        // Animation logic can be moved here if needed
    }

    public BufferedImage getCurrentImage(Player player) {
        switch (player.direction) {
            case "up":
                return (player.spriteNumber == 1) ? player.up1 : player.up2;
            case "down":
                return (player.spriteNumber == 1) ? player.down1 : player.down2;
            case "left":
                return (player.spriteNumber == 1) ? player.left1 : player.left2;
            case "right":
                return (player.spriteNumber == 1) ? player.right1 : player.right2;
            default:
                return player.down1;
        }
    }
}