
import java.net.*;
import java.io.*;
import java.util.*;


public class ProxyThread extends Thread {
  private static Socket client = null;
  private static final int BUFFER_SIZE = 32768;
  private static HttpRequest request = null;
  private static HttpResponse response = null;
  private static String current_URI_object;
    private static Map<String, byte[]> cached_request_objects;

  public ProxyThread(Socket socket, Map<String, byte[]> cached_request_objects ) {
      super("ProxyThread");
      this.cached_request_objects = cached_request_objects;
    this.client = socket;
  }
    
    public void run() {

    /* Process request. If there are any exceptions, then simply
     * return and end this request. This unfortunately means the
     * client will hang for a while, until it timeouts. */
    /* Read request */
    Socket server = null;

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

      //close out all resources
      if (client != null) {
        client.close();
      }
      if (server != null) {
        server.close();
      }
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

}
