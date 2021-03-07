package snailstrike.items;

import client.ClientClass;
import java.awt.Graphics;
import java.util.ArrayList;
import snailstrike.animators.ItemsAnimator;
import snailstrike.controllers.AudioResourceController;

import snailstrike.controllers.SceneController;
import snailstrike.gameobj.Snail;
import snailstrike.gameobj.Vector;
import snailstrike.utils.Delay;
import snailstrike.utils.Global;
import snailstrike.utils.Path;

public class Defend extends Items {

    private Delay delayRemove;
    private double moveWithSnailX;//被蝸牛吃到時跟著蝸牛走的移動
    private double moveWithSnailY;//被蝸牛吃到時跟著蝸牛走的移動
    private int snailX;
    private int snailY;
    private Vector boomV;//爆炸時的位置

    public Defend(int serialNum, int x, int y) {
        super(serialNum, x, y, 20, 18,
                SceneController.instance().irc().tryGetImage(new Path().img().props().Defend()),
                ItemsAnimator.Kind.DEFEND, ItemsAnimator.Boom.BOMB_BOOM, 501, 557, 501, 557, 1);//explosionFieldR>70好像才正常
//     (int x, int y, int r, int explosionFieldR, 
//             Image itemNormal, Image itemBoom,ItemsAnimator.Kind kind, ItemsAnimator.Boom boom, 
//               int BOMB_NORMAL_X, int BOMB_NORMAL_Y,
//            int BOMB_BOOM_X, int BOMB_BOOM_Y,double boomPower)
        delayRemove = new Delay(400);
        this.setSituation(situation.EXIST);

    }

    @Override
    public void boomMoving(Snail snail) {

    }

    @Override
    public Vector getBoomV() {
        return null;
    }

    public void setBoomV(Vector vector) {
        this.boomV = vector;
    }

    @Override
    public void afterSnailEated() {
        this.snailRef.isKeepHp();
//        snailX = snail.painter().centerX()+10;
//        snailY = snail.painter().centerY()+10;
        moveWithSnailX = this.snailRef.getSpeedX();
        moveWithSnailY = this.snailRef.getSpeedY();

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
            delayRemove.play();
            AudioResourceController.getInstance().play(new Path().sounds().items().ItemsThrowSwipe());

            if (delayRemove.count()) {
                super.setState(ItemsAnimator.State.REMOVE);
                ArrayList<String> changeState = new ArrayList<>();
                changeState.add(Integer.toString(this.getSerialNum()));
                changeState.add("REMOVE");
                ClientClass.getInstance().sent(Global.ClientMessage.CHANGE_ITEM_STATE, changeState);
            }

        }
    }

    @Override
    public void setMouseEvent(ArrayList<String> strs) {

    }
}
