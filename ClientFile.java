/**
 * Cornell Farlin / Phil Schwartz
 * CSCI 4311-01
 * Semester Project 
 */
//Test update
import java.net.*;
import java.io.*;
import java.util.*;

public class ClientFile {
    private int option;
    Scanner scanner;
    Client client;
    String host;
    int port;
    int buffsize =0;
    InputStream input;
    BufferedOutputStream buffOut;
    public static final String LIST = "-list";
    public static final String TRANSFER = "-transfer";
    private static final int LIST_PORT = 1000;
    private static final int TRANSFER_PORT = 2000;
    public static final int EXIT = 1; 

    public ClientFile(){
        try{
            scanner = new Scanner(System.in);
            start();
        } catch(Exception e){ 
            e.printStackTrace();
        }}
    
    public static void main(String args[]){
           new ClientFile(); 
    }
    
    //Exit process and disconnects with server
    public void exit(){
        System.out.println("Good-bye");
    }
    
    /*
     * Method to collect the port number and determine which process the clients
     * wants to connect to.
     */
    public void start() throws Exception{
        host = ""; //SET HOST TO SERVER HOST
        this.port = readProcessPort();
        while(port != 1){
            if(port == -1)//command error
                this.port = readProcessPort();
            else{
                client = new Client(host, this.port);
                menu();
                client.write(scanner.nextLine()); //write to server
                System.out.println(client.read()); //read from server
                //Get process
                this.port = readProcessPort();
            }
        }
        //Exit client
        exit();
    }
    
    //Menu for user to choose to send or download a file 
    public void menu() throws Exception{    
        while(true){
            System.out.println("You have chosen to transfer a file.");
            System.out.println("What would you like to do?");
            System.out.println("Choose a numerical option:");
            System.out.println("1.----- Send File");
            System.out.println("2.------ Download File");
            System.out.println("3.------- Return to Main menu");
            System.out.println("4.-------- Exit");
            
            String num = scanner.next();
            int option = Integer.parseInt(num);
            
            switch(option){
                case 1:
                client.write("SEND");
                send();
                break;
                case 2:
                client.write("DOWNLOAD");
                download();
                break;
                case 3:
                (new ClientFile()).start();
                case 0:
                client.write("EXIT");
                System.exit(1);
                default:
                System.out.println("Invalid input");
            }}}
    
    //Prints out a menu to the client, and reads the specified process to connect to.
    public int readProcessPort(){
        System.out.println();
        System.out.println("Welcome to UNO-file transfer system.");
        System.out.println("This system is for students to exchange notes.");
        System.out.println("Please press '-list', for a current list of files.");
        System.out.println("If you know the file you want to transfer");
        System.out.println("type '-transfer'");
        System.out.println("OR");
        System.out.println("Simply enter 0 to Exit.");
        System.out.flush();
        String process;
        process = scanner.next().trim();
        /*
         * Reads from the client input to determine which server/process to connect to. 
         * The system is setup where each server/process has its own port.
         */   
        if (process.equals(LIST))
            return LIST_PORT;
        else if (process.equals(TRANSFER)){
            return TRANSFER_PORT;
        }else if (process.equals("1")){
            return 1;
        }else{
            System.out.println("Invalid process entered. Retry!");
            return -1;
        }}
    
    /*
     * Send method for the user to send a file to the server.
     * If there is a file on the server with the same name, the
     * user is prompted to send a overwrite to the server or not.
     */
    public void send() throws Exception{
        String file;
        System.out.println("Enter the filename: ");
        file = scanner.next();
        File filename = new File(file);
        if(!(filename.exists())){
            System.out.println("File does not exists.");
        }
        client.write(file);
        String serverMsg = client.read();
        if(serverMsg.compareTo("File already exists")==0){
            String option;
            System.out.println("File already exists, Overwrite file?\n");
            System.out.println("Type 'Y' or 'N'");
            option = scanner.next().trim();
            if(option.equals("Y") || option.equals("y")){
                    client.write("Y");
            }else if(option.equals("N") || option.equals("n")){
                    client.write("N");
                    return;
            }else{
               System.out.println("Invalid input");
               return;
            }
            System.out.println("Sending file");
            long length = filename.length();
            if(length> Integer.MAX_VALUE){
              System.out.println("File is to large");
            }
            
            byte[] bytes = new byte[(int)length];
            FileInputStream filein = new FileInputStream(filename);
            BufferedInputStream buffin = new BufferedInputStream(filein);
            BufferedOutputStream buffout = new BufferedOutputStream(client.getOutputStream());
            int count;
            while((count =buffin.read(bytes))>0){
              buffout.write(bytes,0,count);
            }
        readProcessPort();
        }
        else{
        System.out.println("Sending file");
            long length = filename.length();
            if(length> Integer.MAX_VALUE){
              System.out.println("File is to large");
            }
            System.out.println(length);
            byte[] bytes = new byte[(int)length];
            FileInputStream filein = new FileInputStream(filename);
            BufferedInputStream buffin = new BufferedInputStream(filein);
            BufferedOutputStream buffout = new BufferedOutputStream(client.getOutputStream());
            System.out.println(bytes);
            System.out.println(buffout);
            int count;
            while((count =buffin.read(bytes))>0){
              buffout.write(bytes,0,count);
            }
        readProcessPort();
        }}

    /*
     * Download method that alows user to download a file from server.
     * Method checks to if there is a file with the same file name that 
     * exits, if so an overwrite option is given to the user.
     */
    public void download()throws Exception{
        String filename;
        System.out.println("Enter the file name");
        filename = scanner.next().trim();
        client.write(filename);
        String serverMsg = client.read();
        System.out.println(serverMsg);

        if(serverMsg.compareTo("File not found") == 0){
            System.out.println("File does not exists");
        }
        else if(serverMsg.compareTo("Ready")==0){

            System.out.println("Downloading File...");
            File file = new File(filename);
            if(file.exists()){
                System.out.println("File Already exists.\n");
                System.out.println("Would you like to OverWrite?\n");
                System.out.println("Type 'Y' of 'N'");
                String option;
                option = scanner.next().trim();
                System.out.println(option);
               
                if(option.equals("Y") || option.equals("y")){
                   //FileOutputStream fileout = new FileOutputStream(file);
                  System.out.println("hi");
                   try{
                     input = client.getInputStream();
                     buffsize = client.getReceiveBufferSize();
                     System.out.println("hi1");
                   }catch(Exception e){
                     System.out.println("Error with input stream");
                   }
                   try{
                     buffOut = new BufferedOutputStream(new FileOutputStream(file));
                     System.out.println("hi2");
                   }catch(Exception e){
                     System.out.println("Something wrong with the file");
                   }
                   System.out.println("hi3");////some error from here
                   byte[] bytes = new byte[buffsize];
                   int count;
                  
                   while((count =input.read(bytes))>0){
                     buffOut.write(bytes,0,count);
                  
                   }
                   System.out.println("hi4");////some error to here
                   
                }else if(option.equals("N") | option.equals("n")){
                        readProcessPort();
                        ;
                }else{
                        System.out.println("Invalid input");
                        readProcessPort();
                }
                if(option.equals("Y") | option.equals("y")){
                        System.out.println("Send successful");
                        readProcessPort();
                }else
                        System.out.println("Send UNsuccessful");
            }else{
                   int i= 0;
                   try{
                     input = client.getInputStream();
                     buffsize = client.getReceiveBufferSize();
                   }catch(Exception e){
                     System.out.println("Error with input stream");
                   }
                   try{
                     BufferedOutputStream buffOut = new BufferedOutputStream(new FileOutputStream(file));
                   }catch(Exception e){
                     System.out.println("Something wrong with the file");
                   }
      
                   byte[] bytes = new byte[buffsize];
                   int count;
                   while((count =input.read(bytes))>0){
                     buffOut.write(bytes,0,count);
                   }
                   System.out.println("Send successful");
                   readProcessPort();
  
            }}}

    //LIST METHOD
    public void list(){}
}
