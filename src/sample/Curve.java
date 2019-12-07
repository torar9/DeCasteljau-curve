package sample;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.math.BigDecimal;
import java.util.LinkedList;

public class Curve
{
    public static final int POINT_RADIUS = 5;
    public static final double MAXIMUM_PARAM_T = 1.0;
    public static final double STEP_T = 0.001;

    private boolean showTangents;
    private boolean showPoints;
    private boolean showBorder;
    private LinkedList<Point> pointList;
    private LinkedList<Point> tempPointList;
    private BigDecimal maxT;
    private Color mainTangentColor;//Barva usecky na ktere se nachazi bod krivky
    private Color curveColor;//Barva bodu křivky
    private Color borderColor;

    public Curve()
    {
        pointList = new LinkedList<>();
        tempPointList = new LinkedList<Point>();
        setDefaultShow();
        setDefaultColors();
        setMaxT(new BigDecimal(MAXIMUM_PARAM_T));
    }

    public LinkedList<Point> getPointList()
    {
        return pointList;
    }

    public void clearList()
    {
        pointList.clear();
    }

    public void addPoint(Point p)
    {
        pointList.addLast(p);
    }

    private void setDefaultShow()
    {
        enablePoints(true);
        showTangents(true);
        enableBorder(true);
    }

    public void setDefaultColors()
    {
        setCurveColor(Color.BLACK);
        setMainTangentColor(Color.CRIMSON);
        setBorderColor(Color.GRAY);
    }

    private Point pointLeap(Point a, Point b, BigDecimal t)
    {
        double x = t.doubleValue() * b.getX() + (1 - t.doubleValue()) * a.getX();
        double y = t.doubleValue() * b.getY() + (1 - t.doubleValue()) * a.getY();

        return new Point(x, y);
    }

    private void drawCurve(GraphicsContext gc)
    {
        LinkedList<Point> list = new LinkedList<>(pointList);
        Point temp;
        Point previousPoint = list.getFirst();
        BigDecimal t = new BigDecimal(0.0);

        do
        {
            tempPointList.clear();
            for(int i = list.size() - 1; i > 0; i--)
            {
                for(int j = i; j > 0; j--)
                {
                    temp = list.removeFirst();
                    Point newp = pointLeap(temp, list.getFirst(), t);
                    list.addLast(newp);
                    tempPointList.add(newp);
                }
                list.removeFirst();
            }

            temp = list.getFirst();
            BasicPainter.drawLine(previousPoint, temp, getCurveColor(), gc);
            previousPoint = temp;
            list = (LinkedList<Point>) pointList.clone();

            t = t.add(new BigDecimal(STEP_T).setScale(3, BigDecimal.ROUND_HALF_UP));
        }
        while(t.compareTo(maxT) == -1 || t.compareTo(maxT) == 0);

        if(showTangents)
        {
            for (Point p : tempPointList)
            {
                p.drawFullCircle(POINT_RADIUS, gc);
            }

            for(int i = pointList.size() - 2; i > 0; i--)
            {
                Point tmp = tempPointList.removeFirst();
                for(int j = i; j > 0; j--)
                {
                    BasicPainter.drawLine(tmp, tempPointList.getFirst(), mainTangentColor, gc);
                    tmp = tempPointList.removeFirst();
                }
            }
        }
    }

    private void drawBorder(GraphicsContext gc)
    {
        if(showBorder)
        {
            Point tmp = pointList.getFirst();
            for(int i = 0; i < pointList.size(); i++)
            {
                BasicPainter.drawLine(tmp, pointList.get(i), borderColor, gc);
                tmp = pointList.get(i);
            }
            BasicPainter.drawLine(tmp, pointList.getFirst(), borderColor, gc);
        }
    }

    public void draw(GraphicsContext gc)
    {
        if(pointList.isEmpty())
            return;

        if(showBorder)
            drawBorder(gc);

        drawCurve(gc);

        if(showPoints)
            drawPoints(gc);
    }

    private void drawPoints(GraphicsContext gc)
    {
        int i = 0;
        for(Point p : pointList)
        {
            String text = "P" + i++;
            p.setText(text);
            p.drawFullCircle(5, gc);
        }
    }

    //Gettery a Settery

    public BigDecimal getMaxT()
    {
        return maxT;
    }

    public void setMaxT(BigDecimal maxT)
    {
        this.maxT = maxT.setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    public void setMaxT(double maxT)
    {
        setMaxT(new BigDecimal(maxT));
    }

    public void enablePoints(boolean drawPoints)
    {
        this.showPoints = drawPoints;
    }

    public void showTangents(boolean drawSegments)
    {
        this.showTangents = drawSegments;
    }

    public Color getCurveColor()
    {
        return curveColor;
    }

    public void setCurveColor(Color curveColor)
    {
        this.curveColor = curveColor;
    }

    public void setMainTangentColor(Color mainTangentColor)
    {
        this.mainTangentColor = mainTangentColor;
    }

    public void setBorderColor(Color borderColor)
    {
        this.borderColor = borderColor;
    }

    public void enableBorder(boolean showBorder)
    {
        this.showBorder = showBorder;
    }
}