package snailstrike.scene;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import menu.BackgroundColor;
import menu.BackgroundImage;
import menu.BackgroundType;
import menu.Button;
import menu.Label;
import menu.Style;
import menu.impl.MouseTriggerImpl;
import snailstrike.controllers.AudioResourceController;
import snailstrike.controllers.SceneController;
import static snailstrike.scene.MainScene.addButton;
import snailstrike.utils.CommandSolver;
import snailstrike.utils.Global;
import snailstrike.utils.Path;

public class HintScene extends Scene {

    private Image img;
    private final int spaceY = (Global.SCREEN_Y - 300 - 208) / 3;
    private final int buttonWidth = 150;
    private final int buttonHeight = 75;
    private Image introductionImg;
    private Image instructionsImg;
    private Image itemIntroductionImg;
    private Label introduction;
    private Label instructions;
    private Label itemIntroduction;
    private Button returnToHome;
    private Image present;

    public HintScene() {
        img = SceneController.instance().irc().tryGetImage(new Path().img().backgrounds().HintScene());
        introduction = addLabel(120, 104, SceneController.instance().irc().tryGetImage(
                new Path().img().objs().introduction()));
        instructions = addLabel(120, 104 + buttonHeight + spaceY, SceneController.instance().irc().tryGetImage(
                new Path().img().objs().instruction()));
        itemIntroduction = addLabel(120, 104 + (buttonHeight + spaceY) * 2, SceneController.instance().irc().tryGetImage(
                new Path().img().objs().itemIntroduction()));
        returnToHome = addButton(120, 104 + (buttonHeight + spaceY) * 3, SceneController.instance().irc().tryGetImage(
                new Path().img().objs().back()));
        introductionImg = SceneController.instance().irc().tryGetImage(new Path().img().backgrounds().Introduction());
        instructionsImg = SceneController.instance().irc().tryGetImage(new Path().img().backgrounds().Intstruction());
        itemIntroductionImg = SceneController.instance().irc().tryGetImage(new Path().img().backgrounds().ItemsIntroduction());

    }

    @Override
    public void sceneBegin() {

        returnToHome.setClickedActionPerformed((int x, int y) -> {
            SceneController.instance().change(new MainScene());
            AudioResourceController.getInstance().play(new Path().sounds().obj().buttonClick());
        });
        present = introductionImg;
        introduction.isFocus();

    }

    @Override
    public void sceneEnd() {
        img = null;

        introductionImg = null;
        instructionsImg = null;
        itemIntroductionImg = null;
        introduction = null;
        instructions = null;
        itemIntroduction = null;
        returnToHome = null;
        present = null;

    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(img, 0, 0, Global.SCREEN_X, Global.SCREEN_Y, 0, 0, img.getWidth(null), img.getHeight(null), null);
        introduction.paint(g);
        instructions.paint(g);
        itemIntroduction.paint(g);
        returnToHome.paint(g);
        g.drawImage(present, 345, 104, 864, 513, null);
    }

    @Override
    public void update() {
        introduction.setClickedActionPerformed((x, y) -> {
            present = introductionImg;
            AudioResourceController.getInstance().play(new Path().sounds().obj().buttonClick());
        });

        instructions.setClickedActionPerformed((x, y) -> {
            present = instructionsImg;
            AudioResourceController.getInstance().play(new Path().sounds().obj().buttonClick());
        });
        itemIntroduction.setClickedActionPerformed((x, y) -> {
            present = itemIntroductionImg;
            AudioResourceController.getInstance().play(new Path().sounds().obj().buttonClick());
        });

    }

    public Label addLabel(int x, int y, Image img) {
        Style normal = new Style.StyleRect(150, 75, new BackgroundImage(img))
                .setHaveBorder(true)
                .setBorderColor(new Color(112, 66, 20));
        Style hover = new Style.StyleRect(150, 75, new BackgroundImage(img))
                .setHaveBorder(true)
                .setBorderColor(new Color(230, 153, 102));
        Style focus = new Style.StyleRect(150, 75, new BackgroundImage(img))
                .setHaveBorder(true)
                .setBorderColor(new Color(161, 107, 71));
        Label label = new Label(x, y, normal);
        label.setStyleHover(hover);
        label.setStyleFocus(focus);
        return label;
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
    public CommandSolver.KeyListener keyListener() {
        return null;
    }

    @Override
    public CommandSolver.MouseCommandListener mouseListener() {
        return (MouseEvent e, CommandSolver.MouseState state, long trigTime) -> {
            MouseTriggerImpl.mouseTrig(returnToHome, e, state);
            MouseTriggerImpl.mouseTrig(this.instructions, e, state);
            MouseTriggerImpl.mouseTrig(this.introduction, e, state);
            MouseTriggerImpl.mouseTrig(this.itemIntroduction, e, state);
        };
    }

}
