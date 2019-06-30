package Interfaces;

import Models.Message;

public interface Observer {
    void notifyChanged();

    void notifyUserConnected(int socket);

    void notifyMessageReceived(Message message);
}
