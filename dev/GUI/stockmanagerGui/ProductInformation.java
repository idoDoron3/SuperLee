package GUI.stockmanagerGui;

import GUI.storeGui.stockReport.MainStockReport;
import Stock.Service.ProductService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ProductInformation extends JPanel {


        private ProductMenuGui parent;
        private JTextArea textArea;

        public ProductInformation(ProductMenuGui mainStockReport) {
            this.parent = mainStockReport;
            setLayout(new BorderLayout());

            JLabel titleLabel = new JLabel("<html><br>Product Information :<br><br> </html>");
            titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
            titleLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 24));
            add(titleLabel, BorderLayout.NORTH);

            JPanel product_report_ = new JPanel(new FlowLayout(FlowLayout.CENTER));
            JLabel enterNameLabel = new JLabel("Please enter the product name you want to get details on:");
            JTextField nameField = new JTextField(20);
            JButton submitButton = new JButton("Submit");

            product_report_.add(enterNameLabel);
            product_report_.add(nameField);
            product_report_.add(submitButton);

            add(product_report_, BorderLayout.CENTER);

            // Add action listener for the submit button
            submitButton.addActionListener(e -> {
                String product = nameField.getText();
                // Perform
                String report = ProductService.getInstance().productReport(product);
                if (report.equals("")) {
                    JOptionPane.showMessageDialog(null, "Category was not found!");
                } else {
                    if (textArea != null) {
                        product_report_.remove(textArea);
                    }
                    textArea = new JTextArea(report);
                    textArea.setFont(new Font("Comic Sans MS", Font.BOLD, 18));
                    // ... (set other properties)

                    product_report_.add(textArea);

                    // Refresh the panel to show the confirmation panel
                    revalidate();
                    repaint();
                }
            });
            JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            JButton backButton = new JButton("Back");
            bottomPanel.add(backButton);

            add(bottomPanel, BorderLayout.WEST);
            backButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    parent.showDefaultPanelFromChild();
                    if (textArea != null) {
                        product_report_.remove(textArea);
                    }
                }
            });

        }

}

