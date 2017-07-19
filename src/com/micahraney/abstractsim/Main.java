package com.micahraney.abstractsim;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

public class Main {

    public static void main(String[] args) {
	// write your code here
        System.out.print("This is a test");

        //configure frame
        JFrame frame = new JFrame("Abstract Art Generator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        Map<String,Integer> prefs = new HashMap<>();
        prefs.put("iterations",10000);
        prefs.put("intricacy", 75);
        ArtPanel artPanel = new ArtPanel(prefs);
        frame.add(artPanel, BorderLayout.CENTER);

        JButton button = new JButton("Render");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                (new Thread(new RenderTask(artPanel, true, frame))).start();
            }
        });
        frame.add(button,BorderLayout.SOUTH);

        frame.pack();
        frame.setVisible(true);
    }
}
