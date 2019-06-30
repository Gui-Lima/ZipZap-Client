package Client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Connection_Receiver implements Runnable {

    private Socket socket;
    private boolean stopped = false;
    private DataInputStream input;
    private DataOutputStream output;

    public Connection_Receiver(Socket clientSocket) throws IOException {
        this.socket = clientSocket;
        this.input = new DataInputStream(this.socket.getInputStream());
        this.output = new DataOutputStream(this.socket.getOutputStream());
    }

    @Override
    public void run() {
        while(!stopped){
            try {
                System.out.println(this.input.readUTF());
                //tem que notificar o front
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
