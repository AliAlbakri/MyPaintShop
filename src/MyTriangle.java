import java.io.Serializable;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

public class MyTriangle extends Polygon implements  Serializable, SelectableNode {

    Color c;
    boolean pressed = false;

    public MyTriangle(Point2D side1, Point2D side2, Point2D side3) {
        super();
        super.getPoints().addAll(
                new Double[] { side1.getX(), side1.getY(), side2.getX(), side2.getY(), side3.getX(), side3.getY() });
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