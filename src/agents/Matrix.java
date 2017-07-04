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
 * Class to define a matrix.
 */
package agents;

import java.util.ArrayList;

public class Matrix {
  
  private Integer rows;                             // Number of rows
  private Integer columns;                          // Number of columns
  private ArrayList<Double> matrix;                 // Matrix of items
  
  /**
   * Builder
   */
  public Matrix () {
    rows = 0;
    columns = 0;
    matrix = new ArrayList<Double> ();
  }
  
  /**
   * Builder
   * @param rows
   * @param columns
   */
  public Matrix (Integer rows, Integer columns) {
    this.rows = rows;
    this.columns = columns;
    matrix = new ArrayList<Double> ();
  }
  
  /**
   * Function to add a new matrix item
   * @param matrix
   */
  public void addItem(Double value) {
    getMatrix().add(value);
  }
  
  /**
   * Function to print a matrix
   */
  public void printMatrix(){ 
    for(int i = 0; i < getRows(); i++){
      for(int j = 0; j < getColumns(); j++){
        System.out.printf("%.3f \t", getItem(i, j));
      }
      System.out.println("\n");
    }
  }
  
  /**
   * Function to get the position of a item
   * @param row
   * @param column
   * @return position
   */
  private int getPos(int row, int column){
    return row * this.getColumns() + column;
  }
  
  /**
   * Function to return a item of the matrix
   * @param row
   * @param column
   * @return item
   */
  public Double getItem(int row, int column) {
    return getMatrix().get(getPos(row, column));
  }
  
  /**
   * Function to update a item
   * @param row
   * @param colum
   * @param item
   */
  public void updateItem(int row, int column, Double item) {
    getMatrix().set(getPos(row, column), item);
  }
  
  /**
   * Function to normalize the matrix
   * @param matrix
   */
  protected Matrix normalizeMatrix() {
    Matrix normalizeMatrix = new Matrix (getRows(),
        getColumns());

    for(int i = 0; i < getRows(); i++) {
      for(int j = 0; j < getColumns(); j++) {
        
        Double item = getItem(i, j);
        Double sumSquare = 0.0;
            for(int k = 0; k < getRows(); k++) {
              sumSquare += Math.pow(getItem(k, j), 2);
            }

        item = item / (Math.sqrt(sumSquare));
        normalizeMatrix.addItem(item);
      }
    }

    return normalizeMatrix;    
  }
  
  public Integer getRows() { return rows; }

  public void setRows(Integer rows) { this.rows = rows; }

  public Integer getColumns() { return columns; }

  public void setColumns(Integer columns) { this.columns = columns; }
  
  public ArrayList<Double> getMatrix() { return matrix; }
  
  public void setMatrix(ArrayList<Double> matrix) { this.matrix = matrix; }
}
