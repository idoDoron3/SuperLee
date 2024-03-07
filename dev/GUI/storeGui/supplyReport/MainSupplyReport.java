package GUI.storeGui.supplyReport;

import GUI.storeGui.StoreManagerGUI;
import Supplier_Module.Business.Managers.Order_Manager;
import Supplier_Module.Business.Managers.SupplyManager;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.LinkedList;

public class MainSupplyReport extends JPanel {
    private StoreManagerGUI parent;
    private JPanel mainPanel;
    public MainSupplyReport(StoreManagerGUI storeManagerGUI){
        this.parent = storeManagerGUI;
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
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setOpaque(false);

        GridBagConstraints titleConstraints = new GridBagConstraints();
        titleConstraints.gridx = 0; // Column index
        titleConstraints.gridy = 0; // Row index
        titleConstraints.anchor = GridBagConstraints.NORTH; // Align in the center horizontally
        titleConstraints.insets = new Insets(10, 0, 10, 0);

        JLabel titleLabel = new JLabel("<html>Supplier Report <br></html>");
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 24));
        centerPanel.add(titleLabel, titleConstraints);


        // Create button panel
        JPanel watchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        JLabel enterNumberLabel = new JLabel("Please enter the Supplier id you want to watch:");
        JTextField numberField = new JTextField(10);
        JButton submitButton = new JButton("Submit");

        watchPanel.add(enterNumberLabel);
        watchPanel.add(numberField);
        watchPanel.add(submitButton);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weighty = 0.5; // Place components vertically in the middle
        gbc.anchor = GridBagConstraints.NORTH; // Center-align components
        centerPanel.add(watchPanel, gbc);

        submitButton.addActionListener(e -> {
            String supplierNumber = numberField.getText();
            if (isExistSupplier(supplierNumber)) {
                centerPanel.remove(watchPanel);
                int supID = Integer.parseInt(supplierNumber);
                // Get the supplier report
                //String[] lines = SupplyManager.getSupply_manager().getSupplier(supID).getSupplierReport();
                LinkedList<String> lines = SupplyManager.getSupply_manager().getSupplier(supID).getSupplierReport();
                ;
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
                textArea.setBorder(BorderFactory.createLineBorder(Color.BLACK)); // Set border line

                //reportPanel.add(scrollPane, BorderLayout.CENTER);
                gbc.gridy = 1;
                gbc.anchor = GridBagConstraints.CENTER;
                centerPanel.add(textArea, gbc);
                //reportPanel.setBackground(new Color(0, 0, 0, 0));

                // Remove the existing components and add the report panel
                //mainPanel.add(reportPanel, BorderLayout.CENTER);
                mainPanel.revalidate();
                mainPanel.repaint();
            } else {

                JOptionPane.showMessageDialog(null, "Invalid supplier ID. Please try again.");
            }
        });

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton backButton = new JButton("Back");
        bottomPanel.setOpaque(false);
        bottomPanel.add(backButton);

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

}
