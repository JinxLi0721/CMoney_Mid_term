/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package menu;

//設定背景為顏色
import java.awt.Color;
import java.awt.Graphics;

public class BackgroundColor extends BackgroundType {

    private Color color;

    public BackgroundColor(Color color) {
        this.color = color;
    }

    @Override
    public Color getBackground() {
        return this.color;
    }

    @Override
    public void paintBackground(Graphics g, boolean isOval, boolean isFill, int x, int y, int width, int height) {
        g.setColor(color);
        if (isOval) {
            if (isFill) {
                g.fillOval(x, y, width, height);
            } else {
                g.drawOval(x, y, width, height);
            }
        } else {
            int arc = 0;
            if (width > height) {
                arc = height / 3;
            } else {
                arc = width / 3;
            }
            if (isFill) {
                g.fillRoundRect(x, y, width, height, arc, arc);
//                    g.fillRect(x, y, width, height);
            } else {
                g.drawRoundRect(x, y, width, height, arc, arc);
//                    g.drawRect(x, y, width, height);
            }
        }
    }
}
