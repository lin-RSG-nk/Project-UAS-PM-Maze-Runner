package LEVEL;

import My2DMazeRunner.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


public class levelManager {

    GamePanel gp;
    Tingkatan [] level ;
    int[][] mapLevelnum;

    public levelManager(GamePanel gp){
        this.gp = gp;
        level = new Tingkatan[10];

        mapLevelnum = new int[gp.maxScreenRow][gp.maxScreenCol];
        getLevelImage();
        loadMap(1); // Load default level 1
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
    public void loadMap(int levelNumber) {
        try {
            String mapFile;
            switch (levelNumber) {
                case 1:
                    mapFile = "/Map/Maplvl1Example.txt";
                    break;
                case 2:
                    mapFile = "/Map/Maplvl2Example.txt";
                    break;
                case 3:
                    // If Level 3 map doesn't exist, use Level 2 map
                    mapFile = "/Map/Maplvl2Example.txt";
                    break;
                default:
                    mapFile = "/Map/Maplvl1Example.txt";
                    break;
            }
            
            InputStream is = getClass().getResourceAsStream(mapFile);
            if (is == null) {
                // Fallback to level 1 if file doesn't exist
                is = getClass().getResourceAsStream("/Map/Maplvl1Example.txt");
            }
            
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            int col = 0;
            int row = 0;

            while (row < gp.maxScreenRow) {       // 24 row
                String line = br.readLine();
                if (line == null) break; // Handle case where file has fewer rows
                String[] numbers = line.split(" ");

                while (col < gp.maxScreenCol && col < numbers.length) {   // 40 col
                    int num = Integer.parseInt(numbers[col]);
                    mapLevelnum[row][col] = num;
                    col++;
                }
                col = 0;
                row++;
            }

            br.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void draw (Graphics2D g2){
        int col = 0;
        int row = 0;
        int x = 0;
        int y = 0;

        while (row < gp.maxScreenRow) {
            while (col < gp.maxScreenCol) {
                int mapNum = mapLevelnum[row][col];
                g2.drawImage(level[mapNum].image, x, y, gp.tileSize, gp.tileSize, null);
                col++;
                x += gp.tileSize;
            }
            col = 0;
            x = 0;
            row++;
            y += gp.tileSize;
        }

    }

}










