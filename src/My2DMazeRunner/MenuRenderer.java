package My2DMazeRunner;

import java.awt.*;

public class MenuRenderer {
    private static final Font MENU_FONT = new Font("Courier New", Font.BOLD, 48);
    private static final String[] MENU_OPTIONS = {"Level", "Setting", "Exit"};

    public static void render(Graphics2D g2, GamePanel gp, int menuOption) {
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);

        if (gp.menuBackground != null) {
            g2.drawImage(gp.menuBackground, 0, 0, gp.screenWidth, gp.screenHeight, null);
        } else {
            g2.setColor(Color.BLACK);
            g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);
        }

        g2.setFont(MENU_FONT);
        int menuY = 420;
        int menuSpacing = 75;

        for (int i = 0; i < MENU_OPTIONS.length; i++) {
            renderMenuOption(g2, gp, MENU_OPTIONS[i], i, menuOption, menuY + (i * menuSpacing));
        }
    }

    private static void renderMenuOption(Graphics2D g2, GamePanel gp, String option,
                                         int index, int selectedIndex, int y) {
        FontMetrics fm = g2.getFontMetrics();
        int x = (gp.screenWidth - fm.stringWidth(option)) / 2;

        if (index == selectedIndex) {
            renderSelectedOption(g2, option, x, y);
        } else {
            renderNormalOption(g2, option, x, y);
        }
    }

    private static void renderSelectedOption(Graphics2D g2, String option, int x, int y) {
        g2.setColor(new Color(255, 215, 0));
        int arrowSize = 12;
        int arrowX = x - 35;
        int arrowY = y - 20;

        g2.fillPolygon(new int[]{arrowX, arrowX + arrowSize, arrowX + arrowSize},
                new int[]{arrowY, arrowY - arrowSize, arrowY + arrowSize}, 3);

        g2.setColor(new Color(255, 255, 150));
        g2.drawString(option, x, y);

        g2.setColor(new Color(255, 215, 0, 100));
        g2.drawString(option, x + 1, y + 1);
    }

    private static void renderNormalOption(Graphics2D g2, String option, int x, int y) {
        g2.setColor(new Color(255, 255, 255, 220));
        g2.drawString(option, x, y);
    }
}