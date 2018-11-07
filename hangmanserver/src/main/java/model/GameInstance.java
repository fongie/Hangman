package model;

import DTO.Guess;
import DTO.LetterPosition;
import DTO.StatusReport;

import java.io.*;
import java.util.ArrayList;
import java.util.Random;

/**
 * Starts a new game instance when a client connects
 */
public class GameInstance {

   private String word;
   private int wordLength;
   private int remainingAttempts;
   private int score;
   private ArrayList<LetterPosition> correctLetters;

   public GameInstance() {
      score = 0;
      startNewGame();
   }

   public StatusReport makeReport() {
      /*
      JsonArrayBuilder jsonLetters = Json.createArrayBuilder();
      for (LetterPosition lp : correctLetters) {
         jsonLetters.add(Json.createObjectBuilder().add("char", lp.getLetter()).add("pos", lp.getPosition()));
      }
      JsonObject json = Json.createObjectBuilder()
            .add("score", score)
            .add("wordLength", wordLength)
            .add("remainingAttempts", remainingAttempts)
            .add("correctLetters", jsonLetters)
            .build();

      System.out.println(json.toString());
      */
      //System.out.println(report);
      return new StatusReport(wordLength,remainingAttempts,score,correctLetters);
   }

   public StatusReport makeGuess(Guess guess) {
      if (guess.isGuessedFullWord()) {
         guessWord(guess.getWord());
      } else if (!guess.isGuessedFullWord()) {
         guessLetter(guess.getLetter());
      }
      return makeReport();
   }
   private void guessWord(String guessedWord) {
      if (guessedWord.equals(word)) {
         win();
      } else {
         remainingAttempts--;
         checkRemainingAttempts();
      }
   }
   private void guessLetter(char letter) {
      remainingAttempts--;
      for (LetterPosition lp : correctLetters) { //do nothing if letter was already guessed
         if (lp.getLetter() == letter) {
            return;
         }
      }

      for (int i = 0; i < wordLength; i++) {
         char c = word.charAt(i);
         if (letter == c) {
            LetterPosition lp = new LetterPosition(c, i);
            correctLetters.add(lp);
         }
      }

      checkRemainingAttempts();
   }

   private void checkRemainingAttempts() {
      if (wordLength == correctLetters.size()) {
         win();
      }
      if (remainingAttempts < 1) {
         lose();
      }
   }
   private void win() {
      score++;
      System.out.println("You win");
   }

   private void lose() {
      score--;
      System.out.println("You lost");
   }

   private void startNewGame() {
      word = getRandomWord();
      wordLength = word.length();
      remainingAttempts = word.length();
      correctLetters = new ArrayList<LetterPosition>();

      /*
      guessLetter('o');
      guessWord("hello");
      */
   }

   private String getRandomWord() {
      Random rand = new Random();
      int randomWord = rand.nextInt(51525) + 1; //words list has 51525 words, I could count words programatically but I dont intend to change wordlist and it takes time so I hardcode it
      BufferedReader reader = new BufferedReader(new InputStreamReader(GameInstance.class.getResourceAsStream("/words.txt")));

      try {
         int i = 0;
         while (i != randomWord) {
            reader.readLine();
            i++;
         }
         String word = reader.readLine().toLowerCase();
         reader.close();
         return word;
      } catch (IOException e) {
         e.printStackTrace();
      }
      return "hangman";
   }
}
