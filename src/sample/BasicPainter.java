package sample;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.PixelWriter;
import javafx.scene.paint.Color;

public class BasicPainter
{

    public static void putPixel(int x, int y, Color color, GraphicsContext gc)
    {
        PixelWriter pxw = gc.getPixelWriter();
        pxw.setColor(x, y, color);
    }

    public static void putPixel(double x, double y, Color color, GraphicsContext gc)
    {
        PixelWriter pxw = gc.getPixelWriter();
        pxw.setColor((int)x, (int)y, color);
    }

    public static void drawLine(Point pa, Point pb, Color color, GraphicsContext gc)
    {
        drawLine((int)pa.getX(), (int)pa.getY(), (int)pb.getX(), (int)pb.getY(), color, gc);
    }

    public static void drawLine(int xz, int yz, int xk, int yk, Color color, GraphicsContext gc)
    {
        if ((xz == xk) && (yz == yk))
        {
            putPixel(xz, yz, color, gc);

        }
        else
        {
            int dx = Math.abs(xk - xz);
            int dy = Math.abs(yk - yz);
            int diff = dx - dy;

            int posun_x, posun_y;

            if (xz < xk)
                posun_x = 1;
            else posun_x = -1;

            if (yz < yk)
                posun_y = 1;
            else posun_y = -1;

            while ((xz != xk) || (yz != yk))
            {

                int p = 2 * diff;

                if (p > -dy)
                {
                    diff -= dy;
                    xz += posun_x;
                }
                if (p < dx)
                {
                    diff += dx;
                    yz += posun_y;
                }
                putPixel(xz, yz, color, gc);
            }
        }
    }
    /*
    public static void drawLine(int xz, int yz, int xk, int yk, Color color, GraphicsContext gc)
    {
        int dx = xk - xz;
        int dy = yk - yz;

        if(dy == 0)
        {
            horizontalLine(xz, yz, xk, yk, color, gc);
            return;
        }
        if(dx == 0)
        {
            verticalLine(xz, yz, xk, yk, color, gc);
            return;
        }

        double a = (double)dy / dx;//Smernice usecky

        if(Math.abs(a) <= 1)
        {
            if(xk < xz)//Zjistim zda jsou prohozene body
            {
                int tmp = xz;
                xz = xk;
                xk = tmp;

                tmp = yz;
                yz = yk;
                yk = tmp;
            }

            double y = yz;
            for(int x = xz; x <= xk; x++)
            {
                putPixel(x, (int)y, color, gc);
                y += a;
            }
            return;
        }

        if(Math.abs(a) > 1)
        {
            if(yk < yz)//Zjistim zda jsou prohozene body
            {
                int tmp = xz;
                xz = xk;
                xk = tmp;

                tmp = yz;
                yz = yk;
                yk = tmp;

                dx = xk - xz;//Prepocitam smernici usecky
                dy = yk - yz;
            }
            a = (double)dx / dy;

            double x = xz;
            for(int y = yz; y <= yk; y++)
            {
                putPixel((int)x, y, color, gc);
                x += a;
            }
            return;
        }
    }*/

    public static void putPixel(Point p, Color color, GraphicsContext gc)
    {
        putPixel(p.getX(), p.getY(), color, gc);
    }

    public static void horizontalLine(int xz, int yz, int xk, int yk, Color color, GraphicsContext gc)
    {
        if(xk < xz)
        {
            int tmp = xz;
            xz = xk;
            xk = tmp;

            tmp = yz;
            yz = yk;
            yk = tmp;
        }

        for(int i = xz; i <= xk; i++)
        {
            putPixel(i, yz, color, gc);
        }
    }

    public static void verticalLine(int xz, int yz, int xk, int yk, Color color, GraphicsContext gc)
    {
        if(yk < yz)
        {
            int tmp = xz;
            xz = xk;
            xk = tmp;

            tmp = yz;
            yz = yk;
            yk = tmp;
        }

        for(int i = yz; i <= yk; i++)
        {
            putPixel(xz, i, color, gc);
        }
    }
}
