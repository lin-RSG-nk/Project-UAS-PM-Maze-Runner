package ENTITY;

import AIMode.PathFinder;
import My2DMazeRunner.GamePanel;
import My2DMazeRunner.KeyHandler;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Player extends Entity {
    GamePanel gp;
    KeyHandler keyH;


    public boolean onPath = false; // Status apakah AI aktif
    PathFinder pFinder;
    boolean fKeyReleased = true;
    public boolean searching = false; // Status sedang scanning visual

    public Player(GamePanel gp, KeyHandler keyH) {
        this.gp = gp;
        this.keyH = keyH;

        setDefaultValues();
        getPlayerImage();
        setCollisionBox();

        // Inisialisasi Pathfinder
        pFinder = new PathFinder(gp);
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
        collisionWidth = 15;  // Lebih kecil dari 32 (tileSize)
        collisionHeight = 18; // Lebih kecil dari 32 (tileSize)

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

        if (keyH.fPressed && fKeyReleased) {
            onPath = !onPath;
            fKeyReleased = false;

            if(onPath) {
                // MULAI PROSES SEARCHING VISUAL
                startSearch();
            } else {
                // Stop dan reset
                searching = false;
                pFinder.pathList.clear();
            }
        }
        if (!keyH.fPressed) {
            fKeyReleased = true;
        }
        boolean isMoving = false;


        if (onPath) {

            if(searching) {
                // --- FASE SCANNING (VISUALISASI) ---
                // Jalankan 1 step scan per frame (atau bisa di loop 5x biar lebih cepet)
                boolean found = pFinder.searchStep();

                if(found) {
                    searching = false; // Selesai scanning, lanjut jalan
                }
                // Jika belum found, frame berikutnya akan lanjut scan lagi

            } else {
                // --- FASE JALAN (MOVEMENT) ---
                automatedMove();
            }

        } else {
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

            // Check collision (hanya untuk manual, AI diasumsikan sudah pintar menghindari tembok)
            if (isMoving) { // Cek collision hanya jika ada input gerak
                if (!checkCollision(newX, newY)) {
                    x = newX;
                    y = newY;
                }
            }
        }

        // Update posisi collision box (berlaku untuk Manual & AI)
        collisionX = x + collisionDefaultX;
        collisionY = y + collisionDefaultY;

        // Logic ini sekarang menangani animasi baik untuk AI maupun Manual
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
        }
    }

    public boolean checkCollision(int newX, int newY) {
        // Hitung posisi collision box yang baru
        int newCollisionX = newX + collisionDefaultX;
        int newCollisionY = newY + collisionDefaultY;

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

    public void startSearch() {
        int startCol = (x + collisionDefaultX) / gp.tileSize;
        int startRow = (y + collisionDefaultY) / gp.tileSize;
        int goalCol = gp.levelM.goalCol;
        int goalRow = gp.levelM.goalRow;

        pFinder.setNodes(startCol, startRow, goalCol, goalRow);
        searching = true; // Aktifkan mode scanning
    }

    public void draw(Graphics2D g2) {
        BufferedImage image = null;

        if(onPath) {
            pFinder.draw(g2);
        }

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
    // Method untuk memicu pencarian jalan
    public void searchPath() {
        // Konversi posisi pixel player ke grid
        int startCol = (x + collisionDefaultX) / gp.tileSize;
        int startRow = (y + collisionDefaultY) / gp.tileSize;

        // Ambil tujuan dari levelManager
        int goalCol = gp.levelM.goalCol;
        int goalRow = gp.levelM.goalRow;

        pFinder.setNodes(startCol, startRow, goalCol, goalRow);

        if (pFinder.searchStep()) {
            // Path ketemu
        } else {
            // Path tidak ketemu (mungkin tertutup tembok)
            onPath = false;
        }
    }

    // Method untuk menggerakkan player secara otomatis
    public boolean automatedMove() {

        if (pFinder.pathList.size() > 0) {

            // Ambil target node berikutnya
            int nextCol = pFinder.pathList.get(0).col;
            int nextRow = pFinder.pathList.get(0).row;

            int targetX = nextCol * gp.tileSize;
            int targetY = nextRow * gp.tileSize;

            // Tentukan arah & gerak (Logic sederhana mendekati target)
            if (y < targetY) {
                y += speed;
                direction = "down";
            } else if (y > targetY) {
                y -= speed;
                direction = "up";
            }
            if (x < targetX) {
                x += speed;
                direction = "right";
            } else if (x > targetX) {
                x -= speed;
                direction = "left";
            }

            // Cek jarak ke target. Jika sudah dekat, "snap" ke posisi dan hapus node dari list
            int distCmdX = Math.abs(x - targetX);
            int distCmdY = Math.abs(y - targetY);

            if(distCmdX < speed && distCmdY < speed) {
                x = targetX;
                y = targetY;
                pFinder.pathList.remove(0); // Hapus node yang sudah dilewati
            }

            return true; // Player sedang bergerak

        } else {
            // Path habis (sudah sampai atau berhenti)
            return false;
        }
    }
    // Masukkan di dalam class Player (misalnya di bawah method setDefaultValues)
    public void resetState() {
        // 1. Matikan mode AI
        onPath = false;

        // 2. Bersihkan sisa jalur dari level sebelumnya
        if (pFinder != null) {
            pFinder.pathList.clear();
            pFinder.resetNodes(); // Opsional: reset status node di pathfinder
        }

        // 3. Reset animasi/arah (opsional, agar terlihat rapi saat mulai)
        direction = "down";
        spriteNumber = 1;
    }

}