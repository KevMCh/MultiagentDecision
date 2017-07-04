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
 * Agent using the Analytic Hierarchy Process to get your opinion.
 */

package agents;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import messages.Opinion;

public class AHPAgent extends GeneralAgent {
  
  private Matrix attributes;                                              // Matrix for the importance of attributes
  private ArrayList<Double> priorityAttributes;                           // Priority of the attributes
  private ArrayList<Matrix> comparativeAttributes;                        // Comparative according to attributes
  private ArrayList<ArrayList<Double>> priorityComparativeAttributes;     // Priority of the comparative according to attributes
  private Matrix priorityMatrix;                                          // Priority matrix of the comparative

  /**
   * Default build
   */
  public AHPAgent (int numbOptions) {
    
    createOpinion(numbOptions);
  }
  
  /**
   * Funtion to create the agent opinion
   */
  public void createOpinion(int numbOptions) {
    String fileName = "AgentAHP";
    
    readAttributes(fileName + "Attributes");
    setAttributes(normalizeMatrixByPairs(getAttributes()));
    setPriorityAttributes(calculatePriority(getAttributes()));
        
    setComparativeAttributes(new ArrayList<Matrix> ());
    setPriorityComparativeAttributes(new ArrayList<ArrayList<Double>> ());
    
    for(int attributes = 0; attributes < getPriorityAttributes().size(); attributes++){
      readValues(fileName + "OptionsAttributes" + (attributes + 1));
      getComparativeAttributes().set(attributes, normalizeMatrixByPairs(getComparativeAttributes().get(attributes)));
      
      getPriorityComparativeAttributes().add(calculatePriority(getComparativeAttributes().get(attributes)));
    }
    
    setPriorityMatrix(createPriorityMatrix(getPriorityComparativeAttributes()));

    createFinalOpinion();
  }

  /**
   * File to read the attributes
   * @param fileName
   */
  private void readAttributes(String fileName) {
    String pathProject = System.getProperty("user.dir");
    File file = new File (pathProject + PATHFILES + fileName);
    FileReader fr;
    try {
      fr = new FileReader (file);
      BufferedReader br = new BufferedReader(fr);

      String line;
      Integer row = 0;
      Integer column = 0;
      while((line = br.readLine()) != null){
        if(!line.equals("")) {
          String[] values = line.split(" ");
          
          if(row == 0){
            column = values.length;
            setAttributes(new Matrix(column, column));
          }
          row++;
          
          for(int i = 0; i < values.length; i++) {
            getAttributes().addItem(Double.parseDouble(values[i]));
          }
        }
      }
      
    } catch (FileNotFoundException e) {
      e.printStackTrace();
      
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * File to read the data
   * @param fileName
   */
  private void readValues(String fileName) {
    
    String pathProject = System.getProperty("user.dir");
    File file = new File (pathProject + PATHFILES + fileName);
    FileReader fr;
    try {
      fr = new FileReader (file);
      BufferedReader br = new BufferedReader(fr);

      String line;
      Integer row = 0;
      Integer column = 0;
      
      Matrix attributesOption = null;
      while((line = br.readLine()) != null){
        if(!line.equals("")) {
          String[] values = line.split(" ");
          
          if(row == 0){
            column = values.length;
            attributesOption = new Matrix(column, column);
          }
          row++;
          
          for(int i = 0; i < values.length; i++) {
            attributesOption.addItem(Double.parseDouble(values[i]));
          }
        }
      }
      
      
      getComparativeAttributes().add(attributesOption);
      
    } catch (FileNotFoundException e) {
      e.printStackTrace();
      
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  
  /**
   * Calculate the priority of the attributes
   */
  private ArrayList<Double> calculatePriority(Matrix matrix) {
    ArrayList<Double> priority = new ArrayList<Double> ();
    
    for(int i = 0; i < matrix.getRows(); i++) {
      Double sum = 0.0;
      for(int j = 0; j < matrix.getColumns(); j++) {
        sum += matrix.getItem(i, j);
      }
      
      priority.add(sum / matrix.getColumns());
    } 
    
    return priority;
  }
  
  private Matrix createPriorityMatrix(ArrayList<ArrayList<Double>> priorityComparativeAttributes) {
    Matrix priorityMatrix = new Matrix(priorityComparativeAttributes.get(0).size(),
                                       priorityComparativeAttributes.size());
    
    for(int i = 0; i < priorityMatrix.getRows(); i++) {
      for(int j = 0; j < priorityMatrix.getColumns(); j++) {
        priorityMatrix.addItem(priorityComparativeAttributes.get(j).get(i));
      }
    }
    
    return priorityMatrix;
  }
  
  /**
   * Function to normalize a matrix by attributes
   * @param attributes
   * @return
   */
  private Matrix normalizeMatrixByPairs(Matrix attributes) {
    Matrix normalizeAttributes = new Matrix(attributes.getRows(),
                                            attributes.getColumns());
    
    ArrayList<Double> sumatory = new ArrayList<Double> ();
    
    for(int column = 0; column < attributes.getColumns(); column++) {
      Double sum = 0.0;
      for(int row = 0; row < attributes.getRows(); row++) {
        sum += attributes.getItem(row, column);
      }
      sumatory.add(sum);
    }
        
    for(int row = 0; row < attributes.getRows(); row++) {
      for(int column = 0; column < attributes.getColumns(); column++) {        
        normalizeAttributes.addItem(attributes.getItem(row, column) / sumatory.get(column));
      }
    }

    return normalizeAttributes;
  }
  
  /**
   * Funtion to create the agent opinion
   */
  private void createFinalOpinion() {
    Double itemValue;
    ArrayList<Double> valueOpinion = new ArrayList<Double> ();
    
    for(int row = 0; row < getPriorityMatrix().getRows(); row++){
      itemValue = 0.0;
      
      for(int column = 0; column < getPriorityMatrix().getColumns(); column++) {
        itemValue += getPriorityMatrix().getItem(row, column) * getPriorityAttributes().get(column);
      }
      valueOpinion.add(itemValue);
    }
        
    Opinion opinion = new Opinion ();
    opinion.setValueOpinion(valueOpinion);
    setOpinion(opinion);    
  }
  
  /**
   * Write all data from agent
   */
  public void writeDataAgent() {
    writeAttributesMatrix();
    writePriorityAttributes();
    writeComparativeAttributes();
    writePriorityComparativeAttributes();
    writePriorityMatrix();
    writeOpinion();
  }
  
  /**
   * Write the value of the attributes
   */
  private void writeAttributesMatrix() {
    System.out.println("Attributes values");
    getAttributes().printMatrix();
    System.out.println();
  }
  
  /**
   * Write the priority
   */
  private void writePriorityAttributes() {
    System.out.println("Priority Attributes:");
    for(int i = 0; i < getPriorityAttributes().size(); i++) {
      System.out.print(getPriorityAttributes().get(i) + " ");
    }
    System.out.println("\n");
  }
  
  /**
   * Write all attributes for all options
   */
  private void writeComparativeAttributes() {
    System.out.println("Comparative Attributes:");
    
    for(int i = 0; i < getComparativeAttributes().size(); i++) {
      System.out.println("\nAttribute " + (i + 1) + ":");
      getComparativeAttributes().get(i).printMatrix();
    }
  }
  
  /**
   * Write all priority of the comparative according to attributes
   */
  private void writePriorityComparativeAttributes() {
    
    System.out.println("Priority of the comparative according to attributes:\n");
    
    for(int i = 0; i < getPriorityComparativeAttributes().size(); i++) {
      System.out.println("Attribute " + (i + 1) + ":");
      for(int j = 0; j < getPriorityComparativeAttributes().get(i).size(); j++) {
        System.out.print(getPriorityComparativeAttributes().get(i).get(j) + " ");
      }
      System.out.println("\n");
    }
  }
  
  /**
   * Write the priority matrix
   */
  private void writePriorityMatrix() {
    System.out.println("Priority Matrix:");
    getPriorityMatrix().printMatrix();
    System.out.println();
  }
  
  public Matrix getAttributes() { return attributes; }

  public void setAttributes(Matrix attributes) { this.attributes = attributes; }
  
  public ArrayList<Double> getPriorityAttributes() { return priorityAttributes; }

  public void setPriorityAttributes(ArrayList<Double> priorityAttributes) { this.priorityAttributes = priorityAttributes; }
  
  public ArrayList<Matrix> getComparativeAttributes() { return comparativeAttributes; }

  public void setComparativeAttributes(ArrayList<Matrix> comparativeAttributes) { this.comparativeAttributes = comparativeAttributes; }
  
  public ArrayList<ArrayList<Double>> getPriorityComparativeAttributes() { return priorityComparativeAttributes; }

  public void setPriorityComparativeAttributes(ArrayList<ArrayList<Double>> priorityComparativeAttributes) { this.priorityComparativeAttributes = priorityComparativeAttributes; }
  
  public Matrix getPriorityMatrix() { return priorityMatrix; }

  public void setPriorityMatrix(Matrix priorityMatrix) { this.priorityMatrix = priorityMatrix; }
}
