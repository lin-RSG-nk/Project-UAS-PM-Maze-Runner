package My2DMazeRunner;

import ENTITY.Player;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel implements Runnable{
    //Screeen Setting
    final int originalTileSize = 16;
    final int scale = 2;

    public final int tileSize = originalTileSize  * scale;
    public final int screenWidth = 1280;
    public final int screenHeight = 720;

    //FPS
    int fps = 60;

    KeyHandler keyH = new KeyHandler();
    Thread gameThread;
    Player player = new Player(this, keyH);

    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);

    }

    public void startThread(){
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {


        double drawInterval = 1000000000/fps;
        double nextDrawtime = System.nanoTime() + drawInterval;

        while(gameThread != null){

            //Update : perubahan setiap inputan akann di tampilkan melaluli ini
            update();
            //Draw : dimana setelah data di updat maka akan di munculkan di interface perubahannya
            repaint();


            try {
                double remainingTime = nextDrawtime - System.nanoTime();
                remainingTime = remainingTime / 1000000;

                if (remainingTime < 0){
                    remainingTime = 0;
                }

                Thread.sleep((long)remainingTime);

                nextDrawtime += drawInterval;

            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void update(){
        player.update();
    }
    public void paintComponent(Graphics g){
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;

        player.draw(g2);

        g2.dispose();
    }
}
