/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package menu;

 //設定背景為圖片
import java.awt.AlphaComposite;
import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;


    public class BackgroundImage extends BackgroundType {

        private Image img;

        public BackgroundImage(Image image) {
            super();
            this.img = image;
        }

        @Override
        public Image getBackground() {
            return this.img;
        }

        @Override
        public void paintBackground(Graphics g, boolean isOval, boolean isFill, int x, int y, int width, int height) {
            if (imageSrcIn == null) {
                imageSrcIn = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
                ovalSrcIn = imageSrcIn.createGraphics();
            }
            if (isOval) {
                Composite c = ovalSrcIn.getComposite();
                ovalSrcIn.fillOval(0, 0, width, height);
                ovalSrcIn.setComposite(AlphaComposite.SrcIn);
                ovalSrcIn.drawImage(this.img, 0, 0, width, height, null);
                g.drawImage(imageSrcIn, x, y, null);
                ovalSrcIn.setComposite(c);
            } else {
                int arc = 0;
                if (width > height) {
                    arc = height / 3;
                } else {
                    arc = width / 3;
                }
                if (imageSrcIn == null) {
                    imageSrcIn = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
                    ovalSrcIn = imageSrcIn.createGraphics();
                }
                Composite c = ovalSrcIn.getComposite();
                ovalSrcIn.fillRoundRect(0, 0, width, height, arc, arc);
                ovalSrcIn.setComposite(AlphaComposite.SrcIn);
                ovalSrcIn.drawImage(this.img, 0, 0, width, height, null);
                g.drawImage(imageSrcIn, x, y, imageSrcIn.getWidth(), imageSrcIn.getHeight(), null);
                ovalSrcIn.setComposite(c);
            }
        }
    }
