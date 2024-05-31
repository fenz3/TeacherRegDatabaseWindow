import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class TeacherDatabaseWindow extends JFrame {

    private JTextField searchField;
    private JTable table;
    private DefaultTableModel model;

    public TeacherDatabaseWindow() {
        setTitle("Teachers Database");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(null);

        // Заголовок
        JLabel titleLabel = new JLabel("TEACHERS DATABASE");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setBounds(180, 10, 300, 30);
        add(titleLabel);

        // Кнопка Back
        JButton backButton = new JButton("BACK");
        backButton.setBounds(20, 10, 100, 30);
        add(backButton);
        
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        // Поле пошуку
        searchField = new JTextField();
        searchField.setBounds(150, 50, 200, 30);
        add(searchField);

        // Кнопка пошуку
        JButton searchButton = new JButton("SEARCH");
        searchButton.setBounds(370, 50, 100, 30);
        add(searchButton);
        
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchTeachers();
            }
        });

        // Кнопка видалення
        JButton deleteButton = new JButton("DELETE");
        deleteButton.setBounds(480, 50, 100, 30);
        add(deleteButton);
        
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteTeacher();
            }
        });

        // Таблиця
        String[] columnNames = {"ID", "Teacher Name", "Department", "Year of Birth", "Year of Experience", "Marital Status"};
        model = new DefaultTableModel(new String[0][0], columnNames);
        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(20, 100, 560, 250);
        add(scrollPane);

        // Завантаження даних
        loadTeachers();

        setVisible(true);
    }

    private void loadTeachers() {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/teacherdb", "root", "")) {
            String query = "SELECT * FROM teachers";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            model.setRowCount(0);
            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getString("teachername"),
                        rs.getString("department"),
                        rs.getInt("yearofbirth"),
                        rs.getInt("yearofexperience"),
                        rs.getString("maritialstatus")
                });
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void searchTeachers() {
        String maritialStatus = searchField.getText();
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/teacherdb", "root", "")) {
            String query = "SELECT * FROM teachers WHERE maritialstatus=?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, maritialStatus);
            ResultSet rs = stmt.executeQuery();
            model.setRowCount(0);
            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getString("teachername"),
                        rs.getString("department"),
                        rs.getInt("yearofbirth"),
                        rs.getInt("yearofexperience"),
                        rs.getString("maritialstatus")
                });
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void deleteTeacher() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            int id = (int) table.getValueAt(selectedRow, 0);
            try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/teacherdb", "root", "")) {
                String query = "DELETE FROM teachers WHERE id=?";
                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setInt(1, id);
                stmt.executeUpdate();
                loadTeachers();
                JOptionPane.showMessageDialog(this, "Teacher deleted successfully!");
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error deleting teacher.");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a teacher to delete.");
        }
    }

    public static void main(String[] args) {
        new TeacherDatabaseWindow();
    }
}
