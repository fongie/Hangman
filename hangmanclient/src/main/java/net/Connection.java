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
   }

   public StatusReport start() {
      return waitForReport();
   }

   public StatusReport makeGuess(Guess guess) {
      /* test threading
      try {
         Thread.sleep(1000);
      } catch (InterruptedException e) {
         e.printStackTrace();
      }
       */
      try {
         send.writeObject(guess);
         send.flush();
         send.reset();
      } catch (IOException e) {
         e.printStackTrace();
         System.err.println("Could not send guess to server");
      }

      return waitForReport();
   }

   private StatusReport waitForReport() {
      StatusReport report = null;
      try {
         report = (StatusReport) receive.readObject();
      } catch (IOException e) {
         e.printStackTrace();
      } catch (ClassNotFoundException e) {
         e.printStackTrace();
      }
      return report;
   }

}
