package Predpis;

import Database.SQLHandler;
import Main.MainDialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.List;

public class PredpisDialog extends JDialog {
    private JPanel contentPane;
    private JList list1;
    private JButton button_pridaj;
    private JButton button_spet;
    private JButton button_detail;
    private JButton buttonOK;
    private JButton buttonCancel;

    public PredpisDialog(Dialog dialog) {
        super(dialog);
        JCheckBox[] vyber = new JCheckBox[4];
        vyber[0] = new JCheckBox("Datum");
        vyber[1] = new JCheckBox("Lekarnik");
        vyber[2] = new JCheckBox("Pacient");
        vyber[3] = new JCheckBox("Ucinna latka");
        String msg = "Informacie pre zobrazenie predpisov:";
        Object[] params = {msg, vyber[0], vyber[1], vyber[2],vyber[3]};
        int chose = JOptionPane.showConfirmDialog(null,params,"Volba filtra",JOptionPane.OK_CANCEL_OPTION);
        if(chose==0) {
            setContentPane(contentPane);
            setDefaultCloseOperation(DISPOSE_ON_CLOSE);
            setModal(true);
            SQLHandler.getInstance();
            ResultSet predpisy = SQLHandler.getInstance().getPredpisy(vyber[0].isSelected(),vyber[1].isSelected(),vyber[2].isSelected(),vyber[3].isSelected());
            List<String> list = new ArrayList<String>();
            List<Integer> id = new ArrayList<Integer>();
            try {
                while(predpisy.next()){
                    id.add(predpisy.getInt(1));
                    String temp = "";
                    for(int i =2;i<=predpisy.getMetaData().getColumnCount();i++){
                        temp += predpisy.getMetaData().getColumnName(i)+": "+predpisy.getString(i)+" ";
                    }
                    list.add(temp);
                    temp = "";
                }
                list1.setListData(list.toArray());
            } catch (SQLException e) {
                e.printStackTrace();
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
                    int ind = 1;
                    try {
                        ind = id.get(list1.getSelectedIndex());
                    }catch (Exception ee){
                        System.out.printf("Nebol zvoleny zaznam");
                    }
                    dispose();
                    PredpisPodrobnostiDialog dialog = new PredpisPodrobnostiDialog((Dialog) null,ind);
                    dialog.pack();
                    dialog.setVisible(true);
                }
            });
        }else{
            dispose();
            MainDialog mdialog = new MainDialog((Dialog) null);
            mdialog.pack();
            mdialog.setVisible(true);
        }
        button_pridaj.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                PridajPredpisDialog pdialog = new PridajPredpisDialog((Dialog) null);
                pdialog.pack();
                pdialog.setVisible(true);
            }
        });
    }
}
