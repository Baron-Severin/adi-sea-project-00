import java.util.HashMap;


public class Pokemon {

  String team;
  int hp;
  String displayName;

  public Pokemon(int setTeam, InventoryManager inventoryManager) {

    if (setTeam == 1) {
      team = "ally";
    } else {
      team = "enemy";
    }

    HashMap<String, Integer> inventory = inventoryManager.inventory;

    Object[] keys = inventory.keySet().toArray();

    int randomIndex = (int) (keys.length * Math.random());

    displayName = (String) keys[randomIndex];

    hp = inventory.get(displayName);

    inventory.remove(displayName);

  }

  public void hurtPokemon(int howMuch) {
    hp -= howMuch;
  }

}
