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

public class AHPAgent extends GeneralAgent {
  
  /**
   * Default build
   */
  public AHPAgent () {
    
  }
  
  /**
   * Funtion to create the agent opinion
   */
  public void createOpinion(String fileName) {
    readOpinion(fileName);
  }
  
  /**
   * File to read the data
   * @param fileName
   */
  private void readOpinion(String fileName) {
    String pathProject = System.getProperty("user.dir");
    File file = new File (pathProject + PATHFILES + fileName);
    FileReader fr;
    try {
      fr = new FileReader (file);
      BufferedReader br = new BufferedReader(fr);

      String line = br.readLine();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
      
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
