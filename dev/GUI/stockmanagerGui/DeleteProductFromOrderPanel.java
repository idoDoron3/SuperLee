package GUI.stockmanagerGui;

import Supplier_Module.Business.Agreement.SupplierProduct;
import Supplier_Module.Business.Managers.Order_Manager;
import Supplier_Module.Business.Managers.SupplyManager;
import Supplier_Module.Business.Order;
import Supplier_Module.DAO.OrderDAO;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class DeleteProductFromOrderPanel extends JPanel {
   // //private EditOrderPanelB parentPanel;
    private OrderManagementGui parentPanel;
    private JPanel mainPanel;
    private Order order;


    public DeleteProductFromOrderPanel(OrderManagementGui parent, int orderID) {
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


        JLabel titleLabel = new JLabel("<html>Delete Product From Order: <br></html>");
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 24));
        centerPanel.add(titleLabel, gbc);

        JLabel textLabel = new JLabel("select any product you want to delete from the list and press remove");
        textLabel.setHorizontalAlignment(SwingConstants.CENTER);
        textLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
        textLabel.setOpaque(true);
        gbc.gridy =1;
        gbc.insets = new Insets(40, 0, 20, 0); // Adjust spacing as needed
        centerPanel.add(textLabel, gbc);

        JPanel removePanel = new JPanel();
        removePanel.setLayout(new BoxLayout(removePanel, BoxLayout.X_AXIS));

        // Create and add the new components
        String[] items = getAllProductsNameOfOrder(orderID);
        JComboBox<String> comboBox = new JComboBox<>(items);
        JButton removeButton = new JButton("Remove");

        removePanel.add(comboBox);
        removePanel.add(Box.createHorizontalStrut(15));
        removePanel.add(removeButton);


        // Add empty borders to create spacing

        gbc.gridy = 2;
        gbc.insets = new Insets(0, 0, 10, 0); // Adjust spacing as needed
        centerPanel.add(removePanel, gbc);

        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedItem = (String) comboBox.getSelectedItem();
                // Perform validation or further processing with the selected item and entered text
                if (selectedItem == null ) {
                    JOptionPane.showMessageDialog(null, "You need to chose a Product to Remove!");
                } else {
                    JOptionPane.showMessageDialog(null, selectedItem + " Removed!");

                    // Perform the desired action with the selected item and entered text
                    Order order1=Order_Manager.getOrder_Manager().getPeriodOrderById(orderID);
                    SupplierProduct itemToDelete=order1.isProductInOrder(selectedItem);
                    Order_Manager.getOrder_Manager().deleteProductFromOrder(order1,itemToDelete);
                    // Remove the selected item from the JComboBox
                    comboBox.removeItem(selectedItem);
                    if(comboBox.getItemCount()==0) //
                    {
                        OrderDAO.getInstance().Delete(order1);
                        JOptionPane.showMessageDialog(null, "There is no more products in the order \n Order Deleted");
                        centerPanel.remove(comboBox);
                    }
                }
            }
        });
        gbc.gridy =3;
        centerPanel.add(removePanel,gbc);


        // Create button panel
        JButton backButton = new JButton("Back");
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.setOpaque(false);
        bottomPanel.add(backButton);
        mainPanel.add(centerPanel,BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
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
}