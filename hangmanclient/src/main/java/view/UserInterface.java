package view;

import contr.Controller;

import java.util.Scanner;

/**
 * The main point of user interaction, a command line view
 */
public class UserInterface {
   private Controller cntr;

   public UserInterface() {
      cntr = new Controller();
   }

   public void start() {
      System.out.println("Welcome to hangman!");
      System.out.println("You will be given a random word to guess. If you write one letter, you guess at a letter and will see whether it exists in the word or not and at what location. If you write more than one letter, you are guessing the whole word.");

      cntr.startGame();

      //Scanner input = new Scanner(System.in);
   }

}
