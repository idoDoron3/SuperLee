package GUI.supplyGui;

import Supplier_Module.Business.Managers.SupplyManager;
import Supplier_Module.Business.Supplier;
import Supplier_Module.DAO.SupplierDAO;
import Supplier_Module.DAO.SupplierProductDAO;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class EditSupplierPanel extends JPanel {
    private SupplierGUI parent;
    private JPanel mainPanel;
    private EditSupplierAgreemantGui editSupplierAgreemantGui;
    private EditSupplierCardGui editSupplierCardGui;
    private Supplier supplier;
    public EditSupplierPanel(SupplierGUI supplierGUI) {
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


        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.NORTH; // Align in the center horizontally
        gbc.insets = new Insets(10, 0, 10, 0); // Adjust spacing as needed

        JLabel titleLabel = new JLabel("Edit Supplier");
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 24));

        centerPanel.add(titleLabel, gbc);

        JPanel editPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel enterNumberLabel = new JLabel("Please enter the supplier number you want to Edit:");
        JTextField numberField = new JTextField(10);
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
            String supplierNumber = numberField.getText();
            // Perform delete action based on the supplier number
            if(isExistSupplier(supplierNumber))
            {
                this.supplier=SupplyManager.getSupply_manager().getSupplier(Integer.parseInt(supplierNumber));
                centerPanel.remove(editPanel);
                JPanel buttonPanel = new JPanel();
                buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
                buttonPanel.setOpaque(false);
                JButton editCardButton = null;
                try {
                    editCardButton = createButton("Edit Card","/GUI/pictures/new-supplier.jpg");
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                JButton editAgreementButton = null;
                try {
                    editAgreementButton = createButton("Edit Agreemant","/GUI/pictures/update.jpg");
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }

                buttonPanel.add(Box.createHorizontalGlue());
                buttonPanel.add(editCardButton);
                buttonPanel.add(Box.createHorizontalStrut(20));
                buttonPanel.add(editAgreementButton);
                buttonPanel.add(Box.createHorizontalGlue());

                gbcd.gridy =1;
                gbcd.anchor = GridBagConstraints.CENTER;
                centerPanel.add(buttonPanel, gbcd);
//                this.supplier = SupplierDAO.getInstance().getSupplier(Integer.parseInt(supplierNumber));//TODO


                // Add action listener for the yes button
                editCardButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        try {
                            openEditCardPanel();
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                });

                editAgreementButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        try {
                            openEditAgreemantPanel();
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                });

                // Refresh the panel to show the confirmation panel
                revalidate();
                repaint();
            }
            else {
                JOptionPane.showMessageDialog(null, "Invalid Supplier!", "ERROR",  JOptionPane.ERROR_MESSAGE);
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
    private JButton createButton(String text, String imagePath) throws IOException {
        // Create button panel
        int width = 150;
        int height = 150;
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
        Font buttonFont = new Font("Tahoma", Font.BOLD, 16);
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

    private void openEditCardPanel() throws IOException {
        mainPanel.setVisible(false);

        if (editSupplierCardGui == null) {
            editSupplierCardGui = new EditSupplierCardGui(this,supplier);
            editSupplierCardGui.setPreferredSize(mainPanel.getSize());
            editSupplierCardGui.setMaximumSize(mainPanel.getMaximumSize());
            editSupplierCardGui.setMinimumSize(mainPanel.getMinimumSize());
            editSupplierCardGui.setSize(mainPanel.getSize());
        }

        add(editSupplierCardGui, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    private void openEditAgreemantPanel() throws IOException {
        mainPanel.setVisible(false);

        if (editSupplierAgreemantGui == null) {
            editSupplierAgreemantGui = new EditSupplierAgreemantGui(this, supplier);
            editSupplierAgreemantGui.setPreferredSize(mainPanel.getSize());
            editSupplierAgreemantGui.setMaximumSize(mainPanel.getMaximumSize());
            editSupplierAgreemantGui.setMinimumSize(mainPanel.getMinimumSize());
            editSupplierAgreemantGui.setSize(mainPanel.getSize());
        }

        add(editSupplierAgreemantGui, BorderLayout.CENTER);
        revalidate();
        repaint();
    }
    public void showDefaultPanelFromChild() {
        mainPanel.setVisible(true);
        removeCurrentChildPanel();
        revalidate();
        repaint();
    }

    private void removeCurrentChildPanel() {
        if (editSupplierCardGui != null && editSupplierCardGui.isShowing()) {
            remove(editSupplierCardGui);
            editSupplierCardGui = null;
        } else if (editSupplierAgreemantGui != null && editSupplierAgreemantGui.isShowing()) {
            remove(editSupplierAgreemantGui);
            editSupplierAgreemantGui = null;
        }
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
}
