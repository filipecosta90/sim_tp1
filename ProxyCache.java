/**
 * ProxyCache.java - Simple caching proxy
 *
 * $Id: ProxyCache.java,v 1.3 2004/02/16 15:22:00 kangasha Exp $
 *
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
  private static  Map<String, byte[]> cached_request_objects;

    
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

  public static void handle(Socket client) {
    Socket server = null;
    HttpRequest request = null;
    HttpResponse response = null;

    /* Process request. If there are any exceptions, then simply
     * return and end this request. This unfortunately means the
     * client will hang for a while, until it timeouts. */
    String current_URI_object;
    /* Read request */
    try {
      BufferedReader fromClient = new BufferedReader(new InputStreamReader(client.getInputStream())) ; /* Fill in */
      request = new HttpRequest(fromClient);/* Fill in */
      current_URI_object = request.getURI();
    } catch (IOException e) {
      System.out.println("Error reading request from client: " + e);
      return;
    }
    /* Send request to server */
    try {
      /* Open socket and write request to socket */

      server = new Socket(request.getHost(), request.getPort() ); /* Fill in */
      if ( cached_request_objects.containsKey(request.getURI() ) ){
        System.out.println("requested object is already cached: " + request.getURI());
      }
      DataOutputStream toServer = new DataOutputStream(server.getOutputStream());/* Fill in */
      toServer.writeBytes(request.toString());/* Fill in */
    } catch (UnknownHostException e) {
      System.out.println("Unknown host: " + request.getHost());
      System.out.println(e);
      return;
    } catch (IOException e) {
      System.out.println("Error writing request to server: " + e);
      return;
    }
    /* Read response and forward it to client */
    try {
      DataInputStream fromServer = new DataInputStream(server.getInputStream()); /* Fill in */
      response = new HttpResponse(fromServer);/* Fill in */
      DataOutputStream toClient = new DataOutputStream(client.getOutputStream());/* Fill in */
      toClient.writeBytes(response.toString());/* Fill in */
      toClient.write(response.body);/* Write response to client. First headers, then body */
      client.close();
      server.close();
      /* Insert object into the cache */
      /* Fill in (optional exercise only) */
      if ( ! cached_request_objects.containsKey(request.getURI()) && response.getBodyLength() > 0 ){
        System.out.println("Inserting a new object in cache of size " + response.getBodyLength() + " from URI: " + request.getURI());
        cached_request_objects.put(request.getURI(),response.getBody());
      }
    } catch (IOException e) {
      System.out.println("Error writing response to client: " + e);
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
          Socket server;
          int i=0;
          while((i++ < maxConnections) || (maxConnections == 0)){
              //doComms connection;
              
              try{
              server = socket.accept();
              handle(server);
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

