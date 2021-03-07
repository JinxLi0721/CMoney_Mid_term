package snailstrike.scene;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import menu.BackgroundColor;
import menu.BackgroundType;
import menu.Button;
import menu.EditText;
import menu.Label;
import menu.Style;
import menu.impl.MouseTriggerImpl;
import snailstrike.controllers.AudioResourceController;
import snailstrike.controllers.SceneController;
import snailstrike.utils.CommandSolver;
import snailstrike.utils.Global;
import snailstrike.utils.Path;

public class InputScene extends Scene {

    private Image img;
    private ArrayList<Label> labels;
    private EditText inputName;
    private EditText inputIP;
    private Button confirm;
    private Button back;
    private int currentFocus;
    private Font fontString;

    @Override
    public void sceneBegin() {
        img = SceneController.instance().irc().tryGetImage(new Path().img().backgrounds().InputScene());
        inputName = this.addEditText(Global.SCREEN_X / 2, Global.SCREEN_Y / 3 + 50, "Please enter your name");
        inputName.setEditLimit(12);
        inputIP = this.addEditText(Global.SCREEN_X / 2, Global.SCREEN_Y / 3 + 150, "Please enter IP");
        confirm = MainScene.addButton(Global.SCREEN_X / 2 + 200, Global.SCREEN_Y / 2 + 200, SceneController.instance().irc().tryGetImage(
                new Path().img().objs().confirm()));
        back = MainScene.addButton(Global.SCREEN_X / 2 - 200, Global.SCREEN_Y / 2 + 200, SceneController.instance().irc().tryGetImage(
                new Path().img().objs().back()));
        confirm.setClickedActionPerformed((int x, int y)
                -> {
            AudioResourceController.getInstance().play(new Path().sounds().obj().buttonClick());
            SceneController.instance().change(new ChooseSkillScene(inputName.getEditText(), inputIP.getEditText()));
        });
        back.setClickedActionPerformed((int x, int y)
                -> {
            AudioResourceController.getInstance().play(new Path().sounds().obj().buttonClick());
            SceneController.instance().change(new MainScene());
        });
        labels = new ArrayList<>();
        labels.add(inputName);
        labels.add(inputIP);
//        labels.add(back);
//        labels.add(confirm);
        currentFocus = 0;
        fontString = new Font("", Font.BOLD, 30);
    }

    @Override
    public void sceneEnd() {
        inputName = null;
        inputIP = null;
        confirm = null;
        back = null;
        labels = null;
    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(img, 0, 0, Global.SCREEN_X, Global.SCREEN_Y, 0, 0, img.getWidth(null), img.getHeight(null), null);
        g.setColor(Color.white);
        g.setFont(fontString);
        g.drawString("Name", inputName.getX() - 100, inputName.getY() + 40);
        g.drawString("IP", inputIP.getX() - 50, inputIP.getY() + 40);
        this.inputName.paint(g);
        this.inputIP.paint(g);
        this.confirm.paint(g);
        this.back.paint(g);
    }

    @Override
    public void update() {

    }

    public EditText addEditText(int x, int y, String str) {
        Style normal = new Style.StyleRect(250, 70, new BackgroundColor(Color.DARK_GRAY))
                .setTextColor(Color.white)
                .setTextFont(new Font("", Font.BOLD, 20));
        Style hover = new Style.StyleRect(250, 70, new BackgroundColor(Color.gray))
                .setTextFont(new Font("", Font.BOLD, 20));;
        Style focus = new Style.StyleRect(250, 70, new BackgroundColor(Color.WHITE))
                .setTextColor(Color.black)
                .setTextFont(new Font("", Font.BOLD, 20));
        EditText input = new EditText(x - 125, y - 35, str, normal);
        input.setStyleHover(hover);
        input.setStyleFocus(focus);
        return input;
    }

    @Override
    public CommandSolver.MouseCommandListener mouseListener() {
        return (MouseEvent e, CommandSolver.MouseState state, long trigTime) -> {
            MouseTriggerImpl.mouseTrig(inputName, e, state);
            MouseTriggerImpl.mouseTrig(inputIP, e, state);
            MouseTriggerImpl.mouseTrig(confirm, e, state);
            MouseTriggerImpl.mouseTrig(back, e, state);

        };
    }

    @Override
    public CommandSolver.KeyListener keyListener() {
        return new CommandSolver.KeyListener() {
            @Override
            public void keyPressed(int commandCode, long trigTime) {

            }

            @Override
            public void keyReleased(int commandCode, long trigTime) {
            }

            @Override
            public void keyTyped(char c, long trigTime) {
                if (c == KeyEvent.VK_ENTER) {
                    if (labels.get(currentFocus).getIsFocus()) {
                        labels.get(currentFocus).unHover();
                        labels.get(currentFocus).unFocus();
                        currentFocus = currentFocus + 1 >= labels.size() ? currentFocus : currentFocus + 1;
                    }
                    if (labels.get(currentFocus) instanceof EditText) {
                        if (labels.get(currentFocus).getIsFocus()) {
                            labels.get(currentFocus).unFocus();
                            labels.get(currentFocus).unHover();
                        } else {
                            labels.get(currentFocus).isFocus();
                        }
                    } else {
                        if (labels.get(currentFocus).getIsHover()) {
                            labels.get(currentFocus).clickedActionPerformed();
                        } else {
                            labels.get(currentFocus).isHover();
                        }
                    }
                }
                inputName.keyTyped(c);
                inputIP.keyTyped(c);
            }
        };
    }

}
