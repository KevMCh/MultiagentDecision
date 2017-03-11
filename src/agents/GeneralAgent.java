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
 * Class representing an general agent.
 */
package agents;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
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
  
  protected static final String MODERATOR = "Moderator";      // Moderator constant
  protected static final String PATHFILES = "/src/files/";    // Path to the files
  
  private Opinion opinion;                                    // Value opinion

  @Override
  protected void setup() {
    MessagePack message = new MessagePack(this.getName(), opinion);

    addBehaviour(new OneShotBehaviour() {
      @Override
      public void action (){        
        createOpinion(this.getAgent().getLocalName());
        
        sendMessageToAgent (MODERATOR);
      }
    });
  }
  
  /**
   * Function to send a message to a agent
   * @param nameAgent
   */
  public void sendMessageToAgent (String nameAgent){
    
    MessagePack message = new MessagePack(this.getName(), getOpinion());
    
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
   * Funtion to create the agent opinion
   */
  private void createOpinion(String fileName) {
    Opinion newOpinion = new Opinion();
    newOpinion.setValueOpinion(readValues(fileName));
    
    setOpinion(newOpinion);
  }
  
  /**
   * File to read the data
   * @param fileName
   */
  private ArrayList<Double> readValues(String fileName) {
    String pathProject = System.getProperty("user.dir");
    File file = new File (pathProject + PATHFILES + fileName);
    FileReader fr;
    
    try {
      fr = new FileReader (file);
      BufferedReader br = new BufferedReader(fr);

      String line = br.readLine();
      String[] values = line.split(" ");
      
      ArrayList<Double> data = new ArrayList<Double> ();
      for(int i = 0; i < values.length; i++){
        data.add(Double.parseDouble(values[i]));
      }
            
      return data;
      
    } catch (FileNotFoundException e) {
      e.printStackTrace();
      
    } catch (IOException e) {
      e.printStackTrace();
    }
    
    return null;
  }

  public Opinion getOpinion() { return opinion; }

  public void setOpinion(Opinion opinion) { this.opinion = opinion; }

}
