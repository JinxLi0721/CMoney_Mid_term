package snailstrike.items;

import snailstrike.animators.ItemsAnimator;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import snailstrike.animators.ItemsAnimator.*;
import snailstrike.controllers.SceneController;
import snailstrike.gameobj.Rect;
import snailstrike.gameobj.Snail;
import snailstrike.gameobj.Vector;
import snailstrike.utils.CommandSolver;
import snailstrike.utils.Path;

public abstract class Items extends Explosion {
    
    protected Snail snailRef;
    private int serialNum;
    private ItemsAnimator propsAnimator;
    public Situation situation;
    private double boomPower;
    private Image itemExist;

    public enum Situation {
        EXIST,
        EATED,
        THROWED,
        STOP;//看有沒有很多道具有其他動作需要，不然直接設translate(0,0)就好
    }

    public Items(int serialNum, int x, int y, int r, int explosionFieldR) {
        super(x, y, r, explosionFieldR);
        this.serialNum = serialNum;
    }

    public Items(int serialNum, int x, int y, int r, int explosionFieldR,
            Image itemNormal, Image itemBoom, Kind kind, Boom boom,
            int BOMB_NORMAL_X, int BOMB_NORMAL_Y,
            int BOMB_BOOM_X, int BOMB_BOOM_Y, double boomPower) {
        super(x, y, r, explosionFieldR);
        this.serialNum = serialNum;
        itemExist = SceneController.instance().irc().tryGetImage(new Path().img().props().BoxExist());
        propsAnimator = new ItemsAnimator(itemExist, itemNormal, itemBoom, kind, boom,
                BOMB_NORMAL_X, BOMB_NORMAL_Y,
                BOMB_BOOM_X, BOMB_BOOM_Y);
        this.boomPower = boomPower;

    }

    public Items(int serialNum, int x, int y, int r, int explosionFieldR,
            Image itemNormal, Kind kind, Boom boom,
            int BOMB_NORMAL_X, int BOMB_NORMAL_Y,
            int BOMB_BOOM_X, int BOMB_BOOM_Y, double boomPower) {
        super(x, y, r, explosionFieldR);
        this.serialNum = serialNum;
        itemExist = SceneController.instance().irc().tryGetImage(new Path().img().props().BoxExist());
        propsAnimator = new ItemsAnimator(itemExist, itemNormal, kind, boom,
                BOMB_NORMAL_X, BOMB_NORMAL_Y,
                BOMB_BOOM_X, BOMB_BOOM_Y);
        this.boomPower = boomPower;
    }

    public void setSituation(Situation situation) {
        this.situation = situation;
    }

    public int getSerialNum() {
        return this.serialNum;
    }
    
    public ItemsAnimator getItemsAnimator(){
        return propsAnimator;
    }

    public Situation getSituation() {
        return situation;
    }

    public void setState(State state) {
        propsAnimator.setState(state);
    }
    
    public void setSnailRef(Snail snail){
        this.snailRef = snail;
    }
    
    public Snail getSnailRef(){
        return this.snailRef;
    }

    public double getBoomPower() {
        return this.boomPower;
    }

    public State getState() {
        return this.propsAnimator.getState();
    }

    public Kind getKind() {
        return this.propsAnimator.getKind();
    }

    public void setAllCenter(int x, int y) {
        this.painter().setCenter(x, y);
        this.collider().setCenter(x, y);
        this.explosionField().setCenter(x, y);

    }

    public abstract void afterSnailEated();

    public abstract void boomMoving(Snail snail);

    public abstract Vector getBoomV();

    public abstract void setMouseEvent(ArrayList<String> strs);

    @Override
    public void paintComponent(Graphics g) {
        this.propsAnimator.paintBox(this.painter().centerX() - 20, this.painter().centerY() - 20, this.painter().centerX() + 20, this.painter().centerY() + 20, g);
        this.propsAnimator.paint(this.painter().left(), this.painter().top(), this.painter().right(), this.painter().bottom(), g);
    }

    @Override
    public void update() {
        propsAnimator.update();
        if(this.situation == situation.EATED){
            this.afterSnailEated();
        }
    }

    public void mouseTrig(Rect rect, MouseEvent e, CommandSolver.MouseState state, long trigTime) {

    }

}
