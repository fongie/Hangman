package view;

import DTO.Guess;
import DTO.LetterPosition;
import DTO.StatusReport;
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

      printGameStatus(cntr.startGame());

      while (true) {
         System.out.print(">: ");
         Scanner input = new Scanner(System.in);
         String c = input.next();
         Guess g = new Guess(c.charAt(0));
         StatusReport reply = cntr.makeGuess(g);
         printGameStatus(reply);
      }
   }

   private void printGameStatus(StatusReport status) {
      StringBuilder all = new StringBuilder();
      all.append("Your current score is ");
      all.append(status.getScore());
      all.append(".\n");
      all.append("You have ");
      all.append(status.getRemainingAttempts());
      all.append(" attempts remaining.");
      all.append("\n");

      StringBuilder word = new StringBuilder();
      for (int i = 0; i < status.getWordLength(); i++) {
         word.append("_");
      }
      for (LetterPosition lp : status.getCorrectLetters()) {
         word.setCharAt(lp.getPosition(), lp.getLetter());
      }

      for (int i = 1; i < status.getWordLength() * 2; i += 2) {
         word.insert(i, " ");
      }
      all.append(word);
      System.out.println(all.toString());
   }
}
