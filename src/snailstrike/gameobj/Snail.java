package snailstrike.gameobj;

import client.ClientClass;
import com.sun.glass.events.KeyEvent;
import snailstrike.utils.*;
import java.awt.Color;
import snailstrike.utils.Animator.State;
import snailstrike.utils.Global.Direction;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import snailstrike.animators.BurnAnimator;
import snailstrike.controllers.AudioResourceController;
import snailstrike.skills.*;
import snailstrike.utils.CommandSolver.MouseState;
import snailstrike.utils.Global.ClientMessage;

public class Snail extends GameObject implements CommandSolver.KeyListener {

    private int serialNum;
    private String name;
    private Animator animator;
    private BurnAnimator burn;
    private Direction dir;
    private int hp;
    private final int hpLimit;
    private int magicPower;
    private final int magicPowerLimit;
    private Vector speed;
    private Vector a;
    private ArrayList<SkillDis> skills;
    private ArrayList<Skill> emission;
    private double speedX;
    private double speedY;
    private int speedUpX;
    private int speedUpY;
    private int choosenSkill;
    private Delay delaySkill;
    private Delay delayVisible;
    private boolean isVisible;
    private boolean isCollision;
    private boolean[] pressed;
    private boolean isEat;
    private boolean isEnd;
    private boolean isKeepHp;
    private boolean isBurn;
    private boolean isSpeedUp;
    private double snailSurvivedTime;
    private int totalHit;

    public enum Motion {
        STOP,
        WALK;
    }

    public enum SkillChoose {
        ATTACKFORCE("AttackForce", new Path().img().skills().attackForce_1(),
                new Path().img().skills().attackForce_2(),
                new Path().img().skills().attackForce_3(), 200, 250),
        ATTACKRANGE("AttackRange", new Path().img().skills().attackRange_1(),
                new Path().img().skills().attackRange_2(),
                new Path().img().skills().attackRange_3(), 200, 250),
        ATTACKSPLIT("AttackSplit", new Path().img().skills().attackSplit_1(),
                new Path().img().skills().attackSplit_2(),
                new Path().img().skills().attackSplit_3(), 200, 250),
        BOOMERANG("Boomerang", new Path().img().skills().boomerang_1(),
                new Path().img().skills().boomerang_2(),
                new Path().img().skills().boomerang_3(), 200, 250),
        ENRICHBLOOD("EnrichBlood", new Path().img().skills().enrichBlood_1(),
                new Path().img().skills().enrichBlood_2(),
                new Path().img().skills().enrichBlood_3(), 200, 250),
        TELEPORTATION("Teleportation", new Path().img().skills().teleportation_1(),
                new Path().img().skills().teleportation_2(),
                new Path().img().skills().teleportation_3(), 200, 250),
        INVISIBLE("Invisible", new Path().img().skills().invisible_1(),
                new Path().img().skills().invisible_2(),
                new Path().img().skills().invisible_3(), 200, 250),
        VELOCITY("Velocity", new Path().img().skills().velocity_1(),
                new Path().img().skills().velocity_2(),
                new Path().img().skills().velocity_3(), 200, 250), //        TRACK("Track", new Path().img().skills().track(), 28, 27),
        ;

        private String kind;
        private String imagePathNormal;
        private String imagePathHover;
        private String imagePathFocus;
        private int skillWidth;
        private int skillHeight;

        SkillChoose(String kind, String imagePathNormal, String imagePathHover, String imagePathFocus, int skillWidth, int skillHeight) {
            this.kind = kind;
            this.imagePathNormal = imagePathNormal;
            this.imagePathHover = imagePathHover;
            this.imagePathFocus = imagePathFocus;
            this.skillWidth = skillWidth;
            this.skillHeight = skillHeight;
        }

        public String getKind() {
            return this.kind;
        }

        public String getImagePathNormal() {
            return this.imagePathNormal;
        }

        public String getImagePathHover() {
            return this.imagePathHover;
        }

        public String getImagePathFocus() {
            return this.imagePathFocus;
        }

        public int getSkillWidth() {
            return this.skillWidth;
        }

        public int getSkillHeight() {
            return this.skillHeight;
        }

    }

    public enum SkillDis {
        ATTACKFORCE("AttackForce", new Path().img().skills().fire(), 28, 27),
        ATTACKRANGE("AttackRange", new Path().img().skills().bubble(), 28, 27),
        ATTACKSPLIT("AttackSplit", new Path().img().skills().smog(), 28, 27),
        BOOMERANG("Boomerang", new Path().img().skills().boomerang(), 28, 27),
        ENRICHBLOOD("EnrichBlood", new Path().img().skills().enrichblood(), 27, 27),
        TELEPORTATION("Teleportation", new Path().img().skills().teleportation(), 28, 27),
        INVISIBLE("Invisible", new Path().img().skills().wind(), 31, 27),
        VELOCITY("Velocity", new Path().img().skills().velocity(), 28, 32), //        TRACK("Track", new Path().img().skills().track(), 28, 27),
        ;

        private String kind;
        private String imagePath;
        private int skillWidth;
        private int skillHeight;

        SkillDis(String kind, String imagePath, int skillWidth, int skillHeight) {
            this.kind = kind;
            this.imagePath = imagePath;
            this.skillWidth = skillWidth;
            this.skillHeight = skillHeight;

        }

        public String getKind() {
            return this.kind;
        }

        public String getImagePath() {
            return this.imagePath;
        }

        public int getSkillWidth() {
            return this.skillWidth;
        }

        public int getSkillHeight() {
            return this.skillHeight;
        }

    }

    public Snail(int x, int y, String name, ArrayList<SkillDis> skills) {
        super(x, y, Global.UNIT_X / 8);
        animator = new Animator(0, State.WALK);
        burn = new BurnAnimator(BurnAnimator.State.WALK);
        dir = Direction.DOWN;
        hp = 800;
        hpLimit = hp;
        magicPower = 300;
        magicPowerLimit = magicPower;
        speed = new Vector();
        emission = new ArrayList<>();
        this.skills = skills;
        this.name = name;
        delaySkill = new Delay(300);
        delaySkill.loop();
        isCollision = false;
        pressed = new boolean[4];
        isEat = false;
        for (int i = 0; i < this.pressed.length; i++) {
            pressed[i] = false;
        }
        isEnd = false;
        isKeepHp = false;
        snailSurvivedTime = 0;
        isVisible = true;
        delayVisible = new Delay(300);
        totalHit = 0;
        isBurn = false;
        speedUpX = 0;
        speedUpY = 0;

    }

    public void setsSnailSurvivedTime(double snailSurvivedTime) {
        this.snailSurvivedTime = snailSurvivedTime;
    }

    public double getSnailSurvivedTime() {
        return this.snailSurvivedTime;
    }

    public void setTotalHit(int n) {
        this.totalHit = n;
    }

    public int getTotalHit() {
        return this.totalHit;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean getIsSpeedUp() {
        return this.isSpeedUp;
    }

    public void isSpeedUp() {
        isSpeedUp = true;
    }

    public void unSpeedUp() {
        isSpeedUp = false;
        this.speedUpX = 0;
        this.speedUpY = 0;
    }

    public String getName() {
        return this.name;
    }

    public void setSerialNum(int serialNum) {
        this.serialNum = serialNum;
    }

    public int getSerialNum() {
        return this.serialNum;
    }

    public void setSpeedX(double x) {
        this.speedX = x;
    }

    public void setSpeedY(double y) {
        this.speedY = y;
    }

    public void isEnd() {
        isEnd = true;
    }

    public boolean getIsEnd() {
        return isEnd;
    }

    public int getHpLimit() {
        return hpLimit;
    }

    public void isKeepHp() {
        isKeepHp = true;
    }

    public boolean getIsKeepHp() {
        return isKeepHp;
    }

    public void unKeepHp() {
        isKeepHp = false;
    }

    public void isEat() {
        isEat = true;
    }

    public void unEat() {
        isEat = false;
    }

    public boolean getIsEat() {
        return isEat;
    }

    public int getSnailHp() {
        return this.hp;
    }

    public void setSnailHp(int hp) {
        this.hp = hp;
    }

    public int getMagicPower() {
        return this.magicPower;
    }

    public void setMagicPower(int magicPower) {
        this.magicPower = magicPower;
    }

    public void isCollision() {
        isCollision = true;
    }

    public boolean getIsCollision() {
        return isCollision;
    }

    public boolean getIsVisible() {
        return isVisible;
    }

    public void unvisible() {
        this.isVisible = false;
        this.delayVisible.loop();
    }

    public void isVisible() {
        this.isVisible = true;
        delayVisible.pause();
    }

    public void setSpeedXY(double x, double y) {
        this.speedX = x;
        this.speedY = y;
    }

    public double getSpeedX() {
        return speedX;
    }

    public double getSpeedY() {
        return speedY;
    }

    public Vector getSpeed() {
        return speed;
    }

    public ArrayList<SkillDis> getSkills() {
        return this.skills;
    }

    public void changeDir(Direction dir) {
        this.dir = dir;
    }

    public void friction() {
        speedY *= 0.995;
        speedX *= 0.995;
//    this.vector = this.vector.multiply(0.96);
    }

    public void moveX() {
        if (this.collider().right() > Global.MAP_RANGE) {
            this.collider().setCenter(Global.MAP_RANGE - this.collider().radius(), this.collider().centerY() - 1);
            this.painter().setCenter(Global.MAP_RANGE - this.collider().radius(), this.collider().centerY() - 1);
            this.speedX = 0;
        } else if (this.collider().left() < 0) {
            this.collider().setCenter(0 + this.collider().radius(), this.collider().centerY() + 1);
            this.painter().setCenter(0 + this.collider().radius(), this.collider().centerY() + 1);
            this.speedX = 0;
        } else if (isSpeedUp) {
            this.translateX((int) speedUpX);
        } else {
            this.translateX((int) speedX);
//            this.translateX((int) this.vector.vx());
        }
    }

    public void moveY() {
        if (this.collider().bottom() > Global.MAP_RANGE) {
            this.collider().setCenter(this.collider().centerX(), Global.MAP_RANGE - this.collider().radius() - 1);
            this.painter().setCenter(this.collider().centerX(), Global.MAP_RANGE - this.collider().radius() - 1);
            this.speedY = 0;
        } else if (this.collider().top() < 0) {
            this.collider().setCenter(this.collider().centerX(), 0 + this.collider().radius() + 1);
            this.painter().setCenter(this.collider().centerX(), 0 + this.collider().radius() + 1);
            this.speedY = 0;
        } else if (isSpeedUp) {
            this.translateY((int) speedUpY);
        } else {
//            this.translateY((int) this.vector.vy());
            this.translateY((int) speedY);
        }
    }

    public Vector reverseVector() {
        return speed.reverse();
    }

    public void setState(State state) {
        this.animator.setState(state);
    }

    public Skill throwSkill() {
        if (!this.emission.isEmpty()) {
            Skill tmp = this.emission.get(this.emission.size() - 1);
            this.emission.remove(this.emission.size() - 1);
            return tmp;
        }
        return null;

    }

    public static ArrayList<SkillDis> chooseSkills() {
        ArrayList<SkillDis> chooseSkill = new ArrayList<>();
        ArrayList<SkillDis> attack = new ArrayList<>();
        attack.add(SkillDis.ATTACKFORCE);
        attack.add(SkillDis.ATTACKSPLIT);
        attack.add(SkillDis.ATTACKRANGE);
        attack.add(SkillDis.BOOMERANG);
//        attack.add(SkillDis.TRACK);
        ArrayList<SkillDis> defend = new ArrayList<>();
        defend.add(SkillDis.ENRICHBLOOD);
        defend.add(SkillDis.INVISIBLE);
        defend.add(SkillDis.TELEPORTATION);
        defend.add(SkillDis.VELOCITY);
        int addAttack = Global.random(0, attack.size() - 1);
        int addDefend = Global.random(0, defend.size() - 1);
        chooseSkill.add(attack.get(addAttack));
        attack.remove(addAttack);
        chooseSkill.add(defend.get(addDefend));
        defend.remove(addDefend);
        defend.addAll(attack);
        chooseSkill.add(defend.get(Global.random(0, defend.size() - 1)));
        return chooseSkill;
    }

    private boolean overlap() {
        int distanceToCenter = (int) Math.sqrt(Math.pow(this.collider().centerX() - Global.MAP_RANGE / 2, 2)
                + Math.pow(this.collider().centerY() - Global.MAP_RANGE / 2, 2));
        return distanceToCenter > Global.CIRCLE_RANGE_SHRINK;
    }

    @Override
    public void paintComponent(Graphics g) {
        for (int i = 0; i < this.emission.size(); i++) {
            this.emission.get(i).paint(g);
        }
        if (isVisible) {
            this.animator.paint(dir, this.painter().left(), this.painter().top(),
                    this.painter().right(), this.painter().bottom(), g);
            g.setColor(Color.white);
            g.drawString(this.name, this.painter().left(), this.painter().top() - 10);
            g.setColor(Color.red);
            g.fillRect(this.painter().left(), this.painter().top() - 5, this.hp / 8, 3);
        } else {
            g.setColor(Color.white);
            g.drawString(this.name, this.painter().left(), this.painter().top() - 10);
            g.setColor(Color.red);
            g.fillRect(this.painter().left(), this.painter().top() - 5, this.hp / 8, 3);
        }
        if (isBurn) {
            burn.paint(Direction.DOWN, this.painter().left() + 1, this.painter().top() + 2,
                    this.painter().right() - 1, this.painter().bottom(), g);
            AudioResourceController.getInstance().play(new Path().sounds().obj().burnning());
        }
    }

    @Override
    public void update() {
        if (this.hp > 0) {
            if (this.delayVisible.count() && !this.isVisible) {
                this.isVisible = true;
                delayVisible.pause();
                ArrayList<String> invisible = new ArrayList<>();
                ClientClass.getInstance().sent(ClientMessage.SNAIL_VISIBLE, invisible);
            }
            this.animator.update();
            this.burn.update();
            if (this.isSpeedUp) {
                moveX();
                moveY();
            }
            if (Math.abs(speedX) >= 1.0 && !isSpeedUp) {
//            } else if (Math.abs(this.vector.vx()) >= 0.0 || Math.abs(this.vector.vy()) >= 0) {
                moveX();
                friction();
            }
            if (Math.abs(speedY) >= 1.0 && !isSpeedUp) {
                moveY();
                friction();
            }
//            friction();
        }
        for (int i = 0; i < this.emission.size(); i++) {
            this.emission.get(i).update();
            if (this.emission.get(i).outOfScreen()) {
                emission.remove(i--);
            }
        }
        if (this.overlap()) {
            if (!isEnd && !isKeepHp) {
                this.hp = --this.hp < 0 ? 0 : this.hp;
            }
            this.isBurn = true;

        } else {
            this.isBurn = false;
        }
        if (this.magicPower < this.magicPowerLimit) {
            magicPower++;
        }
        speed.setVectorXY(painter().centerX(), painter().centerY());
    }

    public void addSkill(Snail snail, Rect rect, MouseEvent e) {
        int skillvx;
        int skillvy;
        Vector skill;
        if (e != null) {
            skillvx = (int) (e.getX() + rect.left() - this.collider().centerX());
            skillvy = (int) (e.getY() + rect.top() - this.collider().centerY());
            skill = new Vector(skillvx, skillvy).normalize().multiply(12);
        } else {
            skill = new Vector(this.speedX, this.speedY).normalize().multiply(12);
        }
        switch (skills.get(choosenSkill)) {
            case ATTACKFORCE:
                if (this.magicPower > 100) {
                    emission.add(new AttackForce(this.serialNum, "AttackForce", this.collider().centerX() + (int) (skill.vx() * 5),
                            collider().centerY() + (int) (skill.vy() * 5), 25, 15, skill));
                    this.magicPower -= 100;
                }
                break;
            case ATTACKRANGE:
                if (this.magicPower > 100) {
                    emission.add(new AttackRange(this.serialNum, "AttackRange", this.collider().centerX() + (int) (skill.vx() * 6),
                            collider().centerY() + (int) (skill.vy() * 6), 35, 10, skill));
                    this.magicPower -= 100;
                }
                break;
            case ATTACKSPLIT:
                if (this.magicPower > 200) {
                    emission.add(new AttackSplit(this.serialNum, "AttackSplit", this.collider().centerX() + (int) (skill.vx() * 5),
                            collider().centerY() + (int) (skill.vy() * 5), 25, 10, skill));
                    this.magicPower -= 200;
                }
                break;
            case BOOMERANG:
                if (this.magicPower > 150) {
                    emission.add(new Boomerang(this.serialNum, "Boomerang", this, this.collider().centerX() + (int) (skill.vx() * 5),
                            collider().centerY() + (int) (skill.vy() * 5), 25, 10, skill.multiply(2)));
                    this.magicPower -= 150;
                }
                break;
            case ENRICHBLOOD:
                if (this.magicPower > 150 && hp < 750) {
                    this.hp = this.hp + 50 > this.hpLimit ? hpLimit : this.hp + 50;
                    emission.add(new EnrichBlood("EnrichBlood", this.collider().centerX(),
                            collider().centerY() - 30, 25, new Vector(0, 0)));
                    this.magicPower -= 150;
                }
                break;
            case INVISIBLE:
                if (this.isVisible && this.magicPower > 150) {
                    this.isVisible = false;
                    this.magicPower -= 150;
                    delayVisible.loop();
                    ArrayList<String> invisible = new ArrayList<>();
                    ClientClass.getInstance().sent(ClientMessage.SNAIL_INVISIBLE, invisible);
                }
                break;
            case VELOCITY:
                if (this.magicPower > 100 && (speedX > 1 || speedX < -1) && (speedY > 1 || speedY < -1)) {
                    this.translate((int) this.speedX * 5, (int) this.speedY * 5);
                    this.setSpeedXY(speedX * 2, speedY * 2);
                    emission.add(new Velocity("Velocity", this.collider().centerX() - (int) this.speedX * 5,
                            collider().centerY() - (int) this.speedY * 5, 25, new Vector(0, 0)));
                    this.magicPower -= 100;
                }
                break;
            case TELEPORTATION:
                if (this.magicPower > 150 && this.hp > 0) {
                    this.collider().setCenter(e.getX() + rect.left(), e.getY() + rect.top());
                    this.painter().setCenter(e.getX() + rect.left(), e.getY() + rect.top());
                    this.magicPower -= 150;
                }
                break;
//            case TRACK:
//                if (this.magicPower > 150 && snail != null) {
//                    emission.add(new Track("Track", snail, this.collider().centerX() + (int) (skill.vx() * 5),
//                            collider().centerY() + (int) (skill.vy() * 5), 25, 6, skill));
//                    this.magicPower -= 150;
//                }
//                break;

        }

    }

    public void mouseTrig(Snail snail, Rect rect, MouseEvent e, CommandSolver.MouseState state, long trigTime) {
        if (state == MouseState.PRESSED && !this.isEat) {
            addSkill(snail, rect, e);
        }
    }

    @Override
    public void keyPressed(int commandCode, long trigTime) {
        ArrayList<String> snailState = new ArrayList<>();
        ArrayList<String> snailDir = new ArrayList<>();
        switch (commandCode) {
            case 0:
                changeDir(Global.Direction.UP);
                snailDir.add("UP");
                ClientClass.getInstance().sent(ClientMessage.SNAIL_SET_DIR, snailDir);
                snailDir.clear();
                animator.setState(State.WALK);
                snailState.add("WALK");
                ClientClass.getInstance().sent(ClientMessage.SNAIL_SET_STATE, snailState);
                snailState.clear();

                if (isSpeedUp) {
                    speedUpY = -4;
                } else {
                    speedY -= speedY > -7 ? 0.07 : 0;//加速度上限、每次加速度遞增值
                }
                break;
            case 1:
                changeDir(Global.Direction.DOWN);
                snailDir.add("DOWN");
                ClientClass.getInstance().sent(ClientMessage.SNAIL_SET_DIR, snailDir);
                snailDir.clear();
                animator.setState(State.WALK);
                snailState.add("WALK");
                ClientClass.getInstance().sent(ClientMessage.SNAIL_SET_STATE, snailState);
                snailState.clear();
                if (isSpeedUp) {
                    speedUpY = 4;
                } else {
                    speedY += speedY < 7 ? 0.07 : 0;
                }
                break;
            case 2:
                changeDir(Global.Direction.LEFT);
                snailDir.add("LEFT");
                ClientClass.getInstance().sent(ClientMessage.SNAIL_SET_DIR, snailDir);
                snailDir.clear();
                animator.setState(State.WALK);
                snailState.add("WALK");
                ClientClass.getInstance().sent(ClientMessage.SNAIL_SET_STATE, snailState);
                snailState.clear();
                if (isSpeedUp) {
                    speedUpX = -4;
                } else {
                    speedX -= speedX > -7 ? 0.07 : 0;
                }
                break;
            case 3:
                changeDir(Global.Direction.RIGHT);
                snailDir.add("RIGHT");
                ClientClass.getInstance().sent(ClientMessage.SNAIL_SET_DIR, snailDir);
                snailDir.clear();
                animator.setState(State.WALK);
                snailState.add("WALK");
                ClientClass.getInstance().sent(ClientMessage.SNAIL_SET_STATE, snailState);
                snailState.clear();
                if (isSpeedUp) {
                    speedUpX = 4;
                } else {
                    speedX += speedX < 7 ? 0.07 : 0;
                }
                break;
        }
    }

    @Override
    public void keyReleased(int commandCode, long trigTime) {
        if (commandCode < 4) {
            if (commandCode < 2) {
                speedUpY = 0;
            } else if (commandCode < 4) {
                speedUpX = 0;
            }
            this.animator.setState(State.STAND);
            ArrayList<String> snailState = new ArrayList<>();
            snailState.add("STAND");
            ClientClass.getInstance().sent(ClientMessage.SNAIL_SET_STATE, snailState);
        }
    }

    @Override
    public void keyTyped(char c, long trigTime) {
        switch (c) {
            case KeyEvent.VK_Z:
                choosenSkill = 0;
                break;
            case KeyEvent.VK_X:
                choosenSkill = 1;
                break;
            case KeyEvent.VK_C:
                choosenSkill = 2;
                break;
        }
    }
}
