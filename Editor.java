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

//import javax.swing.*;
public class Editor extends Application {

    // instantiate list, displayChar, root, textRoot, cursor, KeyEventHandler here
    public static int WINDOW_WIDTH = 500;
    public static int WINDOW_HEIGHT = 500;
    public static int MARGIN = 5;
    public static Text displayChar;
    public static Group root;
    public static Group textRoot;
    public static editor.LinkedList list;
    public static KeyEventHandler keyEventHandler;
    public static MouseClickEventHandler mouseClickHandler;
    public static Cursor cursor;
    int posX = 5;
    int posY = 0;
    public int STARTING_FONT_SIZE = 12;
    public static int fontSize;
    public static String fontName;
    public static ScrollBar scrollBar;
    public int scrollbarMove;


    public static void main(String[] args) {
        Application.launch(editor.Editor.class, args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Mac TextEditor");

        final Parameters params = getParameters();
        final List<String> parameters = params.getRaw();


        String openFileName = parameters.get(0).toString();

        if (parameters.size() > 1) {
            //if the second argument exists, we check if it is debug.
            String debugOrNo = parameters.get(1).toString();
            if (debugOrNo.equals("debug")) {
                System.out.println("debugging started");

                return;
            }
            System.out.println("I don't understand the second argument");
        }


        // Register the event handler to be called for all KEY_PRESSED and KEY_TYPED events.
        root = new Group();
        list = new editor.LinkedList();

        // display text
        fontSize = STARTING_FONT_SIZE;
        fontName = "Verdana";
        displayChar = new Text(posX, posY, "");
        displayChar.setTextOrigin(VPos.TOP);
        displayChar.setFont(Font.font(fontName, fontSize));

        // add cursor to root
        cursor = new Cursor(posX, posY);
        root.getChildren().add(cursor.showCursor());

        // add textRoot & displayText to root
        textRoot = new Group();
        textRoot.getChildren().add(displayChar);
        root.getChildren().add(textRoot);


        // keyEvent handler
        keyEventHandler = new KeyEventHandler(list, root, WINDOW_WIDTH, WINDOW_HEIGHT);
        mouseClickHandler = new MouseClickEventHandler(list, root, cursor);
        Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT, Color.PINK);
        scene.setOnKeyTyped(keyEventHandler);
        scene.setOnKeyPressed(keyEventHandler);
        scene.setOnMouseClicked(mouseClickHandler);


        try {
            File openFile = new File(openFileName);

            if (openFile.isDirectory()) {
                System.out.println("Unable to open file nameThatIsADirectory");

            }
            if (!openFile.exists()) {
                //if files does not exist, we create a new file
                openFile = new File(openFileName);
                openFile.createNewFile();

            }


            FileReader reader = new FileReader(openFile);
            BufferedReader myReader = new BufferedReader(reader);

            int intRead = -1;

            while ((intRead = myReader.read()) != -1) {
                // The integer read can be cast to a char, because we're assuming ASCII.
                char charRead = (char) intRead;
                displayChar = new Text(posX, posY, String.valueOf(charRead));
                displayChar.setTextOrigin(VPos.TOP);
                displayChar.setFont(Font.font("Verdana", 12));
                root.getChildren().add(displayChar);
                posX += displayChar.getLayoutBounds().getWidth();
            }

        } catch (FileNotFoundException fileNotFoundException) {
            System.out.println("File not found! Exception was: " + fileNotFoundException);
        }


        // window resize
        scene.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldScreenWidth, Number newScreenWidth) {

                WINDOW_WIDTH = newScreenWidth.intValue();

                // update scrollbar
                int scrollBarWidth = (int) Math.round(scrollBar.getLayoutBounds().getWidth());
                int usableScreenWidth = WINDOW_WIDTH - scrollBarWidth;
                scrollBar.setLayoutX(usableScreenWidth);

                list.windowWidthInsideMargin = newScreenWidth.intValue() - 2 * MARGIN - scrollBarWidth;
                list.render(list.windowWidthInsideMargin, cursor);

            }
        });
        scene.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldScreenHeight, Number newScreenHeight) {

                int newHeight = newScreenHeight.intValue();
                WINDOW_HEIGHT = newHeight;

                scrollBar.setPrefHeight(newHeight);

            }
        });

        // set up initial scrollBar
        scrollBar = new ScrollBar();
        scrollBar.setOrientation(Orientation.VERTICAL);
        scrollBar.setPrefHeight(WINDOW_HEIGHT);
        scrollBar.setMin(0);
        scrollBar.setValue(0);
        scrollBar.setMax(500);
        scrollBar.setVisibleAmount(0);
        int scrollBarWidth = (int) Math.round(scrollBar.getLayoutBounds().getWidth());
        int usableScreenWidth = WINDOW_WIDTH - scrollBarWidth;
        list.windowWidthInsideMargin = WINDOW_WIDTH - 2 * MARGIN - scrollBarWidth;

        scrollbarMove = 0;

        scrollBar.setLayoutX(usableScreenWidth);
        root.getChildren().add(scrollBar);

        scrollBar.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                // use the difference of old & new value to move textRoot

                double change = newValue.doubleValue() - oldValue.doubleValue();
                double changeAbs = Math.abs(change);
                int move = (int) Math.round(changeAbs * LinkedList.textLengthInEachPixel);

                if (change <= 0) {
                    scrollbarMove += move;
                } else {
                    scrollbarMove -= move;
                }

                textRoot.setLayoutY(scrollbarMove);
                scrollBar.setValue(newValue.doubleValue());
            }
        });

        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
