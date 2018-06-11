package algos;

import agent.Agent;
import messaging.Message;
import messaging.MessageBox;
import messaging.MessageType;
import utils.Position;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

public class Algo {

    public synchronized void solve(Agent agent){

        // Lecture boite message
        MessageBox messageBox = agent.getMessageBox();
        synchronized (messageBox){
            List<Message> messagesRecus = messageBox.getMessage(agent.getIdAgent());
            if (messagesRecus.size() > 0){
                for (Message m : messagesRecus) {
                    if (m.getMessageType() == MessageType.REQUEST && m.getPosToFree().isEqual(agent.getCurrentPosition())) {  // On nous demande de bouger
                        moveToBestSolution(agent);   // On bouge ou on demande de faire de la place
                        agent.getMessageBox().sendMessage(m.getFrom().getIdAgent(), new Message(agent, m.getFrom(), MessageType.RESPONSE, null)); // On répond à la demande
                    }
                }
            }
        }

        // Mouvement
        moveToBestSolution(agent);

    }

    private void moveToBestSolution(Agent agent){

        Position bestNext = getBestNextPosition(agent); // On récupère la meilleur des positions suivantes

        if (agent.isMoveAvailable(bestNext)){   // Si elle est libre, on bouge
            agent.move(bestNext);
            agent.getMessageBox().getMessage(agent.getIdAgent()).clear();
        }
        else {  // Sinon on demande qu'elle soit libérée
            Agent agentToCall = agent.getMap().getPosition(bestNext);
            askAndWaitForMoving(agent, agentToCall, bestNext);
            agent.move(bestNext);
            agent.getMessageBox().getMessage(agent.getIdAgent()).clear();
        }

    }

    private void askAndWaitForMoving(Agent agent, Agent agentToCall, Position posToFree){
        try {

            agent.getMessageBox().sendMessage(agentToCall.getIdAgent(), new Message(agent, agentToCall, MessageType.REQUEST, posToFree));
            while (agent.getMessageBox()
                    .getMessage(agent.getIdAgent())
                        .stream()
                        .filter(message -> message.getMessageType() == MessageType.RESPONSE).collect(Collectors.toList()).size() <= 0){

                System.out.println(String.format("Agent %d ( %s ) attend une réponse", agent.getIdAgent(), agent.getAgentName()));
                Thread.sleep(150);
            }
            System.out.println(String.format("Agent %d ( %s ) a eu une réponse", agent.getIdAgent(), agent.getAgentName()));

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private Position getBestNextPosition(Agent agent){

        List<Position> next = agent.getAdjacentPositions();
        List<Position> bestNextPosition = new ArrayList<>();
        double minCost = Double.MAX_VALUE;
        for (Position p : next) {
            double dist = calculerDistPosition(p, agent.getGoalPosition());
            if (dist < minCost){
                bestNextPosition.clear();
                bestNextPosition.add(p);
                minCost = dist;
            }
            else if(dist == minCost){
                bestNextPosition.add(p);
            }
        }
        if (bestNextPosition.size() == 1){ // Si une seule meilleur solution
            return bestNextPosition.get(0);
        }
        else {  // Si plusieurs meilleurs solutions
            List<Position> available = bestNextPosition.stream().filter(agent::isMoveAvailable).collect(toList()); // On cherche s'il y'en a une de dispo
            if (available.size() > 0 ){
                return available.get(0);
            }
            return bestNextPosition.get(new Random().nextInt(bestNextPosition.size()));
        }
    }

    private double calculerDistPosition(Position position, Position currentPosition){
        return currentPosition.distEuclidienne(currentPosition);
    }

}
