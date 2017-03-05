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

package messages;

import java.io.Serializable;

public class MessagePack implements Serializable {
  
  private String name;              // Name of the agents sending the message
  private Opinion opinion;          // Agent's opinion
  
  /**
   * Default builder
   */
  public MessagePack () {}
  
  /**
   * Builder 
   * @param name
   * @param opinion
   */
  public MessagePack (String name, Opinion opinion) {
    this.name = name;
    this.opinion = opinion;
  }
  
  public String getName() { return name; }

  public void setName(String name) { this.name = name; }

  public Opinion getOpinion() { return opinion; }

  public void setOpinion(Opinion opinion) { this.opinion = opinion; }
}
