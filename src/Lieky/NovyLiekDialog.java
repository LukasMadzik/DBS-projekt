package Lieky;

import Database.SQLHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.List;

public class NovyLiekDialog extends JDialog {
    private JPanel contentPane;
    private JTextField textField1;
    private JTextField textField2;
    private JComboBox vyrobcaComboBox;
    private JButton button_novy_vyrobca;
    private JButton button_preskr_obmedz;
    private JComboBox formaComboBox1;
    private JButton button_hradenie;
    private JButton button_vytvorit;
    private JLabel obmedzeniaLabel;
    private JComboBox latkaComboBox;
    private List<Integer> obmedzeniaNaPridanie_id = new ArrayList<Integer>();

    public String getLiekName(){
        return textField1.getText();
    }

    public NovyLiekDialog(Dialog dialog) {
        super(dialog);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setContentPane(contentPane);
        setModal(true);
        List<Integer> vyrobcovia_id = new ArrayList<Integer>();
        List<Integer> formy_id = new ArrayList<Integer>();
        List<Integer> latky_id = new ArrayList<Integer>();
        List<String> obmedzenia_skratka = new ArrayList<String>();
        List<Integer> obmedzenia_id = new ArrayList<Integer>();
        try {
            ResultSet vyrobcovia = SQLHandler.getInstance().getVyrobcovia();
            while(vyrobcovia.next()){
                vyrobcovia_id.add(vyrobcovia.getInt(1));
                vyrobcaComboBox.addItem(vyrobcovia.getString(2) + " \t(" + vyrobcovia.getInt(1) + ")");
            }
            ResultSet formy = SQLHandler.getInstance().getFormy();
            while (formy.next()){
                formy_id.add(formy.getInt(1));
                formaComboBox1.addItem(formy.getString(2) + " \t(" + formy.getInt(1) + ")");
            }
            ResultSet latky = SQLHandler.getInstance().getUcinneLatky();
            while(latky.next()){
                latky_id.add(latky.getInt(1));
                latkaComboBox.addItem(latky.getString(2) + " \t(" + latky.getInt(1) + ")");
            }
        } catch (SQLException e) {
            System.out.printf("Chyba pri citani z resultset: %s\n",e);
        }

        button_preskr_obmedz.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<String> possibilities = new ArrayList<String>();
                ResultSet obmedzenia = SQLHandler.getInstance().getObmedzenia();
                try {
                    while(obmedzenia.next()){
                        obmedzenia_skratka.add(obmedzenia.getString(3));
                        obmedzenia_id.add(obmedzenia.getInt(1));
                        possibilities.add(obmedzenia.getString(2) + " \t(" + obmedzenia.getString(3) + ")");
                    }
                } catch (SQLException ex) {
                    System.out.printf("Chyba pri citani z resultset: %s\n",ex);
                }
                JFrame frame = new JFrame("Input");
                String s = (String)JOptionPane.showInputDialog(frame,"Vyber obmedzenie:","Vyber obmedzenia",
                        JOptionPane.QUESTION_MESSAGE,null,possibilities.toArray(),null);
                obmedzeniaNaPridanie_id.add(possibilities.indexOf(s)+1);
                if(s!=null){
                    obmedzeniaLabel.setText(obmedzeniaLabel.getText()+s);
                }
            }
        });

        button_vytvorit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(SQLHandler.getInstance().vytvorLiek(textField1.getText(),Integer.parseInt(textField2.getText()),
                        vyrobcovia_id.get(vyrobcaComboBox.getSelectedIndex()),formy_id.get(formaComboBox1.getSelectedIndex()),
                        latky_id.get(latkaComboBox.getSelectedIndex()),obmedzeniaNaPridanie_id)){
                    JOptionPane.showMessageDialog(null, "Liek bol vytvoreny");
                    dispose();

                }else{
                    JOptionPane.showMessageDialog(null, "Liek nebol vytvoreny");
                }
            }
        });
    }
}
