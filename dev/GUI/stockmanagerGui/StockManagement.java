package GUI.stockmanagerGui;

import GUI.storeGui.stockReport.SpecificProductReport;
import GUI.storeGui.stockReport.stockReportsPanel;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class StockManagement extends JPanel {
    private StockManagerGUI mainGUI;
    private JPanel mainPanel;
    private  ProductMenuGui productMenu;
    private MarketMenuGui marketMenu;

    public StockManagement(StockManagerGUI mainGUI) throws IOException {
        this.mainGUI = mainGUI;
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
        JButton productMenu = createButton("Product Menu", "/GUI/pictures/period-report.jpg");
        //JButton reportsMenu = createButton("Reports Menu", "/GUI/pictures/order_manager.jpg");
        JButton marketMenu = createButton("Market Menu", "/GUI/pictures/historic-orders.jpg");


        JPanel menuPanel = new JPanel();
        int verticalGap = 35; // Set the desired vertical gap between rows
        int horizontalGap = 45;
        menuPanel.setLayout(new GridLayout(1, 2, horizontalGap, verticalGap));
        menuPanel.add(productMenu);
        //menuPanel.add(reportsMenu);
        menuPanel.add(marketMenu);

        menuPanel.setOpaque(false);

        JPanel mainWrapperPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        mainWrapperPanel.add(menuPanel);
        mainWrapperPanel.setBorder(BorderFactory.createEmptyBorder(120, 0, 0, 0)); // 10 is the top padding
        mainPanel.add(mainWrapperPanel, BorderLayout.CENTER);

        mainWrapperPanel.setOpaque(false);


        JButton backButton = new JButton("Back");
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(backButton);

        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0)); // 10 is the top padding

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel, BorderLayout.CENTER);

        productMenu.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
//                parent.showDefaultPanelFromChild();]
                try {
                    openProductMenu();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        marketMenu.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
//                parent.showDefaultPanelFromChild();]
                try {
                    openMarketMenu();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });


        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                mainGUI.showDefaultPanelFromChild();
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
        button.setMargin(new Insets(0, 0, 0, 0)); // Set the margin to zero


        return button;

    }
    public void showDefaultPanelFromChild() {
        mainPanel.setVisible(true);
        removeCurrentChildPanel();
        revalidate();
        repaint();
    }

    private void removeCurrentChildPanel() {
        if (productMenu != null && productMenu.isShowing()) {
            remove(productMenu);
        }
        else if (marketMenu != null && marketMenu.isShowing()) {
            remove(marketMenu);
        }
    }
    public void showMainPanel() {
        mainPanel.setVisible(true);
        if (productMenu != null) {
            productMenu.setVisible(false);
        }
        if (marketMenu != null) {
            marketMenu.setVisible(false);
        }
    }

    private void openProductMenu() throws IOException {

        mainPanel.setVisible(false);

        if (productMenu == null) {
            productMenu = new ProductMenuGui(this);
            productMenu.setPreferredSize(mainPanel.getSize());
            productMenu.setMaximumSize(mainPanel.getMaximumSize());
            productMenu.setMinimumSize(mainPanel.getMinimumSize());
            productMenu.setSize(mainPanel.getSize());
        }
        productMenu.setVisible(true);
        add(productMenu, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    private void openMarketMenu() throws IOException {

        mainPanel.setVisible(false);

        if (marketMenu == null) {
            marketMenu = new MarketMenuGui(this);
            marketMenu.setPreferredSize(mainPanel.getSize());
            marketMenu.setMaximumSize(mainPanel.getMaximumSize());
            marketMenu.setMinimumSize(mainPanel.getMinimumSize());
            marketMenu.setSize(mainPanel.getSize());
        }
        marketMenu.setVisible(true);
        add(marketMenu, BorderLayout.CENTER);
        revalidate();
        repaint();
    }


}
