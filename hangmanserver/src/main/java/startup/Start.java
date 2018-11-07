package startup;

import net.Server;

/**
 * Starting point for the Hangman server
 */
public class Start {
   public static void main(String[] args) {
      new Server().start();
   }
}