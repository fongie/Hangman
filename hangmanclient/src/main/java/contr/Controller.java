package contr;

import DTO.StatusReport;
import net.Connection;
import DTO.Guess;

public class Controller {
   private Connection conn;
   public Controller() {
   }

   public StatusReport startGame() {
      conn = new Connection();
      return conn.start();
   }

   public StatusReport makeGuess(Guess guess) {
      return conn.makeGuess(guess);
   }
}
