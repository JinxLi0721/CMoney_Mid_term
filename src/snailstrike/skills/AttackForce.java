package snailstrike.skills;

import snailstrike.animators.SkillAnimator;
import snailstrike.controllers.SceneController;
import snailstrike.gameobj.Vector;
import snailstrike.utils.Path;

public class AttackForce extends Skill {

    public AttackForce(int snailEmissionNum, String kindOfSkill, int x, int y, int r, int skillPower, Vector vector) {
        super(snailEmissionNum, kindOfSkill, x, y, r, vector);
        this.animator = new SkillAnimator(SceneController.instance().irc().tryGetImage(new Path().img().skills().fire()),
                28, 27, SkillAnimator.State.WALK);
        this.skillPower = skillPower;
    }
}
