package GUI.stockmanagerGui;

import LoginRegister.Presentation.LoginMenuNew;
import Supplier_Module.Business.Agreement.SupplierProduct;
import Supplier_Module.Business.Managers.Order_Manager;
import Supplier_Module.Business.Managers.SupplyManager;
import Supplier_Module.Business.Supplier;
import Supplier_Module.DAO.SupplierDAO;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

public class AddOrderPanel extends JPanel {
    private OrderManagementGui parentPanel;
    private JPanel mainPanel;
    private Map<SupplierProduct, Integer> productsOfOrder;

    public AddOrderPanel(OrderManagementGui parnet) {
        this.parentPanel = parnet;
        this.productsOfOrder = new HashMap<>();
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

        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.NORTH; // Align in the center horizontally
        gbc.insets = new Insets(10, 0, 10, 0);

        JLabel titleLabel = new JLabel("Create a new periodic order:");
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 24));
        centerPanel.add(titleLabel, gbc);

        JPanel addPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel enterNumberLabel = new JLabel("Please enter supplier number:");
        JTextField numberField = new JTextField(12);
        JButton submitButton = new JButton("Submit");

        addPanel.add(enterNumberLabel);
        addPanel.add(numberField);
        addPanel.add(submitButton);

        GridBagConstraints gbcd = new GridBagConstraints();
        gbcd.gridx = 0;
        gbcd.gridy = 1;
        gbcd.weighty = 0.5; // Place components vertically in the middle
        gbcd.anchor = GridBagConstraints.NORTH; // Center-align components
        centerPanel.add(addPanel, gbcd);



// Add action listener for the submit button
        submitButton.addActionListener(e -> {
            String supplierNumber = numberField.getText();
            // Perform delete action based on the supplier number
            if (!isPositiveInteger(supplierNumber)) {
                JOptionPane.showMessageDialog(null, "invalid input!", "ERROR", JOptionPane.ERROR_MESSAGE);
            } else if (SupplierDAO.getInstance().getSupplier(Integer.parseInt(supplierNumber)) == null) {//check if the supplier number exist
                JOptionPane.showMessageDialog(null, "there is no supplier with id" + supplierNumber, "ERROR", JOptionPane.ERROR_MESSAGE);
            } else if (SupplierDAO.getInstance().getSupplier(Integer.parseInt(supplierNumber)).getAgreement().getProductList().size()==0 ){
                JOptionPane.showMessageDialog(null, "this supplier doesn't has products!", "ERROR", JOptionPane.ERROR_MESSAGE);
            } else {
                centerPanel.remove(addPanel);

                // Add action listener for the yes button
                int supID = Integer.parseInt(supplierNumber);
                String[] items = getProductOfSupplier(supID);
                Supplier supplier = SupplierDAO.getInstance().getSupplier(supID);
                JComboBox<String> itemComboBox = new JComboBox<>(items);
                JTextField textBox = new JTextField(10);
                JButton addToCartButton = new JButton("Add to Cart");

                // Add new components to the main panel
                JPanel inputPanel = new JPanel();
                int verticalGap = 15; // Set the desired vertical gap between rows
                int horizontalGap = 7;
                inputPanel.setLayout(new GridLayout(1, 3, horizontalGap, verticalGap));
                inputPanel.add(itemComboBox);
                inputPanel.add(textBox);
                inputPanel.add(addToCartButton);
                inputPanel.setOpaque(false);
                gbcd.gridy = 1;
                gbcd.anchor = GridBagConstraints.CENTER;
                centerPanel.add(inputPanel, gbcd);

                // Add action listener for the addToCartButton
                addToCartButton.addActionListener(actionEvent -> {
                    String item = (String) itemComboBox.getSelectedItem();
                    String quantity = textBox.getText();

                    // Perform input validation for the new components
                    if (isValidAmount(supID, quantity, item)) {
                        // Perform success logic here
                        SupplierProduct sp = supplier.getAgreement().getProduct(item);
                        int amount = Integer.parseInt(quantity);
                        productsOfOrder.put(sp,amount);
                        itemComboBox.removeItem(item);
                        JOptionPane.showMessageDialog(null, "Successfully added to cart");
                        // Initialize as needed
                    } else {
                        // Perform error logic here
                        JOptionPane.showMessageDialog(null, "Invalid input. Please try again.");
                        // Initialize as needed
                    }
                });

                // Update the container and layout
                JButton createOrderButton = new JButton("Finish and create Period order");
                gbcd.gridy =2;
                centerPanel.add(createOrderButton, gbcd);
                createOrderButton.addActionListener(actionEvent -> {
                    if (productsOfOrder.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "There is no products in the order");
                    } else {
                        Map<Supplier,Map<SupplierProduct,Integer>> order_map = new HashMap<>();
                        order_map.put(supplier,productsOfOrder);
                        LoginMenuNew.getInstance();
                        LocalDate localDate = LoginMenuNew.getLocalDate();
                        Order_Manager.getOrder_Manager().current_orders(order_map,localDate);
                        JOptionPane.showMessageDialog(null, "Order add successfully");
                        createOrderButton.setVisible(false);
                    }

                });


                revalidate();
                repaint();
            }

        });
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton backButton = new JButton("Back");
        bottomPanel.setOpaque(false);
        bottomPanel.add(backButton);

        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
        add(mainPanel, BorderLayout.CENTER);

        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                parentPanel.showDefaultPanelFromChild();
            }
        });
    }


    public String[] getProductOfSupplier(int supplierNumber)
    {
        return SupplyManager.getSupply_manager().productsNamesOfSupplier(supplierNumber);
    }

    public boolean isPositiveInteger(String input) {
        if (input == null || input.isEmpty()) {
            return false;
        }

        for (int i = 0; i < input.length(); i++) {
            if (!Character.isDigit(input.charAt(i))) {
                return false;
            }
        }

        int number = Integer.parseInt(input);
        return number > 0;
    }

    public boolean isValidAmount(int supID,String amount, String productName)
    {
        if(!isPositiveInteger(amount))
            return false;
        else
        {
            int availableAmount=SupplyManager.getSupply_manager().getSupplier(supID).getAgreement().getProduct(productName).getAmount_available();
            int requestedAmount=Integer.parseInt(amount);
            return availableAmount>=requestedAmount;
        }
    }
}




