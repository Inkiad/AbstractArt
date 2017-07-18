package com.micahraney.abstractsim;

import javax.swing.*;
import java.util.prefs.Preferences;

/**
 * ArtPanel is a view controller for an AbstractImage object, offering convenience interation methods and the
 * ability to easily render an AbstractImage as a JPanel.
 * Created by MicahRaney on 7/18/2017.
 */
public class ArtPanel extends JPanel {

    private AbstractImage image;

    public ArtPanel(Preferences prefs) {
        image = new AbstractImage(prefs);
    }

    /**
     * Returns the AbstractImage that the ArtPanel renders
     * @return AbstractImage intance
     */
    public AbstractImage getImage(){
        return image;
    }

    /**
     * Queues the art generation on the image on a worker thread.
     * Thread-Safe.
     */
    public void generateArt() {
        //TODO: implement this.
    }
}
