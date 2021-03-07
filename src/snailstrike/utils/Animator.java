/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package snailstrike.utils;

import snailstrike.controllers.SceneController;
import java.awt.Graphics;
import java.awt.Image;

/**
 *
 * @author user1
 */
public class Animator {
    public enum State {
        STAND(new int[]{1}, 60),
        WALK(new int[]{0, 1, 2, 3, 2, 1}, 10);

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

    public Animator(int type, State state) {
        img = SceneController.instance().irc().tryGetImage(
                new Path().img().actors().snail());
        delay = new Delay(0);
        delay.loop();
        count = 0;
        this.type = type;
        setState(state);
    }

    public final void setState(State state) {
//        count = count % state.arr.length;
        this.state = state;
        this.delay.setLimit(state.speed);
    }

    public void paint(Global.Direction dir, int left, int top, int right, int bottom, Graphics g) {
        
        if(count>=state.arr.length){
            count=0;
        }
        g.drawImage(img,
                left, top,
                right, bottom,
                (type % 4) * Global.UNIT_X * 3 + Global.UNIT_X * state.arr[count],
                (type / 4) * Global.UNIT_Y * 4 + Global.UNIT_Y * dir.getValue(),
                (type % 4) * Global.UNIT_X * 3 + Global.UNIT_X + Global.UNIT_X * state.arr[count],
                (type / 4) * Global.UNIT_Y * 4 + Global.UNIT_Y + Global.UNIT_Y * dir.getValue(), null);
    }

    public void update() {
        if (delay.count()) {
            count = ++count % state.arr.length;
        }
    }
    
    public boolean isStand(){
        return this.state == State.STAND;
    }

}
