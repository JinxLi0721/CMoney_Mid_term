package snailstrike.gameobj;

import java.awt.Graphics;
import java.awt.Image;
import snailstrike.controllers.SceneController;
import snailstrike.utils.Delay;
import snailstrike.utils.Path;

/**
 *
 * @author USER
 */
public class Lava {

    protected final int FIRE_WIDTH = 512;
    protected final int FIRE_HEIGHT = 512;

    protected Image image;
    protected int[] positionX;
    protected int[] positionY;
    protected int countX;
    protected Delay delay;

    public Lava() {

        image = SceneController.instance().irc().tryGetImage(new Path().img().map().lavaAll());
        delay = new Delay(5);
        delay.loop();
        positionX = new int[9];
        for (int i = 0; i < positionX.length; i++) {
            positionX[i] = i;
        }
        countX = 0;
    }

    public void paint(int left, int top, int right, int bottom, Graphics g) {
        for (int i = 0; i < 6; i++) {
            for (int w = 0; w < 6; w++) {
                int lLeft = FIRE_WIDTH * i;
                int lTop = FIRE_HEIGHT * w;
                int lRight = FIRE_WIDTH * (i + 1);
                int lBottom = FIRE_HEIGHT * (w + 1);
                if (!overlap(left, top, right, bottom, lLeft, lTop, lRight, lBottom)) {
                    continue;
                }
                lLeft = Math.max(lLeft, left);
                lTop = Math.max(lTop, top);
                lRight = Math.min(lRight, right);
                lBottom = Math.min(lBottom, bottom);
                g.drawImage(image, lLeft, lTop,
                        lRight, lBottom,
                        positionX[countX] * FIRE_WIDTH + (lLeft % 512),
                        lTop % 512,
                        positionX[countX] * FIRE_WIDTH + (lRight % 513),
                        lBottom % 513, null);
            }
        }
    }

    private final boolean overlap(int left, int top, int right, int bottom,
            int lLeft, int lTop, int lRight, int lBottom) {
        if (lLeft > right) {
            return false;
        }
        if (lRight < left) {
            return false;
        }
        if (lTop > bottom) {
            return false;
        }
        if (lBottom < top) {
            return false;
        }
        return true;
    }

    public void update() {
        if (delay.count()) {
            countX = ++countX % positionX.length;
//            System.out.println("countX:" + countX);
        }
    }
}
