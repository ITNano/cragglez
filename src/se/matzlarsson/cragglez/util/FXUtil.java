package se.matzlarsson.cragglez.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;
import java.net.URL;
import java.util.function.Consumer;

public class FXUtil {

    private static final int magicInvalidX = -1254, magicInvalidY = -1251;

    public static void openWindow(URL url, String title, int width, int height){
        openWindow(url, title, width, height, null);
    }

    public static void openWindow(URL url, String title, int width, int height, Object controller){
        openWindow(url, title, width, height, controller, magicInvalidX, magicInvalidY);
    }

    public static void openWindow(URL url, String title, int width, int height, Object controller, int x, int y){
        openWindow(url, title, width, height, controller, x, y, null);
    }

    public static void openPopup(URL url, String title, int width, int height, Object controller, Window parent){
        openPopup(url, title, width, height, controller, magicInvalidX, magicInvalidY, parent);
    }

    public static void openPopup(URL url, String title, int width, int height, Object controller, int x, int y, Window parent){
        openWindow(url, title, width, height, controller, x, y, dialog -> {
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.initOwner(parent);
        });
    }

    private static void openWindow(URL url, String title, int width, int height, Object controller, int x, int y, Consumer<Stage> init){
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(url);
            if(controller != null) {
                loader.setController(controller);
            }
            Stage stage = new Stage();
            stage.setTitle(title);
            stage.setScene(new Scene(loader.load(), width, height));
            if(x != magicInvalidX && y != magicInvalidY) {
                stage.setX(x);
                stage.setY(y);
            }
            if(init != null){
                init.accept(stage);
            }
            stage.show();
        }catch(IOException ioe){
            System.out.println("Couldn't show window!");
        }
    }


    public static void makeTextfieldNumeric(final TextField tf){
        tf.textProperty().addListener( (observable, oldValue, newValue) -> {
            if (!newValue.matches("[-]?\\d*")) {
                if(newValue.charAt(0) == '-'){
                    tf.setText("-" + newValue.substring(1).replaceAll("[^\\d]", ""));
                }else {
                    tf.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
    }


    public static BackgroundImage getBackgroundImage(String url, int width, int height){
        return new BackgroundImage(new Image(url, width, height, false, true), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
    }

}
