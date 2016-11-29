/**
 * Created by Mateusz on 09.03.2016.
 */
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;


class AboutDialog extends JDialog
{
    public AboutDialog(Frame owner)
    {
        super(owner,"O Programie",true);
        setSize(405,190);
        setResizable(false);
        JButton exit = new JButton("OK");
        exit.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent event)
            {
                setVisible(false);
            }
        });
        add(new JLabel("<html><h1><i>Turing Machine - Emulator</i></h1><hr>Written by: Mateusz Kalinowski<br>"
                + "E-mail:"
                + " mateusz.kaliowski@icloud.com<br>Version: 1.0</html>"),BorderLayout.CENTER);
        JPanel panel = new JPanel();
        panel.add(exit);
        add(panel,BorderLayout.SOUTH);

    }


}
