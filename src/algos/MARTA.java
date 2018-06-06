package algos;

import agent.Agent;
import utils.Position;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MARTA {

    Position currentPosi;
    List<Integer> costToGoalPosi;
    List<Position> childPositions = new HashSet<>();

    private void solvePuzzle(Agent agent) {

        //Step 1 [initialize] set sx = si (initial state);
        currentPosi = agent.getCurrentPosition();
        //Step 2 [expansion] expand sx and let C(sx) to be the set of child states
        childPositions = agent.getAdjacentPositions();
        //Step 3 [termination?] if there exists a goal state in C(sx), then move to the goal state and quit
        for(Position posi : childPositions){
            if (agent.getGoalPosition().equals(posi)){
                //agent.moveToPosi(posi);
                return;
            }
        }
        //Step 4 [look ahead search] for all sy e C(sx), calculate f(sx,sy)=c(sx,sy)+f(sy) which is the estimated cost from sx to the goal state through sy.
        //f(sy) is calculated from a look-ahead search of depth d from sy as follows : f(sy)= min[c(sy,sw)+h(sw)]


    }

}
