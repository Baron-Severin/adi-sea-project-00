/**
 * Created by erikrudie on 7/1/16.
 */
public class ConsoleLogger {

  int screenWidth = 125;

  public void printStars() {

    String stars = "*";

    for (int i = 0; i < screenWidth; i++) {
      stars += "*";
    }

    System.out.println(stars);

  }

  public void centerText(String text) {

    if (text.contains("\n")) {
      String[] splitText = text.split("\n");

      for (int i = 0; i < splitText.length; i++) {
//        if (splitText[i].equals("\n")) {
//          System.out.println("\n");
//        } else {
        System.out.println("\n");
          centerText(splitText[i]);
//        }
      }

    } else {

      int lengthOfText = text.length();
      int printSpaces = 0;
      String spaces = "";

      if (screenWidth > lengthOfText) {
        printSpaces = (screenWidth - lengthOfText) / 2;
      }

      for (int i = 0; i < printSpaces; i++) {
        spaces += " ";
      }

      System.out.println(spaces + text);

    }
  }

}
