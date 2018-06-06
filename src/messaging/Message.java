package messaging;


import agent.Agent;
import utils.Position;

public class Message {

    private Agent from;
    private Agent to;
    private MessageType messageType;
    private Position posToFree;

    public Message(Agent from, Agent to, MessageType type, Position posToFree) {
        this.from = from;
        this.to = to;
        this.messageType = type;
        this.posToFree = posToFree;
    }


}
