package GUI.loginRegisterGui;

import GUI.MainGUI;
import GUI.stockmanagerGui.StockManagerGUI;
import GUI.supplyGui.SupplierGUI;
import LoginRegister.Business.LoginManager;
import LoginRegister.Business.RegisterManager;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;

public class loginRegisterGUI extends JFrame {

    private JLayeredPane layeredPane;
    private MainGUI mainGUI;
    private JPanel mainPanel;

    private SupplierGUI supplierGUI;
    //    private StoreManagerGUI storeManagerGUI;
    private StockManagerGUI stockManagerGUI;


    //    public LoginRegisterGUI(MainGUI mainGUI) throws IOException {
    public loginRegisterGUI() throws IOException {

        setResizable(false);
        setTitle("SUPER LEE");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 650);

        // Create layered pane
        layeredPane = new JLayeredPane();
        layeredPane.setLayout(new BorderLayout());
        getContentPane().add(layeredPane);



        setLayout(new BorderLayout());

        // Create main panel
        Image background = null;
        try {
            background = ImageIO.read(getClass().getResource("/GUI/pictures/background.jpg"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Image finalBackground = background;
        mainPanel = new JPanel(){
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(finalBackground, 0, 0, getWidth(), getHeight(), this);
            }
        };
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        JLabel titleLabel = new JLabel("<html> Please login / register :</html>");
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setVerticalAlignment(SwingConstants.TOP);
        titleLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 24));
        mainPanel.add(titleLabel, BorderLayout.SOUTH);
        mainPanel.setLayout(new FlowLayout());


        // Create button panel
//        JButton backButton = new JButton("Back");
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.setOpaque(false);

        // Create buttons
        JButton Login = createButton("Login", "/GUI/pictures/add-order.jpg");
        JButton register = createButton("Register", "/GUI/pictures/update.jpg");


        buttonPanel.add(Box.createHorizontalGlue());
        buttonPanel.add(Login);
        buttonPanel.add(Box.createHorizontalStrut(20));
        buttonPanel.add(register);


        JTextField usernameField = new JTextField(15);
        JPasswordField passwordField = new JPasswordField(15);

        usernameField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    Login.doClick();
                }
            }
        });

        passwordField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    Login.doClick();
                }
            }
        });
//        JTextField passwordField = new JTextField(15);
        JPanel textFieldPanel = new JPanel();
        textFieldPanel.setLayout(new FlowLayout());
        textFieldPanel.setOpaque(false);
        textFieldPanel.add(new JLabel("Username: "));
        textFieldPanel.add(usernameField);
        textFieldPanel.add(new JLabel("Password: "));
        textFieldPanel.add(passwordField);

        //////////////
        String[] options = {"Supplier Manager", "Stock Manager", "Store manager"};
        JComboBox<String> comboBox = new JComboBox<>(options);

        // Create panel for combo box
        JPanel comboBoxPanel = new JPanel();
        comboBoxPanel.setLayout(new FlowLayout());
        comboBoxPanel.setOpaque(false);
        comboBoxPanel.add(new JLabel("Select an option: "));
        textFieldPanel.add(comboBox);

        // Add combo box panel to the main panel
//        mainPanel.add(comboBoxPanel, BorderLayout.CENTER);

        //////////////

        mainPanel.add(textFieldPanel, new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));




        // Add button panel to the main panel
        mainPanel.add(Box.createVerticalStrut(120)); // Adjust the spacing as needed
        mainPanel.add(buttonPanel,BorderLayout.CENTER);


        mainPanel.add(Box.createVerticalStrut(200));


        add(mainPanel, BorderLayout.CENTER);

        // Add action listeners for the buttons
        Login.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // login function
                String userName, password, role;
                userName = usernameField.getText();
                char[] pass = passwordField.getPassword();
                password = new String(pass);
                //password = passwordField.getText();
                role = (String)comboBox.getSelectedItem();
                if (!checkIfUsernameValid(userName)) {
                    // raise box of invalid username
                    JOptionPane.showMessageDialog(null, "The username must contains only letters and numbers! ");
                }
                else{
                    LoginManager loginManager = LoginManager.getInstance();
                    String errorMessage = loginManager.login(userName,password,role);
                    if (errorMessage.equals("")) {
                        if (role.toLowerCase().equals("stock manager")) {
                            try {
                                openStockManager();
                            }
                            catch (IOException ex) {
                                throw new RuntimeException(ex);

                            }


                        }
                        else if (role.toLowerCase().equals("supplier manager")) {
                            // show SupplierManagerGUI
                            try {
//                                mainGUI.setVisible(true);
                                openSupplierManager();
                            } catch (IOException ex) {
                                throw new RuntimeException(ex);
                            }
                        }
                        else {
//                          // show storeManagerGUI
                            try {
//                                mainGUI.setVisible(true);
//                                mainGUI.openStoreManager();
                                openStoreManager();
                            } catch (IOException ex) {
                                throw new RuntimeException(ex);
                            }
                        }

                    }
                    else {
//                        System.out.println(errorMessage);
                        // raise box with error message
                        JOptionPane.showMessageDialog(null, errorMessage);

                    }

                }

            }
        });

        register.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //openEditOrderPanel();
                String userName, password, role;
                userName = usernameField.getText();
                char[] pass = passwordField.getPassword();
                password = new String(pass);
                //password = passwordField.getText();
                role = (String)comboBox.getSelectedItem();
                if(role != null)
                    role = role.toLowerCase();
                if (!checkIfUsernameValid(userName)) {
                    // raise box of invalid username
                    JOptionPane.showMessageDialog(null, "The username must contains only letters and numbers! ");
                }
                else{
                    RegisterManager registerManager = RegisterManager.getInstance();
                    if (!registerManager.register(userName, password, role)) {
                        JOptionPane.showMessageDialog(null,"This username already exist! " );

                    }
                    else {
                        JOptionPane.showMessageDialog(null,"Your registration was successful, You can login now! " );

                    }
                }

            }
        });

    }
    private Boolean checkIfUsernameValid(String username) {
        return username.matches("[a-zA-Z0-9]+");
    }
    private JButton createButton(String text, String imagePath) throws IOException {
        // Create button panel
        int width = 150;
        int height = 150;
        JPanel buttonPanel = new JPanel(null);
        buttonPanel.setLayout(new BorderLayout());
//        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0)); // Remove label margin

        // Create image label
        JLabel imageLabel = new JLabel();
        Image image = ImageIO.read(getClass().getResource(imagePath));
        Image small_image = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        ImageIcon imageIcon = new ImageIcon(small_image);
        imageLabel.setIcon(imageIcon);
        imageLabel.setBounds(0,0,width,height);
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        buttonPanel.add(imageLabel, BorderLayout.CENTER);

        // Create text label
        Font buttonFont = new Font("Tahoma", Font.BOLD, 16);
        JLabel textLabel = new JLabel(text);
        textLabel.setFont(buttonFont);
        textLabel.setHorizontalAlignment(SwingConstants.CENTER);
        buttonPanel.add(textLabel, BorderLayout.SOUTH);

        // Create button
        JButton button = new JButton();
        button.setLayout(new BorderLayout());
        button.add(buttonPanel, BorderLayout.CENTER);
        button.setFocusPainted(false);
        button.setVerticalAlignment(SwingConstants.TOP); // Adjust vertical alignment
        button.setVerticalTextPosition(SwingConstants.BOTTOM); // Adjust vertical text position
        button.setHorizontalTextPosition(SwingConstants.CENTER); // Adjust horizontal text position
        button.setMargin(new Insets(0, 0, 0, 0)); // Set the margin to zer


        return button;

    }

    public void run() {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                loginRegisterGUI login;
                try {
                    login = new loginRegisterGUI();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                login.setVisible(true);
            }
        });
    }
    public void openSupplierManager() throws IOException {          // used to be private
        mainPanel.setVisible(false);

        if (supplierGUI == null) {
            supplierGUI = new SupplierGUI(this,null);
            supplierGUI.setPreferredSize(mainPanel.getSize());
            supplierGUI.setMaximumSize(mainPanel.getMaximumSize());
            supplierGUI.setMinimumSize(mainPanel.getMinimumSize());
            supplierGUI.setSize(mainPanel.getSize());
        }
        supplierGUI.setVisible(true);
        add(supplierGUI, BorderLayout.CENTER);
        revalidate();
        repaint();
    }
    public void openStoreManager() throws IOException {             // used to be private
        mainPanel.setVisible(false);

        if (mainGUI == null) {
            mainGUI = new MainGUI(this);
            mainGUI.setPreferredSize(mainPanel.getSize());
            mainGUI.setMaximumSize(mainPanel.getMaximumSize());
            mainGUI.setMinimumSize(mainPanel.getMinimumSize());
            mainGUI.setSize(mainPanel.getSize());
        }
        mainGUI.setVisible(true);
        add(mainGUI, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    public void openStockManager() throws IOException {            // used to be private
        mainPanel.setVisible(false);

        if (stockManagerGUI == null) {
            stockManagerGUI = new StockManagerGUI(this,null);
            stockManagerGUI.setPreferredSize(mainPanel.getSize());
            stockManagerGUI.setMaximumSize(mainPanel.getMaximumSize());
            stockManagerGUI.setMinimumSize(mainPanel.getMinimumSize());
            stockManagerGUI.setSize(mainPanel.getSize());
        }
        stockManagerGUI.setVisible(true);
        add(stockManagerGUI, BorderLayout.CENTER);
        revalidate();
        repaint();
    }
    public void showMainPanel() {
        mainPanel.setVisible(true);
        if (supplierGUI != null) {
            supplierGUI.setVisible(false);
        }
        if (mainGUI != null) {
            mainGUI.setVisible(false);
        }
        if (stockManagerGUI != null) {
            stockManagerGUI.setVisible(false);
        }
        // Hide other screens if necessary
    }
}