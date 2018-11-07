package net;

import DTO.Guess;
import DTO.StatusReport;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

public class Connection {
   private static final String HOST = "192.168.0.2";
   private static final int PORT = 8080;
   private static final int LINGER_TIME = 5000;
   private static final int HALF_HOUR = 1800000;
   private static final int HALF_MINUTE = 30000;

   private Socket socket;
   private ObjectInputStream receive;
   private ObjectOutputStream send;

   public Connection() {

      socket = new Socket();
      InetSocketAddress server = new InetSocketAddress(HOST, PORT);

      try {
         socket.connect(server, HALF_MINUTE);
         socket.setSoTimeout(HALF_HOUR);
         send = new ObjectOutputStream(socket.getOutputStream());
         receive = new ObjectInputStream(socket.getInputStream());
      } catch (IOException e) {
         e.printStackTrace();
         System.err.println("Could not connect to server.");
      }

      try {
         StatusReport report = (StatusReport) receive.readObject();
         System.out.println(report.toString());
      } catch (IOException e) {
         e.printStackTrace();
      } catch (ClassNotFoundException e) {
         e.printStackTrace();
      }
   }

   public void makeGuess(Guess guess) {
      try {
         System.out.println(guess.toString());
         send.writeObject(guess);
      } catch (IOException e) {
         e.printStackTrace();
         System.err.println("Could not send guess to server");
      }

      try {
         StatusReport report = (StatusReport) receive.readObject();
         System.out.println(report.toString());
      } catch (IOException e) {
         e.printStackTrace();
      } catch (ClassNotFoundException e) {
         e.printStackTrace();
      }

   }
}
