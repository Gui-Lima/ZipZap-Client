package Controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javax.annotation.Resource;
import java.io.IOException;

public class Chat{

    static final String pathToChat = "../Resources/Chat.fxml";

    public void display(){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource(pathToChat));
            Parent root = (Parent) loader.load();
            Stage stage = new Stage();
            stage.setTitle("Chat");
            stage.setScene(new Scene(root));
            stage.show();
        } catch(Exception e){
            e.printStackTrace();
        }
    }
}
