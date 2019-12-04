package sample;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.math.BigDecimal;

public class Curve extends BasicPainter
{
    public static final int POINT_RADIUS = 5;
    public static final double MAXIMUM_PARAM_T = 1.0;

    private boolean showTangents;
    private boolean showPoints;
    private boolean showBorder;
    private Point pointA;
    private Point pointB;
    private Point pointC;
    private Point pointD;
    private BigDecimal maxT;
    private Color sideTangentColor;//Barva okolnich usecek
    private Color mainTangentColor;//Barva usecky na ktere se nachazi bod krivky
    private Color curveColor;//Barva bodu křivky
    private Color borderColor;

    public Curve()
    {
        setDefaultShow();
        setDefaultColors();
        setMaxT(new BigDecimal(MAXIMUM_PARAM_T));
    }

    private void setDefaultShow()
    {
        enablePoints(true);
        showTangents(true);
        enableBorder(true);
    }

    public void setDefaultColors()
    {
        setSideTangentColor(Color.CYAN);
        setCurveColor(Color.BLACK);
        setMainTangentColor(Color.CRIMSON);
        setBorderColor(Color.LIGHTGRAY);
    }

    private Point pointLeap(Point a, Point b, BigDecimal t)
    {
        /*
        double x = a.getX() + (b.getX() - a.getX()) * t.doubleValue();
        double y = a.getY() + (b.getY() - a.getY()) * t.doubleValue();*/

        double x = t.doubleValue() * b.getX() + (1 - t.doubleValue()) * a.getX();
        double y = t.doubleValue() * b.getY() + (1 - t.doubleValue()) * a.getY();

        return new Point(x, y);
    }

    private void drawCurve(GraphicsContext gc)
    {
        Point previousPoint, ab, bc, cd, abbc, bccd, result;
        BigDecimal t = new BigDecimal(0.0);
        previousPoint = pointA;

        do
        {
            ab = pointLeap(pointA, pointB, t);
            bc = pointLeap(pointB, pointC, t);
            cd = pointLeap(pointC, pointD, t);
            abbc = pointLeap(ab, bc, t);
            bccd = pointLeap(bc, cd, t);
            result = pointLeap(abbc, bccd, t);

            drawLine(previousPoint, result, curveColor, gc);

            previousPoint = result;
            t = t.add(new BigDecimal(0.001).setScale(3, BigDecimal.ROUND_HALF_UP));
        }
        while(t.compareTo(maxT) == -1 || t.compareTo(maxT) == 0);

        if(showTangents)
        {
            drawLine(ab, bc, sideTangentColor, gc);
            drawLine(bc, cd, sideTangentColor, gc);
            drawLine(abbc, bccd, mainTangentColor, gc);
        }

        if(showPoints)
        {
            ab.drawFullCircle(POINT_RADIUS, gc);
            bc.drawFullCircle(POINT_RADIUS, gc);
            cd.drawFullCircle(POINT_RADIUS, gc);
            abbc.drawFullCircle(POINT_RADIUS, gc);
            bccd.drawFullCircle(POINT_RADIUS, gc);
            result.drawFullCircle(POINT_RADIUS, gc);
        }
    }

    private void drawBorder(GraphicsContext gc)
    {
        drawLine(pointA, pointB, borderColor, gc);
        drawLine(pointB, pointC, borderColor, gc);
        drawLine(pointC, pointD, borderColor, gc);
        drawLine(pointD, pointA, borderColor, gc);
    }

    public void draw(GraphicsContext gc)
    {
        if(showBorder)
            drawBorder(gc);
        
        drawPoints(gc);
        drawCurve(gc);
    }

    private void drawPoints(GraphicsContext gc)
    {
        pointA.drawFullCircle(POINT_RADIUS, gc);
        pointB.drawFullCircle(POINT_RADIUS, gc);
        pointC.drawFullCircle(POINT_RADIUS, gc);
        pointD.drawFullCircle(POINT_RADIUS, gc);
    }

    //Gettery a Settery

    public void setPoints(Point pA, Point pB, Point pC, Point pD)
    {
        setPointA(pA);
        setPointB(pB);
        setPointC(pC);
        setPointD(pD);
    }

    public Point getPointA()
    {
        return pointA;
    }

    public void setPointA(Point pointA)
    {
        this.pointA = pointA;
    }

    public Point getPointB()
    {
        return pointB;
    }

    public void setPointB(Point pointB)
    {
        this.pointB = pointB;
    }

    public Point getPointC()
    {
        return pointC;
    }

    public void setPointC(Point pointC)
    {
        this.pointC = pointC;
    }

    public Point getPointD()
    {
        return pointD;
    }

    public void setPointD(Point pointD)
    {
        this.pointD = pointD;
    }

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

    public Color getSideTangentColor()
    {
        return sideTangentColor;
    }

    public void setSideTangentColor(Color sideTangentColor)
    {
        this.sideTangentColor = sideTangentColor;
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

    public Color getMainTangentColor()
    {
        return mainTangentColor;
    }

    public void setMainTangentColor(Color mainTangentColor)
    {
        this.mainTangentColor = mainTangentColor;
    }

    public Color getBorderColor()
    {
        return borderColor;
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