/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package snailstrike.skills;

import snailstrike.animators.SkillAnimator;
import snailstrike.controllers.SceneController;
import snailstrike.gameobj.Vector;
import snailstrike.utils.Path;

/**
 *
 * @author Kylie
 */
public class Teleportation extends Skill {

    public Teleportation(String kindOfSkill, int x, int y, int r, Vector vector) {
        super(0, kindOfSkill, x, y, r, vector);
        this.animator = new SkillAnimator(SceneController.instance().irc().tryGetImage(new Path().img().skills().teleportation()),
                28, 27, SkillAnimator.State.WALK);
        delayDisappear.play();
    }

}
