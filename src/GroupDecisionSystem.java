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
 * Main class.
 */

import agents.AHPAgent;
import agents.ElectreAgent;
import agents.GeneralAgent;
import agents.PrometheeAgent;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.ControllerException;
import jade.wrapper.StaleProxyException;

public class GroupDecisionSystem {
  
  private static final String MODERATOR = "Moderator";            // Moderator constant
  private static final String AGENT = "Agent";                    // Agent constant
  
  private static final String ADDRESS = "localhost";              // Address to the profile
  private static final String PORT = "1337";                      // Port to run the agents
  
  private static final Integer NUMAGENTS = 12;                    // Number of agents
  private static final double ACCORD = 0.85;                      // Value of agreement of the agents
  private static final double DISAGREEMENT = 0.4;                 // Value of disagreement of the agents
  private static final int NUMBEROPTIONS = 4;                     // Number of options
  
  /**
   * Main function
   * @param args
   */
  public static void main(String[] args) {
    
    /* AHPAgent ahpAgent = new AHPAgent (NUMBEROPTIONS);
    ahpAgent.writeDataAgent(); */
    
    /* ElectreAgent electre = new ElectreAgent ();
    electre.writeAtributtes(); */
    
    /* PrometheeAgent prometheeAgent = new PrometheeAgent ();
    prometheeAgent.writeData(); */
    
    Runtime runtime = Runtime.instance();
    Profile profile = createProfile();
    ContainerController containerController = runtime.createMainContainer(profile);
    
    Object[] argsAgent = {NUMAGENTS, ACCORD, DISAGREEMENT};
    
    createNewAgent(containerController, MODERATOR, "agents.ModeratorAgent", argsAgent);

    for(int i = 1; i <= NUMAGENTS; i++) {
      Object[] argsAgentClient = { AGENT + i };
      createNewAgent(containerController, AGENT + i, "agents.GeneralAgent", argsAgentClient);
    }
  }
  
  /**
   * Function to create a new agent
   * @param contrainerController
   * @param name
   * @param packageAgent
   * @param numAgents
   */
  private static void createNewAgent(ContainerController contrainerController, String name, String packageAgent, Object[] numAgents) {
    AgentController agentController;
    
    try{
      agentController = contrainerController.createNewAgent(name, packageAgent, numAgents);
      agentController.start();
    } catch (StaleProxyException e){
      e.printStackTrace();
    }
  }

  /**
   * Create the profile of the jade project
   * @return profile
   */
  private static Profile createProfile() {
    Profile profile = new ProfileImpl();
    profile.setParameter(Profile.MAIN_HOST, ADDRESS);
    profile.setParameter(Profile.MAIN_PORT, PORT);
    // profile.setParameter(Profile.GUI, "true");
    
    return profile;
  }
}