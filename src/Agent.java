import map.Map;
import utils.Direction;
import utils.Position;

public class Agent extends Thread {

    private Map map;
    private Position current;
    private Position endPoint;

    public Agent(Map map){
        this.map = map;
    }

    private void move(Direction direction){
        switch (direction){
            case UP:
                if(isMoveAvailable(direction))
                    current.setY(current.getY()-1);
                break;
            case RIGHT:
                if(isMoveAvailable(direction))
                    current.setX(current.getX()+1);
                break;
            case DOWN:
                if(isMoveAvailable(direction))
                    current.setY(current.getY()+1);
                break;
            case LEFT:
                if(isMoveAvailable(direction))
                    current.setX(current.getX()-1);
                break;
            default:
                    // ne bouge pas
                break;
        }
    }

    private boolean isMoveAvailable(Direction direction){
        Position tempPos = new Position();
        switch (direction){
            case UP:
                tempPos.setX(current.getX());
                tempPos.setY(current.getY()-1);
                return tempPos.getY() >= 0 && map.getPosition(tempPos) == null;

            case RIGHT:
                tempPos.setX(current.getX()+1);
                tempPos.setY(current.getY());
                return tempPos.getX() < map.getSize() && map.getPosition(tempPos) == null;

            case DOWN:
                tempPos.setX(current.getX());
                tempPos.setY(current.getY()+1);
                return tempPos.getY() < map.getSize() && map.getPosition(tempPos) == null;

            case LEFT:
                tempPos.setX(current.getX()-1);
                tempPos.setY(current.getY());
                return tempPos.getX() >= 0 && map.getPosition(tempPos) == null;

            default:
                return false;
        }
    }

}
