import java.io.Serializable;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;

public class MyEllipse extends Ellipse implements Serializable, SelectableNode {

    Color c;
    boolean pressed = false;

    public MyEllipse(double centerX, double centerY, double radiusX, double radiusY) {
        super(centerX, centerY, radiusX, radiusY);
        super.setFill(Color.TRANSPARENT);
        super.setStroke(Color.BLACK);
    }
    public MyEllipse() {
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