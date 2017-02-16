package agents;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

public class GeneralAgent extends Agent {
  
  @Override
  protected void setup() {
   addBehaviour(new OneShotBehaviour() {
     @Override
     public void action (){
       ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
       msg.setContent("Send");
       msg.addReceiver(new AID("moderator", AID.ISLOCALNAME));
       send(msg);
     }
   });
  }
}
