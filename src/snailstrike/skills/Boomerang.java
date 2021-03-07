package snailstrike.skills;

import snailstrike.animators.SkillAnimator;
import snailstrike.controllers.SceneController;
import snailstrike.gameobj.Snail;
import snailstrike.gameobj.Vector;
import snailstrike.utils.Path;

public class Boomerang extends Skill {

    public Boomerang(int snailEmissionNum, String kindOfSkill, Snail snailReference, int x, int y, int r, int skillPower, Vector vector) {
        super(snailEmissionNum, kindOfSkill, x, y, r, vector);
        this.animator = new SkillAnimator(SceneController.instance().irc().tryGetImage(new Path().img().skills().boomerang()),
                28, 27, SkillAnimator.State.WALK);
        this.snailReference = snailReference;
        this.skillPower = skillPower;
    }

    @Override
    public void update() {
        this.speed = speed.add(new Vector(snailReference.collider().centerX() - this.collider().centerX(),
                snailReference.collider().centerY() - this.collider().centerY()).normalize());
        this.translate((int) speed.vx(), (int) speed.vy());
        this.animator.update();
    }
}
