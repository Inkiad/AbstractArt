package com.micahraney.abstractsim;

import java.awt.image.BufferedImage;
import java.util.prefs.Preferences;

/**
 * AbtractImage is the model component of the system, providing a model for what abstract art is and generating
 * the abstract images per the defined algorighm.
 *
 * Created by MicahRaney on 7/18/2017.
 */
public class AbstractImage {    //algorithm properties

    private int iterations, variance, xstep, ystep;
    private volatile int curIters = 0;

    public AbstractImage(Preferences prefs) {
        //load the preferences for the generation algorithm off the provided preferences object.
        iterations = prefs.getInt("iterations", 10);
        variance = prefs.getInt("variance", 10);
        xstep = prefs.getInt("xstep", 10);
        ystep = prefs.getInt("ystep", 10);
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
     * Queues the art generation on the image. After each iteration, .notifyAll() will be called on this object
     * to notify any GUI listeners on the progress of the rendering.
     *
     * WARNING: Do NOT call on the GUI thread, will likely cause UI freezes!
     */
    public void generateArt() {


        for (curIters = 0; curIters < iterations; curIters++) {
            //TODO: implement art generation algorithm

            //notify all listeners of the rendering update.
            synchronized (this) {
                this.notifyAll();
            }
        }
        //TODO: implement this.
    }
}
