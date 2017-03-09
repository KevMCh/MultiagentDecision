package agents;

import java.util.ArrayList;

import messages.MessagePack;

public class InformationData {
  
  private static double ALPHA = 0.8;                    // Value of moderator agreement
  private static double DELTA = 0.2;                    // Moderator's disagreement value
  
  private Integer numAgents;                            // Number of agents
  
  private ArrayList<Double> opinionGroup;               // Group opinion
  private ArrayList<MessagePack> allOpinions;           // All opinions
  
  private ArrayList<Double> groupSimilarity;            // Similarities with the group
  private SimilitudeMatrix agentSimilarity;             // Similarities between agents
  
  private Double QSAQ;                                  // Degree of strong group agreement
  private Double QSDQ;                                  // Degree of strong group disagreement
  private Double QSDI;                                  // Stronger Disagreement Indicator in Group
  private ArrayList<Double> ISAQ;                       // Strong agreement between the decision-maker and the group
  private ArrayList<Double> ISDQ;                       // Degree of strong disagreement between the decision maker and the group
  private ArrayList<Double> ISDI;                       // Stronger disagreement indicator of the decision-maker and the group
  
  /**
   * Default builder
   * @param numAgents
   */
  public InformationData(Integer numAgents) {
    allOpinions = new ArrayList<MessagePack> ();
    groupSimilarity = new ArrayList<Double> ();
    
    this.numAgents = numAgents;
    
    agentSimilarity = new SimilitudeMatrix(getNumAgents(), getNumAgents());
  }
  
  /**
   * Function to add a new opinion
   * @param messagepack
   */
  public void addNewOpinion(MessagePack messagepack) {
    getAllOpinions().add(messagepack);
  }
  
  /**
   * Function to calculate all the data of the final decision
   */
  public void calculateGroupInformation() {
    setOpinionGroup(calculateGroupOpinion());

    updateGroupSimilarity();
    updateAgentsSimilarity();
    
    updateQSAQ();
    updateQSDQ();
    updateQSDI();
    updateISAQ();
    updateISDQ();
    updateISDI();
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
   * Function to update the degree of strong group agreement
   */
  private void updateQSAQ(){
    Double count = 0.0;
    for(int i = 0; i < getOpinionGroup().size(); i++){
      if(!isLower(getOpinionGroup().get(i), ALPHA)){
        count ++;
      }
    }
    
    setQSAQ(count / numAgents);
  }
  
  /**
   * Function to update the degree of strong group disagreement
   */
  private void updateQSDQ(){
    Double count = 0.0;
    for(int i = 0; i < getOpinionGroup().size(); i++){
      if(isLower(getOpinionGroup().get(i), DELTA)){
        count ++;
      }
    }
    
    setQSDQ(count / numAgents);
  }
  
  /**
   * Function to update the strongest disagreement indicator in the group
   */
  private void updateQSDI(){
    Double auxQSDI = Double.MAX_VALUE;
    
    System.out.println(auxQSDI);
    
    for(int i = 0; i < getAgentSimilarity().getRows(); i++){
      for(int j = 0; j < getAgentSimilarity().getColumns(); j++){
        if((auxQSDI > getAgentSimilarity().getItem(i, j)) && (i != j)){
          auxQSDI = getAgentSimilarity().getItem(i, j);
        }
      }
    }
    
    setQSDI(auxQSDI);
  }
  
  /**
   * Function to update the strong agreement between the decision-maker and the group
   */
  private void updateISAQ(){
    ArrayList<Double> auxISAQ = new ArrayList<Double> ();
    for(int i = 0; i < getAgentSimilarity().getRows(); i++){
      Double iISAQ = Double.MIN_VALUE;
      for(int j = 0; j < getAgentSimilarity().getColumns(); j++){
        
        if(!isLower(getAgentSimilarity().getItem(i, j), ALPHA)){
          iISAQ ++;
        }
        
        auxISAQ.add(iISAQ / getNumAgents());
      }
    }
    
    setISAQ(auxISAQ);
  }
  
  /**
   * Function to update the degree of strong disagreement between the decision maker and the group
   */
  private void updateISDQ(){
    ArrayList<Double> auxISDQ = new ArrayList<Double> ();
    for(int i = 0; i < getAgentSimilarity().getRows(); i++){
      Double iISDQ = Double.MIN_VALUE;
      for(int j = 0; j < getAgentSimilarity().getColumns(); j++){
        
        if(isLower(getAgentSimilarity().getItem(i, j), DELTA)){
          iISDQ ++;
        }
        
        auxISDQ.add(iISDQ / getNumAgents());
      }
    }
    
    setISDQ(auxISDQ);
  }
  
  /**
   * Function to update the degree of stronger disagreement indicator of the decision-maker and the group
   */
  private void updateISDI(){
    
  }
  
  /**
   * Function to check that a value is lower
   */
  private boolean isLower(double value1, double value2) {
    if(value1 < value2) {
      return true;
    }
    
    return false;
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
  
  /**
   * Print the ISAQ
   */
  private void writeISAQ(){
    for(int i = 0; i < getISAQ().size(); i++){
      System.out.print(getISAQ().get(i));
      
      if(getISAQ().size() - 1 > i){
        System.out.print(" - ");
      }
    }
  }
  
  /**
   * Print the ISDQ
   */
  private void writeISDQ(){
    for(int i = 0; i < getISDQ().size(); i++){
      System.out.print(getISDQ().get(i));
      
      if(getISDQ().size() - 1 > i){
        System.out.print(" - ");
      }
    }
  }
  
  /**
   * Function to print a opinion
   * @param opinion
   */
  private void writeOpinion(ArrayList<Double> opinion){
    for(int i = 0; i < opinion.size(); i++){
      System.out.print(opinion.get(i));
      
      if(i < opinion.size() - 1){
        System.out.print(" - ");
      }
    }
  }
  
  public void writeData() {
    System.out.println("\nGroup Opinion:");
    writeOpinion(getOpinionGroup());
    System.out.print("\n");
    
    System.out.println("\nGroup Similarity:");
    writeGroupSimilitary();
    System.out.print("\n");
    
    System.out.println("\nAgents Similarity:");
    writeAgentSimilitarity();
    
    System.out.println("\nQSAQ:");
    System.out.println(getQSAQ());
    
    System.out.println("\nQSDQ:");
    System.out.println(getQSDQ());
    
    System.out.println("\nQSDI:");
    System.out.println(getQSDI());
    
    System.out.println("\nISAQ:");
    writeISAQ();
    
    System.out.println("\nISDQ:");
    writeISDQ();
  }
  
  public ArrayList<MessagePack> getAllOpinions() { return allOpinions; }

  public void setAllOpinions(ArrayList<MessagePack> allOpinions) { this.allOpinions = allOpinions; }
  
  public ArrayList<Double> getOpinionGroup() { return opinionGroup; }

  public void setOpinionGroup(ArrayList<Double> opinionGroup) { this.opinionGroup = opinionGroup; }
  
  public ArrayList<Double> getGroupSimilarity() { return groupSimilarity; }

  public void setGroupSimilarity(ArrayList<Double> groupSimilarity) { this.groupSimilarity = groupSimilarity; }

  public SimilitudeMatrix getAgentSimilarity() { return agentSimilarity; }

  public void setAgentSimilarity(SimilitudeMatrix agentSimilarity) { this.agentSimilarity = agentSimilarity; }
  
  public Integer getNumAgents() { return numAgents; }

  public void setNumAgents(Integer numAgents) { this.numAgents = numAgents; }
  
  public Double getQSAQ() { return QSAQ; }

  public void setQSAQ(Double QSAQ) { this.QSAQ = QSAQ; }

  public Double getQSDQ() { return QSDQ; }

  public void setQSDQ(Double QSDQ) { this.QSDQ = QSDQ; }

  public Double getQSDI() { return QSDI; }

  public void setQSDI(Double QSDI) { this.QSDI = QSDI; }
  
  public ArrayList<Double> getISAQ() { return ISAQ; }

  public void setISAQ(ArrayList<Double> ISAQ) { this.ISAQ = ISAQ; }

  public ArrayList<Double> getISDQ() { return ISDQ; }

  public void setISDQ(ArrayList<Double> ISDQ) { this.ISDQ = ISDQ;
  }

  public ArrayList<Double> getISDI() { return ISDI; }

  public void setISDI(ArrayList<Double> ISDI) { this.ISDI = ISDI; }
}
