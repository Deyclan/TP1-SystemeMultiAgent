package agent;

import map.Map;
import messaging.Message;
import messaging.MessageBox;
import messaging.MessageType;
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
        Agent agent;
        switch (direction){
            case UP:
                tempPos.setX(current.getX());
                tempPos.setY(current.getY()-1);
                if (tempPos.getY() < 0 ) {
                    return false;
                }
                if ((agent = map.getPosition(tempPos)) != null){
                    messageBox.sendMessage(agent.getIdAgent(), new Message(this, agent, MessageType.REQUEST, tempPos));
                    return false;
                }
                return true;

            case RIGHT:
                tempPos.setX(current.getX()+1);
                tempPos.setY(current.getY());
                if (tempPos.getX() >= map.getSize() ) {
                    return false;
                }
                if ((agent = map.getPosition(tempPos)) != null){
                    messageBox.sendMessage(agent.getIdAgent(), new Message(this, agent, MessageType.REQUEST, tempPos));
                    return false;
                }
                return true;

            case DOWN:
                tempPos.setX(current.getX());
                tempPos.setY(current.getY()+1);
                if (tempPos.getY() >= map.getSize()) {
                    return false;
                }
                if ((agent = map.getPosition(tempPos)) != null){
                    messageBox.sendMessage(agent.getIdAgent(), new Message(this, agent, MessageType.REQUEST, tempPos));
                    return false;
                }
                return true;

            case LEFT:
                tempPos.setX(current.getX()-1);
                tempPos.setY(current.getY());
                if (tempPos.getX() < 0 ) {
                    return false;
                }
                if ((agent = map.getPosition(tempPos)) != null){
                    messageBox.sendMessage(agent.getIdAgent(), new Message(this, agent, MessageType.REQUEST, tempPos));
                    return false;
                }
                return true;

            default:
                return false;
        }
    }

    public int getIdAgent() {
        return idAgent;
    }
}
