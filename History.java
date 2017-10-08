package editor;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;
import java.util.Stack;
import javafx.scene.web.WebView;
import javafx.scene.web.WebView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;

public class History {

    public static Stack<Group> undoStack = new Stack<>();
    public static Stack<Group> redoStack = new Stack<>();

/*
    public void copy() {
        String selectedText = (String) webview.getEngine().executeScript(
                "editor.getSelection();");
        final Clipboard clipboard = Clipboard.getSystemClipboard();
        final ClipboardContent content = new ClipboardContent();
        content.putString(selectedText);
        clipboard.setContent(content);
    }

    public void paste() {
        final Clipboard clipboard = Clipboard.getSystemClipboard();
        String content = (String) clipboard.getContent(DataFormat.PLAIN_TEXT);
        webview.getEngine().executeScript(String.format("editor.replaceSelection(\"%s\");", content));
    }

*/

}