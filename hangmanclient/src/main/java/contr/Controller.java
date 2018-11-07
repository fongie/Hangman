package contr;

import net.Connection;

public class Controller {
   private Connection conn;
   public Controller() {
   }

   public void startGame() {
      conn = new Connection();
   }
}
