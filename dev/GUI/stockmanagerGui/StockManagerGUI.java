package GUI.stockmanagerGui;

import GUI.MainGUI;
import GUI.loginRegisterGui.loginRegisterGUI;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Scanner;
import Stock.Business.Chain;
import Stock.Business.Market;
import Stock.Business.MarketManager;
import Stock.Business.ProductManager;
import Stock.Service.ProductService;
import Stock.DataAccess.ProductDetailsDAO;

public class StockManagerGUI extends JPanel {
    private loginRegisterGUI loginRegisterGUI;
    private MainGUI mainGUI;
    private JPanel mainPanel;
    private OrderManagementGui orderManagementGui;
    private StockManagement stockManagement;

    public StockManagerGUI(loginRegisterGUI loginRegisterGUI, MainGUI mainGUI) throws IOException {
        startMenu();
        this.mainGUI = mainGUI;
        this.loginRegisterGUI = loginRegisterGUI;
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
        mainPanel.setLayout(new BorderLayout());
        JLabel titleLabel = new JLabel("<html><br> Welcome to Stock Management <br> Please select option :</html>");
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 24));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Create buttons
        JButton stockManagementButton = createButton("Stock Management", "/GUI/pictures/stock-manager.jpg");
        JButton orderButton = createButton("Order Management", "/GUI/pictures/order_manager.jpg");

        JPanel menuPanel = new JPanel();
        int verticalGap = 35; // Set the desired vertical gap between rows
        int horizontalGap = 45;
        menuPanel.setLayout(new GridLayout(1, 2, horizontalGap, verticalGap));
        menuPanel.add(stockManagementButton);
        //menuPanel.add(reportsMenu);
        menuPanel.add(orderButton);

        menuPanel.setOpaque(false);

        JPanel mainWrapperPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        mainWrapperPanel.add(menuPanel);
        mainWrapperPanel.setBorder(BorderFactory.createEmptyBorder(120, 0, 0, 0)); // 10 is the top padding
        mainPanel.add(mainWrapperPanel, BorderLayout.CENTER);

        mainWrapperPanel.setOpaque(false);


        String back = "back" ;
        if(loginRegisterGUI != null)
            back = "Disconnect";
        // Create button panel
        JButton backButton = new JButton(back);
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(backButton);

        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0)); // 10 is the top padding

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel, BorderLayout.CENTER);



//        JPanel buttonPanel = new JPanel();
//        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
//        buttonPanel.setOpaque(false);
//
//
//        buttonPanel.add(Box.createHorizontalGlue());
//        buttonPanel.add(stockManagementButton);
//        buttonPanel.add(Box.createHorizontalStrut(60));
//        buttonPanel.add(orderButton);
//        buttonPanel.add(Box.createHorizontalGlue());
//
//        JPanel bottomPanel = new JPanel();
//        bottomPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
//        bottomPanel.add(backButton);
//
//        // Add button panel to the main panel
//        mainPanel.add(Box.createVerticalStrut(120)); // Adjust the spacing as needed
//        mainPanel.add(buttonPanel,BorderLayout.CENTER);
//
//
//        mainPanel.add(Box.createVerticalStrut(200));
//        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
//
//
//        add(mainPanel, BorderLayout.CENTER);

        // Add action listeners for the buttons
        stockManagementButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    openStockManagement();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        orderButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    openOrderManagement();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });


        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(loginRegisterGUI != null)
                    loginRegisterGUI.showMainPanel();
                else{
                    mainGUI.showMainPanel();
                }
            }
        });
    }
    private JButton createButton(String text, String imagePath) throws IOException {
        // Create button panel
        int width = 150;
        int height = 150;
        JPanel buttonPanel = new JPanel(null);
        buttonPanel.setLayout(new BorderLayout());

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
        button.setMargin(new Insets(0, 0, 0, 0)); // Set the margin to zer


        return button;

    }

    private void openStockManagement() throws IOException {
        mainPanel.setVisible(false);

        if (stockManagement == null) {
            stockManagement = new StockManagement(this);
            stockManagement.setPreferredSize(mainPanel.getSize());
            stockManagement.setMaximumSize(mainPanel.getMaximumSize());
            stockManagement.setMinimumSize(mainPanel.getMinimumSize());
            stockManagement.setSize(mainPanel.getSize());
        }

        add(stockManagement, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    private void openOrderManagement() throws IOException {
        mainPanel.setVisible(false);

        if (orderManagementGui == null) {
            orderManagementGui = new OrderManagementGui(this);
            orderManagementGui.setPreferredSize(mainPanel.getSize());
            orderManagementGui.setMaximumSize(mainPanel.getMaximumSize());
            orderManagementGui.setMinimumSize(mainPanel.getMinimumSize());
            orderManagementGui.setSize(mainPanel.getSize());
        }

        add(orderManagementGui, BorderLayout.CENTER);
        revalidate();
        repaint();
    }



    public void showDefaultPanelFromChild() {
        mainPanel.setVisible(true);
        removeCurrentChildPanel();
        revalidate();
        repaint();
    }

    private void removeCurrentChildPanel() {
        if (orderManagementGui != null && orderManagementGui.isShowing()) {
            remove(orderManagementGui);
        } else if (stockManagement != null && stockManagement.isShowing()) {
            remove(stockManagement);
        }
    }

    public void startMenu() {
        /**
         * Displays the initial menu for selecting the market to manage and entering the number of shelves.
         * Prompts the user to create a default market and then displays the stock menu for managing products.
         */
        Chain chain;
        Market market;
        String numOfMarkets;
        String numOfShelves = "30";
        String numOfMarketToManagement;

        if (ProductDetailsDAO.getNumberOfMarketsInChain() != 0 && ProductDetailsDAO.getManagedMarket() != 0 &&
                ProductDetailsDAO.getNumOfShelves() != 0) {
            chain = new Chain(ProductDetailsDAO.getNumberOfMarketsInChain());
            market = new Market(ProductDetailsDAO.getNumOfShelves());
            numOfMarketToManagement = Integer.toString(ProductDetailsDAO.getManagedMarket());
            ProductManager.setStore(market.getStore());
            ProductManager.setStorage(market.getStorage());
            ProductManager.setShortages(market.getShortages());
            MarketManager.setMarket(market);
            ProductService.getInstance().sendToSupplierAllProductsQuantity();
        }
        else {
            numOfMarkets = "1";
            numOfMarketToManagement = "1";
            chain = new Chain(Integer.parseInt(numOfMarkets));
            ProductDetailsDAO.setNumberOfMarketsInChain(Integer.parseInt(numOfMarkets));
            ProductDetailsDAO.setManagedMarket(Integer.parseInt(numOfMarketToManagement));
        }
        market = new Market(Integer.parseInt(numOfShelves));
        ProductDetailsDAO.setNumOfShelves(Integer.parseInt(numOfShelves));
        ProductDetailsDAO.getInstance().saveDetails();
        ProductManager.getInstance();
        ProductManager.setShortages(market.getShortages());
        ProductManager.setStorage(market.getStorage());
        ProductManager.setStore(market.getStore());
        MarketManager.setMarket(market);
    }
}