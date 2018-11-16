package model;

import DTO.Guess;
import DTO.LetterPosition;
import DTO.StatusReport;

import java.io.*;
import java.util.ArrayList;
import java.util.Random;

/**
 * Handles all game logic for Hangman
 */
public class Game {

   private String word;
   private int wordLength;
   private int remainingAttempts;
   private int score;
   private ArrayList<LetterPosition> correctLetters;

   /**
    * Constructor. Starts a new game.
    */
   public Game() {
      score = 0;
      startNewGame();
   }

   /**
    * Create a report on the state of the game (not including "cheats" such as the actual word)
    * @return
    */
   public StatusReport makeReport() {
      return new StatusReport(wordLength,remainingAttempts,score,correctLetters);
   }

   /**
    * Make a guess on a letter or the full word
    * @param guess
    * @return A StatusReport
    */
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
      startNewGame();
   }

   private void lose() {
      score--;
      startNewGame();
   }

   private void startNewGame() {
      word = getRandomWord();
      wordLength = word.length();
      remainingAttempts = word.length();
      correctLetters = new ArrayList<LetterPosition>();

      System.out.println("Starting new game with word: " + word);
   }

   private String getRandomWord() {
      Random rand = new Random();
      int randomWord = rand.nextInt(51525) + 1; //words list has 51525 words, I could count words programatically but I dont intend to change wordlist and it takes time so I hardcode it
      BufferedReader reader = new BufferedReader(new InputStreamReader(Game.class.getResourceAsStream("/words.txt")));

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
