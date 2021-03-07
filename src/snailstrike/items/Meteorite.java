package snailstrike.items;

import client.ClientClass;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import snailstrike.animators.ItemsAnimator;
import snailstrike.animators.ItemsAnimator.State;
import snailstrike.controllers.AudioResourceController;

import snailstrike.controllers.SceneController;
import snailstrike.gameobj.Rect;
import snailstrike.gameobj.Snail;
import snailstrike.gameobj.Vector;
import snailstrike.utils.CommandSolver;
import snailstrike.utils.Delay;
import snailstrike.utils.Global;
import snailstrike.utils.Path;

public class Meteorite extends Items {

    private Delay delayBoom;
    private double moveWithSnailX;//被蝸牛吃到時跟著蝸牛走的移動
    private double moveWithSnailY;//被蝸牛吃到時跟著蝸牛走的移動
    private double moveX;//丟炸彈時的移動
    private double moveY;//丟炸彈時的移動
    private Vector mouseV;
    private Vector meteoriteV;
    private Vector newV;//bombV+mouseV，只用來除 移動時要多久到達
    private Vector totalDis;//移動xy總距離
    private Vector boomV;//爆炸時的位置

    public Meteorite(int serialNum, int x, int y) {
        super(serialNum, x, y, 80, 200,
                SceneController.instance().irc().tryGetImage(new Path().img().props().Meteorite()),
                SceneController.instance().irc().tryGetImage(new Path().img().props().MeteoriteExplose()),
                ItemsAnimator.Kind.METEORITE, ItemsAnimator.Boom.METEORITE_BOOM, 208, 194, 600, 338, 15);//explosionFieldR>70好像才正常
//     (int x, int y, int r, int explosionFieldR, 
//             Image itemNormal, Image itemBoom,ItemsAnimator.Kind kind, ItemsAnimator.Boom boom, 
//               int BOMB_NORMAL_X, int BOMB_NORMAL_Y,
//            int BOMB_BOOM_X, int BOMB_BOOM_Y,double boomPower)

        totalDis = new Vector();
        boomV = new Vector();
        delayBoom = new Delay(90);
        this.setSituation(situation.EXIST);
    }

    public boolean meteoriteMove() {
        totalDis.setVectorXY(totalDis.vx() + Math.abs(moveX), totalDis.vy() + Math.abs(moveY));
        if (totalDis.length() <= mouseV.sub(meteoriteV).length()) {
            moveX += (mouseV.vx() - meteoriteV.vx()) / (newV.length());
            moveY += (mouseV.vy() - meteoriteV.vy()) / (newV.length());
        }
        if (totalDis.length() != 0) {
            return totalDis.length() >= mouseV.sub(meteoriteV).length();
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
        return boomV;
    }

    @Override
    public void paintComponent(Graphics g) {
        int i = 0;
        i += 50;
        super.getItemsAnimator().paintBox(this.painter().centerX() - 20, this.painter().centerY() - 20, this.painter().centerX() + 20, this.painter().centerY() + 20, g);
        super.getItemsAnimator().paint(this.painter().left() + i, this.painter().top() + i, this.painter().right() - i, this.painter().bottom() - i, g);
        if (super.getState() == State.BOOM) {
            super.getItemsAnimator().paint(this.painter().left(), this.painter().top(), this.painter().right(), this.painter().bottom(), g);
        }

//        super.paintComponent(g);
    }

    @Override
    public void update() {
        super.update();
//        if(situation == situation.EXIST){
//            super.setState(ItemsAnimator.State.BOX_EXIST);
//        }
        if (situation == situation.EATED) {
            this.collider().setCenter(snailRef.collider().centerX(), snailRef.collider().centerY() - 50);
            this.painter().setCenter(snailRef.collider().centerX(), snailRef.collider().centerY() - 50);
            afterSnailEated();
            translateAll((int) moveWithSnailX, (int) moveWithSnailY);
        }
        if (situation == situation.THROWED) {
            translateAll((int) moveX, (int) moveY);
            if (meteoriteMove()) {
                situation = situation.STOP;
                ArrayList<String> changeSituation = new ArrayList<>();
                changeSituation.add(Integer.toString(this.getSerialNum()));
                changeSituation.add("STOP");
                ClientClass.getInstance().sent(Global.ClientMessage.CHANGE_ITEM_SITUATION, changeSituation);
                translateAll(0, 0);
                if (Global.IS_DEBUG_PRINT) {
                    System.out.println("final" + this.collider().left() + ", " + this.collider().top());
                }
            }
        }
        if (situation == situation.THROWED || situation == situation.STOP) {
            delayBoom.play();
            if (delayBoom.count()) {
                super.setState(ItemsAnimator.State.BOOM);
                AudioResourceController.getInstance().play(new Path().sounds().items().ItemsLongBoom());
                ArrayList<String> changeState = new ArrayList<>();
                changeState.add(Integer.toString(this.getSerialNum()));
                changeState.add("BOOM");
                ClientClass.getInstance().sent(Global.ClientMessage.CHANGE_ITEM_STATE, changeState);
            }
            if (situation == situation.STOP) {
                boomV.setVectorXY(painter().centerX(), painter().centerY());
            }
        }
//        if (Global.IS_DEBUG_PRINT) {
//            System.out.println("bombupdate");
//        }
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
                ClientClass.getInstance().sent(Global.ClientMessage.CHANGE_ITEM_SITUATION, changeSituation);
                changeSituation.clear();
                meteoriteV = new Vector(this.painter().centerX(), this.painter().centerY());
                mouseV = new Vector(e.getX() + rect.left(), e.getY() + rect.top());
                newV = meteoriteV.add(mouseV);
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
        newV = meteoriteV.add(mouseV);
    }
}
