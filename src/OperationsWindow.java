package src;

import src.db_connector.sql_main;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;
import java.awt.event.ActionEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

public class OperationsWindow extends JDialog {
    private JPanel main_panel;
    private JButton executeButton;
    private JPanel top_panel;
    private JPanel bottom_panel;
    private JTextField textField_table_name;
    private JComboBox<String> comboBox1;
    private JTable table_mysql_data;
    private JScrollPane tableScrollPane;
    private final sql_main sql_connector;

    public OperationsWindow(JFrame parent, sql_main sql_connector) {
        super(parent, "Operations Window", true);
        setContentPane(main_panel);
        setSize(500, 350);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        init_UI();

        this.sql_connector = sql_connector;  // sql connector instance with credentials loaded

        // add action listeners to buttons
        executeButton.addActionListener(this::executeButtonActionPerformed);
    }

    private void init_UI() {
        // set up combo box
        String[] items = {"Fetch All Rows", "Count Rows", "Filtered Select"};
        comboBox1.setModel(new DefaultComboBoxModel<>(items));
        tableScrollPane.setViewportView(table_mysql_data);
    }

    private void executeButtonActionPerformed(ActionEvent e) {
        // get selected option index from combo box
        int selected_index = comboBox1.getSelectedIndex();
        if (selected_index == 0) {
            // fetch all rows
            fetchAllRows();
        } else if (selected_index == 1) {
            // count rows
            countRows();
        } else if (selected_index == 2) {
            // filtered select
            filteredSelect();
        }
    }

    private void fetchAllRows(){
        // get table name
        String table_name = textField_table_name.getText();
        // get data from database
        try{
            ResultSet table_data = sql_connector.execute_query("SELECT * FROM " + table_name);
            // create table model
            DefaultTableModel table_model = new DefaultTableModel();
            table_model.setColumnIdentifiers(sql_connector.getColumnNames(table_name));

            Vector<String> columnNames = sql_connector.getColumnNames(table_name);
            TableColumnModel columnModel = table_mysql_data.getColumnModel();
            for (int i = 0; i < columnModel.getColumnCount(); i++) {
                columnModel.getColumn(i).setHeaderValue(columnNames.get(i));
            }

            table_mysql_data.setTableHeader(new JTableHeader(columnModel));
            table_mysql_data.getTableHeader().setResizingAllowed(false);

            // add data to table model
            while (table_data.next()) {
                Vector<String> row = new Vector<>();
                for (int i = 1; i <= table_data.getMetaData().getColumnCount(); i++) {
                    row.add(table_data.getString(i));
                }
                table_model.addRow(row);
            }
            // set table model to table
            table_mysql_data.setModel(table_model);
        }
        catch (SQLException ex){
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void countRows() {
        // get table name
        String table_name = textField_table_name.getText();
        // get data from database
        int data = sql_connector.countRows(table_name);
        // show data in table as 1 row with 1 column
        DefaultTableModel table_model = new DefaultTableModel();
        table_model.setColumnIdentifiers(new String[]{"Count"});
        table_model.addRow(new String[]{String.valueOf(data)});
        table_mysql_data.setModel(table_model);
    }

    private void filteredSelect() {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
