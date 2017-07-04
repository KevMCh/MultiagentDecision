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
 * Class to represent opinion.
 */

package messages;

import java.io.Serializable;
import java.util.ArrayList;

public class Opinion implements Serializable {
  
  private ArrayList<Double> valueOpinion;       // Vector to represent a opinion
  
  /**
   * Default builder
   */
  public Opinion () {
    valueOpinion = new ArrayList<Double> ();
  }
  
  /**
   * Write the value of the opinion
   */
  public void writeValueOpinion(){
    for(int i = 0; i < getValueOpinion().size(); i++) {
      System.out.print(getValueOpinion().get(i) + " ");
    }
  }
  
  public ArrayList<Double> getValueOpinion() { return valueOpinion; }
  
  public void setValueOpinion(ArrayList<Double> valueOpinion) { this.valueOpinion = valueOpinion; }
}
