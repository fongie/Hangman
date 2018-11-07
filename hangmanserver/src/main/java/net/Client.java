package net;

import DTO.Guess;
import DTO.StatusReport;
import model.GameInstance;

import java.io.*;
import java.net.Socket;

public class Client implements Runnable {

   private Socket socket;
   private GameInstance gameInstance;
   private ObjectInputStream from;
   private ObjectOutputStream to;

   Client(Socket socket) {
      this.socket = socket;
      gameInstance = new GameInstance();
   }

   @Override
   public void run() {
      System.out.println("Starting new client thread!");

      try {
         from = new ObjectInputStream(socket.getInputStream());
         to = new ObjectOutputStream(socket.getOutputStream());

         to.writeObject(gameInstance.makeReport()); //at start give number of letters etc to client
      } catch (IOException e) {
         e.printStackTrace();
         System.err.println("Server failed while creating a listening socket.");
      }

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
            e.printStackTrace();
            System.err.println("Server failed while receiving a guess from client");
         } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.err.println("Server failed due to Guess class not being found");
         }
      }
}
