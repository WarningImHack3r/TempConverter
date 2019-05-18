/**
 * TempConverter
 */

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.text.*;
import javax.swing.border.*;

public class TempConverter extends JFrame {
    private static final long serialVersionUID = 1L;

    public TempConverter(String titre) {
        super(titre);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.getContentPane().setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
        this.setSize(new Dimension(450, 190));
        this.init(titre);
        
        this.setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setVisible(true);
    }
    
    public void init(String titre) {
        // Init
        JPanel top = new JPanel();
        top.setLayout(new BoxLayout(top, BoxLayout.Y_AXIS));
        top.setBorder(new EmptyBorder(10, 10, 10, 10));
        JLabel title = new JLabel(titre);
        title.setFont(new Font(null, Font.PLAIN, 24));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JPanel bottom = new JPanel();
        bottom.setBorder(new EmptyBorder(10, 10, 10, 10));
        JPanel panelGauche = new JPanel();
        panelGauche.setBorder(new EmptyBorder(10, 10, 10, 10));
        JButton fleche = new JButton("⇄");
        JPanel panelDroit = new JPanel();
        panelDroit.setBorder(new EmptyBorder(10, 10, 10, 10));
        JTextField temp1 = new JTextField(5);
        JTextField temp2 = new JTextField(5);
        String[] temps = {"°C", "°F", "°K"};
        JComboBox comboG = new JComboBox(temps);
        JComboBox comboD = new JComboBox(temps);
        comboD.setSelectedIndex(1);

        // Listeners
        temp1.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent event) {
                doTemp1(temp1, temp2, comboG, comboD);
            }
        });
        
        comboG.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                String selected = String.valueOf(comboG.getSelectedItem());
                if (selected == String.valueOf(comboD.getSelectedItem())) {
                    if (selected == temps[0]) {
                        comboD.setSelectedIndex(1);
                    } else {
                        comboD.setSelectedIndex(0);
                    }
                }
                doTemp2(temp1, temp2, comboG, comboD);
            }
        });

        temp2.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent event) {
                doTemp2(temp1, temp2, comboG, comboD);
            }
        });

        comboD.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                String selected = String.valueOf(comboD.getSelectedItem());
                if (selected == String.valueOf(comboG.getSelectedItem())) {
                    if (selected == temps[0]) {
                        comboG.setSelectedIndex(1);
                    } else {
                        comboG.setSelectedIndex(0);
                    }
                }
                doTemp1(temp1, temp2, comboG, comboD);
            }
        });

        fleche.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                String tempTemp = temp1.getText();
                temp1.setText(temp2.getText());
                temp2.setText(tempTemp);
                String comboTemp = String.valueOf(comboG.getSelectedItem());
                comboG.setSelectedItem(String.valueOf(comboD.getSelectedItem()));
                comboD.setSelectedItem(comboTemp);
            }
        });
        
        // Add
        panelGauche.add(temp1);
        panelGauche.add(comboG);
        panelDroit.add(temp2);
        panelDroit.add(comboD);
        top.add(title);
        bottom.add(panelGauche);
        bottom.add(fleche);
        bottom.add(panelDroit);
        this.add(top);
        this.add(bottom);
    }
    
    public void doTemp1(JTextField temp1, JTextField temp2, JComboBox comboG, JComboBox comboD) {
        String typed = temp1.getText();
        typed = typed.replace(",", ".");
        if (!typed.matches("^([+-]?\\d*\\.?\\d*)$") || typed.length() < 1) {
            temp2.setText("");
            return;
        }
        try {
            temp2.setText("" + calc(typed, String.valueOf(comboG.getSelectedItem()), String.valueOf(comboD.getSelectedItem())));
        } catch (NumberFormatException e) {
            temp2.setText("");
        }
    }

    public void doTemp2(JTextField temp1, JTextField temp2, JComboBox comboG, JComboBox comboD) {
        String typed = temp2.getText();
        typed = typed.replace(",", ".");
        if (!typed.matches("^([+-]?\\d*\\.?\\d*)$") || typed.length() < 1) {
            temp1.setText("");
            return;
        }
        try {
            temp1.setText("" + calc(typed, String.valueOf(comboD.getSelectedItem()), String.valueOf(comboG.getSelectedItem())));
        } catch (NumberFormatException e) {
            temp1.setText("");
        }
    }

    public String calc(String value, String convertFrom, String convertTo) {
        DecimalFormat df = new DecimalFormat("#.##");
        String resu = "";
        if (convertFrom == "°C") {
            if (convertTo == "°F") {
                resu = df.format(Float.valueOf(value.trim())*9/5+32);
            } else if (convertTo == "°K") {
                resu = df.format(Float.valueOf(value.trim())+273.15);
            }
        } else if (convertFrom == "°F") {
            if (convertTo == "°C") {
                resu = df.format((Float.valueOf(value.trim())-32)*5/9);
            } else if (convertTo == "°K") {
                resu = df.format(((Float.valueOf(value.trim())-32)*5/9)+273.15);
            }
        } else if (convertFrom == "°K") {
            if (convertTo == "°C") {
                resu = df.format(Float.valueOf(value.trim())-273.15);
            } else if (convertTo == "°F") {
                resu = df.format(((Float.valueOf(value.trim()))-273.15)*9/5+32);
            }
        }
        return resu;
    }

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new TempConverter("TempConverter");
            }
        });
    }
}