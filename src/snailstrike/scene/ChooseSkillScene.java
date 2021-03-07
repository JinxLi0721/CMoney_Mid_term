/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package snailstrike.scene;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import menu.BackgroundImage;
import menu.Button;
import menu.Label;
import menu.Style;
import menu.impl.MouseTriggerImpl;
import snailstrike.controllers.AudioResourceController;
import snailstrike.controllers.SceneController;
import snailstrike.gameobj.SkillDisplay;
import snailstrike.gameobj.Snail.SkillChoose;
import snailstrike.gameobj.Snail.SkillDis;
import snailstrike.utils.CommandSolver;
import snailstrike.utils.Delay;
import snailstrike.utils.Global;
import snailstrike.utils.Path;

/**
 *
 * @author Kylie
 */
public class ChooseSkillScene extends Scene {

    private Image img;
    private final String name;
    private final String IP;
    private ArrayList<SkillDisplay> skills;
    private ArrayList<SkillDis> skillChosen;
    private Button confirm;
    private Button back;
    private Color stringColor;
    private Color StringBorder;
    private Delay delayString;

    public ChooseSkillScene(String name, String IP) {
        this.name = name;
        this.IP = IP;
    }

    @Override
    public void sceneBegin() {
        img = SceneController.instance().irc().tryGetImage(new Path().img().backgrounds().ChooseSkillScene());
        skills = new ArrayList<>();
        int xSpacing = (Global.SCREEN_X - 200 * SkillChoose.values().length / 2) / (SkillChoose.values().length / 2 + 1);
        int ySpacing = (Global.SCREEN_Y - 250 * SkillChoose.values().length / 4 - 150) / (SkillChoose.values().length / 4 + 1);
        for (int i = 0; i < SkillChoose.values().length; i++) {
            Label add = skillDisplay(xSpacing + i % 4 * (200 + xSpacing),
                    50 + ySpacing + i / 4 * (250 + ySpacing), SkillChoose.values()[i]);
            skills.add(new SkillDisplay(add, SkillChoose.values()[i].getKind()));
        }
        skillChosen = new ArrayList<>();
        confirm = MainScene.addButton(Global.SCREEN_X / 2 + 200, Global.SCREEN_Y - 75, SceneController.instance().irc().tryGetImage(
                new Path().img().objs().confirm()));
        back = MainScene.addButton(Global.SCREEN_X / 2 - 200, Global.SCREEN_Y - 75, SceneController.instance().irc().tryGetImage(
                new Path().img().objs().back()));
        confirm.setClickedActionPerformed((int x, int y)
                -> {
            AudioResourceController.getInstance().play(new Path().sounds().obj().buttonClick());
            if (skillChosen.size() == 3) {
                SceneController.instance().change(new MultiplayerScene(name, IP, skillChosen));
            }
        });

        this.stringColor = Color.BLACK;
        this.StringBorder = Color.WHITE;
        this.delayString = new Delay(20);
        back.setClickedActionPerformed((int x, int y) -> {
            AudioResourceController.getInstance().play(new Path().sounds().obj().buttonClick());
            SceneController.instance().change(new InputScene());
        });

    }

    @Override
    public void sceneEnd() {
        skills = null;
        skillChosen = null;
        confirm = null;
    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(img, 0, 0, Global.SCREEN_X, Global.SCREEN_Y, 0, 0, img.getWidth(null), img.getHeight(null), null);
        g.setColor(this.StringBorder);
        g.fillRoundRect(Global.SCREEN_X / 2 - 165, 20, 330, 40, 20, 20);
        g.setColor(this.stringColor);
        g.setFont(Global.FONT_ORIGIN);
        g.drawString("Choose 3 skills to use", 475, 50);
        for (int i = 0; i < this.skills.size(); i++) {
            skills.get(i).paint(g);
        }
        confirm.paint(g);
        back.paint(g);
    }

    @Override
    public void update() {
        for (int i = 0; i < this.skills.size(); i++) {
            skills.get(i).update();
        }
        confirm.update();
        back.update();
        if (delayString.count()) {
            this.stringColor = Color.BLACK;
            this.StringBorder = Color.WHITE;
            delayString.pause();
        }
    }

    @Override
    public CommandSolver.MouseCommandListener mouseListener() {
        return (MouseEvent e, CommandSolver.MouseState state, long trigTime) -> {
            if (e == null) {
                return;
            }
            if (state == CommandSolver.MouseState.MOVED) {
                for (int i = 0; i < this.skills.size(); i++) {
                    if (rectOverlap(skills.get(i).getskillDis(), e.getX(), e.getY())) {
                        skills.get(i).getskillDis().isHover();
                    } else {
                        skills.get(i).getskillDis().unHover();
                    }
                }
            }
            if (state == CommandSolver.MouseState.PRESSED) {
                for (int i = 0; i < this.skills.size(); i++) {
                    if (rectOverlap(skills.get(i).getskillDis(), e.getX(), e.getY())) {
                        if (skills.get(i).getskillDis().getIsFocus()) {
                            skillChosen.remove(chooseSkillDis(skills.get(i).getSkillString()));
                            skills.get(i).getskillDis().unFocus();
                        } else {
                            if (skillChosen.size() < 3) {
                                skillChosen.add(chooseSkillDis(skills.get(i).getSkillString()));
                                skills.get(i).getskillDis().isFocus();
                            }
                        }
                    }
                }
                if (skillChosen.size() < 3) {
                    if (rectOverlap(confirm, e.getX(), e.getY())) {
//                        this.stringColor = Color.YELLOW;
                        this.StringBorder = Color.YELLOW;
                        delayString.play();
                    }
                }
            }
            if (skillChosen.size() >= 3) {
                MouseTriggerImpl.mouseTrig(confirm, e, state);
            }

            MouseTriggerImpl.mouseTrig(back, e, state);
        };
    }

    @Override
    public CommandSolver.KeyListener keyListener() {
        return null;
    }

    public Label skillDisplay(int x, int y, SkillChoose skillChoose) {
        Style normal = new Style.StyleRect(200, 250, false,
                new BackgroundImage(SceneController.instance().irc().tryGetImage(skillChoose.getImagePathNormal())))
                .setHaveBorder(false)
                .setBorderColor(Color.GRAY)
                .setBorderThickness(3);
        Style hover = new Style.StyleRect(200, 250, false,
                new BackgroundImage(SceneController.instance().irc().tryGetImage(skillChoose.getImagePathHover())))
                .setHaveBorder(false)
                .setBorderColor(Color.DARK_GRAY)
                .setBorderThickness(3);
        Style focus = new Style.StyleRect(200, 250, false,
                new BackgroundImage(SceneController.instance().irc().tryGetImage(skillChoose.getImagePathFocus())))
                .setHaveBorder(false)
                .setBorderColor(Color.BLACK)
                .setBorderThickness(3);
        Label label = new Label(x, y, normal);
        label.setStyleHover(hover);
        label.setStyleFocus(focus);
        System.out.println(x + ", " + y);
        return label;
    }

//    private static boolean ovalOverlap(Label obj, int eX, int eY) {
//        int r = (int) (Math.sqrt(Math.pow(obj.width() / 2, 2) - Math.pow(obj.height() / 2, 2)));
//        int r1X = obj.getX() + obj.width() / 2 - r;
//        int r2X = obj.getX() + obj.width() / 2 + r;
//        int rY = obj.getY() + obj.height() / 2;
//        int threePointDistance = (int) (Math.sqrt(Math.pow(r1X - eX, 2) + Math.pow(rY - eY, 2)) + Math.sqrt(Math.pow(r2X - eX, 2) + Math.pow(rY - eY, 2)));
//        return threePointDistance <= obj.width();
//    }
    private static boolean rectOverlap(Label obj, int eX, int eY) {
        return eX < obj.right() && eX > obj.left() && eY > obj.top() && eY < obj.bottom();
    }

    public SkillDis chooseSkillDis(String string) {
        switch (string) {
            case "AttackForce":
                return SkillDis.ATTACKFORCE;
            case "AttackRange":
                return SkillDis.ATTACKRANGE;
            case "AttackSplit":
                return SkillDis.ATTACKSPLIT;
            case "Boomerang":
                return SkillDis.BOOMERANG;
            case "EnrichBlood":
                return SkillDis.ENRICHBLOOD;
            case "Teleportation":
                return SkillDis.TELEPORTATION;
            case "Invisible":
                return SkillDis.INVISIBLE;
            case "Velocity":
                return SkillDis.VELOCITY;
//            case "Track":
//                return SkillDis.TRACK;
            default:
                return null;
        }
    }

}
