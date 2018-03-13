package se.matzlarsson.cragglez.debug;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import se.matzlarsson.cragglez.model.Model;

import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

public class ScreensController implements Initializable{

    private Model m = Model.getInstance();

    @FXML
    private Label debugLabel;

    @Override
    public void initialize(URL url, ResourceBundle bundle){
        flushDataToScreen();
    }

    @FXML
    private void reloadScreens(){
        m.reloadScreens();
        flushDataToScreen();
    }

    @FXML
    private void close(){
        ((Stage)debugLabel.getScene().getWindow()).close();
    }

    private void flushDataToScreen(){
        String data = String.join("\n", Arrays.stream(m.getScreenData()).map(s -> s.toString()).toArray(sz -> new String[sz]));
        debugLabel.setText("Displays:\n" + data);
    }
}
