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
import java.util.Arrays;
import java.util.Date;

public class AddQuantity extends JPanel {

    public AddQuantity(){

        setOpaque(true);

        JLabel categoryLabel = new JLabel("Category");
        Font categoryLabelFont = categoryLabel.getFont();
        Font categoryLabelNewFont = categoryLabelFont.deriveFont(Font.PLAIN, 18);
        categoryLabel.setFont(categoryLabelNewFont);

        JTextField categoryTextField = new JTextField();
        Font categoryTextFieldFont = categoryTextField.getFont();
        Font categoryTextFieldNewFont = categoryTextFieldFont.deriveFont(Font.PLAIN, 18);
        categoryTextField.setFont(categoryTextFieldNewFont);

        JLabel invalidCategoryLabel = new JLabel("Invalid Category");
        Font invalidCategoryLabelFont = invalidCategoryLabel.getFont();
        Font invalidCategoryLabelNewFont = invalidCategoryLabelFont.deriveFont(Font.PLAIN, 18);
        invalidCategoryLabel.setFont(invalidCategoryLabelNewFont);
        invalidCategoryLabel.setForeground(Color.RED);


        JLabel subCategoryLabel = new JLabel("Sub Category");
        Font subCategoryLabelFont = subCategoryLabel.getFont();
        Font subCategoryLabelNewFont = subCategoryLabelFont.deriveFont(Font.PLAIN, 18);
        subCategoryLabel.setFont(subCategoryLabelNewFont);

        JTextField subCategoryTextField = new JTextField();
        Font subCategoryTextFieldFont = subCategoryTextField.getFont();
        Font subCategoryTextFieldNewFont = subCategoryTextFieldFont.deriveFont(Font.PLAIN, 18);
        subCategoryTextField.setFont(subCategoryTextFieldNewFont);

        JLabel invalidSubCategoryLabel = new JLabel("Invalid Sub Category");
        Font invalidSubCategoryLabelFont = invalidSubCategoryLabel.getFont();
        Font invalidSubCategoryLabelNewFont = invalidSubCategoryLabelFont.deriveFont(Font.PLAIN, 18);
        invalidSubCategoryLabel.setFont(invalidSubCategoryLabelNewFont);
        invalidSubCategoryLabel.setForeground(Color.RED);


        JLabel subSubCategoryLabel = new JLabel("Sub Sub Category");
        Font subSubCategoryLabelFont = subSubCategoryLabel.getFont();
        Font subSubCategoryLabelNewFont = subSubCategoryLabelFont.deriveFont(Font.PLAIN, 18);
        subSubCategoryLabel.setFont(subSubCategoryLabelNewFont);

        JTextField subSubCategoryTextField = new JTextField();
        Font subSubCategoryTextFieldFont = subSubCategoryTextField.getFont();
        Font subSubCategoryTextFieldNewFont = subSubCategoryTextFieldFont.deriveFont(Font.PLAIN, 18);
        subSubCategoryTextField.setFont(subSubCategoryTextFieldNewFont);

        JLabel invalidSubSubCategoryLabel = new JLabel("Invalid Sub Sub Category");
        Font invalidSubSubCategoryLabelFont = invalidSubSubCategoryLabel.getFont();
        Font invalidSubSubCategoryLabelNewFont = invalidSubSubCategoryLabelFont.deriveFont(Font.PLAIN, 18);
        invalidSubSubCategoryLabel.setFont(invalidSubSubCategoryLabelNewFont);
        invalidSubSubCategoryLabel.setForeground(Color.RED);

        JLabel quantityLabel = new JLabel("Added Quantity");
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


        JLabel dateLabel = new JLabel("Date (DD/MM/YYYY)");
        Font dateLabelFont = dateLabel.getFont();
        Font dateLabelNewFont = dateLabelFont.deriveFont(Font.PLAIN, 18);
        dateLabel.setFont(dateLabelNewFont);

        JTextField dateTextField = new JTextField();
        Font dateTextFieldFont = dateTextField.getFont();
        Font dateTextFieldNewFont = dateTextFieldFont.deriveFont(Font.PLAIN, 18);
        dateTextField.setFont(dateTextFieldNewFont);

        JLabel invalidDateLabel = new JLabel("Invalid Date");
        Font invalidDateLabelFont = invalidDateLabel.getFont();
        Font invalidDateLabelNewFont = invalidDateLabelFont.deriveFont(Font.PLAIN, 18);
        invalidDateLabel.setFont(invalidDateLabelNewFont);
        invalidDateLabel.setForeground(Color.RED);

//        quantityTextField.setColumns(15);
//        categoryTextField.setColumns(15);
//        dateTextField.setColumns(15);
//        subCategoryTextField.setColumns(15);
//        subSubCategoryTextField.setColumns(15);


        JPanel inputPanel = new JPanel();
        int verticalGap = 30; // Set the desired vertical gap between rows
        int horizontalGap = 15;
        inputPanel.setLayout(new GridLayout(5, 3, horizontalGap, verticalGap));
        inputPanel.add(categoryLabel);
        inputPanel.add(categoryTextField);
        inputPanel.add(invalidCategoryLabel);
        invalidCategoryLabel.setVisible(false);
        inputPanel.add(subCategoryLabel);
        inputPanel.add(subCategoryTextField);
        inputPanel.add(invalidSubCategoryLabel);
        invalidSubCategoryLabel.setVisible(false);
        inputPanel.add(subSubCategoryLabel);
        inputPanel.add(subSubCategoryTextField);
        inputPanel.add(invalidSubSubCategoryLabel);
        invalidSubSubCategoryLabel.setVisible(false);
        inputPanel.add(quantityLabel);
        inputPanel.add(quantityTextField);
        inputPanel.add(invalidQuantityLabel);
        invalidQuantityLabel.setVisible(false);
        inputPanel.add(dateLabel);
        inputPanel.add(dateTextField);
        inputPanel.add(invalidDateLabel);
        invalidDateLabel.setVisible(false);

        inputPanel.setOpaque(false);

        JPanel inputWrapperPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        inputWrapperPanel.add(inputPanel);
        add(inputWrapperPanel, BorderLayout.WEST);

        inputWrapperPanel.setOpaque(false);

        // Create button panel
        JButton submitButton = new JButton("Submit");

        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonsPanel.add(submitButton, BorderLayout.WEST);

        buttonsPanel.setOpaque(false);

        inputWrapperPanel.add(buttonsPanel, BorderLayout.EAST);


//
//        JLabel category = new JLabel("Category");
//        JTextField categoryLabel = new JTextField();
//        categoryLabel.setColumns(15);
//
//        JLabel subCategory = new JLabel("Sub Category");
//        JTextField subCategoryLabel = new JTextField();
//        subCategoryLabel.setColumns(15);
//
//        JLabel subSubCategory = new JLabel("Sub Sub Category");
//        JTextField subSubCategoryLabel = new JTextField();
//        subSubCategoryLabel.setColumns(15);
//
//        JLabel quantity = new JLabel("Quantity");
//        JTextField quantityLabel = new JTextField();
//        quantityLabel.setColumns(15);
//        JPanel texts = new JPanel();
//        texts.setLayout(new BoxLayout(texts, BoxLayout.Y_AXIS));
//        texts.setOpaque(false);
//
//        texts.add(createTextFieldPanel(category,categoryLabel));
//        texts.add(createTextFieldPanel(subCategory,subCategoryLabel));
//        texts.add(createTextFieldPanel(subSubCategory,subSubCategoryLabel));
//        texts.add(createTextFieldPanel(quantity,quantityLabel));
//        add(texts,BorderLayout.CENTER);

//        try {
//            JButton submit = createButton("Submit", "/GUI/pictures/stock-manager.jpg");
//            add(submit,BorderLayout.SOUTH);
//


            setVisible(true);

            submitButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    // Handle submit button action
                    ProductService productService = ProductService.getInstance();
                    LocalDate localDate = LocalDate.now();
                    ArrayList<JLabel> inputsArrayList = new ArrayList<>(Arrays.asList(
                            invalidCategoryLabel, invalidSubCategoryLabel, invalidSubSubCategoryLabel,
                            invalidQuantityLabel,invalidDateLabel));
                    ArrayList<Boolean> inputChecks = new ArrayList<>();

                    String categoryStr = categoryTextField.getText();
                    inputChecks.add(checkIfOnlyLetters(categoryStr));
                    String subCategoryStr = subCategoryTextField.getText();
                    inputChecks.add(checkSubCategory(subCategoryStr));
                    String subSubCategoryStr = subSubCategoryTextField.getText();
                    inputChecks.add(checkSubSubCategory(subSubCategoryStr));
                    String quantityStr = quantityTextField.getText();
                    inputChecks.add(!quantityStr.equals("") && checkIfPositiveIntegerNumber(quantityStr));
                    String dateStr = dateTextField.getText();
                    Date expirationDate = dateInput(dateStr);
                    inputChecks.add(expirationDate != null);

                    boolean allTrue = !inputChecks.contains(Boolean.FALSE);

                    if (allTrue) {
                        for (JLabel currentInput : inputsArrayList) {
                            currentInput.setVisible(false);
                        }
                        Product product = productService.getProductByCategories(subCategoryStr,subSubCategoryStr);
                        if (product != null) {
                            productService.addMoreItemsToProduct(product,expirationDate,Integer.parseInt(quantityStr));
                            categoryTextField.setText("");
                            subCategoryTextField.setText("");
                            subSubCategoryTextField.setText("");
                            quantityTextField.setText("");
                            dateTextField.setText("");
                            JOptionPane.showMessageDialog(null, product.getName()+" Quantity was updated");
                        } else {
                            JOptionPane.showMessageDialog(null, "The product wan not found");
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

    Boolean checkIfOnlyLetters(String str) {
        if(str.equals("")){
            return false;
        }
        return str.matches("[a-zA-Z' ]+");
    }
    Boolean checkSubCategory(String subCategoryStr) {
        if(subCategoryStr.equals("")){
            return false;
        }
        return subCategoryStr.matches("[a-zA-Z0-9% ]+");
    }
    Boolean checkSubSubCategory(String subSubCategoryStr) {
        /**
         * Checks if a given string matches the format of a sub-sub category.
         * A sub-sub category should consist of a number followed by a space and a word.
         * Example: "5 g", "1 l", "10 p".
         *
         * @param subSubCategoryStr the sub-sub category string to be checked
         * @return true if the string matches the format of a sub-sub category, false otherwise
         */
        if(subSubCategoryStr.equals("")){
            return false;
        }
        String[] parts = subSubCategoryStr.split(" ");
        double number;
        if (parts.length == 2) {
            try {
                number = Double.parseDouble(parts[0]);
                String word = parts[1];
                if (!word.matches("[a-zA-Z]+")) {
                    System.out.println("your product's subSubCategory does not match the format.");
                    return false;
                }
            } catch (NumberFormatException e) {
                System.out.println("your product's subSubCategory does not match the format.");
                return false;
            }
        } else {
            System.out.println("your product's subSubCategory does not match the format.");
            return false;
        }
        return true;
    }
    boolean checkIfPositiveDoubleNumber(String number) {
        try {
            if(number.equals("")){
                return false;
            }
            double d = Double.parseDouble(number);
            return d > 0.0;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    Boolean checkIfPositiveIntegerNumber(String number) {
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
    Date dateInput(String dateStr) {
        /**
         * Prompts the user to enter a date in the format "dd/MM/yyyy" and returns it as a Date object.
         * If the date is invalid or in the past, it prints an error message and returns null.
         *
         * @return the date entered by the user as a Date object, or null if it is invalid or in the past.
         */
        if(dateStr.equals("") || !dateStr.contains("/")){
            return null;
        }
        String[] parts = dateStr.split("/");
        try {
            int day = Integer.parseInt(parts[0]);
            int month = Integer.parseInt(parts[1]);
            int year = Integer.parseInt(parts[2]);


            if (month < 1 || month > 12) {
                System.out.println("Invalid month");
                return null;
            }
            if (day < 1 || day > 31) {
                System.out.println("Invalid day");
                return null;
            }
            LocalDate date = LocalDate.of(year, month, day);
            if (date.isBefore(LocalDate.now())) {
                System.out.println("The date is not in the future");
                return null;
            }
            Date dateToReturn = Date.from(date.atStartOfDay().atZone(java.time.ZoneId.systemDefault()).toInstant());
            return dateToReturn;
        } catch (NumberFormatException e) {
            return null;
        }
    }


}
