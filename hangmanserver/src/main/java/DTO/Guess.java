package DTO;

public class Guess {
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
}
