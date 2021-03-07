
package snailstrike.gameobj;

import camera.MapInformation;
import java.awt.Color;
import java.awt.Graphics;
import snailstrike.utils.GameKernel.GameInterface;
import snailstrike.utils.Global;


public abstract  class GameObjectRect implements GameInterface {
       private final Rect collider;
    private final Rect painter;

    public GameObjectRect(int x, int y, int width, int height) {
        this(x, y, width, height, x, y, width, height);
    }

    public GameObjectRect(Rect rect) {
        collider = new Rect(rect);
        painter = new Rect(rect);
    }

    public GameObjectRect(int x, int y, int width, int height,
            int x2, int y2, int width2, int height2) {
        collider = Rect.genWithCenter(x, y, width, height);
        painter = Rect.genWithCenter(x2, y2, width2, height2);
    }

    public GameObjectRect(Rect rect, Rect rect2) {
        collider = new Rect(rect);
        painter = new Rect(rect2);
    }

    public boolean outOfScreen() {
        if (painter.bottom() <= 0) {
            return true;
        }
        if (painter.right() <= 0) {
            return true;
        }
        if (painter.left() >= MapInformation.mapInfo().right()) {
            return true;
        }
        return painter.top() >= MapInformation.mapInfo().bottom();
    }

    public boolean touchTop() {
        return collider.top() <= 0;
    }

    public boolean touchLeft() {
        return collider.left() <= 0;
    }

    public boolean touchRight() {
        return collider.right() >= MapInformation.mapInfo().right();
    }

    public boolean touchBottom() {
        return collider.bottom() >= MapInformation.mapInfo().bottom();
    }

    public boolean isCollision(GameObjectRect obj) {
        return collider.overlap(obj.collider);
    }
       public boolean isCollision(GameObject obj) {
        return collider.overlap(obj.collider().left(),
                obj.collider().top(),
                obj.collider().right()
        ,obj.collider().bottom());
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

    public final Rect collider() {
        return collider;
    }

    public final Rect painter() {
        return painter;
    }

    @Override
    public final void paint(Graphics g) {
        paintComponent(g);
        if (Global.IS_DEBUG) {
            g.setColor(Color.RED);
            g.drawRect(this.painter.left(), this.painter.top(), this.painter.width(), this.painter.height());
            g.setColor(Color.BLUE);
            g.drawRect(this.collider.left(), this.collider.top(), this.collider.width(), this.collider.height());
            g.setColor(Color.BLACK);
        }
    }

    public abstract void paintComponent(Graphics g);

    public int getRGBInt() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
    

