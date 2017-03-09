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
 * Class representing an agent.
 */
package agents;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

import messages.MessagePack;
import messages.Opinion;

public class GeneralAgent extends Agent {
 
  @Override
  protected void setup() {
    ArrayList<Double> valueOpinion = new ArrayList<Double>();
    for (int i = 1; i < 5 ; i++){
      valueOpinion.add((double) i); 
    }
        
    Opinion opinion = new Opinion();
    opinion.setValueOpinion(valueOpinion);
    
    MessagePack message = new MessagePack(this.getName(), opinion);
    
   addBehaviour(new OneShotBehaviour() {
     @Override
     public void action (){
       ACLMessage msg = new ACLMessage(ACLMessage.INFORM);

       try {
         msg.setContentObject(message);
       } catch (IOException e) {
         e.printStackTrace();
       }
       msg.addReceiver(new AID("moderator", AID.ISLOCALNAME));
       send(msg);
     }
   });
  }
}
