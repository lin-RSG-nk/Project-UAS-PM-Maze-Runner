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
    public int startRow = 1, startCol = 1; // Start position (S)
    public int goalRow = 22, goalCol = 37; // Goal position (G)

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

            // Portal/Finish point uses earth tile (will be drawn with special effect)
            level [2] = new Tingkatan();
            level [2].image = ImageIO.read(getClass().getResourceAsStream("/Tiles/earth.png"));

        }catch (IOException e){
            e.printStackTrace();;
        }

    }
    public void loadMap(int levelNumber) {
        try {
            String mapFile;
            switch (levelNumber) {
                case 1:
                    mapFile = "/Map/Maplvl1.txt";
                    break;
                case 2:
                    mapFile = "/Map/Maplvl2.txt";
                    break;
                case 3:
                    mapFile = "/Map/Maplvl3.txt";
                    break;
                default:
                    mapFile = "/Map/Maplvl1.txt";
                    break;
            }
            
            InputStream is = getClass().getResourceAsStream(mapFile);
            if (is == null) {
                // Fallback to level 1 if file doesn't exist
                is = getClass().getResourceAsStream("/Map/Maplvl1.txt");
            }
            
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            int row = 0;

            while (row < gp.maxScreenRow) {       // 24 row
                String line = br.readLine();
                if (line == null) break; // Handle case where file has fewer rows
                
                // Remove spaces if any, then process character by character
                line = line.replaceAll("\\s+", "");
                
                for (int col = 0; col < gp.maxScreenCol && col < line.length(); col++) {
                    char ch = line.charAt(col);
                    switch (ch) {
                        case '#': // Wall
                            mapLevelnum[row][col] = 1;
                            break;
                        case '.': // Path (earth)
                            mapLevelnum[row][col] = 0;
                            break;
                        case 'S': // Start position
                            mapLevelnum[row][col] = 0; // Start is also a path
                            startRow = row;
                            startCol = col;
                            break;
                        case 'G': // Goal position
                            mapLevelnum[row][col] = 2; // Goal uses value 2
                            goalRow = row;
                            goalCol = col;
                            break;
                        default:
                            mapLevelnum[row][col] = 1; // Default to wall
                            break;
                    }
                }
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
                
                // Draw portal effect for finish point (value 2)
                if (mapNum == 2) {
                    // Draw glowing portal effect
                    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.7f));
                    g2.setColor(new Color(135, 206, 250, 150)); // Light blue glow
                    g2.fillOval(x + 4, y + 4, gp.tileSize - 8, gp.tileSize - 8);
                    g2.setColor(new Color(255, 255, 255, 200)); // White center
                    g2.fillOval(x + 8, y + 8, gp.tileSize - 16, gp.tileSize - 16);
                    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
                }
                
                col++;
                x += gp.tileSize;
            }
            col = 0;
            x = 0;
            row++;
            y += gp.tileSize;
        }

    }
    
    // Get tile type at world coordinates
    public int getTileType(int worldX, int worldY) {
        int col = worldX / gp.tileSize;
        int row = worldY / gp.tileSize;
        
        if (col < 0 || col >= gp.maxScreenCol || row < 0 || row >= gp.maxScreenRow) {
            return 1; // Wall if out of bounds
        }
        
        return mapLevelnum[row][col];
    }
    
    // Check if position is finish point
    public boolean isFinishPoint(int worldX, int worldY) {
        int col = worldX / gp.tileSize;
        int row = worldY / gp.tileSize;
        
        if (col < 0 || col >= gp.maxScreenCol || row < 0 || row >= gp.maxScreenRow) {
            return false;
        }
        
        return mapLevelnum[row][col] == 2;
    }

}










