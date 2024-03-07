package GUI.stockmanagerGui;

import Stock.Business.Product;
import Stock.Service.MarketService;
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

public class ChangeMinQuantityGUI extends JPanel {
    private JPanel mainPanel;
    private ProductMenuGui parent;

    public ChangeMinQuantityGUI(ProductMenuGui parent) {
        this.parent = parent;
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
        JLabel titleLabel = new JLabel("<html><br>Change minimum quantity for product <br><br> </html>");
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 24));
        mainPanel.add(titleLabel, BorderLayout.NORTH);


        // Create text fields
        // Create text fields
        JLabel catalogNumberLabel = new JLabel("Catalog Number");
        Font catalogNumberLabelFont = catalogNumberLabel.getFont();
        Font catalogNumberLabelNewFont = catalogNumberLabelFont.deriveFont(Font.PLAIN, 18);
        catalogNumberLabel.setFont(catalogNumberLabelNewFont);

        JTextField catalogNumberTextField = new JTextField();
        Font catalogNumberTextFieldFont = catalogNumberTextField.getFont();
        Font catalogNumberTextFieldNewFont = catalogNumberTextFieldFont.deriveFont(Font.PLAIN, 18);
        catalogNumberTextField.setFont(catalogNumberTextFieldNewFont);

        JLabel invalidCatalogNumberLabel = new JLabel("Invalid Catalog Number");
        Font invalidCatalogNumberLabelFont = invalidCatalogNumberLabel.getFont();
        Font invalidCatalogNumberLabelNewFont = invalidCatalogNumberLabelFont.deriveFont(Font.PLAIN, 18);
        invalidCatalogNumberLabel.setFont(invalidCatalogNumberLabelNewFont);
        invalidCatalogNumberLabel.setForeground(Color.RED);


        JLabel newMinQuantity = new JLabel("New minimum Quantity");
        Font newMinQuantityFont = newMinQuantity.getFont();
        Font newMinQuantityNewFont = newMinQuantityFont.deriveFont(Font.PLAIN, 18);
        newMinQuantity.setFont(newMinQuantityNewFont);

        JTextField newMinQuantityTextField = new JTextField();
        Font newMinQuantityTextFieldFont = newMinQuantityTextField.getFont();
        Font newMinQuantityTextFieldNewFont = newMinQuantityTextFieldFont.deriveFont(Font.PLAIN, 18);
        newMinQuantityTextField.setFont(newMinQuantityTextFieldNewFont);

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
        inputPanel.add(invalidCatalogNumberLabel);
        invalidCatalogNumberLabel.setVisible(false);
        inputPanel.add(newMinQuantity);
        inputPanel.add(newMinQuantityTextField);
        inputPanel.add(invalidQuantityLabel);
        invalidQuantityLabel.setVisible(false);

        inputPanel.setOpaque(false);

        JPanel inputWrapperPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        inputWrapperPanel.add(inputPanel);
        mainPanel.add(inputWrapperPanel, BorderLayout.WEST);

        inputWrapperPanel.setOpaque(false);

        // Create button panel
        JButton submitButton = new JButton("Submit");
        //JButton submitButton = createButton("Submit", "/GUI/pictures/stock-manager.jpg");
        JButton backButton = new JButton("Back");

        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonsPanel.add(submitButton);
        buttonsPanel.add(backButton);

        buttonsPanel.setOpaque(false);
        buttonsPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0)); // 10 is the top padding

        mainPanel.add(buttonsPanel, BorderLayout.SOUTH);

        add(mainPanel, BorderLayout.CENTER);
        submitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Handle submit button action
                ProductService productService = ProductService.getInstance();

                MarketService marketService = MarketService.getInstance();
                ArrayList<JLabel> inputsArrayList = new ArrayList<>(Arrays.asList(
                        invalidCatalogNumberLabel, invalidQuantityLabel));
                ArrayList<Boolean> inputChecks = new ArrayList<>();

                String catalogNumberStr = catalogNumberTextField.getText();
                inputChecks.add(!catalogNumberStr.isEmpty());
                String quantityStr = newMinQuantityTextField.getText();
                inputChecks.add(checkIfPositiveDoubleNumber(quantityStr));

                boolean allTrue = !inputChecks.contains(Boolean.FALSE);

                if (allTrue) {
                    for (JLabel currentInput: inputsArrayList) {
                        currentInput.setVisible(false);
                    }
                    Product product = productService.getProductByUniqueCode(catalogNumberStr);
                    if (product != null) {
                        productService.setMinimumQuantity(product,Integer.parseInt(quantityStr), LocalDate.now());
                        catalogNumberTextField.setText("");
                        newMinQuantityTextField.setText("");
                        JOptionPane.showMessageDialog(null,"Min quantity of " + product.getName()+" was updated to " +quantityStr);
                    }
                    else {
                        JOptionPane.showMessageDialog(null,"Product Not Found");
                    }
                }
                else {
                    int index = 0;
                    for (boolean currentInputValid : inputChecks) {
                        // Perform operations on the 'element' using the index 'index'
                        if (!currentInputValid) {
                            inputsArrayList.get(index).setVisible(true);
                        }
                        else {
                            inputsArrayList.get(index).setVisible(false);
                        }
                        index++;
                    }
                }
            }
        });

        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                invalidCatalogNumberLabel.setVisible(false);
                invalidQuantityLabel.setVisible(false);

                catalogNumberTextField.setText("");
                newMinQuantityTextField.setText("");

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

}
