package algos;

import agent.Agent;
import messaging.Message;
import messaging.MessageType;
import utils.Direction;
import utils.Position;

import java.util.*;

public class MARTAImproved {

    Position currentPosi;
    List<Position> childPositions;

    Map<Position,Integer> posAndCost;
    Map<Integer,Position> availablePosAndCost;

    Boolean isSolved=false;

    public Boolean getSolved() {
        return isSolved;
    }

    public void solvePuzzle(Agent agent) {
        boolean hasMoved = false;
        posAndCost = new HashMap<>();
        availablePosAndCost= new HashMap<>();

        childPositions = new ArrayList<>();

        //Step 1 [initialize] set sx = si (initial state);
        currentPosi = agent.getCurrentPosition();
        //Step 2 [expansion] expand sx and let C(sx) to be the set of child states
        childPositions = agent.getAdjacentPositions();
        //Step 3 [termination?] if there exists a goal state in C(sx), then move to the goal state and quit
        for (Position posi : childPositions) {
            if (agent.getGoalPosition().isEqual(posi)) {
                Direction direction = agent.posToDir(posi);
                hasMoved = agent.isMoveAvailable(direction);
                if(hasMoved){
                    agent.move(direction);
                    System.out.println("Agent "+agent.getIdAgent()+" : Arrived");
                    isSolved = true;
                }
                else {
                    Agent agentToMove = agent.getAgentToMove(posi);
                    agent.getMessageBox().sendMessage(agentToMove.getIdAgent(), new Message(agent, agentToMove, MessageType.REQUEST, posi));
                }
                return;
            }
        }
        //Step 4 [look ahead search] for all sy e C(sx), calculate f(sx,sy) which is the estimated cost from sx to the goal state through sy.
        this.lookAheadSearch(childPositions, agent);

        //Step 7 [Move] set sx=sy'
        Position nextPosi = getBestChildIndex(agent);
        Direction direction = agent.posToDir(nextPosi);
        hasMoved = agent.isMoveAvailable(direction);

        //Step 8 Go to step 2
        if (hasMoved) {
            agent.move(direction);
        }
    }

    private Position getBestChildIndex(Agent agent) {
        int minCost = Integer.MAX_VALUE;
        Position minPos = null;
        if (posAndCost != null) {
            // get min cost
            for (Map.Entry entry : posAndCost.entrySet()) {
                if ((Integer) entry.getValue() < minCost){
                    minCost = (Integer) entry.getValue();
                    minPos = (Position) entry.getKey();
                }
                if (agent.isMoveAvailable((Position) entry.getKey())){
                    availablePosAndCost.put((Integer) entry.getValue(), (Position) entry.getKey());
                }
            }
            if (availablePosAndCost.containsValue(minCost)){
                return availablePosAndCost.get(minCost);
            }
        }
        return minPos;
    }

    private void lookAheadSearch(List<Position> childPositions, Agent agent) {
        int costFromPosiToGoal;
        for (Position posi : childPositions) {
            costFromPosiToGoal = Math.abs((agent.getCurrentPosition().getX() + posi.getX()) - agent.getGoalPosition().getX());
            costFromPosiToGoal += Math.abs((agent.getCurrentPosition().getY() + posi.getY()) - agent.getGoalPosition().getY());
            this.posAndCost.put(posi, costFromPosiToGoal);
        }
    }

}
