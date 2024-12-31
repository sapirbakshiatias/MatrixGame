package assignments.ex3;

import java.io.Serializable;

public class Index2D implements Pixel2D, Serializable{
    private int _x, _y;
    public Index2D() {this(0,0);}
    public Index2D(int x, int y) {_x=x;_y=y;}
    public Index2D(Pixel2D t) {this(t.getX(), t.getY());}

    public Index2D(String pos)  {
        // add you code here
    try {
        String[] s = pos.split(",");
        _y = Integer.parseInt(s[0].trim());
        _x = Integer.parseInt(s[1].trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid coordinates in Index2D string: " + pos, e);
           }
    }

    @Override
    public int getX() {
        return _x;
    }

    @Override
    public int getY() {
        return _y;
    }
    public double distance2D(Pixel2D t) {
        // add you code here
        double x = this.getX() - t.getX();
        double y = this.getY() - t.getY();
        double d = (x*x+y*y);
        return Math.sqrt(d);
    }

    @Override
    public String toString() {
        String ans = _x+","+_y;
        return ans;
    }
    @Override
    public boolean equals(Object t) {
        // add you code here
        if(t == null || !(t instanceof Index2D)) {return false;}
        Index2D p2 = (Index2D)t;
        return ( (_x == p2._x) && (_y == p2._y));
    }
}
