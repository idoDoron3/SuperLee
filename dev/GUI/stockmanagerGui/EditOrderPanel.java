package GUI.stockmanagerGui;

import Supplier_Module.Business.Managers.Order_Manager;
import Supplier_Module.Business.Order;
import Supplier_Module.DAO.OrderDAO;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class EditOrderPanel extends JPanel {
    private OrderManagementGui parentPanel;
    private JPanel mainPanel;
    private DeleteProductFromOrderPanel deleteProductFromOrderPanel;
    private EditProductFromOrderPanel editProductFromOrderPanel;
    private String number;


    public EditOrderPanel (OrderManagementGui parnet)
    {
        this.parentPanel = parnet;
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
        gbc.insets = new Insets(10, 0, 10, 0); // Adjust spacing as needed

        JLabel titleLabel = new JLabel("<html>Edit Order:<br></html>");
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 24));
        centerPanel.add(titleLabel, gbc);

        // Create button panel
        JPanel editPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel enterNumberLabel = new JLabel("Please enter the Order number you want to Edit:");
        JTextField numberField = new JTextField(10);
        JButton submitButton = new JButton("Submit");


        editPanel.add(enterNumberLabel);
        editPanel.add(numberField);
        editPanel.add(submitButton);

        GridBagConstraints gbcd = new GridBagConstraints();
        gbcd.gridx = 0;
        gbcd.gridy = 1;
        gbcd.weighty = 0.5; // Place components vertically in the middle
        gbcd.anchor = GridBagConstraints.NORTH; // Center-align components
        centerPanel.add(editPanel, gbcd);



// Add action listener for the submit button
        submitButton.addActionListener(e -> {
            String orderNumber = numberField.getText();
            if (!isExistOrder(orderNumber)) {
                {
                    JOptionPane.showMessageDialog(null, "Invalid order ID");
                }
            }
            else {

                if (OrderDAO.getInstance().getOrderById(Integer.parseInt(orderNumber)).getKind() != 2) {
                    JOptionPane.showMessageDialog(null, "Invalid order ID");
                }
            else{
                centerPanel.remove(editPanel);
                this.number = numberField.getText();
                JPanel buttonPanel = new JPanel();
                buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
                buttonPanel.setOpaque(false);
                JButton editProduct = null;
                try {
                    editProduct = createButton("Edit Product", "/GUI/pictures/new-supplier.jpg");
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                JButton deleteProduct = null;
                try {
                    deleteProduct = createButton("Delete Product", "/GUI/pictures/update.jpg");
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }

                buttonPanel.add(Box.createHorizontalGlue());
                buttonPanel.add(editProduct);
                buttonPanel.add(Box.createHorizontalStrut(20));
                buttonPanel.add(deleteProduct);
                buttonPanel.add(Box.createHorizontalGlue());

                gbcd.gridy = 1;
                gbcd.anchor = GridBagConstraints.CENTER;
                centerPanel.add(buttonPanel, gbcd);
//                this.supplier = SupplierDAO.getInstance().getSupplier(Integer.parseInt(supplierNumber));//TODO


                // Add action listener for the yes button
                editProduct.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        try {
                            openEditProductFromOrderPanel();
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                });

                deleteProduct.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        openDeleteProductFromOrderPanel();
                    }
                });

                // Refresh the panel to show the confirmation panel
                revalidate();
                repaint();
            }
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
    private void openDeleteProductFromOrderPanel() {
        mainPanel.setVisible(false);

        if (deleteProductFromOrderPanel == null) {
            deleteProductFromOrderPanel = new DeleteProductFromOrderPanel(parentPanel, Integer.parseInt(number));
            deleteProductFromOrderPanel.setPreferredSize(mainPanel.getSize());
            deleteProductFromOrderPanel.setMaximumSize(mainPanel.getMaximumSize());
            deleteProductFromOrderPanel.setMinimumSize(mainPanel.getMinimumSize());
            deleteProductFromOrderPanel.setSize(mainPanel.getSize());
        }

        add(deleteProductFromOrderPanel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    private void openEditProductFromOrderPanel() throws IOException {
        mainPanel.setVisible(false);

        if (editProductFromOrderPanel == null) {
            editProductFromOrderPanel = new EditProductFromOrderPanel(parentPanel,Integer.parseInt(number));
            editProductFromOrderPanel.setPreferredSize(mainPanel.getSize());
            editProductFromOrderPanel.setMaximumSize(mainPanel.getMaximumSize());
            editProductFromOrderPanel.setMinimumSize(mainPanel.getMinimumSize());
            editProductFromOrderPanel.setSize(mainPanel.getSize());
        }

        add(editProductFromOrderPanel, BorderLayout.CENTER);
        revalidate();
        repaint();
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

    public void showDefaultPanelFromChild() {
        mainPanel.setVisible(true);
        removeCurrentChildPanel();
        revalidate();
        repaint();
    }

    private void removeCurrentChildPanel() {
        if (deleteProductFromOrderPanel != null && deleteProductFromOrderPanel.isShowing()) {
            remove(deleteProductFromOrderPanel);
            deleteProductFromOrderPanel =null;
        }
        if (editProductFromOrderPanel != null && editProductFromOrderPanel.isShowing()) {
            remove(editProductFromOrderPanel);
            editProductFromOrderPanel = null;
        }
    }

    public boolean isPositiveInteger(String input) {
        if (input == null || input.isEmpty())
        {
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
    public boolean isExistOrder(String id)
    {
        if(!isPositiveInteger(id))
            return false;
        else {
            return Order_Manager.getOrder_Manager().isExistOrder(Integer.parseInt(id));
        }
    }


}
