package agents;

import java.util.ArrayList;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import messages.MessagePack;

public class ModeratorAgent extends Agent {
  
  private ArrayList<MessagePack> allOpinions;
  private Integer numAgents;

  @Override
  protected void setup() {
    allOpinions = new ArrayList<MessagePack> ();
    
    Object[] args = getArguments();
    numAgents = (Integer) args[0];
    
    addBehaviour(new CyclicBehaviour() {
      @Override
      public void action (){
        ACLMessage msg = receive();
        
        if(msg != null){
          try {            
            MessagePack messagepack = (MessagePack) msg.getContentObject();
            getAllOpinions().add(messagepack);
                         
            if(getAllOpinions().size() >= getNumAgents()) {
              ArrayList<Integer> opinionGroup = calculateGroupOpinion();
              writeOpinion(opinionGroup);
            }
                      
          } catch (UnreadableException e) {
            e.printStackTrace();
          }
        } else {
          block();
        }
      }
    });
  }
  
  private ArrayList<Integer> calculateGroupOpinion() {
    ArrayList<Integer> opinionGroup = new ArrayList<Integer> ();

    for(int i = 0; i < getAllOpinions().get(0).getOpinion().size(); i++){
      Integer sumValue = 0;
      
      for(int j = 0; j < getAllOpinions().size(); j++){
        sumValue += allOpinions.get(j).getOpinion().get(i);
      }
      
      opinionGroup.add(sumValue / allOpinions.size());
    }
    
    return opinionGroup;
  }
  
  private void writeOpinion(ArrayList<Integer> opinion){
    for(int i = 0; i < opinion.size(); i++){
      System.out.print(opinion.get(i));
    }
    System.out.println();
  }
  
  public ArrayList<MessagePack> getAllOpinions() { return allOpinions; }

  public void setAllOpinions(ArrayList<MessagePack> allOpinions) { this.allOpinions = allOpinions; }
  
  public Integer getNumAgents() { return numAgents; }

  public void setNumAgents(Integer numAgents) { this.numAgents = numAgents; }
}