package GUI.storeGui.stockReport;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class stockReportsPanel extends JPanel {

    private MainStockReport parent;
    private String report;

    public stockReportsPanel(MainStockReport parent, String report){
        this.parent = parent;
        this.report = report;



        JPanel stockRep = new JPanel();
        JTextArea textArea = new JTextArea(report);
        textArea.setFont(new Font("Comic Sans MS", Font.BOLD, 18));
        int longestLineWidth = 0;
        String[] lines = report.split("\n");
        FontMetrics fontMetrics = textArea.getFontMetrics(textArea.getFont());
        for (String line : lines) {
            int lineWidth = fontMetrics.stringWidth(line);
            if (lineWidth > longestLineWidth) {
                longestLineWidth = lineWidth;
            }
        }
        textArea.setPreferredSize(new Dimension(longestLineWidth, textArea.getPreferredSize().height));
        textArea.setEditable(false);

//        textArea.setLineWrap(true);
//        textArea.setWrapStyleWord(true);

// Set vertical alignment to center
//        textArea.setAlignmentY(JTextArea.CENTER_ALIGNMENT);



        stockRep.add(textArea);
        JButton back = new JButton("Back");

        stockRep.add(back);

        parent.add(stockRep);
//        mainPanel.setVisible(false);
        stockRep.setVisible(true);
        back.setVisible(true);
        back.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                back.setVisible(false);
                stockRep.setVisible(false);
                parent.showDefaultPanelFromChild();
            }
        });
    }




}