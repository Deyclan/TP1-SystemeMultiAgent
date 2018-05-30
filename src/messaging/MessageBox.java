package messaging;

import agent.Agent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MessageBox {

    private static MessageBox instance;
    private Map<Integer, ArrayList<Message>> Box;

    private MessageBox(int nbAgent){
        Box = new HashMap<>();
        for (int x = 0 ; x <nbAgent ; x++){
            Box.put(x, new ArrayList<Message>());
        }
    }

    public Map<Integer, ArrayList<Message>> getBox() {
        return Box;
    }

    public static MessageBox getInstance(int nbAgent) {
        if (instance == null){
            instance = new MessageBox(nbAgent);
        }
        return instance;
    }

    public synchronized void sendMessage(int idAgent, Message message){
        Box.get(idAgent).add(message);
    }

    public synchronized ArrayList<Message> getMessage(int idAgent){
        return Box.get(idAgent);
    }

}
