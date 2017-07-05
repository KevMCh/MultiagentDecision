/**
 * Project of Multi-Agent Decisions
 * 
 * Máster Universitario en Ingeniería Informática
 * E.S.I.T.– INFORMÁ́TICA
 * Advanced Intelligent Systems
 * 
 * Project to develop the conflict of differents agents.
 * All the agents will give their opinions and through 
 * the moderator it will lead to the best solution.
 * 
 * @author  Kevin M. Ch.
 * @version 0.0.0
 * 
 * Class to define the moderator.
 */
package agents;

import java.io.IOException;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import messages.MessagePack;
import messages.Opinion;

public class ModeratorAgent extends Agent {
  
  private double accord;                                // Level of agent agreement
  private double disagreement;                          // Level of agent desagreement
  
  private InformationData data;                         // Data of the decision

  @Override
  protected void setup() {
    Object[] args = getArguments();
    setAccord((double) args[1]);
    setDisagreement((double) args[2]);
    
    setData(new InformationData((Integer) args[0]));
    
    addBehaviour(new CyclicBehaviour() {
      @Override
      public void action () {
        ACLMessage msg = receive();
        
        if(msg != null){
          try {            
            MessagePack messagepack = (MessagePack) msg.getContentObject();
            getData().addNewOpinion(messagepack);
                                     
            if(getData().getAllOpinions().size() >= getData().getNumAgents()) {
              getData().calculateGroupInformation();
              getData().writeData();
              
              if(getData().getQSAQ() > getAccord()) {
                int idSolution = searchSolution();
                System.out.println("The solution is the alternative " + idSolution + ".");
                
              } else {
                int idAgent = searchISDIAgent();
                System.out.println("\nModify agent " + idAgent + "\n");
                String identififierAgent = getData().getAllOpinions().get(idAgent).getName();
                
                getData().getAllOpinions().remove(idAgent);
                
                sendMessageToAgent(identififierAgent);
              }
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
  
  /**
   * Function to send a message with the opinion to a agent
   * @param nameAgent
   */
  public void sendMessageToAgent (String nameAgent){
    Opinion opinion = new Opinion ();
    opinion.setValueOpinion(getData().getOpinionGroup());
    
    MessagePack message = new MessagePack(this.getName(), opinion);
    
    ACLMessage msg = new ACLMessage(ACLMessage.INFORM);

    try {
      msg.setContentObject(message);
    } catch (IOException e) {
      e.printStackTrace();
    }
    msg.addReceiver(new AID(nameAgent, AID.ISLOCALNAME));
    send(msg);
  }
  
  /**
   * 
   * @return
   */
  private int searchISDIAgent() {
    int idAgent = 0;
    Double min = Double.MAX_VALUE;
    
    for(int i = 0; i < getData().getISDI().size(); i++) {
      if(getData().getISDI().get(i) < min) {
        min = getData().getISDI().get(i);
        idAgent = i;
      }
    }
    
    return idAgent;
  }
  
  /**
   * Search the best solution of the alternativities
   * @return
   */
  private int searchSolution() {
    int idAlternative = 0;
    Double max = Double.MIN_VALUE;
    
    for(int i = 0; i < getData().getOpinionGroup().size(); i++) {
      if(getData().getOpinionGroup().get(i) > max) {
        max = getData().getISDI().get(i);
        idAlternative = i;
      }
    }
    
    return idAlternative;
  }
  
  public double getAccord() { return accord; }

  public void setAccord(double accord) { this.accord = accord; }

  public double getDisagreement() { return disagreement; }

  public void setDisagreement(double disagreement) { this.disagreement = disagreement; }
  
  public InformationData getData() { return data; }

  public void setData(InformationData data) { this.data = data; }
}