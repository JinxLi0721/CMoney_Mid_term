/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package snailstrike.animators;

import java.awt.Graphics;
import java.awt.Image;
import snailstrike.utils.Delay;
import snailstrike.utils.Global;

/**
 *
 * @author USER
 */
public class ItemsAnimator {

    private final int BOMB_BOOM_X;//爆炸圖的Xj寬度
    private final int BOMB_BOOM_Y;//爆炸圖的y高度
    private final int BOMB_NORMAL_X;//道具正常狀態的x寬度
    private final int BOMB_NORMAL_Y;//道具正常狀態的y高度
    private Delay delayBoom;//爆炸圖動畫速度
    private Image itemNormal;//道具正常圖
    private Image itemBoom;//道具爆炸圖
    private Image itemExist;//道具尚未被吃到的箱子統一圖
    private Delay delayKind;//正常圖動畫速度
    private int bombCount;//正常圖update
    private int bombBoomCount;//爆炸圖update
    private Delay delayRemove;//爆炸後移除前的延遲時間

    private State state;
    private Kind kind;
    private Boom boom;

    public enum State {//道具狀態
        BOX_EXIST,
        NORMAL,
        BOOM,
        REMOVE;

    }

    public enum Kind {//道具種類圖
        BOMB(new int[]{0, 1, 2}, 10),
        TRAP(new int[]{0}, 15),
        EXPLODE_AT_ONCE(new int[]{0}, 10),
        TRACK(new int[]{0}, 10),
        DEFEND(new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8}, 10),
        METEORITE(new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15}, 6),//做慢慢變小的隕石
        METEORITE_ROLL(new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11}, 6),//找在旋轉滾的火球
        BUMP(new int[]{0, 1, 2, 3, 2, 1}, 10),//讓蝸牛滑動的
        BLACK_HOLE(new int[]{0, 1, 2, 3, 4}, 7);
        private int[] arr;
        private int speed;

        Kind(int[] arr, int speed) {
            this.arr = arr;
            this.speed = speed;
        }
    }

    public enum Boom {//道具爆炸圖
        BOMB_BOOM(new int[]{0, 1, 2, 3, 4, 4, 4, 5,}, 8),
        TRAP_BOOM(new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8}, 8),
        EXPLODE_AT_ONCE_BOOM(new int[]{0, 1, 2, 3, 4}, 8),
        TRACK_BOOM(new int[]{0, 1, 2, 3, 4}, 8),
        DEFEND_BOOM(new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8}, 8),
        METEORITE_BOOM(new int[]{0, 1, 2, 3, 4}, 6),//
        METEORITE_ROLL_BOOM(new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29}, 10),
        BUMP_BOOM(new int[]{0, 1, 2}, 10),
        BLACK_HOLE_BOOM(new int[]{0, 1, 2, 3, 4}, 7);
        private int[] arr;
        private int speed;

        Boom(int[] arr, int speed) {
            this.arr = arr;
            this.speed = speed;
        }
    }

    public ItemsAnimator(Image itemExist, Image itemNormal, Image itemBoom, Kind kind, Boom boom,
            int BOMB_NORMAL_X, int BOMB_NORMAL_Y,
            int BOMB_BOOM_X, int BOMB_BOOM_Y) {
        this.BOMB_NORMAL_X = BOMB_NORMAL_X;
        this.BOMB_BOOM_Y = BOMB_BOOM_Y;
        this.BOMB_BOOM_X = BOMB_BOOM_X;
        this.BOMB_NORMAL_Y = BOMB_NORMAL_Y;
        this.kind = kind;
        this.itemBoom = itemBoom;
        this.itemNormal = itemNormal;
        this.itemExist = itemExist;
        delayBoom = new Delay(boom.speed);
        delayBoom.loop();
        delayKind = new Delay(kind.speed);
        delayKind.loop();
        bombCount = 0;
        bombBoomCount = 0;
        setBoom(boom);
        state = state.BOX_EXIST;
        delayRemove = new Delay(10);
    }
    //無boom圖

    public ItemsAnimator(Image itemExist, Image itemNormal, Kind kind, Boom boom,
            int BOMB_NORMAL_X, int BOMB_NORMAL_Y,
            int BOMB_BOOM_X, int BOMB_BOOM_Y) {
        this.BOMB_NORMAL_X = BOMB_NORMAL_X;
        this.BOMB_BOOM_Y = BOMB_BOOM_Y;
        this.BOMB_BOOM_X = BOMB_BOOM_X;
        this.BOMB_NORMAL_Y = BOMB_NORMAL_Y;
        this.kind = kind;
        this.itemNormal = itemNormal;
        this.itemExist = itemExist;
        delayBoom = new Delay(boom.speed);
        delayBoom.loop();
        delayKind = new Delay(kind.speed);
        delayKind.loop();
        this.delayKind.setLimit(kind.speed);
        bombCount = 0;
        bombBoomCount = 0;
        setBoom(boom);
        state = state.BOX_EXIST;
        delayRemove = new Delay(30);
    }

    public void setBoom(Boom boom) {
        this.boom = boom;
        this.delayBoom.setLimit(boom.speed);
    }

    public void setRemoveDelay(int t) {
        this.delayRemove.setLimit(t);
    }

    public void setState(State state) {
        this.state = state;
    }

    public State getState() {
        return this.state;
    }

    public Kind getKind() {
        return this.kind;
    }

    public void paint(Global.Direction dir, int left, int top, int right, int bottom, Graphics g) {

    }

    public void paintBox(int left, int top, int right, int bottom, Graphics g) {
        if (state == state.BOX_EXIST) {
            g.drawImage(itemExist, left, top, right, bottom, 0, 0, 50, 38, null);
//            System.out.println("BoxExist");
        }
    }

    public void paint(int left, int top, int right, int bottom, Graphics g) {

        if (state == state.NORMAL) {
            int x1 = BOMB_NORMAL_X * kind.arr[bombCount];
            int y1 = 0;
            int x2 = BOMB_NORMAL_X * kind.arr[bombCount] + BOMB_NORMAL_X;
            int y2 = BOMB_NORMAL_Y;
            g.drawImage(itemNormal, left, top, right, bottom, x1, y1, x2, y2, null);
            delayKind.count();

        }

        if (state == state.BOOM) {
            int x1 = BOMB_BOOM_X * boom.arr[bombBoomCount];
            int y1 = 0;
            int x2 = BOMB_BOOM_X * boom.arr[bombBoomCount] + BOMB_BOOM_X;
            int y2 = BOMB_BOOM_Y;
            g.drawImage(itemBoom, left - 50, top - 50, right + 50, bottom + 50, x1, y1, x2, y2, null);
            delayBoom.count();
        }

    }

    public void update() {
        if (delayBoom.count()) {
            bombBoomCount = ++bombBoomCount % boom.arr.length;

        }
        if (delayKind.count()) {
            bombCount = ++bombCount % kind.arr.length;
        }
        if (state == State.BOOM) {
            delayRemove.play();
        }
        if (delayRemove.count()) {
            setState(state.REMOVE);
        }
    }

}
