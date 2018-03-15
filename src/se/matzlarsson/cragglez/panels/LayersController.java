package se.matzlarsson.cragglez.panels;

import javafx.beans.Observable;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;
import se.matzlarsson.cragglez.model.Layer;
import se.matzlarsson.cragglez.model.Model;
import se.matzlarsson.cragglez.util.FXUtil;
import se.matzlarsson.cragglez.util.PropertyUtil;
import se.matzlarsson.cragglez.util.Util;

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
                        setContextMenu(null);
                        setPrefHeight(40.0);
                    }else {
                        if(getContextMenu() == null){
                            setContextMenu(getItemContextMenu());
                        }
                        controller.setLayer(layer);
                        setGraphic(graphic);
                    }
                }
            };
            return cell;
        });

        layersList.setItems(Model.getInstance().layersProperty());
    }

    public void addLayer(){
        showUpdateWindow(null);
    }

    private void showUpdateWindow(Layer layer){
        FXUtil.openPopup(getClass().getResource("updatelayer.fxml"), "Layer "+(layer==null?"creator":"editor"), 200, 425, new UpdateLayerController(layer), layersList.getScene().getWindow());
    }

    private ContextMenu getItemContextMenu(){
        final ContextMenu menu = new ContextMenu();
        MenuItem update = new MenuItem(" Edit");
        update.setOnAction(event -> {
            showUpdateWindow(layersList.getSelectionModel().getSelectedItem());
        });
        MenuItem delete = new MenuItem(" Delete        ");
        delete.setOnAction(event -> {
            Model.getInstance().removeLayer(layersList.getSelectionModel().getSelectedItem());
        });
        menu.getItems().addAll(update, delete);
        return menu;
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
                layerPositionX.textProperty().bind(getPercentageBinding(layer.xProperty()));
                layerPositionY.textProperty().bind(getPercentageBinding(layer.yProperty()));
                layerWidth.textProperty().bind(getPercentageBinding(layer.widthProperty()));
                layerHeight.textProperty().bind(getPercentageBinding(layer.heightProperty()));
            }
        }

        private StringBinding getPercentageBinding(IntegerProperty ip){
            return PropertyUtil.multipleBound(new Observable[]{ip, layer.isPercentagesProperty()}, () -> ip.get() + (layer.isPercentagesProperty().get() ? "%" : ""));
        }
    }

    public class UpdateLayerController implements Initializable{

        private Layer layer;

        @FXML
        private TextField layerName;
        @FXML
        private Spinner<Integer> layerLevel;
        @FXML
        private TabPane posSizePane;
        @FXML
        private TextField layerX;
        @FXML
        private TextField layerY;
        @FXML
        private TextField layerWidth;
        @FXML
        private TextField layerHeight;
        @FXML
        private TextField layerPercentageX;
        @FXML
        private TextField layerPercentageY;
        @FXML
        private TextField layerPercentageWidth;
        @FXML
        private TextField layerPercentageHeight;
        @FXML
        private Button doneButton;

        public UpdateLayerController(Layer layer){
            this.layer = layer;
        }

        @Override
        public void initialize(URL url, ResourceBundle bundle){
            FXUtil.makeTextfieldNumeric(layerX);
            FXUtil.makeTextfieldNumeric(layerY);
            FXUtil.makeTextfieldNumeric(layerWidth);
            FXUtil.makeTextfieldNumeric(layerHeight);

            if(layer != null) {
                layerName.setText(layer.nameProperty().get());
                layerLevel.getEditor().setText(layer.levelProperty().get() + "");
                if(layer.isPercentagesProperty().get()) {
                    layerPercentageX.setText(layer.xProperty().get() + "");
                    layerPercentageY.setText(layer.yProperty().get() + "");
                    layerPercentageWidth.setText(layer.widthProperty().get() + "");
                    layerPercentageHeight.setText(layer.heightProperty().get() + "");
                    posSizePane.getSelectionModel().select(0);
                }else{
                    layerX.setText(layer.xProperty().get() + "");
                    layerY.setText(layer.yProperty().get() + "");
                    layerWidth.setText(layer.widthProperty().get() + "");
                    layerHeight.setText(layer.heightProperty().get() + "");
                    posSizePane.getSelectionModel().select(1);
                }
            }else{
                layerLevel.getEditor().setText("1");
            }

            doneButton.disableProperty().bind(isValidForm().not());
        }

        private BooleanBinding isValidForm(){
            BooleanBinding percentageCorrect = posSizePane.selectionModelProperty().get().selectedIndexProperty().isEqualTo(0).and(layerPercentageX.textProperty().isNotEmpty()).
                    and(layerPercentageY.textProperty().isNotEmpty()).and(layerPercentageWidth.textProperty().isNotEmpty()).and(layerPercentageHeight.textProperty().isNotEmpty());
            BooleanBinding pixelsCorrect = posSizePane.selectionModelProperty().get().selectedIndexProperty().isEqualTo(1).and(layerX.textProperty().isNotEmpty()).
                    and(layerY.textProperty().isNotEmpty()).and(layerWidth.textProperty().isNotEmpty()).and(layerHeight.textProperty().isNotEmpty());
            return layerName.textProperty().isNotEmpty().and(layerLevel.getEditor().textProperty().isNotEmpty()).and(percentageCorrect.or(pixelsCorrect));
        }

        @FXML
        private void saveChanges(){
            boolean percentage = posSizePane.getSelectionModel().getSelectedIndex() == 0;
            String name = layerName.getText();
            int level = Util.getInt(layerLevel.getEditor().getText());
            int x = Util.getInt(percentage?layerPercentageX.getText():layerX.getText());
            int y = Util.getInt(percentage?layerPercentageY.getText():layerY.getText());
            int w = Util.getInt(percentage?layerPercentageWidth.getText():layerWidth.getText());
            int h = Util.getInt(percentage?layerPercentageHeight.getText():layerHeight.getText());
            if(this.layer == null){
                Model.getInstance().createLayer(name, level, x, y, w, h, percentage);
            }else{
                this.layer.isPercentagesProperty().set(percentage);
                this.layer.nameProperty().set(name);
                this.layer.levelProperty().set(level);
                this.layer.xProperty().set(x);
                this.layer.yProperty().set(y);
                this.layer.widthProperty().set(w);
                this.layer.heightProperty().set(h);
            }

            ((Stage)layerName.getScene().getWindow()).close();
        }

    }

}
