package map;

import agent.Agent;
import javafx.geometry.Pos;
import utils.Position;

import java.util.List;
import java.util.Observable;

public class Map extends Observable {

    public static Map instance;
    private int size;

    private Agent[][] grille;

    private Map(int size){
        this.size = size;
        grille = new Agent[size][size];
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

    public Agent[][] getGrille() {
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

    public void addAgentList(List<Agent> agents){
        for (Agent agent : agents) {
            addAgent(agent);
        }
    }

    public void addAgent(Agent agent) {
        Position position = agent.getCurrentPosition();
        if (grille[position.getX()][position.getY()] != null) {
            System.out.println("Error : Impossible d'ajouter l'agent "+agent+",  la case ["+position.getX()+","+position.getY()+"] est déjà occupée");
        } else {
            grille[position.getX()][position.getY()] = agent;
        }
    }
}
