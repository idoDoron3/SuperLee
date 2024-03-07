package GUI.supplyGui;

import Supplier_Module.Business.Card.ContactMember;
import Supplier_Module.Business.Card.SupplierCard;
import Supplier_Module.Business.Managers.SupplyManager;
import Supplier_Module.Business.Supplier;
import Supplier_Module.DAO.ContactMemberDAO;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.LinkedList;
import java.util.regex.Pattern;

public class DeleteContactGui extends JPanel {
    private ContactMamberGui parent;
    private JPanel mainPanel;
    private SupplierCard supplierCard;
    public DeleteContactGui(ContactMamberGui contactMamberGui, SupplierCard supplierCard1) {
        this.parent = contactMamberGui;
        this.supplierCard = supplierCard1;


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

        GridBagConstraints titleConstraints = new GridBagConstraints();
        titleConstraints.gridx = 0; // Column index
        titleConstraints.gridy = 0; // Row index
        titleConstraints.anchor = GridBagConstraints.NORTH; // Align in the top vertically
        titleConstraints.insets = new Insets(20, 0, 20, 0); // Add some top and bottom margin

        JLabel titleLabel = new JLabel("Delete Contact Member");
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 24));
        centerPanel.add(titleLabel, titleConstraints);

        JPanel deletePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel enterNumberLabel = new JLabel("Please enter the contact member phone number you want to delete:");
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
            String phone_number = numberField.getText();
            if(isExistContact(this.supplierCard,phone_number))
            {
                /////////////////////////////////////////////////////////////////////////
                LinkedList<String> lines = getContact(this.supplierCard,phone_number).getContactReport();
                StringBuilder reportBuilder = new StringBuilder();
                JTextArea textArea = new JTextArea();
                textArea.setFont(new Font("Comic Sans MS", Font.BOLD, 18));

                // Concatenate the lines with line breaks
                //StringBuilder reportBuilder = new StringBuilder();
                int longestLineWidth = 0;
                for (String line : lines) {
                    reportBuilder.append(line).append("\n");
                    int lineWidth = SwingUtilities.computeStringWidth(textArea.getFontMetrics(textArea.getFont()), line);
                    longestLineWidth = Math.max(longestLineWidth, lineWidth);
                }
                String reportText = reportBuilder.toString();
                textArea.setText(reportText);
                textArea.setPreferredSize(new Dimension(longestLineWidth, textArea.getPreferredSize().height));
                textArea.setOpaque(false);
                /////////////////////////////////////////////////////////////////////////

                centerPanel.remove(deletePanel);
                GridBagConstraints gbcDetails = new GridBagConstraints();
                gbcDetails.gridx = 0;
                gbcDetails.gridy = 1;
                gbcDetails.weighty = 0.5; // Place component vertically in the middle
                gbcDetails.anchor = GridBagConstraints.CENTER; // Center-align component
                JLabel contactDetails = new JLabel(reportText);
                //centerPanel.add(contactDetails, gbcDetails);
                centerPanel.add(textArea, gbcDetails);

                // Example: Print a message on the panel
                JTextArea messageArea = new JTextArea();
                messageArea.setEditable(false);
                messageArea.append("Are you sure you want to delete  " + phone_number + "?\n");
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
                    SupplyManager.getSupply_manager().removeContact_members(this.supplierCard,phone_number);
                    JOptionPane.showMessageDialog(null, "contact member has been deleted.");
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
                JOptionPane.showMessageDialog(null, "invalid input!", "ERROR", JOptionPane.ERROR_MESSAGE);
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
    public static boolean isInteger(String input) {
        try {
            Integer.parseInt(input);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public boolean isExistContact(SupplierCard supplierCard, String phoneNumber)
    {
       for(ContactMember c: supplierCard.getContact_members())
       {
           if(c.getPhone_number().equals(phoneNumber))
               return true;
       }
        return false;
    }

    public ContactMember getContact(SupplierCard supplierCard, String phoneNumber)
    {
        for(ContactMember c: supplierCard.getContact_members())
        {
            if(c.getPhone_number().equals(phoneNumber))
                return c;
        }
        return null;
    }



}
