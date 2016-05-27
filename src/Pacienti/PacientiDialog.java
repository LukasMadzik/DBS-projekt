package Pacienti;

import Database.SQLHandler;
import Main.MainDialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PacientiDialog extends JDialog {
    private JPanel contentPane;
    private JList list1;
    private JButton button_spet;
    private JButton button_detail;
    private JButton novyButton;

    public PacientiDialog(Dialog dialog) {
        super(dialog);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setContentPane(contentPane);
        setModal(true);
        SQLHandler.getInstance();
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
                MainDialog dialog = new MainDialog((Dialog) null);
                dialog.pack();
                dialog.setVisible(true);
            }
        });
        button_detail.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int id = 1;
                try {
                    id = Integer.parseInt(list1.getSelectedValue().toString().replaceAll("[\\D]", ""));
                } catch (Exception ex){
                    System.out.printf("Nebol zvoleny zaznam\n");
                }
                dispose();
                PacientiPodrobnostiDialog dialog = new PacientiPodrobnostiDialog((Dialog) null,id);
                dialog.pack();
                dialog.setVisible(true);
            }
        });
        novyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                NovyPacientDialog dialog = new NovyPacientDialog((Dialog) null);
                dialog.pack();
                dialog.setVisible(true);
            }
        });
    }
}
