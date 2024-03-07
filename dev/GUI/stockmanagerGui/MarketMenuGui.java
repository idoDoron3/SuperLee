package GUI.stockmanagerGui;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class MarketMenuGui extends JPanel {
    private StockManagement parent;

    private DiscountByCategoryGUI discountByCategoryGUI;
    private DiscountByCatalogNumberGUI discountByCatalogNumberGUI;
    private DiscountForCategoryGUI discountForCategoryGUI;
    private AddShelvesToMarketGUI addShelvesToMarketGUI;

    private JPanel mainPanel;

    public MarketMenuGui(StockManagement parent) throws IOException {
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
        JLabel titleLabel = new JLabel("<html><br> Welcome to Market Menu <br> Please select option :</html>");
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 24));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        JButton discountByCategory = createButton("Discount By Category", "/GUI/pictures/specific-order.jpg");
        JButton discountByCatalogNumber = createButton("Discount By Catalog Number", "/GUI/pictures/product-report.jpg");
        JButton discountForCategory = createButton("Discount For Category", "/GUI/pictures/category-report.jpg");
        JButton addShelves = createButton("Add Shelves", "/GUI/pictures/supply.JPG");

        JPanel menuPanel = new JPanel();
        int verticalGap = 35; // Set the desired vertical gap between rows
        int horizontalGap = 15;
        menuPanel.setLayout(new GridLayout(1, 4, horizontalGap, verticalGap));
        menuPanel.add(discountByCategory);
        menuPanel.add(discountByCatalogNumber);
        menuPanel.add(discountForCategory);
        menuPanel.add(addShelves);

        menuPanel.setOpaque(false);

        JPanel mainWrapperPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        mainWrapperPanel.add(menuPanel);
        mainWrapperPanel.setBorder(BorderFactory.createEmptyBorder(120, 0, 0, 0)); // 10 is the top padding
        mainPanel.add(mainWrapperPanel, BorderLayout.CENTER);

        mainWrapperPanel.setOpaque(false);

        JButton backButton = new JButton("Back");
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(backButton);

        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0)); // 10 is the top padding

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel, BorderLayout.CENTER);

        discountByCategory.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
//                parent.showDefaultPanelFromChild();
                openDiscountByCategory();
            }
        });

        discountByCatalogNumber.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
//                parent.showDefaultPanelFromChild();
                openDiscountByCatalogNumber();
            }
        });

        discountForCategory.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
//                parent.showDefaultPanelFromChild();
                openDiscountForCategory();
            }
        });

        addShelves.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
//                parent.showDefaultPanelFromChild();
                openAddShelvesToMarket();

            }
        });

        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                parent.showMainPanel();
            }
        });

    }

    private JButton createButton(String text, String imagePath) throws IOException {
        // Create button panel
        int width = 172;
        int height = 150;
        JPanel buttonPanel = new JPanel(null);
        buttonPanel.setLayout(new BorderLayout());

        // Create image label
        JLabel imageLabel = new JLabel();
        Image image = ImageIO.read(getClass().getResource(imagePath));
        Image small_image = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        ImageIcon imageIcon = new ImageIcon(small_image);
        imageLabel.setIcon(imageIcon);
        imageLabel.setBounds(0, 0, width, height);
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
        button.setMargin(new Insets(0, 0, 0, 0)); // Set the margin to zero

        return button;
    }

    public void showDefaultPanelFromChild() {
        mainPanel.setVisible(true);
        removeCurrentChildPanel();
        revalidate();
        repaint();
    }

    private void removeCurrentChildPanel() {
        if (discountByCategoryGUI != null && discountByCategoryGUI.isShowing()) {
            remove(discountByCategoryGUI);
        } else if (discountByCatalogNumberGUI != null && discountByCatalogNumberGUI.isShowing()) {
            remove(discountByCatalogNumberGUI);
        } else if (discountForCategoryGUI != null && discountForCategoryGUI.isShowing()) {
            remove(discountForCategoryGUI);
        } else if (addShelvesToMarketGUI != null && addShelvesToMarketGUI.isShowing()) {
            remove(addShelvesToMarketGUI);
        }
    }

    public void openDiscountByCategory() {
        mainPanel.setVisible(false);

        if (discountByCategoryGUI == null) {
            discountByCategoryGUI = new DiscountByCategoryGUI(this);
            discountByCategoryGUI.setPreferredSize(mainPanel.getSize());
            discountByCategoryGUI.setMaximumSize(mainPanel.getMaximumSize());
            discountByCategoryGUI.setMinimumSize(mainPanel.getMinimumSize());
            discountByCategoryGUI.setSize(mainPanel.getSize());
        }
        discountByCategoryGUI.setVisible(true);
        add(discountByCategoryGUI, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    public void openDiscountByCatalogNumber() {
        mainPanel.setVisible(false);

        if (discountByCatalogNumberGUI == null) {
            discountByCatalogNumberGUI = new DiscountByCatalogNumberGUI(this);
            discountByCatalogNumberGUI.setPreferredSize(mainPanel.getSize());
            discountByCatalogNumberGUI.setMaximumSize(mainPanel.getMaximumSize());
            discountByCatalogNumberGUI.setMinimumSize(mainPanel.getMinimumSize());
            discountByCatalogNumberGUI.setSize(mainPanel.getSize());
        }
        discountByCatalogNumberGUI.setVisible(true);
        add(discountByCatalogNumberGUI, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    public void openDiscountForCategory() {
        mainPanel.setVisible(false);

        if (discountForCategoryGUI == null) {
            discountForCategoryGUI = new DiscountForCategoryGUI(this);
            discountForCategoryGUI.setPreferredSize(mainPanel.getSize());
            discountForCategoryGUI.setMaximumSize(mainPanel.getMaximumSize());
            discountForCategoryGUI.setMinimumSize(mainPanel.getMinimumSize());
            discountForCategoryGUI.setSize(mainPanel.getSize());
        }
        discountForCategoryGUI.setVisible(true);
        add(discountForCategoryGUI, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    public void openAddShelvesToMarket() {
        mainPanel.setVisible(false);

        if (addShelvesToMarketGUI == null) {
            addShelvesToMarketGUI = new AddShelvesToMarketGUI(this);
            addShelvesToMarketGUI.setPreferredSize(mainPanel.getSize());
            addShelvesToMarketGUI.setMaximumSize(mainPanel.getMaximumSize());
            addShelvesToMarketGUI.setMinimumSize(mainPanel.getMinimumSize());
            addShelvesToMarketGUI.setSize(mainPanel.getSize());
        }
        addShelvesToMarketGUI.setVisible(true);
        add(addShelvesToMarketGUI, BorderLayout.CENTER);
        revalidate();
        repaint();
    }
}



