package net;

import DTO.Guess;
import DTO.StatusReport;
import model.Game;

import java.io.*;
import java.net.Socket;

/**
 * Handles a unique client that connected to the server and server-client communication concerning their game
 */
public class Client implements Runnable {

   private Server server;
   private Socket socket;
   private Game game;
   private ObjectInputStream from;
   private ObjectOutputStream to;
   private boolean connected;

   /**
    * Constructor
    * @param socket
    * @param server
    */
   Client(Socket socket, Server server) {
      this.server = server;
      this.socket = socket;
      this.connected = true;
   }

   /**
    * Starts main game loop that reads and writes to TCP sockets using game logic in the Game class
    */
   public void run() {
      start();
      while (connected) {
         try {
            Guess newGuess = (Guess) from.readObject();
            StatusReport reply = game.makeGuess(newGuess);
            writeObject(reply);
         } catch (IOException e) {
            disconnect();
            System.err.println("Client disconnected.");
         } catch (ClassNotFoundException e) {
            disconnect();
            e.printStackTrace();
            System.err.println("Server failed due to Guess class not being found");
         }
      }
   }

   private void start() {
      System.out.println("Starting new client thread!");
      game = new Game();

      try {
         from = new ObjectInputStream(socket.getInputStream());
         to = new ObjectOutputStream(socket.getOutputStream());
         writeObject(game.makeReport()); //at game start send client info on word
      } catch (IOException e) {
         e.printStackTrace();
         System.err.println("Server failed while creating a listening socket.");
      }
   }
   private void disconnect() {
      try {
         socket.close();
      } catch (IOException e) {
         e.printStackTrace();
      }
      connected = false;
      server.removeClient(this);
   }

   private void writeObject(StatusReport obj) {
      try {
         to.writeObject(obj);
         to.flush();
         to.reset(); //otherwise old objects are cached and old messages can mess up the communication
      } catch (IOException e) {
         disconnect();
         System.err.println("Guess failed or client disconnected.");
         e.printStackTrace();
      }
   }

}
