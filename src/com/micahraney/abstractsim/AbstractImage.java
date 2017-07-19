package com.micahraney.abstractsim;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Map;
import java.util.prefs.Preferences;

/**
 * AbtractImage is the model component of the system, providing a model for what abstract art is and generating
 * the abstract images per the defined algorighm.
 * <p>
 * Created by MicahRaney on 7/18/2017.
 */
public class AbstractImage {    //algorithm properties

    private int iterations, intricacy;
    private volatile int curIters = 0;
    private volatile boolean isRendered = false;
    private BufferedImage image;

    public AbstractImage(Map<String, Integer> prefs) {
        //load the preferences for the generation algorithm off the provided preferences object.
        iterations = prefs.get("iterations");
        intricacy = prefs.get("intricacy");
        //TODO: Add robust preference handling that can take an incomplete pref set using defaults.


        image = new BufferedImage(800, 600, BufferedImage.TYPE_INT_ARGB);
    }

    public int getIterationCount() {
        return iterations;
    }

    /**
     * returns the number of iterations that the image generation has undergone. If this equals the total number
     * of iterations (found by getIterationCount()), then the image is fully rendered. Else the image is still
     * rendering.
     *
     * @return number of iterations the rendering has undergone.
     */
    public int getIterationProgress() {
        return curIters;
    }

    /**
     * The state of the render. True if the render process is completed, false if not. Useful for methods that
     * keep up on the render status/progress bars that need to go away when the render is complete.
     *
     * @return render status. True if rendered. False if not.
     */
    public boolean isRendered() {
        return isRendered;
    }

    public Image getRender() {
        return image;
    }

    /**
     * Queues the art generation on the image. After each iteration, .notifyAll() will be called on this object
     * to notify any GUI listeners on the progress of the rendering.
     * <p>
     * WARNING: Do NOT call on the GUI thread, will likely cause UI freezes!
     */
    public void generateArt() {

        //the image is no longer fully rendered.
        isRendered = false;

        //get image attributess and Graphics instance.
        Graphics g = image.getGraphics();
        int x = image.getWidth(), y = image.getHeight();

        //clear the render
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, x, y);

        //initialize the memory space for these randomizations.
        int[] xpts = new int[intricacy], ypts = new int[intricacy];

        System.out.println("Rendering...");
        for (curIters = 0; curIters < iterations; curIters++) {
            //TODO: implement art generation algorithm

            //generate a list of points of a random size within the memory space
            int tr = (int)(Math.random()*intricacy);
            for(int i = 0; i < tr; i++){
                xpts[i] = (int)(Math.random()*x);
                ypts[i] = (int)(Math.random()*y);
            }

            //generate random color
            g.setColor(new Color((float) Math.random(),
                    (float) Math.random(),
                    (float) Math.random(),
                    (float) Math.random()));

            g.fillPolygon(xpts, ypts, tr);

            //notify all listeners of the rendering update.
            synchronized (this) {
                this.notifyAll();
            }
        }
        System.out.println("Done!");

        //the render is done!
        isRendered = true;
        //notify all listeners that the render is complete.
        synchronized (this) {
            this.notifyAll();
        }
        //TODO: implement this.
    }

    /**
     * Clears the image and sets the reisisRendered flag to false
     */
    public void clearImage() {
        isRendered = false;
        //TODO: implement actual clearing
    }
}
