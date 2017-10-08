package editor;

import javafx.animation.FillTransition;
import javafx.animation.Transition;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;


public class Cursor  {
    public Rectangle r;
    public FillTransition ft;
    public int posX;
    public int posY;
    public Cursor(int x, int y) {
        r = new Rectangle();
        r.setX(x);
        r.setY(y);
        r.setWidth(1.0);
        r.setHeight(14.96);

        //r.setFill(Color.BLACK);
        ft = new FillTransition(Duration.millis(500), r, Color.BLACK, Color.PINK);
        ft.setAutoReverse(true);
        ft.setCycleCount(Transition.INDEFINITE);
        ft.play();

    }
    public void setX(int x) {
        r.setX(x);
    }
    public void setY(int y) {
        r.setY(y);
    }

    // change cursor height when font size change
    public void setHeight(int h) {
        r.setHeight(h);
    }

    public int getPosX() {
        return (int) Math.round(r.getX());
    }

    public int getPosY() {
        return (int) Math.round(r.getY());
    }

    public void printCursorPosition () {
        System.out.println(getPosX()+", "+getPosY());
    }

    public Rectangle showCursor() {
        return r;
    }

}
