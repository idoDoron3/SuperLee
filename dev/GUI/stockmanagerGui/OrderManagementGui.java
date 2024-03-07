package GUI.stockmanagerGui;

import GUI.MainGUI;
import LoginRegister.Presentation.LoginMenuNew;
import Stock.Service.ProductService;
import Supplier_Module.Service.SupplierService;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.time.LocalDate;


public class OrderManagementGui extends JPanel {
    private StockManagerGUI mainGUI;
    private JPanel mainPanel;
    private AddOrderPanel addOrderPanel;
    private EditOrderPanel editOrderPanel;
    private DeleteOrderPanel deleteOrderPanel;

    public OrderManagementGui(StockManagerGUI mainGUI) throws IOException {
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

        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setOpaque(false);

        JLabel titleLabel = new JLabel("<html>Welcome to Order Management <br> Please select option :</html>");
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 24));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 0, 10, 0); // Adjust spacing as needed
        centerPanel.add(titleLabel, gbc);


        // Create button panel
        JButton backButton = new JButton("Back");
        JPanel buttonPanel = new JPanel(new GridLayout(2,2,15,15));
        buttonPanel.setOpaque(false);

        // Create buttons
        JButton addOrderButton = createButton("Add Period Order", "/GUI/pictures/add-order.jpg");
        JButton editOrderButton = createButton("Edit Period Order", "/GUI/pictures/update.jpg");
        JButton deleteOrderButton = createButton("Delete Period Order", "/GUI/pictures/delete.jpg");
        JButton defaultOrderButton = createButton("Order Each Product", "/GUI/pictures/order-report.jpg");

        buttonPanel.add(addOrderButton);
        buttonPanel.add(editOrderButton);
        buttonPanel.add(deleteOrderButton);
        buttonPanel.add(defaultOrderButton);

        gbc.gridy =1;
        centerPanel.add(buttonPanel,gbc);


        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.setOpaque(false);
        bottomPanel.add(backButton);

        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);


        add(mainPanel, BorderLayout.CENTER);

        // Add action listeners for the buttons
        addOrderButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                openAddOrderPanel();
            }
        });

        editOrderButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                openEditOrderPanel();
            }
        });

        deleteOrderButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                openDeleteOrderPanel();
            }
        });
        defaultOrderButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                LocalDate localDate=LoginMenuNew.getInstance().getLocalDate();
                SupplierService.getSupplierService().updatePeriodOrders(ProductService.getInstance().sendToSupplierAllProductsQuantity(),localDate);
                JOptionPane.showMessageDialog(null, "There is periodic order for each product!");
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
        int width = 120;
        int height = 120;
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
        Font buttonFont = new Font("Tahoma", Font.BOLD, 10);
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

    private void openAddOrderPanel() {
        mainPanel.setVisible(false);

        if (addOrderPanel == null) {
            addOrderPanel = new AddOrderPanel(this);
            addOrderPanel.setPreferredSize(mainPanel.getSize());
            addOrderPanel.setMaximumSize(mainPanel.getMaximumSize());
            addOrderPanel.setMinimumSize(mainPanel.getMinimumSize());
            addOrderPanel.setSize(mainPanel.getSize());
        }

        add(addOrderPanel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    private void openEditOrderPanel() {
        mainPanel.setVisible(false);

        if (editOrderPanel == null) {
            editOrderPanel = new EditOrderPanel(this);
            editOrderPanel.setPreferredSize(mainPanel.getSize());
            editOrderPanel.setMaximumSize(mainPanel.getMaximumSize());
            editOrderPanel.setMinimumSize(mainPanel.getMinimumSize());
            editOrderPanel.setSize(mainPanel.getSize());
        }

        add(editOrderPanel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }
    private void openDeleteOrderPanel() {
        mainPanel.setVisible(false);

        if (deleteOrderPanel == null) {
            deleteOrderPanel = new DeleteOrderPanel(this);
            deleteOrderPanel.setPreferredSize(mainPanel.getSize());
            deleteOrderPanel.setMaximumSize(mainPanel.getMaximumSize());
            deleteOrderPanel.setMinimumSize(mainPanel.getMinimumSize());
            deleteOrderPanel.setSize(mainPanel.getSize());
        }

        add(deleteOrderPanel, BorderLayout.CENTER);
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
        if (addOrderPanel != null && addOrderPanel.isShowing()) {
            remove(addOrderPanel);
            addOrderPanel =null;
        } else if (editOrderPanel != null && editOrderPanel.isShowing()) {
            remove(editOrderPanel);
            editOrderPanel =null;
        } else if (deleteOrderPanel != null && deleteOrderPanel.isShowing()) {
            remove(deleteOrderPanel);
            deleteOrderPanel = null;
        }
    }

}