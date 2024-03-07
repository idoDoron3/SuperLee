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

public class AddShelvesToMarketGUI extends JPanel {

    private JPanel mainPanel;
    private MarketMenuGui parent;

    public AddShelvesToMarketGUI(MarketMenuGui parent) {
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
        JLabel titleLabel = new JLabel("<html><br> Add More Shelves To Market <br><br> </html>");
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 24));
        mainPanel.add(titleLabel, BorderLayout.NORTH);


        // Create text fields
        JLabel extraShelvesLabel = new JLabel("Shelves");
        Font extraShelvesLabelFont = extraShelvesLabel.getFont();
        Font extraShelvesLabelNewFont = extraShelvesLabelFont.deriveFont(Font.PLAIN, 18);
        extraShelvesLabel.setFont(extraShelvesLabelNewFont);

        JTextField extraShelvesTextField = new JTextField(12);
        Font extraShelvesTextFieldFont = extraShelvesTextField.getFont();
        Font extraShelvesTextFieldNewFont = extraShelvesTextFieldFont.deriveFont(Font.PLAIN, 18);
        extraShelvesTextField.setFont(extraShelvesTextFieldNewFont);

        JLabel invalidExtraShelvesLabel = new JLabel("Invalid Number Of Shelves");
        Font invalidExtraShelvesLabelFont = invalidExtraShelvesLabel.getFont();
        Font invalidExtraShelvesLabelNewFont = invalidExtraShelvesLabelFont.deriveFont(Font.PLAIN, 18);
        invalidExtraShelvesLabel.setFont(invalidExtraShelvesLabelNewFont);
        invalidExtraShelvesLabel.setForeground(Color.RED);


        JPanel inputPanel = new JPanel();
        int verticalGap = 35; // Set the desired vertical gap between rows
        int horizontalGap = 15;
        inputPanel.setLayout(new GridLayout(1, 3, horizontalGap, verticalGap));
        inputPanel.add(extraShelvesLabel);
        inputPanel.add(extraShelvesTextField);
        inputPanel.add(invalidExtraShelvesLabel);
        invalidExtraShelvesLabel.setVisible(false);

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
                String extraShelvesStr = extraShelvesTextField.getText();
                boolean validInput = checkIfPositiveIntegerNumber(extraShelvesStr);

                if (validInput) {
                    invalidExtraShelvesLabel.setVisible(false);
                    marketService.appendMarket(Integer.parseInt(extraShelvesStr));
                    extraShelvesTextField.setText("");
                    JOptionPane.showMessageDialog(null,"Number Of Shelves updated");
                }
                else {
                    invalidExtraShelvesLabel.setVisible(true);
                }
            }
        });

        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                invalidExtraShelvesLabel.setVisible(false);

                extraShelvesTextField.setText("");

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
    Boolean checkIfPositiveIntegerNumber(String number) {
        return number.matches("[0-9]+") && Integer.parseInt(number) > 0;
    }


}