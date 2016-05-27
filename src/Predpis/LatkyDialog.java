package Predpis;

import Database.SQLHandler;
import Lieky.NovyLiekDialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.List;

public class LatkyDialog extends JDialog {
    private JPanel contentPane;
    private JList list1;
    private JButton button_spet;
    private JButton button_ok;
    private JButton novaUcinnaLatkaButton;
    private int latka_id = -1;
    private String liek = "Liek nebol zvoleny";
    private int liek_id=-1;

    public String getLiek() {
        return liek;
    }

    public int getLiek_id() {
        return liek_id;
    }

    public int getLatka_id() {
        return latka_id;
    }

    public LatkyDialog(Dialog dialog) {
        super(dialog);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setContentPane(contentPane);
        setModal(true);
        ResultSet pacienti = SQLHandler.getInstance().getUcinneLatky();
        List<String> mena = new ArrayList<String>();
        try {
            while(pacienti.next()){
                mena.add(pacienti.getString(2) + " \t(" + pacienti.getInt(1) + ")");
            }
            java.util.Collections.sort(mena);
            list1.setListData(mena.toArray());
        } catch (SQLException e) {
            System.out.printf("Chyba pri citani z resultset: %s\n",e);
        }
        button_ok.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    latka_id = Integer.parseInt(list1.getSelectedValue().toString().replaceAll("[\\D]", ""));
                } catch (Exception ex){
                    System.out.printf("Nebol zvoleny zaznam\n");
                }
                ResultSet lieky = SQLHandler.getInstance().getLiekyByLatky(latka_id);
                List<String> possibilities = new ArrayList<String>();
                List<Integer> lieky_id = new ArrayList<Integer>();
                try {
                    if(!lieky.next()){
                        if(JOptionPane.showConfirmDialog(null,"Liek s touto ucinnou latkou nebol najdeny, zelate si takyto liek vytvorit?", null, JOptionPane.YES_NO_OPTION) == 0){
                            NovyLiekDialog novyLiekDialog = new NovyLiekDialog((Dialog)null);
                            novyLiekDialog.pack();
                            novyLiekDialog.setVisible(true);
                        }
                    }else{
                        lieky_id.add(lieky.getInt("ucinna_latka_id"));
                        possibilities.add(lieky.getString("nazov"));
                        while(lieky.next()){
                            possibilities.add(lieky.getString("nazov"));
                            lieky_id.add(lieky.getInt("ucinna_latka_id"));
                        }
                        JFrame frame = new JFrame("Input");
                        String s = (String)JOptionPane.showInputDialog(frame,"Vyber liek:","Lieky",JOptionPane.QUESTION_MESSAGE,null,possibilities.toArray(),"ham");
                        if(s!=null){
                            liek_id=lieky_id.get(possibilities.indexOf(s));
                            liek=s;
                            dispose();
                        }
                    }
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        });
        button_spet.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        novaUcinnaLatkaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(SQLHandler.getInstance().vytvorUcinnuLatku(JOptionPane.showInputDialog("Nazov novej ucinnej latky:"))){
                    JOptionPane.showMessageDialog(null,"Latka bola vytvorena");
                }else{
                    JOptionPane.showMessageDialog(null,"Latka nebola vytvorena");
                }
            }
        });
    }
}
