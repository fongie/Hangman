package model;

public class LetterPosition {
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
