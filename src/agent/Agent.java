package agent;

import javafx.scene.paint.Color;
import map.Map;
import messaging.Message;
import messaging.MessageBox;
import messaging.MessageType;
import utils.Direction;
import utils.Position;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class Agent extends Thread {

    private Map map;
    private MessageBox messageBox;
    private Position current;
    private Position endPoint;
    private int idAgent;
    private Color agentColor = Color.GRAY;
    private ArrayList<Message> agentBox;

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

    @Override
    public void run() {
        super.run();
        synchronized (this) {
            while (true) {
                agentBox = messageBox.getBox().get(this.idAgent);
                if (agentBox.size() != 0){
                    Message message = agentBox.get(0);
                    if (message.getMessageType().equals(MessageType.REQUEST)){

                    }
                }
                if (current.isEqual(endPoint)) {
                    // TODO : Move si y'a des demandes
                    System.out.println("Agent "+this.getIdAgent()+" Arrived");
                } else {
                    Position pos = getWorthAvailableMove();
                    if (pos != null) {
                        this.move(posToDir(pos));
                    }
                }
                try {
                    this.wait(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                notifyAll();
            }
        }
    }

    /**
     *  Movement Methods
     */
    public void move(Direction direction){
        Position tempPos = new Position();
        switch (direction){
            case LEFT:
                if(isMoveAvailable(direction)) {
                    tempPos.setX(current.getX());
                    tempPos.setY(current.getY() - 1);
                    if(map.move(current, tempPos)){ // On tente de bouger sur la grille
                        System.out.println("Agent "+this.getIdAgent()+ " : Moving LEFT");
                        current = tempPos;
                    }
                }
                break;
            case DOWN:
                if(isMoveAvailable(direction)) {
                    tempPos.setX(current.getX()+1);
                    tempPos.setY(current.getY());
                    if(map.move(current, tempPos)){
                        System.out.println("Agent "+this.getIdAgent()+ " : Moving DOWN");
                        current = tempPos;
                    }
                }
                break;
            case RIGHT:
                if(isMoveAvailable(direction)) {
                    tempPos.setX(current.getX());
                    tempPos.setY(current.getY() + 1);
                    if(map.move(current, tempPos)){
                        System.out.println("Agent "+this.getIdAgent()+ " : Moving RIGHT");
                        current = tempPos;
                    }
                }
                break;
            case UP:
                if(isMoveAvailable(direction)) {
                    tempPos.setX(current.getX() - 1);
                    tempPos.setY(current.getY());
                    if(map.move(current, tempPos)){
                        System.out.println("Agent "+this.getIdAgent()+ " : Moving UP");
                        current = tempPos;
                    }
                }
                break;
            default:
                    // ne bouge pas
                break;
        }
    }

    public java.util.Map<Position, Double> getWeightAvailableMoves(){
        List<Position> positions = getAvailableMoves();
        HashMap<Position, Double> posWeight = new HashMap<>();
        for (Position pos : positions) {
            posWeight.put(pos, pos.distEuclidienne(getGoalPosition()));
        }
        return posWeight;
    }

    public Position getWorthAvailableMove(){
        List<Position> positions = getAvailableMoves();
        Comparator<Position> ComparePosToEndPoint = Comparator.comparing(p -> p.distEuclidienne(endPoint));
        return positions.stream().min(ComparePosToEndPoint).get();
    }

    public Direction posToDir(Position position){
        int differenceX = this.current.getX() - position.getX();
        int differenceY = this.current.getY() - position.getY();

        if (Math.abs(differenceX) >= Math.abs(differenceY)){ // Déplacement axe x
            if (differenceX > 0){
                return Direction.UP;
            }
            else {
                return Direction.DOWN;
            }
        }
        else { // Déplacement axe y
            if (differenceY > 0){
                return Direction.LEFT;
            }
            else {
                return Direction.RIGHT;
            }
        }
    }

    public List<Position> getAvailableMoves(){
        List<Position> positions = new ArrayList<>();
        if(isMoveAvailable(Direction.DOWN)){
            positions.add(new Position(current.getX()+1, current.getY()));
        }
        if(isMoveAvailable(Direction.RIGHT)){
            positions.add(new Position(current.getX(), current.getY()+1));
        }
        if(isMoveAvailable(Direction.UP)) {
            positions.add(new Position(current.getX()-1, current.getY()));
        }
        if(isMoveAvailable(Direction.LEFT)){
            positions.add(new Position(current.getX(), current.getY()-1));
        }
        return positions;
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

    public boolean isMoveAvailable(Direction direction) {
        Position tempPos = new Position();
        Agent agent;
        switch (direction){
            case LEFT:
                tempPos.setX(current.getX());
                tempPos.setY(current.getY()-1);
                if (tempPos.getY() < 0 ) {
                    return false;
                }
                if ((agent = map.getPosition(tempPos)) != null) {
                    messageBox.sendMessage(agent.getIdAgent(), new Message(this, agent, MessageType.REQUEST, tempPos));
                    return false;
                }
                return true;

            case DOWN:
                tempPos.setX(current.getX()+1);
                tempPos.setY(current.getY());
                if (tempPos.getX() >= map.getSize()) {
                    return false;
                }
                if ((agent = map.getPosition(tempPos)) != null) {
                    messageBox.sendMessage(agent.getIdAgent(), new Message(this, agent, MessageType.REQUEST, tempPos));
                    return false;
                }
                return true;

            case RIGHT:
                tempPos.setX(current.getX());
                tempPos.setY(current.getY()+1);
                if (tempPos.getY() >= map.getSize()) {
                    return false;
                }
                if ((agent = map.getPosition(tempPos)) != null) {
                    messageBox.sendMessage(agent.getIdAgent(), new Message(this, agent, MessageType.REQUEST, tempPos));
                    return false;
                }
                return true;

            case UP:
                tempPos.setX(current.getX()-1);
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

    /**
     *  Setters and Getters
     */
    public int getIdAgent() {
        return idAgent;
    }

    public Color getAgentColor() {
        return agentColor;
    }

    public void setAgentColor(Color agentColor) {
        this.agentColor = agentColor;
    }

    public void setCurrentPosition(Position current) {
        this.current = current;
    }

    public void setEndPoint(Position endPoint) {
        this.endPoint = endPoint;
    }

    public Position getCurrentPosition() { return current; }

    public Position getGoalPosition() { return endPoint; }
}
