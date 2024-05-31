import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class TeacherRegistrationForm extends JFrame {

    private JTextField nameField, idField, birthField, expField;
    private JComboBox<String> deptComboBox, statusComboBox;

    public TeacherRegistrationForm() {
        // Створення основного фрейму
        setTitle("Teachers Registration");
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        // Заголовок
        JLabel titleLabel = new JLabel("KNEU TEACHERS");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setBounds(160, 10, 200, 30);
        add(titleLabel);

        // Поля введення та мітки
        JLabel nameLabel = new JLabel("Teacher Name");
        nameLabel.setBounds(20, 60, 100, 20);
        add(nameLabel);
        
        nameField = new JTextField();
        nameField.setBounds(150, 60, 200, 20);
        add(nameField);

        JLabel idLabel = new JLabel("ID");
        idLabel.setBounds(370, 60, 20, 20);
        add(idLabel);
        
        idField = new JTextField();
        idField.setBounds(400, 60, 50, 20);
        add(idField);

        JLabel deptLabel = new JLabel("Department");
        deptLabel.setBounds(20, 90, 100, 20);
        add(deptLabel);
        
        String[] departments = {"Informatics and Systemology", "Mathematics", "Physics", "Chemistry"};
        deptComboBox = new JComboBox<>(departments);
        deptComboBox.setBounds(150, 90, 200, 20);
        add(deptComboBox);

        JLabel birthLabel = new JLabel("Year of Birth");
        birthLabel.setBounds(20, 120, 100, 20);
        add(birthLabel);
        
        birthField = new JTextField();
        birthField.setBounds(150, 120, 200, 20);
        add(birthField);

        JLabel expLabel = new JLabel("Year of experience");
        expLabel.setBounds(20, 150, 130, 20);
        add(expLabel);
        
        expField = new JTextField();
        expField.setBounds(150, 150, 200, 20);
        add(expField);

        JLabel statusLabel = new JLabel("Marital status");
        statusLabel.setBounds(20, 180, 100, 20);
        add(statusLabel);
        
        String[] statusOptions = {"Married", "Single", "Divorsed","Widoved"};
        statusComboBox = new JComboBox<>(statusOptions);
        statusComboBox.setBounds(150, 180, 200, 20);
        add(statusComboBox);

        // Кнопки
        JButton addButton = new JButton("ADD");
        addButton.setBounds(380, 100, 100, 30);
        add(addButton);

        JButton dbButton = new JButton("DATABASE");
        dbButton.setBounds(380, 140, 100, 30);
        add(dbButton);

        JButton exitButton = new JButton("EXIT");
        exitButton.setBounds(380, 180, 100, 30);
        add(exitButton);

        // Додавання обробника подій для кнопки виходу
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        
        // Додавання обробника подій для кнопки додавання
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addTeacherToDatabase();
            }
        });
        
        // Додавання обробника подій для кнопки DATABASE
        dbButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new TeacherDatabaseWindow();
            }
        });

        // Відображення фрейму
        setVisible(true);
    }

    private void addTeacherToDatabase() {
        String teacherName = nameField.getText();
        String department = (String) deptComboBox.getSelectedItem();
        int yearOfBirth = Integer.parseInt(birthField.getText());
        int yearOfExperience = Integer.parseInt(expField.getText());
        String maritialStatus = (String) statusComboBox.getSelectedItem();

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/teacherdb", "root", "")) {
            String query = "INSERT INTO teachers (teachername, department, yearofbirth, yearofexperience, maritialstatus) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, teacherName);
            stmt.setString(2, department);
            stmt.setInt(3, yearOfBirth);
            stmt.setInt(4, yearOfExperience);
            stmt.setString(5, maritialStatus);
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Teacher added successfully!");
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error adding teacher to database.");
        }
    }

    public static void main(String[] args) {
        new TeacherRegistrationForm();
    }
}
