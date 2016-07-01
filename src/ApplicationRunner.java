import java.util.HashMap;
import java.util.Scanner;

public class ApplicationRunner {

  boolean playIsSet;
  Pokemon allyPokemon;
  Pokemon enemyPokemon;

  HashMap<String, Integer> inventory;

  public static void main(String[] args) {

    ApplicationRunner applicationRunner = new ApplicationRunner();
    GameLogic gameLogic = new GameLogic();
    InventoryManager inventoryManager = new InventoryManager();

    applicationRunner.setUpReferences(inventoryManager, gameLogic);


    System.out.println("\nWelcome to Dianna's Dinosaur & Donut Emporium's inventory system! \n\n");
    System.out.println("Available commands are 'Add', 'List', 'Delete', 'Help', 'Save', 'Load', and 'Quit'\n\n\n");

    try {
      Thread.sleep(500);
    } catch(InterruptedException ex) {
      Thread.currentThread().interrupt();
    }

    while (true) {
      applicationRunner.setUpReferences(inventoryManager, gameLogic);
      applicationRunner.collectInput(gameLogic, inventoryManager);
    }

  }

  private void setUpReferences(InventoryManager inventoryManager, GameLogic gameLogic) {
    inventory = inventoryManager.inventory;
    gameLogic.inventoryManager = inventoryManager;
  }

  private void collectInput (GameLogic gameLogic, InventoryManager inventoryManager) {


    if (!(gameLogic.isPlaySet())) {
      System.out.println("Please input your command");
    } else {
      System.out.println("Commands are 'Attack', 'Run', and 'History'");
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
      System.out.println("Please add at least two items to call Play");
    }

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
      System.out.println("I didn't understand that, could you try again?");
      collectInput(gameLogic, inventoryManager);
    }






  }

  private void callAdd() {
    System.out.println("What product would you like to add?");

    Scanner input = new Scanner(System.in);
    String newProduct = input.nextLine().toLowerCase();
    newProduct = capitalize(newProduct);

    System.out.println("How many would you like to add?");

    int productInt = addHowMany();

    addProduct(newProduct, productInt);
  }

  private int addHowMany () {

    Scanner input = new Scanner(System.in);
    String productQuantity = input.nextLine().toLowerCase();

    int productInt = validateInt(productQuantity);

    if (productInt <= 0){
      System.out.println("I'm sorry, that doesn't appear to be a valid number.  Can you try again?");
      productInt = addHowMany();
    }

    return productInt;

  }

  private int validateInt(String number) {

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

  private void callList() {


    Object[] keys = inventory.keySet().toArray();

    if (inventory.size() == 0) {
      System.out.println("No items found");
    }

    for (int i = 0; i < inventory.size(); i++) {

      int j = i + 1;

      System.out.println(j + ". " + keys[i] + " --- " + inventory.get(keys[i]));

    }
  }

  private void callDelete(InventoryManager inventoryManager) {

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

          callDelete(inventoryManager);

        }

      }
    }
  }

  private void callHelp() {

    System.out.println("Available commands are as follows:");
    System.out.println("Add  : adds a product to inventory.  Will request product name and quantity.");
    System.out.println("List  : will list current inventory");
    System.out.println("Delete  : will print current inventory then request the index of the object to be deleted");
    System.out.println("Save  : saves your current list");
    System.out.println("Load  : loads a saved list");

  }


  private void addProduct(String object, int quantity) {

    int currentStock = quantity;

    if (inventory.containsKey(object)) {
      currentStock += inventory.get(object);
    }

    object = capitalize(object);

    inventory.put(object, currentStock);

    System.out.println("Product added to stock: " + object + ". Quantity added: " + quantity + ".  Current total: " + currentStock);
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

    System.out.println(keys[index-1] + " removed from inventory");

  }

  private static String capitalize(final String line) {
    return Character.toUpperCase(line.charAt(0)) + line.substring(1);
  }





}


