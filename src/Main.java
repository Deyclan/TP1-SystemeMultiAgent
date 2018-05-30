import javafx.application.Application;
import javafx.stage.Stage;
import map.Map;
import messaging.MessageBox;
import views.GridView;


public class Main extends Application {

    private static int GRID_SIZE = 5;
    private static int NB_AGENT = 5;
    private static Map MAP;
    private static MessageBox MESSAGE_BOX;

    @Override
    public void start(Stage primaryStage) throws Exception {
        MAP = Map.getInstance(GRID_SIZE);
        MESSAGE_BOX = MessageBox.getInstance(NB_AGENT);
        GridView view = new GridView(MAP);
    }
}
