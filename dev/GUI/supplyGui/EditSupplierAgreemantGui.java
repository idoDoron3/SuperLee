package GUI.supplyGui;

import Supplier_Module.Business.Supplier;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class EditSupplierAgreemantGui extends JPanel {
    private EditSupplierPanel parent;
    private Supplier supplier;
    private JPanel mainPanel;
    private AddSupplierProduct addSupplierProduct;
    private  EditSupplierProduct editSupplierProduct;
    private  DeleteSupplieProduct deleteSupplieProduct;

    public EditSupplierAgreemantGui(EditSupplierPanel editSupplierPanel, Supplier supplier1) throws IOException {
        this.parent = editSupplierPanel;
        this.supplier =supplier1;
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

        JLabel titleLabel = new JLabel("Edit Supplier Agreement");
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 24));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(20, 0, 20, 0); // Adjust spacing as needed
        centerPanel.add(titleLabel, gbc);

        JButton backButton = new JButton("Back");
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.setOpaque(false);

        // Create buttons
        JButton addProductButton = createButton("Add Product","/GUI/pictures/new-supplier.jpg");
        JButton editProductButton = createButton("Edit Product","/GUI/pictures/update.jpg");
        JButton deleteProductButton = createButton("Delete Product","/GUI/pictures/delete-supplier.jpg");

        buttonPanel.add(Box.createHorizontalGlue());
        buttonPanel.add(addProductButton);
        buttonPanel.add(Box.createHorizontalStrut(20));
        buttonPanel.add(editProductButton);
        buttonPanel.add(Box.createHorizontalStrut(20));
        buttonPanel.add(deleteProductButton);
        buttonPanel.add(Box.createHorizontalGlue());

        gbc.gridy =1;
        centerPanel.add(buttonPanel,gbc);


        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.setOpaque(false);
        bottomPanel.add(backButton);

        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);


        add(mainPanel, BorderLayout.CENTER);

        // Add action listeners for the buttons
        addProductButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                openAddProductPanel();
            }
        });

        editProductButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                openEditProductPanel();
            }
        });

        deleteProductButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                openDeleteProductPanel();
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

    private void openAddProductPanel() {
        mainPanel.setVisible(false);

        if (addSupplierProduct == null) {
            addSupplierProduct = new AddSupplierProduct(this,supplier);
            addSupplierProduct.setPreferredSize(mainPanel.getSize());
            addSupplierProduct.setMaximumSize(mainPanel.getMaximumSize());
            addSupplierProduct.setMinimumSize(mainPanel.getMinimumSize());
            addSupplierProduct.setSize(mainPanel.getSize());
        }

        add(addSupplierProduct, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    private void openEditProductPanel() {
        mainPanel.setVisible(false);

        if (editSupplierProduct == null) {
            editSupplierProduct = new EditSupplierProduct(this, supplier);
            editSupplierProduct.setPreferredSize(mainPanel.getSize());
            editSupplierProduct.setMaximumSize(mainPanel.getMaximumSize());
            editSupplierProduct.setMinimumSize(mainPanel.getMinimumSize());
            editSupplierProduct.setSize(mainPanel.getSize());
        }

        add(editSupplierProduct, BorderLayout.CENTER);
        revalidate();
        repaint();
    }
    private void openDeleteProductPanel() {
        mainPanel.setVisible(false);

        if (deleteSupplieProduct == null) {
            deleteSupplieProduct = new DeleteSupplieProduct(this, supplier);
            deleteSupplieProduct.setPreferredSize(mainPanel.getSize());
            deleteSupplieProduct.setMaximumSize(mainPanel.getMaximumSize());
            deleteSupplieProduct.setMinimumSize(mainPanel.getMinimumSize());
            deleteSupplieProduct.setSize(mainPanel.getSize());
        }

        add(deleteSupplieProduct, BorderLayout.CENTER);
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
        if (addSupplierProduct != null && addSupplierProduct.isShowing()) {
            remove(addSupplierProduct);
            addSupplierProduct = null;
        } else if (editSupplierProduct != null && editSupplierProduct.isShowing()) {
            remove(editSupplierProduct);
            editSupplierProduct = null;
        }else if (deleteSupplieProduct != null && deleteSupplieProduct.isShowing()) {
            remove(deleteSupplieProduct);
            deleteSupplieProduct = null;
        }
    }

}
