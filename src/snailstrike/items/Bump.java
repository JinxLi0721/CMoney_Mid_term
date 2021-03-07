package snailstrike.items;

import client.ClientClass;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import snailstrike.animators.ItemsAnimator;
import snailstrike.controllers.SceneController;
import snailstrike.gameobj.Rect;
import snailstrike.gameobj.Snail;
import snailstrike.gameobj.Vector;
import snailstrike.utils.CommandSolver;
import snailstrike.utils.Delay;
import snailstrike.utils.Global;
import snailstrike.utils.Path;

public class Bump extends Items {

    private Delay delayRemove;
    private Delay delayBoom;
    private double moveWithSnailX;//被蝸牛吃到時跟著蝸牛走的移動
    private double moveWithSnailY;//被蝸牛吃到時跟著蝸牛走的移動
    private double moveX;//丟炸彈時的移動
    private double moveY;//丟炸彈時的移動
    private Vector mouseV;
    private Vector bombV;
    private Vector newV;//bombV+mouseV，只用來除 移動時要多久到達
    private Vector totalDis;//移動xy總距離
    private Vector boomV;//爆炸時的位置

    public Bump(int serialNum, int x, int y) {
        super(serialNum, x, y, 20, 20,
                SceneController.instance().irc().tryGetImage(new Path().img().props().Bump()),
                SceneController.instance().irc().tryGetImage(new Path().img().props().Bump()),
                ItemsAnimator.Kind.BUMP, ItemsAnimator.Boom.BUMP_BOOM, 454, 454, 454, 454, 10);

//     (int x, int y, int r, int explosionFieldR, 
//             Image itemNormal, Image itemBoom,ItemsAnimator.Kind kind, ItemsAnimator.Boom boom, 
//               int BOMB_NORMAL_X, int BOMB_NORMAL_Y,
//            int BOMB_BOOM_X, int BOMB_BOOM_Y,double boomPower)
        totalDis = new Vector();
        boomV = new Vector();
        delayRemove = new Delay(300);
        this.setSituation(situation.EXIST);
        delayBoom = new Delay(10);

    }

    @Override
    public Vector getBoomV() {
        return boomV;
    }

    @Override
    public void boomMoving(Snail snail) {
//        boomV.sub(snail.getSpeed());
//        snail.translate((int)(boomV.vx()*boomPower), (int)(boomV.vy()*boomPower));
    }

    @Override
    public void afterSnailEated() {
//        moveWithSnailX = this.snailRef.getSpeedX() * 0.3;
//        moveWithSnailY = this.snailRef.getSpeedY() * 0.3;

        moveWithSnailX = this.snailRef.getSpeedX() * 1.001;
        moveWithSnailY = this.snailRef.getSpeedY() * 1.001;
        this.snailRef.setSpeedXY(moveWithSnailX, moveWithSnailY);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
    }

    @Override
    public void update() {
        super.update();
//        if (situation == situation.EATED) {
//            translateAll((int) moveWithSnailX, (int) moveWithSnailY);
//                delayRemove.play();
//            if (delayRemove.count()) {
//                super.setState(ItemsAnimator.State.REMOVE);
//            }
//        }
        if (situation == situation.EATED || situation == situation.THROWED || situation == situation.STOP) {
//            delayBoom.play();
//            if (delayBoom.count()) {
//                super.setState(ItemsAnimator.State.BOOM);
//            }
            this.collider().setCenter(snailRef.collider().centerX(), snailRef.collider().centerY() + 18);
            this.painter().setCenter(snailRef.collider().centerX(), snailRef.collider().centerY() + 18);
            afterSnailEated();
            translateAll((int) moveWithSnailX, (int) moveWithSnailY);
            delayRemove.play();

            if (delayRemove.count()) {
                this.snailRef.setSpeedXY(snailRef.getSpeedX() / 1.01, this.snailRef.getSpeedY() / 1.01);
                super.setState(ItemsAnimator.State.REMOVE);
                ArrayList<String> changeState = new ArrayList<>();
                changeState.add(Integer.toString(this.getSerialNum()));
                changeState.add("REMOVE");
                ClientClass.getInstance().sent(Global.ClientMessage.CHANGE_ITEM_STATE, changeState);
            }
        }

        if (situation == situation.STOP) {
            boomV.setVectorXY(painter().centerX(), painter().centerY());
        }
//        if (Global.IS_DEBUG_PRINT) {
//            System.out.println("bombupdate");
//        }
    }

    @Override
    public void mouseTrig(Rect rect, MouseEvent e, CommandSolver.MouseState state, long trigTime) {
//        if (situation == situation.EATED) {
//            if (state == CommandSolver.MouseState.PRESSED) {
//                situation = situation.THROWED;
//                bombV = new Vector(this.painter().centerX(), this.painter().centerY());
//                if (Global.IS_DEBUG_PRINT) {
//                    System.out.println(e.getPoint() + "E");
//                    System.out.println(rect.left() + "RECT" + rect.top());
//                    System.out.println(this.bombV.vx() + "BOMB" + this.bombV.vy());
//                }
//                mouseV = new Vector(e.getX() + rect.left(), e.getY() + rect.top());
//                newV = bombV.add(mouseV);
//
//            }
//        }
    }

    @Override
    public void setMouseEvent(ArrayList<String> strs) {

    }
}
