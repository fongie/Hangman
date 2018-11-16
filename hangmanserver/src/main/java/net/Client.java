package net;

import DTO.Guess;
import DTO.StatusReport;
import model.Game;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.concurrent.*;

/**
 * Handles a unique client that connected to the server and server-client communication concerning their game
 */
public class Client {

   private Server server;
   private SocketChannel channel;
   private Game game;
   private ByteBuffer buffer;

   /**
    * Constructor
    * @param channel
    * @param server
    */
   Client(SocketChannel channel, Server server) {
      this.server = server;
      this.channel = channel;
      try {
         start();
      } catch (IOException e) {
         System.err.println("Could not start client handler");
      }
   }

   /**
    * Get the buffer containing an object to be sent from the client
    * @return The buffer
    */
   ByteBuffer getBuffer() {
      return buffer;
   }

   /**
    * Read from channel a received object from client, that is a Guess. Make the guess and prepare to sent a StatusReport back to the client.
    * @throws IOException
    */
   void receiveGuess() throws IOException {
      ByteBuffer buffer = ByteBuffer.allocate(256);
      channel.read(buffer);
      ByteArrayInputStream byteIn = new ByteArrayInputStream(buffer.array());
      ObjectInputStream oIn = new ObjectInputStream(byteIn);

      Guess guess = null;
      try {
         guess = (Guess) oIn.readObject();
      } catch (ClassNotFoundException e) {
         e.printStackTrace();
      }

      prepareWrite(game.makeGuess(guess));
   }
   private void start() throws IOException {
      System.out.println("Starting new client!");
      ExecutorService executor = Executors.newSingleThreadExecutor();
      Future<Game> future = executor.submit(() -> new Game()); //Game constructor does IO with file handling
      try {
         game = future.get();
      } catch (InterruptedException e) {
         System.err.println("Could not start a new game");
         e.printStackTrace();
      } catch (ExecutionException e) {
         System.err.println("Could not start a new game");
         e.printStackTrace();
      }
      prepareWrite(game.makeReport());

   }
   private void prepareWrite(StatusReport obj) throws IOException {
      //prepare byte buffer
      ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
      ObjectOutputStream oOut = new ObjectOutputStream(byteOut);

      System.out.println(obj.toString());

      //put statusreport in buffer
      oOut.writeObject(obj);
      oOut.flush();
      oOut.close();

      buffer = ByteBuffer.wrap(byteOut.toByteArray());
   }

   /**
    * Disconnect the client
    */
   void disconnect() {
      try {
         channel.close();
      } catch (IOException e) {
         e.printStackTrace();
      }
   }


}
