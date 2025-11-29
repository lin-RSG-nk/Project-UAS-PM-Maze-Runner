package AIMode;

import My2DMazeRunner.GamePanel;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;

public class PathFinder {

    GamePanel gp;
    Node[][] node;
    ArrayList<Node> openList = new ArrayList<>();
    public ArrayList<Node> pathList = new ArrayList<>();
    Node startNode, goalNode, currentNode;
    boolean goalReached = false;
    int step = 0;

    public PathFinder(GamePanel gp) {
        this.gp = gp;
        instantiateNodes();
    }

    public void instantiateNodes() {
        node = new Node[gp.maxScreenCol][gp.maxScreenRow];
        int col = 0;
        int row = 0;
        while (col < gp.maxScreenCol && row < gp.maxScreenRow) {
            node[col][row] = new Node(col, row);
            col++;
            if (col == gp.maxScreenCol) {
                col = 0;
                row++;
            }
        }
    }

    public void resetNodes() {
        int col = 0;
        int row = 0;
        while (col < gp.maxScreenCol && row < gp.maxScreenRow) {
            node[col][row].open = false;
            node[col][row].checked = false;
            node[col][row].solid = false;
            node[col][row].parent = null;
            col++;
            if (col == gp.maxScreenCol) {
                col = 0;
                row++;
            }
        }
        openList.clear();
        pathList.clear();
        goalReached = false;
        step = 0;
    }

    public void setNodes(int startCol, int startRow, int goalCol, int goalRow) {
        resetNodes();
        startNode = node[startCol][startRow];
        currentNode = startNode;
        goalNode = node[goalCol][goalRow];

        openList.add(startNode); // Masukkan start node ke antrian awal

        for (int col = 0; col < gp.maxScreenCol; col++) {
            for (int row = 0; row < gp.maxScreenRow; row++) {
                int tileNum = gp.levelM.getTileType(col * gp.tileSize, row * gp.tileSize);
                if (tileNum == 1) {
                    node[col][row].solid = true;
                }
            }
        }
    }

    // METHOD BARU: Search per langkah (bukan loop while penuh)
    public boolean searchStep() {

        if (goalReached) return true; // Sudah ketemu
        if (openList.isEmpty()) return false; // Jalan buntu/habis

        // Ambil node antrian pertama (BFS)
        currentNode = openList.get(0);

        // Cek apakah ini goal
        if (currentNode == goalNode) {
            goalReached = true;
            trackThePath();
            return true;
        }

        // Tandai sebagai checked dan hapus dari antrian
        currentNode.checked = true;
        openList.remove(0);

        // Buka tetangga
        int col = currentNode.col;
        int row = currentNode.row;

        if(row - 1 >= 0) openNode(node[col][row-1]);
        if(col - 1 >= 0) openNode(node[col-1][row]);
        if(row + 1 < gp.maxScreenRow) openNode(node[col][row+1]);
        if(col + 1 < gp.maxScreenCol) openNode(node[col+1][row]);

        return false; // Belum ketemu, lanjut langkah berikutnya
    }

    public void openNode(Node node) {
        if (!node.open && !node.checked && !node.solid) {
            node.open = true;
            node.parent = currentNode;
            openList.add(node);
        }
    }

    public void trackThePath() {
        Node current = goalNode;
        while (current != startNode) {
            pathList.add(0, current);
            current = current.parent;
        }
    }

    // VISUALISASI: Menggambar kotak proses
    public void draw(Graphics2D g2) {

        // Jangan gambar jika pathfinder belum diset (misal node null)
        if(node == null) return;

        for (int col = 0; col < gp.maxScreenCol; col++) {
            for (int row = 0; row < gp.maxScreenRow; row++) {

                // Lewati node yang belum diapa-apain
                if(!node[col][row].checked && !node[col][row].open) continue;

                int x = col * gp.tileSize;
                int y = row * gp.tileSize;

                // WARNA 1: CHECKED (Sudah dilewati/diperiksa) -> ORANGE
                if (node[col][row].checked) {
                    g2.setColor(new Color(255, 165, 0, 100)); // Transparan
                    g2.fillRect(x, y, gp.tileSize, gp.tileSize);
                }
                // WARNA 2: OPEN (Sedang dalam antrian untuk dicek) -> HIJAU
                else if (node[col][row].open) {
                    g2.setColor(new Color(0, 255, 0, 100));
                    g2.fillRect(x, y, gp.tileSize, gp.tileSize);
                }
            }
        }

        // WARNA 3: PATH (Jalur final) -> MERAH
        if(goalReached) {
            g2.setColor(new Color(255, 0, 0, 100));
            for(int i = 0; i < pathList.size(); i++) {
                int x = pathList.get(i).col * gp.tileSize;
                int y = pathList.get(i).row * gp.tileSize;
                g2.fillRect(x, y, gp.tileSize, gp.tileSize);
            }
        }
    }
}