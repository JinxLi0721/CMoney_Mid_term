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
import snailstrike.utils.Global.ClientMessage;
import static snailstrike.utils.Global.ClientMessage.*;
import snailstrike.utils.Path;

public class Trap extends Items {

    private Delay delayBoom;
    private double moveWithSnailX;//被蝸牛吃到時跟著蝸牛走的移動
    private double moveWithSnailY;//被蝸牛吃到時跟著蝸牛走的移動
    private Vector boomV;//爆炸時的位置

    public Trap(int serialNum, int x, int y) {
        super(serialNum, x, y, 40, 100,
                SceneController.instance().irc().tryGetImage(new Path().img().props().trapLeaves()),
                SceneController.instance().irc().tryGetImage(new Path().img().props().trapExplose()),
                ItemsAnimator.Kind.TRAP, ItemsAnimator.Boom.TRACK_BOOM,
                130, 100, 96, 108, 10);
//             Items(int x, int y, int r, int explosionFieldR, 
//             Image itemNormal, Image itemBoom,ItemsAnimator.Kind kind, ItemsAnimator.Boom boom, 
//               int BOMB_NORMAL_X, int BOMB_NORMAL_Y,
//            int BOMB_BOOM_X, int BOMB_BOOM_Y,double boomPower)
        boomV = new Vector();
        delayBoom = new Delay(300);
        this.setSituation(situation.EXIST);

    }

    @Override
    public Vector getBoomV() {
        return boomV;
    }

    public void setBoomV(Vector vector) {
        this.boomV = vector;
    }

    @Override
    public void afterSnailEated() {
        moveWithSnailY = this.snailRef.getSpeedY();
        moveWithSnailX = this.snailRef.getSpeedX();
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
        if (situation == situation.EATED) {
            this.collider().setCenter(snailRef.collider().centerX(), snailRef.collider().centerY() - 50);
            this.painter().setCenter(snailRef.collider().centerX(), snailRef.collider().centerY() - 50);
            afterSnailEated();
            translateAll((int) moveWithSnailX, (int) moveWithSnailY);
        }
        if (situation == situation.THROWED) {
            translateAll(0, 0);

        }
        if (situation == situation.THROWED || situation == situation.STOP) {
            delayBoom.play();
            if (delayBoom.count()) {
                super.setState(ItemsAnimator.State.BOOM);
                AudioResourceController.getInstance().play(new Path().sounds().items().ItemsTrapBoom());
                ArrayList<String> changeState = new ArrayList<>();
                changeState.add(Integer.toString(this.getSerialNum()));
                changeState.add("BOOM");
                ClientClass.getInstance().sent(Global.ClientMessage.CHANGE_ITEM_STATE, changeState);
            }
            if (situation == situation.STOP) {
                boomV.setVectorXY(painter().centerX(), painter().centerY());
            }
        }
    }

    @Override
    public void mouseTrig(Rect rect, MouseEvent e, CommandSolver.MouseState state, long trigTime) {
        if (situation == situation.EATED) {
            if (state == CommandSolver.MouseState.PRESSED) {
                situation = situation.THROWED;
                AudioResourceController.getInstance().play(new Path().sounds().items().ItemsThrowSwipe());
                ArrayList<String> changeSituation = new ArrayList<>();
                changeSituation.add(Integer.toString(this.getSerialNum()));
                changeSituation.add("THROWED");
                ClientClass.getInstance().sent(CHANGE_ITEM_SITUATION, changeSituation);
                changeSituation.clear();
                boomV = new Vector(this.painter().centerX(), this.painter().centerY());
                ArrayList<String> setBoomV = new ArrayList<>();
                setBoomV.add(Integer.toString(super.getSerialNum()));
                setBoomV.add(Integer.toString(this.painter().centerX()));
                setBoomV.add(Integer.toString(this.painter().centerY()));
                ClientClass.getInstance().sent(ClientMessage.ITEM_SET_MOUSEEVENT, setBoomV);
                setBoomV.clear();
            }
        }
    }

    @Override
    public void setMouseEvent(ArrayList<String> strs) {
        boomV = new Vector(Double.parseDouble(strs.get(1)), Double.parseDouble(strs.get(2)));
    }

}
