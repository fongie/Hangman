package view;

import DTO.Guess;
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
      cntr.startGame();

      System.out.println("Welcome to hangman!");
      System.out.println("You will be given a random word to guess. If you write one letter, you guess at a letter and will see whether it exists in the word or not and at what location. If you write more than one letter, you are guessing the whole word.");

      Scanner input = new Scanner(System.in);
      String c = input.next();

      Guess g = new Guess(c.charAt(0));

      cntr.makeGuess(g);
   }


}
