package agents;

import javax.swing.JOptionPane;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

public class ModeratorAgent extends Agent {

  @Override
  protected void setup() {
    addBehaviour(new CyclicBehaviour() {
      @Override
      public void action (){
        ACLMessage msg = receive();
        
        if(msg != null){
          JOptionPane.showMessageDialog(null, "Message received: " + msg.getContent());
        } else {
          block();
        }
      }
    });
  }
}
