package snailstrike.skills;

import java.awt.Graphics;
import snailstrike.gameobj.GameObject;
import snailstrike.gameobj.Vector;
import snailstrike.animators.*;
import snailstrike.gameobj.Snail;
import snailstrike.utils.Delay;
import snailstrike.utils.Global;

public class Skill extends GameObject {

    protected String kindOfSkill;
    protected SkillAnimator animator;
    protected Vector speed;
    protected int distance;
    protected int skillPower;
    protected Snail snailReference;
    protected Delay delayDisappear;
    protected int snailEmissionNum;

    public Skill(int snailEmissionNum, String kindOfSkill, int x, int y, int r, Vector vector) {
        super(x, y, r);
        this.snailEmissionNum = snailEmissionNum;
        this.speed = vector;
        this.distance = 0;
        this.kindOfSkill = kindOfSkill;
        this.delayDisappear = new Delay(10);
    }
    
    public String kindOfSkill(){
        return this.kindOfSkill;
    }

    public Vector getSpeed() {
        return this.speed;
    }
    
    public int getSnailEmissionNum(){
        return this.snailEmissionNum;
    }

    public int distance() {
        return this.distance;
    }
    
    public void setDistance(int distance){
        this.distance = distance;
    }
    
    public int skillPower(){
        return this.skillPower;
    }
    
    public Delay delayDisappear(){
        return this.delayDisappear;
    }
    
    public Snail getSnailReference(){
        return this.snailReference;
    }

    @Override
    public void paintComponent(Graphics g) {
        this.animator.paintComponent(Global.Direction.DOWN, this.painter().left(), this.painter().top(),
                this.painter().right(), this.painter().bottom(), g);
    }

    @Override
    public void update() {
        this.translate((int) speed.vx(), (int) speed.vy());
        this.distance += this.speed.length();
        this.animator.update();
    }
}
