
import java.net.*;
import java.io.*;
import java.util.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class ProxyThread extends Thread {
  private  Socket client = null;
  private  Map<String, HttpResponse> cached_request_objects;

  public ProxyThread(Socket socket, Map<String, HttpResponse> cached_request_objects ) {
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
    HttpRequest request = null;
    HttpResponse response = null;
    String current_URI_object;
    boolean cached = false;

    long startTime = System.currentTimeMillis();

    try {
      BufferedReader fromClient = new BufferedReader(new InputStreamReader(client.getInputStream())) ; /* Fill in */
      if(fromClient != null){
        request = new HttpRequest(fromClient);/* Fill in */
        current_URI_object = request.getURI();
      }
      else{
        return;
      }
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
        cached = true;
      }
      else{
        DataOutputStream toServer = new DataOutputStream(server.getOutputStream());/* Fill in */
        toServer.writeBytes(request.toString());/* Fill in */
      }
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
      if ( cached == true ){
        response = cached_request_objects.get( request.getURI() );
      }
      else {
        DataInputStream fromServer = new DataInputStream(server.getInputStream()); /* Fill in */
        response = new HttpResponse(fromServer);/* Fill in */
      }
      // System.out.println("$$$$ Response " + response.toString() );
      DataOutputStream toClient = new DataOutputStream(client.getOutputStream());/* Fill in */
      toClient.writeBytes(response.toString());/* Fill in */
      toClient.write(response.body);/* Write response to client. First headers, then body */

      //close out all resources
      if (server != null) {
        server.close();
      }
      /* Insert object into the cache */
      /* Fill in (optional exercise only) */
      if ( ! cached_request_objects.containsKey(request.getURI()) && response.getBodyLength() > 0 ){
        System.out.println("Inserting a new object in cache of size " + response.getBodyLength() + " from URI: " + request.getURI());
        cached_request_objects.put(request.getURI(),response);
      }
    } catch (IOException e) {
      System.out.println("Error writing response to client: " + e);
    }
    long stopTime = System.currentTimeMillis();
    long elapsedTime = stopTime - startTime;
    double bits_per_second = response.getBodyLength() > 0 ? ((double)response.getBodyLength() * 8.0f) / ((double)elapsedTime / 1000.0f ) : 0.0f ;

    StringBuilder sb = new StringBuilder();

    String savestr = "response_time.csv";

    try{

      File f = new File(savestr);

      PrintWriter out = null;
      if ( f.exists() && !f.isDirectory() ) {
        out = new PrintWriter(new FileOutputStream(new File(savestr), true));
        sb.append(request.getURI());
        sb.append(',');
        sb.append(cached);
        sb.append(',');
        sb.append(elapsedTime);
        sb.append(',');
        sb.append(response.getBodyLength());
        sb.append(',');
        sb.append(bits_per_second);
        sb.append('\n');
        out.append(sb.toString());
        out.close();
      }
      else {
        out = new PrintWriter(savestr);
        sb.append("uri");
        sb.append(',');
        sb.append("cached");
        sb.append(',');
        sb.append("request time ms");
        sb.append(',');
        sb.append("response size bytes");
        sb.append(',');
        sb.append("bits per sec");
        sb.append('\n');
        sb.append(request.getURI());
        sb.append(',');
        sb.append(cached);
        sb.append(',');
        sb.append(elapsedTime);
        sb.append(',');
        sb.append(response.getBodyLength());
        sb.append(',');
        sb.append(bits_per_second);
        sb.append('\n');
        out.write(sb.toString());
        out.close();
      }

    } catch (FileNotFoundException e) {
    }

  }

}
