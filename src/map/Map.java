package map;

import sun.management.Agent;

import java.util.ArrayList;

public class Map {

    public ArrayList<ArrayList<Agent>> grille;


    public Map(int size){

        // Création de la grille vide
        grille = new ArrayList<>();
        for (int x=0; x < size ; x++){
            ArrayList<Agent> temp = new ArrayList<Agent>();
            for (int y=0; y<size ; y++){
                temp.add(null);
            }
            grille.add(temp);
        }
    }

}
