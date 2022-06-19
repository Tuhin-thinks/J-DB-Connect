package src;

import src.db_connector.sql_main;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class db_connect_action_listener implements ActionListener {

    private final JTextField db_host_text;
    private final JTextField db_port_text;
    private final JTextField db_user_text;
    private final JTextField db_pass_text;
    private final JTextField db_name_text;
    private final JLabel main_db_status;
    private final JFrame main_frame;
    private sql_main sql_connector;

    public db_connect_action_listener(JTextField db_host_text, JTextField db_port_text, JTextField db_user_text, JTextField db_pass_text, JTextField db_name_text, JLabel main_db_status, JFrame main_frame) {
        // store as class variables
        this.db_host_text = db_host_text;
        this.db_port_text = db_port_text;
        this.db_user_text = db_user_text;
        this.db_pass_text = db_pass_text;
        this.db_name_text = db_name_text;
        this.main_db_status = main_db_status;
        this.main_frame = main_frame;
    }

    /**
     * Called when the user clicks the connect button.
     * @param e ActionEvent
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        // check event type
        System.out.println("Event type: " + e.getActionCommand());
        // connect to database
        boolean response = connectToDatabase();
        if (response){
            this.main_db_status.setText("Database Status: Connected");
            openOperationsWindow();
        }
        else{
            this.main_db_status.setText("Database Status: Disconnected");
        }
    }

    /**
     * Connect to database
     * @return true if successful, false if not
     */
    public boolean connectToDatabase() {
        // connect to database
        // print out connection details
        String host = db_host_text.getText();
        String port = db_port_text.getText();
        String user = db_user_text.getText();
        String pass = db_pass_text.getText();
        String db_name = db_name_text.getText();
        // connect to mysql database
        this.sql_connector = new sql_main(host, port, user, pass, db_name);
        return this.sql_connector.connectToDatabase();
    }

    private void openOperationsWindow() {
        // open a modal dialog box to perform operations on db
        OperationsWindow operationsWindow = new OperationsWindow(this.main_frame, this.sql_connector);
        operationsWindow.setVisible(true);
    }

    public void closeDatabase() {
        try {
            this.sql_connector.close_connection();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
