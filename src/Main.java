import java.util.ArrayList;

public class Main {

    public static int GRID_SIZE = 5;
    public static ArrayList<ArrayList<Agent>> grille;

    public static void main(String[] args) {
        createGrid();

    }

    private static void createGrid(){
        // Création de la grille
        grille = new ArrayList<>();
        for (int x=0; x < GRID_SIZE ; x++){
            ArrayList<Agent> temp = new ArrayList<Agent>();
            temp.add(null);
            temp.add(null);
            temp.add(null);
            temp.add(null);
            temp.add(null);
            grille.add(temp);
        }
    }

}
