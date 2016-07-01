import java.io.*;
import java.util.HashMap;


public class InventoryManager implements Serializable {

    HashMap<String, Integer> inventory;
  File theDir;

  public InventoryManager() {
    inventory = new HashMap<String, Integer>();
    theDir = new File("/tmp/inventory.ser");
  }

  public void saveInventory() {

    if (!theDir.exists()) {
      boolean result = false;

      try{
        theDir.mkdir();
        result = true;
      }
      catch(SecurityException se){
      }
      if(result) {
        System.out.println("DIR created");
      }
    }

    try
    {
      FileOutputStream fileOut =
        new FileOutputStream(theDir);
      ObjectOutputStream out = new ObjectOutputStream(fileOut);
      out.writeObject(inventory);
      out.close();
      fileOut.close();
      System.out.println("Inventory successfully saved.");
    }catch(IOException i)
    {
      i.printStackTrace();
    }
  }

  public void loadInventory() {
    try
    {
      FileInputStream fileIn = new FileInputStream(theDir);
      ObjectInputStream in = new ObjectInputStream(fileIn);
      inventory = (HashMap<String, Integer>) in.readObject();
      in.close();
      fileIn.close();
      System.out.println("Inventory successfully loaded.");
    }catch(IOException i)
    {
      System.out.println("Saved inventory not found");
      return;
    }catch(ClassNotFoundException c)
    {
      System.out.println("Saved inventory not found");
      return;
    }
  }

}
