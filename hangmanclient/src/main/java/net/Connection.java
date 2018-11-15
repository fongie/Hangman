package net;

import DTO.Guess;
import DTO.StatusReport;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

/**
 * Handles server connection and server calls
 */
public class Connection implements Runnable {
   private static final String HOST = "192.168.0.2";
   private static final int PORT = 8080;

   private Observer printer;
   private boolean connected;

   private Selector selector;
   private SocketChannel socketChannel;

   private boolean readyToSend;
   private ByteBuffer sending;

   /**
    * Constructor, starts in new thread
    */
   public Connection(Observer printer) {
      this.printer = printer;
      new Thread(this).start();
   }

   @Override
   public void run() {
      start();
      while (true && connected) {
         communicate();
      }
      exit();
   }

   public void makeGuess(Guess guess) {
      ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
      ObjectOutputStream oOut = null;
      try {
         oOut = new ObjectOutputStream(byteOut);
         oOut.writeObject(guess);
         oOut.flush();
         oOut.close();
      } catch (IOException e) {
         e.printStackTrace();
      }
      sending = ByteBuffer.wrap(byteOut.toByteArray());
      readyToSend = true;
   }

   private void receiveReport() throws IOException {
      ByteBuffer buffer = ByteBuffer.allocate(1024);
      socketChannel.read(buffer);
      ByteArrayInputStream byteIn = new ByteArrayInputStream(buffer.array());
      ObjectInputStream oIn = new ObjectInputStream(byteIn);

      StatusReport report = null;
      try {
         report = (StatusReport) oIn.readObject();
      } catch (ClassNotFoundException e) {
         e.printStackTrace();
      }
      oIn.close();

      printer.print(report);
   }
   private void communicate() {
      try {
         selector.select(); //blocks until something can be done with channel
         for (SelectionKey key : selector.selectedKeys()) {
            selector.selectedKeys().remove(key);
            if (!key.isValid()) {
               continue;
            }
            if (key.isReadable()) {
               receiveReport();
               key.interestOps(SelectionKey.OP_WRITE);
            } else if (key.isWritable() && readyToSend) {
               socketChannel.write(sending);
               sending.clear();
               readyToSend = false;
               key.interestOps(SelectionKey.OP_READ);
            }
         }
      } catch (IOException e) {
         connected = false;
         System.err.println("Disconnected");
      }

   }

   /**
    * Starts the non-blocking socket communication, connects to server
    *
    */
   private void start() {
      try {
         //open channel (connect) to server
         socketChannel = SocketChannel.open();
         socketChannel.configureBlocking(false);
         InetSocketAddress server = new InetSocketAddress(HOST, PORT);
         socketChannel.connect(server);

         //register selector to the channel
         selector = Selector.open();
         socketChannel.register(selector, SelectionKey.OP_CONNECT); //notify when connection is done

         //wait for server to respond to the connection attempt and finish it
         selector.select();
         for (SelectionKey key : selector.selectedKeys()) { //because its a "SET" we cant just get the key but have to use an iterator..
            selector.selectedKeys().remove(key);
            socketChannel.finishConnect();
            connected = true;
            System.out.println("Connected to server!");
            key.interestOps(SelectionKey.OP_READ);
         }
      } catch (IOException e) {
         System.err.println("Could not connect to server");
         e.printStackTrace();
      }

   }
   private void exit() {
      try {
         selector.close();
         socketChannel.close();
      } catch (IOException e) {
         System.err.println("Did not exit gracefully");
      }
      ;
      System.exit(0);

   }


}
