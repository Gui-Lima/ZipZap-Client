package Client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class Connection {
    static final int SERVER_ADDRESS = 9000;
    private Socket socket;

    public void connectToServer() throws IOException {
        socket = new Socket("localhost", SERVER_ADDRESS);
    }

    public void disconnectFromServer() throws IOException{
        socket.close();
    }

    public void stablishConnectionToUser(int port) throws IOException {
        InputStream input  = socket.getInputStream();
        OutputStream output = socket.getOutputStream();
        String message = String.valueOf(port);
        output.write(message.getBytes());
        output.close();
        input.close();
    }
}
