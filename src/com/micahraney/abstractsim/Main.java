package com.micahraney.abstractsim;

import sun.rmi.server.InactiveGroupException;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class Main {

    private static JTextField iterationsField,  cvarianceField, seedField, intricacyField;

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
        prefTable.setLayout(new GridLayout(4,2));
        prefTable.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));

        seedField = new JTextField();
        prefTable.add(new JLabel("Seed:"),3,0);
        prefTable.add(seedField,3,1);

        intricacyField = new JTextField();
        prefTable.add(new JLabel("Intricacy:"),2,0);
        prefTable.add(intricacyField,2,1);

        cvarianceField = new JTextField();
        prefTable.add(new JLabel("Color Variance: "),1,0);
        prefTable.add(cvarianceField,1,1);

        iterationsField= new JTextField();
        iterationsField.setPreferredSize(new Dimension(150,10));
        prefTable.add(new JLabel("Iterations: "),0,0);
        prefTable.add(iterationsField,0,1);

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

        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fc = new JFileChooser();
                fc.setSelectedFile(new File(System.getProperty("user.dir")));
                if(fc.showSaveDialog(frame)!=JFileChooser.APPROVE_OPTION)
                    return;
                fc.setDialogTitle("Select Export Location");
                File of = fc.getSelectedFile();

                if(of == null)
                    return;//no file selected

                (new Thread(new ExportTask(artPanel,3840, 2160, of))).start();
            }
        });
        buttonPanel.add(saveButton);
        southPanel.add(buttonPanel, BorderLayout.EAST);


        //initialize render button
        JButton button = new JButton("Render");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                //parse the seed
                long seed;
                if(seedField.getText().isEmpty()) {
                    seed = System.currentTimeMillis();
                    seedField.setText(seed + "");
                }
                else{
                    seed = seedField.getText().hashCode();
                }
                artPanel.setSeed(seed);
                Map<String,Integer> prefs = loadUIPreferences();
                if(prefs == null){
                    JOptionPane.showMessageDialog(frame,"All render options (except seed) must be\n" +
                            "integers (whole numbers)! Cannot process.","Render Error!",JOptionPane.WARNING_MESSAGE);
                    return;
                }
                //prefs must be good.
                artPanel.loadPreferences(prefs);
                (new Thread(new RenderTask(artPanel, true, frame))).start();
            }
        });
        southPanel.add(button, BorderLayout.CENTER);


        frame.add(southPanel,BorderLayout.SOUTH);
        showUIPreferences(prefs);
        frame.pack();
        frame.setVisible(true);
    }

    private static Map<String, Integer> loadUIPreferences(){
        int iterations, intricacy, cvariance;
        try{
            iterations = Integer.parseInt(iterationsField.getText());
            intricacy = Integer.parseInt(intricacyField.getText());
            cvariance = Integer.parseInt(cvarianceField.getText());
        }
        catch(NumberFormatException e){
            return null;
        }

        //initialize prefs
        Map<String,Integer> prefs = new HashMap<>();
        prefs.put("iterations",iterations);
        prefs.put("intricacy", intricacy);
        prefs.put("cvariance", cvariance);

        return prefs;
    }
    private static void showUIPreferences(Map<String, Integer> prefs){

        //initialize ArtPanel
        iterationsField.setText(""+prefs.get("iterations"));
        intricacyField.setText(""+prefs.get("intricacy"));
        cvarianceField.setText(""+prefs.get("cvariance"));

    }

}
