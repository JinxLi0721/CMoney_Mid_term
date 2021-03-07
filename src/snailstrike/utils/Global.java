/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package snailstrike.utils;

import java.awt.Font;

/**
 *
 * @author user1
 */
public class Global {

    public enum Direction {
        UP(3),
        DOWN(0),
        LEFT(1),
        RIGHT(2);

        private int value;

        Direction(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    public static final int LEFT = 1;
    public static final int RIGHT = 2;
    public static final int UP = 3;
    public static final int DOWN = 4;
    public static final int SPACE = 5;
    public static final int A = 6;
    public static final int D = 7;

    public static final boolean IS_DEBUG = false;
    public static final boolean IS_DEBUG_PRINT = false;

    public static void log(String str) {
        if (IS_DEBUG) {
            System.out.println(str);
        }
    }

    public class ClientMessage {

        public final static int SNAIL_CONNECT = 1;
        public final static int CHANGE_CIRCLERANGE = 2;
        public final static int SNAIL_INFORMATION = 3;
        public final static int SNAIL_INVISIBLE = 4;
        public final static int SNAIL_VISIBLE = 5;
        public final static int SNAIL_TOTALHIT = 6; //this
        public final static int ADD_SKILL = 7;
        public final static int SNAIL_SET_VECTOR = 8;
        public final static int ADD_ITEM = 9;
        public final static int CHANGE_ITEM_STATE = 10;//this
        public final static int CHANGE_ITEM_SITUATION = 11; //this
        public final static int ITEM_AFTER_SNAIL = 12; //this
        public final static int SNAIL_SET_STATE = 13;
        public final static int SNAIL_SET_DIR = 14;
        public final static int ITEM_SET_MOUSEEVENT = 15;
        public final static int SNAIL_END = 16;
        public final static int GAMEOVER = 17;
        public final static int SNAIL_EXECPTION = 18;

    }

    // 單位大小
    public static final int UNIT_X = 200;
    public static final int UNIT_Y = 200;
    //範圍大小
    public static int CIRCLE_RANGE_SHRINK = 1000;
    public static int CIRCLE_RANGE = 1000;
    public final static int MAP_RANGE = 3000;
    // 視窗大小
    public static final int WINDOW_WIDTH = 1280;
    public static final int WINDOW_HEIGHT = 760;
    public static final int SCREEN_X = WINDOW_WIDTH - 8 - 8;
    public static final int SCREEN_Y = WINDOW_HEIGHT - 31 - 8;
    // 資料刷新時間
    public static final int UPDATE_TIMES_PER_SEC = 60;// 每秒更新60次遊戲邏輯
    public static final int NANOSECOND_PER_UPDATE = 1000000000 / UPDATE_TIMES_PER_SEC;// 每一次要花費的奈秒數
    // 畫面更新時間
    public static final int FRAME_LIMIT = 60;
    public static final int LIMIT_DELTA_TIME = 1000000000 / FRAME_LIMIT;
    //遊戲統一字形
    public static final Font FONT_ORIGIN = new Font("", Font.BOLD, 30);
    public static final Font FONT_DISPLAY = new Font("", Font.BOLD, 20);

    public static int random(int min, int max) {
        return (int) (Math.random() * (max - min + 1) + min);
    }

    public static boolean random(int rate) {
        return random(1, 100) <= rate;
    }
}
