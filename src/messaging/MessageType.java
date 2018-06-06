package messaging;

public enum MessageType {

    REQUEST(0),
    RESPONSE(1);

    private int id;

    MessageType(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
