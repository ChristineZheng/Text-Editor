package editor;
import Jama.EigenvalueDecomposition;
import javafx.application.Application;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.beans.value.ChangeListener;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.ScrollBar;
import javafx.scene.input.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.util.LinkedList;

public class MouseClickEventHandler implements EventHandler<MouseEvent> {

    // constructor
    public MouseClickEventHandler(editor.LinkedList l, Group g, Cursor c) {
        Editor.cursor = c;
        Editor.list = l;
        Editor.root = g;

    }

    @Override
    public void handle(MouseEvent mouseEvent) {

        double mousePressedX = mouseEvent.getX();
        double mousePressedY = mouseEvent.getY();

        Editor.list.closestMouse(Editor.cursor, mousePressedX, mousePressedY);
        Editor.cursor.setX(Editor.list.Xposition);
        Editor.cursor.setY(Editor.list.Yposition);

    }


}
