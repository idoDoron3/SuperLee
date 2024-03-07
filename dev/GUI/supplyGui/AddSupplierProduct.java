package GUI.supplyGui;

import Supplier_Module.Business.Agreement.SupplierProduct;
import Supplier_Module.Business.Discount.PrecentageDiscount;
import Supplier_Module.Business.Discount.Range;
import Supplier_Module.Business.Managers.SupplyManager;
import Supplier_Module.Business.Supplier;
import Supplier_Module.DAO.Pair;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.plaf.basic.BasicInternalFrameTitlePane;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.LinkedList;
import java.util.regex.Pattern;

public class AddSupplierProduct extends JPanel {
    private EditSupplierAgreemantGui parent;
    private JPanel mainPanel;
    private Supplier supplier;
    private LinkedList<PrecentageDiscount> discounts;

    public AddSupplierProduct(EditSupplierAgreemantGui editSupplierAgreemantGui, Supplier supplier1) {
        this.parent = editSupplierAgreemantGui;
        this.supplier = supplier1;
        discounts = new LinkedList<>();

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

        JLabel titleLabel = new JLabel("Add Supplier Product");
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 24));
        mainPanel.add(titleLabel, BorderLayout.NORTH);


        // Create text fields
        JLabel nameLabel = new JLabel("Product Name:");
        Font nameLabelFont = nameLabel.getFont();
        Font nameLabelNewFont = nameLabelFont.deriveFont(Font.BOLD, 14);
        nameLabel.setFont(nameLabelNewFont);

        JTextField nameLabelTextField = new JTextField();
        Font nameLabelTextFieldFont = nameLabelTextField.getFont();
        Font nameTextFieldNewFont = nameLabelTextFieldFont.deriveFont(Font.BOLD, 14);
        nameLabelTextField.setFont(nameTextFieldNewFont);

        JLabel invalidNameLabel = new JLabel("Invalid Name");
        Font invalidNameLabelFont = invalidNameLabel.getFont();
        Font invalidNameLabelNewFont = invalidNameLabelFont.deriveFont(Font.PLAIN, 14);
        invalidNameLabel.setFont(invalidNameLabelNewFont);
        invalidNameLabel.setForeground(Color.RED);

        JLabel idLabel = new JLabel("Catalog Number");
        Font idLabelFont = idLabel.getFont();
        Font idLabelNewFont = idLabelFont.deriveFont(Font.PLAIN, 14);
        idLabel.setFont(idLabelNewFont);

        JTextField idTextField = new JTextField();
        Font idTextFieldFont = idTextField.getFont();
        Font idTextFieldNewFont = idTextFieldFont.deriveFont(Font.PLAIN, 14);
        idTextField.setFont(idTextFieldNewFont);

        JLabel invalidIDLabel = new JLabel("Invalid ID");
        Font invalidIDLabelFont = invalidIDLabel.getFont();
        Font invalidIDLabelNewFont = invalidIDLabelFont.deriveFont(Font.PLAIN, 14);
        invalidIDLabel.setFont(invalidIDLabelNewFont);
        invalidIDLabel.setForeground(Color.RED);


        JLabel wieghtLabel = new JLabel("Weight");
        Font weightLabelFont = wieghtLabel.getFont();
        Font wightLabelNewFont = weightLabelFont.deriveFont(Font.PLAIN, 14);
        wieghtLabel.setFont(wightLabelNewFont);

        JTextField weightTextField = new JTextField();
        Font weightTextFieldFont = weightTextField.getFont();
        Font weightTextFieldNewFont = weightTextFieldFont.deriveFont(Font.PLAIN, 14);
        weightTextField.setFont(weightTextFieldNewFont);

        JLabel invalidmWeightLabel = new JLabel("Invalid Address");
        Font invalidWeightLabelFont = invalidmWeightLabel.getFont();
        Font invalidweightLabelNewFont = invalidWeightLabelFont.deriveFont(Font.PLAIN, 14);
        invalidmWeightLabel.setFont(invalidweightLabelNewFont);
        invalidmWeightLabel.setForeground(Color.RED);


        JLabel priceLabel = new JLabel(" Unit Price:");
        Font priceLabelFont = priceLabel.getFont();
        Font priceLabelNewFont = priceLabelFont.deriveFont(Font.BOLD, 14);
        priceLabel.setFont(priceLabelNewFont);

        JTextField priceLabelTextField = new JTextField();
        Font priceLabelTextFieldFont = priceLabelTextField.getFont();
        Font priceTextFieldNewFont = priceLabelTextFieldFont.deriveFont(Font.BOLD, 14);
        priceLabelTextField.setFont(priceTextFieldNewFont);

        JLabel invalidPriceLabel = new JLabel("Invalid Name");
        Font invalidPriceLabelFont = invalidPriceLabel.getFont();
        Font invalidPriceLabelNewFont = invalidPriceLabelFont.deriveFont(Font.PLAIN, 14);
        invalidPriceLabel.setFont(invalidPriceLabelNewFont);
        invalidPriceLabel.setForeground(Color.RED);

        JLabel amountLabel = new JLabel("Avialiable Amount:");
        Font amountLabelFont = amountLabel.getFont();
        Font amountLabelNewFont = amountLabelFont.deriveFont(Font.BOLD, 14);
        amountLabel.setFont(amountLabelNewFont);

        JTextField amountLabelTextField = new JTextField();
        Font amountLabelTextFieldFont = amountLabelTextField.getFont();
        Font amountTextFieldNewFont = amountLabelTextFieldFont.deriveFont(Font.BOLD, 14);
        amountLabelTextField.setFont(amountTextFieldNewFont);

        JLabel invalidAmountLabel = new JLabel("Invalid Name");
        Font invalidAmountLabelFont = invalidAmountLabel.getFont();
        Font invalidAmountLabelNewFont = invalidAmountLabelFont.deriveFont(Font.PLAIN, 14);
        invalidAmountLabel.setFont(invalidAmountLabelNewFont);
        invalidAmountLabel.setForeground(Color.RED);
//min range of the discount
        JLabel minRangeLabel = new JLabel("Min Range of discount");
        Font minRangeLabelFont = minRangeLabel.getFont();
        Font minRangeLabelNewFont = minRangeLabelFont.deriveFont(Font.BOLD, 14);
        minRangeLabel.setFont(minRangeLabelNewFont);

        JTextField minRangeLabelTextField = new JTextField("0");
        minRangeLabelTextField.setEditable(false);
        Font minRangeLabelTextFieldFont = minRangeLabelTextField.getFont();
        Font minRangeTextFieldNewFont = minRangeLabelTextFieldFont.deriveFont(Font.BOLD, 14);
        minRangeLabelTextField.setFont(minRangeTextFieldNewFont);

       JButton submitDiscount = new JButton("Add Discount");

//top range of the discount
        JLabel maxRangeLabel = new JLabel("Max Range of discount");
        Font maxRangeLabelFont = maxRangeLabel.getFont();
        Font maxRangeLabelNewFont = maxRangeLabelFont.deriveFont(Font.BOLD, 14);
        maxRangeLabel.setFont(maxRangeLabelNewFont);

        JTextField maxRangeLabelTextField = new JTextField();
        Font maxRangeLabelTextFieldFont = maxRangeLabelTextField.getFont();
        Font maxRangeTextFieldNewFont = maxRangeLabelTextFieldFont.deriveFont(Font.BOLD, 14);
        maxRangeLabelTextField.setFont(maxRangeTextFieldNewFont);

        JLabel invalidmaxRangeLabel = new JLabel("Invalid Max amount");
        Font invalidmaxRangeLabelFont = invalidmaxRangeLabel.getFont();
        Font invalidmaxRangeLabelNewFont = invalidmaxRangeLabelFont.deriveFont(Font.PLAIN, 14);
        invalidmaxRangeLabel.setFont(invalidmaxRangeLabelNewFont);
        invalidmaxRangeLabel.setForeground(Color.RED);
        //discount
        JLabel discountLabel = new JLabel("Discount");
        Font dixcountLabelFont = discountLabel.getFont();
        Font discountLabelNewFont = dixcountLabelFont.deriveFont(Font.BOLD, 14);
        discountLabel.setFont(discountLabelNewFont);

        JTextField discountLabelTextField = new JTextField();
        Font discountLabelTextFieldFont = discountLabelTextField.getFont();
        Font discountTextFieldNewFont = discountLabelTextFieldFont.deriveFont(Font.BOLD, 14);
        discountLabelTextField.setFont(discountTextFieldNewFont);

        JLabel invalidDiscountLabel = new JLabel("Invalid Discount");
        Font invalidDiscountLabelFont = invalidDiscountLabel.getFont();
        Font invalidDiscountLabelNewFont = invalidDiscountLabelFont.deriveFont(Font.PLAIN, 14);
        invalidDiscountLabel.setFont(invalidDiscountLabelNewFont);
        invalidDiscountLabel.setForeground(Color.RED);


        JPanel inputPanel = new JPanel();
        int verticalGap = 25; // Set the desired vertical gap between rows
        int horizontalGap = 15;
        inputPanel.setLayout(new GridLayout(8, 3, horizontalGap, verticalGap));
        //product name
        inputPanel.add(nameLabel);
        inputPanel.add(nameLabelTextField);
        inputPanel.add(invalidNameLabel);
        invalidNameLabel.setVisible(false);
        //product catalog number
        inputPanel.add(idLabel);
        inputPanel.add(idTextField);
        inputPanel.add(invalidIDLabel);
        invalidIDLabel.setVisible(false);
        //product weight
        inputPanel.add(wieghtLabel);
        inputPanel.add(weightTextField);
        inputPanel.add(invalidmWeightLabel);
        invalidmWeightLabel.setVisible(false);
        //unit price
        inputPanel.add(priceLabel);
        inputPanel.add(priceLabelTextField);
        inputPanel.add(invalidPriceLabel);
        invalidPriceLabel.setVisible(false);
        //aviliable amount
        inputPanel.add(amountLabel);
        inputPanel.add(amountLabelTextField);
        inputPanel.add(invalidAmountLabel);
        invalidAmountLabel.setVisible(false);
        //min range discount
        inputPanel.add(minRangeLabel);
        inputPanel.add(minRangeLabelTextField);
        inputPanel.add(submitDiscount);
        //top range discount
        inputPanel.add(maxRangeLabel);
        inputPanel.add(maxRangeLabelTextField);
        inputPanel.add(invalidmaxRangeLabel);
        invalidmaxRangeLabel.setVisible(false);
        //discount number
        inputPanel.add(discountLabel);
        inputPanel.add(discountLabelTextField);
        inputPanel.add(invalidDiscountLabel);
        invalidDiscountLabel.setVisible(false);

        submitDiscount.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                invalidmaxRangeLabel.setVisible(false);
                invalidDiscountLabel.setVisible(false);
                String max_r = maxRangeLabelTextField.getText();
                String discount = discountLabelTextField.getText();
//                String amount = amountLabelTextField.getText();
//
                int min_range = Integer.parseInt(minRangeLabelTextField.getText());
                if(!checkIfPositiveIntNumber(max_r) || Integer.parseInt(max_r) <= min_range ){
                    invalidmaxRangeLabel.setVisible(true);
                }
                else if(!checkIfPositiveDoubleNumber(discount)){
                    invalidDiscountLabel.setVisible(true);
                }
                else{
                    int max_range  = Integer.parseInt(max_r);
                    double disc = Double.parseDouble(discount);
                    Range range = new Range (min_range,max_range);
                    PrecentageDiscount discount1 = new PrecentageDiscount(range,disc);
                    discounts.add(discount1);
                    int new_min_range = max_range+1;
                    minRangeLabelTextField.setText(String.valueOf(new_min_range));
                    JOptionPane.showMessageDialog(null, "Discount added successfully! ");




                }
            }
        });

        inputPanel.setOpaque(false);

//        mainPanel.add(inputPanel, BorderLayout.CENTER);
        JPanel inputWrapperPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        inputWrapperPanel.add(inputPanel);
        mainPanel.add(inputWrapperPanel, BorderLayout.CENTER);

        inputWrapperPanel.setOpaque(false);

        // Create button panel
        JButton submitButton = new JButton("Add Supplier Product");
        //JButton submitButton = createButton("Submit", "/GUI/pictures/stock-manager.jpg");
        JButton backButton = new JButton("Back");

        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonsPanel.add(submitButton);
        buttonsPanel.add(backButton);

        buttonsPanel.setOpaque(false);

        mainPanel.add(buttonsPanel, BorderLayout.SOUTH);

        add(mainPanel, BorderLayout.CENTER);

        submitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
        // Handle submit button action
                invalidNameLabel.setVisible(false);
                invalidIDLabel.setVisible(false);
                invalidmWeightLabel.setVisible(false);
                invalidPriceLabel.setVisible(false);
                invalidAmountLabel.setVisible(false);
                invalidmaxRangeLabel.setVisible(false);
                invalidDiscountLabel.setVisible(false);
                int mistake_counter = 0;
                LinkedList<SupplierProduct> temp = supplier.getAgreement().getProductList();
                String name = nameLabelTextField.getText();
                String id =idTextField.getText();
                String weight = weightTextField.getText();
                String price = priceLabelTextField.getText();
                String amount = amountLabelTextField.getText();
                for(SupplierProduct sp: temp){
                    if(sp.getProduct_name().equals(name)){
                        mistake_counter++;
                        invalidNameLabel.setVisible(true);
                    }
                    if(checkIfPositiveIntNumber(id) && Integer.parseInt(id) == sp.getLocal_key()){
                        mistake_counter++;
                        invalidIDLabel.setVisible(true);
                    }
                }
                if(!checkIfPositiveIntNumber(id)){
                    mistake_counter++;
                    invalidIDLabel.setVisible(true);
                }
                if(!isValidString(name)){
                    mistake_counter++;
                    invalidNameLabel.setVisible(true);
                }
                if(!checkIfPositiveDoubleNumber(weight)){
                    mistake_counter++;
                    invalidmWeightLabel.setVisible(true);
                }
                if(!checkIfPositiveDoubleNumber(price)){
                    mistake_counter++;
                    invalidPriceLabel.setVisible(true);
                }
                if(!checkIfPositiveIntNumber(amount)){
                    mistake_counter++;
                    invalidAmountLabel.setVisible(true);

                }
                if(mistake_counter== 0) {
                    int ID = Integer.parseInt(id);
                    double weight1 = Double.parseDouble(weight);
                    double price1 = Double.parseDouble(price);
                    int amount1 = Integer.parseInt(amount);
                    if(discounts.get(discounts.size()-1).getAmountRange().getMax() < amount1){
                        discounts.get(discounts.size()-1).getAmountRange().setMax(amount1);
                    }
                    SupplierProduct sp = new SupplierProduct(name,ID,weight1,price1,amount1,discounts,supplier.getCard().getSupplier_number());
                    SupplyManager.getSupply_manager().addProductToAgreement(supplier.getAgreement(),sp, 0);
                    String productName=name;
                    if(SupplyManager.getSupply_manager().isThisProductAlreadyInSystem(productName))
                    {
                        SupplyManager.getSupply_manager().addSupplierByProduct(productName,supplier,true);
                    }
                    else
                    {
                        SupplyManager.getSupply_manager().addSupplierByProduct(productName,supplier,false);
                    }
                    JOptionPane.showMessageDialog(null, "Product "+ name+ " added to " + supplier.getCard().getSupplier_name());

                }
            }
        });

        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                invalidDiscountLabel.setVisible(false);
                invalidmWeightLabel.setVisible(false);
                invalidIDLabel.setVisible(false);
                invalidAmountLabel.setVisible(false);
                invalidPriceLabel.setVisible(false);
                invalidNameLabel.setVisible(false);


                discountLabelTextField.setText("");
                weightTextField.setText("");
                idTextField.setText("");
                priceLabelTextField.setText("");
                amountLabelTextField.setText("");
                nameLabelTextField.setText("");

                parent.showDefaultPanelFromChild();
            }
        });

    }


    private JPanel createTextFieldPanel(JLabel label, JTextField textField) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(label, BorderLayout.WEST);
        panel.add(textField, BorderLayout.CENTER);
        return panel;
    }

    // All the functions that check if the input is valid
    Boolean checkIfOnlyLetters(String str) {
        if (str.equals("")) {
            return false;
        }
        return str.matches("[a-zA-Z' ]+");
    }
    public boolean isValidString(String input) {
        // Regular expression pattern
        String pattern = "^[a-zA-Z0-9\\.]+$";

        // Check if the input matches the pattern
        return input.matches(pattern);
    }



    boolean checkIfPositiveDoubleNumber(String number) {
        try {
            if (number.equals("")) {
                return false;
            }
            double d = Double.parseDouble(number);
            return d > 0.0;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    boolean checkIfPositiveIntNumber(String number) {
        try {
            if (number.equals("")) {
                return false;
            }
            int d = Integer.parseInt(number);
            return d > 0.0;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}


