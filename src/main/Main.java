package main;

import javafx.application.Application;
import javafx.stage.Stage;
import map.Map;
import messaging.MessageBox;
import utils.StringArrayToMap;
import views.GridView;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class Main extends Application {

    private static Map MAP;
    private static MessageBox MESSAGE_BOX;

    @Override
    public void start(Stage primaryStage) throws Exception {

        Scanner scanner = new Scanner(System.in);
        List<String> letters = new ArrayList<>();
        fillLetters(letters);
        List<String> agentsName = new ArrayList<>();

        System.out.println("Application lancé");
        System.out.println(String.format("Entrez la taille de la grille (inférieur a %d), puis appuyez sur \"entrer\"",10));
        int size = scanner.nextInt();
        if (size >= 10 || size < 0){
            System.out.println(String.format("La valeur rentrée est erronée"));
        }
        System.out.println(String.format("Entrez le nombre d'agent à placer sur la grille ( maximum %d ), puis appuyez sur \"entrer\"", size*size - 1));
        int nbAgent = scanner.nextInt();
        fillAgentNames(agentsName, letters, nbAgent);

        String[][] arrivee = setArrivee(agentsName, size);


        MAP = Map.getInstance(arrivee.length);
        MESSAGE_BOX = MessageBox.getInstance(StringArrayToMap.getNbAgents(arrivee));

        //StringArrayToMap.convertToMap(depart, arrivee, MAP, MESSAGE_BOX);
        StringArrayToMap.aleaToMap(arrivee, MAP, MESSAGE_BOX);

        GridView view = new GridView(MAP);
        System.out.println("Lancement de la vue");
        double temp = 1000 / Math.pow(MAP.getSize(),2);
        MAP.startAgents((long) temp);
    }

    private void fillLetters(List<String> letters){
        letters.add("a");
        letters.add("b");
        letters.add("c");
        letters.add("d");
        letters.add("e");
        letters.add("f");
        letters.add("g");
        letters.add("h");
        letters.add("i");
        letters.add("j");
        letters.add("k");
        letters.add("l");
        letters.add("m");
        letters.add("n");
        letters.add("o");
        letters.add("p");
        letters.add("q");
        letters.add("r");
        letters.add("s");
        letters.add("t");
        letters.add("u");
        letters.add("v");
        letters.add("w");
        letters.add("x");
        letters.add("y");
        letters.add("z");
    }

    private void fillAgentNames(List<String> nomAgent, List<String> letters, int nbAgent){
        for (int i = 0; i< nbAgent ; i++){
            nomAgent.add(letters.get(i % letters.size()));
        }
        if (nomAgent.size()>letters.size()){
            int tmp = -1;
            for (int x = letters.size() ; x < nomAgent.size() ; x++){
                int index = x % letters.size();
                if (index == 0){
                    tmp++;
                }
                nomAgent.set(x , String.format("%s%s",letters.get(tmp), nomAgent.get(x)));
            }
        }
    }

    private String[][] setArrivee(List<String> nameList, int gridSize){
        String[][] end = new String[gridSize][gridSize];
        int i = 0;
        for (int x = 0 ; x<gridSize ; x++){
            for (int y = 0 ; y<gridSize ; y++){
                if (i < nameList.size()){
                    end[x][y] = nameList.get(i);
                }else {
                    end[x][y] = "";
                }
                i++;
            }
        }
        return end;
    }
}
