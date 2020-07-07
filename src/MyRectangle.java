import java.io.Serializable;

import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class MyRectangle extends Rectangle implements SelectableNode ,Serializable {

    Color c;
    boolean pressed = false;

    public MyRectangle(double x, double y, double width, double height) {
        super(x, y, width, height);
        super.setFill(Color.TRANSPARENT);
        super.setStroke(Color.BLACK);
    }
    public MyRectangle() {
        this(0,0,0,0);
    }

    @Override
    public boolean requestSelection(boolean select) {
        return true;
    }

    @Override
    public void notifySelection(boolean select) {

        if (select) {
            pressed = true;
            c = (Color) this.getStroke();
            this.setStroke(Color.RED);
            this.setStrokeWidth(3);
        } else {
            pressed = false;
            this.setStroke(c);
            this.setStrokeWidth(1);
        }
    }

    @Override
    public boolean MyIsPressed() {

        return pressed;
    }

}