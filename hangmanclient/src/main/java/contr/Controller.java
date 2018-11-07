package contr;

import net.Connection;
import DTO.Guess;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

/**
 * Controller responsible for threading on server calls
 */
public class Controller {
   private Connection conn;
   public Controller() {
   }

   /**
    * Set up connection to server and start the game (get the first info from server)
    * @param print Consumes a StatusReport object to print game status to user interface
    */
   public void startGame(Consumer print) {
      CompletableFuture.supplyAsync( //runs in common thread pool
            () -> {
               conn = new Connection();
               return conn.start();
            }
      ).thenAccept(print); //return value is consumed by Consumer print (a Printer) on thread complete
   }

   /**
    * Sends a guess to the server and receives a status report
    * @param guess
    * @param print
    */
   public void makeGuess(Guess guess, Consumer print) {
      CompletableFuture.supplyAsync( //runs in common thread pool
            () -> conn.makeGuess(guess)
            ).thenAccept(print); //return value is consumed by Consumer print (a Printer) on thread complete
   }
}
