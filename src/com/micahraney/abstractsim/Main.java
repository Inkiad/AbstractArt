package com.micahraney.abstractsim;

import javax.swing.*;
import javax.swing.border.Border;
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

        //initialize ArtPanel
        Map<String,Integer> prefs = new HashMap<>();
        prefs.put("iterations",100);
        prefs.put("intricacy", 3);
        prefs.put("cvariance", 100);
        ArtPanel artPanel = new ArtPanel(prefs);
        frame.add(artPanel, BorderLayout.CENTER);



        //initialize preference modification pane
        JPanel southPanel = new JPanel();
        southPanel.setLayout(new BorderLayout());

        JPanel prefTable = new JPanel();
        prefTable.setLayout(new GridLayout(3,2));
        prefTable.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));

        JTextField seedField = new JTextField();
        prefTable.add(new JLabel("Seed:"),2,0);
        prefTable.add(seedField,2,1);

        JTextField intricacyField = new JTextField();
        prefTable.add(new JLabel("Intricacy:"),1,0);
        prefTable.add(intricacyField,1,1);

        JTextField cvarianceField = new JTextField();
        cvarianceField.setPreferredSize(new Dimension(150,10));
        prefTable.add(new JLabel("Color Variance: "),0,0);
        prefTable.add(cvarianceField,0,1);

        southPanel.add(prefTable, BorderLayout.WEST);

        //initialize button options
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(2,1));

        JButton randomButton = new JButton("Random");
        randomButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                long seed = System.currentTimeMillis();
                seedField.setText(seed + "");
                artPanel.setSeed(seed);
                (new Thread(new RenderTask(artPanel, true, frame))).start();
            }
        });
        buttonPanel.add(randomButton);
        buttonPanel.add(new JButton("Save"));
        southPanel.add(buttonPanel, BorderLayout.EAST);

        //initialize render button
        JButton button = new JButton("Render");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                long seed;
                if(seedField.getText().isEmpty()) {
                    seed = System.currentTimeMillis();
                    seedField.setText(seed + "");
                }
                else{
                    seed = seedField.getText().hashCode();
                }
                artPanel.setSeed(seed);
                (new Thread(new RenderTask(artPanel, true, frame))).start();
            }
        });
        southPanel.add(button, BorderLayout.CENTER);


        frame.add(southPanel,BorderLayout.SOUTH);
        frame.pack();
        frame.setVisible(true);
    }
}
