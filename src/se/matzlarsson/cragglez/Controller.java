package se.matzlarsson.cragglez;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuBar;
import javafx.scene.input.InputEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import se.matzlarsson.cragglez.model.Model;
import se.matzlarsson.cragglez.util.FXUtil;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable{

    private Model m = Model.getInstance();

    @FXML
    private MenuBar menuBar;
    @FXML
    private AnchorPane contentBox;

    @Override
    public void initialize(URL url, ResourceBundle bundle){
        menuBar.setFocusTraversable(true);
        try {
            contentBox.getChildren().add(new FXMLLoader(getClass().getResource("panels/layers.fxml")).load());
        }catch(IOException ioe){
            System.out.println("Could not load external panels");
            ioe.printStackTrace();
        }
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
        FXUtil.openWindow(getClass().getResource("debug/"+resource), "Debug - "+titleName, 600, 400);
    }

}
