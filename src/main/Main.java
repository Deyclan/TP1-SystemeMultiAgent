package main;

import agent.Agent;
import javafx.application.Application;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import map.Map;
import messaging.MessageBox;
import utils.Position;
import views.GridView;


public class Main extends Application {

    private static int GRID_SIZE = 10;
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

        Agent agent1 = new Agent(2, MAP, MESSAGE_BOX, Color.BLUE);
        agent1.setCurrentPosition(new Position(0,0));
        agent1.setEndPoint(new Position(1,2));

        MAP.addAgent(agent);
        MAP.addAgent(agent1);

        GridView view = new GridView(MAP);
        System.out.println("View launched");

        MAP.startAgents(1000);
    }
}
