package com.micahraney.abstractsim;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.prefs.Preferences;

/**
 * ArtPanel is a view controller for an AbstractImage object, offering convenience interation methods and the
 * ability to easily render an AbstractImage as a JPanel.
 * Created by MicahRaney on 7/18/2017.
 */
public class ArtPanel extends JPanel {

    private AbstractImage image;

    public ArtPanel(Map<String, Integer> prefs) {
        image = new AbstractImage(prefs);
    }

    /**
     * Returns the AbstractImage that the ArtPanel renders
     *
     * @return AbstractImage intance
     */
    public AbstractImage getImage() {
        return image;
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        //find the properly scaled fit.
        Image img = image.getRender();

        //define source and destination parameters.
        int sy = img.getHeight(null),
                sx = img.getWidth(null),
                dy = getHeight(),
                dx = getWidth();
        //if horizontal scaling is larger than vertical scaling, render using the width as the limiting factor

        if ((float) dx / sx < (float) dy / sy) {

            //calculate the scaling as a float
            float scale = (float)dx / sx;

            //find the height of the rendered image
            int effY = (int) (sy * scale);

            //calculate how far down to start the image render
            int borderY = (dy - effY) / 2;

            System.out.println("Rendering with width constraint... " +
                    "\n\tsx: " + sx +
                    "\n\tsy: " + sy +
                    "\n\tdx: " + dx +
                    "\n\tdy: " + dy +
                    "\n\tscale: " + scale +
                    "\n\teffY: " + effY +
                    "\n\tborderY: " + borderY);

            g.drawImage(img, 0, borderY, dx, dy - borderY, 0, 0, sx, sy, null);

        }
        //else render using the height as the limiting factor
        else {


            //calculate the scaling as a float
            float scale = (float)dy / sy;

            //find the height of the rendered image
            int effX = (int) (sx * scale);

            //calculate how far down to start the image render
            int borderX = (dx - effX) / 2;

            System.out.println("Rendering with height constraint... " +
                    "\n\tsx: " + sx +
                    "\n\tsy: " + sy +
                    "\n\tdx: " + dx +
                    "\n\tdy: " + dy +
                    "\n\tscale: " + scale +
                    "\n\teffX: " + effX +
                    "\n\tborderX: " + borderX);

            g.drawImage(img, borderX, 0, dx - borderX, dy, 0, 0, sx, sy, null);


        }
        System.out.println("Render complete?!");
    }

    public Dimension getPreferredSize(){
        Image img = getImage().getRender();
        return new Dimension(img.getWidth(null), img.getHeight(null));
    }

//    /**
//     * Queues the art generation on the image on a worker thread.
//     * Thread-Safe.
//     */
//    public void generateArt() {
//        //TODO: implement this.
//    }
}
