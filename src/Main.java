import map.Map;
import messaging.MessageBox;


public class Main {

    public static int GRID_SIZE = 5;
    public static int NB_AGENT = 5;
    public static Map MAP;
    public static MessageBox MESSAGE_BOX;

    public static void main(String[] args) {
        MAP = Map.getInstance(GRID_SIZE);
        MESSAGE_BOX = MessageBox.getInstance(NB_AGENT);

    }


}
