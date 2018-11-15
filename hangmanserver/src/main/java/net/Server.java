package net;

import DTO.Guess;

import java.io.IOException;
import java.net.*;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Entrypoint class to the server, which serves clients that wish to play Hangman
 */
public class Server {

   private static final int PORT = 8080;
   private static final int LINGER_TIME = 5000;

   private Selector selector;
   private ServerSocketChannel server; //listening to new connect attempts

   /**
    * Constructor
    */
   public Server() {
      start();
   }

   /**
    * Initialize serversocket and start serving clients forever
    */
   private void start() {
      try {
         selector = Selector.open();
         server = ServerSocketChannel.open();
         server.configureBlocking(false);
         server.bind(new InetSocketAddress(PORT));
         server.register(selector, SelectionKey.OP_ACCEPT); //register this channel to our selector and instruct it to listen for new connections
         System.out.println("Started listening channel..");
         while (true)
            serve();
      } catch (IOException e) {
         e.printStackTrace();
         System.err.println("Server failed while creating a listening socket.");
      }
   }

   private void serve() throws IOException {
      selector.select(); //blocks until there is something to be handled
      Iterator<SelectionKey> it = selector.selectedKeys().iterator();
      while (it.hasNext()) {
         SelectionKey key = it.next();
         it.remove();
         if (!key.isValid()) {
            continue;
         }
         if (key.isAcceptable()) {
            connectNewClient(key);
         } else if (key.isReadable()) {
            Client client = (Client) key.attachment();
            client.receiveGuess();
            key.interestOps(SelectionKey.OP_WRITE);
            // channel.read into byte buffer
            // convert byte buffer to Guess
            // send guess to client and game object

         } else if (key.isWritable()) {
            Client client = (Client) key.attachment();
            SocketChannel channel = (SocketChannel) key.channel();
            channel.write(client.getBuffer());
            key.interestOps(SelectionKey.OP_READ);
            //client must have prepared a byte buffered StatusReport from game to send
            //channel.write that buffer
         }
      }
   }

   private void connectNewClient(SelectionKey selectionKey) throws IOException {
      System.out.println("Connecting new client.");
      ServerSocketChannel listeningChannel = (ServerSocketChannel) selectionKey.channel(); //main listening channel
      SocketChannel clientChannel = listeningChannel.accept(); //accept a new client into a new channel
      clientChannel.configureBlocking(false);
      clientChannel.register(selector, SelectionKey.OP_WRITE, new Client(clientChannel, this)); //now selector watches this key that handles communication with that one client, and client object is attached to the key
      clientChannel.setOption(StandardSocketOptions.SO_LINGER, LINGER_TIME);
   }
}
