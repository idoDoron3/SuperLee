package GUI.storeGui.stockReport;
import GUI.storeGui.StoreManagerGUI;
import Stock.Business.StockReport;
import Stock.DataAccess.CategoryDAO;
import Stock.DataAccess.ProductDAO;
import Stock.Service.ReportsService;


import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class MainStockReport extends JPanel {
    private StoreManagerGUI mainGUI;
    private JPanel mainPanel;
    private CategoryReport categoryReport;
    private SpecificProductReport specificProductReport;

    public MainStockReport(StoreManagerGUI storeManagerGUI) throws IOException {
        this.mainGUI = storeManagerGUI;
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
        JLabel titleLabel = new JLabel("<html><br>Welcome To Stock Report <br> Please select required report  :</html>");
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 24));
        mainPanel.add(titleLabel, BorderLayout.NORTH);


        // Create buttons
        JButton DamageReport = createButton("Damage Report", "/GUI/pictures/damage-report.jpg");
        JButton ShortagesReport = createButton("Shortages Report", "/GUI/pictures/missing-report.jpg");
        JButton OrderReport = createButton("Order Report", "/GUI/pictures/order-report.jpg");
        JButton StockReport = createButton("Stock Report", "/GUI/pictures/stock-report.jpg");
        JButton CategoryReport = createButton("Category Report", "/GUI/pictures/category-report.jpg");
        JButton SpecificProduct = createButton("Product Report", "/GUI/pictures/product-report.jpg");

        JPanel menuPanel = new JPanel();
        int verticalGap = 35; // Set the desired vertical gap between rows
        int horizontalGap = 45;
        menuPanel.setLayout(new GridLayout(2, 3, horizontalGap, verticalGap));
        menuPanel.add(DamageReport);
        menuPanel.add(ShortagesReport);
        menuPanel.add(OrderReport);
        menuPanel.add(StockReport);
        menuPanel.add(CategoryReport);
        menuPanel.add(SpecificProduct);

        menuPanel.setOpaque(false);

        JPanel mainWrapperPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        mainWrapperPanel.add(menuPanel);
        mainWrapperPanel.setBorder(BorderFactory.createEmptyBorder(40, 0, 0, 0)); // 10 is the top padding
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
        DamageReport.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                openDamageReport();
            }
        });
        ShortagesReport.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    openShortagesReports();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        OrderReport.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                openOrderReport();
            }
        });
        StockReport.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                openStockReport();
            }
        });
        CategoryReport.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                openCategoryReport();
            }
        });
        SpecificProduct.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                openSpecificProductReport();
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
        int width = 150;
        int height = 150;
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
        if (categoryReport != null && categoryReport.isShowing()) {
            remove(categoryReport);
        }
        else if (specificProductReport != null && specificProductReport.isShowing()) {
            remove(specificProductReport);
        }
    }

    private void openDamageReport() {
        //open new frame of this report
        String damaged = ReportsService.getInstance().getDamagedReport();
        stockReportsPanel stockPanel = new stockReportsPanel(this,damaged);

        mainPanel.setVisible(false);
        stockPanel.setVisible(true);
        revalidate();
        repaint();

    }

    private void openShortagesReports() throws IOException {
        //open new frame if this report
        String shortages = ReportsService.getInstance().getShortagesReport();
        stockReportsPanel stockPanel = new stockReportsPanel(this,shortages);

        mainPanel.setVisible(false);
        stockPanel.setVisible(true);
        revalidate();
        repaint();
    }

    private void openOrderReport()  {
        //open new frame if this report
        String orderReport = ReportsService.getInstance().getOrderReport();
        stockReportsPanel stockPanel = new stockReportsPanel(this,orderReport);

        mainPanel.setVisible(false);
        stockPanel.setVisible(true);
        revalidate();
        repaint();
    }
    private void openStockReport()  {
        //open new frame if this report
        String stock = ReportsService.getInstance().getStockReport();
        stockReportsPanel stockPanel = new stockReportsPanel(this,stock);

        mainPanel.setVisible(false);
        stockPanel.setVisible(true);
        revalidate();
        repaint();
    }

    private void openCategoryReport()  {
        //new frame of category name
        mainPanel.setVisible(false);

        if (categoryReport == null) {
            categoryReport = new CategoryReport(this);
            categoryReport.setPreferredSize(mainPanel.getSize());
            categoryReport.setMaximumSize(mainPanel.getMaximumSize());
            categoryReport.setMinimumSize(mainPanel.getMinimumSize());
            categoryReport.setSize(mainPanel.getSize());
        }

        add(categoryReport, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    //open new frame if this report
    private void openSpecificProductReport()  {
        //new frame of product name
        //open new frame if this report
        mainPanel.setVisible(false);

        if (specificProductReport == null) {
            specificProductReport = new SpecificProductReport(this);
            specificProductReport.setPreferredSize(mainPanel.getSize());
            specificProductReport.setMaximumSize(mainPanel.getMaximumSize());
            specificProductReport.setMinimumSize(mainPanel.getMinimumSize());
            specificProductReport.setSize(mainPanel.getSize());
        }

        add(specificProductReport, BorderLayout.CENTER);
        revalidate();
        repaint();
    }
}


