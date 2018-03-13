package se.matzlarsson.cragglez;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.input.InputEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import se.matzlarsson.cragglez.model.Model;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

public class Controller implements Initializable{

    private Model m = Model.getInstance();

    @FXML
    private MenuBar menuBar;

    @Override
    public void initialize(URL url, ResourceBundle bundle){
        menuBar.setFocusTraversable(true);
    }

    @FXML
    private void handleKeyInput(final InputEvent event) {
        if (event instanceof KeyEvent) {
            final KeyEvent keyEvent = (KeyEvent) event;
            if(keyEvent.isControlDown()){
                switch(keyEvent.getCode()){
                    case R:         reloadScreens(); break;
                    case CONTROL:   break;
                    default:        System.out.println("Got unbound control key event: " + keyEvent.getCode());
                }
            }
        }
    }

    @FXML
    private void closeProgram(){
        Platform.exit();
        System.exit(0);
    }

    @FXML
    private void reloadScreens(){
        m.reloadScreens();
    }

    @FXML
    private void showScreens(){
        showDebugWindow("Screens", "screens.fxml");
    }


    private void showDebugWindow(String titleName, String resource){
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("debug/"+resource));
            Stage stage = new Stage();
            stage.setTitle("Debug - " + titleName);
            stage.setScene(new Scene(loader.load(), 600, 400));
            stage.show();
        }catch(IOException ioe){
            System.out.println("Couldn't show window!");
        }
    }

}
