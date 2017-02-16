import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;

public class Main {
  
  private static final String PORT = "1337";
  private static final int NUMAGENT = 5;
  
	public static void main(String[] args) {
	  Runtime runtime = Runtime.instance();
	  Profile profile = new ProfileImpl();
	  profile.setParameter(Profile.MAIN_HOST, "localhost");
	  profile.setParameter(Profile.MAIN_PORT, PORT);
	  profile.setParameter(Profile.GUI, "true");
	  ContainerController contrainerController = runtime.createMainContainer(profile);
	  
	  for(int i = 0; i < NUMAGENT; i++) {
	    AgentController agentController;

  	  try{
  	    agentController = contrainerController.createNewAgent("Agent" + i, "agents.GeneralAgent", null);
  	    agentController.start();
  	  } catch (StaleProxyException e){
  	    e.printStackTrace();
  	  }
	  }
	}
}