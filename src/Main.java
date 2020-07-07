import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.Cursor;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class Main extends Application {
    public static void main(String[] args) {

        launch(args);
    }

    Pane canvas = new Pane();
    ArrayList<Shape> shapes = new ArrayList<Shape>();
    double startX;
    double startY;
    FileChooser fileChooser = new FileChooser();
    FileChooser.ExtensionFilter imageFilter = new FileChooser.ExtensionFilter("Picture Files (*.png)", "*.png");


    public static void showErrorMessage(String message) { // This method is used to generate an error message with a customized message

        Stage errorStage = new Stage();
        errorStage.setResizable(false);
        errorStage.setTitle("ERROR");
        VBox errorRoot = new VBox();
        errorRoot.setAlignment(Pos.BASELINE_CENTER);
        Label errorLabel = new Label(message);
        errorLabel.setFont(new Font(16));
        Button errorOKButton = new Button("OK");
        errorOKButton.setFont(new Font(14));
        errorOKButton.setOnAction(e -> {
            errorStage.close();
        });
        errorRoot.getChildren().add(errorLabel);
        errorRoot.getChildren().add(errorOKButton);
        errorStage.setScene(new Scene(errorRoot));
        errorStage.show(); //displays the error window

    }

    public void start(Stage primaryStage) throws Exception {
        fileChooser.getExtensionFilters().add(imageFilter);
        // setting up the main window and the canvas
        int windowX = 1500, windowY = 800; //window dimensions
        HBox primaryRoot = new HBox();
        VBox secondryRoot = new VBox();
        VBox drawingSpace = new VBox();
        VBox drawingTools = new VBox();
        HBox shapePropertiesBar = new HBox(10);

        // setting up the menu bar
        MenuBar menuBar = new MenuBar();
        menuBar.setStyle("-fx-background-color: #999");
        Menu fileMenu = new Menu("File");
        menuBar.getMenus().add(fileMenu);//file menu added the the menu bar
        MenuItem openMenu = new MenuItem("Open"); //menu item#1
        MenuItem saveMenu = new MenuItem("Save"); //menu item#2
        fileMenu.getItems().add(openMenu); fileMenu.getItems().add(saveMenu); // adding menu items to the file menu

        // event handler for the save menu
        saveMenu.setOnAction(e -> {
            File file = fileChooser.showSaveDialog(primaryStage);//this fetches the file save location
            if (file != null) {
                try {
                    WritableImage drawing = new WritableImage(windowX, windowY);
                    canvas.snapshot(null, drawing);//creates an image of the canvas and stores it in the drawing instance of WritableImage
                    RenderedImage renderedImage = SwingFXUtils.fromFXImage(drawing, null); //converts from javafx WritableImage to swing RenderdImage, in order to write it into a png file
                    ImageIO.write(renderedImage, "png", file);
                } catch (IOException ee1) {
                    showErrorMessage("Error: Could not save drawing.");
                }
            }
        });

        // event handler for the open menu
        openMenu.setOnAction(e -> {
            File file = fileChooser.showOpenDialog(primaryStage);//this fetches the file for the user's directory
            if (file != null) {
                try {
                    InputStream inputStream = new FileInputStream(file);
                    Image image = new Image(inputStream);
                    canvas.getChildren().add(new ImageView(image));
                } catch (IOException e1) {
                    showErrorMessage("Error: Image could not be opened.");
                }
            }

        });

        canvas.setPrefSize(windowX, windowY);
        canvas.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, new BorderWidths(3))));

        drawingSpace.setAlignment(Pos.BOTTOM_CENTER);
        // formating
        drawingTools.setSpacing(20);
        drawingTools.setPrefWidth(200);
        drawingTools.setPadding(new Insets(5));
        drawingTools.setStyle("-fx-background-color: #999");

        secondryRoot.getChildren().add(menuBar);
        secondryRoot.getChildren().add(drawingSpace);

        primaryRoot.getChildren().add(secondryRoot);
        primaryRoot.getChildren().add(drawingTools);

        drawingSpace.getChildren().add(canvas);
        drawingSpace.getChildren().add(shapePropertiesBar);

        /* ----------Shape Properties Bar---------- */
        Label XAxis = new Label("X-Axis/Center");
        Label YAxis = new Label("Y-Axis/Center");
        Label circleRadius = new Label("Circle Radius");
        Label rectWidth = new Label("Width/Sides");
        Label rectHeight = new Label("Height");
        Label boarderColor = new Label("Boarder color");
        Label fillingColor = new Label("Filling color");

        TextField XAxisField = new TextField();
        TextField YAxisField = new TextField();
        TextField circleRadiusField = new TextField();
        TextField rectWidthField = new TextField();
        TextField rectHeightField = new TextField();

        Button apply = new Button("Apply");
        apply.setPrefSize(100, 50);
        apply.setBackground(new Background(new BackgroundFill(Color.SPRINGGREEN, null, null)));

        TextField[] textFieldsArray = { XAxisField, YAxisField, circleRadiusField, rectWidthField, rectHeightField };

        for (TextField textField : textFieldsArray) {
            textField.setMaxWidth(130);
            textField.setDisable(true);
        }
        /* ---------- Setting up color options ---------- */
        ColorPicker colorLinePicker = new ColorPicker(Color.BLACK);
        colorLinePicker.getStyleClass().add("split-button");
        ColorPicker colorShapeFiller = new ColorPicker(Color.TRANSPARENT);
        colorShapeFiller.getStyleClass().add("split-button");

        shapePropertiesBar.getChildren().addAll(XAxis, XAxisField, YAxis, YAxisField, circleRadius, circleRadiusField,
                rectWidth, rectWidthField, rectHeight, rectHeightField, fillingColor, colorShapeFiller, boarderColor,
                colorLinePicker, apply);

        shapePropertiesBar.setPadding(new Insets(10));
        shapePropertiesBar.setAlignment(Pos.CENTER);
        shapePropertiesBar.setStyle("-fx-background-color: #999");

        /* ----------Interact---------- */
        SelectionHandler selectionHandler = new SelectionHandler(canvas);
        canvas.addEventHandler(MouseEvent.MOUSE_PRESSED, selectionHandler.getMousePressedEventHandler());
        /* ----------Drawing Tools Column---------- */

        ToggleGroup drawingButtonsGroup = new ToggleGroup();
        ToggleButton freeDrawButton = new ToggleButton("Free Draw");
        ToggleButton drawCircleButton = new ToggleButton("Draw Circle");
        ToggleButton drawRectangleButton = new ToggleButton("Draw Rectangle");
        ToggleButton drawSquareButton = new ToggleButton("Draw Square");
        ToggleButton drawEllipseButton = new ToggleButton("Draw Ellipse");

        ToggleButton[] drawingButtonsArr = { freeDrawButton, drawCircleButton, drawRectangleButton, drawSquareButton,
                drawEllipseButton };

        for (ToggleButton drawingbutton : drawingButtonsArr) {
            drawingbutton.setPrefSize(100, 50);
            drawingbutton.setToggleGroup(drawingButtonsGroup);
            drawingbutton.setCursor(Cursor.HAND);
            drawingTools.getChildren().add(drawingbutton);

        }

        Button clearButton = new Button("Clear");

        clearButton.setAlignment(Pos.CENTER);
        clearButton.setPrefSize(100, 50);
        clearButton.setBackground(new Background(new BackgroundFill(Color.ORANGERED, null, null)));
        drawingTools.getChildren().add(clearButton);

        /*---------------- Event handling ----------------*/

        clearButton.setOnAction(e -> { // clearing the canvas after providing the user with a confirmation prompt
            Stage sureStage = new Stage();
            sureStage.setTitle("Confirmation");
            sureStage.setResizable(false);
            VBox sureRoot = new VBox();
            HBox sureButtons = new HBox();
            Button yesButton = new Button("Yes");
            Button noButton = new Button("No");
            Label sureLabel = new Label("Are sure you want to clear the canvas? (cannot be undone)");
            sureLabel.setAlignment(Pos.CENTER);
            sureRoot.getChildren().add(sureLabel);
            sureRoot.getChildren().add(sureButtons);
            sureButtons.getChildren().add(yesButton);
            sureButtons.getChildren().add(noButton);
            sureButtons.setAlignment(Pos.CENTER);
            yesButton.setPrefSize(40, 20);
            noButton.setPrefSize(40, 20);
            yesButton.setOnAction(e1 -> {
                canvas.getChildren().clear();
                shapes.clear();
                sureStage.close();
            });
            noButton.setOnAction(e1 -> {
                sureStage.close();
            });
            sureStage.setScene(new Scene(sureRoot, 350, 50));
            sureStage.show();
        });

        canvas.setOnMousePressed(event -> {

            for (TextField textField : textFieldsArray) {
                textField.setDisable(true);
            }
            startX = event.getX();
            startY = event.getY();
            if (freeDrawButton.isSelected()) {
                canvas.setOnMouseDragged(dragged -> {
                    Circle dot = new Circle(dragged.getX(), dragged.getY(), 2);
                    dot.setFill(colorShapeFiller.getValue());
                    canvas.getChildren().add(dot);
                });

            }
            // Draw Circle
            else if (drawCircleButton.isSelected() && startX < windowX && startY < windowY) {
                shapes.add(new MyCircle());
                canvas.getChildren().add(shapes.get(shapes.size() - 1));

                canvas.setOnMouseDragged(dragged -> {
                    double endX = dragged.getX();
                    double endY = dragged.getY();

                    ((MyCircle) shapes.get(shapes.size() - 1)).setCenterX((startX + endX) / 2);
                    ((MyCircle) shapes.get(shapes.size() - 1)).setCenterY((startY + endY) / 2);
                    ((MyCircle) shapes.get(shapes.size() - 1))
                            .setRadiusX(Math.sqrt(Math.pow(startX - endX, 2) + Math.pow(startY - endY, 2)) / 2);
                    ((MyCircle) shapes.get(shapes.size() - 1))
                            .setRadiusY(Math.sqrt(Math.pow(startX - endX, 2) + Math.pow(startY - endY, 2)) / 2);

                });

            }
            // Draw Rectangle
            else if (drawRectangleButton.isSelected() && startX < windowX && startY < windowY) {
                shapes.add(new MyRectangle());
                canvas.getChildren().add(shapes.get(shapes.size() - 1));

                canvas.setOnMouseDragged(dragged -> {
                    double endX = dragged.getX();
                    double endY = dragged.getY();

                    // Determine the head of Rectangle
                    if (endX > startX && endY > startY) {
                        ((MyRectangle) shapes.get(shapes.size() - 1)).setX(startX);
                        ((MyRectangle) shapes.get(shapes.size() - 1)).setY(startY);
                        ((MyRectangle) shapes.get(shapes.size() - 1)).setWidth(Math.abs(endX - startX));
                        ((MyRectangle) shapes.get(shapes.size() - 1)).setHeight(Math.abs(endY - startY));
                    } else if (endX > startX && startY > endY) {
                        ((MyRectangle) shapes.get(shapes.size() - 1)).setX(startX);
                        ((MyRectangle) shapes.get(shapes.size() - 1)).setY(endY);
                        ((MyRectangle) shapes.get(shapes.size() - 1)).setWidth(Math.abs(endX - startX));
                        ((MyRectangle) shapes.get(shapes.size() - 1)).setHeight(Math.abs(endY - startY));
                    } else if (startX > endX && endY > startY) {
                        ((MyRectangle) shapes.get(shapes.size() - 1)).setX(endX);
                        ((MyRectangle) shapes.get(shapes.size() - 1)).setY(startY);
                        ((MyRectangle) shapes.get(shapes.size() - 1)).setWidth(Math.abs(endX - startX));
                        ((MyRectangle) shapes.get(shapes.size() - 1)).setHeight(Math.abs(endY - startY));
                    } else {
                        ((MyRectangle) shapes.get(shapes.size() - 1)).setX(endX);
                        ((MyRectangle) shapes.get(shapes.size() - 1)).setY(endY);
                        ((MyRectangle) shapes.get(shapes.size() - 1)).setWidth(Math.abs(endX - startX));
                        ((MyRectangle) shapes.get(shapes.size() - 1)).setHeight(Math.abs(endY - startY));
                    }
                });
            }

            // Draw Square
            else if (drawSquareButton.isSelected() && startX < windowX && startY < windowY) {
                shapes.add(new MySquare());
                canvas.getChildren().add(shapes.get(shapes.size() - 1));

                canvas.setOnMouseDragged(dragged -> {
                    double endX = dragged.getX();
                    double endY = dragged.getY();

                    // Determine the head of Rectangle
                    if (endX > startX && endY > startY) {
                        // Find the smallest side
                        if (Math.abs(startX - endX) > Math.abs(startY - endY)) {
                            ((MySquare) shapes.get(shapes.size() - 1)).setX(startX);
                            ((MySquare) shapes.get(shapes.size() - 1)).setY(startY);
                            ((MySquare) shapes.get(shapes.size() - 1)).setHeight(Math.abs(endY - startY));
                            ((MySquare) shapes.get(shapes.size() - 1)).setWidth(Math.abs(endY - startY));

                        } else {
                            ((MySquare) shapes.get(shapes.size() - 1)).setX(startX);
                            ((MySquare) shapes.get(shapes.size() - 1)).setY(startY);
                            ((MySquare) shapes.get(shapes.size() - 1)).setHeight(Math.abs(endX - startX));
                            ((MySquare) shapes.get(shapes.size() - 1)).setWidth(Math.abs(endX - startX));

                        }
                    } else if (endX > startX && startY > endY) {
                        if (Math.abs(startX - endX) > Math.abs(startY - endY)) {
                            ((MySquare) shapes.get(shapes.size() - 1)).setX(startX);
                            ((MySquare) shapes.get(shapes.size() - 1)).setY(endY);
                            ((MySquare) shapes.get(shapes.size() - 1)).setHeight(Math.abs(endY - startY));
                            ((MySquare) shapes.get(shapes.size() - 1)).setWidth(Math.abs(endY - startY));

                        } else {
                            ((MySquare) shapes.get(shapes.size() - 1)).setX(startX);
                            ((MySquare) shapes.get(shapes.size() - 1)).setY(endY);
                            ((MySquare) shapes.get(shapes.size() - 1)).setHeight(Math.abs(endX - startX));
                            ((MySquare) shapes.get(shapes.size() - 1)).setWidth(Math.abs(endX - startX));
                        }

                    } else if (startX > endX && endY > startY) {
                        if (Math.abs(startX - endX) > Math.abs(startY - endY)) {
                            ((MySquare) shapes.get(shapes.size() - 1)).setX(endX);
                            ((MySquare) shapes.get(shapes.size() - 1)).setY(startY);
                            ((MySquare) shapes.get(shapes.size() - 1)).setHeight(Math.abs(endY - startY));
                            ((MySquare) shapes.get(shapes.size() - 1)).setWidth(Math.abs(endY - startY));

                        } else {
                            ((MySquare) shapes.get(shapes.size() - 1)).setX(endX);
                            ((MySquare) shapes.get(shapes.size() - 1)).setY(startY);
                            ((MySquare) shapes.get(shapes.size() - 1)).setHeight(Math.abs(endX - startX));
                            ((MySquare) shapes.get(shapes.size() - 1)).setWidth(Math.abs(endX - startX));
                        }
                    } else {
                        if (Math.abs(startX - endX) > Math.abs(startY - endY)) {
                            ((MySquare) shapes.get(shapes.size() - 1)).setX(endX);
                            ((MySquare) shapes.get(shapes.size() - 1)).setY(endY);
                            ((MySquare) shapes.get(shapes.size() - 1)).setHeight(Math.abs(endY - startY));
                            ((MySquare) shapes.get(shapes.size() - 1)).setWidth(Math.abs(endY - startY));

                        } else {
                            ((MySquare) shapes.get(shapes.size() - 1)).setX(endX);
                            ((MySquare) shapes.get(shapes.size() - 1)).setY(endY);
                            ((MySquare) shapes.get(shapes.size() - 1)).setHeight(Math.abs(endX - startX));
                            ((MySquare) shapes.get(shapes.size() - 1)).setWidth(Math.abs(endX - startX));
                        }
                    }

                });
            }

            // Draw Ellipse
            else if (drawEllipseButton.isSelected() && startX < windowX && startY < windowY) {
                shapes.add(new MyEllipse());
                canvas.getChildren().add(shapes.get(shapes.size() - 1));

                canvas.setOnMouseDragged(dragged -> {
                    double endX = dragged.getX();
                    double endY = dragged.getY();

                    ((MyEllipse) shapes.get(shapes.size() - 1)).setCenterX((startX + endX) / 2);
                    ((MyEllipse) shapes.get(shapes.size() - 1)).setCenterY((startY + endY) / 2);
                    ((MyEllipse) shapes.get(shapes.size() - 1)).setRadiusX(Math.abs(startX - endX) / 2);
                    ((MyEllipse) shapes.get(shapes.size() - 1)).setRadiusY(Math.abs(startY - endY) / 2);

                });

            }
            // Rejecting any dragged if user did not choose any shape (ToggleButton)
            else
                canvas.setOnMouseDragged(null);

            /* ----------Shape Properties Bar Event Handling---------- */
            for (int i = 0; i < shapes.size(); i++) {
                if (shapes.get(i).isPressed()) {
                    if (shapes.get(i) instanceof MyCircle) {
                        rectWidthField.setText("");
                        rectHeightField.setText("");
                        rectWidthField.setDisable(true);
                        rectHeightField.setDisable(true);
                        XAxisField.setDisable(false);
                        YAxisField.setDisable(false);
                        circleRadiusField.setDisable(false);
                        circleRadiusField
                                .setText(Math.round(((((MyCircle) shapes.get(i)).getRadiusX())) * 100.0) / 100.0 + "");
                        XAxisField.setText(Math.round((((MyCircle) shapes.get(i)).getCenterX() * 100.0) / 100.0) + "");
                        YAxisField.setText(Math.round((((MyCircle) shapes.get(i)).getCenterY() * 100.0) / 100.0) + "");

                    } else if (shapes.get(i) instanceof MySquare) {
                        circleRadiusField.setText("");
                        rectHeightField.setText("");
                        rectWidthField.setDisable(false);
                        rectHeightField.setDisable(true);
                        XAxisField.setDisable(false);
                        YAxisField.setDisable(false);
                        circleRadiusField.setDisable(true);
                        rectWidthField.setText((Math.round(((MySquare) shapes.get(i)).getWidth())) + "");
                        XAxisField.setText((Math.round(((MySquare) shapes.get(i)).getX())) + "");
                        YAxisField.setText((Math.round(((MySquare) shapes.get(i)).getY())) + "");

                    } else if (shapes.get(i) instanceof MyRectangle) {
                        circleRadiusField.setText("");
                        rectWidthField.setDisable(false);
                        rectHeightField.setDisable(false);
                        XAxisField.setDisable(false);
                        YAxisField.setDisable(false);
                        circleRadiusField.setDisable(true);
                        rectWidthField.setText((Math.round(((MyRectangle) shapes.get(i)).getWidth())) + "");
                        rectHeightField.setText((Math.round(((MyRectangle) shapes.get(i)).getHeight())) + "");
                        XAxisField.setText((Math.round(((MyRectangle) shapes.get(i)).getX())) + "");
                        YAxisField.setText((Math.round(((MyRectangle) shapes.get(i)).getY())) + "");
                    } else if (shapes.get(i) instanceof MyEllipse) {
                        circleRadiusField.setText("");
                        circleRadiusField.setDisable(true);
                        rectWidthField.setDisable(false);
                        rectHeightField.setDisable(false);
                        XAxisField.setDisable(false);
                        YAxisField.setDisable(false);
                        rectWidthField.setText((Math.round(((MyEllipse) shapes.get(i)).getRadiusX())) + "");
                        rectHeightField.setText((Math.round(((MyEllipse) shapes.get(i)).getRadiusY())) + "");
                        XAxisField.setText(Math.round((((MyEllipse) shapes.get(i)).getCenterX() * 100.0) / 100.0) + "");
                        YAxisField.setText(Math.round((((MyEllipse) shapes.get(i)).getCenterY() * 100.0) / 100.0) + "");
                    }
                }
            }

        });

        apply.setOnAction(e -> {

            for (int i = 0; i < shapes.size(); i++) {
                if (((SelectableNode) shapes.get(i)).MyIsPressed()) {
                    shapes.get(i).setFill(colorShapeFiller.getValue());
                    shapes.get(i).setStroke(colorLinePicker.getValue());
                    if (shapes.get(i) instanceof MyCircle) {

                        ((MyCircle) shapes.get(i)).setCenterX(Double.parseDouble(XAxisField.getText()));
                        ((MyCircle) shapes.get(i)).setCenterY(Double.parseDouble(YAxisField.getText()));
                        ((MyCircle) shapes.get(i)).setRadiusX(Double.parseDouble(circleRadiusField.getText()));
                        ((MyCircle) shapes.get(i)).setRadiusY(Double.parseDouble(circleRadiusField.getText()));

                        circleRadiusField
                                .setText(Math.round(((((MyCircle) shapes.get(i)).getRadiusX())) * 100.0) / 100.0 + "");
                        XAxisField.setText(Math.round((((MyCircle) shapes.get(i)).getCenterX() * 100.0) / 100.0) + "");
                        YAxisField.setText(Math.round((((MyCircle) shapes.get(i)).getCenterY() * 100.0) / 100.0) + "");

                    } else if (shapes.get(i) instanceof MySquare) {

                        ((MySquare) shapes.get(i)).setX(Double.parseDouble(XAxisField.getText()));
                        ((MySquare) shapes.get(i)).setY(Double.parseDouble(YAxisField.getText()));
                        ((MySquare) shapes.get(i)).setWidth(Double.parseDouble(rectWidthField.getText()));
                        ((MySquare) shapes.get(i)).setHeight(Double.parseDouble(rectWidthField.getText()));

                        rectWidthField.setText((Math.round(((MySquare) shapes.get(i)).getWidth())) + "");
                        XAxisField.setText((Math.round(((MySquare) shapes.get(i)).getX())) + "");
                        YAxisField.setText((Math.round(((MySquare) shapes.get(i)).getY())) + "");

                    } else if (shapes.get(i) instanceof MyRectangle) {

                        ((MyRectangle) shapes.get(i)).setX(Double.parseDouble(XAxisField.getText()));
                        ((MyRectangle) shapes.get(i)).setY(Double.parseDouble(YAxisField.getText()));
                        ((MyRectangle) shapes.get(i)).setWidth(Double.parseDouble(rectWidthField.getText()));
                        ((MyRectangle) shapes.get(i)).setHeight(Double.parseDouble(rectHeightField.getText()));

                        rectWidthField.setText((Math.round(((MyRectangle) shapes.get(i)).getWidth())) + "");
                        rectHeightField.setText((Math.round(((MyRectangle) shapes.get(i)).getHeight())) + "");
                        XAxisField.setText((Math.round(((MyRectangle) shapes.get(i)).getX())) + "");
                        YAxisField.setText((Math.round(((MyRectangle) shapes.get(i)).getY())) + "");

                    } else if (shapes.get(i) instanceof MyEllipse) {

                        ((MyEllipse) shapes.get(i)).setCenterX(Double.parseDouble(XAxisField.getText()));
                        ((MyEllipse) shapes.get(i)).setCenterY(Double.parseDouble(YAxisField.getText()));
                        ((MyEllipse) shapes.get(i)).setRadiusX(Double.parseDouble(rectWidthField.getText()));
                        ((MyEllipse) shapes.get(i)).setRadiusY(Double.parseDouble(rectHeightField.getText()));

                        rectWidthField.setText((Math.round(((MyEllipse) shapes.get(i)).getRadiusX())) + "");
                        rectHeightField.setText((Math.round(((MyEllipse) shapes.get(i)).getRadiusY())) + "");
                        XAxisField.setText(Math.round((((MyEllipse) shapes.get(i)).getCenterX() * 100.0) / 100.0) + "");
                        YAxisField.setText(Math.round((((MyEllipse) shapes.get(i)).getCenterY() * 100.0) / 100.0) + "");
                    }

                }
            }

        });

        Scene scene = new Scene(primaryRoot, windowX + 120, windowY + 80);
        primaryStage.setScene(scene);
        primaryStage.setTitle("MyPaintShop");
        primaryStage.show();
    }

}