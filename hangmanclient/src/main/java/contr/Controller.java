package contr;

import DTO.StatusReport;
import net.Connection;
import DTO.Guess;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class Controller {
   private Connection conn;
   public Controller() {
   }

   public StatusReport startGame() {
      conn = new Connection();
      return conn.start();
   }

   public void makeGuess(Guess guess, Consumer print) {
      CompletableFuture.supplyAsync(
            () -> conn.makeGuess(guess)
            ).thenAccept(print);
   }
}
