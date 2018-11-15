package contr;

import net.Connection;
import DTO.Guess;
import net.Observer;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

/**
 * Controller
 */
public class Controller {
   private Connection conn;
   public Controller() {
   }

   /**
    * Set up connection to server and start the game (get the first info from server)
    * @param print Consumes a StatusReport object to print game status to user interface
    */
   public void startGame(Observer print) {
      conn = new Connection(print);
   }

   /**
    * Sends a guess to the server and receives a status report
    * @param guess
    */
   public void makeGuess(Guess guess) {
      conn.makeGuess(guess);
   }
}
