import java.util.ArrayList;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;

public class Main {
  
  private static final String PORT = "1337";
  private static final Integer NUMAGENTS = 5;
  
  public static void main(String[] args) {
    Runtime runtime = Runtime.instance();
    Profile profile = createProfile();
    ContainerController contrainerController = runtime.createMainContainer(profile);
    
    Object[] argsAgent = {NUMAGENTS};
    
    createNewAgent(contrainerController, "Moderator", "agents.ModeratorAgent", argsAgent);

    for(int i = 0; i < NUMAGENTS; i++) {
      createNewAgent(contrainerController, "Agent" + i, "agents.GeneralAgent", null);
    }
  }
  
  private static void createNewAgent(ContainerController contrainerController, String name, String packageAgent, Object[] numAgents) {
    AgentController agentController;
    
    try{
      agentController = contrainerController.createNewAgent(name, packageAgent, numAgents);
      agentController.start();
    } catch (StaleProxyException e){
      e.printStackTrace();
    }
  }

  private static Profile createProfile() {
    Profile profile = new ProfileImpl();
    profile.setParameter(Profile.MAIN_HOST, "localhost");
    profile.setParameter(Profile.MAIN_PORT, PORT);
    // profile.setParameter(Profile.GUI, "true");
    
    return profile;
  }
}