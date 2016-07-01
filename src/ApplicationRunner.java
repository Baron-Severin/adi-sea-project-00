import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequencer;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Scanner;

public class ApplicationRunner {

  static HashMap<String, Integer> inventory = new HashMap<String, Integer>();
  static boolean playIsSet = false;

  // Play variables
  static int higherHp = 0;
  static int attackValue = 0;

  static Pokemon allyPokemon;
  static Pokemon enemyPokemon;

  static Sequencer battleSequencer;

  public static void main(String[] args) {

    try {
      battleSequencer = MidiSystem.getSequencer();
    } catch (Exception ex) {

    }
    System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n");

    System.out.println("\nWelcome to Dianna's Dinosaur & Donut Emporium's inventory system! \n\n");
    System.out.println("Available commands are 'Add', 'List', 'Delete', 'Help', and 'Quit'\n\n\n");

    try {
      Thread.sleep(500);                 //1000 milliseconds is one second.
    } catch(InterruptedException ex) {
      Thread.currentThread().interrupt();
    }



    while (true) {
      collectInput();
    }

  }


  private static void collectInput () {
    if (!playIsSet) {
      System.out.println("Please input your command");
    } else {
      System.out.println("Commands are 'Attack' and 'Run'");
    }

    Scanner input = new Scanner(System.in);
    String userInput = input.nextLine().toLowerCase();

    userInput = userInput.split(" ")[0];

    if (userInput.length() >= 3 && userInput.substring(0, 3).equals("add")) {
      callAdd();
    }

    else if (userInput.length() >= 4 && (userInput.substring(0, 4).equals("list"))) {
      callList();
    }

    else if (userInput.equals("delete")) {
      callDelete();
    }

    else if (userInput.length() >= 4 && (userInput.substring(0, 4).equals("help"))){
      callHelp();
    }

    else if (userInput.equals("quit") || userInput.equals("exit")){
      System.exit(0);
    }

    else if (userInput.equals("play") && inventory.size() >= 2) {
      callPlay();
    }

    else if (userInput.equals("play") && !(inventory.size() >= 2)) {
      System.out.println("Please add at least two items to call Play");
    }

    else if (userInput.equals("attack") && playIsSet == true) {
      callAttack(allyPokemon, enemyPokemon);

      try {
        Thread.sleep(2000);
      } catch (InterruptedException ex) {
      }

      callAttack(enemyPokemon, allyPokemon);

    }

    else if (userInput.equals("run") && playIsSet == true) {
      callRun();
    }

    else {
      System.out.println("I didn't understand that, could you try again?");
      collectInput();
    }






  }

  private static void callAdd() {
    System.out.println("What product would you like to add?");

    Scanner input = new Scanner(System.in);
    String newProduct = input.nextLine().toLowerCase();
    newProduct = capitalize(newProduct);

    System.out.println("How many would you like to add?");

    int productInt = addHowMany();

    addProduct(newProduct, productInt);
  }

  private static int addHowMany () {

    Scanner input = new Scanner(System.in);
    String productQuantity = input.nextLine().toLowerCase();

    int productInt = validateInt(productQuantity);

    if (productInt <= 0){
      System.out.println("I'm sorry, that doesn't appear to be a valid number.  Can you try again?");
      productInt = addHowMany();
    }

    return productInt;

  }

  private static int validateInt(String number) {

    int productInt;

    try {
      productInt = Integer.parseInt(number);
    } catch (NumberFormatException exception) {
      System.out.println("I'm sorry, I didn't understand that");
      System.out.println("Could you please try again with Arabic numerals?");

      Scanner input = new Scanner(System.in);
      String userInput = input.nextLine().toLowerCase();

      productInt = validateInt(userInput);
      return productInt;
    }

    return(productInt);
  }

  private static void callList() {

//        listInventory();

    Object[] keys = inventory.keySet().toArray();

    if (inventory.size() == 0) {
      System.out.println("No items found");
    }

    for (int i = 0; i < inventory.size(); i++) {

      int j = i + 1;

      System.out.println(j + ". " + keys[i] + " --- " + inventory.get(keys[i]));

    }
  }

  private static void callDelete() {

    if (!isDeleteValid(1)) {
      System.out.println("There isn't anything here to delete yet.");

    } else {

      callList();

      System.out.println("Return: cancel deletion and return to menu.");
      System.out.println("Which item would you like to delete (please enter a number)?");

      Scanner input = new Scanner(System.in);
      String deleteIndex = input.nextLine().toLowerCase();
      deleteIndex = capitalize(deleteIndex);

      if (!deleteIndex.equals("Return")) {

        int productInt = validateInt(deleteIndex);

        if (isDeleteValid(productInt)) {

          deleteAtIndex(productInt);

        } else {

          System.out.println("That item number does not appear to be in the list.  Could you please try again?");
          try {
            Thread.sleep(500);
          } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
          }

          callDelete();

        }

      }
    }
  }

  private static void callHelp() {

    System.out.println("Available commands are as follows:");
    System.out.println("Add  : adds a product to inventory.  Will request product name and quantity.");
    System.out.println("List  : will list current inventory");
    System.out.println("Delete  : will print current inventory then request the index of the object to be deleted");

  }


  private static void addProduct(String object, int quantity) {

    int currentStock = quantity;

    if (inventory.containsKey(object)) {
      currentStock += inventory.get(object);
    }

    object = capitalize(object);

    inventory.put(object, currentStock);

    System.out.println("Product added to stock: " + object + ". Quantity added: " + quantity + ".  Current total: " + currentStock);
  }

  private static boolean isDeleteValid (int index) {
    if (index < 1 || index > inventory.size()) {
      return false;
    } else {
      return true;
    }
  }

  private static void deleteAtIndex (int index) {

    Object[] keys = inventory.keySet().toArray();

    inventory.remove(keys[index-1]);

    System.out.println(keys[index-1] + " removed from inventory");

  }

  private static String capitalize(final String line) {
    return Character.toUpperCase(line.charAt(0)) + line.substring(1);
  }


  private static void callPlay() {

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

    allyPokemon = new Pokemon(1);
    enemyPokemon = new Pokemon(0);

    printPokemon();

    higherHp = Math.max(allyPokemon.hp, enemyPokemon.hp);
    double attackDouble = ((Math.random() * (higherHp * .33) + (higherHp * .33)));
    attackValue = (int) attackDouble;
    if (attackValue < 1) { attackValue = 1; }

  }

  private static void printPokemon() {

    System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n");
    System.out.println(enemyPokemon.displayName + " Hit Points: " + enemyPokemon.hp + "\n");
    System.out.println("                         " + enemyPokemon.displayName);
    System.out.println("\n\n\n\n");
    System.out.println(allyPokemon.displayName + "\n");
    System.out.println(allyPokemon.displayName + " Hit Points: " + allyPokemon.hp + "\n");

  }

  private static void callAttack(Pokemon attacker, Pokemon defender) {

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
    } else {
      System.out.println(attacker.displayName + " missed!");
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

  private static void playBattleEndSound() {

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

  private static void checkForFaints() {
    if (enemyPokemon.hp < 1) {

      playBattleEndSound();
      System.out.println("\n\n\n\n\n\n\n\n\n" + enemyPokemon.displayName + " fainted!  You won!\n\n\n\n\n");

      try {
        Thread.sleep(300000);
      } catch (InterruptedException ex) {
      }

    } else if (allyPokemon.hp < 1) {

      playBattleEndSound();
      System.out.println("\n\n\n\n\n\n\n\n\n" + allyPokemon.displayName + " fainted!  You've lost!\n\n\n\n\n");

      try {
        Thread.sleep(300000);
      } catch (InterruptedException ex) {
      }

    }

  }

  private static void callRun() {

    if (Math.random() > .5) {

      playBattleEndSound();
      System.out.println("\n\n\n\n\n\n\n\n\n" + "You got away!\n\n\n\n\n");

      try {
        Thread.sleep(300000);
      } catch (InterruptedException ex) {
      }

    } else {
      System.out.println("\n\n\n\n\n\n\n\n\n" + "You didn't manage to get away!\n" + enemyPokemon.displayName + " is attacking!");

      try {
        Thread.sleep(1250);
      } catch (InterruptedException ex) {
      }

      callAttack(enemyPokemon, allyPokemon);

    }

  }


}


