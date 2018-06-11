package map;

import agent.Agent;
import utils.Position;

import java.util.List;
import java.util.Observable;

public class Map extends Observable {

    private static Map instance;
    private int size;
    public static int distLock = 0;
    public static boolean cornersOk = false;

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
            instance = new Map(size);
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
        setChanged();
        notifyObservers();
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

    public void startAgents(long betweenLaunchMillis){
        for (int x=0 ; x<grille.length ; x++) {
            for (int y=0 ; y<grille.length ; y++){
                if (grille[x][y] != null){
                    try {
                        Thread.sleep(betweenLaunchMillis);
                        grille[x][y].start();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public void stopAgents(){
        for (int x=0 ; x<grille.length ; x++) {
            for (int y=0 ; y<grille.length ; y++){
                if (grille[x][y] != null){
                    grille[x][y].interrupt();
                }
            }
        }
    }

    public synchronized static void checkLocker(){
        boolean areCornersLocked = true;
        for (int x = 0 ; x< instance.size ; x++){
            for (int y = 0 ; y< instance.size ; y++) {
                Agent agent = instance.grille[x][y];
                if (agent != null && agent.isCorner()) {
                    if (agent.getDistToBorder() <= distLock && !agent.isArrive()) {
                        areCornersLocked = false;
                    }
                }
            }
        }
        if (areCornersLocked) {
            cornersOk = true;
        }
        if (cornersOk){
            boolean everybodylocked = true;
            for (int x = 0; x < instance.size; x++) {
                for (int y = 0; y < instance.size; y++) {
                    Agent agent = instance.grille[x][y];
                    if (agent != null) {
                        if (agent.getDistToBorder() <= distLock && !agent.isArrive()) {
                            everybodylocked = false;
                        }
                    }
                }
            }
            if (everybodylocked) {
                distLock++;
                cornersOk = false;
            }
        }
    }

}
