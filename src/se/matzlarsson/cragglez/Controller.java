package se.matzlarsson.cragglez;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.input.InputEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import se.matzlarsson.cragglez.model.Model;

import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

public class Controller implements Initializable{

    private Model m = Model.getInstance();

    @FXML
    private MenuBar menuBar;

    @FXML
    private Label debugLabel;

    @Override
    public void initialize(URL url, ResourceBundle bundle){
        menuBar.setFocusTraversable(true);
        flushDataToScreen();
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
    private void reloadScreens(){
        m.reloadScreens();
        flushDataToScreen();
    }

    private void flushDataToScreen(){
        String data = String.join("\n", Arrays.stream(m.getScreenData()).map(s -> s.toString()).toArray(sz -> new String[sz]));
        debugLabel.setText(data);
    }

}
