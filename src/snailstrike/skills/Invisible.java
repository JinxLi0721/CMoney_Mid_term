package snailstrike.skills;

import snailstrike.animators.SkillAnimator;
import snailstrike.controllers.SceneController;
import snailstrike.gameobj.Vector;
import snailstrike.utils.Path;

public class Invisible extends Skill {

    public Invisible(String kindOfSkill, int x, int y, int r, Vector vector) {
        super(0, kindOfSkill, x, y, r, vector);
        this.animator = new SkillAnimator(SceneController.instance().irc().tryGetImage(new Path().img().skills().wind()),
                31, 27, SkillAnimator.State.WALK);
    }

}
