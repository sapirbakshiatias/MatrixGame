package assignments.ex3;

import java.io.Serializable;
import java.util.*;

/**
 * This class represents a 2D map as a "screen" or a raster matrix or maze over integers.
 *
 * @author boaz.benmoshe
 */
public class Map implements Map2D, Serializable {
    private int[][] _map;
    private boolean _cyclicFlag = true;

    /**
     * Constructs a w*h 2D raster map with an init value v.
     *
     * @param w
     * @param h
     * @param v
     */
    public Map(int w, int h, int v) {
        init(w, h, v);
    }

    /**
     * Constructs a square map (size*size).
     *
     * @param size
     */
    public Map(int size) {
        this(size, size, 0);
    }

    /**
     * Constructs a map from a given 2D array.
     *
     * @param data
     */
    public Map(int[][] data) {
        init(data);
    }

    @Override
    public void init(int w, int h, int v) {
        // add you code here
        _map = new int[w][h];
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                _map[i][j] = v;
            }
        }
    }

    @Override
    public void init(int[][] arr) {
        // add you code here
        if (arr == null) {
            throw new RuntimeException("Array is null");
        }
        if (arr.length == 0) {
            throw new RuntimeException("Array is empty");
        }
        int length = arr[0].length;
        for (int i = 1; i < arr.length; i++) {
            if (arr[i].length != length) {
                throw new RuntimeException("Array is jagged");
            }
        }
        _map = new int[arr.length][ arr[0].length];
        for (int i = 0; i <arr.length; i++) {
            for (int j = 0; j < arr[0].length; j++) {
                _map[i][j] = arr[i][j];
            }
        }
    }

    @Override
    public int[][] getMap() {
        // add you code here
        int[][] copyMap = new int[_map.length][_map[0].length];
        for (int i = 0; i < _map.length; i++) {
            copyMap[i] = _map[i].clone();
        }
        return copyMap;
        // return _map; //for the test
    }

    @Override
    public int getWidth() { //number of columns
        return _map.length;
    }

    @Override
    public int getHeight() { //number of rows
        return _map[0].length;
    }

    @Override
    public int getPixel(int x, int y) {
        return _map[x][y];
    }

    @Override
    public int getPixel(Pixel2D p) {
        return this.getPixel(p.getX(), p.getY());
    }

    @Override
    public void setPixel(int x, int y, int v) {
        _map[x][y] = v;
    }

    @Override
    public void setPixel(Pixel2D p, int v) {
        setPixel(p.getX(), p.getY(), v);
    }

    @Override
    public int getX() {
        return 0;
    }

    @Override
    public int getY() {
        return 0;
    }

    @Override
    public int fill(Pixel2D startPoint, int newColor) {
        if (newColor == getPixel(startPoint)) return 0; //צבע חדש = צבע בנקודת התחלה: לא לצבוע

        ArrayList<Pixel2D> coloredPixels = new ArrayList<>(); //list for colored
        LinkedList<Pixel2D> points = new LinkedList<>(); // list for check what coloring
        points.add(startPoint);

        while (!points.isEmpty()) {
            Pixel2D currentPixel = points.poll(); //delete and return the first
            if (currentPixel != null && !coloredPixels.contains(currentPixel)) { // not empty + not colored
                Pixel2D[] neighbors = findNeighbours(this, currentPixel, true, null);//find neighbours
                for (Pixel2D neighbor : neighbors) {
                    if (neighbor != null && !coloredPixels.contains(neighbor)) {
                        points.add(neighbor);
                    }
                }
                setPixel(currentPixel, newColor);
                coloredPixels.add(currentPixel);
            }
        }
        return coloredPixels.size();
    }
    @Override
    public int fill(int x, int y, int new_v) {
        Pixel2D p = new Index2D (x,y);
        return fill( p,new_v);
    }
    @Override
    public Map2D allDistance(Pixel2D start, int obsColor) {
        // add you code here
        int[][] map = new int[this.getWidth()][this.getHeight()]; //copy for numbers
        if (this.getPixel(start) != obsColor) { //check if start = obs
            map = allDistance(this, (Index2D) start, obsColor);
        }
        return new Map(map);
    }

    public int[][] allDistance(Map2D map, Index2D start, int obsColor) {
        int[][] mapCopy = new int[this.getWidth()][this.getHeight()]; //map with -1 or obs
//        int ourObsColor = -2;
        for (int i = 0; i < getWidth(); i++) {
            for (int j = 0; j < getHeight(); j++) {
                if (getPixel(i, j) != obsColor)  //all good = -1
                    mapCopy[i][j] = -1;
                else
                    mapCopy[i][j] = -2;
            }
        }
        mapCopy[start.getX()][start.getY()] = 0; //start dis = 0
        LinkedList<Pixel2D> list = new LinkedList<>(); //list of option to check
        list.push(start);
        while (!list.isEmpty()) {
            Pixel2D current = list.pop();
            int count = mapCopy[current.getX()][current.getY()] + 1; //dis = x+1
            Pixel2D[] neighbours = findNeighbours(this, current, false, obsColor); //findNeighbours
            for (Pixel2D neighbour : neighbours) {
                if (neighbour == null)
                    continue;
                int x = neighbour.getX();
                int y = neighbour.getY();
                if (mapCopy[x][y] == -1 || count < mapCopy[x][y]) { //not visited / smaller than x+1
                    mapCopy[x][y] = count;
                    list.push(neighbour);
                }
            }
        }
        for (int i = 0; i < getWidth(); i++) { //return v.obs to original
            for (int j = 0; j < getHeight(); j++) {
                if (mapCopy[i][j] == -2) {
                    mapCopy[i][j] = obsColor;
                }
            }
        }
        return mapCopy;
    }
    /**
     * Computes the distance of the shortest path (minimal number of consecutive neighbors) from p1 to p2.
     * Notes: the distance is using computing the shortest path and returns its length-1, as the distance fro  a point
     * to itself is 0, while the path contains a single point.
     */
    public int shortestPathDist(Pixel2D p1, Pixel2D p2, int obsColor) {
        // add you code here
        Pixel2D[] ans = shortestPath(p1 ,p2, obsColor);
        return ans.length-1;
    }
    @Override
    /**
     * BFS like shortest the computation based on iterative raster implementation of BFS, see:
     * https://en.wikipedia.org/wiki/Breadth-first_search
     */
    public Pixel2D[] shortestPath(Pixel2D p1, Pixel2D p2, int obsColor) {
        // add you code here
        Pixel2D[] ans = null;  // the result
        if (p1.getX() == p2.getX() && p1.getY() == p2.getY()) { //if p1=p2.
            ans = new Pixel2D[1];
            ans[0] = p1;
            return ans;
        }
        if (getPixel(p1) == obsColor || getPixel(p2) == obsColor) //if p is obsColor
            return null;

        int[][] mapCopy = new int[this.getWidth()][this.getHeight()]; //map with -1 or obs
        int ourObsColor = -2;
        for (int i = 0; i < getWidth(); i++) {
            for (int j = 0; j < getHeight(); j++) {
                if (getPixel(i, j) != obsColor)  //all good = -1
                    mapCopy[i][j] = -1;
                else
                    mapCopy[i][j] = -2;
            }
        }
        mapCopy[p1.getX()][p1.getY()] = 0;
        LinkedList<Pixel2D> list = new LinkedList<>(); //list of option
        list.push(p1);
        while (!list.isEmpty()) {
            Pixel2D current = list.pop();
            int count = mapCopy[current.getX()][current.getY()] + 1;
            Pixel2D[] neighbours = findNeighbours(this, current, false, obsColor);
            for (Pixel2D neighbour : neighbours) {
                if (neighbour == null)
                    continue;
                int x = neighbour.getX();
                int y = neighbour.getY();
                if (mapCopy[x][y] == -1 || count < mapCopy[x][y]) {
                    mapCopy[x][y] = count;
                    list.push(neighbour);
                }
            }
        }
        int path_length = mapCopy[p2.getX()][p2.getY()]; //p2.v, the dis
        Pixel2D[] path = new Pixel2D[path_length+1]; //including 0 (start, p1)
        Pixel2D nextPixel = p2;
        for (int i = path_length; i >= 0; i--) { //from p2.v to 0 (p1)
            path[i] = nextPixel;
            nextPixel = minNeighbour(mapCopy, nextPixel, ourObsColor, this.isCyclic());
        }
        return path;
    }

    @Override
    public Pixel2D[] shortestPath(Pixel2D[] points, int obsColor) {
        ArrayList<Pixel2D> path = new ArrayList<>();
        path.add(points[0]);
        boolean[] visited = new boolean[points.length];

        int currentPointIndex = 0;
        boolean allPointsVisited;

        do {
            visited[currentPointIndex] = true;
            int nextPointIndex = currentPointIndex;

            int minDistance = -2;
            for (int j = 0; j < points.length; j++) {
                if (!visited[j]) {
                    int distance = shortestPathDist(points[currentPointIndex], points[j], obsColor);

                    if (distance == -2) return null;

                    if (minDistance == -2 || distance < minDistance) {
                        minDistance = distance;
                        nextPointIndex = j;
                    }
                }
            }

            Pixel2D[] currentPath = shortestPath(points[currentPointIndex], points[nextPointIndex], obsColor);
            for (int k = 1; k < currentPath.length; k++) {
                path.add(currentPath[k]);
            }

            currentPointIndex = nextPointIndex;
            allPointsVisited = true;

            for (boolean v : visited) {
                if (!v) {
                    allPointsVisited = false;
                    break;
                }
            }
        } while (!allPointsVisited);

        return path.toArray(new Pixel2D[0]);
    }

    @Override
    public boolean isInside(Pixel2D p) {
        return isInside(p.getX(), p.getY());
    }

    @Override
    public boolean isCyclic() {
        return _cyclicFlag;
    }

    @Override
    public void setCyclic(boolean isCyclic) {
        _cyclicFlag = isCyclic;
    }

    private boolean isInside(int x, int y) {
        return x >= 0 && y >= 0 && x < this.getWidth() && y < this.getHeight();
    }
//
//    @Override
//    public int numberOfConnectedComponents(int obsColor) {
//        int ans = 0;
//        // add you code here
//        int[][] map = new int[this.getWidth()][this.getHeight()];
//        for (int i = 0; i < map.length; i++) {
//            for (int j = 0; j < map[0].length; j++) {
//                if (getPixel(i, j) != obsColor) {
//                    ans++;
//                    fill(i, j, obsColor);
//                }
//            }
//        }
//        return ans;
//    }
@Override
public int numberOfConnectedComponents(int obsColor) {
    int componentCount = 0;
    int[][] map = new int[this.getWidth()][this.getHeight()];

    for (int i = 0; i < this.getWidth(); i++) {
        for (int j = 0; j < this.getHeight(); j++) {
            if (map[i][j] == 0 && getPixel(i, j) != obsColor) { // found an unvisited component
                componentCount++;
                Pixel2D startPoint = new Index2D(i, j);
                fill(startPoint, obsColor); // fill the component with obsColor
            }
        }
    }

    return componentCount;
}


    @Override
    public boolean equals (Object ob){
        // add you code here
        if (ob == null || ob.getClass() != getClass()) return false;
        Map map = (Map) ob;
        return map.isCyclic() == isCyclic() && Arrays.deepEquals(map._map, _map);
    }

    ////////////////////// Private Methods ///////////////////////
    // add you code here

    public static Pixel2D[] findNeighbours(Map2D map, Pixel2D p, boolean colorMatch, Integer obsColor) {
        if (p == null) {
            throw new IllegalArgumentException("Pixel p cannot be null");
        }
        Pixel2D[] allNei = new Pixel2D[4];
        int x = p.getX();
        int y = p.getY();
        int up = 0, down = 1;
        int right = 2, left = 3;

        boolean topY1 = (y == map.getHeight() - 1); //top row
        if (!topY1 || map.isCyclic()) {
            if (topY1)
                allNei[up] = new Index2D(x, 0); //up c point
            else
                allNei[up] = new Index2D(x, y + 1); // up regular point
        }
        boolean topY2 = (y == 0);  //bottom row
        if (!topY2 || map.isCyclic()) {
            if (topY2)
                allNei[down] = new Index2D(x, map.getHeight() - 1); //down c point
            else
                allNei[down] = new Index2D(x, y - 1); //down regular point
        }
        boolean topX1 = (x == map.getWidth() - 1); //right column
        if (!topX1 || map.isCyclic()) {
            if (topX1)
                allNei[right] = new Index2D(0, y); //right c point
            else
                allNei[right] = new Index2D(x + 1, y);//right regular point
        }
        boolean topX2 = (x == 0); //left column
        if (!topX2 || map.isCyclic()) {
            if (topX2)
                allNei[left] = new Index2D(map.getWidth() - 1, y); //left c point
            else
                allNei[left] = new Index2D(x - 1, y);//left regular point
        }
        if (colorMatch || obsColor != null) { // need color match
            for (int i = 0; i < allNei.length; i++) {
                if (allNei[i] != null) {
                    if (colorMatch && map.getPixel(allNei[i]) != map.getPixel(p)) { //v isn't match
                        allNei[i] = null;
                    }
                    if (obsColor != null && map.getPixel(allNei[i]) == obsColor) { //i is obs
                        allNei[i] = null;
                    }
                }
            }
        }
        return allNei;
    }

    public static Index2D minNeighbour(int[][] map , Pixel2D point, Integer obsColor, boolean isCyclic) {
        int x = point.getX();
        int y = point.getY();
        int height = map[0].length;
        int width = map.length;
        Index2D minPixel = new Index2D(); //pixel
        int min = Integer.MAX_VALUE; //value

        boolean isEdgeYP = (y == height - 1); //top row

        if (!isEdgeYP || isCyclic) { //(edge + c) / (!edge + !c)
            if (isEdgeYP) {
                if(map[x][0] < min && map[x][0] != obsColor) { // up c point
                    min = map[x][0];
                    minPixel = new Index2D(x,0);
                }
            }
            else {
                if (y!=0 && map[x][y-1] < min && map[x][y-1] != obsColor) { //down (regular) point
                    min = map[x][y - 1];
                    minPixel = new Index2D(x,y-1);
                }
            }
        }
        boolean isEdgeYN = y == 0; //bottom row
        if (!(isEdgeYN && !isCyclic)) {
            if (isEdgeYN){
                if(map[x][height-1] < min && map[x][height-1] != obsColor) { //down c point
                    min = map[x][height - 1];
                    minPixel = new Index2D(x,height-1);
                }
            }
            else {
                if (y!=height - 1 && map[x][y+1] < min && map[x][y+1] != obsColor) { //up (regular) point
                    min = map[x][y + 1];
                    minPixel = new Index2D(x,y+1);
                }
            }
        }
        boolean isEdgeXN = x == 0; //left column
        if (!(isEdgeXN && !isCyclic)) {
            if (isEdgeXN){
                if(map[width-1][y] < min && map[width-1][y] != obsColor) { //left c point
                    min = map[width - 1][y];
                    minPixel = new Index2D(width-1,y);
                }
            }
            else {
                if (map[x-1][y] < min && map[x-1][y] != obsColor) { //right (regular) point
                    min = map[x - 1][y];
                    minPixel = new Index2D(x-1,y);
                }
            }
        }
        boolean isEdgeXP = x == width - 1; //right column
        if (!(isEdgeXP && !isCyclic)) {
            if (isEdgeXP){
                if(map[0][y] < min && map[0][y] != obsColor) { //right c point
                    min = map[0][y];
                    minPixel = new Index2D(0,y);
                }
            }
            else {
                if (map[x+1][y] < min && map[x+1][y] != obsColor) { //left (regular) point
                    min = map[x + 1][y];
                    minPixel = new Index2D(x+1,y);
                }
            }
        }
        return minPixel;
    }
    }



