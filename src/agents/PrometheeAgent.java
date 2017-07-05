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
 * Agent that uses the Promethee algorithm to get your opinion.
 */

package agents;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import messages.Opinion;

public class PrometheeAgent extends GeneralAgent {
  
  private final static double ALPHA = 0.6;              // Value to check the functions of preference
  
  private ArrayList<Double> weighing;                   // Weighing of attributes
  private Matrix decisionMatrix;                        // Decision matrix
  private ArrayList<Matrix> preferences;                // Preferences between options according to attribute weights
  private Matrix indexPreference;                       // Alternate preference index
  private ArrayList<Double> strength;                   // Strength of the alternative
  private ArrayList<Double> weakness;                   // Weakness of the alternative
  

  /**
   * Default build
   */
  public PrometheeAgent () {
    weighing = new ArrayList<Double> ();
    decisionMatrix = new Matrix();
    
    createOpinion("AgentPromethee");
  }
  
  /**
   * Funtion to create the agent opinion
   */
  public void createOpinion(String fileName) {
    readValues(fileName, getWeighing(), getDecisionMatrix());
    
    createPreferences();
    createIndexPreferenceMatrix();
    
    createStrength();
    createWeakness();
    
    createOpinion();
  }

  /**
   * File to read the data
   * @param fileName
   * @param weighing
   * @param characteristics
   */
  private static void readValues(String fileName, ArrayList<Double> weighing, Matrix decisionMatrix) {
    String pathProject = System.getProperty("user.dir");
    File file = new File (pathProject + PATHFILES + fileName);
    FileReader fr;
    
    try {
      fr = new FileReader (file);
      BufferedReader br = new BufferedReader(fr);

      String line;
      int numLine = 0;
      while((line = br.readLine()) != null){
        
        if(!line.equals("")) {
          String[] values = line.split(" ");
          
          switch(numLine){
            case 0:{
              for(int i = 0; i < values.length; i++){
                weighing.add(Double.parseDouble(values[i]));
              }
              break;
            }
            
            default:{
              for(int i = 0; i < values.length; i++){
                decisionMatrix.addItem(Double.parseDouble(values[i]));
              }
            }
          }
          
          numLine ++;
        }
      }
      
      decisionMatrix.setRows(numLine - 1);
      decisionMatrix.setColumns(weighing.size());
                              
    } catch (FileNotFoundException e) {
      e.printStackTrace();
      
    } catch (IOException e) {
      e.printStackTrace();
    }    
  }
  
  /**
   * Function to create the preference matrix
   */
  private void createPreferences() {
    setPreferences(new ArrayList<Matrix> ());

    for(int attribute = 0; attribute < getWeighing().size(); attribute++) {
      Matrix matrixPreference = new Matrix(getDecisionMatrix().getRows(),
          getDecisionMatrix().getRows());
      
      for(int row = 0; row < getDecisionMatrix().getRows(); row++) {
        for(int column = 0; column < getDecisionMatrix().getRows(); column++) {
          matrixPreference.addItem(preferenceFunctionI(getDecisionMatrix().getItem(row, attribute) -
              getDecisionMatrix().getItem(column, attribute)));                
        }
      }  
      getPreferences().add(matrixPreference);
    }
  }
  
  /**
   * Function to preference, The first alternative is 
   * preferred to the second according to that attribute
   * if it has a higher value.
   */
  public Double preferenceFunctionI(Double value) {
    if(value > 0.0) {
      return 1.0;
    }
    
    return 0.0;
  }
  
  /**
   * Function to preference, the first alternative is 
   * preferred to the second according to that attribute
   * if it has a higher value.
   */
  public Double preferenceFunctionII(Double value) {
    if(value > 0.0) {
      return 1.0;
    }
    
    return 0.0;
  }
  
  /**
   * Function to preference, the degree to which the first 
   * alternative is preferred to the second alternative according
   *  to that attribute grows linearly from 0 to the alpha 
   *  threshold. From this threshold, it is constantly equal to 1.
   */
  public Double preferenceFunctionIII(Double value) {
    if(value > ALPHA) {
      return 1.0;
      
    } else if(value < 0.0) {
      return 0.0;
    }
    
    return value / ALPHA;
  }
  
  /**
   * Create the index preference matrix
   */
  private void createIndexPreferenceMatrix() {
    setIndexPreference(new Matrix(getDecisionMatrix().getRows(), getDecisionMatrix().getRows()));
        
    for(int row = 0; row < getDecisionMatrix().getRows(); row++) {
      for(int column = 0; column < getDecisionMatrix().getRows(); column++) {
        Double indexValue = 0.0; 
        for(int attribute = 0; attribute < getPreferences().size(); attribute++) {
          indexValue += getWeighing().get(attribute) * getPreferences().get(attribute).getItem(row, column);
        }
        
        getIndexPreference().addItem(indexValue);
      }
    }
  }
  
  /**
   * Create strength array of the alternatives
   */
  private void createStrength() {
    setStrength(new ArrayList<Double> ());
    
    Double strengthAlternative;
    
    for(int row = 0; row < getIndexPreference().getRows(); row++) {
      strengthAlternative = 0.0;
      for(int column = 0; column < getIndexPreference().getColumns(); column++) {
        strengthAlternative += getIndexPreference().getItem(row, column);
      }
      
      getStrength().add(strengthAlternative / (getIndexPreference().getRows() - 1));
    }
  }
  
  /**
   * Create weakness array of the alternatives
   */
  private void createWeakness() {
    setWeakness(new ArrayList<Double> ());
    
    Double weaknessAlternative;
    
    for(int row = 0; row < getIndexPreference().getRows(); row++) {
      weaknessAlternative = 0.0;
      for(int column = 0; column < getIndexPreference().getColumns(); column++) {
        weaknessAlternative += getIndexPreference().getItem(column, row);
      }
      
      getWeakness().add(weaknessAlternative / (getIndexPreference().getRows() - 1));
    }
  }
  
  /**
   * Create the final opinion of the agent
   */
  private void createOpinion() {
    ArrayList<Double> valueOpinion = new ArrayList<Double> ();
    
    for(int i = 0; i < getStrength().size(); i++) {
      valueOpinion.add(getStrength().get(i) - getWeakness().get(i));
    }
    
    int indexMin = -1;
    for(int i = 0; i < valueOpinion.size(); i++) {
      
      if(valueOpinion.get(i) <= 0.0) {
        if(indexMin == -1) {
          indexMin = i;
        } else if(valueOpinion.get(i) <= valueOpinion.get(indexMin)){
          
          indexMin = i;
        }
      }
    }
    
    Double toSum = (1 + Math.abs(valueOpinion.get(indexMin)));
    Double totalSum = 0.0;
    
    for(int i = 0; i < valueOpinion.size(); i++) {
      valueOpinion.set(i,
          (valueOpinion.get(i) + toSum));
      
      totalSum += valueOpinion.get(i);
    }
    
    for(int i = 0; i < valueOpinion.size(); i++) {
      valueOpinion.set(i,
          (valueOpinion.get(i) / totalSum));      
    }
    
    Opinion opinion = new Opinion ();
    opinion.setValueOpinion(valueOpinion);
    
    setOpinion(opinion);
  }
  
  /**
   * Write the data
   */
  public void writeData() {
    writeWeighing();
    writeDecisionMatrix();
    writePreferenceMatrix();
    writeIndexPreference();
    writeStrength();
    writeWeakness();
    writeOpinion();
  }
  
  /**
   * Write the decision matrix
   */
  public void writeWeighing() {
    System.out.println("Weighing:");
    for(int i = 0; i < getWeighing().size(); i++) {
      System.out.print(getWeighing().get(i) + " ");
    }
    System.out.println("\n");
  }
  
  /**
   * Write the decision matrix
   */
  public void writeDecisionMatrix() {
    System.out.println("Decision Matrix:");
    getDecisionMatrix().printMatrix();
    System.out.println();
  }
  
  /**
   * Write the preference matrix
   */
  public void writePreferenceMatrix() {
    System.out.println("Preference Matrix:");
    for(int attribute = 0; attribute < getPreferences().size(); attribute++) {
      System.out.println("Attribute " + (attribute + 1) + ":");
      getPreferences().get(attribute).printMatrix();
    }
    System.out.println();
  }
  
  /**
   * Write the index preference matrix
   */
  public void writeIndexPreference() {
    System.out.println("Index Preference Matrix:");
    getIndexPreference().printMatrix();
    System.out.println();
  }
  
  /**
   * Write the strength of the alternatives
   */
  private void writeStrength() {
    System.out.println("Strength of the alternatives:");
    for(int i = 0; i < getStrength().size(); i++) {
      System.out.print(getStrength().get(i) + " ");
    }
    System.out.println("\n");
  }
  
  /**
   * Write the weakness of the alternatives
   */
  private void writeWeakness() {
    System.out.println("Weakness of the alternatives:");
    for(int i = 0; i < getWeakness().size(); i++) {
      System.out.print(getWeakness().get(i) + " ");
    }
    System.out.println("\n");
  }
  
  public ArrayList<Double> getWeighing() { return weighing; }

  public void setWeighing(ArrayList<Double> weighing) { this.weighing = weighing; }
  
  public Matrix getDecisionMatrix() { return decisionMatrix; }

  public void setDecisionMatrix(Matrix decisionMatrix) { this.decisionMatrix = decisionMatrix; }
  
  public ArrayList<Matrix> getPreferences() { return preferences; }

  public void setPreferences(ArrayList<Matrix> preferences) { this.preferences = preferences; }
  
  public Matrix getIndexPreference() { return indexPreference; }

  public void setIndexPreference(Matrix indexPreference) { this.indexPreference = indexPreference; }
  
  public ArrayList<Double> getStrength() { return strength; }

  public void setStrength(ArrayList<Double> strength) { this.strength = strength; }

  public ArrayList<Double> getWeakness() { return weakness; }

  public void setWeakness(ArrayList<Double> weakness) { this.weakness = weakness; }
}
