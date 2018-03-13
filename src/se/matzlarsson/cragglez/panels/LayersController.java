package se.matzlarsson.cragglez.panels;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import se.matzlarsson.cragglez.model.Layer;
import se.matzlarsson.cragglez.model.Model;
import se.matzlarsson.cragglez.util.PropertyUtil;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LayersController implements Initializable{

    @FXML
    private ListView<Layer> layersList;

    @Override
    public void initialize(URL url, ResourceBundle bundle){
        layersList.setCellFactory(list -> {
            ListCell<Layer> cell = new ListCell<>(){

                private Node graphic;
                private LayerController controller;
                {
                    try{
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("layer.fxml"));
                        loader.setController(new LayerController());
                        graphic = loader.load();
                        controller = loader.getController();
                    }catch(IOException ioe){
                        System.out.println("Could not load layer panel template");
                    }
                }

                @Override
                protected void updateItem(Layer layer, boolean empty){
                    super.updateItem(layer, empty);
                    if(empty){
                        setGraphic(null);
                    }else {
                        controller.setLayer(layer);
                        setGraphic(graphic);
                    }
                }
            };
            cell.setContextMenu(getItemContextMenu());
            return cell;
        });

        layersList.setItems(Model.getInstance().layersProperty());
    }

    private ContextMenu getItemContextMenu(){
        final ContextMenu menu = new ContextMenu();
        MenuItem delete = new MenuItem("Delete");
        delete.setOnAction(event -> {
            if(layersList.getSelectionModel().getSelectedItem() != null) {
                Model.getInstance().removeLayer(layersList.getSelectionModel().getSelectedItem());
            }
        });
        menu.getItems().addAll(delete);
        return menu;
    }

    public ContextMenu getListContextMenu(){
        final ContextMenu menu = new ContextMenu();
        MenuItem add = new MenuItem("Add");
        add.setOnAction(event -> {
            System.out.println("herp");
        });
        return null;
    }


    public class LayerController {

        @FXML
        private Label layerLevel;
        @FXML
        private Label layerName;
        @FXML
        private Label layerPositionX;
        @FXML
        private Label layerPositionY;
        @FXML
        private Label layerWidth;
        @FXML
        private Label layerHeight;

        private Layer layer;

        public void setLayer(Layer layer){
            this.layer = layer;
            if(this.layer != null){
                layerLevel.textProperty().bindBidirectional(this.layer.levelProperty(), PropertyUtil.stringToNumConverter());
                layerName.textProperty().bind(this.layer.nameProperty());
                layerPositionX.textProperty().bindBidirectional(this.layer.xProperty(), PropertyUtil.stringToNumConverter());
                layerPositionY.textProperty().bindBidirectional(this.layer.yProperty(), PropertyUtil.stringToNumConverter());
                layerWidth.textProperty().bindBidirectional(this.layer.widthProperty(), PropertyUtil.stringToNumConverter());
                layerHeight.textProperty().bindBidirectional(this.layer.heightProperty(), PropertyUtil.stringToNumConverter());
            }
        }

        public void removeLayer(){
            if(this.layer != null) {
                Model.getInstance().removeLayer(this.layer);
            }
        }
    }

}
