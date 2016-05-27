package Predpis;

import Database.SQLHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PredpisPodrobnostiDialog extends JDialog {
    private JPanel contentPane;
    private JList list1;
    private JButton zrusitButton;
    private JButton detailButton;
    private JLabel datumLabel;

    public PredpisPodrobnostiDialog(Dialog dialog, int id) {
        super(dialog);
        setContentPane(contentPane);
        setModal(true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        ResultSet predpis = SQLHandler.getInstance().getPredpis(id);
        List<String> array = new ArrayList<String>();
        int lek_id = 1;
        int pac_id = 1;
        int lat_id = 1;
        int liek_id = 1;
        try {
            predpis.next();
            datumLabel.setText(datumLabel.getText() + predpis.getString(1));
            int size = predpis.getMetaData().getColumnCount();
            for (int i = 2; i < size - 3; i++) {
                array.add(predpis.getMetaData().getColumnName(i) + ": " + predpis.getString(i));
            }
            //array:0:lekarnik, 1:pacient, 2:latka
            lek_id = predpis.getInt(size - 3);
            pac_id = predpis.getInt(size -2);
            lat_id = predpis.getInt(size-1);
            liek_id = predpis.getInt(size);
            list1.setListData(array.toArray());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        zrusitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                PredpisDialog predpis = new PredpisDialog((Dialog) null);
                predpis.pack();
                predpis.setVisible(true);
            }
        });
        final int finalLek_id = lek_id;
        final int finalPac_id = pac_id;
        final int finalLat_id = lat_id;
        final int finalLiek_id = liek_id;
        detailButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int id = 0;
                id = list1.getSelectedIndex();
                switch (id) {
                    case 0: {
                        ResultSet lekarnik = null;
                        String str = null;
                        try {
                            lekarnik = SQLHandler.getInstance().getLekarnik(finalLek_id);
                            lekarnik.next();
                            str = "Meno: " + lekarnik.getString(1) +
                                    "\nPracovisko: " + lekarnik.getString(2) +
                                    "\nDoposial zarobil: " + lekarnik.getInt(3);
                            JOptionPane.showMessageDialog(null, str);
                        } catch (SQLException e1) {
                            e1.printStackTrace();
                        }
                        break;
                    }
                    case 1: {
                        ResultSet pacient = null;
                        String str = null;
                        try {
                            pacient = SQLHandler.getInstance().getPacientDetail(finalPac_id);
                            str = array.get(1) + "\nPredpisane lieky:";
                            while (pacient.next()) {
                                str += "\n      " + pacient.getString(1);
                            }
                            JOptionPane.showMessageDialog(null, str);
                        } catch (SQLException e1) {
                            e1.printStackTrace();
                        }
                        break;
                    }
                    case 2: {
                        ResultSet latky = null;
                        String str = "Pre ucinnu latku "+ array.get(2).substring(7)+ " mame v systeme nasledujuce lieky:\n\n";
                        latky=SQLHandler.getInstance().getUcinneLatkyDetail(finalLat_id);
                        int i =0;
                        try {
                            while(latky.next()){
                                i++;
                                if(i==6){
                                    JOptionPane.showMessageDialog(null, "Liekov je viac ako 5, pre uplne zobradenie stlacte OK");
                                }
                                str+=latky.getString(2)+"  "+latky.getString(3)+"  "+latky.getString(4)+" ATC: "
                                        +latky.getString("atc1s")+latky.getString("atc2s")+latky.getString("atc3s")+latky.getString("atc4s")+latky.getString("atc5s")+"\n\n";

                            }
                            JOptionPane.showMessageDialog(null, str);
                        } catch (SQLException e1) {
                            e1.printStackTrace();
                        }
                        break;
                    }
                    case 3:{
                        ResultSet liek = SQLHandler.getInstance().getLiek(finalLiek_id);
                        try {
                            liek.next();
                        String str = "Nazov lieku: "+liek.getString(2)+"  "+liek.getString(3)+"  "+liek.getString(4)+" ATC: "
                                +liek.getString("atc1s")+liek.getString("atc2s")+liek.getString("atc3s")+liek.getString("atc4s")+liek.getString("atc5s")+"\n\n";
                        JOptionPane.showMessageDialog(null, str);
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    }
                    }
                }
            }
        });
    }
}
