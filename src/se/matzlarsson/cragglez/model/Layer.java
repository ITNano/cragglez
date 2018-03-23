package se.matzlarsson.cragglez.model;

import javafx.beans.property.*;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Arrays;

public class Layer implements Externalizable{

    private final IntegerProperty level;
    private final StringProperty name;
    private final IntegerProperty x, y, width, height;
    private final BooleanProperty percentages;
    private final IntegerProperty screen;

    public Layer(){
        this(0, 0, 0, 0, 100, 100, false);
    }

    public Layer(int screen, int level, int x, int y, int width, int height, boolean percentage) {
        this("Layer " + level, screen, level, x, y, width, height, percentage);
    }

    public Layer(String name, int screen, int level, int x, int y, int width, int height, boolean percentage){
        this.name = new SimpleStringProperty(name);
        this.screen = new SimpleIntegerProperty(screen);
        this.level = new SimpleIntegerProperty(level);
        this.x = new SimpleIntegerProperty(x);
        this.y = new SimpleIntegerProperty(y);
        this.width = new SimpleIntegerProperty(width);
        this.height = new SimpleIntegerProperty(height);
        this.percentages = new SimpleBooleanProperty(percentage);
    }

    public IntegerProperty levelProperty(){ return level; }

    public int getLevel(){
        return level.get();
    }

    public StringProperty nameProperty(){
        return name;
    }

    public String getName(){
        return name.get();
    }

    public IntegerProperty screenProperty() {
        return screen;
    }

    public int getScreen(){
        return screen.get();
    }

    public IntegerProperty xProperty() {
        return x;
    }

    public int getX(){
        return x.get();
    }

    public IntegerProperty yProperty() {
        return y;
    }

    public int getY(){
        return y.get();
    }

    public IntegerProperty widthProperty() {
        return width;
    }

    public int getWidth(){
        return width.get();
    }

    public IntegerProperty heightProperty() {
        return height;
    }

    public int getHeight(){
        return height.get();
    }

    public BooleanProperty isPercentagesProperty(){
        return percentages;
    }

    public boolean isPercentages(){
        return percentages.get();
    }

    @Override
    public boolean equals(Object obj){
        if(obj == null || obj.getClass() != this.getClass()){
            return false;
        }

        Layer l2 = (Layer)obj;
        return getName().equals(l2.getName()) && getLevel() == l2.getLevel() && getScreen() == l2.getScreen() &&
               getX() == l2.getX() && getY() == l2.getY() && getWidth() == l2.getWidth() && getHeight() == l2.getHeight() &&
               isPercentages() == l2.isPercentages();
    }

    @Override
    public String toString(){
        return "Layer ["+getName()+"]\n\tLevel: "+getLevel()+"\n\tScreen: "+getScreen()+"\n\t"+getX()+","+getY()+"\n\t"+getWidth()+"x"+getHeight()+"\n\tPercentages: "+(isPercentages()?"Yes":"No");
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(getName());
        out.writeInt(getLevel());
        out.writeInt(getScreen());
        out.writeInt(getX());
        out.writeInt(getY());
        out.writeInt(getWidth());
        out.writeInt(getHeight());
        out.writeBoolean(isPercentages());
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        name.set((String)in.readObject());
        level.set(in.readInt());
        screen.set(in.readInt());
        x.set(in.readInt());
        y.set(in.readInt());
        width.set(in.readInt());
        height.set(in.readInt());
        percentages.set(in.readBoolean());
    }
}
