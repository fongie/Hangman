package net;

import contr.Controller;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

public class Server {

   private static final int PORT = 8080;
   private static final int LINGER_TIME = 5000;
   private static final int HALF_HOUR = 1800000;

   private Controller cntr;
   private ArrayList<Client> clients;

   public Server() {
      cntr = new Controller();
      clients = new ArrayList<Client>();
   }

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
      Client client = new Client(socket); //create new gameclient
      synchronized (clients) { //to avoid race condition when adding new clients
         clients.add(client);
      }
      Thread thread = new Thread(client);
      thread.setPriority(Thread.MAX_PRIORITY);
      thread.start(); //start client in own thread (so many can play at once)
   }
}
