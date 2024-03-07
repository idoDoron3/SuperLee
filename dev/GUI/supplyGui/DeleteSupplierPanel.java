package GUI.supplyGui;

import Supplier_Module.Business.Managers.SupplyManager;
import Supplier_Module.Business.Supplier;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.LinkedList;

public class DeleteSupplierPanel extends JPanel {
    private SupplierGUI parent;
    private JPanel mainPanel;
    public DeleteSupplierPanel(SupplierGUI supplierGUI) {
        this.parent = supplierGUI;
        setLayout(new BorderLayout());

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

        GridBagConstraints titleConstraints = new GridBagConstraints();
        titleConstraints.gridx = 0; // Column index
        titleConstraints.gridy = 0; // Row index
        titleConstraints.anchor = GridBagConstraints.NORTH; // Align in the center horizontally
        titleConstraints.insets = new Insets(10, 0, 10, 0);

        JLabel titleLabel = new JLabel("Delete Supplier");
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 24));

        centerPanel.add(titleLabel, titleConstraints);

        JPanel deletePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel enterNumberLabel = new JLabel("Please enter the supplier number you want to delete:");
        JTextField numberField = new JTextField(10);
        JButton submitButton = new JButton("Submit");

        deletePanel.add(enterNumberLabel);
        deletePanel.add(numberField);
        deletePanel.add(submitButton);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weighty = 0.5; // Place components vertically in the middle
        gbc.anchor = GridBagConstraints.NORTH; // Center-align components
        centerPanel.add(deletePanel, gbc);

        // Add action listener for the submit button
        submitButton.addActionListener(e -> {
            String supplierNumber = numberField.getText();
            if(isExistSupplier(supplierNumber))
            {
                if (!isHasPeriodicOrders(Integer.parseInt(supplierNumber)))
                {
                    centerPanel.remove(deletePanel);
                    GridBagConstraints gbcDetails = new GridBagConstraints();
                    gbcDetails.gridx = 0;
                    gbcDetails.gridy = 1;
                    gbcDetails.weighty = 0.5; // Place component vertically in the middle
                    gbcDetails.anchor = GridBagConstraints.CENTER; // Center-align component

                    //////////////////////////////////////////////////////////////////////
                    LinkedList<String> lines = SupplyManager.getSupply_manager().getSupplier(Integer.parseInt(supplierNumber)).getSupplierReport();
                    JTextArea textArea = new JTextArea();
                    textArea.setFont(new Font("Comic Sans MS", Font.BOLD, 18));

                    // Concatenate the lines with line breaks
                    StringBuilder reportBuilder = new StringBuilder();
                    int longestLineWidth = 0;
                    for (String line : lines) {
                        reportBuilder.append(line).append("\n");
                        int lineWidth = SwingUtilities.computeStringWidth(textArea.getFontMetrics(textArea.getFont()), line);
                        longestLineWidth = Math.max(longestLineWidth, lineWidth);
                    }
                    String reportText = reportBuilder.toString();
                    textArea.setText(reportText);
                    textArea.setPreferredSize(new Dimension(longestLineWidth, textArea.getPreferredSize().height));
                    textArea.setOpaque(true);
                    textArea.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                    textArea.setEditable(false);
                    centerPanel.add(textArea, gbc);
                    //////////////////////////////////////////////////////////////////////

                    // Example: Print a message on the panel
                    JTextArea messageArea = new JTextArea();
                    messageArea.setEditable(false);
                    messageArea.append("Are you sure you want to delete supplier " + supplierNumber + "?\n");
                    messageArea.setOpaque(false);
                    messageArea.setFont(new Font("Tahoma", Font.BOLD, 12));
                    JButton yesButton = new JButton("Yes");
                    JButton noButton = new JButton("No");

                    JPanel confirmationPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
                    confirmationPanel.add(messageArea);
                    confirmationPanel.add(yesButton);
                    confirmationPanel.add(noButton);

                    GridBagConstraints gbcConfirmation = new GridBagConstraints();
                    gbcConfirmation.gridx = 0;
                    gbcConfirmation.gridy = 2;
                    gbcConfirmation.weighty = 0.5; // Place component at the bottom
                    gbcConfirmation.anchor = GridBagConstraints.PAGE_END; // Align component to the bottom
                    gbcConfirmation.insets = new Insets(10, 0, 10, 0); // Add some spacing
                    centerPanel.add(confirmationPanel, gbcConfirmation);

                    // Add action listener for the yes button
                    yesButton.addActionListener(yesEvent -> {
                        // Perform delete confirmation action
                        Supplier supToRemove= SupplyManager.getSupply_manager().getSupplier(Integer.parseInt(supplierNumber));
                        SupplyManager.getSupply_manager().removeSupplier(supToRemove);
                        JOptionPane.showMessageDialog(null, "Supplier " + supplierNumber + " has been deleted.");
                        confirmationPanel.setVisible(false);
                    });

                    // Add action listener for the no button
                    noButton.addActionListener(noEvent -> {
                        // Perform cancel action
                        JOptionPane.showMessageDialog(null, "Deletion canceled.");
                    });

                    // Refresh the panel to show the confirmation panel
                    revalidate();
                    repaint();
                }
                else
                {
                    JOptionPane.showMessageDialog(null, "this supplier has period order!", "ERROR",  JOptionPane.ERROR_MESSAGE);

                }
            }
            else
            {
                JOptionPane.showMessageDialog(null, "invalid input!", "ERROR",  JOptionPane.ERROR_MESSAGE);
            }

        });

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton backButton = new JButton("Back");
        bottomPanel.setOpaque(false);
        bottomPanel.add(backButton);

//        mainPanel.add(bottomPanel, BorderLayout.WEST);
//        add(mainPanel,BorderLayout.CENTER);
        mainPanel.add(centerPanel,BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
        add(mainPanel, BorderLayout.CENTER);
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                parent.showDefaultPanelFromChild();
            }
        });

    }

    public boolean isPositiveInteger(String input) {
        if (input == null || input.isEmpty()) {
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

    public boolean isExistSupplier(String input)
    {
        if(!isPositiveInteger(input))
            return false;
        return SupplyManager.getSupply_manager().isExist(Integer.parseInt(input));

    }
    public boolean isHasPeriodicOrders(int supplierID)
    {
        return SupplyManager.getSupply_manager().isHasPeriodicOrders(supplierID);
    }
 }

