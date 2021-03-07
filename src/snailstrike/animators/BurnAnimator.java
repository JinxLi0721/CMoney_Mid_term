/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package snailstrike.animators;

import java.awt.Graphics;
import java.awt.Image;
import snailstrike.controllers.SceneController;
import snailstrike.utils.Delay;
import snailstrike.utils.Global;
import snailstrike.utils.Path;

/**
 *
 * @author Kylie
 */
public class BurnAnimator {

    public enum State {
        WALK(new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9}, 1);

        private int[] arr;
        private int speed;

        State(int[] arr, int speed) {
            this.arr = arr;
            this.speed = speed;
        }
        
    }

    private final Image img;
    private final Delay delay;
    private int count;
    private State state;
    private final int type;
    private final int BURN_X = 165;
    private final int BURN_Y = 165;

    public BurnAnimator(State state) {
        img = SceneController.instance().irc().tryGetImage(
                new Path().img().actors().burn());
        delay = new Delay(state.speed);
        delay.loop();
        count = 0;
        this.type = 0;
        setState(state);
    }

    public final void setState(State state) {
//        count = count % state.arr.length;
        this.state = state;
        this.delay.setLimit(state.speed);
    }

    public void paint(Global.Direction dir, int left, int top, int right, int bottom, Graphics g) {

        if (count >= state.arr.length) {
            count = 0;
        }
        g.drawImage(img,
                left, top,
                right, bottom,
                BURN_X * state.arr[count],
                BURN_Y * dir.getValue(),
                BURN_X + BURN_X * state.arr[count],
                BURN_Y + BURN_Y * dir.getValue(), null);
    }

    public void update() {
        if (delay.count()) {
            count = ++count % state.arr.length;
        }
    }

}
