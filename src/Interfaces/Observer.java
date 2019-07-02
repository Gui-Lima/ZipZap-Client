package Interfaces;

import Models.Message;

public interface Observer {
    void notifyConnectionEstablished(int port);

    void notifyUserConnected(int port);

    void notifyMessageReceived(Message message);

    void notifyMessageDeletion(Message message);

    void notifyStatusUpdate(Message message);

    void notifyServerClosed(Message message);
}
