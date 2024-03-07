package GUI.stockmanagerGui;

import Stock.Service.MarketService;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class DiscountByCatalogNumberGUI extends JPanel {

    private JPanel mainPanel;
    private MarketMenuGui parent;

    public DiscountByCatalogNumberGUI(MarketMenuGui parent) {
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
        JLabel titleLabel = new JLabel("<html><br> Discount By Catalog Number <br><br> </html>");
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 24));
        mainPanel.add(titleLabel, BorderLayout.NORTH);


        // Create text fields
        JLabel catalogNumberLabel = new JLabel("Catalog Number");
        Font catalogNumberLabelFont = catalogNumberLabel.getFont();
        Font catalogNumberLabelNewFont = catalogNumberLabelFont.deriveFont(Font.PLAIN, 18);
        catalogNumberLabel.setFont(catalogNumberLabelNewFont);

        JTextField catalogNumberTextField = new JTextField(12);
        Font catalogNumberTextFieldFont = catalogNumberTextField.getFont();
        Font catalogNumberTextFieldNewFont = catalogNumberTextFieldFont.deriveFont(Font.PLAIN, 18);
        catalogNumberTextField.setFont(catalogNumberTextFieldNewFont);

        JLabel invalidCatalogNumberLabel = new JLabel("Invalid Catalog Number");
        Font invalidCatalogNumberLabelFont = invalidCatalogNumberLabel.getFont();
        Font invalidCatalogNumberLabelNewFont = invalidCatalogNumberLabelFont.deriveFont(Font.PLAIN, 18);
        invalidCatalogNumberLabel.setFont(invalidCatalogNumberLabelNewFont);
        invalidCatalogNumberLabel.setForeground(Color.RED);


        JLabel discountLabel = new JLabel("Discount");
        Font discountLabelFont = discountLabel.getFont();
        Font discountLabelNewFont = discountLabelFont.deriveFont(Font.PLAIN, 18);
        discountLabel.setFont(discountLabelNewFont);

        JTextField discountTextField = new JTextField(12);
        Font discountTextFieldFont = discountTextField.getFont();
        Font discountTextFieldNewFont = discountTextFieldFont.deriveFont(Font.PLAIN, 18);
        discountTextField.setFont(discountTextFieldNewFont);

        JLabel invalidDiscountLabel = new JLabel("Invalid Discount");
        Font invalidDiscountLabelFont = invalidDiscountLabel.getFont();
        Font invalidDiscountLabelNewFont = invalidDiscountLabelFont.deriveFont(Font.PLAIN, 18);
        invalidDiscountLabel.setFont(invalidDiscountLabelNewFont);
        invalidDiscountLabel.setForeground(Color.RED);


        JPanel inputPanel = new JPanel();
        int verticalGap = 35; // Set the desired vertical gap between rows
        int horizontalGap = 15;
        inputPanel.setLayout(new GridLayout(2, 3, horizontalGap, verticalGap));
        inputPanel.add(catalogNumberLabel);
        inputPanel.add(catalogNumberTextField);
        inputPanel.add(invalidCatalogNumberLabel);
        invalidCatalogNumberLabel.setVisible(false);
        inputPanel.add(discountLabel);
        inputPanel.add(discountTextField);
        inputPanel.add(invalidDiscountLabel);
        invalidDiscountLabel.setVisible(false);

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
                MarketService marketService = MarketService.getInstance();
                ArrayList<JLabel> inputsArrayList = new ArrayList<>(Arrays.asList(
                        invalidCatalogNumberLabel, invalidDiscountLabel));
                ArrayList<Boolean> inputChecks = new ArrayList<>();

                String catalogNumberStr = catalogNumberTextField.getText();
                inputChecks.add(!catalogNumberStr.isEmpty());
                String discountStr = discountTextField.getText();
                inputChecks.add(checkIfPositiveDoubleNumber(discountStr) &&
                        !(0 > Double.parseDouble(discountStr) || Double.parseDouble(discountStr) > 1));

                boolean allTrue = !inputChecks.contains(Boolean.FALSE);

                if (allTrue) {
                    for (JLabel currentInput: inputsArrayList) {
                        currentInput.setVisible(false);
                    }
                    if (marketService.setDiscountForProduct(catalogNumberStr,Double.parseDouble(discountStr))) {
                        catalogNumberTextField.setText("");
                        discountTextField.setText("");
                        JOptionPane.showMessageDialog(null,"Discount updated");
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
                invalidDiscountLabel.setVisible(false);

                catalogNumberTextField.setText("");
                discountTextField.setText("");

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

    private JPanel createTextFieldPanel(JLabel label, JTextField textField) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(label, BorderLayout.WEST);
        panel.add(textField, BorderLayout.CENTER);
        return panel;
    }

    // All the functions that check if the input is valid
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