package snailstrike.gameobj;

public class Coordinate {
    private double x;
    private double y;
    
    public Coordinate(double x, double y){
        this.x = x;
        this.y = y;
    }
    
    public Coordinate(){
        this.x = 0;
        this.y = 0;
    }
    
    public Coordinate add(Coordinate c){
        return new Coordinate(this.x + c.x, this.y + c.y);
    }
    
    public Coordinate sub(Coordinate c){
        return new Coordinate(this.x - c.x, this.y - c.y);
    }
    
    public void setX(double x){
        this.x = x;
    }
    
    public void setY(double y){
        this.y = y;
    }
}
