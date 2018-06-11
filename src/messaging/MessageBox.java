package messaging;

import java.util.*;

public class MessageBox {

    private static MessageBox instance;
    private Map<Integer, List<Message>> Box;

    private MessageBox(int nbAgent) {
        Box = new HashMap<>();
        for (int x = 0; x < nbAgent; x++) {
            List<Message> messages = Collections.synchronizedList(new ArrayList<>());
            Box.put(x, messages);
        }
    }

    public synchronized static MessageBox getInstance(int nbAgent) {
        synchronized (MessageBox.class) {
            if (instance == null) {
                instance = new MessageBox(nbAgent);
            }
            return instance;
        }
    }

    public synchronized Map<Integer, List<Message>> getBox() {
        synchronized (MessageBox.class) {
            return Box;
        }
    }

    public synchronized void sendMessage(int idAgent, Message message) {
        synchronized (MessageBox.class) {
            Box.get(idAgent).add(message);
        }
    }

    public synchronized List<Message> getMessage(int idAgent) {
        synchronized (MessageBox.class) {
            return Box.get(idAgent);
        }
    }

    public synchronized void deleteMessage(int idAgent, Message message){
        synchronized (MessageBox.class) {
            Box.get(idAgent).remove(message);
        }
    }

    public synchronized void deleteAll(int idAgent){
        synchronized (MessageBox.class) {
            Box.get(idAgent).clear();
        }
    }

}
