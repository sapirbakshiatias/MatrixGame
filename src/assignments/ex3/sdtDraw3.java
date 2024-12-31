package assignments.ex3;
import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Stack;
import java.util.concurrent.TimeUnit;

public class sdtDraw3 {
    private static final int SQUARE_SIZE = 50;
    private static final int SPACE_SIZE = 5;

    private static int[][] currentMat;

    public static void main(String[] a) throws InterruptedException {
        int[][] mat1 = {
                {1, 1, 1, 1, 1},
                {1, -1, 1, -1, 1},
                {1, -1, -1, -1, 1},
                {1, -1, 1, -1, 1},
                {1, -1, 1, -1, 1},
                {1, -1, 1, -1, 1}

        };
        drawMat(mat1);

        Scanner sc = new Scanner(System.in);
        boolean cont = true;
        while (cont) {
            System.out.println("Which function do you wish to implement? \n" +
                    "1. Fill ; cyclic\n" +
                    "2. Fill ;  not cyclic\n" +
                    "3. Shortest path ;  cyclic\n" +
                    "4. Shortest path ; not cyclic\n" +
                    "5. ConnectedComponents ; cyclic\n" +
                    "6. ConnectedComponents ; not cyclic\n" +
                    "7. Dist ; cyclic\n" +
                    "8. Dist ; not cyclic\n" +
                    "Write your choice here");
            int choice = sc.nextInt();

            switch (choice) {
                case 1:
                    currentMat = mat1;
                    drawFill(mat1, new Index2D(0, 0), 2, true);
                    break;
                case 2:
                    currentMat = mat1;
                    drawFill(mat1, new Index2D(0, 0), 3, false);
                    break;
                case 3:
                    currentMat = mat1;
                    drawShortestPath(mat1, new Pixel2D[]{new Index2D(0, 0), new Index2D(1, 0), new Index2D(3, 2)}, -1, true);
                    break;
                case 4:
                    currentMat = mat1;
                    drawShortestPath(mat1, new Pixel2D[]{new Index2D(0, 0), new Index2D(1, 0), new Index2D(3, 2)}, 0, false);
                    break;
                case 5:
                    currentMat = mat1;
                    drawConnectedComponents(mat1, -1, true);
                    break;
                case 6:
                    currentMat = mat1;
                    drawConnectedComponents(mat1, -1, false);
                    break;
                case 7:
                    currentMat = mat1;
                    drawDist(mat1, new Index2D(0, 0), -1, true);
                    break;
                case 8:
                    currentMat = mat1;
                    drawDist(mat1, new Index2D(0, 0), -1, false);
                    break;
                case 9:
                    cont = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please enter a number between 1 and 9.");
                    break;
            }
        }
    }


    public static void drawMat(int[][] mat) {
        JFrame frame = new JFrame();
        int mapWidth = mat[0].length * (SQUARE_SIZE + SPACE_SIZE);
        int mapHeight = mat.length * (SQUARE_SIZE + SPACE_SIZE);

        frame.setSize(mapWidth, mapHeight);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                for (int y = 0; y < mat.length; y++) {
                    for (int x = 0; x < mat[0].length; x++) {
                        int v = mat[y][x];
                        Color color;
                        switch (v) {
                            case -1:
                                color = Color.GRAY;
                                break;
                            case 0:
                                color = Color.PINK;
                                break;
                            case 1:
                                color = Color.WHITE;
                                break;
                            case 2:
                                color = Color.RED;
                                break;
                            case 3:
                                color = Color.BLUE;
                                break;
                            case 4:
                                color = Color.YELLOW;
                                break;
                            case 5:
                                color = Color.ORANGE;
                                break;
                            case 6:
                                color = Color.CYAN;
                                break;
                            case 7:
                                color = Color.MAGENTA;
                                break;
                            default:
                                Color[] moreColors = {Color.LIGHT_GRAY, Color.DARK_GRAY, Color.BLACK};
                                color = (v < moreColors.length) ? moreColors[v] : moreColors[v % moreColors.length];
                                break;
                        }
                        int x1 = x * (SQUARE_SIZE + SPACE_SIZE);
                        int y1 = y * (SQUARE_SIZE + SPACE_SIZE);
                        g.setColor(color);
                        g.fillRect(x1, y1, SQUARE_SIZE, SQUARE_SIZE);

                        // Draw number on each cell
                        g.setColor(Color.BLACK);
                        g.drawString(String.valueOf(v), x1 + SQUARE_SIZE / 2, y1 + SQUARE_SIZE / 2);
                    }
                }
            }
        });
        frame.setSize(mapWidth + 10, mapHeight+40);
        frame.setVisible(true);
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void drawFill(int[][] matrix, Pixel2D point, int newColor, boolean cyclic) {
        Map map = new Map(matrix);
        map.setCyclic(cyclic);
        map.fill(point, newColor);
        drawMat(map.getMap());
    }

    public static void drawDist(int[][] matrix, Pixel2D point, int obsColor, boolean cyclic) {
        Map map = new Map(matrix);
        map.setCyclic(cyclic);
        Map2D myMap = map.allDistance(point, obsColor);
        drawMat(myMap.getMap());
    }

    public static void drawShortestPath(int[][] mat, Pixel2D[] points, int obsColor, boolean cyclic) {
        Map map = new Map(mat);
        map.setCyclic(cyclic);
        Pixel2D[] path = map.shortestPath(points, obsColor);
        if (path != null) {
            drawPath(map, path);
        } else {
            System.out.println("No valid path found.");
        }
    }
    private static void drawPath(Map map, Pixel2D[] path) {
        for (Pixel2D p : path) {
            if (p != null) {
                map.setPixel(p, 4);
            }
        }
        drawMat(map.getMap());
    }

public static void drawConnectedComponents(int[][] mat, int obsColor, boolean cyclic) {
    Map2D map = new Map(mat);
    map.setCyclic(cyclic);
    int groups = map.numberOfConnectedComponents(obsColor);
    drawMat(map.getMap());
    System.out.println("Number of connected components: " + groups);
}
}
