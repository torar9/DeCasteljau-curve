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
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;

import java.math.BigDecimal;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable
{
    private Timeline timer;
    private Curve curve;
    private ActiveButtonMode buttonMode;
    private boolean animation;

    @FXML
    private Button animButton;
    @FXML
    private Label coordLabel;
    @FXML
    private Canvas canvas;
    @FXML
    private TextField axField;
    @FXML
    private TextField ayField;
    @FXML
    private TextField bxField;
    @FXML
    private TextField byField;
    @FXML
    private TextField cxField;
    @FXML
    private TextField cyField;
    @FXML
    private TextField dxField;
    @FXML
    private TextField dyField;
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
        buttonMode = ActiveButtonMode.NONE;
        curve = new Curve();
        animation = false;

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

        setTextFieldAsNumericOnly(axField);
        setTextFieldAsNumericOnly(ayField);
        setTextFieldAsNumericOnly(bxField);
        setTextFieldAsNumericOnly(byField);
        setTextFieldAsNumericOnly(cxField);
        setTextFieldAsNumericOnly(cyField);
        setTextFieldAsNumericOnly(dxField);
        setTextFieldAsNumericOnly(dyField);
        updateParamDisplay();
        repaint();
    }

    @FXML
    private void handleMouseDragEvent(MouseEvent event)
    {
        if(event.getX() <= canvas.getWidth() && event.getY() <= canvas.getHeight() && event.getX() >= 0 && event.getY() >= 0)
        {
            switch(buttonMode)
            {
                case NONE:
                    break;
                case PA_BUTTON:
                    axField.setText(Integer.toString((int)event.getX()));
                    ayField.setText(Integer.toString((int)event.getY()));
                    curve.getPointA().setPoint((int)event.getX(), (int)event.getY());
                    repaint();
                    break;
                case PB_BUTTON:
                    bxField.setText(Integer.toString((int)event.getX()));
                    byField.setText(Integer.toString((int)event.getY()));
                    curve.getPointB().setPoint((int)event.getX(), (int)event.getY());
                    repaint();
                    break;
                case PC_BUTTON:
                    cxField.setText(Integer.toString((int)event.getX()));
                    cyField.setText(Integer.toString((int)event.getY()));
                    curve.getPointC().setPoint((int)event.getX(), (int)event.getY());
                    repaint();
                    break;
                case PD_BUTTON:
                    dxField.setText(Integer.toString((int)event.getX()));
                    dyField.setText(Integer.toString((int)event.getY()));
                    curve.getPointD().setPoint((int)event.getX(), (int)event.getY());
                    repaint();
                    break;
            }
        }
    }

    @FXML
    private void handleMousePressEvent(MouseEvent event)
    {
        Point interest = new Point((int)event.getX(), (int)event.getY());

        if(curve.getPointA().isInsideRadius(interest, Curve.POINT_RADIUS))
            buttonMode = ActiveButtonMode.PA_BUTTON;
        if(curve.getPointB().isInsideRadius(interest, Curve.POINT_RADIUS))
            buttonMode = ActiveButtonMode.PB_BUTTON;
        if(curve.getPointC().isInsideRadius(interest, Curve.POINT_RADIUS))
            buttonMode = ActiveButtonMode.PC_BUTTON;
        if(curve.getPointD().isInsideRadius(interest, Curve.POINT_RADIUS))
            buttonMode = ActiveButtonMode.PD_BUTTON;
    }

    @FXML
    private void handleMouseReleaseEvent(MouseEvent event)
    {
        switch(buttonMode)
        {
            case NONE:
                break;
            case PA_BUTTON:
                axField.setText(Integer.toString((int)event.getX()));
                ayField.setText(Integer.toString((int)event.getY()));
                break;
            case PB_BUTTON:
                bxField.setText(Integer.toString((int)event.getX()));
                byField.setText(Integer.toString((int)event.getY()));
                break;
            case PC_BUTTON:
                cxField.setText(Integer.toString((int)event.getX()));
                cyField.setText(Integer.toString((int)event.getY()));
                break;
            case PD_BUTTON:
                dxField.setText(Integer.toString((int)event.getX()));
                dyField.setText(Integer.toString((int)event.getY()));
                break;
        }

        buttonMode = ActiveButtonMode.NONE;
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

    @FXML
    private void handlepaButtonClick()
    {
        buttonMode = ActiveButtonMode.PA_BUTTON;
    }

    @FXML
    private void handlepbButtonClick()
    {
        buttonMode = ActiveButtonMode.PB_BUTTON;
    }

    @FXML
    private void handlepcButtonClick()
    {
        buttonMode = ActiveButtonMode.PC_BUTTON;
    }

    @FXML
    private void handlepdButtonClick()
    {
        buttonMode = ActiveButtonMode.PD_BUTTON;
    }

    private void setTextFieldAsNumericOnly(TextField textField)
    {
        textField.textProperty().addListener(new ChangeListener<String>()
        {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue)
            {
                if (!newValue.matches("\\d*"))
                {
                    textField.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
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

        Point pa = new Point(Integer.parseInt(axField.getText()), Integer.parseInt(ayField.getText()));
        Point pb = new Point(Integer.parseInt(bxField.getText()), Integer.parseInt(byField.getText()));
        Point pc = new Point(Integer.parseInt(cxField.getText()), Integer.parseInt(cyField.getText()));
        Point pd = new Point(Integer.parseInt(dxField.getText()), Integer.parseInt(dyField.getText()));

        curve.setPoints(pa, pb, pc, pd);
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
    }
}
