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
 * Agent that uses the Electre algorithm to get your opinion.
 */

package agents;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import messages.Opinion;

public class ElectreAgent extends GeneralAgent {
  
  private final static double AGREEMENTVALUE = 0.7;             // Constant of the agreement value
  private final static double DISCONCORDANCEVALUE = 0.3;        // Constant of the disconcordance value
  
  private ArrayList<Importance> importances;                    // Importance and types of the atributtes
  private Matrix characteristics;                               // Value of each characteristics
  private Matrix agreement;                                     // Agreement matrix
  private Matrix disconcordance;                                // Disconcordance matrix
  private Matrix indexDisconcordance;                           // Index of disconcordance matrix
  private Matrix dominanceByAgreement;                          // Dominance by agreement matrix
  private Matrix dominanceByDisconcordance;                     // Dominance by disconcordance matrix
  private Matrix aggregateDominance;                            // Aggregate Dominance
  private ArrayList<Double> strength;                           // Strength of the alternative
  private ArrayList<Double> weakness;                           // Weakness of the alternative

  /**
   * Default build
   */
  public ElectreAgent () {
    importances = new ArrayList<Importance> ();
    characteristics = new Matrix ();
    
    createOpinion("AgentElectre");
  }
  
  /**
   * Funtion to create the agent opinion
   */
  private void createOpinion(String fileName) {
    readValues(fileName, getImportances(), getCharacteristics());
    
    setCharacteristics(getCharacteristics().normalizeMatrix());
    weighMatrix();

    createAgreementMatrix();
    createDisconcordanceMatrix();
    createIndexDisconcordance();
    
    createDominanceByAgreement();
    createDominanceByDisconcordance();
    
    createAggregateDominance();
    
    createStrength();
    createWeakness();
    
    createOpinion();
  }

  /**
   * Create the dominance by agreement
   */
  private void createDominanceByAgreement() {
    setDominanceByAgreement(new Matrix(getAgreement().getRows(),
                                       getAgreement().getColumns()));
    
    for(int i = 0; i < getAgreement().getRows(); i++){
      for(int j = 0; j < getAgreement().getColumns(); j++){
        
        if(AGREEMENTVALUE < getAgreement().getItem(i, j)) {
          getDominanceByAgreement().addItem(1.0);
        } else {
          getDominanceByAgreement().addItem(0.0);
        }
      }
    }
  }
  
  /**
   * Create the dominance by disconcordance
   */
  private void createDominanceByDisconcordance() {
    setDominanceByDisconcordance(new Matrix(getIndexDisconcordance().getRows(),
                                       getIndexDisconcordance().getColumns()));
    for(int i = 0; i < getIndexDisconcordance().getRows(); i++){
      for(int j = 0; j < getIndexDisconcordance().getColumns(); j++){
        
        if(DISCONCORDANCEVALUE > getIndexDisconcordance().getItem(i, j)) {
          getDominanceByDisconcordance().addItem(1.0);
        } else {
          getDominanceByDisconcordance().addItem(0.0);
        }
      }
    }
  }
  
  /**
   * Create the aggregate dominance
   */
  private void createAggregateDominance() {
    setAggregateDominance(new Matrix(getAgreement().getRows(),
                                     getAgreement().getColumns()));
    
    for(int i = 0; i < getDominanceByAgreement().getRows(); i++){
      for(int j = 0; j < getDominanceByAgreement().getColumns(); j++){
        
        if(getDominanceByAgreement().getItem(i, j) == 1 &&
           getDominanceByDisconcordance().getItem(i, j) == 1) {
          
          getAggregateDominance().addItem(1.0);
        } else {
          getAggregateDominance().addItem(0.0);
        }
      }
    }
  }

  /**
   * File to read the data
   * @param fileName
   * @param importance
   * @param characteristics
   */
  private static void readValues(String fileName, ArrayList<Importance> importances, Matrix characteristics) {
    String pathProject = System.getProperty("user.dir");
    File file = new File (pathProject + PATHFILES + fileName);
    FileReader fr;
    
    try {
      fr = new FileReader (file);
      BufferedReader br = new BufferedReader(fr);

      String line;
      
      int numLine = 0;
      ArrayList<Double> data = new ArrayList<Double> ();
      ArrayList<Integer> types = new ArrayList<Integer> ();
      while((line = br.readLine()) != null){
        
        if(!line.equals("")) {
          String[] values = line.split(" ");
          
          switch(numLine){
            case 0:{
              for(int i = 0; i < values.length; i++){
                types.add(Integer.parseInt(values[i]));
              }
              break;
            }
            case 1: {
              for(int i = 0; i < values.length; i++){
                Double percentage = Double.parseDouble(values[i]);
                Importance importance = new Importance(types.get(i), percentage);
                importances.add(importance);
              }              
              break;
            }
            
            default:{
              for(int i = 0; i < values.length; i++){
                data.add(Double.parseDouble(values[i]));
              }
            }
          }
          
          numLine ++;
        }
      }
      characteristics.setRows(numLine - 2);
      characteristics.setColumns(importances.size());
      characteristics.setMatrix(data);      
      
                        
    } catch (FileNotFoundException e) {
      e.printStackTrace();
      
    } catch (IOException e) {
      e.printStackTrace();
    }    
  }
  
  /**
   * Weight the matrix
   */
  private void weighMatrix() {
    for(int i = 0; i < getCharacteristics().getColumns(); i++) {
      for(int j = 0; j < getCharacteristics().getRows(); j++) {
        getCharacteristics().updateItem(j, i, 
            getCharacteristics().getItem(j, i) * getImportances().get(i).getValue());
      }
    }
  }
  
  /**
   * Create the agreement matrix of the options
   */
  private void createAgreementMatrix(){
    setAgreement(new Matrix(getCharacteristics().getRows(), getCharacteristics().getRows()));
    Double agreementValue;

    for(int row1 = 0; row1 < getCharacteristics().getRows(); row1++) {
      for(int row2 = 0; row2 < getCharacteristics().getRows(); row2++) {
        agreementValue = 0.0;
        
        if(row1 <= row2){
          for(int column = 0; column < getCharacteristics().getColumns(); column++) {
               
            if(getImportances().get(column).getType() == 1) {

              if(getCharacteristics().getItem(row1, column) >= getCharacteristics().getItem(row2, column)) {
                agreementValue += getImportances().get(column).getValue();
              }
              
            } else {
              if(getCharacteristics().getItem(row1, column) <= getCharacteristics().getItem(row2, column)) {
                agreementValue += getImportances().get(column).getValue();
              }
            }
          }
          
        } else {
          agreementValue = 1 - getAgreement().getItem(row2, row1);
        }
        
        getAgreement().addItem(agreementValue);
      }
    }    
  }
  
  /**
   * Create the disconcordance matrix of the options
   */
  private void createDisconcordanceMatrix() {
    setDisconcordance(new Matrix(getCharacteristics().getRows(), getCharacteristics().getRows()));
    Double disconcordanceValue;

    for(int row1 = 0; row1 < getCharacteristics().getRows(); row1++) {
      for(int row2 = 0; row2 < getCharacteristics().getRows(); row2++) {
        disconcordanceValue = 0.0;
        
        if(row1 <= row2){
          for(int column = 0; column < getCharacteristics().getColumns(); column++) {
               
            if(getImportances().get(column).getType() == 1) {

              if(getCharacteristics().getItem(row1, column) < getCharacteristics().getItem(row2, column)) {
                disconcordanceValue += getImportances().get(column).getValue();
              }
              
            } else {
              if(getCharacteristics().getItem(row1, column) > getCharacteristics().getItem(row2, column)) {
                disconcordanceValue += getImportances().get(column).getValue();
              }
            }
          }
          
        } else {
          disconcordanceValue = 1 - getAgreement().getItem(row2, row1);
        }
        
        getDisconcordance().addItem(disconcordanceValue);
      }
    }    
  }
  
  /**
   * Create the index of disconcordance matrix
   */
  private void createIndexDisconcordance() {
    setIndexDisconcordance(new Matrix(getCharacteristics().getRows(), getCharacteristics().getRows()));
    Double dividendo;
    Double divisor;

    for(int row1 = 0; row1 < getCharacteristics().getRows(); row1++) {
      for(int row2 = 0; row2 < getCharacteristics().getRows(); row2++) {
        
        if(row1 != row2){
          dividendo = Double.MIN_VALUE;
          divisor = Double.MIN_VALUE;
          
          for(int column = 0; column < getCharacteristics().getColumns(); column++) {
               
            if(getImportances().get(column).getType() == 1) {

              if(getCharacteristics().getItem(row1, column) < getCharacteristics().getItem(row2, column) &&
                 dividendo < Math.abs(getCharacteristics().getItem(row1, column) - getCharacteristics().getItem(row2, column))) {
                
                dividendo = Math.abs(getCharacteristics().getItem(row1, column) - getCharacteristics().getItem(row2, column));
              }
              
            } else {
              if(getCharacteristics().getItem(row1, column) > getCharacteristics().getItem(row2, column) &&
              dividendo < Math.abs(getCharacteristics().getItem(row1, column) - getCharacteristics().getItem(row2, column))) {
                
                dividendo = Math.abs(getCharacteristics().getItem(row1, column) - getCharacteristics().getItem(row2, column));
              }
            }
            
            if(divisor < Math.abs(getCharacteristics().getItem(row1, column) - getCharacteristics().getItem(row2, column))) {
              divisor = Math.abs(getCharacteristics().getItem(row1, column) - getCharacteristics().getItem(row2, column));
            }
          }
          
          getIndexDisconcordance().addItem(dividendo / divisor);
          
        } else {
          getIndexDisconcordance().addItem(0.0);
        }
        
        
      }
    }    
  }
  
  /**
   * Create strength array of the alternatives
   */
  private void createStrength() {
    setStrength(new ArrayList<Double> ());
    
    Double strengthAlternative;
    
    for(int row = 0; row < getAggregateDominance().getRows(); row++) {
      strengthAlternative = 0.0;
      for(int column = 0; column < getAggregateDominance().getColumns(); column++) {
        strengthAlternative += getAggregateDominance().getItem(row, column);
      }
      
      getStrength().add(strengthAlternative / (getAggregateDominance().getRows() - 1));
    }
  }
  
  /**
   * Create weakness array of the alternatives
   */
  private void createWeakness() {
    setWeakness(new ArrayList<Double> ());
    
    Double weaknessAlternative;
    
    for(int row = 0; row < getAggregateDominance().getRows(); row++) {
      weaknessAlternative = 0.0;
      for(int column = 0; column < getAggregateDominance().getColumns(); column++) {
        weaknessAlternative += getAggregateDominance().getItem(column, row);
      }
      
      getWeakness().add(weaknessAlternative / (getAggregateDominance().getRows() - 1));
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
   * Write all atributtes
   */
  public void writeAtributtes() {
    writeImportances();
    writeCharacteristics();
    writeAgreement();
    writeDisconcordance();
    writeIndexDisconcordance();
    writeDominanceByAgreement();
    writeDominanceByDisconcordance();
    writeAggregateDominance();
    writeStrength();
    writeWeakness();
    writeOpinion();
  }

  /**
   * Write the importances
   */
  private void writeImportances() {
    System.out.println("Importances:");
    for(int i = 0; i < getImportances().size(); i++){
      System.out.println("\tItem: " + i + ":");
      getImportances().get(i).writeImportance();
    }
    System.out.println();
  }
  
  /**
   * Write characteristics
   */
  private void writeCharacteristics() {
    System.out.println("Characteristics:");
    getCharacteristics().printMatrix();
    System.out.println();
  }
  
  /**
   * Write the agreement
   */
  private void writeAgreement() {
    System.out.println("Agreement:");
    getAgreement().printMatrix();
    System.out.println();
  }

  /**
   * Write disconcordance
   */
  private void writeDisconcordance() {
    System.out.println("Disconcordance:");
    getDisconcordance().printMatrix();
    System.out.println();
  }
  
  /**
   * Write index of disconcordance
   */
  private void writeIndexDisconcordance() {
    System.out.println("Index of disconcordance:");
    getIndexDisconcordance().printMatrix();
    System.out.println();
  }
  
  /**
   * Write the dominance by agreement matrix of the alternatives
   */
  private void writeDominanceByAgreement() {
    System.out.println("Dominance By Agreement:");
    getDominanceByAgreement().printMatrix();
    System.out.println();
  }
  
  /**
   * Write the dominance by disconcordance matrix of the alternatives
   */
  private void writeDominanceByDisconcordance() {
    System.out.println("Dominance By Disconcordance:");
    getDominanceByDisconcordance().printMatrix();
    System.out.println();
  }
  
  /**
   * Write the aggregate dominance matrix of the alternatives
   */
  private void writeAggregateDominance() {
    System.out.println("Aggregate dominance:");
    getAggregateDominance().printMatrix();
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

  public ArrayList<Importance> getImportances() { return importances; }

  public void setImportances(ArrayList<Importance> importances) { this.importances = importances; }

  public Matrix getCharacteristics() { return characteristics; }

  public void setCharacteristics(Matrix characteristics) { this.characteristics = characteristics; }
  
  public Matrix getAgreement() { return agreement; }

  public void setAgreement(Matrix agreement) { this.agreement = agreement; }

  public Matrix getDisconcordance() { return disconcordance; }

  public void setDisconcordance(Matrix disconcordance) { this.disconcordance = disconcordance; }
  
  public Matrix getIndexDisconcordance() { return indexDisconcordance; }

  public void setIndexDisconcordance(Matrix indexDisconcordance) { this.indexDisconcordance = indexDisconcordance; }
  
  public Matrix getDominanceByAgreement() { return dominanceByAgreement; }

  public void setDominanceByAgreement(Matrix dominanceByAgreement) { this.dominanceByAgreement = dominanceByAgreement; }

  public Matrix getDominanceByDisconcordance() { return dominanceByDisconcordance; }

  public void setDominanceByDisconcordance(Matrix dominanceByDisconcordance) { this.dominanceByDisconcordance = dominanceByDisconcordance; }
  
  public Matrix getAggregateDominance() { return aggregateDominance; }

  public void setAggregateDominance(Matrix aggregateDominance) { this.aggregateDominance = aggregateDominance; }
  
  public ArrayList<Double> getStrength() { return strength; }

  public void setStrength(ArrayList<Double> strength) { this.strength = strength; }

  public ArrayList<Double> getWeakness() { return weakness; }

  public void setWeakness(ArrayList<Double> weakness) { this.weakness = weakness; }
}