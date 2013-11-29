import java.net.*;
import java.io.*;

/*
 * Authors:Cornell Farlin/ Phil Schwartz
 * Course: CSCI 4311
 * Date: 11/11/13
 */

/*
 * Client class: reads input from server and writes output from client using a socket.
 * 
 */
public class Client{
  protected Socket socket;
  OutputStream output;
  InputStream input;
  BufferedWriter buffWriter;
  PrintWriter printWriter;
  InputStreamReader inputReader;
  BufferedReader bufferedReader;
 
  
  //Constructs a new Client on the specified Host and Port (using a Socket)
  public Client(String host, int port) throws UnknownHostException, IOException{
    this.socket = new Socket(host, port);
    this.input = socket.getInputStream();
    this.output = socket.getOutputStream();
    this.printWriter = new PrintWriter(output, true);
    this.inputReader = new InputStreamReader(input);
    this.bufferedReader = new BufferedReader(inputReader);
}

   //Writes input from client to socket's OutputStream
  public void write(String message){
    this.printWriter.println(message);
    this.printWriter.flush();
  }
  
  //Reads input from socket's InputStream
  public String read() throws IOException{
    return this.bufferedReader.readLine();
  }
  
  //Close socket
  public void close() throws IOException{
    if(!this.socket.isClosed())
      this.socket.close();
  }
  
  //Is socket open?
  public boolean isOpen(){
      return !(this.socket.isClosed());
    }
  
  //Returns the input stream for the socket
  public InputStream getInputStream() throws IOException{
    return socket.getInputStream();
  }
  
  //Returns the output stream for the socket
  public OutputStream getOutputStream() throws IOException{
    return output;
  }
  
  //Returns the buffer size 
  public int getReceiveBufferSize() throws SocketException{
    return socket.getReceiveBufferSize();
  }}
