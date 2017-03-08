
/**
 * ProxyCache.java - Simple caching proxy
 */


import java.net.*;
import java.io.*;
import java.util.*;

public class ProxyCache {
  /** Port for the proxy */
  private static int port;

  private static int maxConnections;

  /** Socket for client connections */
  private static ServerSocket socket;

  /** Map for caching requested objects **/
  protected static  Map<String, byte[]> cached_request_objects;

  /** Create the ProxyCache object and the socket */
  public static void init(int p) {
    port = p;
    maxConnections=10;
    cached_request_objects = new HashMap<String, byte[]>();
    try {
      socket = new ServerSocket(port) ; /* Fill in */
    } catch (IOException e) {
      System.out.println("Error creating socket: " + e);
      System.exit(-1);
    }
  }

  /** Read command line arguments and start proxy */
  public static void main(String args[]) {
    int myPort = 0;

    try {
      myPort = Integer.parseInt(args[0]);
    } catch (ArrayIndexOutOfBoundsException e) {
      System.out.println("Need port number as argument");
      System.exit(-1);
    } catch (NumberFormatException e) {
      System.out.println("Please give port number as integer.");
      System.exit(-1);
    }
    init(myPort);

    System.out.println("Proxy server listening on port: " + myPort);
    int i=0;
    while( true ){ //(i++ < maxConnections) || (maxConnections == 0)){
      try{
        new ProxyThread(socket.accept(), cached_request_objects ).start();
      }
      catch (IOException ioe) {
        System.out.println("Error reading request from client: " + ioe);
        /* Definitely cannot continue processing this request,
         * so skip to next iteration of while loop. */
        continue;
      }
      //doComms conn_c= new doComms(server);
      //Thread t = new Thread(conn_c);
      //t.start();
    }
    }
  }

