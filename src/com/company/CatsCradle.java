package com.company;

import java.awt.Container;
import javax.swing.JFrame;
import javax.swing.Timer;

/**+
 * Here is a final exercise.
 *
 * <p> This example features animated graphics and a class that models a kind of
 * mathematical object that has two parts (a vector in two dimensions). </p>
 *
 * @author CSC140 Foundations of Computer Science
 * @version 6 March 2013
 */
public class CatsCradle extends JFrame {
    /**
     * Choose values for these parameters that make a pleasing image---experiment!
     */
    private static final int FRAME_WIDTH = 512;
    private static final int FRAME_HEIGHT = 512;
    private static final String FRAME_TITLE = "Cat's Cradle";
    private static final int NUMBER_OF_SIDES = 40;

    /**
     * Create a window on the screen, put a panel in the
     * frame on which to draw a picture, and create a timer
     * that will send periodic signals to the panel that ask it to
     * redraw itself.
     */
    public CatsCradle() {
        this.setSize(FRAME_WIDTH, FRAME_HEIGHT);
        this.setTitle(FRAME_TITLE);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // A JFrame contains a pane to hold panels, buttons,
        // scrollbars, and anything else we want to put into
        // our JFrame.
        Container pane = this.getContentPane();
        CatsCradlePanel panel = new CatsCradlePanel(NUMBER_OF_SIDES);
        pane.add(panel);

        // The "20" here means 20 milliseconds.
        // That means a new image 50 times per second.
        // You can try something different.
        // (Smaller values will result in more images
        // per second: 10 milliseconds between images
        // equals 100 images/second.)
        Timer timer = new Timer(20, panel);
        timer.start();

        this.setVisible(true);
    } // CatsCradle( int, int )

    /**
     * The execution of this program begins here.
     *
     * @param args is a required array that could hold command line
     * arguments (but we will not use any command
     * line arguments in this example).
     */
    public static void main(String[] args) {
        CatsCradle catsCradle = new CatsCradle();
    } // main( String [] )
} // CatsCradle