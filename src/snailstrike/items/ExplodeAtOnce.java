package snailstrike.items;

import client.ClientClass;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import snailstrike.animators.ItemsAnimator;
import snailstrike.controllers.AudioResourceController;
import snailstrike.controllers.SceneController;
import snailstrike.gameobj.Rect;
import snailstrike.gameobj.Snail;
import snailstrike.gameobj.Vector;
import snailstrike.utils.CommandSolver;
import snailstrike.utils.Delay;
import snailstrike.utils.Global;
import snailstrike.utils.Path;

public class ExplodeAtOnce extends Items {

    private Delay delayBoom;

    private Vector boomV;//爆炸時的位置

    public ExplodeAtOnce(int serialNum, int x, int y) {
        super(serialNum, x, y, 30, 20,
                SceneController.instance().irc().tryGetImage(new Path().img().props().ExplodeAtOnce()),
                SceneController.instance().irc().tryGetImage(new Path().img().props().ExplodeAtOnceExplose()),
                ItemsAnimator.Kind.EXPLODE_AT_ONCE, ItemsAnimator.Boom.EXPLODE_AT_ONCE_BOOM,
                50, 38, 95, 95, 10);

//           (int x, int y, int r, int explosionFieldR, 
//             Image itemNormal, Image itemBoom,ItemsAnimator.Kind kind, ItemsAnimator.Boom boom, 
//               int BOMB_NORMAL_X, int BOMB_NORMAL_Y,
//            int BOMB_BOOM_X, int BOMB_BOOM_Y,double boomPower)
        boomV = new Vector();
        delayBoom = new Delay(1);
        this.setSituation(situation.EXIST);
    }

    @Override
    public Vector getBoomV() {
        return boomV;
    }

    @Override
    public void afterSnailEated() {

    }

    @Override
    public void boomMoving(Snail snail) {

    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

    }

    @Override
    public void update() {
        super.update();

        if (situation == situation.THROWED || situation == situation.STOP) {
            delayBoom.play();
            if (delayBoom.count()) {
                AudioResourceController.getInstance().play(new Path().sounds().items().ItemsLongBoom());
                super.setState(ItemsAnimator.State.BOOM);
                ArrayList<String> changeState = new ArrayList<>();
                changeState.add(Integer.toString(this.getSerialNum()));
                changeState.add("BOOM");
                ClientClass.getInstance().sent(Global.ClientMessage.CHANGE_ITEM_STATE, changeState);
                situation = situation.STOP;
                ArrayList<String> changeSituation = new ArrayList<>();
                changeSituation.add(Integer.toString(this.getSerialNum()));
                changeSituation.add("STOP");
                ClientClass.getInstance().sent(Global.ClientMessage.CHANGE_ITEM_SITUATION, changeSituation);
            }
            if (situation == situation.STOP) {
                boomV.setVectorXY(painter().centerX(), painter().centerY());

            }
        }
    }

    @Override
    public void mouseTrig(Rect rect, MouseEvent e, CommandSolver.MouseState state, long trigTime) {

    }

    @Override
    public void setMouseEvent(ArrayList<String> strs) {

    }

}
