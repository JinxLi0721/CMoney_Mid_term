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
public abstract class Shape {

    protected int left;
    protected int top;
    protected int right;
    protected int bottom;

    protected Shape(int left, int top, int right, int bottom) {
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
    }

    public final int centerX() {
        return (left + right) / 2;
    }

    public final int centerY() {
        return (top + bottom) / 2;
    }

    public final double exactCenterX() {
        return (left + right) / 2f;
    }

    public final double exactCenterY() {
        return (top + bottom) / 2f;
    }

    public final Shape translate(int dx, int dy) {
        this.left += dx;
        this.right += dx;
        this.top += dy;
        this.bottom += dy;
        return this;
    }

    public final Shape translateX(int dx) {
        this.left += dx;
        this.right += dx;
        return this;
    }

    public final Shape translateY(int dy) {
        this.top += dy;
        this.bottom += dy;
        return this;
    }

    public final int left() {
        return left;
    }

    public final void setLeft(int left) {
        this.left = left;
    }

    public final int top() {
        return top;
    }

    public final void setTop(int top) {
        this.top = top;
    }

    public final int right() {
        return right;
    }

    public final void setRight(int right) {
        this.right = right;
    }

    public final int bottom() {
        return bottom;
    }

    public final void setBottom(int bottom) {
        this.bottom = bottom;
    }

    public final int width() {
        return this.right - this.left;
    }

    public final int height() {
        return this.bottom - this.top;
    }

    public final void setCenter(int x, int y) {
        translate(x - centerX(), y - centerY());
    }
}
