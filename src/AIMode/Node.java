package AIMode;

public class Node {
    public int col;
    public int row;
    public boolean open;
    public boolean checked;
    public boolean solid;
    public Node parent;

    public Node(int col, int row) {
        this.col = col;
        this.row = row;
    }
}