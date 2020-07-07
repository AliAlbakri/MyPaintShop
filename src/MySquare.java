import java.io.Serializable;

import javafx.scene.paint.Color;

public class MySquare extends MyRectangle implements SelectableNode,Serializable {

    public MySquare(double x, double y, double side) {
        super(x, y, side, side);
        super.setFill(Color.TRANSPARENT);
        super.setStroke(Color.BLACK);
    }

    public MySquare() {
        this(0,0,0);
    }

}