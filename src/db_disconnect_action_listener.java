package src;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class db_disconnect_action_listener implements ActionListener {
    private final JLabel main_db_status;

    public db_disconnect_action_listener(JLabel main_db_status) {
        this.main_db_status = main_db_status;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        this.main_db_status.setText("Database Status: Disconnected");
    }
}
