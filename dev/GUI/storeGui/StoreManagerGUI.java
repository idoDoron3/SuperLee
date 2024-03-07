package GUI.storeGui;

import GUI.MainGUI;
import GUI.storeGui.OrderReport.MainOrderReport;
import GUI.storeGui.stockReport.MainStockReport;
import GUI.storeGui.supplyReport.MainSupplyReport;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class StoreManagerGUI extends JPanel {
    private MainGUI mainGUI;
    private JPanel mainPanel;
    private MainOrderReport mainOrderReport;
    private MainStockReport mainStockReport;
    private MainSupplyReport mainSupplyReport;

    public StoreManagerGUI(MainGUI mainGUI) throws IOException {
        this.mainGUI = mainGUI;
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
        JLabel titleLabel = new JLabel("<html><br> Welcome Store Manager <br> Please select report type :</html>");
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 24));
        mainPanel.add(titleLabel, BorderLayout.NORTH);


        // Create buttons
        JButton supplierReportButton = createButton("supply Reports", "/GUI/pictures/supply.JPG");
        JButton OrderReportButton = createButton("Order Reports", "/GUI/pictures/stock-manager.jpg");
        JButton stockReportButton = createButton("Stock Reports", "/GUI/pictures/stock-report.jpg");

        JPanel menuPanel = new JPanel();
        int verticalGap = 35; // Set the desired vertical gap between rows
        int horizontalGap = 15;
        menuPanel.setLayout(new GridLayout(1, 3, horizontalGap, verticalGap));
        menuPanel.add(supplierReportButton);
        menuPanel.add(OrderReportButton);
        menuPanel.add(stockReportButton);

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

        // Add action listeners
        supplierReportButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                openSupplyReports();
            }
        });
        stockReportButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    openstockReports();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        OrderReportButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    openOrderReports();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                mainGUI.showMainPanel();
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
        if (mainSupplyReport != null && mainSupplyReport.isShowing()) {
            remove(mainSupplyReport);
        } else if (mainOrderReport != null && mainOrderReport.isShowing()) {
            remove(mainOrderReport);
        } else if (mainStockReport != null && mainStockReport.isShowing()) {
            remove(mainStockReport);
        }
    }

    private void openSupplyReports() {
        mainPanel.setVisible(false);
        if (mainSupplyReport == null) {
            mainSupplyReport = new MainSupplyReport(this);
            mainSupplyReport.setPreferredSize(mainPanel.getSize());
            mainSupplyReport.setMaximumSize(mainPanel.getMaximumSize());
            mainSupplyReport.setMinimumSize(mainPanel.getMinimumSize());
            mainSupplyReport.setSize(mainPanel.getSize());
        }

        add(mainSupplyReport, BorderLayout.CENTER);
        revalidate();
        repaint();

    }

    private void openstockReports() throws IOException {
        mainPanel.setVisible(false);
        if (mainStockReport == null) {
            mainStockReport = new MainStockReport(this);
            mainStockReport.setPreferredSize(mainPanel.getSize());
            mainStockReport.setMaximumSize(mainPanel.getMaximumSize());
            mainStockReport.setMinimumSize(mainPanel.getMinimumSize());
            mainStockReport.setSize(mainPanel.getSize());
        }

        add(mainStockReport, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    private void openOrderReports() throws IOException {
        mainPanel.setVisible(false);
        if (mainOrderReport == null) {
            mainOrderReport = new MainOrderReport(this);
            mainOrderReport.setPreferredSize(mainPanel.getSize());
            mainOrderReport.setMaximumSize(mainPanel.getMaximumSize());
            mainOrderReport.setMinimumSize(mainPanel.getMinimumSize());
            mainOrderReport.setSize(mainPanel.getSize());
        }

        add(mainOrderReport, BorderLayout.CENTER);
        revalidate();
        repaint();
    }
}
