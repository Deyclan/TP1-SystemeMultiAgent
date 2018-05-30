package map;

import agent.Agent;
import utils.Position;

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

    private void setPosition(Position position, Agent agent){
        grille[position.getX()][position.getY()] = agent;
    }

    public int getSize() {
        return size;
    }

    // Mouvement sur la grille uniquement
    public synchronized boolean move(Position agentCurrentPos, Position agentDestPos){
        Agent current = getPosition(agentCurrentPos);
        Agent dest = getPosition(agentDestPos);
        if (dest !=null){
            System.out.println("ERROR : Destination not empty. Can't Move");
            return false;
        }
        setPosition(agentDestPos, current);
        setPosition(agentCurrentPos, dest);
        return true;
    }
}
