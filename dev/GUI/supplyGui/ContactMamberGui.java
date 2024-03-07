package GUI.supplyGui;

import Supplier_Module.Business.Card.SupplierCard;
import Supplier_Module.Business.Supplier;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class ContactMamberGui extends JPanel {
    private EditSupplierCardGui parent;
    private JPanel mainPanel;
    private AddContactGui addContactGui;
    private DeleteContactGui deleteContactGui;
    private SupplierCard supplierCard;
    private  Supplier supplier;
    public ContactMamberGui(EditSupplierCardGui editSupplierCardGui, Supplier supplier1) throws IOException {
        this.parent = editSupplierCardGui;
        this.supplier = supplier1;
        this.supplierCard=supplier1.getCard();
        setLayout(new BorderLayout());

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
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.NORTH; // Align in the center horizontally
        gbc.insets = new Insets(10, 0, 10, 0); // Adjust spacing as needed

        JLabel titleLabel = new JLabel("Manage Contact Members");
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 24));
        centerPanel.add(titleLabel, gbc);


        // Create button panel
        JButton backButton = new JButton("Back");
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.setOpaque(false);

        // Create buttons
        JButton addMemberButton = createButton("Add Contact Member","/GUI/pictures/new-supplier.jpg");
        JButton deleteMemberButton = createButton("Delete Contact Member","/GUI/pictures/update.jpg");

        buttonPanel.add(Box.createHorizontalGlue());
        buttonPanel.add(addMemberButton);
        buttonPanel.add(Box.createHorizontalStrut(20));
        buttonPanel.add(deleteMemberButton);
        buttonPanel.add(Box.createHorizontalGlue());
        gbc.gridy =1;
        centerPanel.add(buttonPanel,gbc);


        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.setOpaque(false);
        bottomPanel.add(backButton);

        // Add button panel to the main panel
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(mainPanel, BorderLayout.CENTER);

        // Add action listeners for the buttons
        addMemberButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                openAddContactPanel();
            }
        });

        deleteMemberButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                openDeleteContactPanel();
            }
        });


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

    private void openAddContactPanel() {
        mainPanel.setVisible(false);

        if (addContactGui == null) {
            addContactGui = new AddContactGui(this, supplierCard);
            addContactGui.setPreferredSize(mainPanel.getSize());
            addContactGui.setMaximumSize(mainPanel.getMaximumSize());
            addContactGui.setMinimumSize(mainPanel.getMinimumSize());
            addContactGui.setSize(mainPanel.getSize());
        }

        add(addContactGui, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    private void openDeleteContactPanel() {
        mainPanel.setVisible(false);

        if (deleteContactGui == null) {
            deleteContactGui = new DeleteContactGui(this, supplierCard);
            deleteContactGui.setPreferredSize(mainPanel.getSize());
            deleteContactGui.setMaximumSize(mainPanel.getMaximumSize());
            deleteContactGui.setMinimumSize(mainPanel.getMinimumSize());
            deleteContactGui.setSize(mainPanel.getSize());
        }

        add(deleteContactGui, BorderLayout.CENTER);
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
        if (addContactGui != null && addContactGui.isShowing()) {
            remove(addContactGui);
            addContactGui = null;
        } else if (deleteContactGui != null && deleteContactGui.isShowing()) {
            remove(deleteContactGui);
            deleteContactGui = null;
        }
    }
}
