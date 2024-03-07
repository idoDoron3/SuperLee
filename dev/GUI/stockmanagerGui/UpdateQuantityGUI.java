package GUI.stockmanagerGui;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class UpdateQuantityGUI extends JPanel {
    private JPanel mainPanel;
    private ProductMenuGui parent;
//    private SellPanel sellPanel;
//
//    private AddQuantity addQuantity;
    private JPanel currentPanel;


    public UpdateQuantityGUI(ProductMenuGui parent){


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
        mainPanel = new JPanel(){
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(finalBackground, 0, 0, getWidth(), getHeight(), this);
            }
        };
        mainPanel.setLayout(new BorderLayout());
        JLabel titleLabel = new JLabel("<html><br>Update Quantity:<br><br> </html>");
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 24));
        mainPanel.add(titleLabel, BorderLayout.NORTH);


        // Create button panel
        JButton backButton = new JButton("Back");
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.setOpaque(false);


        // Create buttons
        try {

//            JPanel texts = new JPanel();
//            texts.setLayout(new BoxLayout(texts, BoxLayout.Y_AXIS));

            JLabel catLabel = new JLabel("Category");
            JLabel subCatLabel = new JLabel("Sub Category");
            JLabel subSubCatLabel = new JLabel("Sub Sub Category");
            JLabel manuLabel = new JLabel("Manufacturer");
            JLabel quanLabel = new JLabel("Quantity");
            JLabel minQuanLabel = new JLabel("Minimum Quantity");
            JLabel weightLabel = new JLabel("Weight");
            JLabel dateLabel = new JLabel("Date  (DD/MM/YYYY)");

            JTextField category = new JTextField();
            category.setColumns(15);
            JTextField subCategory = new JTextField();
            subCategory.setColumns(15);
            JTextField subsubCategory = new JTextField();
            subsubCategory.setColumns(15);
            JTextField manufacturer = new JTextField();
            manufacturer.setColumns(15);
            JTextField quantity = new JTextField();
            quantity.setColumns(15);
            JTextField minimumQuantity = new JTextField();
            minimumQuantity.setColumns(15);
            JTextField weight = new JTextField();
            weight.setColumns(15);
            JTextField date = new JTextField();
            date.setColumns(15);
//            texts.add(createTextFieldPanel(catLabel, category));
//            texts.add(createTextFieldPanel(subCatLabel, subCategory));
//            texts.add(createTextFieldPanel(subSubCatLabel, subsubCategory));
//            texts.add(createTextFieldPanel(manuLabel, manufacturer));
//            texts.add(createTextFieldPanel(quanLabel, quantity));
//            texts.add(createTextFieldPanel(minQuanLabel, minimumQuantity));
//            texts.add(createTextFieldPanel(weightLabel, weight));
//            texts.add(createTextFieldPanel(dateLabel, date));
//            texts.setVisible(true);
//            mainPanel.add(texts);





            JButton addQuantity = createButton("Add Quantity", "/GUI/pictures/stock-manager.jpg");
            JButton sell = createButton("Sell", "/GUI/pictures/stock-manager.jpg");



            JPanel choose = new JPanel();

            choose.setOpaque(false);
            choose.add(addQuantity);
            buttonPanel.add(Box.createHorizontalGlue());
            choose.add(sell);



            buttonPanel.add(choose);
            buttonPanel.add(backButton,BorderLayout.SOUTH);

//            JPanel bottomPanel = new JPanel();
//            bottomPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

//            bottomPanel.add(backButton,BorderLayout.EAST);
//            bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));

            // Add button panel to the main panel

            mainPanel.add(Box.createVerticalStrut(120)); // Adjust the spacing as needed
//            mainPanel.add(buttonPanel,BorderLayout.EAST);
            buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0)); // 10 is the top padding
            mainPanel.add(buttonPanel, BorderLayout.SOUTH);
            buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));


            mainPanel.add(Box.createVerticalStrut(200));
//            mainPanel.add(bottomPanel, BorderLayout.EAST);


            add(mainPanel, BorderLayout.CENTER);
            mainPanel.setVisible(true);


            sell.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {

//                    if (sellPanel != null && sellPanel.isShowing()) {
//                        remove(sellPanel);
//                    }
//                    if (addQuantity != null && addQuantity.isShowing()) {
//                        remove(addQuantity);
//                    }
                    openSellPanel();
                }
            });

            addQuantity.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
//                parent.showDefaultPanelFromChild();
//                    if (sellPanel != null && sellPanel.isShowing()) {
//                        remove(sellPanel);
//                    }
//                    if (addQuantity != null && addQuantity.isShowing()) {
//                        remove(addQuantity);
//                    }
                    openAddQuantity();

                }
            });



            backButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    parent.showDefaultPanelFromChild();
                    if(currentPanel != null)
                        currentPanel.setVisible(false);
                }
            });
        }catch (IOException e) {
            System.out.println(e.getMessage());
        }



    }

    private JButton createButton(String text, String imagePath) throws IOException {
        // Create button panel
        int width = 100;
        int height = 100;
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
        button.setMargin(new Insets(0, 0, 0, 0)); // Set the margin to zer


        return button;

    }

//    private void openSellPanel(){
//        if (sellPanel == null) {
//            sellPanel = new SellPanel();
//            sellPanel.setPreferredSize(mainPanel.getSize());
//            sellPanel.setMaximumSize(mainPanel.getMaximumSize());
//            sellPanel.setMinimumSize(mainPanel.getMinimumSize());
//            sellPanel.setSize(mainPanel.getSize());
//        }
//        sellPanel.setVisible(true);
//        sellPanel.setOpaque(false);
//        mainPanel.add(sellPanel, BorderLayout.CENTER);
//
//        revalidate();
//        repaint();
//    }
    private void openSellPanel() {
        JPanel sellPanel = new SellPanel();
        sellPanel.setPreferredSize(mainPanel.getSize());
        sellPanel.setMaximumSize(mainPanel.getMaximumSize());
        sellPanel.setMinimumSize(mainPanel.getMinimumSize());
        sellPanel.setSize(mainPanel.getSize());
        sellPanel.setVisible(true);
        sellPanel.setOpaque(false);

        if (currentPanel != null) {
            mainPanel.remove(currentPanel);
        }

        mainPanel.add(sellPanel, BorderLayout.CENTER);
        currentPanel = sellPanel;

        revalidate();
        repaint();
    }



//    private void openAddQuantity(){
//        if (addQuantity == null) {
//            addQuantity = new AddQuantity();
//            addQuantity.setPreferredSize(addQuantity.getSize());
//            addQuantity.setMaximumSize(addQuantity.getMaximumSize());
//            addQuantity.setMinimumSize(addQuantity.getMinimumSize());
//            addQuantity.setSize(addQuantity.getSize());
//        }
//        addQuantity.setVisible(true);
//        addQuantity.setOpaque(false);
//        mainPanel.add(addQuantity, BorderLayout.CENTER);
//
//        revalidate();
//        repaint();
//    }

    private void openAddQuantity() {
        JPanel addQuantity = new AddQuantity();
        addQuantity.setPreferredSize(mainPanel.getSize());
        addQuantity.setMaximumSize(mainPanel.getMaximumSize());
        addQuantity.setMinimumSize(mainPanel.getMinimumSize());
        addQuantity.setSize(mainPanel.getSize());
        addQuantity.setVisible(true);
        addQuantity.setOpaque(false);

        if (currentPanel != null) {
            mainPanel.remove(currentPanel);
        }

        mainPanel.add(addQuantity, BorderLayout.CENTER);
        currentPanel = addQuantity;

        revalidate();
        repaint();
    }

}
