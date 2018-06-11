package agent;

import algos.MARTA;
import javafx.scene.paint.Color;
import map.Map;
import messaging.Message;
import messaging.MessageBox;
import messaging.MessageType;
import utils.Direction;
import utils.Position;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class Agent extends Thread {

    private Map map;
    private MessageBox messageBox;
    private Position currentPosition;
    private Position endPoint;
    private int idAgent;
    private Color agentColor = Color.GRAY;
    private String name;
    private List<Message> agentBox;
    private boolean arrive;
    private boolean isCorner;

    private int distToBorder;

    private Random random = new Random();

    private static final int WAITING_TIME = 1000;
    private int waitingTime = WAITING_TIME;

    public Agent(int idAgent, Map map, MessageBox messageBox) {
        this.idAgent = idAgent;
        this.map = map;
        this.messageBox = messageBox;
        this.arrive = false;
    }

    public Agent(int idAgent, Map map, MessageBox messageBox, Color color) {
        this.idAgent = idAgent;
        this.map = map;
        this.messageBox = messageBox;
        this.agentColor = color;
        this.arrive = false;
    }

    public Agent(int idAgent, Map map, MessageBox messageBox, Color color, String name) {
        this.idAgent = idAgent;
        this.map = map;
        this.messageBox = messageBox;
        this.agentColor = color;
        this.name = name;
        this.arrive = false;
    }

    @Override
    public synchronized void run() {
        super.run();
        synchronized (this) {
            try {
                MARTA marta = new MARTA();
                if (isCorner){
                    int unLooper = 0; // Doit permettre d'éviter de rester bloquer dans une boucle
                    while (!marta.getSolved() || distToBorder > Map.distLock) {

                        if (distToBorder <= Map.distLock){
                            waitingTime = (int) WAITING_TIME/4;
                        } else waitingTime = WAITING_TIME;

                        boolean hasMoved = false;
                        synchronized (messageBox) {
                            agentBox = new ArrayList<>(messageBox.getBox().get(getIdAgent()));
                        }
                        if (agentBox.size() > 0 && distToBorder > Map.distLock && unLooper > 20) {
                            unLooper = 0;
                            List<Message> requests = agentBox.stream().filter(message -> message.getMessageType() == MessageType.REQUEST).collect(Collectors.toList());
                            for (Message m : requests) {
                                if (m.getPosToFree().isEqual(currentPosition)) {
                                    List<Position> available = getAvailableMoves();
                                    List<Direction> availableDir = available.stream().map(this::posToDir).collect(Collectors.<Direction>toList());
                                    if (availableDir.size() > 0) {
                                        this.move(availableDir.get(0));
                                        if (currentPosition.isEqual(endPoint)){
                                            marta.setSolved(true);
                                        }
                                        hasMoved = true;
                                        synchronized (messageBox) {
                                            messageBox.sendMessage(m.getFrom().getIdAgent(), new Message(this, m.getFrom(), MessageType.RESPONSE, null));
                                            messageBox.deleteMessage(this.getIdAgent(), m);
                                        }
                                        Thread.sleep( waitingTime + random.nextInt(waitingTime));
                                    }
                                } else {
                                    synchronized (messageBox) {
                                        messageBox.getBox().get(getIdAgent()).remove(m);
                                    }
                                }
                            }
                        }
                        unLooper++;
                        Thread.sleep(waitingTime + random.nextInt(waitingTime));
                        if (!hasMoved) {
                            marta.solvePuzzle(this);
                        }
                        Thread.sleep(100);
                    }
                }
                else {
                    synchronized (map){

                    }
                    while (!marta.getSolved() || distToBorder > Map.distLock || !Map.isCornersOk()) {

                        if (distToBorder <= Map.distLock){
                            waitingTime = (int) WAITING_TIME/4;
                        } else waitingTime = WAITING_TIME;

                        boolean hasMoved = false;
                        synchronized (messageBox) {
                            agentBox = new ArrayList<>(messageBox.getBox().get(getIdAgent()));
                        }
                        if (agentBox.size() > 0) {
                            List<Message> requests = new ArrayList<>(agentBox.stream().filter(message -> message.getMessageType() == MessageType.REQUEST).collect(Collectors.toList()));
                            for (Message m : requests) {
                                if (m.getPosToFree().isEqual(currentPosition)) {
                                    List<Position> available = getAvailableMoves();
                                    List<Direction> availableDir = available.stream().map(this::posToDir).collect(Collectors.<Direction>toList());
                                    if (availableDir.size() > 0) {
                                        this.move(availableDir.get(0));
                                        if (currentPosition.isEqual(endPoint)){
                                            marta.setSolved(true);
                                        }
                                        hasMoved = true;
                                        synchronized (messageBox) {
                                            messageBox.sendMessage(m.getFrom().getIdAgent(), new Message(this, m.getFrom(), MessageType.RESPONSE, null));
                                            messageBox.deleteMessage(this.getIdAgent(), m);
                                        }
                                        Thread.sleep(waitingTime + random.nextInt(waitingTime) + 0);
                                    }
                                } else {
                                    synchronized (messageBox) {
                                        messageBox.getBox().get(getIdAgent()).remove(m);
                                    }
                                }
                            }
                        }
                        Thread.sleep(waitingTime + random.nextInt(waitingTime));
                        if (!hasMoved) {
                            marta.solvePuzzle(this);
                        }
                        Thread.sleep(100);
                    }
                }
                this.arrive = true;
                Map.checkLocker();
                System.out.println(String.format("Agent %d ( %s ) Arrivé et locké", idAgent, name));


            }catch (Exception e){
                e.printStackTrace();
                run();
            }
        }
    }

    /**
     *  Movement Methods
     */
    public void move(Direction direction){
        getMessageBox().deleteAll(getIdAgent());
        Position tempPos = new Position();
        switch (direction){
            case LEFT:
                if(isMoveAvailable(direction)) {
                    tempPos.setX(currentPosition.getX());
                    tempPos.setY(currentPosition.getY() - 1);
                    if(map.move(currentPosition, tempPos)){ // On tente de bouger sur la grille
                        //System.out.println("Agent "+this.getIdAgent()+ " : Moving LEFT");
                        currentPosition = tempPos;
                    }
                }
                break;
            case DOWN:
                if(isMoveAvailable(direction)) {
                    tempPos.setX(currentPosition.getX()+1);
                    tempPos.setY(currentPosition.getY());
                    if(map.move(currentPosition, tempPos)){
                        //System.out.println("Agent "+this.getIdAgent()+ " : Moving DOWN");
                        currentPosition = tempPos;
                    }
                }
                break;
            case RIGHT:
                if(isMoveAvailable(direction)) {
                    tempPos.setX(currentPosition.getX());
                    tempPos.setY(currentPosition.getY() + 1);
                    if(map.move(currentPosition, tempPos)){
                        //System.out.println("Agent "+this.getIdAgent()+ " : Moving RIGHT");
                        currentPosition = tempPos;
                    }
                }
                break;
            case UP:
                if(isMoveAvailable(direction)) {
                    tempPos.setX(currentPosition.getX() - 1);
                    tempPos.setY(currentPosition.getY());
                    if(map.move(currentPosition, tempPos)){
                        //System.out.println("Agent "+this.getIdAgent()+ " : Moving UP");
                        currentPosition = tempPos;
                    }
                }
                break;
            default:
                    // ne bouge pas
                break;
        }
    }

    public void move(Position position){
        move(posToDir(position));
    }

    public Direction posToDir(Position position){
        int differenceX = this.currentPosition.getX() - position.getX();
        int differenceY = this.currentPosition.getY() - position.getY();

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
            positions.add(new Position(currentPosition.getX()+1, currentPosition.getY()));
        }
        if(isMoveAvailable(Direction.RIGHT)){
            positions.add(new Position(currentPosition.getX(), currentPosition.getY()+1));
        }
        if(isMoveAvailable(Direction.UP)) {
            positions.add(new Position(currentPosition.getX()-1, currentPosition.getY()));
        }
        if(isMoveAvailable(Direction.LEFT)){
            positions.add(new Position(currentPosition.getX(), currentPosition.getY()-1));
        }
        return positions;
    }

    public List<Position> getAdjacentPositions() {
        List<Position> adjacentPos = new ArrayList<>();
        // Up
        Position tempPos1 = new Position();
        tempPos1.setX(currentPosition.getX());
        tempPos1.setY(currentPosition.getY() - 1);
        if (tempPos1.getY() >= 0)
            adjacentPos.add(tempPos1);
        // Right
        Position tempPos2 = new Position();
        tempPos2.setX(currentPosition.getX() + 1);
        tempPos2.setY(currentPosition.getY());
        if (tempPos2.getX() < map.getSize())
            adjacentPos.add(tempPos2);
        // Down
        Position tempPos3 = new Position();
        tempPos3.setX(currentPosition.getX());
        tempPos3.setY(currentPosition.getY() + 1);
        if (tempPos3.getY() < map.getSize())
            adjacentPos.add(tempPos3);
        // Left
        Position tempPos4 = new Position();
        tempPos4.setX(currentPosition.getX() - 1);
        tempPos4.setY(currentPosition.getY());
        if (tempPos4.getX() >= 0)
            adjacentPos.add(tempPos4);
        return adjacentPos;
    }

    public boolean isMoveAvailable(Direction direction) {
        Position tempPos = new Position();
        Agent agent;
        switch (direction){
            case LEFT:
                tempPos.setX(currentPosition.getX());
                tempPos.setY(currentPosition.getY()-1);
                if (tempPos.getY() < 0 ) {
                    return false;
                }
                if ((agent = map.getPosition(tempPos)) != null) {
                    messageBox.sendMessage(agent.getIdAgent(), new Message(this, agent, MessageType.REQUEST, tempPos));
                    return false;
                }
                return true;

            case DOWN:
                tempPos.setX(currentPosition.getX()+1);
                tempPos.setY(currentPosition.getY());
                if (tempPos.getX() >= map.getSize()) {
                    return false;
                }
                if ((agent = map.getPosition(tempPos)) != null) {
                    messageBox.sendMessage(agent.getIdAgent(), new Message(this, agent, MessageType.REQUEST, tempPos));
                    return false;
                }
                return true;

            case RIGHT:
                tempPos.setX(currentPosition.getX());
                tempPos.setY(currentPosition.getY()+1);
                if (tempPos.getY() >= map.getSize()) {
                    return false;
                }
                if ((agent = map.getPosition(tempPos)) != null) {
                    messageBox.sendMessage(agent.getIdAgent(), new Message(this, agent, MessageType.REQUEST, tempPos));
                    return false;
                }
                return true;

            case UP:
                tempPos.setX(currentPosition.getX()-1);
                tempPos.setY(currentPosition.getY());
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

    public boolean isMoveAvailable(Position position) {
        Direction direction = posToDir(position);
        return isMoveAvailable(direction);
    }

    public Agent getAgentToMove(Position position){
        return map.getPosition(position);
    }

    private int minDistToBorder(){
        ArrayList<Integer> i = new ArrayList<>();
        i.add(endPoint.getX());
        i.add(endPoint.getY());
        i.add((map.getSize()-1 - endPoint.getX()));
        i.add((map.getSize()-1 - endPoint.getY()));
        if ((i.get(0).equals(i.get(1)) && i.get(2).equals(i.get(3)))
                || (i.get(1).equals(i.get(2)) && i.get(0).equals(i.get(3)))){
            isCorner = true;
        }
        return i.stream().mapToInt(integer->integer).min().getAsInt();
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
        this.currentPosition = current;
    }

    public void setEndPoint(Position endPoint) {
        this.endPoint = endPoint;
        this.distToBorder = minDistToBorder();
    }

    public Position getCurrentPosition() { return currentPosition; }

    public Position getGoalPosition() { return endPoint; }

    public String getAgentName() {
        return name;
    }

    public MessageBox getMessageBox() {
        return messageBox;
    }

    public Map getMap() {
        return map;
    }

    public boolean isArrive(){
        return arrive;
    }

    public int getDistToBorder() {
        return distToBorder;
    }

    public boolean isCorner() {
        return isCorner;
    }
}
