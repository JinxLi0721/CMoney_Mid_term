package snailstrike.animators;

import java.awt.Graphics;
import java.awt.Image;
import snailstrike.utils.Delay;
import snailstrike.utils.Global;

public class SkillAnimator {

    public enum State {
        STAND(new int[]{0}, 30),
        WALK(new int[]{0, 1, 2, 3, 4, 5, 6, 7}, 5);

        private int[] arr;
        private int speed;

        State(int[] arr, int speed) {
            this.arr = arr;
            this.speed = speed;
        }
    }

    private final int ANIMATOR_UNIX_X;
    private final int ANIMATOR_UNIX_Y;
    private final Image img;
    private final Delay delay;
    private int count;
    private State state;
    private final int type;

    public SkillAnimator(Image img, int ANIMATOR_UNIX_X, int ANIMATOR_UNIX_Y, State state) {
        this.img = img;
        delay = new Delay(0);
        delay.loop();
        count = 0;
        this.type = 0;
        setState(state);
        this.ANIMATOR_UNIX_X = ANIMATOR_UNIX_X;
        this.ANIMATOR_UNIX_Y = ANIMATOR_UNIX_Y;
    }

    public final void setState(State state) {
        count = count % state.arr.length;
        this.state = state;
        this.delay.setLimit(state.speed);
    }

    public void paintComponent(Global.Direction dir, int left, int top, int right, int bottom, Graphics g) {

        g.drawImage(img,
                left, top,
                right, bottom,
                ANIMATOR_UNIX_X * state.arr[count],
                ANIMATOR_UNIX_Y * dir.getValue(),
                ANIMATOR_UNIX_X + ANIMATOR_UNIX_X * state.arr[count],
                ANIMATOR_UNIX_Y + ANIMATOR_UNIX_Y * dir.getValue(), null);
    }

    public void update() {
        if (delay.count()) {
            count = ++count % state.arr.length;
        }
    }

    public boolean isStand() {
        return this.state == State.STAND;
    }

}
