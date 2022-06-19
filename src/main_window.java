package src;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Vector;

public class main_window extends JFrame{//inheriting JFrame
    JFrame main_frame;
    JTextField db_host_text = new JTextField();
    JTextField db_port_text = new JTextField();
    JTextField db_user_text = new JTextField();
    JTextField db_pass_text = new JTextField();
    JTextField db_name_text = new JTextField();
    private db_connect_action_listener connectionListener;
    main_window(){
        // create a main panel to hold all other panels and widgets
        JPanel main_panel = new JPanel();
        // create widgets for database connection
        JLabel db_host = new JLabel("Host");
        JLabel db_port = new JLabel("Port");
        JLabel db_user = new JLabel("User");
        JLabel db_pass = new JLabel("Password");
        JLabel db_name = new JLabel("Database");


        JPanel button_panel = new JPanel();
        JButton db_connect = new JButton("Connect");
        JButton db_disconnect = new JButton("Disconnect");
        button_panel.add(db_connect);
        button_panel.add(db_disconnect);

        // create widgets for main window
        JLabel main_title = new JLabel("Applet Demo 1");
        JLabel main_subtitle = new JLabel("Database Connection");
        JLabel main_db_status = new JLabel("Database Status: Disconnected");


        // add widgets to main window using grid layout
        // add margin to layout
        GridLayout main_layout = new GridLayout(0,2);
        main_panel.setLayout(main_layout);
        List<JComponent> componentList = Arrays.asList(db_host, db_host_text, db_port, db_port_text, db_user,
                db_user_text, db_pass, db_pass_text, db_name, db_name_text,
                main_title, main_subtitle, main_db_status, button_panel);
        main_panel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        for (JComponent component : componentList) {
            main_panel.add(component, BorderLayout.CENTER);
        }

        // create a spacer to fill the space between the main panel and the frame
        JSeparator spacer = new JSeparator(JSeparator.VERTICAL);
        spacer.setPreferredSize(new Dimension(-1, 50));
        add(spacer, BorderLayout.SOUTH);
        add(main_panel, BorderLayout.CENTER);
        // add styling to grid layout

        // configurations to make main window visible
        setTitle("Applet Demo 1");
        setSize(500,350);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // move window to center of screen
        setLocationRelativeTo(null);

        // add action listeners to buttons
        db_connect.addActionListener(e -> {
            if (connectionListener != null) {
                connectionListener.actionPerformed(e);
            }
            else {
                connectionListener = new db_connect_action_listener(db_host_text, db_port_text, db_user_text, db_pass_text, db_name_text, main_db_status, main_frame);
                connectionListener.actionPerformed(e);
            }
        });
        db_disconnect.addActionListener(new db_disconnect_action_listener(main_db_status));

        loadConnectionDetailsIfExist();

        addWindowListener(
                new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                        connectionListener.closeDatabase();
                        System.out.println("Database connection closed [If existing]");
                    }
                }
        );

    }

    private void loadConnectionDetailsIfExist(){
        // load connection details from file
        // if file exists, load connection details
        File file = new File("connection_details.txt");
        if(file.exists()){
            // load connection details
            Vector<String> connectionDetails = FileUtils.readFromFile("connection_details.txt");
            assert connectionDetails != null;
            assert connectionDetails.size() == 5;
            String key, value, line;
            Map<String, JTextField> inpWidgetMapping = Map.of(
                    "Host", db_host_text,
                    "Port", db_port_text,
                    "User", db_user_text,
                    "Password", db_pass_text,
                    "Database", db_name_text
            );
            JTextField referenceWidget;
            for (String connectionDetail : connectionDetails) {
                key = connectionDetail.split(":")[0].strip();
                value = connectionDetail.split(":")[1].strip();

                referenceWidget = inpWidgetMapping.get(key);
                referenceWidget.setText(value);
            }
        }
        // if file does not exist, do nothing
    }

    public static void main(String[] args) {
        new main_window();

    }
}
