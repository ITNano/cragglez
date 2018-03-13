package se.matzlarsson.cragglez.model;

import java.awt.*;

public class Screen {

    private GraphicsDevice gd = null;
    private int screenIndex;

    public Screen(GraphicsDevice gd, int index){
        this.gd = gd;
        this.screenIndex = index;
    }

    public Rectangle getBounds(){
        return this.gd.getDefaultConfiguration().getBounds();
    }

    @Override
    public String toString(){
        Rectangle b = getBounds();
        return "Display "+(screenIndex+1)+" :: ("+((int)b.getX())+", "+((int)b.getY())+")   "+((int)b.getWidth())+"x"+((int)b.getHeight());
    }



    public static Screen[] getScreens(){
        GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice[] devices = env.getScreenDevices();
        Screen[] res = new Screen[devices.length];
        for(int i = 0; i<devices.length; i++){
            res[i] = new Screen(devices[i], i);
        }
        return res;
    }
}
