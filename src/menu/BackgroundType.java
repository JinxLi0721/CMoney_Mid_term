package menu;

import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class BackgroundType {

    public BackgroundType() {
//        imageSrcIn = new BufferedImage(Global.SCREEN_X, Global.SCREEN_Y, BufferedImage.TYPE_INT_ARGB);
    }

    public abstract <T> T getBackground();
    protected Graphics2D ovalSrcIn;
    protected BufferedImage imageSrcIn;

    public abstract void paintBackground(Graphics g, boolean isOval, boolean isFill, int x, int y, int width, int height);

}
