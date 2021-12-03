package com.company;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Line2D;
import java.util.Random;
import javax.swing.JPanel;

public class CatsCradlePanel extends JPanel implements ActionListener {

    // Specify the color of the background on which the figures
    // will be drawn.
    private static final Color BG_COLOR = new Color(169, 169, 169);
    private static final Color FG_COLOR = new Color(180, 192, 224);
    // MARGIN gives some separation between the figures drawn
    // and the edge of the panel in which they are drawn.
    // The value must be at least zero and less than 0.5.
    private static final double MARGIN = 0.1;
    // Bigger values of SPEED result in a slower animation.
    // Smaller values of SPEED result in a faster animation.
    private static final double SPEED = 64.0;
    // Specify the thickness of the line segments used
    // to draw the inside and outside figures.
    private static final float OUTSIDE_LINE_THICKNESS = 1;
    private static final float INSIDE_LINE_THICKNESS = 1;
    private int numberOfSides;
    private double outerStep;
    private double innerStep;
    private double outerAngle;
    private double innerAngle;
    private double angle;
    private Color[] colors;
    private Stroke insideStroke;
    private Stroke outsideStroke;

    public CatsCradlePanel(int numberOfSides) {
        this.setBackground(BG_COLOR);
        this.setForeground(FG_COLOR);
        this.numberOfSides = numberOfSides;
        this.outerStep = -2.0 * Math.PI / (SPEED * numberOfSides);
        this.innerStep = +2.0 * Math.PI / (2.0 * SPEED * numberOfSides);
        this.outerAngle = 0.0;
        this.innerAngle = 0.0;
        this.insideStroke = new BasicStroke(INSIDE_LINE_THICKNESS,
                BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
        this.outsideStroke = new BasicStroke(OUTSIDE_LINE_THICKNESS,
                BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);

        // Make a palette of random numbers.
        this.colors = new Color[numberOfSides];

        Random random = new Random();
        for (int i = 0; i < numberOfSides; i++) {
            // In the following 3 statements, instead
            // of 64 and 192 you may use any pair of
            // non-negative integers whose sum is 256
            // (or less than 256 if you wish).
            int red = 64 + random.nextInt(69);
            int green = 64 + random.nextInt(69);
            int blue = 64 + random.nextInt(69);
            this.colors[i] = new Color(red, green, blue);
        } // for
    } // CatsCradlePanel()

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2D = (Graphics2D) g;

        // Determine the dimensions of the panel
        // in which we are drawing the figure.
        int w = this.getWidth();
        int h = this.getHeight();

        // Make 2 sets of vertices.
        // The outside vertices lie on a circle
        // whose radius is 1.0 and whose center
        // is at the origin.
        // The inside vertices lie on a circle
        // whose radius is the golden ratio and whose center
        // is also at the origin.
        Vector2D[] outside = new Vector2D[this.numberOfSides];
        Vector2D[] inside = new Vector2D[this.numberOfSides];
        double goldenRatio = 2.0 / (Math.sqrt(5.0) + 1);
        for (int i = 0; i < this.numberOfSides; i++) {
            double fraction = ((double) i) / this.numberOfSides;
            this.angle = fraction * 2.0 * Math.PI;
            double x = Math.cos(angle + outerAngle);
            double y = Math.sin(angle + outerAngle);
            outside[i] = new Vector2D(x, y);

            x = Math.cos(angle + innerAngle);
            y = Math.sin(angle + innerAngle);
            inside[i] = new Vector2D(goldenRatio * x, goldenRatio * y);
        } // for

        // Make the 2 polygons that are defined by the convex hulls of
        // of the outside and inside sets of points (respectively).
        // If there are 3 points in a set, we get an equilateral triangle.
        // If there are 4 points, we get a square.
        // If there are more points, we get a pentagon, hexagon, or n-gon.

        // No rotation in this step---we'll take care of that elsewhere.
        double rotation = 0.0;
        // This is how much bigger we have to make
        // the figures to fill the panel (and still
        // leave a margin between the figures and the
        // edge of the panel).
        double scaleX = (1.0 - 2.0 * MARGIN) * w / 2;
        double scaleY = (1.0 - 2.0 * MARGIN) * h / 2;
        // This is how far we have to move the figures
        // to put their centers at the center of the panel.
        double deltaX = w / 2;
        double deltaY = h / 2;

        g2D.setColor(FG_COLOR);
        for (int i = 0; i < outside.length; i++) {
            Vector2D u = outside[i];
            u = u.rotateScaleTranslate(rotation, scaleX, scaleY, deltaX, deltaY);

            Vector2D v = outside[(i + 1) % this.numberOfSides];
            v = v.rotateScaleTranslate(rotation, scaleX, scaleY, deltaX, deltaY);

            double x0 = u.getX();
            double y0 = u.getY();
            double x1 = v.getX();
            double y1 = v.getY();
            Line2D line = new Line2D.Double(x0, y0, x1, y1);
            g2D.setStroke(this.outsideStroke);
            g2D.draw(line);

            u = inside[i];
            u = u.rotateScaleTranslate(rotation, scaleX, scaleY, deltaX, deltaY);

            v = inside[(i + 1) % this.numberOfSides];
            v = v.rotateScaleTranslate(rotation, scaleX, scaleY, deltaX, deltaY);

            x0 = u.getX();
            y0 = u.getY();
            x1 = v.getX();
            y1 = v.getY();

            line = new Line2D.Double(x0, y0, x1, y1);
            g2D.setStroke(this.insideStroke);
            g2D.draw(line);
        } // for

        // Make the remaining edges (line segments) needed to define
        // the complete graph on the outside set of points.
        g2D.setStroke(this.outsideStroke);
        for (int i = 0; i < this.numberOfSides; i++) {
            Vector2D u = outside[i];
            u = u.rotateScaleTranslate(angle, scaleX, scaleY, deltaX, deltaY);
            double x0 = u.getX();
            double y0 = u.getY();
            for (int j = i + 1; j < this.numberOfSides; j++) {
                Vector2D v = outside[j];
                v = v.rotateScaleTranslate(rotation, scaleX, scaleY, deltaX, deltaY);
                double x1 = v.getX();
                double y1 = v.getY();

                // Make color a function of the line segment's length.
                // (The segment is longer if it connects vertices whose
                // indices differ more.)
                int index = Math.abs(i - j);
                g2D.setColor(this.colors[index]);

                Line2D line = new Line2D.Double(x0, y0, x1, y1);
                g2D.draw(line);
            } // for
        } // for

        // Make the remaining edges (line segments) needed to define
        // the complete graph on the inside set of points.
        g2D.setStroke(this.insideStroke);
        for (int i = 0; i < this.numberOfSides; i++) {
            Vector2D u = inside[i];
            u = u.rotateScaleTranslate(angle, scaleX, scaleY, deltaX, deltaY);
            double x0 = u.getX();
            double y0 = u.getY();
            for (int j = i + 1; j < this.numberOfSides; j++) {
                Vector2D v = inside[j];
                v = v.rotateScaleTranslate(rotation, scaleX, scaleY, deltaX, deltaY);
                double x1 = v.getX();
                double y1 = v.getY();

                // Make color a function of the line segment's length.
                // (The segment is longer if it connects vertices whose
                // indices differ more.)
                int index = Math.abs(i - j);
                g2D.setColor(this.colors[index]);

                Line2D line = new Line2D.Double(x0, y0, x1, y1);
                g2D.draw(line);
            } // for
        } // for
    } // paintComponent( Graphics )

    @Override
    public void actionPerformed(ActionEvent e) {
        this.outerAngle += this.outerStep;
        this.innerAngle += this.innerStep;
        this.repaint();
    } // actionPerformed( ActionEvent )
} // CatsCradlePanel