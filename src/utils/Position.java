package utils;

public class Position {

    private int x;
    private int y;

    public Position(){
        this.x=0;
        this.y=0;
    }
    public Position(int x, int y){
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }
    public void setY(int y) {
        this.y = y;
    }

    public boolean isEqual(Position position){
        return position.getY() == this.getY() && position.getX() == this.getX();
    }
    public double distEuclidienne(Position position){
        return Math.sqrt(Math.pow(this.getX() + position.getX(), 2)+Math.pow(this.getY() + position.getY(), 2));
    }
}
