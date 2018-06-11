package utils;

import agent.Agent;
import main.Main;
import map.Map;
import messaging.MessageBox;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;


public class StringArrayToMap {



    public static void convertToMap(String[][] agents, String[][] finalPositions, Map map, MessageBox messageBox){
        try {
            int i = 0;
            for (int x = 0; x < agents.length; x++) {
                for (int y = 0; y < agents[x].length; y++) {
                    if (!agents[x][y].equals("")) {
                        Agent agent = new Agent(i, map, messageBox, letterToColor(agents[x][y]), agents[x][y]);
                        agent.setCurrentPosition(new Position(x, y));
                        agent.setEndPoint(setEndPos(agents[x][y], finalPositions));
                        map.addAgent(agent);
                        i++;
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void aleaToMap(String[][] finalPositions, Map map, MessageBox messageBox){
        try {
            List<Position> availablePos = new ArrayList<>();
            List<Agent> agents = new ArrayList<>();
            int i = 0;
            for (int x = 0; x < finalPositions.length; x++) {
                for (int y = 0; y < finalPositions[x].length; y++) {
                    availablePos.add(new Position(x, y));
                    if (!finalPositions[x][y].equals("")){
                        Agent agent = new Agent(i, map, messageBox, letterToColor(finalPositions[x][y]), finalPositions[x][y]);
                        agent.setEndPoint(new Position(x, y));
                        agents.add(agent);
                        i++;
                    }
                }
            }
            int index = 0;
            Random random = new Random();
            while (index < agents.size()){
                int alea = random.nextInt(availablePos.size());
                agents.get(index).setCurrentPosition(availablePos.get(alea));
                availablePos.remove(alea);
                index++;
            }
            for (Agent agent:agents) {
                map.addAgent(agent);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private static Color letterToColor(String letter){
        Color toReturn;
        switch (letter){
            case "a":
                toReturn = Color.ALICEBLUE;
                break;
            case "b":
                toReturn = Color.BLUE;
                break;
            case "c":
                toReturn = Color.CADETBLUE;
                break;
            case "d":
                toReturn = Color.DARKBLUE;
                break;
            case "e":
                toReturn = Color.CHOCOLATE;
                break;
            case "f":
                toReturn = Color.FIREBRICK;
                break;
            case "g":
                toReturn = Color.GREEN;
                break;
            case "h":
                toReturn = Color.HONEYDEW;
                break;
            case "i":
                toReturn = Color.INDIGO;
                break;
            case "j":
                toReturn = Color.NAVAJOWHITE;
                break;
            case "k":
                toReturn = Color.KHAKI;
                break;
            case "l":
                toReturn = Color.LAVENDER;
                break;
            case "m":
                toReturn = Color.MAGENTA;
                break;
            case "n":
                toReturn = Color.NAVY;
                break;
            case "o":
                toReturn = Color.OLDLACE;
                break;
            case "p":
                toReturn = Color.PALEGOLDENROD;
                break;
            case "q":
                toReturn = Color.AQUA;
                break;
            case "r":
                toReturn = Color.RED;
                break;
            case "s":
                toReturn = Color.SADDLEBROWN;
                break;
            case "t":
                toReturn = Color.TAN;
                break;
            case "u":
                toReturn = Color.PERU;
                break;
            case "v":
                toReturn = Color.VIOLET;
                break;
            case "w":
                toReturn = Color.WHEAT;
                break;
            case "x":
                toReturn = Color.ORANGE;
                break;
            case "y":
                toReturn = Color.YELLOW;
                break;
            case "z":
                toReturn = Color.AZURE;
                break;
            default:
                toReturn = Color.GRAY;
                break;

        }
        return toReturn;
    }

    private static Position setEndPos(String agent, String[][] finalPos) throws Exception {
        for (int x=0 ; x<finalPos.length ; x++) {
            for (int y=0 ; y<finalPos[x].length ; y++){
                if (agent.equals(finalPos[x][y])){
                    return new Position(x,y);
                }
            }
        }
        throw new Exception("Impossible de trouver l'agent correespondant dans le tableau d'arrivée");
    }

    public static int getNbAgents(String[][] agents){
        int i = 0;
        for (int x = 0; x < agents.length; x++) {
            for (int y = 0; y < agents[x].length; y++) {
                if (!agents[x][y].equals("")){
                    i++;
                }
            }
        }
        return i;
    }
}
