package Main;

import Lieky.LiekyDialog;
import Pacienti.PacientiDialog;
import Predpis.PredpisDialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MainDialog extends JDialog {
    private JPanel contentPane;
    private JButton button_predpisy;
    private JButton button_lieky;
    private JButton button_pacienti;

    public MainDialog(Dialog dialog) {
        super(dialog);
        setContentPane(contentPane);
        setModal(true);

        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        button_pacienti.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                PacientiDialog pacienti = new PacientiDialog((Dialog) null);
                pacienti.pack();
                pacienti.setVisible(true);
            }
        });
        button_predpisy.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                PredpisDialog predpis = new PredpisDialog((Dialog) null);
                predpis.pack();
                predpis.setVisible(true);
            }
        });
        button_lieky.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                LiekyDialog liekyDialog = new LiekyDialog((Dialog) null);
                liekyDialog.pack();
                liekyDialog.setVisible(true);
            }
        });
    }

    public static void main(String[] args) {

        MainDialog dialog = new MainDialog((Dialog)null);
        dialog.pack();
        dialog.setVisible(true);
    }
}
