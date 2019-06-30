package Interfaces;

import Models.Message;

public interface Observer {
    void notifyConnectionEstablished(int port);

    void notifyUserConnected(int port);

    void notifyMessageReceived(Message message);
}
