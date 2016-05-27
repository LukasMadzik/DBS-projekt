package Predpis;

import Database.SQLHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.List;

public class PacientDialog extends JDialog {
    private JPanel contentPane;
    private JButton button_ok;
    private JButton button_spet;
    private JList list1;
    private String pacient = "Pacient nezvoleny";
    private int id = -1;

    public String getPacient(){
        return pacient;
    }

    public int getPacientID(){
        return id;
    }

    public PacientDialog(Dialog dialog) {
        super(dialog);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setContentPane(contentPane);
        setModal(true);
        ResultSet pacienti = SQLHandler.getInstance().getPacienti();
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
        button_spet.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        button_ok.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    id = Integer.parseInt(list1.getSelectedValue().toString().replaceAll("[\\D]", ""));
                } catch (Exception ex){
                    System.out.printf("Nebol zvoleny zaznam\n");
                }
                dispose();
                try {
                    pacient = mena.get(list1.getSelectedIndex());
                }catch (Exception ex){
                    System.out.printf("%s",ex);
                }
            }
        });
    }
}
