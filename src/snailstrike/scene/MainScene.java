package snailstrike.scene;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import menu.BackgroundImage;
import menu.Button;
import menu.Style;
import menu.Style.StyleRect;
import menu.impl.MouseTriggerImpl;
import snailstrike.controllers.AudioResourceController;
import snailstrike.controllers.SceneController;
import snailstrike.utils.CommandSolver;
import snailstrike.utils.Delay;
import snailstrike.utils.Global;
import snailstrike.utils.Path;

public class MainScene extends Scene {

    private Image img;
    private Image cloud;
    private Image title;
    private Image snailstop;
    private Button clickToGameStart;
    private Button clickToGameHint;
    private int[] animation;
    private int[] cloudAni;
    private int[] snailStopAni;
    private Delay delay;
    private Delay cDelay;
    private Delay snailDelay;
    private Delay snailStopDelay;
    private int count;
    private int cCount;
    private final int width = 553;
    private final int height = 311;
    private final int cloudWidth = 494;
    private final int cloudHeight = 84;
    private final int snailHeight = 494;
    private final int snailWidth = 826;
    private final int snailStopHeight = 367;
    private final int snailStopWidth = 275;
    private int cloudPositionX;
    private int cloudPositionY;

    @Override
    public void sceneBegin() {
        AudioResourceController.getInstance().play(new Path().sounds().StartScene());
        img = SceneController.instance().irc().tryGetImage(new Path().img().backgrounds().startScreen());
        clickToGameStart = addButton(Global.SCREEN_X / 2, Global.SCREEN_Y / 2 + 100, SceneController.instance().irc().tryGetImage(
                new Path().img().objs().start()));
        clickToGameHint = addButton(Global.SCREEN_X / 2, Global.SCREEN_Y / 2 + 200, SceneController.instance().irc().tryGetImage(
                new Path().img().objs().seeMe()));
        title = SceneController.instance().irc().tryGetImage(new Path().img().backgrounds().title());
        snailstop = SceneController.instance().irc().tryGetImage(new Path().img().backgrounds().snailfacerightall());
        cloud = SceneController.instance().irc().tryGetImage(new Path().img().backgrounds().startScreenCloud());
        clickToGameHint.setClickedActionPerformed((int x, int y) -> {
            SceneController.instance().change(new HintScene());
             AudioResourceController.getInstance().play(new Path().sounds().obj().buttonClick());
                });
        clickToGameStart.setClickedActionPerformed((int x, int y) ->{
            SceneController.instance().change(new InputScene());
             AudioResourceController.getInstance().play(new Path().sounds().obj().buttonClick());
                });
        animation = new int[4];
        for (int i = 0; i < animation.length; i++) {
            animation[i] = i;
        }
        cloudAni = new int[]{0, 1};
        snailStopAni = new int[]{0, 1};
        delay = new Delay(10);
        cDelay = new Delay(30);
        snailDelay = new Delay(40);
        snailStopDelay = new Delay(25);
        delay.loop();
        snailDelay.loop();
        snailStopDelay.loop();
        cDelay.loop();
        count = 0;
        cCount = 0;
        cloudPositionX = Global.WINDOW_WIDTH + cloudWidth;
        cloudPositionY = 84;
    }

    @Override
    public void sceneEnd() {
        clickToGameStart = null;
        clickToGameHint = null;
        animation = null;
        img = null;
        cloud = null;

    }

    @Override
    public void paint(Graphics g) {
        g.setColor(Color.gray);
//        g.drawImage(img, 0, 0, Global.SCREEN_X, Global.SCREEN_Y, 0, 0, img.getWidth(null), img.getHeight(null), null);
        int x1 = width * animation[count];
        int y1 = 0;
        int x2 = width * animation[count] + width;
        int y2 = height;
        g.drawImage(img, 0, 0, Global.WINDOW_WIDTH, Global.WINDOW_HEIGHT, x1, y1, x2, y2, null);
        int cx1 = cloudWidth * cloudAni[cCount];
        int cy1 = 0;
        int cx2 = cloudWidth * cloudAni[cCount] + cloudWidth;
        int cy2 = cloudHeight;
        g.drawImage(cloud, cloudPositionX, cloudPositionY,
                cloudPositionX - cloudWidth, cloudPositionY + cloudHeight, cx1, cy1, cx2, cy2, null);
        g.drawImage(title, 180, 100, 908, 252, null);
        int stx1 = snailStopWidth * snailStopAni[cCount];
        int sty1 = 0;
        int stx2 = snailStopWidth * snailStopAni[cCount] + snailStopWidth;
        int sty2 = snailStopHeight;
        g.drawImage(snailstop, Global.WINDOW_WIDTH / 3 + snailWidth / 2,
                160 + snailHeight / 2, Global.WINDOW_WIDTH / 3 + snailWidth / 2 + (snailStopWidth * 2) / 3,
                160 + snailHeight / 2 + (snailStopHeight * 2) / 3, stx1, sty1, stx2, sty2, null);
        this.clickToGameStart.paint(g);
        this.clickToGameHint.paint(g);

    }

    @Override
    public void update() {
        if (delay.count()) {
            count = ++count % animation.length;
        }
        if (cDelay.count()) {
            cCount = ++cCount % cloudAni.length;

        }
        cloudPositionX -= 2;
        if (cloudPositionX <= -cloudWidth) {
            cloudPositionX = Global.WINDOW_WIDTH + cloudWidth;
        }
    }

    public static Button addButton(int x, int y, Image img) {
        Style normal = new StyleRect(150, 75, new BackgroundImage(img))
                .setHaveBorder(true)
                .setBorderColor(new Color(112, 66, 20));
        Style hover = new StyleRect(150, 75, new BackgroundImage(img))
                .setHaveBorder(true)
                .setBorderColor(new Color(230, 153, 102));
        Style focus = new StyleRect(150, 75, new BackgroundImage(img))
                .setHaveBorder(true)
                .setBorderColor(new Color(161, 107, 71));
        Button button = new Button(x - 75, y - 38, normal);
        button.setStyleHover(hover);
        button.setStyleFocus(focus);
        return button;
    }

    @Override
    public CommandSolver.MouseCommandListener mouseListener() {
        return (MouseEvent e, CommandSolver.MouseState state, long trigTime) -> {
            MouseTriggerImpl.mouseTrig(clickToGameStart, e, state);
            MouseTriggerImpl.mouseTrig(clickToGameHint, e, state);
        };
    }

    @Override
    public CommandSolver.KeyListener keyListener() {
        return null;
    }
}
