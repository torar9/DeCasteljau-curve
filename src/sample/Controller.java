package sample;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.math.BigDecimal;
import java.net.URL;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ResourceBundle;

public class Controller implements Initializable
{
    private Timeline timer;
    private Curve curve;
    private Point temp;
    private boolean clickMode;
    private boolean animation;

    @FXML
    private Button animButton;
    @FXML
    private Label coordLabel;
    @FXML
    private Canvas canvas;
    @FXML
    private Slider paramTSlider;
    @FXML
    private CheckBox pointsCheckBox;
    @FXML
    private CheckBox segmentsCheckBox;
    @FXML
    private CheckBox borderCheckBox;
    @FXML
    private Label paramTLabel;

    public Controller()
    {
    }

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        curve = new Curve();
        animation = false;
        clickMode = false;

        timer = new Timeline(new KeyFrame(Duration.millis(50), new EventHandler<ActionEvent>()
        {
            boolean cycle;

            @Override
            public void handle(ActionEvent event)
            {
                BigDecimal paramT = new BigDecimal(paramTSlider.getValue());

                if((paramT.compareTo(new BigDecimal(1.0)) == -1) && cycle)
                    paramT = paramT.add(new BigDecimal(0.01)).setScale(2, BigDecimal.ROUND_HALF_UP);
                else
                    paramT = paramT.subtract(new BigDecimal(0.01)).setScale(2, BigDecimal.ROUND_HALF_UP);

                if(paramT.compareTo(new BigDecimal(1.0)) == 0)
                {
                    cycle = false;
                }
                if(paramT.compareTo(new BigDecimal(0.0)) == -1)
                {
                    cycle = true;
                }

                paramTSlider.setValue(paramT.doubleValue());
                updateParamDisplay();
                repaint();
            }
        }));
        timer.setCycleCount(Timeline.INDEFINITE);
        updateParamDisplay();

        setDefaultPoints();

        repaint();
    }

    private void setDefaultPoints()
    {
        curve.addPoint(new Point(100, 400));
        curve.addPoint(new Point(100, 100));
        curve.addPoint(new Point(700, 100));
        curve.addPoint(new Point(700, 400));
    }

    @FXML
    private void handleMouseDragEvent(MouseEvent event)
    {
        if(clickMode)
        {
            if(event.getX() <= canvas.getWidth() && event.getY() <= canvas.getHeight() && event.getX() >= 0 && event.getY() >= 0)
            {
                temp.setX(event.getX());
                temp.setY(event.getY());
                repaint();
            }
        }
    }

    @FXML
    private void handleMousePressEvent(MouseEvent event)
    {
        if(MouseButton.PRIMARY == event.getButton())
        {
            for(Point e : curve.getPointList())
            {
                if(e.isInsideRadius(new Point(event.getX(), event.getY()),curve.POINT_RADIUS))
                {
                    temp = e;
                    clickMode = true;
                    break;
                }
            }
        }
        else
        {
            curve.getPointList().removeIf(
                    p -> (p.isInsideRadius(new Point(event.getX(), event.getY()), curve.POINT_RADIUS))
            );
        }
    }

    @FXML
    private void handleMouseReleaseEvent(MouseEvent event)
    {
        if(MouseButton.PRIMARY == event.getButton())
        {
            if(!clickMode)
            {
                curve.addPoint(new Point(event.getX(), event.getY()));
            }
            clickMode = false;
        }
        repaint();
    }

    @FXML
    private void handleAnimateButtonClick()
    {
        if(animation)
        {
            animButton.setText("Animate");
            timer.stop();
            animation = false;
        }
        else
        {
            animButton.setText("Stop");
            timer.play();
            animation = true;
        }
    }

    @FXML
    private void handleSliderClickEvent()
    {
        updateParamDisplay();
        repaint();
    }

    @FXML
    private void handleSliderDragEvent()
    {
        updateParamDisplay();
        repaint();
    }

    @FXML
    private void handleMouseMovementEvent(MouseEvent event)
    {
        coordLabel.setText("x: " + event.getX() + " y: " + event.getY());
    }

    @FXML
    private void handleDrawButtonClick()
    {
        repaint();
    }


    private void clearCanvas()
    {
        canvas.getGraphicsContext2D().clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }

    private void updateParamDisplay()
    {
        double t = paramTSlider.getValue();
        paramTLabel.setText("t: " + (Math.round(t * 100.0) / 100.0));//Round to x.xx
    }

    private void repaint()
    {
        double t = paramTSlider.getValue();

        clearCanvas();

        curve.setMaxT(t);
        curve.draw(canvas.getGraphicsContext2D());
    }

    @FXML
    private void handlePointsCheckBoxAction(Event event)
    {
        curve.enablePoints(pointsCheckBox.isSelected());
        repaint();
    }

    @FXML
    private void handleSegmentsCheckBoxAction(Event event)
    {
        curve.showTangents(segmentsCheckBox.isSelected());
        repaint();
    }

    @FXML
    private void handleBorderCheckBoxAction()
    {
        curve.enableBorder(borderCheckBox.isSelected());
        repaint();
    }

    @FXML
    private void handleClearButtonClick()
    {
        clearCanvas();
        curve.clearList();
    }
}
