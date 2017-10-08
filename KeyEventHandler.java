package editor;
import javafx.application.Application;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.beans.value.ChangeListener;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.ScrollBar;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.util.LinkedList;



public class KeyEventHandler implements EventHandler<KeyEvent> {

    public KeyEventHandler(editor.LinkedList l, Group g, int windowWidth, int windowHeight) {

        // instantiate cursor, textRoot here
        Editor.list = l;
        Editor.root = g;
        Editor.WINDOW_HEIGHT = windowHeight;
        Editor.WINDOW_WIDTH = windowWidth;
    }


    public void handle(KeyEvent keyEvent) {

        if (keyEvent.getEventType() == KeyEvent.KEY_TYPED && !keyEvent.isShortcutDown()) {

            String characterTyped = keyEvent.getCharacter();

            if (characterTyped.length() > 0 && characterTyped.charAt(0) == 8) {

                Text lastBuffer = Editor.list.delete(Editor.cursor);
                Editor.textRoot.getChildren().remove(lastBuffer);

                keyEvent.consume();
            }

            if (characterTyped.length() > 0 && characterTyped.charAt(0) != 8) {

                Editor.displayChar = new Text(Editor.cursor.getPosX(), Editor.cursor.getPosY(), String.valueOf(characterTyped));
                Editor.displayChar.setTextOrigin(VPos.TOP);
                Editor.displayChar.setFont(Font.font(Editor.fontName, Editor.fontSize));

                Editor.list.heightOfFont = (int) Math.round(Editor.displayChar.getLayoutBounds().getHeight());
                Editor.list.add(Editor.displayChar, Editor.cursor);
                Editor.cursor.setX(Editor.list.Xposition);
                Editor.cursor.setY(Editor.list.Yposition);
                Editor.textRoot.getChildren().add(Editor.displayChar);

                keyEvent.consume();
            }

        } else if (keyEvent.getEventType() == KeyEvent.KEY_PRESSED) {

            KeyCode code = keyEvent.getCode();
            KeyCombination keyCom = new KeyCodeCombination(KeyCode.P, KeyCombination.CONTROL_DOWN);

            if (keyEvent.isShortcutDown()) {
                if (code == KeyCode.PLUS || code == KeyCode.EQUALS) {
                    Editor.fontSize += 4;
                    editor.LinkedList.Node pointer = new editor.LinkedList.Node(null, null, null);
                    pointer.next = Editor.list.front.next;
                    while (pointer.next.item != null) {
                        pointer.next.item.setFont(Font.font(Editor.fontName, Editor.fontSize));
                        pointer.next = pointer.next.next;
                    }
                    // update heightOfFont in LinkedList
                    Editor.list.heightOfFont = (int) Math.round(Editor.list.front.next.item.getLayoutBounds().getHeight());
                    Editor.cursor.setHeight(Editor.list.heightOfFont);

                    Editor.list.render(Editor.list.windowWidthInsideMargin, Editor.cursor);

                } else if (code == KeyCode.MINUS) {
                    Editor.fontSize = Math.max(4, Editor.fontSize - 4);
                    editor.LinkedList.Node pointer = new editor.LinkedList.Node(null, null, null);
                    pointer.next = Editor.list.front.next;

                    while (pointer.next.item != null) {
                        pointer.next.item.setFont(Font.font(Editor.fontName, Editor.fontSize));
                        pointer.next = pointer.next.next;
                    }
                    // update heightOfFont in LinkedList
                    Editor.list.heightOfFont = (int) Math.round(Editor.list.front.next.item.getLayoutBounds().getHeight());
                    Editor.cursor.setHeight(Editor.list.heightOfFont);

                    Editor.list.render(Editor.list.windowWidthInsideMargin, Editor.cursor);

                    // check control P
                } else if (code == KeyCode.P) {
                    Editor.cursor.printCursorPosition();
                }

            } else if ((code == KeyCode.BACK_SPACE || code == KeyCode.DELETE)) {
                Text lastBuffer = Editor.list.delete(Editor.cursor);
                Editor.textRoot.getChildren().remove(lastBuffer);

            } else {
                Editor.list.arrowKey(code, Editor.cursor);
            }
        }
    }
}


