
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
public class Water {
        protected final int FIRE_WIDTH = 128;
    protected final int FIRE_HEIGHT = 128;

    protected Image image;
    protected int[] positionX;
    protected int[] positionY;
    protected int countX;
    protected Delay delay;

    public Water() {

        image = SceneController.instance().irc().tryGetImage(new Path().img().map().waterbackground());
        delay = new Delay(5);
        delay.loop();
        positionX = new int[5];
        for (int i = 0; i < positionX.length; i++) {
            positionX[i] = i;
        }
        countX = 0;
    }

    public void paint(int left, int top, int right, int bottom, Graphics g) {
       for(int i = 0 ;i<12;i++){
            for(int w = 0 ; w<12;w++){
                g.drawImage(image, 256*i, 256*w,
                right*i+256, bottom*w+256,
                positionX[countX] * FIRE_WIDTH,
                0,
                positionX[countX] * FIRE_WIDTH + FIRE_WIDTH,
                FIRE_HEIGHT, null);
            }
       }

    }

    public void update() {
        if (delay.count()) {
            countX = ++countX % positionX.length;
//            System.out.println("countX:" + countX);
        }
    }
}
