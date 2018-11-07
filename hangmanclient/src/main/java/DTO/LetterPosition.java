package DTO;

import java.io.Serializable;

/**
 * Represents a correctly guessed letter and its position in the word
 */
public class LetterPosition implements Serializable {
   private char letter;
   private int position;

   public LetterPosition(char letter, int position) {
      this.letter = letter;
      this.position = position;
   }

   public char getLetter() {
      return letter;
   }

   public int getPosition() {
      return position;
   }

   @Override
   public String toString() {
      return "{" + letter + "," + position + "}";
   }
}
