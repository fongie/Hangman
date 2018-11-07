package DTO;

import java.io.Serializable;

public class Guess implements Serializable {
   private String word;
   private char letter;
   private boolean guessedFullWord;

   public Guess(char letter) {
      this.letter = letter;
      this.guessedFullWord = false;
   }
   public Guess(String word) {
      this.word = word;
      this.guessedFullWord = true;
   }
   public String getWord() {
      return word;
   }

   public char getLetter() {
      return letter;
   }

   public boolean isGuessedFullWord() {
      return guessedFullWord;
   }
   @Override
   public String toString() {
      StringBuilder sb = new StringBuilder();
      sb.append("Guessing ");
      if (guessedFullWord) {
         sb.append(word);
      } else {
         sb.append(letter);
      }
      return sb.toString();
   }
}
