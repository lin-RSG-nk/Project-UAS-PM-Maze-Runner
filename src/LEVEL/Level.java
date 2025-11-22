package LEVEL;

import My2DMazeRunner.GamePanel;

public class Level {

    GamePanel gp;
    Level [] level;
    Level1 Level = new Level1();


    public Level (GamePanel gp){
        this.gp = gp;

        level = new Level [5];
        Level.getLevelImage();
    }

}
