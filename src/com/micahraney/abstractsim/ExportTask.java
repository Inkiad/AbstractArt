package com.micahraney.abstractsim;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * RenderTask works to render the image on a separate thread.
 * <p>
 * Created by MicahRaney on 7/18/2017.
 */
public class ExportTask implements Runnable {

    //Image to edit/calculate things for
    private ArtPanel panel;
    private int w, h;
    private Component parent;
    private boolean runUI;

    /**
     * Initialize the RenderTask to work on the given image. May also run a UI ProgressBar dialog if runUI is true.
     *
     * @param panel    ArtPanel to generate image on.
     * @param runUI    if true, a UI dialog progress bar will be shown.
     * @param uiParent the parent of the UI dialog. may be null
     */
    public ExportTask(ArtPanel panel, int width, int height, boolean runUI, Component uiParent) {
        this.panel = panel;
        w = width;
        h = height;
        parent = uiParent;
    }

    /**
     * Initialize the RenderTask to work on the given image.
     *
     * @param panel ArtPanel to generate image on.
     */
    public ExportTask(ArtPanel panel, int width, int height) {
        this(panel, width, height, false, null);
    }

    public void run() {

        //start the UI for the thing.
        UIUpdater uiu = new UIUpdater(panel.getImage(), parent);
        uiu.start();

        BufferedImage image = new BufferedImage(w,h, BufferedImage.TYPE_INT_ARGB);


        //generate the art for real.
        panel.getImage().generateArt();
        panel.repaint();
    }

}
