package Pacienti;

import Database.SQLHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class NovyPacientDialog extends JDialog {
    private JPanel contentPane;
    private JTextField textFieldMeno;
    private JTextField textFieldAdresa;
    private JTextField textFieldRodneCislo;
    private JButton button_vyber_poistovnu;
    private JButton vytvoritButton;
    private JButton zrusitButton;

    public NovyPacientDialog(Dialog dialog) {
        setContentPane(contentPane);
        setModal(true);
        final String[] meno = new String[1];
        final String[] bydlisko = new String[1];
        final int[] poistovna_id = new int[1];
        poistovna_id[0] = -1;
        final String[] rodne_cislo = new String[1];
        vytvoritButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                meno[0] = textFieldMeno.getText();
                bydlisko[0] = textFieldAdresa.getText();
                rodne_cislo[0] = textFieldRodneCislo.getText();
                if(!meno[0].isEmpty() && !bydlisko[0].isEmpty() && !rodne_cislo[0].isEmpty() && poistovna_id[0]!=-1){
                    SQLHandler.getInstance().vytvorPacienta(meno[0],bydlisko[0],rodne_cislo[0],poistovna_id[0]);
                    JOptionPane.showMessageDialog(null,"Pacient bol vytvoreny");
                }else{
                    JOptionPane.showMessageDialog(null,"Pacient nebol vytvoreny");
                }
            }
        });
        button_vyber_poistovnu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                java.util.List<String> possibilities = new ArrayList<String>();
                ResultSet poistovne = SQLHandler.getInstance().getPoistovne();
                try {
                    while(poistovne.next()){
                        possibilities.add(poistovne.getString(2) + " \t(" + poistovne.getInt(3) + ")");
                    }
                } catch (SQLException ex) {
                    System.out.printf("Chyba pri citani z resultset: %s\n",ex);
                }

                JFrame frame = new JFrame("Input");
                String s = (String)JOptionPane.showInputDialog(frame,"Zadaj novu poistovnu:","Zmena poistovne",JOptionPane.QUESTION_MESSAGE,null,possibilities.toArray(),"ham");
                poistovna_id[0] =Integer.parseInt(s.replaceAll("[\\D]", ""));
            }
        });
        zrusitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                PacientiDialog dialog = new PacientiDialog((Dialog) null);
                dialog.pack();
                dialog.setVisible(true);
            }
        });
    }
}
