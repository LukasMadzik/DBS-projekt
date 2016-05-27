package Pacienti;

import Database.SQLHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PacientiPodrobnostiDialog extends JDialog {
    private JPanel contentPane;
    private JList list1;
    private JButton button_spet;
    private JButton zmenitButton;
    private JButton vymazatButton;

    public PacientiPodrobnostiDialog(Dialog dialog, int id) {
        super(dialog);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setContentPane(contentPane);
        setModal(true);
        ResultSet pacient = SQLHandler.getInstance().getPacient(id);
        List<String> array = new ArrayList<String>();
        try {
            pacient.next();
            int size = pacient.getMetaData().getColumnCount();
            for(int i = 1;i<=pacient.getMetaData().getColumnCount();i++){
                array.add(pacient.getString(i));
            }
            list1.setListData(array.toArray());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        button_spet.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                PacientiDialog dialog = new PacientiDialog((Dialog) null);
                dialog.pack();
                dialog.setVisible(true);
            }
        });
        zmenitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int ind = list1.getSelectedIndex();
                if(ind == -1){
                    ind = 0;
                }
                switch(ind){
                    case 0:{
                        String nove_meno = JOptionPane.showInputDialog("Nove meno:",array.get(0));
                        if(nove_meno!=null) {
                            SQLHandler.getInstance().zmenMenoPacienta(id, nove_meno);
                            array.remove(0);
                            array.add(0, nove_meno);
                            list1.setListData(array.toArray());
                        }
                        break;
                    }
                    case 1:{
                        String nova_adresa = JOptionPane.showInputDialog("Nova adresa:",array.get(1));
                        if(nova_adresa!=null) {
                            SQLHandler.getInstance().zmenAdresuPacienta(id, nova_adresa);
                            array.remove(1);
                            array.add(1, nova_adresa);
                            list1.setListData(array.toArray());
                        }
                        break;
                    }
                    case 2:{
                        String nove_cislo = JOptionPane.showInputDialog("Nove rodne cislo:",array.get(2));
                        if (nove_cislo!=null){
                            SQLHandler.getInstance().zmenRodneCisloPacienta(id, nove_cislo);
                            array.remove(2);
                            array.add(2, nove_cislo);
                            list1.setListData(array.toArray());
                        }
                        break;
                    }
                    case 3:{
                        List<String> possibilities = new ArrayList<String>();
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
                        if(s!=null){
                            SQLHandler.getInstance().zmenPoistovnuPacienta(id,Integer.parseInt(s.toString().replaceAll("[\\D]", "")));
                            array.remove(3);
                            array.add(3, String.valueOf(Integer.parseInt(s.toString().replaceAll("[\\D]", ""))));
                            list1.setListData(array.toArray());
                        }
                    }
                }
            }
        });
        vymazatButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(SQLHandler.getInstance().vymazPacienta(id)){
                    JOptionPane.showMessageDialog(null, "Pacient bol vymazany");
                    dispose();
                    PacientiDialog dialog = new PacientiDialog((Dialog) null);
                    dialog.pack();
                    dialog.setVisible(true);
                }else{
                    JOptionPane.showMessageDialog(null, "Pacient nebol vymazany");
                }
            }
        });
    }
}
