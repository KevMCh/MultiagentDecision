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

import java.util.ArrayList;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import messages.MessagePack;

public class ModeratorAgent extends Agent {
  
  private static double ALPHA = 0.8;                    // Value of moderator agreement
  private static double DELTA = 0.2;                    // Moderator's disagreement value
  
  private Integer numAgents;                            // Number of agents
  private double accord;                                // Level of agent agreement
  private double disagreement;                          // Level of agent desagreement
  
  private ArrayList<Double> opinionGroup;               // Group opinion
  private ArrayList<MessagePack> allOpinions;           // All opinions
  
  private ArrayList<Double> groupSimilarity;            // Similarities with the group
  private SimilitudeMatrix agentSimilarity;             // Similarities between agents

  @Override
  protected void setup() {
    initialize();
    
    addBehaviour(new CyclicBehaviour() {
      @Override
      public void action (){
        ACLMessage msg = receive();
        
        if(msg != null){
          try {            
            MessagePack messagepack = (MessagePack) msg.getContentObject();
            getAllOpinions().add(messagepack);
                         
            if(getAllOpinions().size() >= getNumAgents()) {
              setOpinionGroup(calculateGroupOpinion());
              
              updateGroupSimilarity();
              System.out.println("\nGroup Similarity:");
              writeGroupSimilitary();
              System.out.print("\n");
              
              updateAgentsSimilarity();
              System.out.println("\nAgents Similarity:");
              writeAgentSimilitarity();
              System.out.print("\n");
            }
                      
          } catch (UnreadableException e) {
            e.printStackTrace();
          }
        } else {
          block();
        }
      }
      
      /**
       * Function to update the GroupSimilitary
       */
      private void updateGroupSimilarity() {
        for (int i = 0; i < getNumAgents(); i++){
            getGroupSimilarity().add(
                getSimilitude(getAllOpinions().get(i).getOpinion().getValueOpinion(),
                getOpinionGroup()));
        }
      }
      
      /**
       * Function to update the AgentsSimilitary
       */
      private void updateAgentsSimilarity() {
        for(int i = 0; i < getNumAgents(); i ++){
          for (int j = 0; j < getNumAgents(); j++){
            
            if(i == j) {
              getAgentSimilarity().addSimilitude(0.0);
            } else {
              getAgentSimilarity().addSimilitude(getSimilitude(
                  getAllOpinions().get(i).getOpinion().getValueOpinion(), 
                  getAllOpinions().get(j).getOpinion().getValueOpinion()));
            }
          }
        }
      }
    });
  }
  
  /**
   * Function to initialize the moderator variables
   */
  private void initialize() {
    setAllOpinions(new ArrayList<MessagePack> ());
    setGroupSimilarity(new ArrayList<Double> ());
    
    Object[] args = getArguments();
    setNumAgents((Integer) args[0]);
    setAccord((double) args[1]);
    setDisagreement((double) args[2]);
    
    setAgentSimilarity(new SimilitudeMatrix(getNumAgents(), getNumAgents()));
  }

  /**
   * Function to calculate the average
   * @return average
   */
  private ArrayList<Double> calculateAverage() {
    ArrayList<Double> average = new ArrayList<Double> ();

    for(int i = 0; i < getAllOpinions().get(0).getOpinion().getValueOpinion().size(); i++){
      Double sumValue = 0.0;
      
      for(int j = 0; j < getAllOpinions().size(); j++){
        sumValue += allOpinions.get(j).getOpinion().getValueOpinion().get(i);
      }
      
      average.add(sumValue / allOpinions.size());
    }
    
    return average;
  }
  
  /**
   * Function to calculate the group opinion
   * @return groupOpinion
   */
  private ArrayList<Double> calculateGroupOpinion() {
    ArrayList<Double> opinionGroup = new ArrayList<Double> ();

    for(int i = 0; i < getAllOpinions().get(0).getOpinion().getValueOpinion().size(); i++){
      Double product = 0.0;
      
      for(int j = 0; j < getAllOpinions().size(); j++){
        product *= allOpinions.get(j).getOpinion().getValueOpinion().get(i);
      }
      
      opinionGroup.add(Math.pow(product, 1 / getNumAgents()));
    }
    
    return opinionGroup;
  }
  
  /**
   * Function to print a opinion
   * @param opinion
   */
  private void writeOpinion(ArrayList<Double> opinion){
    for(int i = 0; i < opinion.size(); i++){
      System.out.print(opinion.get(i));
    }
    System.out.println();
  }
  
  /**
   * Function to calculate a scalar of the two vectors
   * @param vector1
   * @param vector2
   * @return scalar
   */
  private double scalarProduct(ArrayList<Double> vector1, ArrayList<Double> vector2) {
    double scalar = 0.0;
    
    for(int i = 0; i < vector1.size(); i++){
      scalar += vector1.get(i) * vector2.get(i);
    }
    
    return scalar;
  }
  
  /**
   * Function to calculate the sine
   * @param vector1
   * @param vector2
   * @return sin of the vectors
   */
  private double sinAngle (ArrayList<Double> vector1, ArrayList<Double> vector2) {
    double scalar = scalarProduct(vector1, vector2);
        
    double aux1 = 0.0;
    double aux2 = 0.0;
    for(int j = 0; j < vector1.size(); j++) {
      aux1 += Math.pow(vector1.get(j), 2);
      aux2 += Math.pow(vector1.get(j), 2);
    }
    
    double ang1 = Math.sqrt(aux1);
    double ang2= Math.sqrt(aux2);
    
    double angle = Math.acos(scalar / (ang1 * ang2));
    
    return Math.sin(angle);
  }
  
  /**
   * Function to calculate the similitude
   * @param vector1
   * @param vector2
   * @return value of similitude
   */
  private double getSimilitude (ArrayList<Double> vector1, ArrayList<Double> vector2) {
    return 1 - sinAngle (vector1, vector2);    
  }
  
  /**
   * Print the group similarity of the group with the agents
   */
  private void writeGroupSimilitary() {
    for(int i = 0; i < getGroupSimilarity().size(); i++) {
      System.out.print(getGroupSimilarity().get(i));
      
      if(i < getGroupSimilarity().size() - 1) {
        System.out.print(" - ");
      }
    }
  }
  
  /**
   * Print the similarity of the agents between them
   */
  private void writeAgentSimilitarity() {
    getAgentSimilarity().printSimilitudeMatrix();
  }
  
  public ArrayList<MessagePack> getAllOpinions() { return allOpinions; }

  public void setAllOpinions(ArrayList<MessagePack> allOpinions) { this.allOpinions = allOpinions; }
  
  public Integer getNumAgents() { return numAgents; }

  public void setNumAgents(Integer numAgents) { this.numAgents = numAgents; }
  
  public ArrayList<Double> getOpinionGroup() { return opinionGroup; }

  public void setOpinionGroup(ArrayList<Double> opinionGroup) { this.opinionGroup = opinionGroup; }
  
  public ArrayList<Double> getGroupSimilarity() { return groupSimilarity; }

  public void setGroupSimilarity(ArrayList<Double> groupSimilarity) { this.groupSimilarity = groupSimilarity; }

  public SimilitudeMatrix getAgentSimilarity() { return agentSimilarity; }

  public void setAgentSimilarity(SimilitudeMatrix agentSimilarity) { this.agentSimilarity = agentSimilarity; }
  
  public double getAccord() { return accord; }

  public void setAccord(double accord) { this.accord = accord; }

  public double getDisagreement() { return disagreement; }

  public void setDisagreement(double disagreement) { this.disagreement = disagreement; }
}