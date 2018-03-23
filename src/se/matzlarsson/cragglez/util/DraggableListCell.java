package se.matzlarsson.cragglez.util;

import javafx.collections.ObservableList;
import javafx.scene.control.ListCell;
import javafx.scene.input.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.io.Serializable;

public class DraggableListCell<T extends Serializable> extends ListCell<T> {

    private static final DataFormat df = new DataFormat("any object");
    private static final Rectangle overlayTopRect = new Rectangle(300, 20, Color.color(0.9, 0.9, 0.9, 0.6));
    private static final Rectangle overlayBotRect = new Rectangle(0, 20, 300, 20);
    private Area prevArea = Area.NONE;

    { overlayBotRect.setFill(Color.color(0.9, 0.9, 0.9, 0.6)); }

    public DraggableListCell() {
        ListCell thisCell = this;
        updateSelectionMarkup(null);

        setOnDragDetected(event -> {
            if (getItem() == null) {
                return;
            }

            Dragboard dragboard = startDragAndDrop(TransferMode.MOVE);
            ClipboardContent content = new ClipboardContent();
            content.put(df, getItem());
            dragboard.setDragView(snapshot(null, null));
            dragboard.setContent(content);

            event.consume();
        });

        setOnDragOver(event -> {
            if (event.getGestureSource() != thisCell && event.getDragboard().hasContent(df)) {
                event.acceptTransferModes(TransferMode.MOVE);
            }
            updateSelectionMarkup(event);
            event.consume();
        });

        setOnDragEntered(event -> {
            updateSelectionMarkup(event);
        });

        setOnDragExited(event -> {
            updateSelectionMarkup(null);
        });

        setOnDragDropped(event -> {
            System.out.println("Getting stuff dropped");
            if (getItem() == null) {
                return;
            }

            updateSelectionMarkup(null);
            Dragboard db = event.getDragboard();
            boolean success = false;

            if (db.hasContent(df)) {
                ObservableList<T> items = getListView().getItems();
                int startPos = items.indexOf(db.getContent(df));
                int targetPos = items.indexOf(getItem());

                // Will put below if dropped on lower half
                if(event.getY() > getBoundsInLocal().getHeight()/2){
                    targetPos += 1;
                }

                // Find available items
                ObservableList<T> cells = getListView().getItems();
                // Add on new position
                cells.add(targetPos, cells.get(startPos));
                // Remove from old position
                if(startPos > targetPos){
                    cells.remove(startPos+1);
                }else {
                    cells.remove(startPos);
                }
                // Fix focus
                getListView().getSelectionModel().select(startPos>targetPos ? targetPos : targetPos-1);

                success = true;
            }
            event.setDropCompleted(success);

            event.consume();
        });

        setOnDragDone(DragEvent::consume);
    }

    private void updateSelectionMarkup(DragEvent event){
        boolean invalid = event == null || event.getGestureSource() == this || isEmpty() || !event.getDragboard().hasContent(df) || event.getY() > getHeight();
        Area newArea = (invalid ? Area.NONE : (event.getY() > getHeight()/2 ? Area.BOTTOM : Area.TOP));
        if(newArea != prevArea){
            if(prevArea != Area.NONE){
                getChildren().remove(prevArea == Area.TOP ? overlayTopRect : overlayBotRect);
            }
            if(newArea != Area.NONE){
                getChildren().add(newArea == Area.TOP ? overlayTopRect : overlayBotRect);
            }
            prevArea = newArea;
        }
    }

    private enum Area{
        NONE, TOP, BOTTOM
    }

}