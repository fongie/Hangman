package net;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

/**
 * Entrypoint class to the server, which serves clients that wish to play Hangman
 */
public class Server {

   private static final int PORT = 8080;
   private static final int LINGER_TIME = 5000;
   private static final int HALF_HOUR = 1800000;

   private ArrayList<Client> clients;

   /**
    * Constructor
    */
   public Server() {
      clients = new ArrayList<Client>();
   }

   /**
    * Initialize serversocket and start serving clients forever
    */
   public void start() {
      try {
         ServerSocket serverSocket = new ServerSocket(PORT);
         while (true)
            serve(serverSocket);
      } catch (IOException e) {
         e.printStackTrace();
         System.err.println("Server failed while creating a listening socket.");
      }
   }

   private void serve(ServerSocket serverSocket) {
      try {
         connectNewClient(serverSocket.accept());
      } catch (SocketException e) {
         e.printStackTrace();
         System.err.println("Server failed while accepting a new client connection.");
      } catch (IOException e) {
         e.printStackTrace();
         System.err.println("Server failed while accepting a new client connection.");
      }
   }

   private void connectNewClient(Socket socket) throws SocketException {
      socket.setSoLinger(true,LINGER_TIME);
      socket.setSoTimeout(HALF_HOUR);
      Client client = new Client(socket, this); //create new gameclient
      synchronized (clients) { //to avoid race condition when adding new clients
         clients.add(client);
      }
      Thread thread = new Thread(client);
      thread.setPriority(Thread.MAX_PRIORITY); //to prioritize clients playing over connecting new clients
      thread.start(); //start client in own thread (so many can play at once)
   }

   /**
    * Remove a disconnected client from list of clients
    * @param client
    */
   void removeClient(Client client) {
      synchronized (clients) {
         clients.remove(client);
      }
   }
}
