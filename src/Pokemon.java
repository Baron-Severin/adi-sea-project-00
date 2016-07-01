import java.util.HashMap;

/**
 * Created by erikrudie on 6/30/16.
 */
public class Pokemon {

  String team;
  int hp;
  String displayName;

  public Pokemon(int setTeam) {

    if (setTeam == 1) {
      team = "ally";
    } else {
      team = "enemy";
    }

    HashMap<String, Integer> inventory = ApplicationRunner.inventory;

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
