package ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FindPasswordUI extends JFrame {

    private JPanel contentPane;
    private JTextField idField;
    private JTextField firstnameField;
    private JTextField lastnameField;
    private JTextField emailField;
    private JTextField birthdateField;
    private JTextArea resultTextArea;

    public FindPasswordUI() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(500, 80, 400, 500);
        setResizable(false);
        contentPane = new JPanel();
        contentPane.setBackground(new Color(0, 0, 0));
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblFindPassword = new JLabel("비밀번호 찾기");
        lblFindPassword.setHorizontalAlignment(SwingConstants.CENTER);
        lblFindPassword.setFont(new Font("Nanum Gothic", Font.BOLD, 20));
        lblFindPassword.setForeground(new Color(221, 228, 232));
        lblFindPassword.setBounds(14, 50, 373, 50);
        contentPane.add(lblFindPassword);

        JLabel lblId = new JLabel("아이디 / ID");
        lblId.setForeground(new Color(254, 255, 255));
        lblId.setFont(new Font("Nanum Gothic", Font.PLAIN, 12));
        lblId.setBounds(72, 120, 100, 27);
        contentPane.add(lblId);

        JLabel lblFirstName = new JLabel("이름 / First Name");
        lblFirstName.setForeground(new Color(254, 255, 255));
        lblFirstName.setFont(new Font("Nanum Gothic", Font.PLAIN, 12));
        lblFirstName.setBounds(72, 170, 130, 27);
        contentPane.add(lblFirstName);

        JLabel lblLastName = new JLabel("성 / Last Name");
        lblLastName.setForeground(new Color(254, 255, 255));
        lblLastName.setFont(new Font("Nanum Gothic", Font.PLAIN, 12));
        lblLastName.setBounds(72, 220, 130, 27);
        contentPane.add(lblLastName);

        JLabel lblEmail = new JLabel("이메일 / Email");
        lblEmail.setForeground(new Color(254, 255, 255));
        lblEmail.setFont(new Font("Nanum Gothic", Font.PLAIN, 12));
        lblEmail.setBounds(72, 270, 100, 27);
        contentPane.add(lblEmail);

        JLabel lblBirthdate = new JLabel("생년월일 / Birthdate");
        lblBirthdate.setForeground(new Color(254, 255, 255));
        lblBirthdate.setFont(new Font("Nanum Gothic", Font.PLAIN, 12));
        lblBirthdate.setBounds(72, 320, 130, 27);
        contentPane.add(lblBirthdate);

        idField = new JTextField();
        idField.setFont(new Font("Nanum Gothic", Font.PLAIN, 20));
        idField.setBounds(69, 140, 262, 33);
        contentPane.add(idField);
        idField.setColumns(10);

        firstnameField = new JTextField();
        firstnameField.setFont(new Font("Nanum Gothic", Font.PLAIN, 20));
        firstnameField.setBounds(69, 190, 262, 33);
        contentPane.add(firstnameField);
        firstnameField.setColumns(10);

        lastnameField = new JTextField();
        lastnameField.setFont(new Font("Nanum Gothic", Font.PLAIN, 20));
        lastnameField.setBounds(69, 240, 262, 33);
        contentPane.add(lastnameField);
        lastnameField.setColumns(10);

        emailField = new JTextField();
        emailField.setFont(new Font("Nanum Gothic", Font.PLAIN, 20));
        emailField.setBounds(69, 290, 262, 33);
        contentPane.add(emailField);
        emailField.setColumns(10);

        birthdateField = new JTextField();
        birthdateField.setFont(new Font("Nanum Gothic", Font.PLAIN, 20));
        birthdateField.setBounds(69, 340, 262, 33);
        contentPane.add(birthdateField);
        birthdateField.setColumns(10);

        JButton findPasswordButton = new JButton("비밀번호 재설정하기");
        findPasswordButton.setBackground(new Color(197, 225, 255));
        findPasswordButton.setFont(new Font("Nanum Gothic", Font.BOLD, 14));
        findPasswordButton.setBounds(69, 400, 262, 40);
        contentPane.add(findPasswordButton);

        findPasswordButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String id = idField.getText();
                String firstname = firstnameField.getText();
                String lastname = lastnameField.getText();
                String email = emailField.getText();
                String birthdate = birthdateField.getText();

                try {
                    Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/twit", "root", "David100894@");

                    // Check if the entered information is correct to retrieve the password
                    String query = "SELECT password FROM account WHERE id=? AND firstname=? AND lastname=? AND email=? AND birthdate=?";
                    try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                        preparedStatement.setString(1, id);
                        preparedStatement.setString(2, firstname);
                        preparedStatement.setString(3, lastname);
                        preparedStatement.setString(4, email);
                        preparedStatement.setString(5, birthdate);

                        ResultSet resultSet = preparedStatement.executeQuery();
                        if (resultSet.next()) {
                            String foundPassword = resultSet.getString("password");

                            // Display found password with half as asterisks in a dialog
                            String maskedPassword = maskHalfPassword(foundPassword);
                            JOptionPane.showMessageDialog(FindPasswordUI.this, "찾은 비밀번호: " + maskedPassword, "비밀번호 찾기", JOptionPane.INFORMATION_MESSAGE);
                            setLocationRelativeTo(null);

                            // Ask user if they want to reset the password
                            int option = JOptionPane.showConfirmDialog(FindPasswordUI.this, "비밀번호를 재설정하시겠습니까?", "비밀번호 재설정", JOptionPane.YES_NO_OPTION);
                            setLocationRelativeTo(null);
                            if (option == JOptionPane.YES_OPTION) {
                                // Prompt the user to enter a new password
                                String newPassword = JOptionPane.showInputDialog(FindPasswordUI.this, "새로운 비밀번호를 입력하세요:");
                                setLocationRelativeTo(null);

                                // Check if the entered password is not empty and meets the minimum length requirement
                                if (newPassword != null && newPassword.length() >= 5) {
                                    // Call a method to handle password reset
                                    resetPassword(id, newPassword);
                                    JOptionPane.showMessageDialog(FindPasswordUI.this, "비밀번호가 재설정되었습니다.", "비밀번호 재설정", JOptionPane.INFORMATION_MESSAGE);
                                    setLocationRelativeTo(null);
                                } else {
                                    JOptionPane.showMessageDialog(FindPasswordUI.this, "비밀번호는 최소 5글자 이상이어야 합니다.", "비밀번호 재설정 실패", JOptionPane.ERROR_MESSAGE);
                                    setLocationRelativeTo(null);
                                }
                            }else {
                                dispose();
                            }


                        } else {
                            // Display a message if no matching record is found
                            JOptionPane.showMessageDialog(FindPasswordUI.this, "일치하는 정보를 찾을 수 없습니다.", "비밀번호 찾기", JOptionPane.WARNING_MESSAGE);
                            setLocationRelativeTo(null);
                        }
                    }
                    connection.close();
                } catch (SQLException exception) {
                    exception.printStackTrace();
                }
            }

            // Add a new method to mask half of the password with '*'
            private String maskHalfPassword(String password) {
                int length = password.length();
                int halfLength = length / 2;

                // Create a string of '*' with halfLength characters
                String maskedPart = new String(new char[halfLength]).replace('\0', '*');

                return password.substring(0, halfLength) + maskedPart;
            }


            // Add a new method to handle password reset
            private void resetPassword(String id, String newPassword) {
                try {
                    Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/twit", "root", "David100894@");

                    // Update the password in the database
                    String updateQuery = "UPDATE account SET password=? WHERE id=?";
                    try (PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {
                        updateStatement.setString(1, newPassword);
                        updateStatement.setString(2, id);
                        updateStatement.executeUpdate();
                    }

                    connection.close();
                } catch (SQLException exception) {
                    exception.printStackTrace();
                }
            }

        });
        setLocationRelativeTo(null);
    }
}
