import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;

public class Main {
    protected static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    protected static final String DB_URL = "jdbc:mysql://localhost:3306/Bank?useSSL=false";
    protected static final String USER = "root";
    protected static final String PASS = "Jill@admin123";
    static String enteredUsername, enteredPassword;
    static JFrame frame;
    static private double userbal = 0;
    static ImageIcon bankIcon = new ImageIcon("D:\\Codes\\Bank\\BankMain\\src\\mainlablebank.png");
    static String Firsnameofuser = "";

    public static void main(String[] args) {

        LoginFrame f = new LoginFrame();
    }

    private static void openHomePage() {

        BankHome home = new BankHome();

        LoginFrame.loginFrame.dispose();
    }

    static public void LoginAuth() {
        enteredUsername = LoginFrame.userTextField.getText();
        char[] enteredPasswordChars = LoginFrame.passwordField.getPassword();
        enteredPassword = new String(enteredPasswordChars);
        boolean Islogin = false;
        if (enteredPassword.isEmpty() || enteredUsername.isEmpty()) {
            JOptionPane.showMessageDialog(frame,
                    "Entered Username or Password is empty",
                    "INVALID INPUT",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            Class.forName(JDBC_DRIVER);
            Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);

            String sql = "SELECT c.CustomerID, c.FirstName, a.Balance FROM Customers c JOIN Accounts a ON c.CustomerID = a.CustomerID WHERE c.CustomerID= ? and c.customerpass= ? ;";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, enteredUsername);
            stmt.setString(2, enteredPassword);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {

                Islogin = true;
                Firsnameofuser = rs.getString("FirstName");
                userbal = rs.getDouble("Balance");

                // This is for debugging perpose
                /*
                 * JOptionPane.showMessageDialog(frame,
                 * "Login Successful! Welcome, User " + enteredUsername,
                 * "SUCCESS",
                 * JOptionPane.INFORMATION_MESSAGE);
                 */

            } else {
                // Account not found
                JOptionPane.showMessageDialog(frame,
                        "Account not found",
                        "ERROR",
                        JOptionPane.ERROR_MESSAGE);
            }

            if (Islogin) {
                // Opens the Account Home Page
                Main.openHomePage();
            }

        } catch (SQLException | ClassNotFoundException ex) {
            throw new RuntimeException(ex);
        }
    }

}
