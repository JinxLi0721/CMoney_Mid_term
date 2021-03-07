/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package snailstrike;

import java.awt.AlphaComposite;
import java.awt.Composite;
import snailstrike.scene.Scene;
import snailstrike.controllers.SceneController;
import snailstrike.utils.CommandSolver.*;
import snailstrike.utils.GameKernel;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import snailstrike.animators.BurnAnimator;
import snailstrike.animators.BurnAnimator.State;
import snailstrike.utils.Global;

/**
 *
 * @author user1
 */
public class GI implements GameKernel.GameInterface, MouseCommandListener, KeyListener {

    public GI(Scene startScene) {
        SceneController.instance().change(startScene);
    }

    @Override
    public void paint(Graphics g) {
        SceneController.instance().paint(g);
    }

    @Override
    public void update() {
        SceneController.instance().update();
    }

    @Override
    public void mouseTrig(MouseEvent e, MouseState state, long trigTime) {
        MouseCommandListener ml = SceneController.instance().mouseListener();
        if (ml != null) {
            ml.mouseTrig(e, state, trigTime);
        }
    }

    @Override
    public void keyPressed(int commandCode, long trigTime) {
        KeyListener kl = SceneController.instance().keyListener();
        if (kl != null) {
            kl.keyPressed(commandCode, trigTime);
        }
    }

    @Override
    public void keyReleased(int commandCode, long trigTime) {
        KeyListener kl = SceneController.instance().keyListener();
        if (kl != null) {
            kl.keyReleased(commandCode, trigTime);
        }
    }

    @Override
    public void keyTyped(char c, long trigTime) {
        KeyListener kl = SceneController.instance().keyListener();
        if (kl != null) {
            kl.keyTyped(c, trigTime);
        }
    }

}
