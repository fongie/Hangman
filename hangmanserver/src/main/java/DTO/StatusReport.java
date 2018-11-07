package DTO;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Represents a Status Report sent from the server, with info on the state of the game.
 */
public class StatusReport implements Serializable {
   private final int wordLength;
   private final int remainingAttempts;
   private final int score;
   private final ArrayList<LetterPosition> correctLetters;

   public StatusReport(int wordLength, int remainingAttempts, int score, ArrayList<LetterPosition> correctLetters) {
      this.wordLength = wordLength;
      this.remainingAttempts = remainingAttempts;
      this.score = score;
      this.correctLetters = correctLetters;
   }

   @Override
   public String toString() {
      StringBuilder sb = new StringBuilder();
      sb.append("wc: ");
      sb.append(wordLength);
      sb.append(" remaining: ");
      sb.append(remainingAttempts);
      sb.append(" score: ");
      sb.append(score);
      sb.append(" correctLetters: ");
      for (LetterPosition lp : correctLetters) {
         sb.append(lp.toString());
         sb.append(" ");
      }
      return sb.toString();
   }

   public int getWordLength() {
      return wordLength;
   }

   public int getRemainingAttempts() {
      return remainingAttempts;
   }

   public int getScore() {
      return score;
   }

   public ArrayList<LetterPosition> getCorrectLetters() {
      return correctLetters;
   }
}
