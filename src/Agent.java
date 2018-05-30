import map.Map;
import messaging.MessageBox;
import utils.Direction;
import utils.Position;

public class Agent extends Thread {

    private Map map;
    private MessageBox messageBox;
    private Position current;
    private Position endPoint;
    private int idAgent;

    public Agent(int idAgent, Map map, MessageBox messageBox){
        this.idAgent = idAgent;
        this.map = map;
        this.messageBox = messageBox;
    }

    private void move(Direction direction){
        Position tempPos = new Position();
        switch (direction){
            case UP:
                if(isMoveAvailable(direction)) {
                    tempPos.setX(current.getX());
                    tempPos.setY(current.getY() - 1);
                    if(map.move(current, tempPos)){ // On tente de bouger sur la grille
                        current = tempPos;
                    }
                }
                break;
            case RIGHT:
                if(isMoveAvailable(direction)) {
                    tempPos.setX(current.getX()+1);
                    tempPos.setY(current.getY());
                    if(map.move(current, tempPos)){
                        current = tempPos;
                    }
                }
                break;
            case DOWN:
                if(isMoveAvailable(direction)) {
                    tempPos.setX(current.getX());
                    tempPos.setY(current.getY() + 1);
                    if(map.move(current, tempPos)){
                        current = tempPos;
                    }
                }
                break;
            case LEFT:
                if(isMoveAvailable(direction)) {
                    tempPos.setX(current.getX() - 1);
                    tempPos.setY(current.getY());
                    if(map.move(current, tempPos)){
                        current = tempPos;
                    }
                }
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
