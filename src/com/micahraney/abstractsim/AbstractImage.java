package com.micahraney.abstractsim;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Map;
import java.util.Random;
import java.util.prefs.Preferences;

/**
 * AbtractImage is the model component of the system, providing a model for what abstract art is and generating
 * the abstract images per the defined algorighm.
 * <p>
 * Created by MicahRaney on 7/18/2017.
 */
public class AbstractImage {    //algorithm properties

    private int iterations = 100, intricacy = 3, cvariance = 10;
    private long seed;
    private volatile int curIters = 0;
    private volatile boolean isRendered = false;
    private BufferedImage image;

    public AbstractImage(Map<String, Integer> prefs, long seed) {
        //load the preferences for the generation algorithm off the provided preferences object.
        loadPrefereces(prefs);
        this.seed = seed;
        //TODO: Add robust preference handling that can take an incomplete pref set using defaults.


        image = new BufferedImage(1200, 800, BufferedImage.TYPE_INT_ARGB);
    }

    public void setSeed(long seed){
        this.seed = seed;
    }

    public long getSeed(){
        return seed;
    }

    public void loadPrefereces(Map<String, Integer> prefs) {
        iterations = prefs.get("iterations");
        intricacy = prefs.get("intricacy");
        cvariance = prefs.get("cvariance");
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

        Random rand = new Random(seed);

        //the image is no longer fully rendered.
        isRendered = false;

        //get image attributess and Graphics instance.
        Graphics gr = image.getGraphics();
        int x = image.getWidth(), y = image.getHeight();

        //clear the render
        gr.setColor(Color.WHITE);
        gr.fillRect(0, 0, x, y);

        //set the base color
        int rbase = (int) (256 * rand.nextDouble()),
                gbase = (int) (256 * rand.nextDouble()),
                bbase = (int) (256 * rand.nextDouble());

        //initialize the color variables
        int r, g, b, a;

        //find the color deltas
        double rdelta = (rbase - (256 * rand.nextDouble())) / iterations,
                gdelta = (gbase - (256 * rand.nextDouble())) / iterations,
                bdelta = (bbase - (256 * rand.nextDouble())) / iterations;

        //initialize the memory space for polygon point selection,
        int[] xpts = new int[intricacy * iterations], ypts = new int[intricacy * iterations];

        System.out.println("Rendering...");
        for (curIters = 0; curIters < iterations; curIters++) {
            //TODO: implement art generation algorithm

            //generate a list of points of a random size within the memory space
//            int tr = (int)(rand.nextDouble()*intricacy);
            int tr = (iterations - curIters) * intricacy;
            for (int i = 0; i < tr; i++) {
                xpts[i] = (int) (rand.nextDouble() * x);
                ypts[i] = (int) (rand.nextDouble() * y);
            }

            //generate random color from the base color, through the variance defined
            r = (int) (rbase + rdelta * curIters + (rand.nextDouble() > 0.5 ? 1 : -1) * cvariance * rand.nextDouble());
            g = (int) (gbase + gdelta * curIters + (rand.nextDouble() > 0.5 ? 1 : -1) * cvariance * rand.nextDouble());
            b = (int) (bbase + bdelta * curIters + (rand.nextDouble() > 0.5 ? 1 : -1) * cvariance * rand.nextDouble());
            a = (int) (256 * rand.nextDouble());

            //check that the color is in the range [0,255]
            r = boundCheck(r);
            g = boundCheck(g);
            b = boundCheck(b);

            //
            gr.setColor(new Color(r, g, b, a));

            gr.fillPolygon(xpts, ypts, tr);

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

    private int boundCheck(int color) {
        if (color > 255)
            return 255;
        else if (color < 0)
            return 0;
        return color;
    }

    /**
     * Clears the image and sets the reisisRendered flag to false
     */
    public void clearImage() {
        isRendered = false;
        //TODO: implement actual clearing
    }
}
