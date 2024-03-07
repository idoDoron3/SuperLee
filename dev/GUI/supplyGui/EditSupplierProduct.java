package GUI.supplyGui;

import Supplier_Module.Business.Agreement.SupplierProduct;
import Supplier_Module.Business.Supplier;
import Supplier_Module.DAO.SupplierProductDAO;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.LinkedList;
import java.util.regex.Pattern;

public class EditSupplierProduct extends JPanel {
    private EditSupplierAgreemantGui parent;
    private JPanel mainPanel;
    private Supplier supplier;

    public EditSupplierProduct(EditSupplierAgreemantGui editSupplierAgreemantGui, Supplier supplier1) {
        this.parent = editSupplierAgreemantGui;
        this.supplier = supplier1;

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
        gbc.insets = new Insets(10, 0,10, 0);

        JLabel titleLabel = new JLabel("Edit Supplier Product");
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 24));
        centerPanel.add(titleLabel, gbc);

        JPanel editPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel enterNumberLabel = new JLabel("Please enter the supplier Product name you want to Edit:");
        JTextField numberField = new JTextField(12);
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
            String product_name = numberField.getText();
            // Perform delete action based on the supplier number
//            Pattern pattern = Pattern.compile("^[a-zA-Z]+$");
            SupplierProduct supplierProduct =null;
            LinkedList<SupplierProduct> temp = supplier.getAgreement().getProductList();
            boolean found = false;
            for(SupplierProduct sp : temp){
                if(sp.getProduct_name().equals(product_name)){
                    found =true;
                    supplierProduct = sp;
                    break;
                }
            }
            if(!found){
                JOptionPane.showMessageDialog(null, "invalid input!", "ERROR",  JOptionPane.ERROR_MESSAGE);
            }
//            else if(product_name.equals( " " )){//check if the supplier has this product
//                JOptionPane.showMessageDialog(null, "there is no "+ product_name + " that "+ this.supplier.getCard().getSupplier_name() + "can supply", "ERROR",  JOptionPane.ERROR_MESSAGE);
//            }
//            else if(product_name.equals("this product is in period order")){
//                JOptionPane.showMessageDialog(null, "this supplier has period order!", "ERROR",  JOptionPane.ERROR_MESSAGE);
//            }
            else {
                mainPanel.remove(editPanel);

                JLabel nameLabel = new JLabel("Product amount:");
                Font nameLabelFont = nameLabel.getFont();
                Font nameLabelNewFont = nameLabelFont.deriveFont(Font.BOLD, 18);
                nameLabel.setFont(nameLabelNewFont);

                JTextField nameLabelTextField = new JTextField();
                Font nameLabelTextFieldFont = nameLabelTextField.getFont();
                Font nameTextFieldNewFont = nameLabelTextFieldFont.deriveFont(Font.BOLD, 18);
                nameLabelTextField.setFont(nameTextFieldNewFont);

                JButton update1Button = new JButton("Update");

                JLabel idLabel = new JLabel("Unit Price");
                Font idLabelFont = idLabel.getFont();
                Font idLabelNewFont = idLabelFont.deriveFont(Font.BOLD, 18);
                idLabel.setFont(idLabelNewFont);


                JTextField idTextField = new JTextField();
                Font idTextFieldFont = idTextField.getFont();
                Font idTextFieldNewFont = idTextFieldFont.deriveFont(Font.BOLD, 18);
                idTextField.setFont(idTextFieldNewFont);

                JButton update2Button = new JButton("Update");

                JPanel inputPanel = new JPanel();
                int verticalGap = 15; // Set the desired vertical gap between rows
                int horizontalGap = 10;
                inputPanel.setLayout(new GridLayout(2, 3, horizontalGap, verticalGap));
                inputPanel.add(nameLabel);
                inputPanel.add(nameLabelTextField);
                inputPanel.add(update1Button);

                inputPanel.add(idLabel);
                inputPanel.add(idTextField);
                inputPanel.add(update2Button);
                inputPanel.setOpaque(false);
                gbcd.gridy =1;
                gbcd.anchor = GridBagConstraints.CENTER;
                centerPanel.add(inputPanel, gbcd);
//                centerPanel.add(inputWrapperPanel, BorderLayout.CENTER);


                // Add action listener for the yes button
                update1Button.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        String amount = nameLabelTextField.getText();
                        if(!isInteger(amount) || Integer.parseInt(amount) <0){
                            JOptionPane.showMessageDialog(null, "amount is not valid!");
                        }
                        else {
                            int amount1 = Integer.parseInt(amount);
                            SupplierProductDAO.getInstance().UpdateAmount(supplier.getCard().getSupplier_number(),product_name,amount1);
                            JOptionPane.showMessageDialog(null, "product " + product_name + " amount has been update to " + nameLabelTextField.getText());
                        }
                    }
                });

                update2Button.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        String price = idTextField.getText();
                        if(!isDouble(price) || Double.parseDouble(price) <0 ){
                            JOptionPane.showMessageDialog(null, "price is not valid!");
                        }
                        else {
                            Double price1 = Double.parseDouble(price);
                            SupplierProductDAO.getInstance().UpdatePrice(supplier.getCard().getSupplier_number(),product_name,price1);
                            JOptionPane.showMessageDialog(null, "product " + product_name + " price has been update to " + idTextField.getText());

                        }
                    }
                });

                // Refresh the panel to show the confirmation panel
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
                parent.showDefaultPanelFromChild();
            }
        });

    }

    public void showDefaultPanelFromChild() {
        mainPanel.setVisible(true);
        removeCurrentChildPanel();
        revalidate();
        repaint();
    }

    private void removeCurrentChildPanel() {

    }

    public static boolean isInteger(String input) {
        try {
            Integer.parseInt(input);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    public static boolean isDouble(String input) {
        try {
            Double.parseDouble(input);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }

    }

    }
