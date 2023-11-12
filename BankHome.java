import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.PieSectionLabelGenerator;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.sql.*;
import java.text.*;
import javax.swing.*;
import javax.swing.table.*;
import java.util.Date;

public class BankHome {
    static final ImageIcon AccountImg = new ImageIcon("account.png");
    static JPanel top, rightside, center;
    static JFrame mainframe;
    static ImageIcon icon1 = new ImageIcon("mainlablebank.png");
    static JLabel imageLabel1, imageLabel2, imageLable3, ctime;
    static JButton Accountcenter, accountb, Trasfer, Card, transactions;
    static Connection conn;
    static {
        try {
            conn = DriverManager.getConnection(Main.DB_URL, Main.USER, Main.PASS);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    static JPanel textHis = new JPanel();
    static JPanel TransactionData = new JPanel(null);
    static int AccountNumber, UserContactNumber;
    static double AccBalace = 0;
    static String AccType, AccOpdate, fname, lname, email, uaddress, dob, USERUPIID;
    static JTextField Rec_Bank_no, Bamount, ifsc_code;
    static JPanel TransactionsChart = new JPanel();

    public BankHome()
    // public static void main(String[] args) //To see the frame again and again for
    // testing
    {
        SQLConnection();

        Thread thread = new Thread(() -> {
            while (true) {
                SQLConnection();
                try {
                    Thread.sleep(3000);
                    System.out.println("Reloading Database");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.setDaemon(true); // Set the thread as a daemon
        thread.start();
        mainframe = new JFrame("NATIONAL BANK");
        mainframe.setSize(1300, 700);
        mainframe.setResizable(true);
        mainframe.setLayout(new BorderLayout());
        mainframe.setIconImage(Main.bankIcon.getImage());

        top = new JPanel(new GridLayout(1, 5, 10, 10));
        top.setBackground(Color.white);
        top.setPreferredSize(new Dimension(1200, 120));
        top.setBackground(new Color(236, 88, 0));
        imageLabel1 = new JLabel(icon1);
        imageLabel2 = new JLabel("<html>" +
                "<body>" +
                "<p 'style=font-size:400%;' >NATIONAL</p>" +
                "</body>" +
                "</html>");
        imageLabel2.setFont(new Font("", Font.BOLD, 12));
        imageLabel2.setForeground(Color.BLACK);

        imageLable3 = new JLabel("<html>" +
                "<body>" +
                "<center 'style=font-size:420%;' >BANK</center>" +
                "</body>" +
                "</html>");
        imageLable3.setFont(new Font("", Font.BOLD, 13));
        imageLable3.setForeground(Color.BLACK);

        ctime = new JLabel();
        ctime.setText(" TIME ");
        Timer timer = new Timer(1000, e -> updateTime());
        timer.start();
        ctime.setForeground(new Color(10, 9, 9));

        Accountcenter = new JButton();
        Accountcenter.setIcon(AccountImg);
        Accountcenter.setText("LOG OUT");
        Accountcenter.setFont(new Font("Arian", Font.BOLD, 18));
        Accountcenter.setIconTextGap(-10);
        Accountcenter.setFocusable(false);
        Accountcenter.setBackground(Color.white);
        Accountcenter.setOpaque(false);
        Accountcenter.setBorder(BorderFactory.createLineBorder(Color.WHITE));
        Accountcenter.addActionListener(e -> {
            int i = JOptionPane.showConfirmDialog(mainframe, "Do You Want To Log Out ?", "Log out",
                    JOptionPane.YES_NO_OPTION);
            if (i == JOptionPane.YES_OPTION) {
                mainframe.dispose();
                if (LoginFrame.loginFrame != null) {
                    LoginFrame.loginFrame.setVisible(true);
                } else {
                    SwingUtilities.invokeLater(LoginFrame::new);
                }
            }
        });

        top.add(imageLabel1);
        top.add(imageLabel2);
        top.add(imageLable3);
        top.add(ctime);
        top.add(Accountcenter);

        rightside = new JPanel();
        rightside.setLayout(new FlowLayout(FlowLayout.CENTER, 100, 40));
        rightside.setBackground(new Color(239, 155, 15));
        rightside.setPreferredSize(new Dimension(250, 1200));

        accountb = new JButton("ACCOUNT");
        accountb.setOpaque(true);
        ImageIcon accIcon = new ImageIcon("  account.gif");
        accountb.setFocusable(false);
        accountb.setForeground(new Color(0, 0, 0));
        accountb.setBackground(new Color(229, 228, 226));
        accountb.setIcon(accIcon);
        accountb.setPreferredSize(new Dimension(200, 80));
        accountb.addActionListener(e1 -> AccountButton());

        Trasfer = new JButton("TRANSFER");
        Trasfer.setOpaque(true);
        ImageIcon transfericon = new ImageIcon("  transfer.gif");

        Trasfer.setFocusable(false);
        Trasfer.setForeground(new Color(0, 0, 0));
        Trasfer.setBackground(new Color(229, 228, 226));
        Trasfer.setIcon(transfericon);
        Trasfer.setPreferredSize(new Dimension(200, 80));
        Trasfer.addActionListener(e1 -> TransferButton());

        Card = new JButton(" CARD'S ");
        ImageIcon cardGif = new ImageIcon("  cards.gif");
        JLabel cardgiflabel = new JLabel(cardGif);
        cardgiflabel.setOpaque(false);
        cardgiflabel.setBackground(new Color(229, 228, 226));
        Card.setOpaque(true);
        Card.setFocusable(false);
        Card.setForeground(new Color(0, 0, 0));
        Card.setBackground(new Color(229, 228, 226));
        Card.setPreferredSize(new Dimension(200, 80));
        Card.add(cardgiflabel);
        Card.setIconTextGap(10);
        Card.addActionListener(e -> Cards());

        transactions = new JButton(" TRANSACTION'S ");
        ImageIcon t = new ImageIcon("  transaction-History.gif");
        transactions.setOpaque(true);
        transactions.setFocusable(false);
        transactions.setForeground(new Color(0, 0, 0));
        transactions.setBackground(new Color(229, 228, 226));
        transactions.setPreferredSize(new Dimension(200, 80));
        transactions.setIcon(t);
        transactions.addActionListener(e -> TrasactionsButton());

        rightside.add(accountb);
        rightside.add(Trasfer);
        rightside.add(Card);
        rightside.add(transactions);

        center = new JPanel(null);
        center.setBackground(new Color(255, 248, 220));
        center.setPreferredSize(new Dimension(500, 500));

        mainframe.add(center, BorderLayout.CENTER);
        mainframe.add(rightside, BorderLayout.WEST);
        mainframe.add(top, BorderLayout.NORTH);

        boolean containsSaving = AccType.contains("Savings");
        boolean containsCurrent = AccType.contains("Current");
        boolean containsFixDeposit = AccType.contains("Fix-Deposit");
        if (containsSaving || containsCurrent
                || (containsSaving && containsFixDeposit || containsCurrent && containsFixDeposit)) {
            System.out.println("Program Starts");
        } else if (containsFixDeposit) {
            Card.setEnabled(false);
            Trasfer.setEnabled(false);
        } else {
            JOptionPane.showMessageDialog(null, "Error Occured While Fetching Account Type", "Account Type Error",
                    JOptionPane.ERROR_MESSAGE);
        }

        accountb.doClick();
        mainframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainframe.setVisible(true);

        // Runtime.getRuntime().addShutdownHook(new Thread(() -> {
        // System.out.println("Thank you! See You Soon");
        // }));
    }

    static JLabel firstname = new JLabel();
    static JLabel ABal = new JLabel();
    static JPanel transactionPanel = new JPanel();
    static JTextField receivers_upiId, upiIdamount;
    static JButton confirm, Bankt, Upi, Transfer;
    static String cardnum;
    static JLabel cardNumber;

    private static void updateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd, yyyy");
        SimpleDateFormat dateFormat1 = new SimpleDateFormat("EEEE");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        String formattedDate = dateFormat.format(new Date());
        String formattedDayOfWeek = dateFormat1.format(new Date());
        String formattedTime = timeFormat.format(new Date());

        String htmlText = "<html><center>" + formattedDate + "<br>" + formattedDayOfWeek + "<br>" + formattedTime
                + "</center></html>";
        ctime.setText(htmlText);
        ctime.setVerticalAlignment(JLabel.CENTER);
        ctime.setHorizontalAlignment(JLabel.CENTER);
        ctime.setFont(new Font("Arial", Font.BOLD, 15));
    }

    private static synchronized void AccountButton() {
        center.removeAll();
        center.repaint();
        center.revalidate();

        JLabel uNameLabel, uAddressLabel, uContactNoLabel, uEmailLabel, uDateofBirthLabel;
        JLabel uAccountNoLabel, uAccountBalanceLabel, uAccountType, uAccOpendt;

        JPanel UserInfo = new JPanel(new GridLayout(6, 1));
        UserInfo.setBackground(new Color(255, 248, 220));
        UserInfo.setBounds(18, 18, 420, 480);

        JPanel AccountDetails = new JPanel(null);
        AccountDetails.setBackground(Color.WHITE);
        AccountDetails.setBounds(470, 48, 490, 280);
        AccountDetails.setBorder(BorderFactory.createLineBorder(Color.BLACK, 9));

        uNameLabel = new JLabel(
                "<html><body><center 'style=font-size:120%'>NAME : " + fname + " " + lname + "</center></body></html>");
        uNameLabel.setFont(new Font("Arian", Font.BOLD, 20));
        UserInfo.add(uNameLabel);

        uAddressLabel = new JLabel(
                "<html><body><center 'style=font-size:120%'>ADDRESS : " + uaddress + "</center></body></html>");
        uAddressLabel.setFont(new Font("Arian", Font.BOLD, 15));
        UserInfo.add(uAddressLabel);

        uContactNoLabel = new JLabel("<html><body><center 'style=font-size:120%'>CONTACT NO : " + UserContactNumber
                + "</center></body></html>");
        uContactNoLabel.setFont(new Font("Arian", Font.BOLD, 20));
        UserInfo.add(uContactNoLabel);

        uEmailLabel = new JLabel(
                "<html><body><center 'style=font-size:120%'>EMAIL : " + email + "</center></body></html>");
        uEmailLabel.setFont(new Font("Arian", Font.BOLD, 20));
        UserInfo.add(uEmailLabel);

        uDateofBirthLabel = new JLabel(
                "<html><body><center 'style=font-size:120%'>DATE OF BIRTH : " + dob + "</center></body></html>");
        uDateofBirthLabel.setFont(new Font("Arian", Font.BOLD, 20));
        UserInfo.add(uDateofBirthLabel);

        uAccOpendt = new JLabel("<html><body><center 'style=font-size:120%'>Account Opened at : " + AccOpdate
                + "</center></body></html>");
        uAccOpendt.setFont(new Font("Arian", Font.ITALIC, 20));
        UserInfo.add(uAccOpendt);

        uAccountNoLabel = new JLabel(
                "<html><body><span 'style=font-size:100%'>Account No :  </span><center 'style=font-size:250%'>"
                        + AccountNumber + " </center></body></html>");
        uAccountNoLabel.setFont(new Font("Arian", Font.BOLD, 15));
        uAccountNoLabel.setBounds(25, 15, 160, 150);
        AccountDetails.add(uAccountNoLabel);

        uAccountBalanceLabel = new JLabel(
                "<html><body><span 'style=font-size:100%'>Account Balance :  </span><center 'style=font-size:250%'>"
                        + AccBalace + " </center></body></html>");
        uAccountBalanceLabel.setFont(new Font("Arian", Font.BOLD, 15));
        uAccountBalanceLabel.setBounds(260, 15, 160, 150);
        AccountDetails.add(uAccountBalanceLabel);

        uAccountType = new JLabel(
                "<html><body><span 'style=font-size:99%'>Account Type :  </span><center 'style=font-size:250%'>"
                        + AccType + " </center></body></html>");
        uAccountType.setFont(new Font("Arian", Font.BOLD, 13));
        uAccountType.setBounds(40, 120, 350, 150);
        AccountDetails.add(uAccountType);

        center.add(AccountDetails);
        AccountDetails.revalidate();
        AccountDetails.repaint();
        center.add(UserInfo);

    }

    private static void TransferButton() {
        center.removeAll();
        center.revalidate();
        center.repaint();

        JPanel Tselect = new JPanel(new GridLayout(2, 1, 70, 70));
        Tselect.setBounds(10, 20, 290, 480);
        Tselect.setBackground(new Color(255, 248, 220));
        Tselect.setOpaque(true);

        Upi = new JButton("<html><body><center 'style=font-size:150%;'>UPI</center></body></html>");
        Upi.setPreferredSize(new Dimension(150, 150));
        Upi.setBackground(new Color(255, 248, 220));
        Upi.setForeground(new Color(241, 98, 24));
        Upi.setOpaque(true);
        Upi.setFocusable(false);
        Upi.setFont(new Font("Arian", Font.BOLD, 30));
        Upi.setBorder(BorderFactory.createLineBorder(new Color(150, 150, 111)));
        Upi.addActionListener(a -> SelectUPi());
        Tselect.add(Upi);

        Transfer = new JButton("<html><body><center 'style=font-size:150%;'>Bank Transfer</center></body></html>");
        Transfer.setPreferredSize(new Dimension(150, 150));
        Transfer.setBackground(new Color(255, 248, 220));
        Transfer.setForeground(new Color(241, 98, 24));
        Transfer.setOpaque(true);
        Transfer.setFocusable(false);
        Transfer.setFont(new Font("Arian", Font.BOLD, 30));
        Transfer.addActionListener(E -> SelectBankTransfer());

        Tselect.add(Transfer);

        JPanel SelectedT = new JPanel(new BorderLayout());
        SelectedT.setBounds(310, 10, 650, 530);
        SelectedT.setBackground(Color.BLACK);
        SelectedT.setOpaque(true);

        transactionPanel = new JPanel();
        transactionPanel.setOpaque(true);
        transactionPanel.setBackground(new Color(255, 248, 220));
        transactionPanel.setPreferredSize(new Dimension(200, 200));

        center.add(Tselect);
        center.add(SelectedT);
        SelectedT.add(BorderLayout.CENTER, transactionPanel);

    }

    static private void SelectUPi() {

        Upi.setEnabled(false);
        Transfer.setEnabled(true);
        transactionPanel.removeAll();
        transactionPanel.revalidate();
        transactionPanel.repaint();

        JLabel UpiId = new JLabel(
                "<html><body><p 'style=font-size:150%;'><strong>ENTER SENDER'S UPI ID: </strong></p></body></html>");
        UpiId.setOpaque(true);
        UpiId.setForeground(new Color(1, 1, 1));
        UpiId.setBackground(new Color(255, 248, 220));
        UpiId.setPreferredSize(new Dimension(250, 100));

        receivers_upiId = new JTextField(18);
        receivers_upiId.setFont(new Font("Serif", Font.BOLD, 20));
        receivers_upiId.setSize(100, 40);
        receivers_upiId.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));

        JLabel upiamount = new JLabel("<html><body><center 'style=font-size:150%;'>AMOUNT : </center></body></html>");
        upiamount.setOpaque(true);
        upiamount.setForeground(new Color(1, 1, 1));
        upiamount.setBackground(new Color(255, 248, 220));
        upiamount.setPreferredSize(new Dimension(250, 100));

        upiIdamount = new JTextField(18);
        upiIdamount.setFont(new Font("Serif", Font.BOLD, 20));
        upiIdamount.setSize(100, 40);
        upiIdamount.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));

        JButton upiPay = new JButton("<html><body><center 'style=font-size:150%;'> PAY /- </center></body></html>");
        upiPay.setFont(new Font("cascadia Code Semibold", Font.ITALIC, 24));
        upiPay.setFocusable(false);
        upiPay.setOpaque(true);
        upiPay.setBackground(new Color(255, 255, 255));
        upiPay.setForeground(new Color(226, 184, 61));
        upiPay.addActionListener(E -> UPIPAYMENTWAY());

        transactionPanel.add(UpiId);
        transactionPanel.add(receivers_upiId);
        transactionPanel.add(upiamount);
        transactionPanel.add(upiIdamount);
        transactionPanel.add(upiPay);
    }

    private static void SelectBankTransfer() {
        Transfer.setEnabled(false);
        Upi.setEnabled(true);
        transactionPanel.removeAll();
        transactionPanel.revalidate();
        transactionPanel.repaint();

        JLabel Ba = new JLabel(
                "<html><body><p 'style=font-size:150%;'><strong>ENTER BANK ACCOUNT NUMBER: </strong></p></body></html>");
        Ba.setOpaque(true);
        Ba.setForeground(new Color(1, 1, 1));
        Ba.setBackground(new Color(255, 248, 220));
        Ba.setPreferredSize(new Dimension(250, 100));

        JLabel ifc = new JLabel(
                "<html><body><p 'style=font-size:150%;'><strong>ENTER IFSC CODE : </strong></p></body></html>");
        ifc.setOpaque(true);
        ifc.setForeground(new Color(1, 1, 1));
        ifc.setBackground(new Color(255, 248, 220));
        ifc.setPreferredSize(new Dimension(250, 100));

        Rec_Bank_no = new JTextField(18);
        Rec_Bank_no.setFont(new Font("Serif", Font.BOLD, 20));
        Rec_Bank_no.setSize(100, 40);
        Rec_Bank_no.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));

        ifsc_code = new JTextField(18);
        ifsc_code.setFont(new Font("Serif", Font.BOLD, 20));
        ifsc_code.setSize(100, 40);
        ifsc_code.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));

        JLabel BAm = new JLabel("<html><body><center 'style=font-size:150%;'>AMOUNT : </center></body></html>");
        BAm.setOpaque(true);
        BAm.setForeground(new Color(1, 1, 1));
        BAm.setBackground(new Color(255, 248, 220));
        BAm.setPreferredSize(new Dimension(250, 100));

        Bamount = new JTextField(18);
        Bamount.setFont(new Font("Serif", Font.BOLD, 20));
        Bamount.setSize(100, 40);
        Bamount.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));

        Bankt = new JButton("<html><body><center 'style=font-size:150%;'> PAY /- </center></body></html>");
        Bankt.setFont(new Font("cascadia Code Semibold", Font.ITALIC, 24));
        Bankt.setFocusable(false);
        Bankt.setOpaque(true);
        Bankt.setBackground(new Color(255, 255, 255));
        Bankt.setForeground(new Color(226, 184, 61));

        Bankt.addActionListener(E -> payBank());

        transactionPanel.add(Ba);
        transactionPanel.add(Rec_Bank_no);
        transactionPanel.add(ifc);
        transactionPanel.add(ifsc_code);
        transactionPanel.add(BAm);
        transactionPanel.add(Bamount);
        transactionPanel.add(Bankt);
    }

    private static void UPIPAYMENTWAY() {
        Upi.setEnabled(false);
        Transfer.setEnabled(true);
        transactionPanel.removeAll();
        transactionPanel.revalidate();
        transactionPanel.repaint();

        JPanel upiconfirm = new JPanel();
        upiconfirm.setOpaque(true);
        upiconfirm.setBackground(new Color(255, 248, 220));
        upiconfirm.setPreferredSize(new Dimension(460, 450));

        JLabel pin = new JLabel(
                "<html><body><center 'style=font-size:120%;'>ENTER YOUR UPI PIN : </center></body></html>");
        pin.setForeground(Color.BLACK);
        pin.setFont(new Font("Arian", Font.BOLD, 14));
        pin.setOpaque(true);
        pin.setBackground(new Color(255, 248, 220));
        pin.setPreferredSize(new Dimension(200, 100));

        JPasswordField upiPIN = new JPasswordField(9);
        upiPIN.setBackground(new Color(205, 189, 189));
        upiPIN.setBorder(BorderFactory.createLineBorder(Color.black, 3));
        upiPIN.setPreferredSize(new Dimension(200, 40));
        upiPIN.setFont(new Font("Arian", Font.BOLD, 18));

        confirm = new JButton("<html><body><center 'style=font-size:120%;'> CONFIRM  </center></body></html>");
        confirm.setPreferredSize(new Dimension(200, 120));
        confirm.setBackground(new Color(255, 248, 220));
        confirm.setForeground(new Color(241, 98, 24));
        confirm.setOpaque(true);
        confirm.setFocusable(false);
        confirm.setFont(new Font("Arian", Font.BOLD, 30));
        confirm.addActionListener(E -> payupi());

        upiconfirm.add(pin);
        upiconfirm.add(upiPIN);
        upiconfirm.add(confirm);
        transactionPanel.add(upiconfirm);
    }

    static private void payupi() {
        confirm.setEnabled(false);
        int amount = Integer.parseInt(upiIdamount.getText());
        String ID = receivers_upiId.getText();
        if (amount > AccBalace) {
            JOptionPane.showMessageDialog(null, "Insufficient Balance", "BALANCE", JOptionPane.ERROR_MESSAGE);
        } else {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                int count = 0;
                try (Connection conn = DriverManager.getConnection(Main.DB_URL, Main.USER, Main.PASS)) {
                    String st1 = "SELECT COUNT(UserUpiId) As Isthere FROM Accounts WHERE UserUpiId = ?";
                    try (PreparedStatement isthere = conn.prepareStatement(st1)) {
                        isthere.setString(1, ID);

                        try (ResultSet rs = isthere.executeQuery()) {
                            if (rs.next()) {
                                count = rs.getInt("Isthere");
                            }
                            if (count == 0) {
                                String insertTransactionQuery = "INSERT INTO transactions(AccountNumber, TransactionType, Amount, TransactionDateTime, Balancetype, Bank_Type, Senders_Upi_id) VALUES (?, 'Upi', ?, NOW(), 0, 0, ?)";
                                try (PreparedStatement insertTransactionStmt = conn
                                        .prepareStatement(insertTransactionQuery)) {
                                    insertTransactionStmt.setInt(1, AccountNumber);
                                    insertTransactionStmt.setInt(2, amount);
                                    insertTransactionStmt.setString(3, ID);
                                    int insertResult = insertTransactionStmt.executeUpdate();

                                    // Update account balance
                                    String updateBalanceQuery = "UPDATE accounts SET balance = balance - ? WHERE accountnumber = ?";
                                    try (PreparedStatement updateBalanceStmt = conn
                                            .prepareStatement(updateBalanceQuery)) {
                                        updateBalanceStmt.setInt(1, amount);
                                        updateBalanceStmt.setInt(2, AccountNumber);
                                        int updateResult = updateBalanceStmt.executeUpdate();

                                        if (insertResult > 0 && updateResult > 0) {
                                            JOptionPane.showMessageDialog(null, "Payment Successful", "Sent",
                                                    JOptionPane.PLAIN_MESSAGE);
                                        } else {
                                            JOptionPane.showMessageDialog(null, "Failed to process the payment",
                                                    "Error", JOptionPane.ERROR_MESSAGE);
                                        }
                                    }
                                }

                            } else if (count == 1) {
                                String f2 = "SELECT AccountNumber AS AccNum FROM accounts WHERE UserUpiId = ?";
                                int r_acc_no = 0;
                                try (PreparedStatement f = conn.prepareStatement(f2)) {
                                    f.setString(1, ID);
                                    ResultSet accquery = f.executeQuery();
                                    if (accquery.next()) {
                                        r_acc_no = accquery.getInt("AccNum");
                                    }
                                    String updateQuery1 = "UPDATE accounts SET balance = balance + ? WHERE accountnumber = ?";
                                    try (PreparedStatement updateStatement = conn.prepareStatement(updateQuery1)) {
                                        updateStatement.setInt(1, amount);
                                        updateStatement.setInt(2, r_acc_no);
                                        updateStatement.executeUpdate();
                                    } catch (SQLException e) {
                                        System.out.println("updateQuery1\n");
                                        e.printStackTrace();
                                    }

                                    String updateQuery2 = "UPDATE accounts SET balance = balance - ? WHERE accountnumber = ?";
                                    try (PreparedStatement updateStatement = conn.prepareStatement(updateQuery2)) {
                                        updateStatement.setInt(1, amount);
                                        updateStatement.setInt(2, AccountNumber);
                                        updateStatement.executeUpdate();
                                    } catch (SQLException e) {
                                        e.printStackTrace();
                                    }
                                    String insertQuery = "INSERT INTO transactions (AccountNumber, TransactionType, Amount, TransactionDateTime, Balancetype, Remark, Bank_Type, Senders_Upi_id) VALUES (?, 'Upi', ?, NOW(), 0, 'UPI Payment', 1, ?)";
                                    try (PreparedStatement insertStatement1 = conn.prepareStatement(insertQuery)) {
                                        // Replace with the actual account number, amount, and upiId
                                        insertStatement1.setInt(1, AccountNumber);
                                        insertStatement1.setInt(2, amount);
                                        insertStatement1.setString(3, ID);
                                        insertStatement1.executeUpdate();
                                    } catch (SQLException e) {
                                        JOptionPane.showMessageDialog(null, "Error", "Error",
                                                JOptionPane.ERROR_MESSAGE);
                                        e.printStackTrace();
                                    }

                                    String insertQuery2 = "INSERT INTO transactions (AccountNumber, TransactionType, Amount, TransactionDateTime, Balancetype, Remark, Bank_Type, Senders_Upi_id) VALUES (?, 'Upi', ?, NOW(), 1 , 'UPI Payment', 1, ?)";
                                    try (PreparedStatement insertStatement2 = conn.prepareStatement(insertQuery2)) {
                                        insertStatement2.setInt(1, r_acc_no);
                                        insertStatement2.setInt(2, amount);
                                        insertStatement2.setString(3, USERUPIID);
                                        insertStatement2.executeUpdate();

                                        JOptionPane.showMessageDialog(null, "Payment SuccessFull", "Payment",
                                                JOptionPane.PLAIN_MESSAGE);
                                    } catch (SQLException e) {
                                        e.printStackTrace();
                                    }

                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                            } else if (count >= 2) {
                                JOptionPane.showMessageDialog(null, "An unexpected error occured", "Error 404",
                                        JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(null, "An error occurred while processing the UPI payment", "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
            accountb.doClick();
        }
    }

    static private void payBank() {

        Bankt.setEnabled(false);
        String amountText = Bamount.getText();
        double amount;
        if (!amountText.isEmpty()) {
            amount = Double.parseDouble(amountText);
        } else {
            amount = 0;
            JOptionPane.showMessageDialog(null, "Please enter a valid amount", "Error", JOptionPane.ERROR_MESSAGE);
        }

        int Bno = Integer.parseInt(Rec_Bank_no.getText());
        String ifsc = ifsc_code.getText();
        if (amount > AccBalace) {
            JOptionPane.showMessageDialog(null, "Insufficient Balance", "BALANCE", JOptionPane.ERROR_MESSAGE);
        } else {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                int count = 0;
                try (Connection conn = DriverManager.getConnection(Main.DB_URL, Main.USER, Main.PASS)) {

                    String st1 = "SELECT COUNT(AccountNumber) As Isthere FROM Accounts WHERE AccountNumber = ?";
                    try (PreparedStatement isthere = conn.prepareStatement(st1)) {
                        isthere.setInt(1, Bno);

                        try (ResultSet rs = isthere.executeQuery()) {
                            if (rs.next()) {
                                count = rs.getInt("Isthere");
                            }
                            if (count == 0) {
                                String insertTransactionQuery = "INSERT INTO transactions(AccountNumber, TransactionType, Amount, TransactionDateTime, Balancetype, Bank_Type, Senders_acc_no,ifsc) VALUES (?, 'Bank-Transfer', ?, NOW(), 0, 0, ?,?)";
                                try (PreparedStatement insertTransactionStmt = conn
                                        .prepareStatement(insertTransactionQuery)) {
                                    insertTransactionStmt.setInt(1, AccountNumber);
                                    insertTransactionStmt.setDouble(2, amount);
                                    insertTransactionStmt.setInt(3, Bno);
                                    insertTransactionStmt.setString(4, ifsc);
                                    int insertResult = insertTransactionStmt.executeUpdate();

                                    // Update account balance
                                    String updateBalanceQuery = "UPDATE accounts SET balance = balance - ? - 3.75 WHERE accountnumber = ?";
                                    try (PreparedStatement updateBalanceStmt = conn
                                            .prepareStatement(updateBalanceQuery)) {
                                        updateBalanceStmt.setDouble(1, amount);
                                        updateBalanceStmt.setInt(2, AccountNumber);
                                        int updateResult = updateBalanceStmt.executeUpdate();

                                        if (insertResult > 0 && updateResult > 0) {
                                            accountb.doClick();
                                            JOptionPane.showMessageDialog(null, "Payment Successful", "Sent",
                                                    JOptionPane.PLAIN_MESSAGE);

                                        } else {
                                            JOptionPane.showMessageDialog(null, "Failed to process the payment",
                                                    "Error", JOptionPane.ERROR_MESSAGE);
                                        }
                                    }
                                }

                            } else if (count == 1) {
                                String f2 = "SELECT AccountNumber AS AccNum FROM accounts WHERE AccountNumber = ?";
                                int r_acc_no = 0;
                                try (PreparedStatement f = conn.prepareStatement(f2)) {
                                    f.setInt(1, Bno);
                                    ResultSet accquery = f.executeQuery();
                                    if (accquery.next()) {
                                        r_acc_no = accquery.getInt("AccNum");
                                    }
                                    String updateQuery1 = "UPDATE accounts SET balance = balance + ? WHERE accountnumber = ?";
                                    try (PreparedStatement updateStatement = conn.prepareStatement(updateQuery1)) {
                                        updateStatement.setDouble(1, amount);
                                        updateStatement.setInt(2, r_acc_no);
                                        updateStatement.executeUpdate();
                                    } catch (SQLException e) {
                                        System.out.println("updateQuery1\n");
                                        e.printStackTrace();
                                    }

                                    String updateQuery2 = "UPDATE accounts SET balance = balance - ? WHERE accountnumber = ?";
                                    try (PreparedStatement updateStatement = conn.prepareStatement(updateQuery2)) {
                                        updateStatement.setDouble(1, amount);
                                        updateStatement.setInt(2, AccountNumber);
                                        updateStatement.executeUpdate();
                                    } catch (SQLException e) {
                                        e.printStackTrace();
                                    }

                                    // INSERT statement
                                    String insertQuery = "INSERT INTO transactions (AccountNumber, TransactionType, Amount, TransactionDateTime, Balancetype, Remark, Bank_Type, Senders_acc_no,ifsc) VALUES (?, 'Bank-Transfer', ?, NOW(), 0, 'Bank-Transfer', 1, ?,'NA09090');";
                                    try (PreparedStatement insertStatement1 = conn.prepareStatement(insertQuery)) {

                                        insertStatement1.setInt(1, AccountNumber);
                                        insertStatement1.setDouble(2, amount);
                                        insertStatement1.setInt(3, r_acc_no);

                                        insertStatement1.executeUpdate();
                                    } catch (SQLException e) {
                                        e.printStackTrace();
                                    }

                                    String insertQuery2 = "INSERT INTO transactions (AccountNumber, TransactionType, Amount, TransactionDateTime, Balancetype, Remark, Bank_Type, Senders_acc_no,ifsc) VALUES (?, 'Bank-Transfer', ?, NOW(), 1 , 'Bank-Transfer', 1, ?,'NA09090');";
                                    try (PreparedStatement insertStatement2 = conn.prepareStatement(insertQuery2)) {
                                        insertStatement2.setInt(1, r_acc_no);
                                        insertStatement2.setDouble(2, amount);
                                        insertStatement2.setInt(3, AccountNumber);

                                        insertStatement2.executeUpdate();

                                        JOptionPane.showMessageDialog(null, "Payment SuccessFull", "Payment",
                                                JOptionPane.PLAIN_MESSAGE);
                                    } catch (SQLException e) {
                                        e.printStackTrace();
                                    }

                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                            } else if (count >= 2) {
                                JOptionPane.showMessageDialog(null, "An unexpected error occured", "Error 404",
                                        JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(null, "An error occurred while processing the UPI payment", "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "An error occurred while processing the payment", "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    static private void Cards() {
        center.removeAll();
        center.repaint();
        center.revalidate();

        JPanel showcard = new JPanel(null);
        showcard.setBounds(460, 20, 400, 280);
        showcard.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0), 8));
        showcard.setBorder(BorderFactory.createLineBorder(Color.orange, 8, true));
        ImageIcon card = new ImageIcon("  Card.png");
        JLabel cardBackground = new JLabel(card);

        cardBackground.setOpaque(true);
        cardBackground.setBounds(0, 0, 400, 280);

        JLabel cardnolable = new JLabel("CARD NUMBER : \n");
        cardnolable.setBounds(150, 11, 120, 15);

        cardNumber = new JLabel(String.valueOf(cardnum));
        cardNumber.setBounds(69, 30, 250, 50);
        cardNumber.setFont(new Font("Arian", Font.BOLD, 35));

        JLabel exp = new JLabel("Valid Till : ");
        exp.setFont(new Font("Arian", Font.PLAIN, 16));
        exp.setBounds(42, 121, 100, 50);

        JLabel expdate = new JLabel("05/29");
        expdate.setFont(new Font("Arian", Font.BOLD, 25));
        expdate.setBounds(42, 150, 100, 55);

        JLabel cv = new JLabel("CVV : ");
        cv.setFont(new Font("Arian", Font.PLAIN, 16));
        cv.setBounds(200, 121, 100, 50);

        JButton cvv = new JButton("***");
        cvv.setOpaque(true);
        cvv.setFocusable(false);
        cvv.setBackground(new Color(244, 67, 67));
        cvv.setBounds(190, 161, 100, 30);
        cvv.setBorder(BorderFactory.createLineBorder(Color.black, 5, true));
        final int r = 99 + ((int) (Math.random() * 800));
        cvv.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String currenttext = cvv.getText();
                if (currenttext.equals("***")) {
                    cvv.setText(String.valueOf(r));
                    cvv.setFont(new Font("Arian", Font.BOLD, 15));
                } else {
                    cvv.setText("***");
                }
            }
        });

        ImageIcon rupay = new ImageIcon("  rupay.png");

        Image image = rupay.getImage();
        BufferedImage bufferedImage = new BufferedImage(image.getWidth(null), image.getHeight(null),
                BufferedImage.TYPE_INT_ARGB);
        Graphics g = bufferedImage.createGraphics();
        g.drawImage(image, 0, 0, null);
        g.dispose();

        JLabel rupaylogo = new JLabel(rupay);
        rupaylogo.setBounds(290, 192, 100, 50);
        showcard.add(cardnolable);
        showcard.add(cardNumber);
        showcard.add(exp);
        showcard.add(expdate);
        showcard.add(cv);
        showcard.add(cvv);
        showcard.add(rupaylogo);
        showcard.add(cardBackground);
        center.add(showcard);

        textHis = new JPanel();
        textHis.setBounds(15, 90, 320, 400);
        textHis.setBackground(new Color(255, 248, 220));

        center.add(textHis);

        JPanel History = new JPanel();
        JLabel T = new JLabel();

        JPanel CardChartPanel = new JPanel();
        JButton cardhis = new JButton("SHOW HISTORY");

        cardhis.setOpaque(true);
        cardhis.setFocusable(false);
        cardhis.setBackground(new Color(108, 255, 176));
        cardhis.setForeground(new Color(254, 151, 52));
        cardhis.setBorder(BorderFactory.createLineBorder(Color.BLACK, 10, true));
        cardhis.setBounds(10, 10, 150, 60);

        cardhis.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String currentText = cardhis.getText();

                if (currentText.equals("SHOW HISTORY")) {
                    cardhis.setText("SHOW CARD");
                    showcard.setVisible(false);
                    textHis.setVisible(false);
                    center.repaint();
                    center.revalidate();

                    History.setVisible(true);
                    History.setBounds(30, 110, 500, 500);
                    History.setBackground(new Color(241, 248, 217));

                    String f1 = "select TransactionID, TransactionType, Amount, TransactionDateTime, Remark from transactions where TransactionType='CARD' AND accountNumber = ? ;";
                    String c = "Select COUNT(TransactionID) FROM Transactions where AccountNumber = ? AND TransactionType='CARD';";
                    int count = 0;
                    try {
                        Class.forName(Main.JDBC_DRIVER);

                        try (Connection conn = DriverManager.getConnection(Main.DB_URL, Main.USER, Main.PASS)) {
                            try (PreparedStatement co = conn.prepareStatement(c)) {
                                co.setInt(1, AccountNumber);
                                ResultSet cou = co.executeQuery();
                                while (cou.next()) {
                                    count = cou.getInt("COUNT(TransactionID)");
                                }
                            }
                            try (PreparedStatement cardT = conn.prepareStatement(f1)) {
                                cardT.setInt(1, AccountNumber);

                                try (ResultSet resultSet = cardT.executeQuery()) {
                                    String[][] data = new String[count][5];
                                    int i = 0; // Initialize row index
                                    while (resultSet.next()) {
                                        data[i][0] = String.valueOf(resultSet.getInt("TransactionID"));
                                        data[i][1] = resultSet.getString("TransactionType");
                                        data[i][2] = String.valueOf(resultSet.getDouble("Amount"));
                                        data[i][3] = String.valueOf(resultSet.getTimestamp("TransactionDateTime"));
                                        data[i][4] = resultSet.getString("Remark");

                                        i++; // Increment row index
                                    }

                                    String[] columnNames = { "TransactionID", "TransactionType", "Amount",
                                            "TransactionDateTime", "Remark" };

                                    DefaultTableModel tableModel = new DefaultTableModel(data, columnNames);
                                    JTable table = new JTable(tableModel);
                                    table.setRowHeight(45);
                                    table.setEnabled(false);

                                    JScrollPane scrollPane = new JScrollPane(table);
                                    scrollPane.setPreferredSize(new Dimension(499, 499));
                                    History.removeAll();
                                    History.add(scrollPane);
                                    History.revalidate();
                                    History.repaint();

                                }
                            }
                        }
                    } catch (ClassNotFoundException | SQLException exception) {
                        exception.printStackTrace();
                    }

                    CardChartPanel.setBackground(new Color(255, 248, 220));
                    // CardChartPanel.setBackground(Color.BLACK);
                    CardChartPanel.setBounds(600, 110, 650, 500);
                    DefaultCategoryDataset dataset = createDatasetCards();

                    JFreeChart chart = ChartFactory.createLineChart(
                            "Card Expense", // title
                            "Date", // X-Axis Label
                            "Amount", // Y-Axis Label
                            dataset, PlotOrientation.VERTICAL, true, true, true);

                    ChartPanel panel = new ChartPanel(chart);
                    panel.setPreferredSize(new Dimension(640, 490));

                    CardChartPanel.setVisible(true);
                    CardChartPanel.add(panel);
                    center.add(History);
                    center.add(CardChartPanel);
                } else {
                    center.repaint();
                    center.revalidate();
                    cardhis.setText("SHOW HISTORY");
                    History.setVisible(false);
                    showcard.setVisible(true);
                    textHis.setVisible(true);
                    CardChartPanel.setVisible(false);

                }
            }

        });
        String f1 = "select TransactionID, TransactionType, Amount, TransactionDateTime, Remark from transactions where TransactionType='CARD' AND accountNumber = ? ;";
        String c = "Select COUNT(TransactionID) FROM Transactions where AccountNumber = ? AND TransactionType='CARD';";
        int count = 0;
        try {
            Class.forName(Main.JDBC_DRIVER);

            try (Connection conn = DriverManager.getConnection(Main.DB_URL, Main.USER, Main.PASS)) {
                try (PreparedStatement co = conn.prepareStatement(c)) {
                    co.setInt(1, AccountNumber);
                    ResultSet cou = co.executeQuery();
                    while (cou.next()) {
                        count = cou.getInt("COUNT(TransactionID)");
                    }
                }
                try (PreparedStatement cardT = conn.prepareStatement(f1)) {
                    cardT.setInt(1, AccountNumber);

                    try (ResultSet resultSet = cardT.executeQuery()) {
                        String[][] data = new String[count][5];
                        int i = 0; // Initialize row index
                        while (resultSet.next()) {
                            data[i][0] = String.valueOf(resultSet.getInt("TransactionID"));
                            data[i][1] = String.valueOf(resultSet.getDouble("Amount"));
                            data[i][2] = resultSet.getString("Remark");

                            i++; // Increment row index
                        }

                        String[] columnNames = { "TransactionID", "Amount", "Remark" };

                        DefaultTableModel tableModel = new DefaultTableModel(data, columnNames);
                        JTable table = new JTable(tableModel);
                        table.setRowHeight(45);
                        table.setEnabled(false);

                        JScrollPane scrollPane = new JScrollPane(table);
                        scrollPane.setPreferredSize(new Dimension(320, 400));
                        textHis.removeAll();
                        textHis.add(scrollPane);
                        textHis.revalidate();
                        textHis.repaint();

                    }
                }
            }
        } catch (ClassNotFoundException | SQLException exception) {
            exception.printStackTrace();
        }
        center.add(cardhis);
    }

    private static DefaultCategoryDataset createDatasetCards() {

        String series1 = "EXPENDS";

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        String f1 = "select TransactionID, TransactionType, Amount, TransactionDateTime, Remark from transactions where TransactionType='CARD' AND accountNumber = ? ;";
        String c = "Select COUNT(TransactionID) FROM Transactions where AccountNumber = ? AND TransactionType='CARD';";
        try {
            Class.forName(Main.JDBC_DRIVER);
            int count = 0;
            try (Connection conn = DriverManager.getConnection(Main.DB_URL, Main.USER, Main.PASS)) {
                try (PreparedStatement co = conn.prepareStatement(c)) {
                    co.setInt(1, AccountNumber);
                    ResultSet cou = co.executeQuery();
                    while (cou.next()) {
                        count = cou.getInt("COUNT(TransactionID)");
                    }
                }
                try (PreparedStatement cardT = conn.prepareStatement(f1)) {
                    cardT.setInt(1, AccountNumber);
                    try (ResultSet resultSet = cardT.executeQuery()) {
                        String[] data2 = new String[count];
                        int[] data1 = new int[count];
                        int i = 0;
                        while (resultSet.next() && i < count) {
                            data1[i] = (int) resultSet.getDouble("Amount");
                            data2[i] = String.valueOf(resultSet.getTimestamp("TransactionDateTime"));
                            dataset.addValue(data1[i], series1, data2[i]);
                            i++;
                        }
                    }
                }
            }
        } catch (ClassNotFoundException | SQLException exception) {
            exception.printStackTrace();
        }
        return dataset;
    }

    static JPanel exp = new JPanel();
    static JPanel exp1 = new JPanel();
    static JPanel rec = new JPanel();
    static JPanel rec1 = new JPanel();
    static JPanel pie3d = new JPanel();
    static JPanel piePanel = new JPanel();

    private static void TrasactionsButton() {
        center.removeAll();
        center.repaint();
        center.revalidate();

        ImageIcon table = new ImageIcon("  Table.png");
        ImageIcon Graph = new ImageIcon("  Graph.png");

        JButton Switch = new JButton();
        Switch.setOpaque(true);
        Switch.setFocusable(false);
        Switch.setIcon(table);
        Switch.setBorderPainted(true);
        Switch.setBackground(new Color(255, 248, 220));
        Switch.setBounds(560, 1, 100, 85);
        Switch.setBorder(BorderFactory.createLineBorder(Color.black, 2, true));

        Switch.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JPanel graphs = new JPanel(null);
                if (Switch.getIcon() == Graph) {
                    Switch.setIcon(table);
                    pie3d.setVisible(false);
                    TransactionData.removeAll();
                    TransactionData.repaint();

                    graphs.setBounds(0, 0, 1250, 580);
                    graphs.setBackground(Color.gray);

                    ImageIcon barcharticon = new ImageIcon("  barcharticon.png");
                    JButton Bar = new JButton();
                    Bar.setIcon(barcharticon);
                    Bar.setOpaque(true);
                    Bar.setFocusable(false);
                    Bar.setBounds(230, 10, 60, 60);
                    Bar.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            exp.setVisible(false);
                            pie3d.setVisible(false);
                            piePanel.setVisible(false);
                            rec.setVisible(false);
                            graphs.repaint();
                            graphs.revalidate();

                            exp1 = new JPanel();
                            rec1 = new JPanel();
                            rec1.setBounds(625, 90, 600, 470);
                            exp1.setBounds(20, 90, 600, 470);

                            CategoryDataset dataset = BarfetchTransactionData(false);

                            JFreeChart chart = ChartFactory.createBarChart("Expend", "Types", "Total Amount", dataset,
                                    PlotOrientation.VERTICAL, true, true, false);

                            ChartPanel chartPanel = new ChartPanel(chart);
                            chartPanel.setPreferredSize(new Dimension(600, 400));

                            exp1.add(chartPanel);

                            CategoryDataset dataset1 = BarfetchTransactionData(true);

                            JFreeChart chart1 = ChartFactory.createBarChart("Incomes", "Types", "Total Amount",
                                    dataset1, PlotOrientation.VERTICAL, true, true, false);

                            ChartPanel chartPanel1 = new ChartPanel(chart1);
                            chartPanel1.setPreferredSize(new Dimension(600, 400));

                            rec1.add(chartPanel1);
                            graphs.add(exp1);
                            graphs.add(rec1);
                        }
                    });

                    ImageIcon Linecharticon = new ImageIcon("  linecharticon.png");
                    JButton Line = new JButton();
                    Line.setIcon(Linecharticon);
                    Line.setOpaque(true);
                    Line.setFocusable(false);
                    Line.setBounds(580, 10, 60, 60);
                    Line.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            rec1.setVisible(false);
                            piePanel.setVisible(false);
                            pie3d.setVisible(false);
                            exp1.setVisible(false);
                            TransactionData.repaint();
                            TransactionData.revalidate();
                            graphs.repaint();
                            graphs.revalidate();

                            exp = new JPanel();
                            rec = new JPanel();

                            XYSeriesCollection dataset = new XYSeriesCollection();
                            JFreeChart chart = ChartFactory.createXYLineChart("Transaction Type", "Transaction ID",
                                    "Amount", dataset, PlotOrientation.VERTICAL, true, true, false);
                            ChartPanel chartPanel = new ChartPanel(chart);

                            JButton upiButton = new JButton("UPI");
                            upiButton.setFocusable(false);
                            upiButton.setOpaque(true);
                            upiButton.setBackground(new Color(150, 150, 111));

                            JButton bankTransferButton = new JButton("Bank Transfer");
                            bankTransferButton.setBackground(new Color(150, 150, 111));
                            bankTransferButton.setFocusable(false);
                            bankTransferButton.setOpaque(true);

                            JButton cardButton = new JButton("Card");
                            cardButton.setOpaque(true);
                            cardButton.setFocusable(false);
                            cardButton.setBackground(new Color(150, 150, 111));

                            JButton AllExp = new JButton("ALL");
                            AllExp.setOpaque(true);
                            AllExp.setFocusable(false);
                            AllExp.setBackground(new Color(150, 150, 111));

                            upiButton.addActionListener(
                                    ef -> updateChart("Upi", dataset, chart, false, "Expenditure Through Upi"));
                            bankTransferButton.addActionListener(ed -> updateChart("Bank-Transfer", dataset, chart,
                                    false, "Expenditure Through Bank-Transfer"));
                            cardButton.addActionListener(
                                    er -> updateChart("CARD", dataset, chart, false, "Expenditure Through CARD"));
                            AllExp.addActionListener(ae -> updateChart(
                                    "SELECT TransactionID, Amount FROM Transactions WHERE AccountNumber = ? AND BalanceType = 0;",
                                    dataset, chart, "ALL Expenditure's"));

                            JPanel buttonPanel = new JPanel();
                            buttonPanel.add(upiButton);
                            buttonPanel.add(bankTransferButton);
                            buttonPanel.add(cardButton);
                            buttonPanel.add(AllExp);

                            exp.add(buttonPanel, BorderLayout.NORTH);
                            exp.add(chartPanel, BorderLayout.CENTER);

                            XYSeriesCollection dataset1 = new XYSeriesCollection();
                            JFreeChart chart1 = ChartFactory.createXYLineChart("Transaction Type", "Transaction ID",
                                    "Amount", dataset1, PlotOrientation.VERTICAL, true, true, false);
                            ChartPanel chartPanel1 = new ChartPanel(chart1);

                            JButton upiButton1 = new JButton("UPI");
                            upiButton1.setFocusable(false);
                            upiButton1.setOpaque(true);
                            upiButton1.setBackground(new Color(150, 150, 111));

                            JButton bankTransferButton1 = new JButton("Bank Transfer");
                            bankTransferButton1.setBackground(new Color(150, 150, 111));
                            bankTransferButton1.setFocusable(false);
                            bankTransferButton1.setOpaque(true);

                            JButton AllExp1 = new JButton("ALL");
                            AllExp1.setOpaque(true);
                            AllExp1.setFocusable(false);
                            AllExp1.setBackground(new Color(150, 150, 111));

                            upiButton1.addActionListener(
                                    ef -> updateChart("Upi", dataset1, chart1, true, "Received In Upi Id"));
                            bankTransferButton1.addActionListener(ed -> updateChart("Bank-Transfer", dataset1, chart1,
                                    true, "Received Through Bank Transfer"));
                            AllExp1.addActionListener(ae -> updateChart(
                                    "SELECT TransactionID, Amount FROM Transactions WHERE AccountNumber = ? AND BalanceType = 1;",
                                    dataset1, chart1, "Received By Total"));

                            JPanel buttonPanel1 = new JPanel();
                            buttonPanel1.add(upiButton1);
                            buttonPanel1.add(bankTransferButton1);
                            buttonPanel1.add(AllExp1);

                            chartPanel1.setPreferredSize(new Dimension(550, 420));
                            rec.add(buttonPanel1, BorderLayout.NORTH);
                            rec.add(chartPanel1, BorderLayout.CENTER);
                            rec.add(chartPanel1);

                            exp.setBounds(20, 90, 600, 470);
                            exp.setBackground(Color.gray);
                            chartPanel.setPreferredSize(new Dimension(550, 420));
                            exp.add(chartPanel);

                            rec.setBounds(625, 90, 600, 470);
                            rec.setBackground(Color.gray);

                            graphs.add(exp);
                            graphs.add(rec);
                        }
                    });

                    ImageIcon Piecharticon = new ImageIcon("  piechartIcon.png");
                    JButton Pie = new JButton();
                    Pie.setIcon(Piecharticon);
                    Pie.setOpaque(true);
                    Pie.setFocusable(false);
                    Pie.setBounds(950, 10, 60, 60);
                    Pie.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            exp.setVisible(false);
                            pie3d.setVisible(false);
                            rec.setVisible(false);
                            rec1.setVisible(false);
                            exp1.setVisible(false);
                            graphs.repaint();
                            graphs.revalidate();

                            PieDataset dataset = PiecreateDataset();

                            JFreeChart chart = ChartFactory.createPieChart(
                                    "Transaction Volume by Category",
                                    dataset,
                                    true,
                                    true,
                                    false);

                            // Customize the appearance of the chart
                            chart.setBackgroundPaint(Color.WHITE);

                            // Customize the label format
                            PiePlot plot = (PiePlot) chart.getPlot();
                            plot.setLabelGenerator(createLabelGenerator());
                            plot.setLabelOutlinePaint(null);
                            plot.setLabelShadowPaint(null);

                            ChartPanel chartPanel = new ChartPanel(chart);

                            piePanel = new JPanel();
                            piePanel.setBounds(200, 110, 850, 450);
                            piePanel.setBackground(Color.gray);
                            chartPanel.setPreferredSize(new Dimension(750, 440));
                            piePanel.add(chartPanel);
                            graphs.add(piePanel);

                        }
                    });

                    JFreeChart chart = createChart3D(createDataset3D());
                    ChartPanel chartPanel = new ChartPanel(chart);
                    chartPanel.setPreferredSize(new java.awt.Dimension(800, 450));
                    pie3d = new JPanel();

                    pie3d.setBounds(250, 100, 800, 550);
                    pie3d.add(chartPanel);
                    pie3d.setBackground(Color.gray);

                    graphs.add(Bar);
                    graphs.add(Line);
                    graphs.add(Pie);
                    graphs.add(pie3d);

                    TransactionData.add(graphs);

                } else if (Switch.getIcon() == table) {
                    pie3d.setVisible(false);
                    center.repaint();
                    TransactionData.removeAll();
                    center.revalidate();

                    Switch.setIcon(Graph);

                    int Rowcount = 0;
                    String s1 = "select COUNT(TransactionID) from Transactions WHERE ACCOUNTNUMBER = ?;";

                    try {
                        Class.forName(Main.JDBC_DRIVER);
                        try (Connection connect = DriverManager.getConnection(Main.DB_URL, Main.USER, Main.PASS)) {

                            try (PreparedStatement p1 = connect.prepareStatement(s1)) {
                                p1.setInt(1, AccountNumber);
                                ResultSet demo1 = p1.executeQuery();
                                if (demo1.next()) {
                                    Rowcount = demo1.getInt("COUNT(TransactionID)");
                                }
                                // System.out.println(Rowcount);
                            }
                            String s2 = "select TransactionID, TransactionType, Amount, TransactionDateTime, Balancetype, Remark, ifsc, Senders_acc_no, Senders_Upi_id from Transactions WHERE ACCOUNTNUMBER = ? ORDER BY TransactionID desc";
                            try (PreparedStatement p2 = connect.prepareStatement(s2)) {
                                p2.setInt(1, AccountNumber);

                                String[][] Hdata = new String[Rowcount][9];

                                ResultSet rs = p2.executeQuery();
                                ResultSetMetaData metaData = rs.getMetaData();
                                int numColumns = metaData.getColumnCount();
                                int f = 0;
                                while (rs.next()) {
                                    for (int i = 1; i <= numColumns; i++) {
                                        Hdata[f][i - 1] = rs.getString(i);
                                        if (i == 5) {
                                            int balanceType = rs.getInt("Balancetype");
                                            Hdata[f][i - 1] = (balanceType == 0) ? "Sent/Spend" : "Received";
                                        } else {
                                            Hdata[f][i - 1] = rs.getString(i);
                                        }

                                    }
                                    f++;

                                }
                                String[] columnHeaders = new String[numColumns];
                                for (int i = 1; i <= numColumns; i++) {
                                    columnHeaders[i - 1] = metaData.getColumnName(i);
                                }

                                /*
                                 * System.out.println("Column headers: " + Arrays.toString(columnHeaders));
                                 * for (String[] datum : Hdata) {
                                 * for (String s : datum) {
                                 * System.out.print(s + "\t");
                                 * }
                                 * System.out.println();
                                 * }
                                 */

                                TT(Hdata, columnHeaders);
                            }
                        }
                    } catch (SQLException | ClassNotFoundException exception) {
                        exception.printStackTrace();
                    }
                }
            }
        });

        center.add(Switch);
        Switch.doClick();
        TransactionData.add(TransactionsChart);
        center.add(TransactionData);

    }

    static void updateChart(String Type, XYSeriesCollection dataset, JFreeChart chart, boolean AccBalType,
            String Ctitle) {
        XYSeries series = fetchTransactionData(Type, AccBalType);

        dataset.removeAllSeries();
        dataset.addSeries(series);

        chart.getXYPlot().setDataset(dataset);
        chart.setTitle(Ctitle);
    }

    static void updateChart(String query, XYSeriesCollection dataset, JFreeChart chart, String Ctitle) {
        XYSeries series = fetchTransactionData(query);
        dataset.removeAllSeries();
        dataset.addSeries(series);

        chart.getXYPlot().setDataset(dataset);
        chart.setTitle(Ctitle);
    }

    static XYSeries fetchTransactionData(String query) {
        XYSeries series = new XYSeries("ALL");

        try (Connection connection = DriverManager.getConnection(Main.DB_URL, Main.USER, Main.PASS);
                PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, AccountNumber);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    int transactionID = resultSet.getInt("TransactionID");
                    double amount = resultSet.getDouble("Amount");
                    series.add(transactionID, amount);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return series;
    }

    static XYSeries fetchTransactionData(String Type, Boolean AccBalType) {
        XYSeries series = new XYSeries(Type);

        try (Connection connection = DriverManager.getConnection(Main.DB_URL, Main.USER, Main.PASS);
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "SELECT TransactionID, Amount FROM Transactions WHERE AccountNumber = ? AND TransactionType = ? AND BalanceType = ?;")) {
            preparedStatement.setInt(1, AccountNumber);
            preparedStatement.setString(2, Type);
            preparedStatement.setBoolean(3, AccBalType);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    int transactionID = resultSet.getInt("TransactionID");
                    double amount = resultSet.getDouble("Amount");
                    series.add(transactionID, amount);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return series;
    }

    static void TT(String[][] Hdata, String[] columnHeaders) {
        TransactionData.setBackground(new Color(255, 248, 220));
        TransactionData.setBounds(20, 90, 1250, 600);
        DefaultTableModel model = new DefaultTableModel(Hdata, columnHeaders);
        JTable table = new JTable(model);
        JScrollPane sp = new JScrollPane(table);
        table.setDragEnabled(false);
        table.setEnabled(false);
        table.setRowHeight(45);
        sp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        sp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        sp.setPreferredSize(new Dimension(1250, 580));
        sp.setBounds(0, 0, 1245, 580);
        center.repaint();
        center.revalidate();
        TransactionData.add(sp);
        center.add(TransactionData);
    }

    static private CategoryDataset BarfetchTransactionData(boolean Type) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        try (Connection connection = DriverManager.getConnection(Main.DB_URL, Main.USER, Main.PASS);
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "SELECT TransactionType, SUM(Amount) as TotalAmount FROM Transactions WHERE AccountNumber = ? AND BalanceType = ? GROUP BY TransactionType;");) {
            preparedStatement.setInt(1, AccountNumber);
            preparedStatement.setBoolean(2, Type);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    String transType = resultSet.getString("TransactionType");
                    double totalAmount = resultSet.getDouble("TotalAmount");
                    dataset.addValue(totalAmount, "Transaction Amount", transType);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return dataset;
    }

    private synchronized void SQLConnection() {

        try {
            Class.forName(Main.JDBC_DRIVER);

            String qu = "SELECT * FROM customers WHERE CustomerID = ?;";
            String acc = "SELECT * FROM ACCOUNTS WHERE CustomerID = ?;";
            try (Connection conn = DriverManager.getConnection(Main.DB_URL, Main.USER, Main.PASS);
                    PreparedStatement st = conn.prepareStatement(qu)) {

                st.setString(1, Main.enteredUsername);

                PreparedStatement aco = conn.prepareStatement(acc);
                aco.setString(1, Main.enteredUsername);

                ResultSet accounts = aco.executeQuery();
                ResultSet ud = st.executeQuery();

                if (ud.next()) {
                    fname = ud.getString("FirstName");
                    lname = ud.getString("LastName");
                    dob = String.valueOf(ud.getDate("Dateofbirth"));
                    uaddress = ud.getString("Address");
                    UserContactNumber = ud.getInt("ContactNumber");
                    email = ud.getString("Email");
                    firstname.setText(
                            "<html><body><center 'style=font-size:330%;'> " + fname + " </center></body></html>");

                }
                if (accounts.next()) {
                    AccountNumber = accounts.getInt("AccountNumber");
                    AccType = accounts.getString("AccountType");
                    AccBalace = accounts.getDouble("Balance");
                    AccOpdate = String.valueOf(accounts.getDate("DateOpened"));
                    ABal.setText("<html><body><center style='font-size:160%'>" + AccBalace + "</center></body></html>");
                    USERUPIID = accounts.getString("USERUPIID");
                    cardnum = accounts.getString("Card_no");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static private PieDataset PiecreateDataset() {
        DefaultPieDataset dataset = new DefaultPieDataset();

        try (Connection connection = DriverManager.getConnection(Main.DB_URL, Main.USER, Main.PASS);
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "SELECT TransactionType, SUM(Amount) as TotalAmount FROM Transactions WHERE AccountNumber = ?  GROUP BY TransactionType;");) {
            preparedStatement.setInt(1, AccountNumber);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    String transType = resultSet.getString("TransactionType");
                    double totalAmount = resultSet.getDouble("TotalAmount");
                    dataset.setValue(transType, totalAmount);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return dataset;
    }

    private static PieSectionLabelGenerator createLabelGenerator() {
        NumberFormat format = NumberFormat.getNumberInstance();
        format.setMaximumFractionDigits(2);
        format.setMinimumFractionDigits(2);

        return new StandardPieSectionLabelGenerator(
                "{0} = {1}%", format, NumberFormat.getPercentInstance());
    }

    private static DefaultPieDataset createDataset3D() {
        DefaultPieDataset dataset = new DefaultPieDataset();

        try (Connection connection = DriverManager.getConnection(Main.DB_URL, Main.USER, Main.PASS);
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "SELECT SUM(t.Amount) AS TotalTransactionAmount, a.Balance " +
                                "FROM transactions t " +
                                "JOIN accounts a ON t.AccountNumber = a.AccountNumber " +
                                "WHERE t.AccountNumber = ?")) {

            preparedStatement.setInt(1, AccountNumber);

            try (java.sql.ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    double totalTransactionAmount = resultSet.getDouble("TotalTransactionAmount");
                    double balance = resultSet.getDouble("Balance");

                    dataset.setValue("Total Transaction Amount", totalTransactionAmount);
                    dataset.setValue("Account Balance", balance);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return dataset;
    }

    static private JFreeChart createChart3D(DefaultPieDataset dataset) {
        return ChartFactory.createPieChart3D(
                "Transaction Summary",
                dataset,
                true,
                true,
                false);
    }
}
