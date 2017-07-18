package com.micahraney.abstractsim;

/**
 * WorkerThread works to render the image on a separate thread.
 *
 * Created by MicahRaney on 7/18/2017.
 */
public class WorkerThread extends Thread {

    //Image to edit/calculate things for
    private ArtPanel panel;

    /**
     * Initialize the WorkerThread to work on the given image.
     * @param panel ArtPanel to generate image on.
     */
    public WorkerThread(ArtPanel panel) {
        this.panel = panel;
    }

    public void run(){
        //generate the art for real.
        panel.getImage().generateArt();
    }
}
