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
import snailstrike.utils.Path;

public class BlackHole extends Items {

    protected double moveX;//丟炸彈時的移動
    protected double moveY;//丟炸彈時的移動
    private Delay delayBoom;
    private double moveWithSnailX;//被蝸牛吃到時跟著蝸牛走的移動
    private double moveWithSnailY;//被蝸牛吃到時跟著蝸牛走的移動
    private Vector boomV;//爆炸時的位置
    protected Vector mouseV;
    protected Vector newV;//bombV+mouseV，只用來除 移動時要多久到達
    protected Vector totalDis;//移動xy總距離

    public BlackHole(int serialNum, int x, int y) {
        super(serialNum, x, y, 60, 300,
                SceneController.instance().irc().tryGetImage(new Path().img().props().BlackHoleNormal()),
                SceneController.instance().irc().tryGetImage(new Path().img().props().BlackHoleBoom()),
                ItemsAnimator.Kind.BLACK_HOLE, ItemsAnimator.Boom.BLACK_HOLE_BOOM,
                150, 150, 150, 150, 4);
//             Items(int x, int y, int r, int explosionFieldR, 
//             Image itemNormal, Image itemBoom,ItemsAnimator.Kind kind, ItemsAnimator.Boom boom, 
//               int BOMB_NORMAL_X, int BOMB_NORMAL_Y,
//            int BOMB_BOOM_X, int BOMB_BOOM_Y,double boomPower)
        super.getItemsAnimator().setRemoveDelay(200);
        boomV = new Vector();
        delayBoom = new Delay(20);
        totalDis = new Vector();
        this.setSituation(situation.EXIST);
    }

    @Override
    public void afterSnailEated() {
        moveWithSnailY = this.snailRef.getSpeedY();
        moveWithSnailX = this.snailRef.getSpeedX();
    }

    public boolean blackHoleMove() {
        totalDis.setVectorXY(totalDis.vx() + Math.abs(moveX), totalDis.vy() + Math.abs(moveY));
        if (totalDis.length() <= mouseV.sub(boomV).length()) {
            moveX += (mouseV.vx() - boomV.vx()) / (newV.length() / 50);
            moveY += (mouseV.vy() - boomV.vy()) / (newV.length() / 50);
        }
        if (totalDis.length() != 0) {
            return totalDis.length() >= mouseV.sub(boomV).length();
        }
        return false;
    }

    @Override
    public void boomMoving(Snail snail) {

    }

    public void setBoomV(Vector vector) {
        this.boomV = vector;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

    }

    @Override
    public Vector getBoomV() {
        return boomV;
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
            if (blackHoleMove()) {
                situation = situation.STOP;
                ArrayList<String> changeSituation = new ArrayList<>();
                changeSituation.add(Integer.toString(this.getSerialNum()));
                changeSituation.add("STOP");
                ClientClass.getInstance().sent(ClientMessage.CHANGE_ITEM_SITUATION, changeSituation);
                translateAll(0, 0);
            }
        }
        if (situation == situation.THROWED || situation == situation.STOP) {
            delayBoom.play();
            if (delayBoom.count()) {
                super.setState(ItemsAnimator.State.BOOM);
                AudioResourceController.getInstance().shot(new Path().sounds().items().ItemsLikeCrazyBird());
                ArrayList<String> changeState = new ArrayList<>();
                changeState.add(Integer.toString(this.getSerialNum()));
                changeState.add("BOOM");
                ClientClass.getInstance().sent(ClientMessage.CHANGE_ITEM_STATE, changeState);
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
                AudioResourceController.getInstance().shot(new Path().sounds().items().ItemsThrowSwipe());

                ArrayList<String> changeSituation = new ArrayList<>();
                changeSituation.add(Integer.toString(this.getSerialNum()));
                changeSituation.add("THROWED");
                ClientClass.getInstance().sent(ClientMessage.CHANGE_ITEM_SITUATION, changeSituation);
                changeSituation.clear();
                boomV = new Vector(this.painter().centerX(), this.painter().centerY());
                mouseV = new Vector(e.getX() + rect.left(), e.getY() + rect.top());
                newV = boomV.add(mouseV);
                ArrayList<String> setBoomV = new ArrayList<>();
                setBoomV.add(Integer.toString(super.getSerialNum()));
                setBoomV.add(Integer.toString(this.painter().centerX()));
                setBoomV.add(Integer.toString(this.painter().centerY()));
                setBoomV.add(Double.toString(mouseV.vx()));
                setBoomV.add(Double.toString(mouseV.vy()));
                ClientClass.getInstance().sent(ClientMessage.ITEM_SET_MOUSEEVENT, setBoomV);
                setBoomV.clear();
            }
        }
    }

    @Override
    public void setMouseEvent(ArrayList<String> strs) {
        boomV = new Vector(Double.parseDouble(strs.get(1)), Double.parseDouble(strs.get(2)));
        mouseV = new Vector(Double.parseDouble(strs.get(3)), Double.parseDouble(strs.get(4)));
        newV = boomV.add(mouseV);
    }

}
