import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;

public class Main implements ActionListener {
    protected static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    protected static final String DB_URL = "jdbc:mysql://localhost:3306/Bank?useSSL=false";
    protected static final String USER = "root";
    protected static final String PASS = "";  // Put Your Own Password
    static String enteredUsername;
    static String enteredPassword;
    static JFrame frame;
    static private double userbal = 0;
    static ImageIcon bankIcon = new ImageIcon("mainlablebank.png");
    static String Firsnameofuser = "";

    public static void main(String[] args) {

        LoginFrame f = new LoginFrame();

    }

    private static void openHomePage() {
        SwingUtilities.invokeLater(() -> {
            BankHome home = new BankHome();
            if (LoginFrame.loginFrame != null) {
                LoginFrame.loginFrame.dispose();
            }
        });
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

    public void actionPerformed(ActionEvent e) {
    }
}

class LoginFrame extends JFrame implements ActionListener {
    static LoginFrame loginFrame;
    static ImageIcon eyeClosed = new ImageIcon("eye-opened.png");
    static ImageIcon eyeOpen = new ImageIcon("eye-closed.png");
    Container container = getContentPane();
    static JLabel userLabel = new JLabel("USER ID : ");
    static JLabel passwordLabel = new JLabel("PASSWORD : ");
    static JTextField userTextField = new JTextField(15);
    static JPasswordField passwordField = new JPasswordField(15);
    static JButton loginButton = new JButton("LOGIN");
    static JButton resetButton = new JButton("RESET");
    static JCheckBox showPassword = new JCheckBox("Show Password");
    static JLabel backgroundLabel;

    LoginFrame() {
        loginFrame = this;
        setLayoutManager();
        setLocationAndSize();
        ImageIcon backgroundIcon = new ImageIcon("Bank.png");
        backgroundLabel = new JLabel(backgroundIcon);
        backgroundLabel.setBounds(0, 0, 1400, 700);
        container.add(backgroundLabel);
        loginButton.addActionListener(this);
        resetButton.addActionListener(this);
        showPassword.addActionListener(this);
        addComponentsToContainer();
        this.setFocusable(true);
        this.setIconImage(Main.bankIcon.getImage());
        this.setTitle("Login Form");
        this.setVisible(true);
        userTextField.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                int modifiers = e.getModifiersEx();
                int keyCode = e.getKeyCode();

                if ((modifiers & KeyEvent.CTRL_DOWN_MASK) != 0 && keyCode == KeyEvent.VK_R) {
                    resetButton.doClick();
                } else if (keyCode == KeyEvent.VK_ENTER) {
                    loginButton.doClick();
                }
            }
        });

        passwordField.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                int modifiers = e.getModifiersEx();
                int keyCode = e.getKeyCode();

                if ((modifiers & KeyEvent.CTRL_DOWN_MASK) != 0 && keyCode == KeyEvent.VK_R) {
                    resetButton.doClick();
                } else if (keyCode == KeyEvent.VK_ENTER) {
                    loginButton.doClick();
                }
            }
        });
        this.setBounds(10, 10, 1400, 700);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
    }

    public void setLayoutManager() {
        container.setLayout(null);
    }

    public void setLocationAndSize() {
        userLabel.setBounds(480, 280, 180, 30);
        passwordLabel.setBounds(480, 340, 180, 30);
        userTextField.setBounds(620, 280, 160, 30);
        passwordField.setBounds(620, 340, 160, 30);
        showPassword.setBounds(620, 390, 150, 30);
        loginButton.setBounds(480, 450, 130, 40);
        resetButton.setBounds(650, 450, 130, 40);

    }

    public void addComponentsToContainer() {
        backgroundLabel.setLayout(null);
        backgroundLabel.add(userLabel);
        backgroundLabel.add(passwordLabel);
        backgroundLabel.add(userTextField);
        backgroundLabel.add(passwordField);
        backgroundLabel.add(showPassword);
        backgroundLabel.add(loginButton);
        backgroundLabel.add(resetButton);

        userLabel.setFont(new Font("Arian", Font.BOLD, 18));
        passwordLabel.setFont(new Font("Arian", Font.BOLD, 18));

        userTextField.setOpaque(true);
        userTextField.setBorder(BorderFactory.createLineBorder(getForeground(), 5));
        userTextField.setFont(new Font("Arian", Font.BOLD, 17));

        passwordField.setOpaque(false);
        passwordField.setBorder(BorderFactory.createLineBorder(getForeground(), 5));
        passwordField.setFont(new Font("Arian", Font.BOLD, 17));

        showPassword.setOpaque(true);
        showPassword.setBackground(new Color(231, 231, 231));
        showPassword.setFocusable(false);
        showPassword.setIcon(eyeOpen);
        showPassword.setSelectedIcon(eyeClosed);

        loginButton.setFocusable(false);
        loginButton.setOpaque(true);
        loginButton.setForeground(new Color(241, 98, 24));
        loginButton.setBackground(Color.white);
        loginButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
        loginButton.addActionListener(e -> Main.LoginAuth());

        resetButton.setBackground(Color.white);
        resetButton.setForeground(new Color(241, 98, 24));
        resetButton.setFocusable(false);
        resetButton.setOpaque(true);
        resetButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));

    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == resetButton) {
            userTextField.setText("");
            passwordField.setText("");
        } else if (e.getSource() == showPassword) {
            passwordField.setEchoChar(showPassword.isSelected() ? (char) 0 : '*');
        }
    }

}
