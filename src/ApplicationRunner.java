import java.util.HashMap;
import java.util.Scanner;

public class ApplicationRunner {

  boolean playIsSet;
  Pokemon allyPokemon;
  Pokemon enemyPokemon;

  HashMap<String, Integer> inventory;

  public static void main(String[] args) {

    // instantiate objects and set up references
    ApplicationRunner applicationRunner = new ApplicationRunner();
    GameLogic gameLogic = new GameLogic();
    InventoryManager inventoryManager = new InventoryManager();
    applicationRunner.setUpReferences(inventoryManager, gameLogic);

    ConsoleLogger tempLogger = new ConsoleLogger();
    tempLogger.centerText("\nWelcome to Dianna's Dinosaur & Donut Emporium's inventory system! \n\n");
    tempLogger.centerText("Available commands are 'Add', 'List', 'Delete', 'Help', 'Save', 'Load', and 'Quit'");
    System.out.println("\n\n\n");
    tempLogger = null;

    try {
      Thread.sleep(500);
    } catch(InterruptedException ex) {
      Thread.currentThread().interrupt();
    }

    // update references then check input against menu options
    while (true) {
      applicationRunner.setUpReferences(inventoryManager, gameLogic);
      applicationRunner.collectInput(gameLogic, inventoryManager);
    }

  }
  
  private void printThis(String text) {
    ConsoleLogger logger = new ConsoleLogger();
    logger.centerText(text);
  }

  private void setUpReferences(InventoryManager inventoryManager, GameLogic gameLogic) {
    inventory = inventoryManager.inventory;
    gameLogic.inventoryManager = inventoryManager;
  }

  private void collectInput (GameLogic gameLogic, InventoryManager inventoryManager) {

    if (!(gameLogic.isPlaySet())) {
      printThis("Please input your command");
      System.out.println("\n\n");
    } else {
      printThis("Commands are 'Attack', 'Run', and 'History'");
      System.out.println("\n\n");
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
      callDelete(inventoryManager);
    }

    else if (userInput.equals("save")) {
      inventoryManager.saveInventory();
    }

    else if (userInput.equals("load")) {
      inventoryManager.loadInventory();
    }

    else if (userInput.length() >= 4 && (userInput.substring(0, 4).equals("help"))){
      callHelp();
    }

    else if (userInput.equals("quit") || userInput.equals("exit")){
      System.exit(0);
    }

    else if (userInput.equals("play") && inventory.size() >= 2) {
      gameLogic.callPlay();
      enemyPokemon = gameLogic.enemyPokemon;
      allyPokemon = gameLogic.allyPokemon;
      playIsSet = gameLogic.playIsSet;
    }

    else if (userInput.equals("play") && !(inventory.size() >= 2)) {
      printThis("Please add at least two items to call Play");
    }

    // attack enemy, pause, enemy attacks you
    else if (userInput.equals("attack") && playIsSet) {
      gameLogic.callAttack(allyPokemon, enemyPokemon);

      try {
        Thread.sleep(2000);
      } catch (InterruptedException ex) {
      }

      gameLogic.callAttack(enemyPokemon, allyPokemon);

    }

    else if (userInput.equals("run") && playIsSet) {
      gameLogic.callRun();
    }

    else if (userInput.equals("history") && playIsSet) {
      gameLogic.printHistory();
    }

    else {
      printThis("I didn't understand that, could you try again?");
      collectInput(gameLogic, inventoryManager);
    }

  }

  private void callAdd() {

    printThis("What product would you like to add?");

    Scanner input = new Scanner(System.in);
    String newProduct = input.nextLine().toLowerCase();
    newProduct = capitalize(newProduct);

    printThis("How many would you like to add?");

    int productInt = addHowMany();

    addProduct(newProduct, productInt);
  }

  private int addHowMany () {

    Scanner input = new Scanner(System.in);
    String productQuantity = input.nextLine().toLowerCase();

    int productInt = validateInt(productQuantity);

    if (productInt <= 0){
      printThis("I'm sorry, that doesn't appear to be a valid number.  Can you try again?");
      productInt = addHowMany();
    }

    return productInt;

  }

  private int validateInt(String number) {

    int productInt;

    try {
      productInt = Integer.parseInt(number);
    } catch (NumberFormatException exception) {
      printThis("I'm sorry, I didn't understand that");
      printThis("Could you please try again with Arabic numerals?");

      Scanner input = new Scanner(System.in);
      String userInput = input.nextLine().toLowerCase();

      productInt = validateInt(userInput);
    }
    return productInt;
  }

  private void callList() {

    Object[] keys = inventory.keySet().toArray();

    if (inventory.size() == 0) {
      printThis("No items found");
    }

    for (int i = 0; i < inventory.size(); i++) {

      int j = i + 1;

      printThis(j + ". " + keys[i] + " --- " + inventory.get(keys[i]));

    }
  }

  private void callDelete(InventoryManager inventoryManager) {

    if (!isDeleteValid(1)) {
      printThis("There isn't anything here to delete yet.");

    } else {

      callList();

      printThis("Return: cancel deletion and return to menu.");
      printThis("Which item would you like to delete (please enter a number)?");

      Scanner input = new Scanner(System.in);
      String deleteIndex = input.nextLine().toLowerCase();
      deleteIndex = capitalize(deleteIndex);

      if (!deleteIndex.equals("Return")) {

        int productInt = validateInt(deleteIndex);

        if (isDeleteValid(productInt)) {

          deleteAtIndex(productInt);

        } else {

          printThis("That item number does not appear to be in the list.  Could you please try again?");
          try {
            Thread.sleep(500);
          } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
          }

          callDelete(inventoryManager);

        }
      }
    }
  }

  private void callHelp() {

    printThis("Available commands are as follows:");
    printThis("Add  : adds a product to inventory.  Will request product name and quantity.");
    printThis("List  : will list current inventory");
    printThis("Delete  : will print current inventory then request the index of the object to be deleted");
    printThis("Save  : saves your current list");
    printThis("Load  : loads a saved list");
    printThis("Play  : begins gameplay (requires that at least two inventory items have been added)");
    printThis("Quit  : exits the program");
  }


  private void addProduct(String object, int quantity) {

    int currentStock = quantity;

    if (inventory.containsKey(object)) {
      currentStock += inventory.get(object);
    }

    object = capitalize(object);

    inventory.put(object, currentStock);

    printThis("Product added to stock: " + object + ". Quantity added: " + quantity + ".  Current total: " + currentStock);
  }

  private boolean isDeleteValid (int index) {
    if (index < 1 || index > inventory.size()) {
      return false;
    } else {
      return true;
    }
  }

  private void deleteAtIndex (int index) {

    Object[] keys = inventory.keySet().toArray();

    inventory.remove(keys[index-1]);

    printThis(keys[index-1] + " removed from inventory");

  }

  private static String capitalize(final String line) {
    return Character.toUpperCase(line.charAt(0)) + line.substring(1);
  }

}


