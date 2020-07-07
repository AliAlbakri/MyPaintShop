import java.io.Serializable;

import javafx.scene.paint.Color;

public class MyCircle extends MyEllipse implements SelectableNode , Serializable {

    public MyCircle(double centerX, double centerY, double radius) {
        super(centerX, centerY, radius, radius);
        super.setFill(Color.TRANSPARENT);
        super.setStroke(Color.BLACK);

    }

    public MyCircle() {
        this(0,0,0);
    }

}