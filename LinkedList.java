package editor;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.junit.Test;

import java.util.ArrayList;


public class LinkedList {
    public static int heightOfFont;
    public static Node[] array;
    //public ArrayList<Node> array;
    public int windowWidthInsideMargin = Editor.WINDOW_WIDTH - 2 * Editor.MARGIN;
    public static Node current;
    public static Node front;
    public static Node back;
    public int Xposition;
    public int Yposition;
    public static int totalTextHeight;
    public static double textLengthInEachPixel;


    public static class Node {
        public Node pre;
        public Text item;
        public Node next;

        public int getX() {
            return (int) Math.round(this.item.getX());
        }
        public int getY() { return (int) Math.round(this.item.getY()); }

        public void setX(int x) {
            item.setX(x);
        }
        public void setY(int y) {
            item.setY(y);
        }

        public Node(Node p, Text i, Node n) {
            pre = p;
            item = i;
            next = n;
        }
    }
    // LinkedList constructor
    public LinkedList() {
        //array = new ArrayList<>();
        array = new Node[1000000];
        front = new Node(null, null, null);
        back = new Node(null ,null, null);
        current = front;
        current.next = back;
        Xposition = 5;
        Yposition = 0;

    }


    // get int type Text width, pass in Node
    public static int width(Node input) {
        double width = input.item.getLayoutBounds().getWidth();
        return (int) Math.round(width);
    }
    // get int type Text height, pass in Node
    public static int height(Node input) {
        double height = input.item.getLayoutBounds().getHeight();
        return (int) Math.round(height);
    }
    // get int type Text width
    public static int textWidth(Text input) {
        return (int) Math.round(input.getLayoutBounds().getWidth());
    }
    // get int type Text Height
    public static int textHeight(Text input) {
        return (int) Math.round(input.getLayoutBounds().getHeight());
    }

    // set current be the Node right before cursor position, for mouse click
    public Node get(Cursor cursor) {
        Node targetLine = Line(cursor);
        int XLengthAfterMargin5 = cursor.getPosX() - 5;
        while (XLengthAfterMargin5 > 0) {
            XLengthAfterMargin5 -= width(targetLine);
            targetLine = targetLine.next;
        }
        current = targetLine.pre;
        return current;
    }

    // update cursor position base on current
    public static void updateCursor(Node current, Cursor cursor) {
        if (current == front) {
            cursor.setX(5);
            cursor.setY(0);

        } else if (current.next == back) {
            cursor.setX(current.getX() + width(current));
            cursor.setY(current.getY());

        } else if (current.next.getY() > current.getY()) {
            cursor.setX(current.getX() + width(current));
            cursor.setY(current.getY());

        } else {
            cursor.setX(current.getX() + width(current));
            cursor.setY(current.getY());
        }
    }

    // cursor.posY is already updated, here is just to update cursor.posX and current
    public void closestMouse(Cursor cursor, double X, double Y) {

        if (current == front) {
            cursor.setX(5);
            cursor.setY(0);

        } else if ((X >= back.pre.getX() + width(back.pre)) && (Y >= back.pre.getY())) {
            // below end of file & X >= last letter position + width
            current = back.pre;
            cursor.setX(current.getX() + width(current));
            cursor.setY(back.pre.getY());

        } else {

            if (Y < 0) {
                Node firstLetter = front.next;
                if (X - 5 < width(firstLetter)) {
                    current = front;
                    cursor.setX(5);
                    cursor.setY(0);
                } else {
                    double left = firstLetter.getX();
                    double right = firstLetter.next.getX();
                    Node pointer = firstLetter;
                    while (pointer.next.getY() == 0) {
                        pointer = pointer.next;
                    }
                    while (X - left > 0 && X - right > 0 && firstLetter != pointer.next) {
                        firstLetter = firstLetter.next;
                        left = firstLetter.getX();
                        right = firstLetter.next.getX();
                    }
                    left = Math.abs(X - left);
                    right = Math.abs(X - right);
                    if (left <= right) {
                        current = firstLetter;
                    } else {
                        current = firstLetter.next;
                    }
                }
                cursor.setX(current.getX() + width(current));
                cursor.setY(0);

            } else {

                int index = (int) Math.floor(Y / heightOfFont);
                int cursorNewY = index * heightOfFont;
                Node firstLetterInCursorLine = array[index];

                if (X - 5 < width(firstLetterInCursorLine)) {
                    cursor.setX(5);
                    cursor.setY(cursorNewY);
                    current = get(cursor);

                } else {
                    double left = firstLetterInCursorLine.getX();
                    double right = firstLetterInCursorLine.next.getX();
                    while (X - left > 0 && X - right > 0 && firstLetterInCursorLine.next.next != back) {
                        firstLetterInCursorLine = firstLetterInCursorLine.next;
                        left = firstLetterInCursorLine.getX();
                        right = firstLetterInCursorLine.next.getX();

                    }
                    left = Math.abs(X - left);
                    right = Math.abs(X - right);
                    if (left <= right) {
                        current = firstLetterInCursorLine;
                    } else {
                        current = firstLetterInCursorLine.next;
                    }
                    cursor.setX(current.getX() + width(current));
                    cursor.setY(cursorNewY);
                }
            }
        }
        Xposition = cursor.getPosX();
        Yposition = cursor.getPosY();
    }


    // update current when left, right, up, down KeyEvent happens
    public void arrowKey(KeyCode code, Cursor cursor) {
        if (code == KeyCode.LEFT) {
            // cursor before the first letter, nothing change
            if (cursor.getPosX() == 5 && cursor.getPosY() == 0) {

            } else if (current.pre == front) {
                current = front;
                cursor.setX(5);
                cursor.setY(0);

            } else {
                current = current.pre;
                cursor.setX(current.getX() + width(current));
                cursor.setY(current.getY());
            }
        } else if (code == KeyCode.RIGHT) {
            // cursor at the end of file, nothing change
            if (current.next == back) {

            } else if (current == front) {
                current = front.next;
                cursor.setX(current.getX() + width(current));

            } else if (cursor.getPosX() == 5) {
                current = current.next;
                cursor.setX(current.getX() + width(current));

            } else if (current.next.getY() > current.getY()) {
                //current = current.next;
                cursor.setX(5);
                int Y = cursor.getPosY();
                cursor.setY(Y + heightOfFont);
            } else {
                current = current.next;
                cursor.setX(current.getX() + width(current));
            }
        } else if (code == KeyCode.UP) {
            // cursor at the start of file, nothing change
            if (cursor.getPosX() == 5 && cursor.getPosY() == 0) {

            } else if (cursor.getPosX() == 5) {
                int Y = cursor.getPosY();
                cursor.setY(Y - heightOfFont);
                current = Line(cursor).pre;

            } else if (cursor.getPosY() == 0) {

            } else {
                int cursorY = cursor.getPosY();
                Node firstLetterInPreviousLine = array[getArrayIndex(cursor.getPosY() - heightOfFont)];


                while (firstLetterInPreviousLine.next.getY() != cursorY) {

                    firstLetterInPreviousLine = firstLetterInPreviousLine.next;
                }


                while (firstLetterInPreviousLine.item.getText().equals(" ")) {
                    firstLetterInPreviousLine = firstLetterInPreviousLine.pre;
                }

                int XLengthInPreviousLine = firstLetterInPreviousLine.getX() + width(firstLetterInPreviousLine);


                // previous line shorter then the cursor's X position
                if (cursor.getPosX() >= XLengthInPreviousLine) {

                    current = firstLetterInPreviousLine;
                    cursor.setX(XLengthInPreviousLine);
                    cursor.setY(cursorY - heightOfFont);


                } else {
                    current = closest(cursor.getPosX(), cursor.getPosY() - heightOfFont, cursor);
                }
            }

        } else if (code == KeyCode.DOWN) {
            // cursor at the end of file or on the last line, nothing change
            if (current.next == back && cursor.getPosY() == back.pre.getY()) {

            } else if (cursor.getPosX() == 5) {
                int Y = cursor.getPosY();
                cursor.setY(Y + heightOfFont);
                current = Line(cursor).pre;

                // if cursor is at the last line, nothing change
            } else if (cursor.getPosY() == back.pre.getY()) {

                // if cursor is 2nd last line and only one letter in last line
            } else if (cursor.getPosY() == back.pre.getY() - heightOfFont
                    && cursor.getPosX() >= back.pre.getX() + width(back.pre)) {
                current = back.pre;
                cursor.setX(current.getX() + width(current));
                cursor.setY(current.getY());

            } else {
                // cursor  X position >= last letter X in next line
                int cursorY = cursor.getPosY();
                int cursorX = cursor.getPosX();
                Node firstLetterInNextLine = array[getArrayIndex(cursorY + heightOfFont)];

                System.out.println(firstLetterInNextLine.item.getText());
                System.out.println(firstLetterInNextLine.next.item.getText());
                while (firstLetterInNextLine.next.getY() != cursorY + 2 * heightOfFont) {
                    firstLetterInNextLine = firstLetterInNextLine.next;
                }
                while (firstLetterInNextLine.item.getText().equals(" ")) {
                    firstLetterInNextLine = firstLetterInNextLine.pre;
                }
                int XLengthInNextLine = firstLetterInNextLine.getX() + width(firstLetterInNextLine);

                if (cursor.getPosX() >= XLengthInNextLine) {
                    current = firstLetterInNextLine;
                    cursor.setX(XLengthInNextLine);
                    cursor.setY(cursorY + heightOfFont);
                } else {
                    current = closest(cursor.getPosX(), cursor.getPosY() + heightOfFont, cursor);
                }
            }
        }
        Xposition = cursor.getPosX();
        Yposition = cursor.getPosY();
    }

    // find closest node for UP & DOWN arrow key & update cursor position
    public Node closest(int oldX, int newY, Cursor cursor) {
        Node currentLine = array[newY / heightOfFont];

        if (oldX - 5 < width(currentLine)) {
            cursor.setX(5);
            cursor.setY(newY);

            if (currentLine == front.next) {
                current = front.next;
            } else {
                current = currentLine.pre;
            }

        } else {
            int left = currentLine.getX();
            int right = currentLine.next.getX();
            while (oldX - left > 0 && oldX - right > 0 && currentLine.next.next != back) {
                currentLine = currentLine.next;
                left = currentLine.getX();
                right = currentLine.next.getX();
            }
            left = Math.abs(oldX - left);
            right = Math.abs(oldX - right);
            if (left <= right) {
                current = currentLine;
            } else {
                current = currentLine.next;
            }
            cursor.setX(current.getX() + width(current));
            cursor.setY(newY);
        }
        return current;
    }

    // return the X position of a letter (right side of letter)
    public int getLetterXPosition(Node letterNode) {
        return letterNode.getX();
    }

    // return the Y position of a letter (bottom side of letter)
    public int getLetterYPosition(Node letterNode) {
        return letterNode.getY();
    }

    // find first letter in current line using the height Y
    public Node Line(Cursor cursor) {
        int index = (cursor.getPosY() / heightOfFont);
        return array[index];
    }

    public static int getArrayIndex(int Y) {
        return Y / heightOfFont;
    }

    // add Text
    public void add(Text inputText, Cursor cursor) {
        int cursorX = cursor.getPosX();
        int cursorY = cursor.getPosY();
        int inputTextWidth = textWidth(inputText);
        int inputTextHeight = textHeight(inputText);

        // add in the middle of file, need render
        if (current.next != back) {

                Node temp = current.next;
                Node newNode = new Node(current, inputText, temp);
                newNode.setX(cursorX);
                newNode.setY(cursorY);
                current.next = newNode;
                temp.pre = newNode;
                current = newNode;
                render(windowWidthInsideMargin, cursor);
                cursor.setX(current.getX() + width(current));
                cursor.setY(current.getY());

            // add letter when nothing after current
        } else {

            // add the very first letter in file
            if (cursorX == 5 && cursorY == 0) {

                array[0] = new Node(front, inputText, null);
                array[0].pre = front;
                front.next = array[0];
                current = front.next;
                current.setX(cursorX);
                current.setY(cursorY);
                cursor.setX(current.getX() + inputTextWidth);
                cursor.setY(current.getY());
                current.next = back;
                back.pre = current;


                // add new letter when enter is pressed, so cursor is at the start of new line
            } else if (cursorX == 5 && cursorY >= heightOfFont) {
                Node temp = new Node(current, inputText, null);
                temp.pre = current;
                array[cursorY / heightOfFont] = temp;
                current.next = temp;
                temp.setX(cursorX);
                temp.setY(cursorY);
                current = temp;
                cursor.setX(current.getX() + width(current));
                cursor.setY(current.getY());
                current.next = back;
                back.pre = current;

                // add new letter at the end of line and need to go to next new line
            } else if (cursorX + inputTextWidth > windowWidthInsideMargin) {
                current = wordWrap(current, inputText);
                cursor.setX(current.getX() + width(current));
                cursor.setY(current.getY());
                current.next = back;
                back.pre = current;

                // add new letter as normal
            } else {
                Node newNode = new Node(current, inputText, null);
                current.next = newNode;
                newNode.pre = current;
                current.next.setX(cursorX);
                current.next.setY(cursorY);
                current = current.next;
                cursor.setX(current.getX() + width(current));
                cursor.setY(current.getY());
                current.next = back;
                back.pre = current;

            }
        }
        Xposition = cursor.getPosX();
        Yposition = cursor.getPosY();

        totalTextHeight = totalTextHeight();
        textLengthInEachPixel = textLengthInEachPixel();
        Editor.scrollBar.setMax(totalTextHeight);
    }

    // re-render all letters in the file
    public void render(int windowWidthNoMargin, Cursor cursor) {
        // pointer doesn't need X, Y position ???
        Node pointer = new Node(null, null, null);
        pointer.next = front.next;
        int totalLengthY = 0;
        int totalLengthX = 5;

        while (pointer.next != back) {
            int letterWidth = width(pointer.next);
            int letterHeight = height(pointer.next);
            int indexInArray = (totalLengthY / heightOfFont);

            // array index point to first letter in line
            if (totalLengthX == 5) {

                pointer.next.setX(totalLengthX);
                pointer.next.setY(totalLengthY);

                array[indexInArray] = pointer.next;
                totalLengthX += letterWidth;
                pointer.next = pointer.next.next;

                // most basic update for a letter, no need go to new line
            } else if (totalLengthX + letterWidth <= windowWidthNoMargin) {

                pointer.next.setX(totalLengthX);
                pointer.next.setY(totalLengthY);

                totalLengthX += letterWidth;
                pointer.next = pointer.next.next;
                // word wrap when the last letter become first letter in next line
            } else {
                totalLengthY += heightOfFont;
                totalLengthX = wordWrapForRender(pointer.next, array, totalLengthY) + letterWidth;
                pointer.next = pointer.next.next;
            }
        }
        // update cursor X, Y after render
        updateCursor(current, cursor);

        totalTextHeight = totalTextHeight();
        textLengthInEachPixel = textLengthInEachPixel();
        Editor.scrollBar.setMax(totalTextHeight);

    }

    // wrap extra letter to next line only for render method, return current's X position
    public static int wordWrapForRender(Node letterGoToNewLine, Node[] array, int letterNewYPosition) {
        if (!wordMoreThenOneLineLong(letterGoToNewLine)) {
            Node nodePointer = letterGoToNewLine;
            letterGoToNewLine = findFirstLetterOfLastWord(letterGoToNewLine);
            System.out.println(letterGoToNewLine.item.getText());

            int allLetterLengthBeforeLastWord = letterGoToNewLine.getX() - 5;
            array[getArrayIndex(letterNewYPosition)] = letterGoToNewLine;

            while (letterGoToNewLine != nodePointer) {
                letterGoToNewLine.setY(letterNewYPosition);
                letterGoToNewLine.setX(letterGoToNewLine.getX() - allLetterLengthBeforeLastWord);
                letterGoToNewLine = letterGoToNewLine.next;
            }
            letterGoToNewLine.setY(letterNewYPosition);
            letterGoToNewLine.setX(letterGoToNewLine.getX() - allLetterLengthBeforeLastWord);

        } else {
            letterGoToNewLine.setX(5);
            letterGoToNewLine.setY(letterNewYPosition);
            array[getArrayIndex(letterNewYPosition)] = letterGoToNewLine;

        }
        return letterGoToNewLine.getX();
    }

    // check if a word is more then one line long, use only when add letter but exceed window length
    // the lastNode that pass in is the last node in the line
    public static boolean wordMoreThenOneLineLong(Node lastNode) {
        int yPositionOfLastNode = lastNode.getY();
        Node firstLetterInCurrentLine = array[getArrayIndex(yPositionOfLastNode)];

        if (lastNode.item.getText().equals(" ")) {
            return true;
        } else {
            while (lastNode != firstLetterInCurrentLine) {

                lastNode = lastNode.pre;
                if (lastNode.item.getText().equals(" ")) {
                    return false;
                }
            }
            if (lastNode.item.getText().equals(" ")) {
                return false;
            } else {
                return true;
            }
        }
    }

    // assume the last word is less then a line long, return the last word's first letter (letter after the most recent Empty Space)
    public static Node findFirstLetterOfLastWord(Node lastNode) {
        while (!lastNode.item.getText().equals(" ")) {
            lastNode = lastNode.pre;
        }
        return lastNode.next;
    }

    // wrap word to new line, the lastNodeInLine that pass in is the last node in line, not the new letter,
    // new letter hasn't been input yet.
    public Node wordWrap(Node lastNodeInLine, Text newLetter) {

        int lastWordHeightSoFar = lastNodeInLine.getY();
        int newYposition = lastWordHeightSoFar + heightOfFont;

        // no need wordWrap if word longer then one line, just return the last Node in line
        if (wordMoreThenOneLineLong(lastNodeInLine)) {

            Node newNode = new Node(lastNodeInLine, newLetter, null);
            newNode.setX(5);
            newNode.setY(newYposition);
            lastNodeInLine.next = newNode;
            newNode.pre = lastNodeInLine;
            array[getArrayIndex(newNode.getY())] = newNode;
            return newNode;

            // need wordWrap if last word is less then one line long
        } else {
            // look for the most recently Key Space
            Node pointer = lastNodeInLine;
            lastNodeInLine = findFirstLetterOfLastWord(lastNodeInLine);
            int allLetterLengthBeforeLastWord = lastNodeInLine.getX() - 5;
            array[getArrayIndex(newYposition)] = lastNodeInLine;

            // update position for last word
            while (lastNodeInLine != pointer) {
                lastNodeInLine.setY(newYposition);
                lastNodeInLine.setX(lastNodeInLine.getX() - allLetterLengthBeforeLastWord);
                lastNodeInLine = lastNodeInLine.next;
            }
            lastNodeInLine.setY(newYposition);
            lastNodeInLine.setX(lastNodeInLine.getX() - allLetterLengthBeforeLastWord);

            // add new letter
            Node newNode = new Node(lastNodeInLine, newLetter, null);
            lastNodeInLine.next = newNode;
            newNode.pre = lastNodeInLine;
            lastNodeInLine.next.setX(lastNodeInLine.getX() + width(lastNodeInLine));
            lastNodeInLine.next.setY(newYposition);

            return lastNodeInLine.next;
        }
    }

    public Text delete(Cursor cursor) {

        Text value = current.item;
        if (current == front) {

        } else if (current.next == back) {
            Node before = current.pre;
            before.next = back;
            current = before;
            current.next = back;
            back.pre = current;

        } else {
            Node before = current.pre;
            Node after = current.next;
            before.next = after;
            after.pre = before;
            current = before;
            render(windowWidthInsideMargin, cursor);
        }

        if (current == front) {
            cursor.setX(5);
            cursor.setY(0);

        } else {
            cursor.setX(current.getX() + width(current));
            cursor.setY(current.getY());
        }

        Xposition = cursor.getPosX();
        Yposition = cursor.getPosY();

        totalTextHeight = totalTextHeight();
        textLengthInEachPixel = textLengthInEachPixel();
        Editor.scrollBar.setMax(totalTextHeight);

        return value;
    }

    // total height of whole text using the last node back
    public int totalTextHeight() {
        int totalTextHeight = heightOfFont;
        if (current == front) {
            totalTextHeight = 0;
        } else {
            totalTextHeight += back.pre.getY();
        }
        return totalTextHeight;
    }

    // text height in each pixel in scrollbar
    public double textLengthInEachPixel() {
        int totalTextHeight = heightOfFont;
        if (current == front) {
            totalTextHeight = 0;
        } else {
            totalTextHeight += back.pre.getY();
        }
        double textHeightInEachPixel = totalTextHeight / Editor.WINDOW_HEIGHT;
        return textHeightInEachPixel;
    }



}

