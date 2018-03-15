package se.matzlarsson.cragglez.model;

import javafx.beans.property.*;
import javafx.collections.FXCollections;

import java.util.ArrayList;

public class Model {

    private static Model instance = null;
    private Screen[] screens = null;
    private ListProperty<Layer> layers;

    private Model(){
        reloadScreens();
        layers = new SimpleListProperty<>(FXCollections.observableArrayList());
        layers.add(new Layer("Music", 0, -100, -100, 10, 10, false));
        layers.add(new Layer("Main video", 5, 25, 25, 50, 50, true));
    }

    public static Model getInstance(){
        if(instance == null){
            instance = new Model();
        }
        return instance;
    }

    public void reloadScreens(){
        screens = Screen.getScreens();
    }

    public Screen[] getScreenData(){
        return screens;
    }

    public void createLayer(String name, int level, int x, int y, int width, int height, boolean usePercentage){
        layers.add(new Layer(name, level, x, y, width, height, usePercentage));
    }

    public boolean removeLayer(Layer layer){
        return layers.remove(layer);
    }

    public ReadOnlyListProperty<Layer> layersProperty(){
        return new ReadOnlyListWrapper<>(layers).getReadOnlyProperty();
    }

}
