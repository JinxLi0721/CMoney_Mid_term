/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package snailstrike.gameobj;

/**
 *
 * @author k1207
 */
public class Circle extends Shape {

    public Circle(int left, int top, int right, int bottom) {
        super(left, top, right, bottom);
    }

    public Circle(int ox, int oy, int r) {
        super(ox - r, oy - r, ox + r, oy + r);
    }

    public Circle(Circle circle) {
        super(circle.left, circle.top, circle.right, circle.bottom);
    }

    public final double exactRadius() {
        return (right - left) / 2f;
    }

    public final int radius() {
        return (right - left) / 2;
    }

    public boolean overlap(Circle c) {
        double r = exactRadius() + c.exactRadius();
        double d = Math.sqrt(
                Math.pow(exactCenterX() - c.exactCenterX(), 2)
                + Math.pow(exactCenterY() - c.exactCenterY(), 2)
        );
        return r >= d;
    }

}
