package LEVEL;

import My2DMazeRunner.GamePanel;

import javax.imageio.ImageIO;
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
            level [0].image = ImageIO.read(getClass().getResourceAsStream("Resource/Tiles/earth.png"));
        }catch (IOException e){
            e.printStackTrace();;
        }

    }

}
