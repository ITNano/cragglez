package se.matzlarsson.cragglez.util;

import javafx.collections.ObservableList;
import javafx.scene.control.ListCell;
import javafx.scene.input.*;

import java.io.Serializable;

public class DraggableListCell<T extends Serializable> extends ListCell<T> {

    private static final DataFormat df = new DataFormat("any object");

    public DraggableListCell() {
        ListCell thisCell = this;

        setOnDragDetected(event -> {
            if (getItem() == null) {
                return;
            }

            Dragboard dragboard = startDragAndDrop(TransferMode.MOVE);
            ClipboardContent content = new ClipboardContent();
            content.put(df, getItem());
            dragboard.setDragView(snapshot(null, null));
            dragboard.setContent(content);

            System.out.println(dragboard.getContent(df));

            event.consume();
        });

        setOnDragOver(event -> {
            if (event.getGestureSource() != thisCell && event.getDragboard().hasContent(df)) {
                event.acceptTransferModes(TransferMode.MOVE);
            }

            event.consume();
        });

        setOnDragEntered(event -> {
            if (event.getGestureSource() != thisCell && event.getDragboard().hasContent(df)) {
                setOpacity(0.3);
            }
        });

        setOnDragExited(event -> {
            if (event.getGestureSource() != thisCell && event.getDragboard().hasContent(df)) {
                setOpacity(1);
            }
        });

        setOnDragDropped(event -> {
            if (getItem() == null) {
                return;
            }

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
}