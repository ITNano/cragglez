package se.matzlarsson.cragglez.model;

import java.util.Arrays;

public class Model {

    private static Model instance = null;
    private Screen[] screens = null;

    private Model(){
        reloadScreens();
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

}
