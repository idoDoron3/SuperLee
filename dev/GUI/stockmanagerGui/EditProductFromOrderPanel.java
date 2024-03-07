package GUI.stockmanagerGui;

import Supplier_Module.Business.Agreement.SupplierProduct;
import Supplier_Module.Business.Managers.Order_Manager;
import Supplier_Module.Business.Managers.SupplyManager;
import Supplier_Module.Business.Order;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class EditProductFromOrderPanel extends JPanel {
    //ate EditOrderPanelB parentPanel;
    private OrderManagementGui parentPanel;
    private JPanel mainPanel;

    public EditProductFromOrderPanel(OrderManagementGui parent,int orderID)throws IOException
    {
        this.parentPanel = parent;
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

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.NORTH; // Align in the center horizontally
        gbc.insets = new Insets(0, 0, 70, 0); // Adjust spacing as needed

        JLabel titleLabel = new JLabel("Edit Amount of Product");
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 24));
        centerPanel.add(titleLabel, gbc);


        JLabel textLabel = new JLabel("Select Product From The List And Enter Amount, Later, Press Update");
        textLabel.setHorizontalAlignment(SwingConstants.CENTER);
        textLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
        textLabel.setOpaque(true);
        gbc.gridy =1;
        gbc.insets = new Insets(40, 0, 20, 0); // Adjust spacing as needed
        centerPanel.add(textLabel, gbc);


        JPanel updatePanel = new JPanel();
        updatePanel.setLayout(new BoxLayout(updatePanel, BoxLayout.X_AXIS));

        // Create and add the new components
        String[] items = getAllProductsNameOfOrder(orderID);
        JComboBox<String> comboBox = new JComboBox<>(items);
        JTextField newTextfield = new JTextField(7);
        JButton updateButton = new JButton("update");

        updatePanel.add(comboBox);
        updatePanel.add(Box.createHorizontalStrut(10));
        updatePanel.add(newTextfield);
        updatePanel.add(Box.createHorizontalStrut(10));
        updatePanel.add(updateButton);

        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedItem = (String) comboBox.getSelectedItem();
                String enteredText = newTextfield.getText();
                // Perform validation or further processing with the selected item and entered text
                if (selectedItem == null || enteredText.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "You need to enter valid amount!");
                } else {
                    Order order1=Order_Manager.getOrder_Manager().getPeriodOrderById(orderID);
                    int supID= order1.getSupplier_id();
                    if(isValidAmount(supID,enteredText,selectedItem)) {
                        int amount = Integer.parseInt(enteredText);
                        SupplierProduct itemToDelete = order1.isProductInOrder(selectedItem);
                        Order_Manager.getOrder_Manager().editProductAmount(order1, itemToDelete, amount); // todo
                        JOptionPane.showMessageDialog(null, selectedItem + "amount updated!!");
                    }
                    else
                    {
                        JOptionPane.showMessageDialog(null, "You need to enter valid amount!");
                    }
                }
            }
        });
        gbc.gridy =2;
        centerPanel.add(updatePanel,gbc);

        JButton backButton = new JButton("Back");
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));
        bottomPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        bottomPanel.setOpaque(false);


        bottomPanel.add(Box.createVerticalStrut(50));
        bottomPanel.add(backButton);



        mainPanel.add(centerPanel,BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.PAGE_END);
        add(mainPanel,BorderLayout.CENTER);


        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                parent.showDefaultPanelFromChild();
            }
        });


//        revalidate();
//        repaint();
    }

    public String[] getAllProductsNameOfOrder(int orderID)
    {
        return Order_Manager.getOrder_Manager().getAllProductsNameOfOrder(orderID);
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
            int availableAmount= SupplyManager.getSupply_manager().getSupplier(supID).getAgreement().getProduct(productName).getAmount_available();
            int requestedAmount=Integer.parseInt(amount);
            return availableAmount>=requestedAmount;
        }
    }
}
