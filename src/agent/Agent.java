package agent;

import javafx.scene.paint.Color;
import map.Map;
import messaging.Message;
import messaging.MessageBox;
import messaging.MessageType;
import utils.Direction;
import utils.Position;

import java.util.ArrayList;
import java.util.List;

public class Agent extends Thread {

    private Map map;
    private MessageBox messageBox;
    private Position current;
    private Position endPoint;
    private int idAgent;
    private Color agentColor = Color.GRAY;

    public Agent(int idAgent, Map map, MessageBox messageBox) {
        this.idAgent = idAgent;
        this.map = map;
        this.messageBox = messageBox;
    }

    public Agent(int idAgent, Map map, MessageBox messageBox, Color color) {
        this.idAgent = idAgent;
        this.map = map;
        this.messageBox = messageBox;
        this.agentColor = color;
    }

    public boolean canMoveToPosi(Position position) {
        Direction direction = null;
        if (this.getCurrentPosition().getY() > position.getY())
            direction = Direction.UP;
        if (this.getCurrentPosition().getX() < position.getX())
            direction = Direction.RIGHT;
        if (this.getCurrentPosition().getY() < position.getY())
            direction = Direction.DOWN;
        if (this.getCurrentPosition().getX() > position.getX())
            direction = Direction.LEFT;

        return isMoveAvailable(direction);
    }

    public List<Position> getAdjacentPositions() {
        List<Position> adjacentPos = new ArrayList<>();
        // Up
        Position tempPos1 = new Position();
        tempPos1.setX(current.getX());
        tempPos1.setY(current.getY() - 1);
        if (tempPos1.getY() >= 0)
            adjacentPos.add(tempPos1);
        // Right
        Position tempPos2 = new Position();
        tempPos2.setX(current.getX() + 1);
        tempPos2.setY(current.getY());
        if (tempPos2.getX() < map.getSize())
            adjacentPos.add(tempPos2);
        // Down
        Position tempPos3 = new Position();
        tempPos3.setX(current.getX());
        tempPos3.setY(current.getY() + 1);
        if (tempPos3.getY() < map.getSize())
            adjacentPos.add(tempPos3);
        // Left
        Position tempPos4 = new Position();
        tempPos4.setX(current.getX() - 1);
        tempPos4.setY(current.getY());
        if (tempPos4.getX() >= 0)
            adjacentPos.add(tempPos4);
        return adjacentPos;
    }

    private boolean isMoveAvailable(Direction direction) {
        Position tempPos = new Position();
        Agent agent;
        switch (direction) {
            case UP:
                tempPos.setX(current.getX());
                tempPos.setY(current.getY() - 1);
                if (tempPos.getY() < 0) {
                    return false;
                }
                if ((agent = map.getPosition(tempPos)) != null) {
                    messageBox.sendMessage(agent.getIdAgent(), new Message(this, agent, MessageType.REQUEST, tempPos));
                    return false;
                }
                return true;

            case RIGHT:
                tempPos.setX(current.getX() + 1);
                tempPos.setY(current.getY());
                if (tempPos.getX() >= map.getSize()) {
                    return false;
                }
                if ((agent = map.getPosition(tempPos)) != null) {
                    messageBox.sendMessage(agent.getIdAgent(), new Message(this, agent, MessageType.REQUEST, tempPos));
                    return false;
                }
                return true;

            case DOWN:
                tempPos.setX(current.getX());
                tempPos.setY(current.getY() + 1);
                if (tempPos.getY() >= map.getSize()) {
                    return false;
                }
                if ((agent = map.getPosition(tempPos)) != null) {
                    messageBox.sendMessage(agent.getIdAgent(), new Message(this, agent, MessageType.REQUEST, tempPos));
                    return false;
                }
                return true;

            case LEFT:
                tempPos.setX(current.getX() - 1);
                tempPos.setY(current.getY());
                if (tempPos.getX() < 0) {
                    return false;
                }
                if ((agent = map.getPosition(tempPos)) != null) {
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

    public Color getAgentColor() {
        return agentColor;
    }

    public void setAgentColor(Color agentColor) {
        this.agentColor = agentColor;
    }

    public void setEndPoint(Position endPoint) {
        this.endPoint = endPoint;
    }

    public Position getCurrentPosition() {
        return current;
    }

    public void setCurrentPosition(Position current) {
        this.current = current;
    }

    public Position getGoalPosition() {
        return endPoint;
    }
}
