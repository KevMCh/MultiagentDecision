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
 * Importance of an attribute.
 */

package agents;

public class Importance {
  private Integer type;
  private Double value;
  
  /**
   * Default builder
   * @param type
   * @param value
   */
  public Importance (Integer type, Double value) {
    this.type = type;
    this.value = value;
  }
  
  /**
   * Print the importance class
   */
  public void writeImportance(){
    System.out.println("\t\tType: " + getType());
    System.out.println("\t\tValue: " + getValue());
  }
  
  public Integer getType() { return type; }

  public void setType(Integer type) { this.type = type; }

  public Double getValue() { return value; }

  public void setValue(Double value) { this.value = value; }
}
