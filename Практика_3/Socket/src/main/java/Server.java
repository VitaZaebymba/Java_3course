import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server {
    public static void main(String[] args){
        final ServerSocket serverSocket ;
        final Socket clientSocket ;
        final BufferedReader in;
        final PrintWriter out;
        try {
            serverSocket = new ServerSocket(5000);
            clientSocket = serverSocket.accept();
            out = new PrintWriter(clientSocket.getOutputStream());
            in = new BufferedReader (new InputStreamReader(clientSocket.getInputStream()));
            Message message=new Message();
            Thread sender= new Thread(new Runnable() {
                Message mes=message;
                String msg; //variable that will contains the data writter by the user
                @Override   // annotation to override the run method
                public void run() {
                    while(true){
                        try {
                            Thread.sleep(5000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        if(mes.getMessage().size()!=0){
                            out.println(mes.getMessage());    // write data stored in msg in the clientSocket
                            out.flush();   // forces the sending of the data
                            mes.reset();
                        }

                    }
                }
            });
            sender.start();

            Thread receive= new Thread(new Runnable() {
                Message mes=message;
                String msg ;
                @Override
                public void run() {
                    try {
                        msg = in.readLine();
                        //tant que le client est connecté
                        while(msg!=null){
                            System.out.println("Client : "+msg);
                            mes.add(msg);
                            msg = in.readLine();
                        }

                        System.out.println("Client déconecté");

                        out.close();
                        clientSocket.close();
                        serverSocket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            receive.start();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}