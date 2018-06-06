package main;

import agent.Agent;
import javafx.application.Application;
import javafx.stage.Stage;
import map.Map;
import messaging.MessageBox;
import utils.Position;
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
        Agent agent = new Agent(1, MAP, MESSAGE_BOX);
        agent.setCurrentPosition(new Position(2,2));
        agent.setEndPoint(new Position(1,1));
        MAP.addAgent(agent);
        GridView view = new GridView(MAP);
        System.out.println("View launched");
    }
}
