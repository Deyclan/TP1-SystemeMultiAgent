package algos;

import agent.Agent;
import messaging.Message;
import messaging.MessageBox;
import messaging.MessageType;
import utils.Direction;
import utils.Position;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MARTA {

    Position currentPosi;
    List<Integer> costToGoalPosi;
    List<Position> childPositions;

    Boolean isSolved=false;

    public void solvePuzzle(Agent agent) {
        boolean hasMoved = false;
        costToGoalPosi = new ArrayList<>();
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
                    //System.out.println("Agent "+agent.getIdAgent()+" : Arrived");
                    isSolved = true;
                }
                else {
                    Agent agentToMove = agent.getAgentToMove(posi);
                    MessageBox mb =  agent.getMessageBox();
                    int idAgentToMove = agentToMove.getIdAgent();
                    Message messageToSend = new Message(agent, agentToMove, MessageType.REQUEST, posi);
                    mb.sendMessage(idAgentToMove, messageToSend);
                }
                return;
            }
        }
        //Step 4 [look ahead search] for all sy e C(sx), calculate f(sx,sy) which is the estimated cost from sx to the goal state through sy.
        this.lookAheadSearch(childPositions, agent);
        //Step 5 [Choice] choose the best child state sy' with minimum f
        int index = getBestChildIndex();
        //Step 6 [Estimation update]

        //Step 7 [Move] set sx=sy'
        Position nextPosi = childPositions.get(index);
        Direction direction = agent.posToDir(nextPosi);
        hasMoved = agent.isMoveAvailable(direction);

        //Step 8 Go to step 2
        if (hasMoved) {
            agent.move(direction);
        }
    }

    private int getBestChildIndex() {
        int minCost = -1;
        if (costToGoalPosi != null) {
            // get min cost
            minCost = costToGoalPosi.get(0);
            for (Integer cost : costToGoalPosi) {
                if (cost < minCost)
                    minCost = cost;
            }
            // choose random between same min costs
            int n = costToGoalPosi.size();
            Random generator = new Random();
            int randIndex = 0;
            int coast;
            do {
                randIndex = generator.nextInt(n);
                coast = costToGoalPosi.get(randIndex);
            } while (coast != minCost);
            return randIndex;
        }
        return minCost;
    }

    private void lookAheadSearch(List<Position> childPositions, Agent agent) {
        int costFromPosiToGoal;
        for (Position posi : childPositions) {
            costFromPosiToGoal = Math.abs((agent.getCurrentPosition().getX() + posi.getX()) - agent.getGoalPosition().getX());
            costFromPosiToGoal += Math.abs((agent.getCurrentPosition().getY() + posi.getY()) - agent.getGoalPosition().getY());
            this.costToGoalPosi.add(costFromPosiToGoal);
        }
    }

    public Boolean getSolved() {
        return isSolved;
    }

    public void setSolved(Boolean solved) {
        isSolved = solved;
    }
}
