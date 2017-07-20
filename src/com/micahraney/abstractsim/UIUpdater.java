package com.micahraney.abstractsim;

import javax.swing.*;
import java.awt.*;

public class UIUpdater extends Thread {
    private Component parent;
    private AbstractImage artImage;

    public UIUpdater(AbstractImage image, Component parent) {
        artImage = image;
        this.parent = parent;
    }

    public void run() {
        ProgressMonitor progressMonitor = new ProgressMonitor(parent,
                "Rendering Art...", "", 0, artImage.getIterationCount());

        try {
            while (!artImage.isRendered()) {
                synchronized (artImage) {
                    artImage.wait();
                }
                progressMonitor.setProgress(artImage.getIterationProgress());
            }
        } catch (InterruptedException ex) {
            ex.printStackTrace();
            System.exit(1);
        } finally {
            progressMonitor.close();
        }

    }
}