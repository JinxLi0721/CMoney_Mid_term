package snailstrike.gameobj;

public class ExploseMove {

    private double moveX;//被撞的位移X
    private double moveY;//被撞的位移Y
    private Vector targetV;//被撞的位置
    private Vector propsV;//給予彈出威力的道具位置
    private Vector newV;//bombV+mouseV，只用來除 移動時要多久到達
    private Vector totalDis;//位移xy的總距離
    private Vector boomV;//爆炸時的位置
    private boolean isBombMoveStart;
    private double boomPower;//彈走的距離
    private double friction = 0;//停止的速度(越大滑越慢)

    public ExploseMove(double boomPower) {
        this.boomPower = boomPower;
        newV = new Vector();
        moveX = 0;
        moveY = 0;
        totalDis = new Vector();
        targetV = new Vector();
        propsV = new Vector();
        isBombMoveStart = false;
    }

    public void setAllV(Vector targetV, Vector propsV) {
        this.targetV = targetV;
        this.propsV = propsV;
        newV = newV.zero().add(targetV).add(propsV);
    }

    public void setTargetV(Vector targetV) {
        this.targetV = targetV;
        newV = newV.zero().add(targetV).add(propsV);
    }

    public void setPropsY(Vector propsV) {
        this.propsV = propsV;
        newV = newV.zero().add(targetV).add(propsV);
    }

    public void setBombMoveSlowDownStart() {
        isBombMoveStart = true;
    }
    
    public boolean isBombMoveSlowDownStart() {
        return isBombMoveStart;
    }

    public double getMoveX() {
        return moveX;
    }

    public double getMoveY() {
        return moveY;
    }

    public Vector getTargetV() {
        return targetV;
    }

    public Vector getPropsV() {
        return propsV;
    }

    public Vector getTotalDis() {
        return totalDis;
    }

    public void boomMoveSpeedSlow() {//慢慢停止(不知道怎麼算與炸彈的距離遠近，來改變蝸牛滑走的距離
        Vector tmp = new Vector().add(this.targetV).sub(this.propsV);
        if (totalDis.length() <= tmp.length()*boomPower) {
            
            moveX = tmp.normalize().vx() * (400 / (3 + friction));
            moveY = tmp.normalize().vy() * (400 / (3 + friction));
            friction += 3;//越大，停下速度越慢
               System.out.println("tmp.length"+tmp.length()+"totalDis.length()"+totalDis.length());
        }   else{
            isBombMoveStart = false;
        }
        this.totalDis = totalDis.add(new Vector(Math.abs(moveX), Math.abs(moveY)));
//        if (Math.sqrt((targetV.sub(propsV).lengthSqaure())) < 500) {
//            if (totalDis.length() >= boomPower * 5) {
//                isBombMoveStart = false;
//                System.out.println("totalDis.length"+totalDis.length());
//            }
//        } else if (Math.sqrt((targetV.sub(propsV).lengthSqaure())) >= 500 && Math.sqrt((targetV.sub(propsV).lengthSqaure())) < 550) {
//            if (totalDis.length() >= boomPower) {
//                isBombMoveStart = false;
//                 System.out.println("tmp.length"+tmp.length());
//            }
//        }
    }

    public void bombMoveSpeedUp() {//慢慢加速
        totalDis.setVectorXY(totalDis.vx() + Math.abs(moveX), totalDis.vy() + Math.abs(moveY));
//        newV = targetV.add(propsV);
        if (totalDis.length() <= targetV.sub(propsV).length()) {
            moveX += (targetV.vx() - propsV.vx()) / newV.length();  
            moveY += (targetV.vy() - propsV.vy()) / newV.length();
        }
        if (totalDis.length() != 0) {
            if (totalDis.length() >= Math.sqrt((targetV.sub(propsV).lengthSqaure()))) {
                isBombMoveStart = false;
            }
        }
    }

    public void bombMoveSlowDown() {//先加速-->再減速-->停止
//         newV = targetV.add(propsV);
        if (totalDis.length() <= targetV.sub(propsV).length() * boomPower / 2) {
            moveX += (targetV.vx() - propsV.vx()) / newV.length();
            moveY += (targetV.vy() - propsV.vy()) / newV.length();
        } else {
            moveX += (propsV.vx() - targetV.vx()) / newV.length();
            moveY += (propsV.vy() - targetV.vy()) / newV.length();
        }
        totalDis.setVectorXY(totalDis.vx() + Math.abs(moveX), totalDis.vy() + Math.abs(moveY));
        if (totalDis.length() != 0) {
            if (totalDis.length() >= targetV.sub(propsV).length() * boomPower) {
                isBombMoveStart = false;
            }
        }
    }
}
