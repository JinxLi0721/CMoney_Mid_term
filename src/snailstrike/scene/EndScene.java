package snailstrike.scene;

import java.awt.Graphics;
import java.awt.Image;
import menu.Button;
import snailstrike.controllers.SceneController;
import snailstrike.utils.Global;
import snailstrike.utils.Path;
import menu.Style;
import java.awt.Color;
import java.awt.Font;
import snailstrike.utils.CommandSolver;
import menu.impl.MouseTriggerImpl;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import menu.BackgroundImage;
import snailstrike.controllers.AudioResourceController;
import snailstrike.gameobj.Snail;

public class EndScene extends Scene {

    private String IP;
    private Image bg;
    private Button clickToPlayAgain;
    private Button clickToMainScene;
    private Button clickToCancel;
    private Snail snailWin;
    private Snail thisClientSnail;
    private ArrayList<Snail> deadSnails;
    private int namePositionX;
    private int namePositionY;
    private int xSpacing;
    private int ySpacing;

    public EndScene(Snail thisClientSnail, Snail snailWin, ArrayList<Snail> deadSnails, String IP) {
        AudioResourceController.getInstance().stop(new Path().sounds().GameStartMusic());
        AudioResourceController.getInstance().play(new Path().sounds().EndScene());
        this.IP = IP;
        this.snailWin = snailWin;
        this.thisClientSnail = thisClientSnail;
        this.deadSnails = deadSnails;
        namePositionX = 275;
        namePositionY = 185;
        xSpacing = 160;
        ySpacing = 45;
        if (deadSnails.size() > 0) {
            Collections.sort(deadSnails, (Snail o1, Snail o2) -> o1.getName().charAt(0) - o2.getName().charAt(0));
        }
    }

    @Override
    public void sceneBegin() {
        bg = SceneController.instance().irc().tryGetImage(new Path().img().backgrounds().EndScreen());
        int buttonXspacing = (Global.SCREEN_X - 450) / 4;
        clickToPlayAgain = addButton(buttonXspacing, Global.SCREEN_Y / 2 + 200, SceneController.instance().irc().tryGetImage(
                new Path().img().objs().playAgain()));
        clickToMainScene = addButton(buttonXspacing + (buttonXspacing + 150) * 1, Global.SCREEN_Y / 2 + 200, SceneController.instance().irc().tryGetImage(
                new Path().img().objs().backToMain()));
        clickToCancel = addButton(buttonXspacing + (buttonXspacing + 150) * 2, Global.SCREEN_Y / 2 + 200, SceneController.instance().irc().tryGetImage(
                new Path().img().objs().exit()));
        clickToMainScene.setClickedActionPerformed((int x, int y) -> {
            SceneController.instance().change(new MainScene());
            AudioResourceController.getInstance().play(new Path().sounds().obj().buttonClick());
        });
        clickToPlayAgain.setClickedActionPerformed((int x, int y) -> {
            SceneController.instance().change(new ChooseSkillScene(thisClientSnail.getName(), this.IP));
            AudioResourceController.getInstance().play(new Path().sounds().StartScene());
        });

        clickToCancel.setClickedActionPerformed((int x, int y) -> {
            this.sceneEnd();
            System.exit(0);
            AudioResourceController.getInstance().play(new Path().sounds().obj().buttonClick());
        });
    }

    @Override
    public void sceneEnd() {
        AudioResourceController.getInstance().stop(new Path().sounds().EndScene());
        clickToPlayAgain = null;
        clickToMainScene = null;
        clickToCancel = null;
        deadSnails = null;
        snailWin = null;
        thisClientSnail = null;
        bg = null;

    }

    public Button addButton(int x, int y, Image img) {
        Style normal = new Style.StyleRect(150, 75, new BackgroundImage(img))
                .setHaveBorder(true)
                .setBorderColor(new Color(112, 66, 20));
        Style hover = new Style.StyleRect(150, 75, new BackgroundImage(img))
                .setHaveBorder(true)
                .setBorderColor(new Color(230, 153, 102));
        Style focus = new Style.StyleRect(150, 75, new BackgroundImage(img))
                .setHaveBorder(true)
                .setBorderColor(new Color(161, 107, 71));
        Button button = new Button(x, y, normal);
        button.setStyleHover(hover);
        button.setStyleFocus(focus);
        return button;
    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(bg, 0, 0, Global.SCREEN_X, Global.SCREEN_Y, null);
        this.clickToMainScene.paint(g);
        this.clickToPlayAgain.paint(g);
        this.clickToCancel.paint(g);

        //Title
        g.setFont(new Font("", Font.BOLD, 22));
        if (snailWin.getSnailHp() > 0) {
            g.setColor(Color.orange);
            g.drawString("WIN", namePositionX + xSpacing * 4, namePositionY);
        }
        g.setColor(Color.white);
        g.drawString("Name", namePositionX, namePositionY - 60);
        g.drawString("TotalHits", namePositionX + xSpacing, namePositionY - 60);
        g.drawString("Survival Time", namePositionX + xSpacing * 2, namePositionY - 60);
        g.drawString("HP Left", namePositionX + xSpacing * 3 + 60, namePositionY - 60);
        //survivedSnail
        g.setFont(new Font("", Font.BOLD, 20));
        g.drawString(snailWin.getName(), namePositionX, namePositionY);
        g.drawString(Integer.toString(snailWin.getTotalHit()), namePositionX + xSpacing, namePositionY);
        g.drawString(Double.toString((int) (snailWin.getSnailSurvivedTime() * 100) / 100.0), namePositionX + xSpacing * 2, namePositionY);
        g.drawString(Integer.toString(snailWin.getSnailHp()), namePositionX + xSpacing * 3 + 60, namePositionY);
        //deadSnail
        int range = deadSnails.size() > 10 ? 10 : deadSnails.size();
        int index = 0;
        for (int i = 0; i < range; i++) {
            if (deadSnails.get(i).getTotalHit() > deadSnails.get(index).getTotalHit()) {
                index = i;
            }
            g.drawString(deadSnails.get(i).getName(), namePositionX, namePositionY + ySpacing * (i + 1));
            if (deadSnails.get(i).getTotalHit() < 0) {
                g.drawString("斷線", namePositionX + xSpacing, namePositionY + ySpacing * (i + 1));
            } else {
                g.drawString((Integer.toString(deadSnails.get(i).getTotalHit())), namePositionX + xSpacing, namePositionY + ySpacing * (i + 1));
            }
            if (deadSnails.get(i).getSnailSurvivedTime() < 0) {
                g.drawString("斷線", namePositionX + xSpacing * 2, namePositionY + ySpacing * (i + 1));
            } else {
                g.drawString(Double.toString((int) (deadSnails.get(i).getSnailSurvivedTime() * 100) / 100.0), namePositionX + xSpacing * 2, namePositionY + ySpacing * (i + 1));
            }
            g.drawString(Integer.toString(deadSnails.get(i).getSnailHp()), namePositionX + xSpacing * 3 + 60, namePositionY + ySpacing * (i + 1));

        }
        g.setColor(Color.red);
        if (deadSnails.size() > 0 && deadSnails.get(index).getTotalHit() > snailWin.getTotalHit()
                && deadSnails.get(index).getTotalHit() > 0) {
            g.drawString("Hits", namePositionX + xSpacing * 4 + 100, namePositionY + ySpacing * (index + 1));
        } else {
            if (snailWin.getTotalHit() > 0) {
                g.drawString("Hits", namePositionX + xSpacing * 4 + 100, namePositionY);
            }
        }
    }

    @Override
    public void update() {

    }

    @Override
    public CommandSolver.MouseCommandListener mouseListener() {
        return (MouseEvent e, CommandSolver.MouseState state, long trigTime) -> {
            MouseTriggerImpl.mouseTrig(clickToMainScene, e, state);
            MouseTriggerImpl.mouseTrig(clickToPlayAgain, e, state);
            MouseTriggerImpl.mouseTrig(clickToCancel, e, state);
//            if (state == state.PRESSED) {
//                System.out.println(e.getPoint());
//            }
        };
    }

    @Override
    public CommandSolver.KeyListener keyListener() {
        return null;
    }

}
