package assignments.ex3;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.awt.*;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.jupiter.api.Assertions.*;
/**
 This is a very basic Testing class for Map - please note that this JUnit
 contains only a very limited testing method and should be added many other
 methods for testing all the functionality of Map2D - both in correctness and in runtime.
 */
class MapTest {
    /**
     * _m_3_3 =
     * 0,1,0
     * 1,0,1
     * 0,1,0
     *
     * _m0 =
     * 1,1,1,1,1
     * 1,0,1,0,1
     * 1,0,0,0,1
     * 1,0,1,0,1
     * 1,1,1,1,1
     * 1,0,1,0,1
     *
     * 1, 1, 1, 1, 1
     * 1,-1, 1,-1, 1
     * 1,-1,-1,-1, 1
     * 1,-1, 1,-1, 1
     * 1, 1, 1, 1, 1
     * 1,-1, 1,-1, 1
     *
     * m2[3][2] = 0, m2[1][2] = 10, |sp|=11 (isCiclic = false;}
     * =============
     * 7, 8, 9, 1, 7
     * 6,-1,10,-1, 6
     * 5,-1,-1,-1, 5
     * 4,-1, 0,-1, 4
     * 3, 2, 1, 2, 3
     * 4,-1, 2,-1, 4
     *
     * m[3][2] = 0, m2[1][2] = 5, |sp|=5 (isCiclic = true;}
     * 5, 4, 3, 4, 5
     * 6,-1, 4,-1, 6
     * 5,-1,-1,-1, 5
     * 4,-1, 0,-1, 4
     * 3, 2, 1, 2, 3
     * 4,-1, 2,-1, 4
     */
    private int[][] _map = {{1,1,1,1,1}, {1,0,1,0,1}, {1,0,0,0,1},  {1,0,1,0,1},  {1,1,1,1,1}, {1,0,1,0,1}};
    private int[][] _map_3_3 = {{0,1,0}, {1,0,1}, {0,1,0}};
    private Map2D _m0, _m1, _m2, _m3, _m3_3;

    @BeforeEach
    public void setup() {
        _m0 = new Map(_map);
        _m1 = new Map(_map); _m1.setCyclic(true);
        _m2 = new Map(_map); _m2.setCyclic(false);
        _m3 = new Map(_map);
        _m3_3 = new Map(_map_3_3);
    }
    @Test
    @Timeout(value = 1, unit = SECONDS)
    void init() {
        int[][] bigger = new int [50][50];
        _m1.init(bigger);
        assertEquals(bigger.length, _m1.getWidth());
        assertEquals(bigger[0].length, _m1.getHeight());
        Pixel2D p1 = new Index2D(3,2);
        _m1.fill(p1,1);
    }
    //
    @Test
    void init2() {
        int[][] Arry1 = new int[10][10];
        _m1.init(Arry1);
        assertEquals(Arry1.length, _m1.getWidth());
        assertEquals(Arry1[0].length, _m1.getHeight());
    }

    @Test
    void testEquals() {
        assertEquals(_m0,_m1);
        assertEquals(_m0,_m3);
        assertNotEquals(_m1,_m2);
        _m3.setPixel(2,2,17);
        assertNotEquals(_m0,_m3);
    }
    @Test
    void getMap() {
        int[][] m0 = _m0.getMap();
        _m1.init(m0);
        assertEquals(_m0,_m1);
    }

    @Test
    void testFill0() {
        Pixel2D p1 = new Index2D(0,0);
        int f0 = _m0.fill(p1,2);
        assertEquals(f0,21);
    }
    @Test
    void testFill1() {
        Pixel2D p1 = new Index2D(0,1);
        _m0.setPixel(p1,0);
        int f0 = _m0.fill(p1,2);
        assertEquals(f0,9);
        _m0.setCyclic(false);
        int f2 = _m0.fill(p1,3);
        assertEquals(f2,8);
    }
    @Test
    void testAllDistance() {
        Pixel2D p1 = new Index2D(3,2);
        Pixel2D p2 = new Index2D(1,0);
        Map2D m00 = _m0.allDistance(p1, 0);
        assertEquals(6, m00.getPixel(p2));
    }

    @Test
    void testShortestPath() {
        Pixel2D p1 = new Index2D(3,2);
        Pixel2D p2 = new Index2D(1,2);
        Pixel2D[] path = _m0.shortestPath(p1, p2, 0);
        assertEquals(5, path.length);
        path = _m2.shortestPath(p1, p2, 0);
        assertEquals(11, path.length);
    }

    @Test
    void shortestPath() {
        Pixel2D p1 = new Index2D(0, 0);
        Pixel2D p2 = new Index2D(4, 3);
        Pixel2D[] path = _m0.shortestPath(p1, p2, 0);
        assertEquals(5, path.length);
        path = _m2.shortestPath(p1, p2, 0);
        assertEquals(8, path.length);
    }

    @Test
    void testShortestPath2() {
        Index2D p1 = new Index2D(0, 0);
        Index2D p2 = new Index2D(1, 2);
        Index2D p3 = new Index2D(3, 2);
        Index2D[] points = new Index2D[3];
        points[0] = p1;
        points[1] = p2;
        points[2] = p3;
        Pixel2D[] shortestPath = _m1.shortestPath(points, 0);
        assertNotNull(shortestPath);
        assertEquals(8, shortestPath.length);
    }

    @Test
    void testNumberOfConnectedComponents1() {
        assertEquals(1, _m0.numberOfConnectedComponents(0));

        assertEquals(3, _m1.numberOfConnectedComponents(1));
    }

    @Test
    public void testNumberOfConnectedComponents2() {
        int[][] data = {
                {1, 1, 0, 0, 2},
                {1, 1, 0, 2, 2},
                {0, 0, 0, 2, 2},
                {3, 3, 0, 0, 0},
                {3, 3, 0, 4, 4}
        };
        Map map = new Map(data);
        int obsColor = 0;
        assertEquals(4, map.numberOfConnectedComponents(obsColor));
    }

    @Test
    void testFindNeighbours() {
        Pixel2D[] neighbours = Map.findNeighbours(_m0, new Index2D(0, 0), true, 0);
        assertEquals(neighbours[0].getX(), 0);
        assertEquals(neighbours[0].getY(), 1);
        assertEquals(neighbours[1].getX(), 0);
        assertEquals(neighbours[1].getY(), 4);
        assertEquals(neighbours[3].getX(), 5);
        assertEquals(neighbours[3].getY(), 0);
        assertEquals(neighbours[2].getX(), 1);
        assertEquals(neighbours[2].getY(), 0);
    }


    @Test
    void testFindNeighbours1() {
        Map testMap = new Map(new int[300][300]);
        Pixel2D point2 = new Index2D(100, 100);
        long start = System.currentTimeMillis();
        Map.findNeighbours(testMap, point2, true, 0);
        long end = System.currentTimeMillis();
        long conclusion = (end - start);
        System.out.println("Time taken for validNeighbours " + conclusion);
    }

    @Test
    void testFill() {
        Map testMap = new Map(new int[50][50]);
        Pixel2D point2 = new Index2D(1, 1);
        long start = System.currentTimeMillis();
        testMap.fill(point2, 2);
        long end = System.currentTimeMillis();
        long conclusion = (end - start);
        System.out.println("Time taken for the fill function " + conclusion);
    }

    @Test
    void testMat1() {
        int[][] bigMap = new int[100][100];
        _m1.init(bigMap);
        Pixel2D p1 = new Index2D(0, 0);
        Pixel2D p2 = new Index2D(90, 40);
        assertEquals(bigMap[0].length, _m1.getHeight());
        assertEquals(bigMap.length, _m1.getWidth());
        _m1.shortestPath(p1, p2, 1);
    }

    @Test
    void testMat2() {
        int[][] bigMap = new int[100][100];
        _m1.init(bigMap);
        Pixel2D p1 = new Index2D(0, 0);
        Pixel2D p2 = new Index2D(14, 4);
        Pixel2D p3 = new Index2D(1, 1);
        Pixel2D[] points = new Pixel2D[3];
        points[0] = p1;
        points[1] = p2;
        points[2] = p3;
        assertEquals(bigMap.length, _m1.getWidth());
        assertEquals(bigMap[0].length, _m1.getHeight());
        _m1.shortestPath(points, 1);
    }

    @Test
    @Timeout(value = 1, unit = SECONDS)
    void testMat3() {
        int[][] bigMap = new int[100][100];
        _m1.init(bigMap);
        assertEquals(bigMap.length, _m1.getWidth());
        assertEquals(bigMap[0].length, _m1.getHeight());
        _m1.numberOfConnectedComponents(1);
    }
}

