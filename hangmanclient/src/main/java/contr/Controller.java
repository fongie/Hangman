package contr;

import net.Connection;
import DTO.Guess;

public class Controller {
   private Connection conn;
   public Controller() {
   }

   public void startGame() {
      conn = new Connection();
   }

   public void makeGuess(Guess guess) {
      conn.makeGuess(guess);
   }
}
