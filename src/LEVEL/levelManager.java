package LEVEL;

import My2DMazeRunner.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;


public class levelManager {

    GamePanel gp;
    Tingkatan [] level ;

    public levelManager(GamePanel gp){
        this.gp = gp;
        level = new Tingkatan[10];

        getLevelImage();
    }
    public void getLevelImage(){

        try{
            level [0] = new Tingkatan();
            level [0].image = ImageIO.read(getClass().getResourceAsStream("/Tiles/earth.png"));

            level [1] = new Tingkatan();
            level [1].image = ImageIO.read(getClass().getResourceAsStream("/Tiles/wall.png"));

        }catch (IOException e){
            e.printStackTrace();;
        }

    }
    public void draw (Graphics2D g2){
        g2.drawImage(level[0].image, 0, 0, gp.tileSize, gp.tileSize, null);
    }

}
