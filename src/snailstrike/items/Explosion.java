/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package snailstrike.items;

import java.awt.Color;
import java.awt.Graphics;
import snailstrike.gameobj.Circle;
import snailstrike.gameobj.GameObject;
import snailstrike.utils.Global;

/**
 *
 * @author USER
 */
public abstract class Explosion extends GameObject {

    private final Circle explosionField;
    public final int explosionFieldR;

    public Explosion(int x, int y, int r,int explosionFieldR) {
        super(x, y, r,x,y,r+10);
        explosionField = new Circle(collider().centerX(), collider().centerY(), explosionFieldR);
        this.explosionFieldR = explosionFieldR;
    }

    public boolean isOutterCollision(Explosion explosion) {
        return explosionField.overlap(explosion.explosionField);
    }
    
    public boolean isCollederOutterCollision(GameObject obj){
         return explosionField.overlap(obj.collider());
    }
    

    @Override
    public boolean touchTop() {
        return explosionField.top() <= 0;
    }

    @Override
    public boolean touchLeft() {
        return explosionField.left() <= 0;
    }

    @Override
    public boolean touchRight() {
        return explosionField.right() >= Global.SCREEN_X;
    }

    @Override
    public boolean touchBottom() {
        return explosionField.bottom() >= Global.SCREEN_Y;
    }

    public final void translateAll(int x, int y) {
        explosionField.translate(x, y);
        painter().translate(x, y);
        collider().translate(x, y);

    }

    public final void translateAllX(int x) {
        collider().translateX(x);
        painter().translateX(x);
        explosionField.translateX(x);
    }

    public final void translateAllY(int y) {
        collider().translateY(y);
        painter().translateY(y);
        explosionField.translateY(y);
    }

    public Circle explosionField() {
        return explosionField;
    }

    public final void paintAll(Graphics g) {
        paintComponent(g);
        paint(g);
        if (Global.IS_DEBUG) {
            g.setColor(Color.GREEN);
            g.drawOval(collider().left()-explosionFieldR/2 , collider().top()-explosionFieldR/2 , 
                    collider().width() + explosionFieldR, collider().height() + explosionFieldR);
            g.setColor(Color.BLACK);
            
        }
    }

    public abstract void paintComponent(Graphics g);
}


