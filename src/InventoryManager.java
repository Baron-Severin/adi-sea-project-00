import java.io.Serializable;
import java.util.HashMap;


public class InventoryManager implements Serializable {

    HashMap<String, Integer> inventory;

  public InventoryManager() {
    inventory = new HashMap<String, Integer>();
  }

}
