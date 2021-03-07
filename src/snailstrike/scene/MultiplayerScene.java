package snailstrike.scene;

import snailstrike.items.Bomb;
import snailstrike.items.Items;
import camera.Camera;
import camera.MapInformation;
import client.ClientClass;
import client.CommandReceiver;
import com.sun.glass.events.KeyEvent;
import java.awt.AlphaComposite;
import java.awt.Color;
import snailstrike.utils.*;
import snailstrike.gameobj.Snail;
import snailstrike.utils.CommandSolver.*;
import java.awt.Graphics;
import java.awt.Graphics2D;
import snailstrike.animators.*;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;
import menu.BackgroundImage;
import menu.Label;
import menu.Style;
import snailstrike.controllers.AudioResourceController;
import snailstrike.controllers.SceneController;
import snailstrike.gameobj.Vector;
import snailstrike.skills.*;
import snailstrike.gameobj.*;
import snailstrike.gameobj.Snail.SkillDis;
import snailstrike.items.*;
import snailstrike.items.Items.Situation;
import snailstrike.utils.Global.ClientMessage;
import static snailstrike.utils.Global.IS_DEBUG;

public class MultiplayerScene extends Scene {

    private static final double PI = 3.14;
    private int itemSerialNum;
    private String name;
    private String IP;
    private ArrayList<SkillDis> skillChosen;
    private Snail thisClientSnail;
    private ArrayList<Snail> deadSnails;
    private ArrayList<Snail> snails;
    private ArrayList<Skill> sceneEmission;
    private ArrayList<Items> itemsList;
    private ArrayList<Integer> itemsKind;
    private int randomItemsKind;
    private ExploseMove bombExploseMove;
    private ArrayList<String> snailinf;
    private Delay delayCircleRange;
    private Delay delayProps;
    private Camera cam;
    private static MapInformation mapInfo;
    private Image map;
    private Image map2;
    private int itemsExist;
    private double pastTime;
    private double presentTime;
    private double changeToEndSceneTime;
    private double itemsApperProbability;
    private static Graphics2D ovalSrcIn;
    private static BufferedImage imageSrcIn;
    private Image gameResults;
    private int snailHpLimit;
    private Label[] skillDisplay;

//     private Water water;//擇一
    private Lava lava;//擇一
//    private Lava2 lava2;//擇一

    public MultiplayerScene(String name, String IP, ArrayList<SkillDis> skillChosen) {
        this.IP = IP;
        this.name = name;
        this.skillChosen = skillChosen;
    }

    @Override
    public void sceneBegin() {
        SceneController.instance().irc().tryGetImage(new Path().img().props().BlackHoleNormal());
        SceneController.instance().irc().tryGetImage(new Path().img().props().BlackHoleBoom());
        SceneController.instance().irc().tryGetImage(new Path().img().props().Bump());
        SceneController.instance().irc().tryGetImage(new Path().img().props().Defend());
        SceneController.instance().irc().tryGetImage(new Path().img().props().ExplodeAtOnce());
        SceneController.instance().irc().tryGetImage(new Path().img().props().ExplodeAtOnceExplose());
        SceneController.instance().irc().tryGetImage(new Path().img().props().Meteorite());
        SceneController.instance().irc().tryGetImage(new Path().img().props().MeteoriteExplose());
        SceneController.instance().irc().tryGetImage(new Path().img().props().MeteoriteRoll());
        SceneController.instance().irc().tryGetImage(new Path().img().props().MeteoriteRollExplose());
        SceneController.instance().irc().tryGetImage(new Path().img().props().bombNormal());
        SceneController.instance().irc().tryGetImage(new Path().img().props().explosion_sp());
        SceneController.instance().irc().tryGetImage(new Path().img().props().trapExplose());
        SceneController.instance().irc().tryGetImage(new Path().img().props().trapLeaves());
        SceneController.instance().irc().tryGetImage(new Path().img().objs().youDied());
        SceneController.instance().irc().tryGetImage(new Path().img().objs().youSurvived());
        AudioResourceController.getInstance().stop(new Path().sounds().StartScene());
        AudioResourceController.getInstance().play(new Path().sounds().GameStartMusic());
        

        itemSerialNum = 0;
        Global.CIRCLE_RANGE_SHRINK = Global.CIRCLE_RANGE;
        deadSnails = new ArrayList<>();
//         water = new Water();
        lava = new Lava();
//        lava2 = new Lava2();
        mapInfo = new MapInformation();
        mapInfo.setMapInfo(0, 0, Global.MAP_RANGE, Global.MAP_RANGE);
        snails = new ArrayList<>();
        sceneEmission = new ArrayList<>();
        itemsList = new ArrayList<>();
        itemsKind = new ArrayList<Integer>();
        itemsKind.add(0);
        itemsKind.add(1);
        itemsKind.add(2);
        itemsKind.add(3);
        itemsKind.add(4);
        itemsKind.add(5);
        itemsKind.add(6);
        itemsKind.add(7);
        randomItemsKind = 0;
        snailinf = new ArrayList<>();
        bombExploseMove = new ExploseMove(10);
        delayCircleRange = new Delay(480);
        delayCircleRange.loop();
        presentTime = System.nanoTime();
        changeToEndSceneTime = 0;
        itemsApperProbability = 0;
        delayProps = new Delay(itemsExist);
        itemsExist = 300;
        delayProps.loop();
        map = SceneController.instance().irc().tryGetImage(new Path().img().map().mud2());
        map2 = SceneController.instance().irc().tryGetImage(new Path().img().map().mapSpellAll());
        imageSrcIn = new BufferedImage(Global.CIRCLE_RANGE, Global.CIRCLE_RANGE, BufferedImage.TYPE_INT_ARGB);
        ovalSrcIn = imageSrcIn.createGraphics();
        ovalSrcIn.fillOval(0, 0, Global.CIRCLE_RANGE, Global.CIRCLE_RANGE);
        ovalSrcIn.setComposite(AlphaComposite.SrcIn);
        ovalSrcIn.drawImage(map2, 0, 0, 1000, 1000, null);
        for (int i = 0; i < itemsList.size(); i++) {
            itemsList.get(i).setSituation(Situation.EXIST);
        }
        try {
            ClientClass.getInstance().connect(IP, 9876);
        } catch (IOException ex) {
            ClientClass.getInstance().disConnect();
            Server.Server.instance().create(9876);
            Server.Server.instance().start();
            try {
                ClientClass.getInstance().connect("127.0.0.1", 9876);
            } catch (IOException ex1) {
                Logger.getLogger(MultiplayerScene.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
        this.thisClientSnail = new Snail(Global.MAP_RANGE / 2, Global.MAP_RANGE / 2, name, this.skillChosen);
        thisClientSnail.setSerialNum(ClientClass.getInstance().getID());
        snailinf.add(Integer.toString(this.thisClientSnail.collider().centerX()));
        snailinf.add(Integer.toString(this.thisClientSnail.collider().centerY()));
        snailinf.add(name);
        snailinf.add(thisClientSnail.getSkills().get(0).getKind());
        snailinf.add(thisClientSnail.getSkills().get(1).getKind());
        snailinf.add(thisClientSnail.getSkills().get(2).getKind());
        snails.add(thisClientSnail);
        ClientClass.getInstance().sent(ClientMessage.SNAIL_CONNECT, snailinf);
        cam = new Camera.Builder(1280, 760)
                .setCameraStartLocation(1500, 1500)
                .setChaseObj(thisClientSnail)
                .gen();
        snailHpLimit = thisClientSnail.getHpLimit();
        skillDisplay = new Label[3];
        for (int i = 0; i < skillDisplay.length; i++) {
            BufferedImage img = (BufferedImage) SceneController.instance().irc().tryGetImage(thisClientSnail.getSkills().get(i).getImagePath());
            Image subimg = img.getSubimage(thisClientSnail.getSkills().get(i).getSkillWidth() * 3, 0, thisClientSnail.getSkills().get(i).getSkillWidth(), thisClientSnail.getSkills().get(i).getSkillHeight());
            skillDisplay[i] = skillDisplay(50 + i * 100, Global.SCREEN_Y - 135, subimg);
        }
        skillDisplay[0].isHover();
    }

    @Override
    public void sceneEnd() {
        ClientClass.getInstance().disConnect();
        Server.Server.instance().close();
        name = null;
        IP = null;
        skillChosen = null;
        thisClientSnail = null;
        deadSnails = null;
        snails = null;
        sceneEmission = null;
        itemsList = null;
        itemsKind = null;
        bombExploseMove = null;
        snailinf = null;
        delayCircleRange = null;
        delayProps = null;
        cam = null;
        map = null;
        map2 = null;
        gameResults = null;
        skillDisplay = null;
    }

    public Items choosePropsKind(int itemSerialNum, int x, int y) {
        switch (randomItemsKind) {
            case 0:
                return new Bomb(itemSerialNum, x, y);
            case 1:
                return new Trap(itemSerialNum, x, y);
            case 2:
                return new ExplodeAtOnce(itemSerialNum, x, y);
            case 3:
                return new Defend(itemSerialNum, x, y);
            case 4:
                return new Meteorite(itemSerialNum, x, y);
            case 5:
                return new MeteoriteRoll(itemSerialNum, x, y);
            case 6:
                return new Bump(itemSerialNum, x, y);
            case 7:
                return new BlackHole(itemSerialNum, x, y);
        }
        return null;
    }

    public Label skillDisplay(int x, int y, Image img) {
        Style normal = new Style.StyleOval(75, 75, false, new BackgroundImage(img))
                .setHaveBorder(true)
                .setBorderColor(Color.GRAY)
                .setBorderThickness(3);
        Style hover = new Style.StyleOval(75, 75, false, new BackgroundImage(img))
                .setHaveBorder(true)
                .setBorderColor(Color.BLACK)
                .setBorderThickness(3);
        Label label = new Label(x, y, normal);
        label.setStyleHover(hover);
        return label;
    }

    public Vector setCollisionVector(double snailCenterX, double snailCenterY, double attackCenterX, double attackCenterY) {
        Vector collisionVector = new Vector();
        double vectorX = snailCenterX - attackCenterX;
        double vectorY = snailCenterY - attackCenterY;
        collisionVector.setVectorXY(vectorX, vectorY);
        return collisionVector;
    }

    public Vector exploseMoveVector(Vector snail, Vector attack, double attackPower, Vector collisionVector) {
        snail.add(collisionVector);
        Vector exploseMoveVector = new Vector(collisionVector.normalize().vx() * attackPower, collisionVector.normalize().vy() * attackPower);
        return exploseMoveVector;
    }

    @Override
    public void paint(Graphics g) {
        cam.start(g);
        Rect c = cam.collider();
        lava.paint(c.left(), c.top(), c.right(), c.bottom(), g);
        if (Global.IS_DEBUG) {
            g.setColor(new Color(115, 74, 18));
            g.fillOval(Global.MAP_RANGE / 2 - Global.CIRCLE_RANGE_SHRINK,
                    Global.MAP_RANGE / 2 - Global.CIRCLE_RANGE_SHRINK,
                    Global.CIRCLE_RANGE_SHRINK * 2,
                    Global.CIRCLE_RANGE_SHRINK * 2);
        }
        g.drawImage(imageSrcIn, Global.MAP_RANGE / 2 - Global.CIRCLE_RANGE_SHRINK,
                Global.MAP_RANGE / 2 - Global.CIRCLE_RANGE_SHRINK,
                Global.CIRCLE_RANGE_SHRINK * 2, Global.CIRCLE_RANGE_SHRINK * 2, null);

        for (int i = 0; i < this.snails.size(); i++) {
            if (snails.get(i).getIsVisible() || snails.get(i).getSerialNum() == thisClientSnail.getSerialNum()
                    && cam.isCollision(snails.get(i))) {
                this.snails.get(i).paint(g);
            }
        }
        for (int i = 0; i < this.sceneEmission.size(); i++) {
            if (cam.isCollision(sceneEmission.get(i))) {
                this.sceneEmission.get(i).paint(g);
            }
        }
        for (int i = 0; i < itemsList.size(); i++) {
            if (cam.isCollision(itemsList.get(i))) {
                itemsList.get(i).paintAll(g);
            }
        }
        for (int i = 0; i < deadSnails.size(); i++) {
            deadSnails.get(i).paint(g);
        }
        g.drawImage(gameResults, cam.collider().centerX() - 300,
                cam.collider().centerY() - 300, 600,
                300, null);
        cam.end(g);
        //顯示目前選取的技能
        for (int i = 0; i < skillDisplay.length; i++) {
            skillDisplay[i].paint(g);
        }
        //存活時間顯示
        g.setColor(Color.black);
        g.fillRoundRect(50, 40, 320, 50, 20, 20);
        g.setColor(new Color(229, 181, 36));
//        g.setColor(Color.green);
        g.setFont(Global.FONT_ORIGIN);
        g.drawString("Survive Time " + Double.toString(this.pastTime), 66, 76);
        //顯示蝸牛自己的血量
        g.setColor(Color.gray);
        g.fillRoundRect(Global.SCREEN_X - 320, Global.SCREEN_Y - 160, 270, 100, 40, 40);
        g.setColor(Color.white);
        g.setFont(Global.FONT_DISPLAY);
        g.drawString("TotalHit：" + thisClientSnail.getTotalHit(), Global.SCREEN_X - 300, Global.SCREEN_Y - 130);
        g.drawString("HP", Global.SCREEN_X - 300, Global.SCREEN_Y - 100);
        g.drawString("MP", Global.SCREEN_X - 300, Global.SCREEN_Y - 80);
        g.drawString(thisClientSnail.getSnailHp() + "/800", Global.SCREEN_X - 140, Global.SCREEN_Y - 100);
        g.drawString(thisClientSnail.getMagicPower() + "/300", Global.SCREEN_X - 140, Global.SCREEN_Y - 80);
        g.setColor(Color.red);
        g.fillRect(Global.SCREEN_X - 260, Global.SCREEN_Y - 110, thisClientSnail.getSnailHp() / 8, 6);
        g.setColor(Color.blue);
        g.fillRect(Global.SCREEN_X - 260, Global.SCREEN_Y - 90, thisClientSnail.getMagicPower() / 3, 6);
    }

    @Override
    public void update() {
        cam.update();
//        water.update();
        lava.update();
//        lava2.update();
        pastTime = ((int) ((System.nanoTime() - presentTime) / 10000000)) / 100.0;
        //道具出現頻率
        itemsApperProbability += 0.1;
        if (Global.random(0, (int) (340 + itemsApperProbability)) == 0 && itemsList.size() < 12) {
            if (Global.CIRCLE_RANGE_SHRINK != 200) {
                if (ClientClass.getInstance().getID() == this.snails.get(0).getSerialNum()) {
                    this.randomItemsKind = Global.random(0, itemsKind.size() - 1);
                    int randomX = Global.random((Global.MAP_RANGE / 2 - Global.CIRCLE_RANGE_SHRINK),
                            (Global.MAP_RANGE / 2 + Global.CIRCLE_RANGE_SHRINK));
                    int randomY = Global.random((Global.MAP_RANGE / 2 - Global.CIRCLE_RANGE_SHRINK),
                            (Global.MAP_RANGE / 2 + Global.CIRCLE_RANGE_SHRINK));
                    ArrayList<String> addItem = new ArrayList<>();
                    Items item = choosePropsKind(itemSerialNum, randomX, randomY);
                    if (item != null) {
                        itemsList.add(item);
                        addItem.add(Integer.toString(itemSerialNum));
//                        itemsList.add(new BlackHole(itemSerialNum, Global.random((Global.MAP_RANGE / 2 - Global.CIRCLE_RANGE_SHRINK),
//                                (Global.MAP_RANGE / 2 + Global.CIRCLE_RANGE_SHRINK)),
//                                Global.random((Global.MAP_RANGE / 2 - Global.CIRCLE_RANGE_SHRINK),
//                                        (Global.MAP_RANGE / 2 + Global.CIRCLE_RANGE_SHRINK))));
                        itemSerialNum++;
                        addItem.add(Integer.toString(randomItemsKind));
                        addItem.add(Integer.toString(itemsList.get(itemsList.size() - 1).collider().centerX()));
                        addItem.add(Integer.toString(itemsList.get(itemsList.size() - 1).collider().centerY()));
                        addItem.add(item.getSituation().name());
                        addItem.add(item.getState().name());
                        addItem.add("0");
                        ClientClass.getInstance().sent(ClientMessage.ADD_ITEM, addItem);
                        addItem.clear();
                    }
                    for (int i = 0; i < itemsList.size(); i++) {
                        addItem.add(Integer.toString(itemsList.get(i).getSerialNum()));
                        switch (itemsList.get(i).getKind()) {
                            case BOMB:
                                addItem.add(Integer.toString(0));
                                break;
                            case TRAP:
                                addItem.add(Integer.toString(1));
                                break;
                            case EXPLODE_AT_ONCE:
                                addItem.add(Integer.toString(2));
                                break;
                            case DEFEND:
                                addItem.add(Integer.toString(3));
                                break;
                            case METEORITE:
                                addItem.add(Integer.toString(4));
                                break;
                            case METEORITE_ROLL:
                                addItem.add(Integer.toString(5));
                                break;
                            case BUMP:
                                addItem.add(Integer.toString(6));
                                break;
                            case BLACK_HOLE:
                                addItem.add(Integer.toString(7));
                                break;
                            default:
                                addItem.add(Integer.toString(-1));
                                break;
                        }
                        addItem.add(Integer.toString(itemsList.get(i).collider().centerX()));
                        addItem.add(Integer.toString(itemsList.get(i).collider().centerY()));
                        addItem.add(itemsList.get(i).getSituation().name());
                        addItem.add(itemsList.get(i).getItemsAnimator().getState().name());
                        if (itemsList.get(i).getSnailRef() == null) {
                            addItem.add(Integer.toString(0));
                        } else {
                            addItem.add(Integer.toString(itemsList.get(i).getSnailRef().getSerialNum()));
                        }
                        ClientClass.getInstance().sent(ClientMessage.ADD_ITEM, addItem);
                        addItem.clear();
                    }
                }
            }
        }

        Collections.sort(snails, (Snail o1, Snail o2) -> o1.getSerialNum() - o2.getSerialNum());
        if (delayCircleRange.count()) {
            if (ClientClass.getInstance().getID() == this.snails.get(0).getSerialNum()) {
                if (Global.CIRCLE_RANGE_SHRINK > 300) {
                    Global.CIRCLE_RANGE_SHRINK -= 20;
                    ArrayList<String> circleRange = new ArrayList<>();
                    circleRange.add(Integer.toString(Global.CIRCLE_RANGE_SHRINK));
                    ClientClass.getInstance().sent(ClientMessage.CHANGE_CIRCLERANGE, circleRange);
                    ovalSrcIn.fillOval(0, 0, Global.CIRCLE_RANGE_SHRINK, Global.CIRCLE_RANGE_SHRINK);
                    ovalSrcIn.setComposite(AlphaComposite.SrcIn);
                    ovalSrcIn.drawImage(map2, 0, 0, 1000, 1000,
                            Global.CIRCLE_RANGE - Global.CIRCLE_RANGE_SHRINK,
                            Global.CIRCLE_RANGE - Global.CIRCLE_RANGE_SHRINK,
                            2000 - (Global.CIRCLE_RANGE - Global.CIRCLE_RANGE_SHRINK),
                            2000 - (Global.CIRCLE_RANGE - Global.CIRCLE_RANGE_SHRINK),
                            null);

                }
            }
        }

        ClientClass.getInstance().consume(new CommandReceiver() {
            @Override
            public void receive(int serialNum, int commandCode, ArrayList<String> strs) {
                if (serialNum == ClientClass.getInstance().getID() && commandCode != ClientMessage.SNAIL_TOTALHIT) {
                    return;
                }
                switch (commandCode) {
                    case ClientMessage.SNAIL_CONNECT:
                        for (int i = 0; i < snails.size(); i++) {
                            if (serialNum == snails.get(i).getSerialNum()) {
                                return;
                            }
                        }
                        ArrayList<SkillDis> chooseSkills = new ArrayList<>();
                        for (int i = 0; i < chooseSkills.size(); i++) {
                            switch (strs.get(i + 3)) {
                                case "AttackForce":
                                    chooseSkills.add(Snail.SkillDis.ATTACKFORCE);
                                    break;
                                case "AttackRange":
                                    chooseSkills.add(Snail.SkillDis.ATTACKRANGE);
                                    break;
                                case "AttackSplit":
                                    chooseSkills.add(Snail.SkillDis.ATTACKSPLIT);
                                    break;
                                case "EnrichBlood":
                                    chooseSkills.add(Snail.SkillDis.ENRICHBLOOD);
                                    break;
                                case "Velocity":
                                    chooseSkills.add(Snail.SkillDis.VELOCITY);
                                    break;
                                case "Teleportation":
                                    chooseSkills.add(Snail.SkillDis.TELEPORTATION);
                                    break;
                                case "Boomerang":
                                    chooseSkills.add(Snail.SkillDis.BOOMERANG);
                                    break;
                                case "Invisible":
                                    chooseSkills.add(Snail.SkillDis.INVISIBLE);
                                    break;
//                                case "Track":
//                                    chooseSkills.add(Snail.SkillDis.TRACK);
//                                    break;
                            }
                        }
                        Snail snail = new Snail(Integer.parseInt(strs.get(0)), Integer.parseInt(strs.get(1)), strs.get(2), chooseSkills);
                        if (snail != null) {
                            snails.add(snail);
                            snail.setSerialNum(serialNum);
                            ClientClass.getInstance().sent(ClientMessage.SNAIL_CONNECT, snailinf);
                        }
                        if (thisClientSnail.getSerialNum() == snails.get(0).getSerialNum()) {
                            ArrayList<String> addItem = new ArrayList<>();
                            for (int i = 0; i < itemsList.size(); i++) {
                                addItem.add(Integer.toString(itemsList.get(i).getSerialNum()));
                                switch (itemsList.get(i).getKind()) {
                                    case BOMB:
                                        addItem.add(Integer.toString(0));
                                        break;
                                    case TRAP:
                                        addItem.add(Integer.toString(1));
                                        break;
                                    case EXPLODE_AT_ONCE:
                                        addItem.add(Integer.toString(2));
                                        break;
                                    case DEFEND:
                                        addItem.add(Integer.toString(3));
                                        break;
                                    case METEORITE:
                                        addItem.add(Integer.toString(4));
                                        break;
                                    case METEORITE_ROLL:
                                        addItem.add(Integer.toString(5));
                                        break;
                                    case BUMP:
                                        addItem.add(Integer.toString(6));
                                        break;
                                    case BLACK_HOLE:
                                        addItem.add(Integer.toString(7));
                                        break;
                                    default:
                                        addItem.add(Integer.toString(-1));
                                        break;
                                }
                                addItem.add(Integer.toString(itemsList.get(i).collider().centerX()));
                                addItem.add(Integer.toString(itemsList.get(i).collider().centerY()));
                                addItem.add(itemsList.get(i).getSituation().name());
                                addItem.add(itemsList.get(i).getItemsAnimator().getState().name());
                                if (itemsList.get(i).getSnailRef() == null) {
                                    addItem.add(Integer.toString(0));
                                } else {
                                    addItem.add(Integer.toString(itemsList.get(i).getSnailRef().getSerialNum()));
                                }
                                ClientClass.getInstance().sent(ClientMessage.ADD_ITEM, addItem);
                                addItem.clear();
                            }
                        }
                        break;
                    case ClientMessage.CHANGE_CIRCLERANGE:
                        Global.CIRCLE_RANGE_SHRINK = Integer.parseInt(strs.get(0));
                        ovalSrcIn.fillOval(0, 0, Global.CIRCLE_RANGE_SHRINK, Global.CIRCLE_RANGE_SHRINK);
                        ovalSrcIn.setComposite(AlphaComposite.SrcIn);
                        ovalSrcIn.drawImage(map2, 0, 0, 1000, 1000,
                                Global.CIRCLE_RANGE - Global.CIRCLE_RANGE_SHRINK,
                                Global.CIRCLE_RANGE - Global.CIRCLE_RANGE_SHRINK,
                                2000 - (Global.CIRCLE_RANGE - Global.CIRCLE_RANGE_SHRINK),
                                2000 - (Global.CIRCLE_RANGE - Global.CIRCLE_RANGE_SHRINK),
                                null);
                        break;
                    case ClientMessage.SNAIL_INVISIBLE:
                        for (int i = 0; i < snails.size(); i++) {
                            if (snails.get(i).getSerialNum() == serialNum) {
                                snails.get(i).unvisible();
                            }
                        }
                        break;
                    case ClientMessage.SNAIL_VISIBLE:
                        for (int i = 0; i < snails.size(); i++) {
                            if (snails.get(i).getSerialNum() == serialNum) {
                                snails.get(i).isVisible();
                            }
                        }
                        break;
                    case ClientMessage.SNAIL_INFORMATION:
                        for (int i = 0; i < snails.size(); i++) {
                            if (serialNum == snails.get(i).getSerialNum()) {
                                snails.get(i).collider().setCenter(Integer.parseInt(strs.get(0)), Integer.parseInt(strs.get(1)));
                                snails.get(i).painter().setCenter(Integer.parseInt(strs.get(0)), Integer.parseInt(strs.get(1)));
                                snails.get(i).setSnailHp(Integer.parseInt(strs.get(2)));
                                snails.get(i).setMagicPower(Integer.parseInt(strs.get(3)));
                                snails.get(i).setSpeedXY(Double.parseDouble(strs.get(4)), Double.parseDouble(strs.get(5)));
                                break;
                            }
                        }
                        break;
                    case ClientMessage.ADD_SKILL:
                        sceneAddSkill(serialNum, strs);
                        break;
                    case ClientMessage.SNAIL_SET_VECTOR:
                        for (int i = 0; i < snails.size(); i++) {
                            if (snails.get(i).getSerialNum() == serialNum) {
                                snails.get(i).setSpeedXY(Double.parseDouble(strs.get(0)), Double.parseDouble(strs.get(1)));
                            }
                        }
                        break;
                    case ClientMessage.SNAIL_TOTALHIT:
                        for (int i = 0; i < snails.size(); i++) {
                            if (snails.get(i).getSerialNum() == Integer.parseInt(strs.get(0))) {
                                snails.get(i).setTotalHit(snails.get(i).getTotalHit() + 1);
                                break;
                            }
                        }
                        break;
                    case ClientMessage.ADD_ITEM:
                        randomItemsKind = Integer.parseInt(strs.get(1));
                        if (!itemsList.isEmpty()) {
                            for (int i = 0; i < itemsList.size(); i++) {
                                if (Integer.parseInt(strs.get(0)) == itemsList.get(i).getSerialNum()) {
                                    return;
                                }
                            }
                        }
                        itemsList.add(choosePropsKind(Integer.parseInt(strs.get(0)), Integer.parseInt(strs.get(2)), Integer.parseInt(strs.get(3))));
                        itemsList.get(itemsList.size() - 1).setSituation(Situation.valueOf(strs.get(4)));
                        itemsList.get(itemsList.size() - 1).setState(ItemsAnimator.State.valueOf(strs.get(5)));
                        if (strs.get(6) == "0") {
                            return;
                        } else {
                            for (int i = 0; i < snails.size(); i++) {
                                if (snails.get(i).getSerialNum() == Integer.parseInt(strs.get(6))) {
                                    itemsList.get(itemsList.size() - 1).setSnailRef(snails.get(i));
                                    break;
                                }
                            }
                        }
                        break;
                    case ClientMessage.CHANGE_ITEM_STATE:
                        for (int i = 0; i < itemsList.size(); i++) {
                            if (itemsList.get(i).getSerialNum() == Integer.parseInt(strs.get(0))) {
                                switch (strs.get(1)) {
                                    case "NORMAL":
                                        itemsList.get(i).setState(ItemsAnimator.State.NORMAL);
                                        break;
                                    case "BOOM":
                                        itemsList.get(i).setState(ItemsAnimator.State.BOOM);
                                        break;
                                    case "REMOVE":
                                        itemsList.get(i).setState(ItemsAnimator.State.REMOVE);
                                        break;
                                }
                            }
                        }
                        break;
                    case ClientMessage.CHANGE_ITEM_SITUATION:
                        for (int i = 0; i < itemsList.size(); i++) {
                            if (itemsList.get(i).getSerialNum() == Integer.parseInt(strs.get(0))) {
                                switch (strs.get(1)) {
                                    case "EATED":
                                        itemsList.get(i).setSituation(Situation.EATED);
                                        for (int j = 0; j < snails.size(); j++) {
                                            if (serialNum == snails.get(j).getSerialNum()) {
                                                itemsList.get(i).setSnailRef(snails.get(j));
                                            }
                                        }
                                        break;
                                    case "THROWED":
                                        itemsList.get(i).setSituation(Situation.THROWED);
                                        break;
                                    case "STOP":
                                        itemsList.get(i).setSituation(Situation.STOP);
                                        break;
                                }
                            }
                        }
                        break;
                    case ClientMessage.ITEM_AFTER_SNAIL:
                        for (int i = 0; i < snails.size(); i++) {
                            if (Integer.parseInt(strs.get(1)) == snails.get(i).getSerialNum()) {
                                for (int j = 0; j < itemsList.size(); j++) {
                                    if (itemsList.get(j).getSerialNum() == Integer.parseInt(strs.get(0))) {
                                        itemsList.get(j).setAllCenter(snails.get(i).painter().centerX(), snails.get(i).painter().top() - 30);
                                        itemsList.get(j).setSnailRef(snails.get(i));
                                        break;
                                    }
                                }
                            }
                        }
                    case ClientMessage.SNAIL_SET_STATE:
                        for (int i = 0; i < snails.size(); i++) {
                            if (serialNum == snails.get(i).getSerialNum()) {
                                switch (strs.get(0)) {
                                    case "STAND":
                                        snails.get(i).setState(Animator.State.STAND);
                                        break;
                                    case "WALK":
                                        snails.get(i).setState(Animator.State.WALK);
                                        break;
                                }
                            }
                        }
                        break;
                    case ClientMessage.SNAIL_SET_DIR://
                        for (int i = 0; i < snails.size(); i++) {
                            if (serialNum == snails.get(i).getSerialNum()) {
                                switch (strs.get(0)) {
                                    case "UP":
                                        snails.get(i).changeDir(Global.Direction.UP);
                                        break;
                                    case "DOWN":
                                        snails.get(i).changeDir(Global.Direction.DOWN);
                                        break;
                                    case "LEFT":
                                        snails.get(i).changeDir(Global.Direction.LEFT);
                                        break;
                                    case "RIGHT":
                                        snails.get(i).changeDir(Global.Direction.RIGHT);
                                        break;
                                }
                            }
                        }
                        break;
                    case ClientMessage.ITEM_SET_MOUSEEVENT:
                        for (int i = 0; i < itemsList.size(); i++) {
                            if (itemsList.get(i).getSerialNum() == Integer.parseInt(strs.get(0))) {
                                itemsList.get(i).setMouseEvent(strs);
                            }
                        }
                        break;
                    case ClientMessage.SNAIL_END:
                        for (int i = 0; i < deadSnails.size(); i++) {
                            if (serialNum == deadSnails.get(i).getSerialNum()) {
                                return;
                            }
                        }
                        for (int i = 0; i < snails.size(); i++) {
                            if (serialNum == snails.get(i).getSerialNum() && snails.size() > 1) {
                                snails.get(i).setTotalHit(Integer.parseInt(strs.get(0)));
                                snails.get(i).setsSnailSurvivedTime(Double.parseDouble(strs.get(1)));
                                snails.get(i).isEnd();
                                deadSnails.add(snails.get(i));
                                snails.remove(snails.get(i));
                            }
                        }
                        break;
                    case ClientMessage.GAMEOVER:
                        snails.get(0).setTotalHit(Integer.parseInt(strs.get(0)));
                        snails.get(0).setsSnailSurvivedTime(Double.parseDouble(strs.get(1)));
                        snails.get(0).isEnd();
                        SceneController.instance().change(new EndScene(thisClientSnail, snails.get(0), deadSnails, IP));
                        break;
                    case ClientMessage.SNAIL_EXECPTION:
                        for (int i = 0; i < snails.size(); i++) {
                            if (serialNum == snails.get(i).getSerialNum()) {
                                snails.get(i).setTotalHit(-1);
                                snails.get(i).setsSnailSurvivedTime(-1);
                                deadSnails.add(snails.get(i));
                                snails.remove(snails.get(i));
                            }
                        }
                        break;

                }
            }
        });
        thisClientSnail.update();
        ArrayList<String> tmp = new ArrayList<>();
        tmp.add(Integer.toString(thisClientSnail.collider().centerX()));
        tmp.add(Integer.toString(thisClientSnail.collider().centerY()));
        tmp.add(Integer.toString(thisClientSnail.getSnailHp()));
        tmp.add(Integer.toString(thisClientSnail.getMagicPower()));
        tmp.add(Double.toString(thisClientSnail.getSpeedX()));
        tmp.add(Double.toString(thisClientSnail.getSpeedY()));
        ClientClass.getInstance().sent(ClientMessage.SNAIL_INFORMATION, tmp);
        //skill碰撞
        for (int i = 0; i < this.sceneEmission.size(); i++) {
            this.sceneEmission.get(i).update();
            if (this.sceneEmission.get(i).distance() >= 600 || this.sceneEmission.get(i).delayDisappear().count()) {
                sceneEmission.remove(i--);
                continue;
            }
            //AttackSplit分裂
            if (sceneEmission.get(i).distance() % 200 < 10 && sceneEmission.get(i) instanceof AttackSplit) {
                double theta = sceneEmission.get(i).getSpeed().getRadian();
                double changeRadian = Math.toRadians(20);
                double changeRadianRight = theta + changeRadian > PI ? theta + changeRadian - 2 * PI : theta + changeRadian;
                double changeRadianLeft = theta - changeRadian < (PI * -1) ? theta - changeRadian + 2 * PI : theta - changeRadian;
                Vector attackSpeedRight = new Vector(Math.cos(changeRadianRight), Math.sin(changeRadianRight)).normalize().multiply(12);
                Vector attackSpeedLeft = new Vector(Math.cos(changeRadianLeft), Math.sin(changeRadianLeft)).normalize().multiply(12);
                sceneEmission.add(new AttackSplit(sceneEmission.get(i).getSnailEmissionNum(), "AttackSplit", sceneEmission.get(i).collider().centerX(),
                        sceneEmission.get(i).collider().centerY(), 25, 10, attackSpeedRight
                ));
                sceneEmission.get(sceneEmission.size() - 1).setDistance(sceneEmission.get(i).distance());
                sceneEmission.get(i).getSpeed().setVectorXY(attackSpeedLeft.vx(), attackSpeedLeft.vy());
                ;
            }
            //如果本機腳色可視再判斷碰撞
            if (this.sceneEmission.get(i).isCollision(this.thisClientSnail) && sceneEmission.get(i).getSnailEmissionNum() != thisClientSnail.getSerialNum()
                    && thisClientSnail.getIsVisible() && sceneEmission.get(i).getSnailEmissionNum() != 0) {
                double vectorX = thisClientSnail.collider().centerX()
                        - this.sceneEmission.get(i).collider().centerX();
                double vectorY = thisClientSnail.collider().centerY()
                        - this.sceneEmission.get(i).collider().centerY();
                Vector snailVector = new Vector(thisClientSnail.getSpeedX(), thisClientSnail.getSpeedY());
                Vector collisionVector = snailVector.add(new Vector(vectorX, vectorY));
                thisClientSnail.setSpeedXY(collisionVector.normalize().vx() * this.sceneEmission.get(i).skillPower(),
                        collisionVector.normalize().vy() * this.sceneEmission.get(i).skillPower());
                ArrayList<String> sentVector = new ArrayList<>();
                sentVector.add(Double.toString(collisionVector.normalize().vx() * this.sceneEmission.get(i).skillPower()));
                sentVector.add(Double.toString(collisionVector.normalize().vy() * this.sceneEmission.get(i).skillPower()));
                ClientClass.getInstance().sent(ClientMessage.SNAIL_SET_VECTOR, sentVector);
                ArrayList<String> addSnailTotalHit = new ArrayList<>();
                addSnailTotalHit.add(Integer.toString(sceneEmission.get(i).getSnailEmissionNum()));
                ClientClass.getInstance().sent(ClientMessage.SNAIL_TOTALHIT, addSnailTotalHit);
                sceneEmission.remove(i--);
                continue;
            }
            for (int j = 0; j < snails.size(); j++) {
                if (this.sceneEmission.get(i) instanceof Boomerang && this.sceneEmission.get(i).isCollision(snails.get(j))) {
                    if (sceneEmission.get(i).getSnailReference() == snails.get(j)) {
                        sceneEmission.remove(i--);
                        break;
                    }
                }
            }
        }
        for (int i = 0; i < itemsList.size(); i++) {
            itemsList.get(i).update();
        }
        for (int i = 0; i < deadSnails.size(); i++) {
            deadSnails.get(i).update();
        }

        for (int w = 0; w < itemsList.size(); w++) {
            ArrayList<String> changeState = new ArrayList<>();
            ArrayList<String> changeSituation = new ArrayList<>();
            if (itemsList.get(w).getSituation() == Situation.EXIST) {
                if (itemsList.get(w).isCollision(thisClientSnail) && !thisClientSnail.getIsEat()) {//吃到
                    itemsList.get(w).setState(ItemsAnimator.State.NORMAL);
                    //net
                    changeState.add(Integer.toString(itemsList.get(w).getSerialNum()));
                    changeState.add("NORMAL");
                    ClientClass.getInstance().sent(ClientMessage.CHANGE_ITEM_STATE, changeState);
                    changeState.clear();
                    //net
                    if (itemsList.get(w) instanceof ExplodeAtOnce) {
                        itemsList.get(w).setSituation(Situation.THROWED);
                        //net
                        changeSituation.add(Integer.toString(itemsList.get(w).getSerialNum()));
                        changeSituation.add("THROWED");
                        ClientClass.getInstance().sent(ClientMessage.CHANGE_ITEM_SITUATION, changeSituation);
                        changeSituation.clear();
                        //net
                    } else if (thisClientSnail.getSnailHp() > 0) {
                        itemsList.get(w).setSituation(Situation.EATED);
                        //net
                        changeSituation.add(Integer.toString(itemsList.get(w).getSerialNum()));
                        changeSituation.add("EATED");
                        ClientClass.getInstance().sent(ClientMessage.CHANGE_ITEM_SITUATION, changeSituation);
                        changeSituation.clear();
                        itemsList.get(w).setAllCenter(thisClientSnail.painter().centerX(), thisClientSnail.painter().top() - 30);
                        itemsList.get(w).setSnailRef(thisClientSnail);
                        itemsList.get(w).afterSnailEated();
                        thisClientSnail.isEat();
                        //net
                        ArrayList<String> itemAfterSnail = new ArrayList<>();
                        itemAfterSnail.add(Integer.toString(itemsList.get(w).getSerialNum()));
                        itemAfterSnail.add(Integer.toString(thisClientSnail.getSerialNum()));
                        ClientClass.getInstance().sent(ClientMessage.ITEM_AFTER_SNAIL, itemAfterSnail);
                    }
                }
            } else if (itemsList.get(w).getSituation() == Situation.EATED//吃到後
                    && thisClientSnail.getSnailHp() > 0) {
                if (itemsList.get(w) instanceof Defend) {
//                        thisClientSnail.unEat();
                    thisClientSnail.isSpeedUp();
                    if (itemsList.get(w).getState() == ItemsAnimator.State.REMOVE) {
                        thisClientSnail.unKeepHp();
                        thisClientSnail.unEat();
                        thisClientSnail.unSpeedUp();
                    }
                }
                if (itemsList.get(w) instanceof Bump) {
                    if (itemsList.get(w).getSnailRef() == null) {
                        itemsList.get(w).setSnailRef(thisClientSnail);
                        itemsList.get(w).afterSnailEated();
                        ArrayList<String> itemAfterSnail = new ArrayList<>();
                        itemAfterSnail.add(Integer.toString(itemsList.get(w).getSerialNum()));
                        itemAfterSnail.add(Integer.toString(thisClientSnail.getSerialNum()));
                        ClientClass.getInstance().sent(ClientMessage.ITEM_AFTER_SNAIL, itemAfterSnail);
                    }
                    if (itemsList.get(w).isCollederOutterCollision(thisClientSnail)
                            && itemsList.get(w).getSnailRef() != thisClientSnail) {//道具蝸牛碰撞
                        thisClientSnail.isCollision();
                        Vector CollisionVector = setCollisionVector(thisClientSnail.collider().centerX(), thisClientSnail.collider().centerY(),
                                itemsList.get(w).collider().centerX(), itemsList.get(w).collider().centerY());
                        Vector exploseMoveVector = exploseMoveVector(new Vector(thisClientSnail.getSpeedX(), thisClientSnail.getSpeedY()),
                                itemsList.get(w).getBoomV(), itemsList.get(w).getBoomPower(), CollisionVector);
                        thisClientSnail.setSpeedXY(exploseMoveVector.vx(), exploseMoveVector.vy());
                        ArrayList<String> sentVector = new ArrayList<>();
                        sentVector.add(Double.toString(exploseMoveVector.vx()));
                        sentVector.add(Double.toString(exploseMoveVector.vy()));
                        ClientClass.getInstance().sent(ClientMessage.SNAIL_SET_VECTOR, sentVector);

                    }
                    if (itemsList.get(w).getState() == ItemsAnimator.State.REMOVE) {
                        thisClientSnail.unEat();
                    }
                }

            }
            if (itemsList.get(w).getState() == ItemsAnimator.State.BOOM) {//道具爆炸
                thisClientSnail.unEat();
                if (itemsList.get(w).isCollederOutterCollision(thisClientSnail)) {//道具蝸牛碰撞
                    thisClientSnail.isCollision();
                    if (itemsList.get(w) instanceof MeteoriteRoll) {
                        thisClientSnail.setSpeedXY(itemsList.get(w).getBoomV().vx(), itemsList.get(w).getBoomV().vy());
                    } else if (itemsList.get(w) instanceof Meteorite) {
                        thisClientSnail.setSpeedXY(itemsList.get(w).getBoomV().vx(), itemsList.get(w).getBoomV().vy());
                    } else if (itemsList.get(w) instanceof BlackHole) {
                        Vector CollisionVector = setCollisionVector(thisClientSnail.collider().centerX(), thisClientSnail.collider().centerY(),
                                itemsList.get(w).collider().centerX(), itemsList.get(w).collider().centerY());
                        Vector exploseMoveVector = exploseMoveVector(new Vector(thisClientSnail.getSpeedX(), thisClientSnail.getSpeedY()),
                                itemsList.get(w).getBoomV(), itemsList.get(w).getBoomPower(), CollisionVector);
                        thisClientSnail.setSpeedXY(-exploseMoveVector.vx(), -exploseMoveVector.vy());
                    } else {
                        Vector CollisionVector = setCollisionVector(thisClientSnail.collider().centerX(), thisClientSnail.collider().centerY(),
                                itemsList.get(w).collider().centerX(), itemsList.get(w).collider().centerY());
                        Vector exploseMoveVector = exploseMoveVector(new Vector(thisClientSnail.getSpeedX(), thisClientSnail.getSpeedY()),
                                itemsList.get(w).getBoomV(), itemsList.get(w).getBoomPower(), CollisionVector);
                        thisClientSnail.setSpeedXY(exploseMoveVector.vx(), exploseMoveVector.vy());
                    }
                    //sent 
                    ArrayList<String> sentVector = new ArrayList<>();
                    sentVector.add(Double.toString(thisClientSnail.getSpeedX()));
                    sentVector.add(Double.toString(thisClientSnail.getSpeedY()));
                    ClientClass.getInstance().sent(ClientMessage.SNAIL_SET_VECTOR, sentVector);
                }
            }
            if (itemsList.get(w).getSituation() == Situation.EATED && thisClientSnail.getSnailHp() == 0) {//想要當蝸牛吃到炸彈但還沒丟出去時hp就=0的時候，炸彈消失，但沒成功??
                itemsList.get(w).setState(ItemsAnimator.State.REMOVE);
                changeState.add(Integer.toString(itemsList.get(w).getSerialNum()));
                changeState.add("REMOVE");
                ClientClass.getInstance().sent(ClientMessage.CHANGE_ITEM_STATE, changeState);
                changeState.clear();
            }
            if (itemsList.get(w).getState() == ItemsAnimator.State.REMOVE) {
                if (itemsList.get(w) instanceof BlackHole) {
                    thisClientSnail.setSpeedXY(2, 2);
                    ArrayList<String> sentVector = new ArrayList<>();
                    sentVector.add(Double.toString(thisClientSnail.getSpeedX()));
                    sentVector.add(Double.toString(thisClientSnail.getSpeedY()));
                    ClientClass.getInstance().sent(ClientMessage.SNAIL_SET_VECTOR, sentVector);
                }
                itemsList.remove(w);
            }
        }

        //蝸牛判斷死掉
        if (snails.size() > 1) {
            if (thisClientSnail.getSnailHp() == 0) {
                thisClientSnail.isEnd();
                double snailSurvivedTime = pastTime - changeToEndSceneTime;
                this.thisClientSnail.setsSnailSurvivedTime(snailSurvivedTime);
                ArrayList<String> snailEnd = new ArrayList<>();
                snailEnd.add(Integer.toString(thisClientSnail.getTotalHit()));
                snailEnd.add(Double.toString(snailSurvivedTime));
                ClientClass.getInstance().sent(ClientMessage.SNAIL_END, snailEnd);
                deadSnails.add(thisClientSnail);
                snails.remove(thisClientSnail);
                snailEnd.clear();
            }
        }
        //蝸牛判斷輸贏，出現輸或贏的圖片
        if (snails.size() <= 1 && pastTime > snailHpLimit / 10) {//依所設一開始的HP多寡來判斷最少多少時間算通關
            if (snails.size() == 1) {
                snails.get(0).isEnd();
                if (thisClientSnail.getSerialNum() == snails.get(0).getSerialNum() && changeToEndSceneTime >= 5) {
                    double snailSurvivedTime = pastTime - changeToEndSceneTime;
                    snails.get(0).setsSnailSurvivedTime(snailSurvivedTime);
                    ArrayList<String> snailEnd = new ArrayList<>();
                    snailEnd.add(Integer.toString(snails.get(0).getTotalHit()));
                    snailEnd.add(Double.toString(snailSurvivedTime));
                    ClientClass.getInstance().sent(ClientMessage.GAMEOVER, snailEnd);
                    SceneController.instance().change(new EndScene(thisClientSnail, snails.get(0), deadSnails, this.IP));
                }
            } else if (snails.size() == 0) {
                if (thisClientSnail.getSerialNum() == deadSnails.get(deadSnails.size() - 1).getSerialNum() && changeToEndSceneTime >= 5) {
                    double snailSurvivedTime = pastTime - changeToEndSceneTime;
                    deadSnails.get(deadSnails.size() - 1).setsSnailSurvivedTime(snailSurvivedTime);
                    ArrayList<String> snailEnd = new ArrayList<>();
                    snailEnd.add(Integer.toString(deadSnails.get(deadSnails.size() - 1).getTotalHit()));
                    snailEnd.add(Double.toString(snailSurvivedTime));
                    ClientClass.getInstance().sent(ClientMessage.GAMEOVER, snailEnd);
                    SceneController.instance().change(new EndScene(thisClientSnail, deadSnails.get(deadSnails.size() - 1), deadSnails, this.IP));
                }
            }
            if (thisClientSnail.getSnailHp() > 0) {
                gameResults = SceneController.instance().irc().tryGetImage(new Path().img().objs().youSurvived());
            } else if (thisClientSnail.getSnailHp() == 0) {
                gameResults = SceneController.instance().irc().tryGetImage(new Path().img().objs().youDied());
            }
            changeToEndSceneTime += 0.016;

        }
    }

    @Override
    public MouseCommandListener mouseListener() {
        return (MouseEvent e, MouseState state, long trigTime) -> {
            for (int i = 0; i < itemsList.size(); i++) {
                if (itemsList.get(i).getSnailRef() == this.thisClientSnail) {
                    itemsList.get(i).mouseTrig(cam.collider(), e, state, 0);
                }

            }
            //沒有吃到道具才可以發射技能
            if (!this.thisClientSnail.getIsEat()) {
                if (e != null) {
                    Snail target;
                    for (int i = 0; i < snails.size(); i++) {
                        if (overlap(e.getX(), e.getY(), snails.get(i))) {
                            target = snails.get(i);
                        }
                    }
                    target = null;
                    thisClientSnail.mouseTrig(target, this.cam.collider(), e, state, trigTime);
                }
                if (state == MouseState.PRESSED) {
                    Skill tmp = thisClientSnail.throwSkill();
                    if (tmp != null) {
                        sceneEmission.add(tmp);
                        AudioResourceController.getInstance().play(new Path().sounds().skills().skillsSwipe());
                        ArrayList<String> skillString = new ArrayList<>();
                        skillString.add(sceneEmission.get(this.sceneEmission.size() - 1).kindOfSkill());
                        skillString.add(Integer.toString(sceneEmission.get(this.sceneEmission.size() - 1).collider().centerX()));
                        skillString.add(Integer.toString(sceneEmission.get(this.sceneEmission.size() - 1).collider().centerY()));
                        skillString.add(Integer.toString(sceneEmission.get(this.sceneEmission.size() - 1).collider().radius()));
                        skillString.add(Integer.toString(sceneEmission.get(this.sceneEmission.size() - 1).skillPower()));
                        skillString.add(Double.toString(sceneEmission.get(this.sceneEmission.size() - 1).getSpeed().vx()));
                        skillString.add(Double.toString(sceneEmission.get(this.sceneEmission.size() - 1).getSpeed().vy()));
                        ClientClass.getInstance().sent(ClientMessage.ADD_SKILL, skillString);
                    }
                }
            }
        };
    }

    @Override
    public CommandSolver.KeyListener keyListener() {
        return new KeyListener() {
            @Override
            public void keyPressed(int commandCode, long trigTime) {
                thisClientSnail.keyPressed(commandCode, trigTime);
            }

            @Override
            public void keyReleased(int commandCode, long trigTime) {
                thisClientSnail.keyReleased(commandCode, trigTime);
            }

            @Override
            public void keyTyped(char c, long trigTime) {
                thisClientSnail.keyTyped(c, trigTime);
                switch (c) {
                    case KeyEvent.VK_Z:
                        for (int i = 0; i < skillDisplay.length; i++) {
                            skillDisplay[i].unHover();
                        }
                        skillDisplay[0].isHover();
                        break;
                    case KeyEvent.VK_X:
                        for (int i = 0; i < skillDisplay.length; i++) {
                            skillDisplay[i].unHover();
                        }
                        skillDisplay[1].isHover();
                        break;
                    case KeyEvent.VK_C:
                        for (int i = 0; i < skillDisplay.length; i++) {
                            skillDisplay[i].unHover();
                        }
                        skillDisplay[2].isHover();
                        break;
                }
            }
        };
    }

    public void sceneAddSkill(int serialNum, ArrayList<String> strs) {
        switch (strs.get(0)) {
            case "AttackSplit":
                this.sceneEmission.add(new AttackSplit(serialNum, strs.get(0),
                        Integer.parseInt(strs.get(1)), Integer.parseInt(strs.get(2)),
                        Integer.parseInt(strs.get(3)), Integer.parseInt(strs.get(4)),
                        new Vector(Double.parseDouble(strs.get(5)), Double.parseDouble(strs.get(6)))));
                break;
            case "AttackRange":
                this.sceneEmission.add(new AttackRange(serialNum, strs.get(0),
                        Integer.parseInt(strs.get(1)), Integer.parseInt(strs.get(2)),
                        Integer.parseInt(strs.get(3)), Integer.parseInt(strs.get(4)),
                        new Vector(Double.parseDouble(strs.get(5)), Double.parseDouble(strs.get(6)))));
                break;
            case "AttackForce":
                this.sceneEmission.add(new AttackForce(serialNum, strs.get(0),
                        Integer.parseInt(strs.get(1)), Integer.parseInt(strs.get(2)),
                        Integer.parseInt(strs.get(3)), Integer.parseInt(strs.get(4)),
                        new Vector(Double.parseDouble(strs.get(5)), Double.parseDouble(strs.get(6)))));
                break;
            case "EnrichBlood":
                this.sceneEmission.add(new EnrichBlood(strs.get(0),
                        Integer.parseInt(strs.get(1)), Integer.parseInt(strs.get(2)),
                        Integer.parseInt(strs.get(3)),
                        new Vector(Double.parseDouble(strs.get(5)), Double.parseDouble(strs.get(6)))));
                break;
            case "Teleportation":
                this.sceneEmission.add(new Teleportation(strs.get(0),
                        Integer.parseInt(strs.get(1)), Integer.parseInt(strs.get(2)),
                        Integer.parseInt(strs.get(3)),
                        new Vector(Double.parseDouble(strs.get(5)), Double.parseDouble(strs.get(6)))));
                break;
            case "Velocity":
                this.sceneEmission.add(new Velocity(strs.get(0),
                        Integer.parseInt(strs.get(1)), Integer.parseInt(strs.get(2)),
                        Integer.parseInt(strs.get(3)),
                        new Vector(Double.parseDouble(strs.get(5)), Double.parseDouble(strs.get(6)))));
                break;
            case "Boomerang":
                for (int i = 0; i < snails.size(); i++) {
                    if (serialNum == snails.get(i).getSerialNum()) {
                        this.sceneEmission.add(new Boomerang(serialNum, strs.get(0), snails.get(i),
                                Integer.parseInt(strs.get(1)), Integer.parseInt(strs.get(2)),
                                Integer.parseInt(strs.get(3)), Integer.parseInt(strs.get(4)),
                                new Vector(Double.parseDouble(strs.get(5)), Double.parseDouble(strs.get(6)))));
                        return;
                    }
                }
                break;
//            case "Track":
//                this.sceneEmission.add(new Track(strs.get(0),
//                        Integer.parseInt(strs.get(1)), Integer.parseInt(strs.get(2)),
//                        Integer.parseInt(strs.get(3)),
//                        new Vector(Double.parseDouble(strs.get(5)), Double.parseDouble(strs.get(6)))));
//                break;
        }
    }

    public boolean overlap(int x, int y, Snail snail) {
        int r = snail.collider().radius();
        int distance = (int) (Math.sqrt(Math.pow(x - snail.collider().centerX(), 2) + Math.pow(y - snail.collider().centerY(), 2)));
        return distance <= r;
    }
}
