package messages;

import java.io.Serializable;
import java.util.ArrayList;

public class MessagePack implements Serializable {
  
  private String name;
  private ArrayList<Integer> opinion;
  
  public MessagePack () {}
  
  public MessagePack (String name, ArrayList<Integer> opinion) {
    this.name = name;
    this.opinion = opinion;
  }
  
  public String getName() { return name; }

  public void setName(String name) { this.name = name; }

  public ArrayList<Integer> getOpinion() { return opinion; }

  public void setOpinion(ArrayList<Integer> opinion) { this.opinion = opinion; }
}
