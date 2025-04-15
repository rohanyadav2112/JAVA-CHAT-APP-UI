import java.io.*;
import java.net.*;
import javax.swing.*;
import java.awt.*;


public class server {
    ServerSocket server;
    Socket socket;
    BufferedReader br;   // for Reading
    PrintWriter out;    // for Writing

    public server() {  // constructor
        try {
            server = new ServerSocket(7778);
            System.out.println("Server is ready to accept Connection..");
            System.out.println("Waiting..");
            socket = server.accept();
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream());

            startReading();
            startWriting();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void startReading() {
        // one thread will read
        Runnable r1 = () -> {
            System.out.println("Reader Started..");

            while (true) {     // infinte loop will always read
                try {
                    String msg = br.readLine();
                    if (msg.equals("exit")) {
                        System.out.println("Client Terminated the Chat");
                        break;
                    }
                    System.out.println("Client: " + msg);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        };
        new Thread(r1).start();
    }

    public void startWriting(){
        // another thread will receive the data and will send to the client
        Runnable r2 = ()->{
            System.out.println("Writer Started..");
            while (true){
                try {

                    BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));

                    String content = br1.readLine();
                    out.println(content);
                    out.flush();

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        };
        new Thread(r2).start();
    }
    public static void main(String[] args) {
        System.out.println();
        new server();
    }
}
