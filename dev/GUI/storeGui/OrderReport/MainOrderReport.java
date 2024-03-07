package GUI.storeGui.OrderReport;

import GUI.storeGui.StoreManagerGUI;
import Supplier_Module.Business.Order;
import Supplier_Module.DAO.OrderDAO;


import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainOrderReport extends JPanel {
    private StoreManagerGUI mainGUI;
    private JPanel mainPanel;
    private SpecificOrder specificOrder;

    public MainOrderReport(StoreManagerGUI storeManagerGUI) throws IOException {
        this.mainGUI = storeManagerGUI;
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

        JLabel titleLabel = new JLabel("<html>Welcome To Order Report <br> Please select required report  :</html>");
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 24));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 0, 10, 0); // Adjust spacing as needed
        centerPanel.add(titleLabel, gbc);

        JButton backButton = new JButton("Back");
        JPanel buttonPanel = new JPanel(new GridLayout(2,2,25,25));
        buttonPanel.setOpaque(false);

        // Create buttons
        JButton periodic_order = createButton(" Period Orders", "/GUI/pictures/period-report.jpg");
        JButton on_the_way_orders = createButton("On The Way Orders", "/GUI/pictures/on-the-way-report.jpg");
        JButton historic_orders = createButton("Historic Orders", "/GUI/pictures/historic-orders.jpg");
        JButton specific_order = createButton("Specific Order", "/GUI/pictures/specific-order.jpg");

        // Add buttons to the button panel
        buttonPanel.add(periodic_order);
        buttonPanel.add(on_the_way_orders);
        buttonPanel.add(historic_orders);
        buttonPanel.add(specific_order);

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

        // Add action listeners
        periodic_order.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                List<Integer> period = new ArrayList<>(OrderDAO.getInstance().getAllOrdersByKind(2).keySet());
                if (period.size() == 0) {
                    JOptionPane.showMessageDialog(null, "There isn't Period Orders");
                } else {
                    String ans = "";
                    for (int i = 0; i < period.size() - 1; i++) {
                        ans += period.get(i) + ",";
                    }
                    ans += period.get(period.size() - 1);
                    JOptionPane.showMessageDialog(null, "Periodic Orders ID: " + ans);
                }
            }
        });
        on_the_way_orders.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                List<Integer> lack = new ArrayList<>(OrderDAO.getInstance().getAllOrdersByKind(1).keySet());
                if(lack.size() ==0){
                    JOptionPane.showMessageDialog(null, "There isn't On The Way Orders");
                }
                else {
                    String ans = "";
                    for (int i = 0; i < lack.size() - 1; i++) {
                        ans += lack.get(i) + ",";
                    }
                    ans += lack.get(lack.size() - 1);
                    JOptionPane.showMessageDialog(null, "On The Way Orders ID: " + ans);
                }
            }
        });

        historic_orders.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                List<Integer> historic = new ArrayList<>(OrderDAO.getInstance().getAllOrdersByKind(0).keySet());
                if(historic.size() ==0){
                    JOptionPane.showMessageDialog(null, "There isn't History Orders");
                }
                else {
                    String ans = "";
                    for (int i = 0; i < historic.size() - 1; i++) {
                        ans += historic.get(i) + ",";
                    }
                    ans += historic.get(historic.size() - 1);
                    JOptionPane.showMessageDialog(null, "Historic Orders ID: " + ans);
                }
            }
        });
        specific_order.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                openSpecific_order();
            }
        });
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                mainGUI.showDefaultPanelFromChild();
            }
        });

    }
    private JButton createButton(String text, String imagePath) throws IOException {
        // Create button panel
        int width = 120;
        int height = 120;
        JPanel buttonPanel = new JPanel(null);
        buttonPanel.setLayout(new BorderLayout());

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

    private void openSpecific_order()  {
        //new frame of categry name
        mainPanel.setVisible(false);

        if (specificOrder == null) {
            specificOrder = new SpecificOrder(this);
            specificOrder.setPreferredSize(mainPanel.getSize());
            specificOrder.setMaximumSize(mainPanel.getMaximumSize());
            specificOrder.setMinimumSize(mainPanel.getMinimumSize());
            specificOrder.setSize(mainPanel.getSize());
        }

        add(specificOrder, BorderLayout.CENTER);
        revalidate();
        repaint();
    }
    // Add methods to open other screens and handle back button
    public void showDefaultPanelFromChild() {
        mainPanel.setVisible(true);
        removeCurrentChildPanel();
        revalidate();
        repaint();
    }

    private void removeCurrentChildPanel() {
        if (specificOrder != null && specificOrder.isShowing()) {
            remove(specificOrder);
            specificOrder=null;
        }
    }

}

