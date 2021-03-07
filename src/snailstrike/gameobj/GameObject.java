/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package snailstrike.gameobj;

import snailstrike.utils.GameKernel.GameInterface;
import snailstrike.utils.Global;
import java.awt.Color;
import java.awt.Graphics;

/**
 *
 * @author user1
 */
public abstract class GameObject implements GameInterface {

    private final Circle collider;
    private final Circle painter;

    public GameObject(int x, int y, int r) {
        this(x, y, r, x, y, r);
    }

    public GameObject(Circle circle) {
        this(circle, circle);
    }

    public GameObject(int x, int y, int r,
            int x2, int y2, int r2) {
        collider = new Circle(x, y, r);
        painter = new Circle(x2, y2, r2);
    }

    public GameObject(Circle circle, Circle circle2) {
        collider = new Circle(circle);
        painter = new Circle(circle2);
    }

    public boolean outOfScreen() {
        if (painter.bottom() <= 0) {
            return true;
        }
        if (painter.right() <= 0) {
            return true;
        }
        if (painter.left() >= Global.SCREEN_X) {
            return true;
        }
        return painter.top() >= Global.SCREEN_Y;
    }

    public boolean touchTop() {
        return collider.top() <= 0;
    }

    public boolean touchLeft() {
        return collider.left() <= 0;
    }

    public boolean touchRight() {
        return collider.right() >= Global.SCREEN_X;
    }

    public boolean touchBottom() {
        return collider.bottom() >= Global.SCREEN_Y;
    }

    public boolean isCollision(GameObject obj) {
        return collider.overlap(obj.collider);
    }

    public final void translate(int x, int y) {
        collider.translate(x, y);
        painter.translate(x, y);
    }

    public final void translateX(int x) {
        collider.translateX(x);
        painter.translateX(x);
    }

    public final void translateY(int y) {
        collider.translateY(y);
        painter.translateY(y);
    }

    public final Circle collider() {
        return collider;
    }

    public final Circle painter() {
        return painter;
    }

    @Override
    public final void paint(Graphics g) {
        paintComponent(g);
        if (Global.IS_DEBUG) {
            g.setColor(Color.RED);
            g.drawOval(this.painter.left(), this.painter.top(), this.painter.width(), this.painter.height());
            g.setColor(Color.BLUE);
            g.drawOval(this.collider.left(), this.collider.top(), this.collider.width(), this.collider.height());
            g.setColor(Color.ORANGE);
            g.drawString(this.collider.centerX() + "," + this.collider.centerY(), this.painter.centerX() + 5,  this.painter.top - 10);
            g.setColor(Color.BLACK);
        }
    }

    public abstract void paintComponent(Graphics g);
}
