package agents;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

import messages.MessagePack;

public class GeneralAgent extends Agent {
 
  @Override
  protected void setup() {
    ArrayList<Integer> opinion = new ArrayList<Integer>();
    for (int i = 1; i < 5 ; i++){
      opinion.add(i); 
    }
    
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
