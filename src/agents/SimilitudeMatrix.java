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
 * Class to define the agents message.
 */
package agents;

import java.util.ArrayList;

public class SimilitudeMatrix {
  
  private Integer rows;                             // Number of rows
  private Integer columns;                          // Number of columns
  private ArrayList<Double> similitudeMatrix;       // Matrix to represent the similitude 
  
  /**
   * Default builder
   * @param rows
   * @param columns
   */
  public SimilitudeMatrix (Integer rows, Integer columns) {
    this.rows = rows;
    this.columns = columns;
    similitudeMatrix = new ArrayList<Double> ();
  }
  
  /**
   * Function to add a new matrix item
   * @param similitudeMatrix
   */
  public void addSimilitude(Double valueSimilitude) {
    getSimilitudeMatrix().add(valueSimilitude);
  }
  
  /**
   * Function to print a matrix
   */
  public void printSimilitudeMatrix(){ 
    for(int i = 0; i < getRows(); i++){
      for(int j = 0; j < getColumns(); j++){
        System.out.printf("%.3f \t", getItem(i, j));
      }
      System.out.println();
    }
  }
  
  /**
   * Function to get the position of a item
   * @param row
   * @param column
   * @return position
   */
  private int getPos(int row, int column){
    return row * getRows() + column;
  }
  
  /**
   * Function to return a item of the matrix
   * @param row
   * @param column
   * @return item
   */
  public Double getItem(int row, int column) {
    return getSimilitudeMatrix().get(getPos(row, column));
  }
  
  public Integer getRows() { return rows; }

  public void setRows(Integer rows) { this.rows = rows; }

  public Integer getColumns() { return columns; }

  public void setColumns(Integer columns) { this.columns = columns; }
  
  public ArrayList<Double> getSimilitudeMatrix() { return similitudeMatrix; }
  
  public void setSimilitudeMatrix(ArrayList<Double> similitudeMatrix) { this.similitudeMatrix = similitudeMatrix; }
}
