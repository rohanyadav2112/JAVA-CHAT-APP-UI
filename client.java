import java.net.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;

public class client extends JFrame{
    Socket socket;
    BufferedReader br;
    PrintWriter out;

// Declare Components
private JLabel heading = new JLabel("client area");     // JLabel is a java swing component 
                                                             //used to display a short text string
private JTextArea messageArea = new JTextArea();
private JTextField messageInput = new JTextField();
private Font font = new Font("Roboto",Font.PLAIN,20);
private String str;
//constructor
    public client(){
        try{
            System.out.println("Sending Request to Server...");
            socket = new Socket("127.0.0.1",7778);
            System.out.println("Connection Done...");
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream());

        createGui();
        handleEvents();
        startReading();
         // startWriting();
        }catch(Exception e){

        }
    }
    /**
     * 
     */
    private void handleEvents(){      // method
        messageInput.addKeyListener(new KeyListener(){

            @Override
            public void keyTyped(KeyEvent e) {
                // TODO Auto-generated method stub
              //  throw new UnsupportedOperationException("Unimplemented method 'keyTyped'");
            }

            @Override
            public void keyPressed(KeyEvent e) {
                // TODO Auto-generated method stub
            //    throw new UnsupportedOperationException("Unimplemented method 'keyPressed'");
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if(e.getKeyCode() == 10){
                    //System.out.println("You have Pressed Enter Button..");
                    String contentToSend = messageInput.getText();
                    messageArea.append("Me: " + contentToSend + "\n");
                    out.println(contentToSend);
                    out.flush();
                    messageInput.setText("");
                    messageInput.requestFocus();
                }
               }

        });
    }
    private void createGui(){        // method
        // GUI Code
        this.setTitle("Client Messager[END]");
        this.setSize(600,600);
        this.setLocationRelativeTo(null);    // it is used to center ur Window
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  // it is used to close the window by using exit button
        this.setVisible(true);

        // code for components
        heading.setFont(font);
        messageArea.setFont(font);
        messageInput.setFont(font);
        heading.setHorizontalAlignment(SwingConstants.CENTER);
        heading.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
        messageInput.setHorizontalAlignment(SwingConstants.CENTER);
        // frame layout setting
        this.setLayout(new BorderLayout());

        // adding components to the frame
        this.add(heading, BorderLayout.NORTH);
        this.add(messageArea, BorderLayout.CENTER);
        this.add(messageInput, BorderLayout.SOUTH);


        this.setVisible(true);
    }
    public void startReading() {
        // one thread will read
        Runnable r1 = () -> {
            System.out.println("Reader Started..");

            try {
                while (true) {     // infinte loop will always read

                    String msg = br.readLine();
                    if (msg.equals("exit")) {
                        System.out.println("Server Terminated the Chat");
                        JOptionPane.showMessageDialog(this, "server terminated the chat");
                        messageInput.setEnabled(false);
                        socket.close();
                        break;
                    }
                  //  System.out.println("Server: " + msg);
                  messageArea.append("Server: " + msg + "\n");

                }
            }catch (Exception e){
                e.printStackTrace();
            }
        };

        new Thread(r1).start();

    }
    public void startWriting(){
        // another thread will receive the data and will send to the client
        Runnable r2 = ()->{
            System.out.println("Writer Started..");
            try {
                while (true){
                    BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));

                    String content = br1.readLine();
                    out.println(content);
                    out.flush();

                }
            }catch (Exception e){
                e.printStackTrace();
            }
        };
        new Thread(r2).start();
    }
    public static void main(String[] args) {
        System.out.println("This is Client..");
        new client();
    }
}



