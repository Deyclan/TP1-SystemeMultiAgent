package main;

import javafx.application.Application;
import javafx.stage.Stage;
import map.Map;
import messaging.MessageBox;
import utils.StringArrayToMap;
import views.GridView;



public class Main extends Application {

    private static Map MAP;
    private static MessageBox MESSAGE_BOX;

    @Override
    public void start(Stage primaryStage) throws Exception {
        /*
        String[][] depart = {
                {"","","","e",""},
                {"","","","","c"},
                {"","","a","",""},
                {"d","","","",""},
                {"","","","","b"}
        };
        */
        /*
        String[][] arrivee = {
                {"a","b","c","d","e"},
                {"f","g","h","i","j"},
                {"k","l","m","n","o"},
                {"p","","","",""},
                {"","","","",""}
        };
        /*
        String[][] depart = {
                {"l","f","","e"},
                {"m","a","d","j"},
                {"k","c","g",""},
                {"h","i","b",""},
        };
        */
        String[][] arrivee = {
                {"a","b","c","d"},
                {"e","f","g","h"},
                {"i","j","k","l"},
                {"m","","",""},
        };

        MAP = Map.getInstance(arrivee.length);
        MESSAGE_BOX = MessageBox.getInstance(StringArrayToMap.getNbAgents(arrivee));

        //StringArrayToMap.convertToMap(depart, arrivee, MAP, MESSAGE_BOX);
        StringArrayToMap.aleaToMap(arrivee, MAP, MESSAGE_BOX);

        GridView view = new GridView(MAP);
        System.out.println("View launched");
        double temp = 1000 / Math.pow(MAP.getSize(),2);
        MAP.startAgents((long) temp);
    }
}
