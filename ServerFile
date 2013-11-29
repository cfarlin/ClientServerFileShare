/*
 * Authors: Cornell Farlin / Phil Schwartz
 * Date: 11/11/13
 * Course: CSCI 4311
 */

import java.net.*;
import java.io.*;
import java.util.*;

public class ServerFile extends Thread{
    private static final int  TRANSFER_PORT= 2000;
    private int port;
    Socket clientSocket;
    InputStream input = null;
    FileOutputStream output = null;
    BufferedOutputStream buffOut = null;
    int buffsize=0;
    ServerSocket serverSocket;
    
    protected ServerFile(int port){
        this.port = port;
    }

    public static void main(String args[]){
        new ServerFile(TRANSFER_PORT).start();
    }

    public void run(){
        try{        
            serverSocket = new ServerSocket(this.port);
            System.out.println("File transfer server listening on " +serverSocket);
            while(true){
                clientSocket = serverSocket.accept();
                System.out.println("File transfer server connected to " + clientSocket);
                new Transferfile(clientSocket).start();
            }
       }catch(Exception e){
            e.printStackTrace();
        }}

    private class Transferfile extends Thread{
        Socket client;
        Socket sock;
        BufferedReader in;
        PrintWriter out;
        
        public Transferfile(Socket sock){
            this.sock = sock; 
        }

        public void run(){
            try{
                String clientmsg;
                client = sock;
                in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                out = new PrintWriter(client.getOutputStream());
                clientmsg = in.readLine().trim();
                if(clientmsg.equals("SEND")){
                    receive();
                }else if(clientmsg.equals("DOWNLOAD")){
                    send();
                }

            }catch(Exception ex){
                ex.printStackTrace();
            }}

        /*
         * Send method for server side. Checks to see if file name exists,
         * if so, sends file to client.
         */
        public void send() throws Exception{
            String filename = in.readLine().trim();
            File file = new File(filename);
            if(!(file.exists())){
                out.println("File not found");
                out.flush();
            }else{
                out.println("Ready");
                out.flush();
                long length = file.length();
                if(length> Integer.MAX_VALUE){
                  System.out.println("File is to large");
                }
                byte[] bytes = new byte[(int)length];
                FileInputStream filein = new FileInputStream(filename);
                BufferedInputStream buffin = new BufferedInputStream(filein);
                buffOut = new BufferedOutputStream(clientSocket.getOutputStream());
                
                int count;
                while((count =buffin.read(bytes))>0){
                  buffOut.write(bytes,0,count);
                }}}

        /*
         * Receive Method, before accepting the file, checks to see if 
         * file name is already in the directory. If so, the client
         * is prompted to over write the file or not.
         */
        public void receive() throws Exception{
            String filename= in.readLine().trim();
            if(filename.compareTo("File not found")==0){
                return;
            }
            File file = new File(filename);
            String option;
            if(file.exists()){
                out.println("File already exists");
                out.flush();
                option = in.readLine().trim();
            }else{
                out.println("Send file");
                out.flush();
                option = "Y";
            }
            if(option.compareTo("Y")==0){
                int i= 0;
                String temp;
                try{
                input = clientSocket.getInputStream();
                buffsize = clientSocket.getReceiveBufferSize();
                }catch(Exception e){
                  System.out.println("Error with input stream");
                }
                try{
                buffOut = new BufferedOutputStream(new FileOutputStream(file));
                }catch(Exception e){
                  System.out.println("Something wrong with the file");
                }
      
                byte[] bytes = new byte[buffsize];
                int count;
                while((count =input.read(bytes))>0){
                buffOut.write(bytes,0,count);
                }
                out.println("File received");
            }}}}
