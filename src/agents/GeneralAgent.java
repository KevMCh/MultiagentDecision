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
import java.util.ArrayList;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import messages.MessagePack;
import messages.Opinion;

public class GeneralAgent extends Agent {
  
  protected static final String MODERATOR = "Moderator";      // Moderator constant
  protected static final String PATHFILES = "/src/files/";    // Path to the files
  
  private static final Double RANGEOFCHANGE = 0.2;            // Moderator constant
  
  private String identifier;                                  // Name of the agent
  private Opinion opinion;                                    // Value opinion

  @Override
  protected void setup() {
    Object[] args = getArguments();
    setIdentifier((String) args[0]);
    
    MessagePack message = new MessagePack(this.getName(), opinion);

    addBehaviour(new OneShotBehaviour() {
      @Override
      public void action (){        
        createOpinion(this.getAgent().getLocalName());
        
        sendMessageToAgent (MODERATOR);
      }
    });
    
    addBehaviour(new CyclicBehaviour() {
      @Override
      public void action () {
        ACLMessage msg = receive();
        
        if(msg != null){
          
          try {            
            MessagePack messagepack = (MessagePack) msg.getContentObject();
            
            modifyPersonalOpinion(getOpinion(), messagepack.getOpinion().getValueOpinion());
            sendMessageToAgent(MODERATOR);
                      
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
  public void sendMessageToAgent(String nameAgent){
    
    MessagePack message = new MessagePack(this.getIdentifier(), getOpinion());
    
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
  
  private void modifyPersonalOpinion(Opinion opinion, ArrayList<Double> valueOpinion) {
    int indexMax = 0;
    Double maxDifference = Double.MIN_VALUE;
    
    for(int i = 0; i < valueOpinion.size(); i++) {
      if(Math.abs(opinion.getValueOpinion().get(i) - valueOpinion.get(i)) < maxDifference) {
        maxDifference = Math.abs(opinion.getValueOpinion().get(i) - valueOpinion.get(i));
        indexMax = i;
      }
    }
    
    opinion.getValueOpinion().set(indexMax, opinion.getValueOpinion().get(indexMax) + RANGEOFCHANGE);
    
    setOpinion(opinion);
  }
  
  /**
   * Write the opinion
   */
  protected void writeOpinion() {
    System.out.println("Opinion:");
    getOpinion().writeValueOpinion();
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
  
  public String getIdentifier() {
    return identifier;
  }

  public void setIdentifier(String identifier) {
    this.identifier = identifier;
  }

}
