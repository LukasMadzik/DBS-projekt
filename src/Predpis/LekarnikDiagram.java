package Predpis;

import Database.SQLHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LekarnikDiagram extends JDialog {
    private JPanel contentPane;
    private JList list1;
    private JButton button_ok;
    private JButton button_spet;
    private int id = -1;
    private String lekarnik = "Lekarnik nebol zvoleny";

    public int getId() {
        return id;
    }

    public String getLekarnik() {
        return lekarnik;
    }

    public LekarnikDiagram(Dialog dialog) {
        super(dialog);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setContentPane(contentPane);
        setModal(true);
        ResultSet lekarnici = SQLHandler.getInstance().getLekarnici();
        List<String> mena = new ArrayList<String>();
        try {
            while (lekarnici.next()) {
                mena.add(lekarnici.getString(2) + " \t(" + lekarnici.getInt(1) + ")");
            }
            java.util.Collections.sort(mena);
            list1.setListData(mena.toArray());
        } catch (SQLException e) {
            System.out.printf("Chyba pri citani z resultset: %s\n", e);
        }
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
                    lekarnik = mena.get(list1.getSelectedIndex());
                }catch (Exception ex){
                    System.out.printf("%s",ex);
                }
            }
        });
        button_spet.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }
}
