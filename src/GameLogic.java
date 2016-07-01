import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequencer;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;


public class GameLogic {

  Pokemon allyPokemon;
  Pokemon enemyPokemon;

  Sequencer battleSequencer;
  boolean playIsSet;
  String history = "";

  InventoryManager inventoryManager;

  int higherHp;
  int attackValue;

  public GameLogic() {

    higherHp = 0;
    attackValue = 0;

    try {
      battleSequencer = MidiSystem.getSequencer();
    } catch (Exception ex) {

    }
    System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n");

  }

public boolean isPlaySet() {
  if (playIsSet) {
    return true;
  } else {
    return false;
  }
}


  public void callPlay() {

    playIsSet = true;

    try {

      battleSequencer.open();

      FileInputStream fs = new FileInputStream("115-battle-vs-trainer-.mp3.mid");
      InputStream is = new BufferedInputStream(fs);

      battleSequencer.setSequence(is);

      battleSequencer.start();

    } catch (Exception ex) {
      ex.printStackTrace();
    }

    allyPokemon = new Pokemon(1, inventoryManager);
    enemyPokemon = new Pokemon(0, inventoryManager);

    addToHistory("You were attacked by a wild " + enemyPokemon.displayName + "\nYou chose " + allyPokemon.displayName + ".");

    printPokemon();

    higherHp = Math.max(allyPokemon.hp, enemyPokemon.hp);
    double attackDouble = ((Math.random() * (higherHp * .33) + (higherHp * .33)));
    attackValue = (int) attackDouble;
    if (attackValue < 1) { attackValue = 1; }

  }

  public void printPokemon() {

    System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n");
    System.out.println(enemyPokemon.displayName + " Hit Points: " + enemyPokemon.hp + "\n");
    System.out.println("                         " + enemyPokemon.displayName);
    System.out.println("\n\n\n\n");
    System.out.println(allyPokemon.displayName + "\n");
    System.out.println(allyPokemon.displayName + " Hit Points: " + allyPokemon.hp + "\n");

  }

  public void callAttack(Pokemon attacker, Pokemon defender) {

    printPokemon();
    System.out.println(attacker.displayName +" is attacking!");

    try {
      Thread.sleep(1500);
    } catch (InterruptedException ex) {
    }

    if (Math.random() > .5 ) {

      defender.hurtPokemon(attackValue);
      printPokemon();
      System.out.println(defender.displayName + " was hit!");
      addToHistory(attacker.displayName + " attacked " + defender.displayName + " and hit.");
    } else {
      System.out.println(attacker.displayName + " missed!");
      addToHistory(attacker.displayName + " attacked " + defender.displayName + " and missed.");
    }

    try {
      Thread.sleep(1250);
    } catch (InterruptedException ex) {
    }

    checkForFaints();

    if ((allyPokemon.hp >= 1) && (enemyPokemon.hp >= 1)) {
      printPokemon();
    }

  }

  public void playBattleEndSound() {

    try {

      battleSequencer.stop();
      battleSequencer.open();

      FileInputStream fs = new FileInputStream("116-victory-vs-trainer-.mp3.mid");
      InputStream is = new BufferedInputStream(fs);

      battleSequencer.setSequence(is);

      battleSequencer.start();

    } catch (Exception ex) {
      ex.printStackTrace();
    }

  }

  public void checkForFaints() {
    if (enemyPokemon.hp < 1) {

      addToHistory(enemyPokemon.displayName + " fainted.");
      playBattleEndSound();
      System.out.println("\n\n\n\n\n\n\n\n\n" + enemyPokemon.displayName + " fainted!  You won!\n\n\n\n\n");

      try {
        Thread.sleep(300000);
      } catch (InterruptedException ex) {
      }

    } else if (allyPokemon.hp < 1) {

      addToHistory(allyPokemon.displayName + " fainted.");
      playBattleEndSound();
      System.out.println("\n\n\n\n\n\n\n\n\n" + allyPokemon.displayName + " fainted!  You've lost!\n\n\n\n\n");

      try {
        Thread.sleep(300000);
      } catch (InterruptedException ex) {
      }

    }

  }

  public void callRun() {

    if (Math.random() > .5) {

      addToHistory(allyPokemon.displayName + " ran away.");
      playBattleEndSound();
      System.out.println("\n\n\n\n\n\n\n\n\n" + "You got away!\n\n\n\n\n");

      try {
        Thread.sleep(300000);
      } catch (InterruptedException ex) {
      }

    } else {
      addToHistory(allyPokemon.displayName + " tried to run away, but failed.");
      System.out.println("\n\n\n\n\n\n\n\n\n" + "You didn't manage to get away!\n");

      try {
        Thread.sleep(1000);
      } catch (InterruptedException ex) {
      }

      System.out.println(enemyPokemon.displayName + " is attacking!");

      try {
        Thread.sleep(1250);
      } catch (InterruptedException ex) {
      }

      callAttack(enemyPokemon, allyPokemon);

    }

  }

  public void addToHistory(String addThis) {

    history += "\n";
    history += addThis;

  }

  public void printHistory() {
    System.out.println(history);
  }

}
