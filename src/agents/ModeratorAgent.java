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

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import messages.MessagePack;

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
      public void action (){
        ACLMessage msg = receive();
        
        if(msg != null){
          try {            
            MessagePack messagepack = (MessagePack) msg.getContentObject();
            getData().addNewOpinion(messagepack);
                                     
            if(getData().getAllOpinions().size() >= getData().getNumAgents()) {
              getData().calculateGroupInformation();
              getData().writeData();
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
  
  public double getAccord() { return accord; }

  public void setAccord(double accord) { this.accord = accord; }

  public double getDisagreement() { return disagreement; }

  public void setDisagreement(double disagreement) { this.disagreement = disagreement; }
  
  public InformationData getData() { return data; }

  public void setData(InformationData data) { this.data = data; }
}