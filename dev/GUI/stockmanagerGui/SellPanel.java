package GUI.stockmanagerGui;

import Stock.Business.Product;
import Stock.Service.ProductService;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;


public class SellPanel extends JPanel {


    public SellPanel(){

        setOpaque(true);

        JLabel catalogNumberLabel = new JLabel("Catalog Number");
        Font categoryLabelFont = catalogNumberLabel.getFont();
        Font catalogNumberLabelNewFont = categoryLabelFont.deriveFont(Font.PLAIN, 18);
        catalogNumberLabel.setFont(catalogNumberLabelNewFont);

        JTextField catalogNumberTextField = new JTextField();
        Font categoryTextFieldFont = catalogNumberTextField.getFont();
        Font catalogNumberTextFieldNewFont = categoryTextFieldFont.deriveFont(Font.PLAIN, 18);
        catalogNumberTextField.setFont(catalogNumberTextFieldNewFont);

        JLabel invalidCatalogNumber = new JLabel("Invalid Catalog Number");
        Font invalidCatalogNumberFont = invalidCatalogNumber.getFont();
        Font invalidCatalogNumberNewFont = invalidCatalogNumberFont.deriveFont(Font.PLAIN, 18);
        invalidCatalogNumber.setFont(invalidCatalogNumberNewFont);
        invalidCatalogNumber.setForeground(Color.RED);


        JLabel quantityLabel = new JLabel("Quantity");
        Font quantityLabelFont = quantityLabel.getFont();
        Font quantityLabelNewFont = quantityLabelFont.deriveFont(Font.PLAIN, 18);
        quantityLabel.setFont(quantityLabelNewFont);

        JTextField quantityTextField = new JTextField();
        Font quantityTextFieldFont = quantityTextField.getFont();
        Font quantityTextFieldNewFont = quantityTextFieldFont.deriveFont(Font.PLAIN, 18);
        quantityTextField.setFont(quantityTextFieldNewFont);

        JLabel invalidQuantityLabel = new JLabel("Invalid Quantity");
        Font invalidQuantityLabelFont = invalidQuantityLabel.getFont();
        Font invalidQuantityLabelNewFont = invalidQuantityLabelFont.deriveFont(Font.PLAIN, 18);
        invalidQuantityLabel.setFont(invalidQuantityLabelNewFont);
        invalidQuantityLabel.setForeground(Color.RED);

        JPanel inputPanel = new JPanel();
        int verticalGap = 35; // Set the desired vertical gap between rows
        int horizontalGap = 15;
        inputPanel.setLayout(new GridLayout(2, 3, horizontalGap, verticalGap));
        inputPanel.add(catalogNumberLabel);
        inputPanel.add(catalogNumberTextField);
        inputPanel.add(invalidCatalogNumber);
        invalidCatalogNumber.setVisible(false);
        inputPanel.add(quantityLabel);
        inputPanel.add(quantityTextField);
        inputPanel.add(invalidQuantityLabel);
        invalidQuantityLabel.setVisible(false);

        inputPanel.setOpaque(false);

        JPanel inputWrapperPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        inputWrapperPanel.add(inputPanel);
        add(inputWrapperPanel, BorderLayout.WEST);

        inputWrapperPanel.setOpaque(false);

        // Create button panel
        JButton submitButton = new JButton("Submit");

        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonsPanel.add(submitButton);

        buttonsPanel.setOpaque(false);

        add(buttonsPanel, BorderLayout.SOUTH);




        submitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Handle submit button action
                ProductService productService = ProductService.getInstance();
                LocalDate localDate = LocalDate.now();
                ArrayList<JLabel> inputsArrayList = new ArrayList<>();
                inputsArrayList.add(invalidCatalogNumber);
                inputsArrayList.add(invalidQuantityLabel);
                ArrayList<Boolean> inputChecks = new ArrayList<>();

                inputChecks.add(!catalogNumberTextField.getText().equals(""));
                String quantityStr = quantityTextField.getText();
                inputChecks.add(!quantityStr.equals("") && checkIfPositiveIntegerNumber(quantityStr) &&
                        Integer.parseInt(quantityStr) <= 30);

                boolean allTrue = !inputChecks.contains(Boolean.FALSE);

                if (allTrue) {
                    for (JLabel currentInput : inputsArrayList) {
                        currentInput.setVisible(false);
                    }
                    Product product = productService.getProductByUniqueCode(catalogNumberTextField.getText());
                    if (product!= null) {
                        if (productService.sellProductsByUniqueCode(product,Integer.parseInt(quantityTextField.getText()),LocalDate.now())) {
                            catalogNumberTextField.setText("");
                            quantityTextField.setText("");
//                            JOptionPane.showMessageDialog(null, quantityTextField.getText() +
//                                    " " + product.getName() + " sold");
                            if(product.getStoreQuantity() + product.getStorageQuantity() == 0){
                                JOptionPane.showMessageDialog(null, quantityTextField.getText() +
                                        " " + product.getName() + " sold \n" + "ALERT!!!! the product: " +
                                        product.getName() + " is in shortage");
                            }
                            else if(product.getStoreQuantity() + product.getStorageQuantity() < product.getMinimumQuantity()){
                                JOptionPane.showMessageDialog(null, quantityTextField.getText() +
                                        " " + product.getName() + " sold \n" + "ALERT!!!! the product: " +
                                        product.getName() + " is under the minimum quantity");
                            }
                        }else {
                            JOptionPane.showMessageDialog(null, "There are not enough " + product.getName());
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "The product was not found");
                    }
                } else {
                    int index = 0;
                    for (boolean currentInputValid : inputChecks) {
                        // Perform operations on the 'element' using the index 'index'
                        if (!currentInputValid) {
                            inputsArrayList.get(index).setVisible(true);
                        } else {
                            inputsArrayList.get(index).setVisible(false);
                        }
                        index++;
                    }
                }
            }
        });


    }
    private Boolean checkIfPositiveIntegerNumber(String number) {
        if(number.equals("")){
            return false;
        }
        return number.matches("[0-9]+") && Integer.parseInt(number) > 0;
    }
    private JPanel createTextFieldPanel(JLabel label, JTextField textField) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(label, BorderLayout.WEST);
        panel.add(textField, BorderLayout.CENTER);
        return panel;
    }

    private JButton createButton(String text, String imagePath) throws IOException {
        // Create button panel
        int width = 100;
        int height = 100;
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

}
