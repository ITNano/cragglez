package se.matzlarsson.cragglez.util;

import javafx.collections.ObservableList;
import javafx.scene.control.ListCell;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;

public class DraggableListCell extends ListCell<String> {
    private final ImageView imageView = new ImageView();

    public DraggableListCell() {
        ListCell thisCell = this;

        setOnDragDetected(event -> {
            if (getItem() == null) {
                return;
            }

            Dragboard dragboard = startDragAndDrop(TransferMode.MOVE);
            ClipboardContent content = new ClipboardContent();
            content.putString(getItem());
            dragboard.setDragView(snapshot(null, null));
            dragboard.setContent(content);

            event.consume();
        });

        setOnDragOver(event -> {
            if (event.getGestureSource() != thisCell && event.getDragboard().hasString()) {
                event.acceptTransferModes(TransferMode.MOVE);
            }

            event.consume();
        });

        setOnDragEntered(event -> {
            if (event.getGestureSource() != thisCell && event.getDragboard().hasString()) {
                setOpacity(0.3);
            }
        });

        setOnDragExited(event -> {
            if (event.getGestureSource() != thisCell && event.getDragboard().hasString()) {
                setOpacity(1);
            }
        });

        setOnDragDropped(event -> {
            if (getItem() == null) {
                return;
            }

            Dragboard db = event.getDragboard();
            boolean success = false;

            if (db.hasString()) {
                ObservableList<String> items = getListView().getItems();
                int startPos = items.indexOf(db.getString());
                int targetPos = items.indexOf(getItem());

                // Will put below if dropped on lower half
                if(event.getY() > getBoundsInLocal().getHeight()/2){
                    targetPos += 1;
                }

                System.out.println("Switching "+startPos+" to "+targetPos);

                // Find available items
                ObservableList<String> cells = getListView().getItems();
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