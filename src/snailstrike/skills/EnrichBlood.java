package snailstrike.skills;

import snailstrike.animators.SkillAnimator;
import snailstrike.controllers.SceneController;
import snailstrike.gameobj.Vector;
import snailstrike.utils.Path;

public class EnrichBlood extends Skill {

    public EnrichBlood(String kindOfSkill, int x, int y, int r, Vector vector) {
        super(0, kindOfSkill, x, y, r, vector);
        this.animator = new SkillAnimator(SceneController.instance().irc().tryGetImage(new Path().img().skills().enrichblood()),
                27, 27, SkillAnimator.State.WALK);
        delayDisappear.play();
    }
}
