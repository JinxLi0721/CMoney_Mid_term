/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package snailstrike.scene;

import camera.Camera;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import snailstrike.animators.ItemsAnimator;
import snailstrike.gameobj.ExploseMove;
import snailstrike.gameobj.Snail;
import snailstrike.gameobj.Vector;
import snailstrike.items.Bomb;
import snailstrike.items.Items;
import snailstrike.skills.Skill;
import snailstrike.utils.CommandSolver;
import snailstrike.utils.Global;

/**
 *
 * @author Kylie
 */
public class SingleplayerScene extends Scene {

    private ArrayList<Snail> snails;
    private ArrayList<Skill> sceneEmission;
    private ArrayList<Items> propsList;
    private ExploseMove bombExploseMove;
      private Camera cam;

    @Override
    public void sceneBegin() {
        snails = new ArrayList<>();
        sceneEmission = new ArrayList<>();
        snails.add(new Snail(Global.SCREEN_X / 2, Global.SCREEN_Y / 2, "KYLIE", Snail.chooseSkills()));
        snails.add(new Snail(Global.SCREEN_X / 2, Global.SCREEN_Y / 2, "ZIYING", Snail.chooseSkills()));
        propsList = new ArrayList<>();
        propsList.add(new Bomb(0, 300,300));
        bombExploseMove = new ExploseMove(10);
        for (int i = 0; i < propsList.size(); i++) {
            propsList.get(i).setSituation(Items.Situation.EXIST);
        }
          cam = new Camera.Builder(1280, 760)
                .setCameraStartLocation(1500, 1500)
                //                .setCameraWindowLocation(1500,1500)
                .setChaseObj(snails.get(0))
                .gen();
    }

    @Override
    public void sceneEnd() {

    }

    @Override
    public void paint(Graphics g) {
        if (Global.IS_DEBUG) {
            g.setColor(new Color(115, 74, 18));
            g.fillOval(Global.SCREEN_X / 2 - Global.CIRCLE_RANGE_SHRINK, Global.SCREEN_Y / 2 - Global.CIRCLE_RANGE_SHRINK, Global.CIRCLE_RANGE_SHRINK * 2, Global.CIRCLE_RANGE_SHRINK * 2);
        }
        for (int i = 0; i < this.snails.size(); i++) {
            this.snails.get(i).paint(g);
//            for (int p = 0; p < propsList.size(); p++) {
//                if (propsList.get(p).isCollision(snails.get(i))) {//吃到
//                    propsList.get(p).afterSnailEated(snails.get(i));
//                }
//            }
        }
        for (int i = 0; i < this.sceneEmission.size(); i++) {
            this.sceneEmission.get(i).paint(g);
        }
        for (int i = 0; i < propsList.size(); i++) {
            propsList.get(i).paintAll(g);
        }
    }

    @Override
    public void update() {
        for (int i = 0; i < this.sceneEmission.size(); i++) {
            this.sceneEmission.get(i).update();
            if (this.sceneEmission.get(i).getSpeed().vx() < 1 && this.sceneEmission.get(i).getSpeed().vy() < 1){
                sceneEmission.remove(i--);
                continue;
            }
            for (int j = 0; j < this.snails.size(); j++) {
                if (this.sceneEmission.get(i).isCollision(this.snails.get(j))) {
                    double vectorX = this.snails.get(j).collider().centerX() - 
                            this.sceneEmission.get(i).collider().centerX();
                    double vectorY = this.snails.get(j).collider().centerY() - 
                            this.sceneEmission.get(i).collider().centerY();
                    Vector snailVector = new Vector(this.snails.get(j).getSpeedX(), this.snails.get(j).getSpeedY());
                    Vector collisionVector = snailVector.add(new Vector(vectorX, vectorY));
                    this.snails.get(j).setSpeedXY(collisionVector.normalize().vx() * this.sceneEmission.get(i).skillPower(), collisionVector.normalize().vy() * this.sceneEmission.get(i).skillPower());
                }
            }
            if (this.sceneEmission.get(i).distance() > 500) {
                sceneEmission.remove(i--);
                continue;
            }
        }
          for (int i = 0; i < this.snails.size(); i++) {
            snails.get(i).update();
        }
        for (int i = 0; i < propsList.size(); i++) {
            propsList.get(i).update();
        }
          
        for (int w = 0; w < propsList.size(); w++) {
            for (int i = 0; i < this.snails.size(); i++) {
               
                Snail snail = snails.get(i);
             
                Items prop = propsList.get(w);
                if (propsList.get(w).getSituation() == Items.Situation.EXIST) {
                    if (propsList.get(w).isCollision(snail)) {//吃到
                        propsList.get(w).setSituation(Items.Situation.EATED);
                         
                    }
                } else if (propsList.get(w).getSituation() == Items.Situation.EATED
                        && snail.getSnailHp() > 0) {
                    propsList.get(w).setSnailRef(snails.get(i));
                    propsList.get(w).afterSnailEated();
                      
                }  else if (propsList.get(w).getSituation() == Items.Situation.STOP) {
                  

                    if (propsList.get(w).getState() == ItemsAnimator.State.BOOM) {
                      
                        if (propsList.get(w).isCollederOutterCollision(snail)) {
//                            if (propsList.get(i).situation == Situation.STOP) {
                            snail.isCollision();
                            Vector CollisionVector = setCollisionVector(snail.collider().centerX(), snail.collider().centerY(), prop.collider().centerX(), prop.collider().centerY());
                            Vector exploseMoveVector = exploseMoveVector(snail.getSpeed(), prop.getBoomV(), prop.getBoomPower(), CollisionVector);
                            snail.setSpeedXY(exploseMoveVector.vx(), exploseMoveVector.vy());
                        }
                    }
                }
//                if (propsList.get(w).getSituation() == Situation.EATED) {//想要當蝸牛吃到炸彈但還沒丟出去時hp就=0的時候，炸彈消失，但沒成功??
//                    if (snail.getSnailHp() < 0) {
//                        propsList.get(w).setState(ItemsAnimator.State.REMOVE);
//                    }
//                }
            }
            if (propsList.get(w).getState() == ItemsAnimator.State.REMOVE) {
                propsList.remove(w);
                System.out.println("PropsAnimator.State.REMOVE");
            }
//        for (int j = 0; j < this.snails.size(); j++) {
//            for (int i = 0; i < propsList.size(); i++) {
//                propsList.get(i).update();
//                Snail snail = snails.get(j);
//
//                if (propsList.get(i).getSituation() == Items.Situation.EXIST) {
//                    if (propsList.get(i).isCollision(snail)) {//吃到
//                        propsList.get(i).setSituation(Items.Situation.EATED);
//                    }
//                } else if (propsList.get(i).getSituation() == Items.Situation.EATED //                        && propsList.get(i).getSituation() != Situation.THROWED
//                        ) {
//                    propsList.get(i).afterSnailEated(snail);//如果蝸牛吃到道具，道具跟著蝸牛走
//
//                } else if (propsList.get(i).getSituation() == Items.Situation.STOP) {
//
//                    if (propsList.get(i).getState() == ItemsAnimator.State.BOOM) {
////                        System.out.println("snailX" + snail.getSpeedX() + "Y:" + snail.getSpeedY());
//                        if (propsList.get(i).isCollederOutterCollision(snail)) {
////                            if (propsList.get(i).situation == Situation.STOP) {
//                            bombExploseMove.setBombMoveSlowDownStart();
////                            }    
//                            bombExploseMove.setAllV(snail.getSpeed(), propsList.get(i).getBoomV());
//                        }
//                    }
//                }
////                if (propsList.get(i).isCollederOutterCollision(snail)) {//蝸牛碰到炸彈炸彈爆炸
////                    propsList.get(i).setState(ItemsAnimator.State.BOOM);
////                }
////                    if (Math.abs(snail.getSpeedX()) >= 0 || Math.abs(snail.getSpeedY()) >= 0) {
////                    snail.reverseVector();
////                        if (snail.getSpeedX() != 0 || snail.getSpeedY() != 0) {
////                            snail.translate(-(int) snail.getSpeedX() * 2, -(int) snail.getSpeedY() * 2);//如果被彈開時還繼續按方向鍵，speed會繼續加
////                        }
////                    }
////                    if (Math.abs(propsList.get(i).getMoveX()) >= 0 || Math.abs(propsList.get(i).getMoveY()) >= 0) {
////                        snail.translate((int) (propsList.get(i).getMoveX() * 1.5), (int) (propsList.get(i).getMoveY() * 1.5));//如果被彈開時還繼續按方向鍵，speed會繼續加
////                    }
////                         snail.setSpeedX(0);
////                snail.setSpeedY(0);
//                if (propsList.get(i).getState() == ItemsAnimator.State.REMOVE) {
//                    propsList.remove(i);
//                    System.out.println("ItemsAnimator.State.REMOVE");
//                }
//            }
//        }
    }
    }
 public Vector setCollisionVector(double snailCenterX, double snailCenterY, double attackCenterX, double attackCenterY) {
        Vector collisionVector = new Vector();
        double vectorX = snailCenterX - attackCenterX;
        double vectorY = snailCenterY - attackCenterY;
        collisionVector.setVectorXY(vectorX, vectorY);
        return collisionVector;
    }

    public Vector exploseMoveVector(Vector snail, Vector attack, double attackPower, Vector collisionVector) {

        snail.add(collisionVector);
        Vector exploseMoveVector = new Vector(collisionVector.normalize().vx() * attackPower, collisionVector.normalize().vy() * attackPower);
        return exploseMoveVector;
        /* Vector snailVector = new Vector(this.snails.get(j).getSpeedX(), this.snails.get(j).getSpeedY());//若蝸牛有Vector直接帶進參數就ㄏㄠ
        setCollisionVector()和exploseMoveVector()用完，要把exploseMoveVector加回去snails.get(j).setSpeedXY();
        並判斷你的skills/props在多少距離會消失
        ex:
        this.snails.get(j).setSpeedXY(collisionVector.normalize().vx() * 20, collisionVector.normalize().vy() * 20);
        if (this.sceneEmission.get(i).distance() > 500) {
                sceneEmission.remove(i--);
            }*/
    }
    @Override
    public CommandSolver.MouseCommandListener mouseListener() {
        return (MouseEvent e, CommandSolver.MouseState state, long trigTime) -> {
            for (int i = 0; i < propsList.size(); i++) {
                propsList.get(i).mouseTrig(cam.collider(),e, state, 0);
            }
//            if (this.propsList.size() == 0) {
//                for (int i = 0; i < this.snails.size(); i++) {
//                    this.snails.get(i).mouseTrig(e, state, trigTime);
//                }
//            }
        };
    }

    @Override
    public CommandSolver.KeyListener keyListener() {
        return new CommandSolver.KeyListener() {
            @Override
            public void keyPressed(int commandCode, long trigTime) {
                if (commandCode < 7){
                    snails.get(0).keyPressed(commandCode, trigTime);
                }
                else {
                    commandCode = commandCode % 8;
                    snails.get(1).keyPressed(commandCode, trigTime);
                }
            }

            @Override
            public void keyReleased(int commandCode, long trigTime) {
                if (commandCode < 8){
                    snails.get(0).keyReleased(commandCode, trigTime);
                }
                else {
                    commandCode = commandCode % 8;
                    snails.get(1).keyReleased(commandCode, trigTime);
                }
            }

            @Override
            public void keyTyped(char c, long trigTime) {
                if(c == KeyEvent.VK_SPACE){
                    snails.get(0).keyTyped(c, trigTime);
                    if(propsList.isEmpty()){
                        sceneEmission.add(snails.get(0).throwSkill());
                    }
                }
                if(c == KeyEvent.VK_ENTER){
                    snails.get(1).keyTyped(c, trigTime);
                    if(propsList.isEmpty()){
                        sceneEmission.add(snails.get(1).throwSkill());
                    }
                }
            }
        };
    }
}
