package snailstrike.items;

import client.ClientClass;
import snailstrike.animators.ItemsAnimator;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import snailstrike.controllers.AudioResourceController;

import snailstrike.controllers.SceneController;
import snailstrike.gameobj.Rect;
import snailstrike.gameobj.Snail;
import snailstrike.gameobj.Vector;
import snailstrike.utils.CommandSolver;
import snailstrike.utils.Delay;
import snailstrike.utils.Global;
import snailstrike.utils.Path;

public class Bomb extends Items {

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

//    private int boomPower;
    public Bomb(int serialNum, int x, int y) {

        super(serialNum, x, y, 20, 200,
                SceneController.instance().irc().tryGetImage(new Path().img().props().bombNormal()),
                SceneController.instance().irc().tryGetImage(new Path().img().props().explosion_sp()),
                ItemsAnimator.Kind.BOMB, ItemsAnimator.Boom.BOMB_BOOM, 200, 199, 95, 94, 10);//explosionFieldR>70好像才正常
//     (int x, int y, int r, int explosionFieldR, 
//             Image itemNormal, Image itemBoom,ItemsAnimator.Kind kind, ItemsAnimator.Boom boom, 
//               int BOMB_NORMAL_X, int BOMB_NORMAL_Y,
//            int BOMB_BOOM_X, int BOMB_BOOM_Y,double boomPower)

        totalDis = new Vector();
        boomV = new Vector();
        delayBoom = new Delay(90);
        this.setSituation(situation.EXIST);

//        boomPower = 1;
    }

    @Override
    public Vector getBoomV() {
        return boomV;
    }

    public void setBoomV(Vector vector) {
        this.boomV = vector;
    }

    @Override
    public void boomMoving(Snail snail) {
//        boomV.sub(snail.getSpeed());
//        snail.translate((int)(boomV.vx()*boomPower), (int)(boomV.vy()*boomPower));
    }

    public boolean bombMove() {
        if (totalDis.length() <= mouseV.sub(bombV).length() / 2) {
            if (mouseV.vx() < bombV.vx()) {
                moveX -= (bombV.vx() - mouseV.vx()) / (newV.length() / 15);
            } else {
                moveX += (mouseV.vx() - bombV.vx()) / (newV.length() / 15);
            }
            if (mouseV.vy() < bombV.vy()) {
                moveY -= (bombV.vy() - mouseV.vy()) / (newV.length() / 15);
            } else {
                moveY += (mouseV.vy() - bombV.vy()) / (newV.length() / 15);
            }
        } else {
            if (mouseV.vx() < bombV.vx()) {
                moveX += (bombV.vx() - mouseV.vx()) / (newV.length() / 15);
            } else {
                moveX -= (mouseV.vx() - bombV.vx()) / (newV.length() / 15);
            }
            if (mouseV.vy() < bombV.vy()) {
                moveY += (bombV.vy() - mouseV.vy()) / (newV.length() / 15);
            } else {
                moveY -= (mouseV.vy() - bombV.vy()) / (newV.length() / 15);
            }
        }
        totalDis.setVectorXY(totalDis.vx() + Math.abs(moveX), totalDis.vy() + Math.abs(moveY));
        return totalDis.length() >= mouseV.sub(bombV).length();

    }

    @Override
    public void afterSnailEated() {
        moveWithSnailY = this.snailRef.getSpeedY();
        moveWithSnailX = this.snailRef.getSpeedX();
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
            if (bombMove()) {
                situation = situation.STOP;
                ArrayList<String> changeSituation = new ArrayList<>();
                changeSituation.add(Integer.toString(this.getSerialNum()));
                changeSituation.add("STOP");
                ClientClass.getInstance().sent(Global.ClientMessage.CHANGE_ITEM_SITUATION, changeSituation);
                translateAll(0, 0);
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
                bombV = new Vector(this.painter().centerX(), this.painter().centerY());
                if (Global.IS_DEBUG_PRINT) {
                    System.out.println(e.getPoint() + "E");
                    System.out.println(rect.left() + "RECT" + rect.top());
                    System.out.println(this.bombV.vx() + "BOMB" + this.bombV.vy());
                }
                mouseV = new Vector(e.getX() + rect.left(), e.getY() + rect.top());
                newV = bombV.add(mouseV);
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
        bombV = new Vector(Double.parseDouble(strs.get(1)), Double.parseDouble(strs.get(2)));
        mouseV = new Vector(Double.parseDouble(strs.get(3)), Double.parseDouble(strs.get(4)));
        newV = bombV.add(mouseV);
    }
}
