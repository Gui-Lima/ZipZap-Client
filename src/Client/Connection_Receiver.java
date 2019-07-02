package Client;
import Models.Message;
import Models.Type;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Connection_Receiver implements Runnable {

    private Socket socket;
    private Connection connection;
    private boolean stopped = false;
    private DataInputStream input;
    private DataOutputStream output;
    protected Thread runningThread = null;


    public Connection_Receiver(Socket clientSocket, Connection connection) throws IOException {
        this.connection = connection;
        this.socket = clientSocket;
        this.input = new DataInputStream(this.socket.getInputStream());
        this.output = new DataOutputStream(this.socket.getOutputStream());
    }

    @Override
    public void run() {
        Connection_Receiver connection_receiver = this;
        synchronized (connection_receiver) {
            this.runningThread = Thread.currentThread();
        }
        while(!stopped){
            try {
                String me = this.input.readUTF();
                Message message = new Message(me);
                if(message.getType() == Type.FINISH){
                    this.stopped = true;
                }
                this.connection.notifySomethingHappened(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
