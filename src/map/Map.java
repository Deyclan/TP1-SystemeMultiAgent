package map;

import sun.management.Agent;
import utils.Position;

import java.util.ArrayList;

public class Map {

    public static Map instance;
    private int size;

    private Agent[][] grille;

    private Map(int size){
        this.size = size;
        // Création de la grille vide
        for (int x=0; x < size ; x++){
            for (int y=0; y<size ; y++){
                grille[x][y] = null;
            }
        }
    }

    public static Map getInstance(int size) {
        if (instance == null){
            return new Map(size);
        }
        return instance;
    }

    private Agent[][] getGrille() {
        return grille;
    }

    public Agent getPosition(Position position){
        return grille[position.getX()][position.getY()];
    }

    public int getSize() {
        return size;
    }
}
