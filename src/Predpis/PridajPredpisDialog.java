package Predpis;

import Database.SQLHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class PridajPredpisDialog extends JDialog {
    private JPanel contentPane;
    private JButton button_pacient;
    private JButton button_lekarnik;
    private JButton button_latky;
    private JTextField textField1;
    private JButton pridatButton;
    private JButton zrusitButton;
    private JLabel pacientLabel;
    private JLabel lekarnikLabel;
    private JLabel latkaLabel;
    private JButton buttonOK;
    private JButton buttonCancel;
    private int pacientID;
    private int lekarnikID;
    private int latkaID;
    private int liekID;

    public PridajPredpisDialog(Dialog dialog) {
        super(dialog);
        setContentPane(contentPane);
        setModal(true);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        textField1.setText(dateFormat.format(cal.getTime()));

        button_pacient.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PacientDialog dialog = new PacientDialog((Dialog) null);
                dialog.pack();
                dialog.setVisible(true);
                pacientLabel.setText(dialog.getPacient());
                pacientID=dialog.getPacientID();
                pack();
            }
        });
        zrusitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                PredpisDialog dialog = new PredpisDialog((Dialog) null);
                dialog.pack();
                dialog.setVisible(true);
            }
        });
        button_lekarnik.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LekarnikDiagram dialog = new LekarnikDiagram((Dialog) null);
                dialog.pack();
                dialog.setVisible(true);
                lekarnikLabel.setText(dialog.getLekarnik());
                lekarnikID=dialog.getId();
                pack();
            }
        });
        button_latky.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LatkyDialog dialog = new LatkyDialog((Dialog) null);
                dialog.pack();
                dialog.setVisible(true);
                latkaLabel.setText(dialog.getLiek());
                latkaID=dialog.getLatka_id();
                liekID=dialog.getLiek_id();
                pack();
            }
        });
        pridatButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(SQLHandler.getInstance().vytvorPredpis(textField1.getText(),lekarnikID,pacientID,latkaID,liekID)){
                    JOptionPane.showMessageDialog(null,"Predpis bol vytvoreny");
                    dispose();
                    PredpisDialog dialog1 = new PredpisDialog((Dialog)null);
                    dialog1.pack();
                    dialog1.setVisible(true);
                }else{
                    JOptionPane.showMessageDialog(null,"Predpis bol vytvoreny");
                }
            }
        });
    }
}
