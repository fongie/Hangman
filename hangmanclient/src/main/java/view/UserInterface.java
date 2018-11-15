package view;

import DTO.Guess;
import DTO.LetterPosition;
import DTO.StatusReport;
import contr.Controller;
import net.Observer;

import java.util.Scanner;
import java.util.function.Consumer;

/**
 * The main point of user interaction, a command line view
 */
public class UserInterface implements Runnable {
   private Controller cntr;

   /**
    * Constructor for User Interface
    */
   public UserInterface() {
      cntr = new Controller();
   }

   /**
    * Start the user interface
    */
   public void start() {
      System.out.println("Welcome to hangman!");
      System.out.println("You will be given a random word to guess. If you write one letter, you guess at a letter and will see whether it exists in the word or not and at what location.\n If you write more than one letter, you are guessing the whole word.\nIf you win or lose a round, your score is adjusted and the next round starts immediately.\nGood luck!");
      new Thread(this).start();
   }

   /**
    * Loop to insert commands while playing the game
    */
   public void run() {
      cntr.startGame(new Printer());

      while (true) {
         Scanner in = new Scanner(System.in);
         String input = in.next();
         if (input.trim().length() == 0) {
            continue;
         } else if (input.trim().length() == 1) {
            Guess g = new Guess(input.charAt(0));
            cntr.makeGuess(g);
         } else {
            Guess g = new Guess(input.trim());
            cntr.makeGuess(g);
         }
      }
   }


   private class Printer implements Observer {
      public void print(Object o) {
         printGameStatus((StatusReport) o);
         printPrompt();
      }
      private void printGameStatus(StatusReport status) {
         synchronized (UserInterface.this){
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
      private void printPrompt() {
         synchronized (UserInterface.this) {
            System.out.print(">: ");
         }
      }
   }
}
