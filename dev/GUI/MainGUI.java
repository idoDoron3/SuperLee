package GUI;

import GUI.loginRegisterGui.loginRegisterGUI;
import GUI.stockmanagerGui.StockManagerGUI;
import GUI.storeGui.StoreManagerGUI;
import GUI.supplyGui.SupplierGUI;
import Stock.Presentation.StockMainUI;
import Stock.Service.ProductService;
import Supplier_Module.Service.SupplierService;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;

public class MainGUI extends JPanel {

    private loginRegisterGUI parent;

    private JLayeredPane layeredPane;
    private JPanel mainPanel;
    private SupplierGUI supplierGUI;
    private StoreManagerGUI storeManagerGUI;
    private StockManagerGUI stockManagerGUI;

    static LocalDate localDate;

    static int dayDiff = 0;
    StockMainUI stockMainUi = new StockMainUI();


    public MainGUI(loginRegisterGUI loginRegisterGUI) throws IOException {
        ProductService.getInstance().setProductManager();
        localDate = LocalDate.now();

        parent = loginRegisterGUI;
        setLayout(new BorderLayout());

        // Create main panel
        Image background = null;
        try {
            background = ImageIO.read(getClass().getResource("/GUI/pictures/background.jpg"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Image finalBackground = background;
        mainPanel = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(finalBackground, 0, 0, getWidth(), getHeight(), this);
            }
        };
        mainPanel.setLayout(new BorderLayout());
        JLabel titleLabel = new JLabel("<html><br>Store manager <br> Please select option :</html>");
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 24));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Create buttons
        JButton supplierManagerButton = createButton("suppliers Relation", "/GUI/pictures/supply.JPG");
        JButton storeManagerButton = createButton("Store Manager", "/GUI/pictures/store-manager.jpg");
        JButton stockManagerButton = createButton("Stock Manager", "/GUI/pictures/stock-manager.jpg");

        JPanel menuPanel = new JPanel();
        int verticalGap = 35; // Set the desired vertical gap between rows
        int horizontalGap = 15;
        menuPanel.setLayout(new GridLayout(1, 3, horizontalGap, verticalGap));
        menuPanel.add(supplierManagerButton);
        menuPanel.add(storeManagerButton);
        menuPanel.add(stockManagerButton);

        menuPanel.setOpaque(false);

        JPanel mainWrapperPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        mainWrapperPanel.add(menuPanel);
        mainWrapperPanel.setBorder(BorderFactory.createEmptyBorder(120, 0, 0, 0)); // 10 is the top padding
        mainPanel.add(mainWrapperPanel, BorderLayout.CENTER);

        mainWrapperPanel.setOpaque(false);

        JButton backButton = new JButton("Disconnect");
        JButton nextDayButton = new JButton("Next Day");
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonsPanel.add(nextDayButton);
        buttonsPanel.add(backButton);


        buttonsPanel.setOpaque(false);
        buttonsPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0)); // 10 is the top padding

        mainPanel.add(buttonsPanel, BorderLayout.SOUTH);

        add(mainPanel, BorderLayout.CENTER);

        // Add action listeners
        supplierManagerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    openSupplierManager();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        storeManagerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    openStoreManager();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        stockManagerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    openStockManager();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                parent.showMainPanel();

            }
        });

        nextDayButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //
                stockMainUi.updateForNextDay(localDate);
                executeNextDay();
                String todayDate = localDate.toString();
                JOptionPane.showMessageDialog(null, "Today is: " + todayDate);
            }
        });
    }

    private JButton createButton(String text, String imagePath) throws IOException {
        // Create button panel
        int width = 172;
        int height = 150;
        JPanel buttonPanel = new JPanel(null);
        buttonPanel.setLayout(new BorderLayout());

        // Create image label
        JLabel imageLabel = new JLabel();
        Image image = ImageIO.read(getClass().getResource(imagePath));
        Image small_image = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        ImageIcon imageIcon = new ImageIcon(small_image);
        imageLabel.setIcon(imageIcon);
        imageLabel.setBounds(0, 0, width, height);
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        buttonPanel.add(imageLabel, BorderLayout.CENTER);

        // Create text label
        Font buttonFont = new Font("Tahoma", Font.BOLD, 12);
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
        button.setMargin(new Insets(0, 0, 0, 0)); // Set the margin to zero

        return button;
    }

    private void openSupplierManager() throws IOException {
        mainPanel.setVisible(false);

        if (supplierGUI == null) {
            supplierGUI = new SupplierGUI(null, this);
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

    private void openStoreManager() throws IOException {
        mainPanel.setVisible(false);

        if (storeManagerGUI == null) {
            storeManagerGUI = new StoreManagerGUI(this);
            storeManagerGUI.setPreferredSize(mainPanel.getSize());
            storeManagerGUI.setMaximumSize(mainPanel.getMaximumSize());
            storeManagerGUI.setMinimumSize(mainPanel.getMinimumSize());
            storeManagerGUI.setSize(mainPanel.getSize());
        }
        storeManagerGUI.setVisible(true);
        add(storeManagerGUI, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    private void openStockManager() throws IOException {
        mainPanel.setVisible(false);

        if (stockManagerGUI == null) {
            stockManagerGUI = new StockManagerGUI(null, this);
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
        if (storeManagerGUI != null) {
            storeManagerGUI.setVisible(false);
        }
        if (stockManagerGUI != null) {
            stockManagerGUI.setVisible(false);
        }
    }

    private void executeNextDay(){
        localDate = LocalDate.now().plusDays(++dayDiff);
        HashMap<String,Integer> orderArrived = SupplierService.getSupplierService().SupplyNextDay(localDate);
        ProductService.getInstance().addMoreItemsToProductsFromSupplier(orderArrived);
        System.out.println("today: " + localDate.toString());
    }
}