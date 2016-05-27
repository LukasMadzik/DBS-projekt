package Lieky;

import javax.swing.*;
import java.awt.*;

public class VyberATC extends JDialog {
    private JPanel contentPane;
    private JComboBox comboBox1;
    private JComboBox comboBox2;
    private JComboBox comboBox3;
    private JComboBox comboBox4;
    private JComboBox comboBox5;
    private JButton potvrditButton;
    private JButton zrusitButton;

    public VyberATC(Dialog dialog) {
        super(dialog);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setContentPane(contentPane);
        setModal(true);

    }
}
