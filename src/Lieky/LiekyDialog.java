package Lieky;

import Database.SQLHandler;
import Main.MainDialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.List;

public class LiekyDialog extends JDialog {
    private JPanel contentPane;
    private JList list1;
    private JButton button_nove;
    private JButton button_navrat;

    public LiekyDialog(Dialog dialog) {
        super(dialog);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setContentPane(contentPane);
        setModal(true);
        ResultSet lieky = SQLHandler.getInstance().getLieky();
        List<String> mena = new ArrayList<String>();
        try {
            while(lieky.next()){
                mena.add(lieky.getString("nazov"));
            }
            java.util.Collections.sort(mena);
            list1.setListData(mena.toArray());
        } catch (SQLException e) {
            System.out.printf("Chyba pri citani z resultset: %s\n",e);
        }
        button_nove.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                NovyLiekDialog novyLiekDialog = new NovyLiekDialog((Dialog) null);
                novyLiekDialog.pack();
                novyLiekDialog.setVisible(true);
                mena.add(novyLiekDialog.getLiekName());
                list1.setListData(mena.toArray());
            }
        });
        button_navrat.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                MainDialog dialog = new MainDialog((Dialog)null);
                dialog.pack();
                dialog.setVisible(true);
            }
        });
    }
}
