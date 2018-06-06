package utils;

public enum Direction {

    UP(0),
    RIGHT(1),
    DOWN(2),
    LEFT(3);

    private int id;

    Direction(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
