package sample;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Point
{
    private double x;
    private double y;
    private Color color;

    public Point()
    {
        this(0, 0, Color.BLACK);
    }

    public Point(double x, double y)
    {
        this(x, y, Color.BLACK);
    }

    public Point(double x, double y, Color color)
    {
        setX(x);
        setY(y);
        setColor(color);
    }

    public void drawCircle(int r, GraphicsContext gc)
    {
        double x1, y1;

        for(double i = 0; i < 360; i += 2.0)
        {
            double angle = i;
            x1 = r * Math.cos(Math.toRadians(angle));
            y1 = r * Math.sin(Math.toRadians(angle));
            BasicPainter.putPixel(x + x1, y + y1, color, gc);
        }
    }

    public void drawFullCircle(int r, GraphicsContext gc)
    {
        for(int y = -r; y <= r; y++)
            for(int x = -r; x <= r; x++)
                if(x * x + y * y <= r * r)
                    BasicPainter.putPixel(this.x + x, this.y + y, color, gc);
    }

    public boolean isInsideRadius(Point point, int r)
    {
        double dx = Math.abs(x - point.getX());
        double dy = Math.abs(y - point.getY());

        if(dx > r)
            return false;
        if(dy > r)
            return false;

        return true;
    }

    public void setPoint(double x, double y)
    {
        setX(x);
        setY(y);
    }

    public Color getColor()
    {
        return color;
    }

    public void setColor(Color color)
    {
        this.color = color;
    }

    public double getX()
    {
        return x;
    }

    public void setX(double x)
    {
        this.x = x;
    }

    public double getY()
    {
        return y;
    }

    public void setY(double y)
    {
        this.y = y;
    }

    @Override
    public String toString()
    {
        return "Point{" +
                "x=" + x +
                ", y=" + y +
                ", color=" + color +
                '}';
    }
}
