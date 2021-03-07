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

public class MeteoriteRoll extends Items {

    private Delay delayBoom;
    private double moveWithSnailX;//被蝸牛吃到時跟著蝸牛走的移動
    private double moveWithSnailY;//被蝸牛吃到時跟著蝸牛走的移動
    private double moveX;//丟炸彈時的移動
    private double moveY;//丟炸彈時的移動
    private Vector mouseV;
    private Vector meteoriteV;
    private Vector newV;//bombV-mouseV的向量
    private Vector totalDis;//移動xy總距離
    private Vector boomV;//爆炸時的位置

    public MeteoriteRoll(int serialNum, int x, int y) {
        super(serialNum, x, y, 30, 200,
                SceneController.instance().irc().tryGetImage(new Path().img().props().MeteoriteRoll()),
                SceneController.instance().irc().tryGetImage(new Path().img().props().MeteoriteRollExplose()),
                ItemsAnimator.Kind.METEORITE_ROLL, ItemsAnimator.Boom.METEORITE_ROLL_BOOM, 158, 143, 590, 324, 20);//explosionFieldR>70好像才正常
//     (int x, int y, int r, int explosionFieldR, 
//             Image itemNormal, Image itemBoom,ItemsAnimator.Kind kind, ItemsAnimator.Boom boom, 
//               int BOMB_NORMAL_X, int BOMB_NORMAL_Y,
//            int BOMB_BOOM_X, int BOMB_BOOM_Y,double boomPower)
        totalDis = new Vector();
        boomV = new Vector();
        delayBoom = new Delay(18);
        this.setSituation(situation.EXIST);

    }

    public boolean meteoriteMove() {
        totalDis.setVectorXY(totalDis.vx() + Math.abs(moveX), totalDis.vy() + Math.abs(moveY));
        if (totalDis.length() <= mouseV.sub(meteoriteV).length()) {
            moveX += newV.vx() / 4;
            moveY += newV.vy() / 4;
        }
        if (totalDis.length() != 0) {
            return totalDis.length() >= newV.length();
        }
        return false;
    }

    @Override
    public void afterSnailEated() {

        moveWithSnailY = this.snailRef.getSpeedY();
        moveWithSnailX = this.snailRef.getSpeedX();
    }

    @Override
    public void boomMoving(Snail snail) {
        return;
    }

    @Override
    public Vector getBoomV() {
//        return boomV;
        return new Vector(moveX, moveY);
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
            translateAll((int) moveX, (int) moveY);
            if (meteoriteMove()) {
                delayBoom.play();
                if (delayBoom.count()) {
                    super.setState(ItemsAnimator.State.BOOM);
                    AudioResourceController.getInstance().shot(new Path().sounds().items().ItemsStonesRollingInWater());
//                    AudioResourceController.getInstance().stop(new Path().sounds().items().ItemsStonesRollingInWater());
                    ArrayList<String> changeState = new ArrayList<>();
                    changeState.add(Integer.toString(this.getSerialNum()));
                    changeState.add("BOOM");
                    ClientClass.getInstance().sent(Global.ClientMessage.CHANGE_ITEM_STATE, changeState);
                }
                boomV.setVectorXY(painter().centerX(), painter().centerY());
            }
        }
    }

    @Override
    public void mouseTrig(Rect rect, MouseEvent e, CommandSolver.MouseState state, long trigTime) {
        int x = 0;
        int y = 0;
        if (situation == situation.EATED) {
            if (state == CommandSolver.MouseState.PRESSED) {
                situation = situation.THROWED;
                AudioResourceController.getInstance().shot(new Path().sounds().items().ItemsThrowSwipe());
                ArrayList<String> changeSituation = new ArrayList<>();
                changeSituation.add(Integer.toString(this.getSerialNum()));
                changeSituation.add("THROWED");
                ClientClass.getInstance().sent(Global.ClientMessage.CHANGE_ITEM_SITUATION, changeSituation);
                changeSituation.clear();
                x = (this.collider().centerX());
                y = (this.collider().centerY());
                meteoriteV = new Vector(x, y);
                mouseV = new Vector(e.getX() + rect.left(), e.getY() + rect.top());
                newV = mouseV.sub(meteoriteV).normalize().multiply(5);
                ArrayList<String> setMouseV = new ArrayList<>();
                setMouseV.add(Integer.toString(super.getSerialNum()));
                setMouseV.add(Integer.toString(this.painter().centerX()));
                setMouseV.add(Integer.toString(this.painter().centerY()));
                setMouseV.add(Double.toString(mouseV.vx()));
                setMouseV.add(Double.toString(mouseV.vy()));
                ClientClass.getInstance().sent(Global.ClientMessage.ITEM_SET_MOUSEEVENT, setMouseV);
                setMouseV.clear();
            }
        }
    }

    @Override
    public void setMouseEvent(ArrayList<String> strs) {
        meteoriteV = new Vector(Double.parseDouble(strs.get(1)), Double.parseDouble(strs.get(2)));
        mouseV = new Vector(Double.parseDouble(strs.get(3)), Double.parseDouble(strs.get(4)));
        newV = mouseV.sub(meteoriteV).normalize().multiply(5);
    }
}
