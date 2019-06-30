package Client;

import Interfaces.Observer;
import Models.Message;
import Models.Type;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

public class Connection_Receiver implements Runnable {

    private Socket socket;
    private Connection connection;
    private boolean stopped = false;
    private DataInputStream input;
    private DataOutputStream output;

    public Connection_Receiver(Socket clientSocket, Connection connection) throws IOException {
        this.connection = connection;
        this.socket = clientSocket;
        this.input = new DataInputStream(this.socket.getInputStream());
        this.output = new DataOutputStream(this.socket.getOutputStream());
    }

    @Override
    public void run() {
        while(!stopped){
            try {
                String me = this.input.readUTF();
                Message message = new Message(me);
                this.connection.notifySomethingHappened(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
