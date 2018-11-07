package net;

import DTO.Guess;
import DTO.StatusReport;
import model.GameInstance;

import java.io.*;
import java.net.Socket;

public class Client implements Runnable {

   private Server server;
   private Socket socket;
   private GameInstance gameInstance;
   private ObjectInputStream from;
   private ObjectOutputStream to;
   private boolean connected;

   Client(Socket socket, Server server) {
      this.server = server;
      this.socket = socket;
      this.connected = true;
   }

   public void run() {
      System.out.println("Starting new client thread!");
      gameInstance = new GameInstance();

      try {
         from = new ObjectInputStream(socket.getInputStream());
         to = new ObjectOutputStream(socket.getOutputStream());

         to.writeObject(gameInstance.makeReport()); //at start give number of letters etc to client
         to.flush();
         to.reset();
      } catch (IOException e) {
         e.printStackTrace();
         System.err.println("Server failed while creating a listening socket.");
      }

      while (connected) {
         try {
            Guess newGuess = (Guess) from.readObject();

            System.out.println(newGuess.toString());

            StatusReport reply = gameInstance.makeGuess(newGuess);

            System.out.println("Sending StatusReport:");
            System.out.println(reply.toString());

            to.writeObject(reply);
            to.flush();
            to.reset(); //otherwise old objects are cached and old messages can mess up the communication
         } catch (IOException e) {
            disconnect();
            e.printStackTrace();
            System.err.println("Server failed while receiving a guess from client");
         } catch (ClassNotFoundException e) {
            disconnect();
            e.printStackTrace();
            System.err.println("Server failed due to Guess class not being found");
         }
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
}
