package snailstrike.gameobj;

import java.awt.Graphics;
import menu.Label;
import snailstrike.utils.GameKernel;

public class SkillDisplay implements GameKernel.GameInterface{

    private Label skillDis;
    private String skillChoose;

    public SkillDisplay(Label skillDis, String skillChoose) {
        this.skillChoose = skillChoose;
        this.skillDis = skillDis;
    }

    public String getSkillString() {
        return this.skillChoose;
    }

    public Label getskillDis() {
        return this.skillDis;
    }

    @Override
    public void paint(Graphics g) {
        this.skillDis.paint(g);
    }

    @Override
    public void update() {

    }

}
