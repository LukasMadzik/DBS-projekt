package Poistovne;

import javax.swing.*;

public class PridajLiekDialog extends JDialog {
    private JPanel contentPane;
    private JList list1;
    private JTextField textField1;
    private JButton button_hladaj;
    private JButton button_novy;
    private JButton button_spet;
    private JButton button_vyber;
    private JButton buttonOK;

    public PridajLiekDialog() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
    }
}
