import javax.swing.*;
import java.awt.*;

public class GridPlotter extends JPanel {
    private final int[][] grid;
    private final int cellSize = 20; // Size of each cell in the grid

    public GridPlotter(int[][] grid) {
        this.grid = grid;
        int gridWidth = grid[0].length * cellSize;
        int gridHeight = grid.length * cellSize;
        setPreferredSize(new Dimension(gridWidth, gridHeight));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid[row].length; col++) {
                int x = col * cellSize;
                int y = row * cellSize;
                Color color = grid[row][col] == 1 ? Color.BLACK : Color.WHITE;
                g.setColor(color);
                g.fillRect(x, y, cellSize, cellSize);

                // Optionally draw a border around each cell
                g.setColor(Color.GRAY);
                g.drawRect(x, y, cellSize, cellSize);
            }
        }
    }

    public static void createAndShowGui(int[][] grid, int iteration, String name) {
        JFrame frame = new JFrame(iteration + ", " + name);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(new GridPlotter(grid));
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
