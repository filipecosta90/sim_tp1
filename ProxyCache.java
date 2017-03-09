
/**
 * ProxyCache.java - Simple caching proxy
 */


import java.net.*;
import java.io.*;
import java.util.*;

public class ProxyCache {
  /** Read command line arguments and start proxy */
  public static void main(String args[]) throws FileNotFoundException{
    boolean active = true;

    /** Port for the proxy */
    int port = 3000;

    /** Socket for client connections */
    ServerSocket socket;

    /** Map for caching requested objects **/
    Map<String, HttpResponse> cached_request_objects;

    try {
      port = Integer.parseInt(args[0]);
    } catch (ArrayIndexOutOfBoundsException e) {
      System.out.println("Need port number as argument");
      System.exit(-1);
    } catch (NumberFormatException e) {
      System.out.println("Please give port number as integer.");
      System.exit(-1);
    }

    cached_request_objects = new HashMap<String, HttpResponse>();
    try {
      socket = new ServerSocket(port) ; /* Fill in */
      while( active ){
        System.out.println("Proxy server listening on port: " + port);
        System.out.println("\t Currently with  " + cached_request_objects.size() + " objects in cache.");
        new ProxyThread(socket.accept(), cached_request_objects ).start();
      }
      socket.close();

    } catch (IOException e) {
      System.out.println("Error creating socket: " + e);
      System.exit(-1);
    }
  }
}

