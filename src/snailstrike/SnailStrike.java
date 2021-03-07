package snailstrike;

import client.ClientClass;
import snailstrike.utils.GameKernel;
import static snailstrike.utils.Global.*;
import com.sun.glass.events.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import snailstrike.scene.MainScene;
import snailstrike.utils.Global;
import snailstrike.utils.Global.ClientMessage;

/**
 *
 * @author user1
 */
public class SnailStrike {

    public static void main(String[] args) {
        JFrame jf = new JFrame();// 遊戲視窗本體
        jf.setTitle("ActorMotion");// 視窗標題
        jf.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);// 視窗大小
//        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jf.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);// 關閉視窗時詢問後確認關閉程式
        jf.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                int option = JOptionPane.showConfirmDialog(null, "是否退出遊戲?", "系統提示", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (option == JOptionPane.YES_OPTION) {
                    ArrayList<String> strs = new ArrayList<>();
                    ClientClass.getInstance().sent(ClientMessage.SNAIL_EXECPTION, strs);
                    System.exit(0);
                }
            }
        });

        GI gi = new GI(new MainScene());// 遊戲的本體(邏輯 + 畫面處理)

        int[][] commands = {{KeyEvent.VK_UP, 8},
        {KeyEvent.VK_W, 0},
        {KeyEvent.VK_DOWN, 9},
        {KeyEvent.VK_S, 1},
        {KeyEvent.VK_LEFT, 10},
        {KeyEvent.VK_A, 2},
        {KeyEvent.VK_RIGHT, 11},
        {KeyEvent.VK_D, 3},
        {KeyEvent.VK_SPACE, 4},
        {KeyEvent.VK_ENTER, 12},
        {KeyEvent.VK_C, 7},
        {KeyEvent.VK_COMMA, 13},
        {KeyEvent.VK_X, 6},
        {KeyEvent.VK_PERIOD, 14},
        {KeyEvent.VK_Z, 5},
        {KeyEvent.VK_SLASH, 15}};

        GameKernel gk = new GameKernel.Builder(gi, LIMIT_DELTA_TIME, NANOSECOND_PER_UPDATE)
                .initListener(commands)
                .enableKeyboardTrack(gi)
                .enableMouseTrack(gi)
                .mouseForceRelease()
                .keyCleanMode()
                .trackChar()
                .gen();

        jf.add(gk);

        jf.setVisible(true);
        gk.run(IS_DEBUG);
    }

}
