package view;

import DTO.Guess;
import DTO.LetterPosition;
import DTO.StatusReport;
import contr.Controller;

import java.util.Scanner;
import java.util.function.Consumer;

/**
 * The main point of user interaction, a command line view
 */
public class UserInterface implements Runnable {
   private Controller cntr;

   public UserInterface() {
      cntr = new Controller();
   }

   public void start() {
      System.out.println("Welcome to hangman!");
      System.out.println("You will be given a random word to guess. If you write one letter, you guess at a letter and will see whether it exists in the word or not and at what location.\n If you write more than one letter, you are guessing the whole word.\nIf you win or lose a round, your score is adjusted and the next round starts immediately.\nGood luck!");
      new Thread(this).start();
   }

   public void run() {
      printGameStatus(cntr.startGame());
      printPrompt();

      while (true) {
         Scanner in = new Scanner(System.in);
         String input = in.next();
         if (input.trim().length() == 0) {
            continue;
         } else if (input.trim().length() == 1) {
            Guess g = new Guess(input.charAt(0));
            cntr.makeGuess(g, new Printer()); //printer will print to output when thread done with getting the result of the guess
         } else {
            Guess g = new Guess(input.trim());
            cntr.makeGuess(g, new Printer());
         }
      }
   }

   private synchronized void printPrompt() {
      System.out.print(">: ");
   }

   //as far as I understand, synchronized means "one thread per instance of the class" can operate on it,
   //so since theres only one thread for this userinterface class running then only one "guess" thread (Printer class) can
   //write at a time
   private synchronized void printGameStatus(StatusReport status) {
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
   private class Printer implements Consumer {
      public void accept(Object toPrint) { //when thread completes (in controller), accept is called with return value
            printGameStatus((StatusReport) toPrint);
            printPrompt();
      }
   }
}
