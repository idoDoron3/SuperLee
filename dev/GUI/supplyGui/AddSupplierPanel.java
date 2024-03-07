package GUI.supplyGui;

import Supplier_Module.Business.Agreement.Agreement;
import Supplier_Module.Business.Agreement.MethodSupply.ByFixedDays;
import Supplier_Module.Business.Agreement.MethodSupply.BySuperLee;
import Supplier_Module.Business.Agreement.MethodSupply.BySupplyDays;
import Supplier_Module.Business.Agreement.MethodSupply.MethodSupply;
import Supplier_Module.Business.Card.ContactMember;
import Supplier_Module.Business.Card.SupplierCard;
import Supplier_Module.Business.Defs.Payment_method;
import Supplier_Module.Business.Discount.PrecentageDiscount;
import Supplier_Module.Business.Discount.Range;
import Supplier_Module.Business.Managers.SupplyManager;
import Supplier_Module.Business.Supplier;
import Supplier_Module.DAO.SupplierDAO;

import javax.imageio.ImageIO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

public class AddSupplierPanel extends JPanel {
    private SupplierGUI supplierGUI;
    private JPanel mainPanel;
    private int[] daysWeek  = {0,0,0,0,0,0,0};
    private int supplyDays =0;
    private int mistake_counter =0;
    private MethodSupply methodSupply;
    private LinkedList<PrecentageDiscount> discounts;
    public AddSupplierPanel(SupplierGUI supplier) {
        this.supplierGUI = supplier;
        discounts = new LinkedList<>();

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
        JLabel titleLabel = new JLabel("Add Supplier");
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 20));
        mainPanel.add(titleLabel, BorderLayout.NORTH);


        // Create text fields
        //Supplier Name
        JLabel categoryLabel = new JLabel("Name:");
        Font categoryLabelFont = categoryLabel.getFont();
        Font categoryLabelNewFont = categoryLabelFont.deriveFont(Font.BOLD, 12);
        categoryLabel.setFont(categoryLabelNewFont);

        JTextField categoryTextField = new JTextField();
        Font categoryTextFieldFont = categoryTextField.getFont();
        Font categoryTextFieldNewFont = categoryTextFieldFont.deriveFont(Font.BOLD, 12);
        categoryTextField.setFont(categoryTextFieldNewFont);

        JLabel invalidCategoryLabel = new JLabel("Invalid Name");
        Font invalidCategoryLabelFont = invalidCategoryLabel.getFont();
        Font invalidCategoryLabelNewFont = invalidCategoryLabelFont.deriveFont(Font.PLAIN, 12);
        invalidCategoryLabel.setFont(invalidCategoryLabelNewFont);
        invalidCategoryLabel.setForeground(Color.RED);


        //Supplier Address
        JLabel subCategoryLabel = new JLabel("Address");
        Font subCategoryLabelFont = subCategoryLabel.getFont();
        Font subCategoryLabelNewFont = subCategoryLabelFont.deriveFont(Font.BOLD, 12);
        subCategoryLabel.setFont(subCategoryLabelNewFont);

        JTextField subCategoryTextField = new JTextField();
        Font subCategoryTextFieldFont = subCategoryTextField.getFont();
        Font subCategoryTextFieldNewFont = subCategoryTextFieldFont.deriveFont(Font.BOLD, 12);
        subCategoryTextField.setFont(subCategoryTextFieldNewFont);

        JLabel invalidSubCategoryLabel = new JLabel("Invalid Address");
        Font invalidSubCategoryLabelFont =  invalidSubCategoryLabel.getFont();
        Font invalidSubCategoryLabelNewFont = invalidSubCategoryLabelFont.deriveFont(Font.BOLD, 12);
        invalidSubCategoryLabel.setFont(invalidSubCategoryLabelNewFont);
        invalidSubCategoryLabel.setForeground(Color.RED);

        //Supplier ID
        JLabel subSubCategoryLabel = new JLabel("ID");
        Font subSubCategoryLabelFont = subSubCategoryLabel.getFont();
        Font subSubCategoryLabelNewFont = subSubCategoryLabelFont.deriveFont(Font.BOLD, 12);
        subSubCategoryLabel.setFont(subSubCategoryLabelNewFont);

        JTextField subSubCategoryTextField = new JTextField();
        Font subSubCategoryTextFieldFont = subSubCategoryTextField.getFont();
        Font subSubCategoryTextFieldNewFont = subSubCategoryTextFieldFont.deriveFont(Font.BOLD, 12);
        subSubCategoryTextField.setFont(subSubCategoryTextFieldNewFont);

        JLabel invalidSubSubCategoryLabel = new JLabel("Invalid ID");
        Font invalidSubSubCategoryLabelFont = invalidSubSubCategoryLabel.getFont();
        Font invalidSubSubCategoryLabelNewFont = invalidSubSubCategoryLabelFont.deriveFont(Font.BOLD, 12);
        invalidSubSubCategoryLabel.setFont(invalidSubSubCategoryLabelNewFont);
        invalidSubSubCategoryLabel.setForeground(Color.RED);

        //Supplier Bank
        JLabel bankLabel = new JLabel("Bank Account");
        Font bankLabelFont = bankLabel.getFont();
        Font bankLabelNewFont = bankLabelFont.deriveFont(Font.BOLD, 12);
        bankLabel.setFont(bankLabelNewFont);

        JTextField bankTextField = new JTextField();
        Font bankTextFieldFont = bankTextField.getFont();
        Font bankTextFieldNewFont = bankTextFieldFont.deriveFont(Font.BOLD, 12);
        bankTextField.setFont(bankTextFieldNewFont);

        JLabel invalidBankLabel = new JLabel("Invalid Bank Account");
        Font invalidBankLabelFont = invalidBankLabel.getFont();
        Font invalidBankLabelNewFont = invalidBankLabelFont.deriveFont(Font.BOLD, 12);
        invalidBankLabel.setFont(invalidBankLabelNewFont);
        invalidBankLabel.setForeground(Color.RED);

        //Supplier Payment
        JLabel paymentLabel = new JLabel("Payment Method");
        Font paymentLabelFont = paymentLabel.getFont();
        Font paymentLabelNewFont = paymentLabelFont.deriveFont(Font.BOLD, 12);
        paymentLabel.setFont(paymentLabelNewFont);

        String[] items = {"bit", "cash", "credit card"};
        JComboBox<String> PaymentComboBox = new JComboBox<>(items);


        JLabel invalidPaymentLabel = new JLabel("Invalid Payment Method");


        //Supplier Contact Name
        JLabel ContactName = new JLabel("Contact Member name");
        Font contactNametLabelFont = ContactName.getFont();
        Font ContactNameLabelNewFont = contactNametLabelFont.deriveFont(Font.BOLD, 12);
        ContactName.setFont(ContactNameLabelNewFont);

        JTextField memberNameTextField = new JTextField();
        Font memberNameTextFieldFont = memberNameTextField.getFont();
        Font memberNameTextFieldNewFont = memberNameTextFieldFont.deriveFont(Font.BOLD, 12);
        memberNameTextField.setFont(memberNameTextFieldNewFont);

        JLabel invalidMemberNameLabel = new JLabel("Invalid Member Name");
        Font invalidmemberNameLabelFont = invalidMemberNameLabel.getFont();
        Font invalidmemberNameLabelNewFont = invalidmemberNameLabelFont.deriveFont(Font.BOLD, 12);
        invalidMemberNameLabel.setFont(invalidmemberNameLabelNewFont);
        invalidMemberNameLabel.setForeground(Color.RED);

        //Supplier Contact mail
        JLabel ContactMail = new JLabel("Contact Member Mail");
        Font contactMailtLabelFont = ContactMail.getFont();
        Font ContactMailLabelNewFont = contactMailtLabelFont.deriveFont(Font.BOLD, 12);
        ContactMail.setFont(ContactMailLabelNewFont);

        JTextField memberMailTextField = new JTextField();
        Font memberMailTextFieldFont = memberMailTextField.getFont();
        Font memberMailTextFieldNewFont = memberMailTextFieldFont.deriveFont(Font.BOLD, 12);
        memberMailTextField.setFont(memberMailTextFieldNewFont);

        JLabel invalidMemberMailLabel = new JLabel("Invalid Member Mail");
        Font invalidmemberMailLabelFont = invalidMemberMailLabel.getFont();
        Font invalidmemberMailLabelNewFont = invalidmemberMailLabelFont.deriveFont(Font.BOLD, 12);
        invalidMemberMailLabel.setFont(invalidmemberMailLabelNewFont);
        invalidMemberMailLabel.setForeground(Color.RED);

        //Supplier Contact Phone
        JLabel ContactPhone = new JLabel("Contact Member Phone");
        Font contactPhonetLabelFont = ContactPhone.getFont();
        Font ContactPhoneLabelNewFont = contactPhonetLabelFont.deriveFont(Font.BOLD, 12);
        ContactPhone.setFont(ContactPhoneLabelNewFont);

        JTextField memberPhoneTextField = new JTextField();
        Font memberPhoneTextFieldFont = memberPhoneTextField.getFont();
        Font memberPhoneTextFieldNewFont = memberPhoneTextFieldFont.deriveFont(Font.BOLD, 12);
        memberPhoneTextField.setFont(memberPhoneTextFieldNewFont);

        JLabel invalidMemberPhoneLabel = new JLabel("Invalid Member Phone");
        Font invalidmemberPhoneLabelFont = invalidMemberPhoneLabel.getFont();
        Font invalidmemberPhoneLabelNewFont = invalidmemberPhoneLabelFont.deriveFont(Font.BOLD, 12);
        invalidMemberPhoneLabel.setFont(invalidmemberPhoneLabelNewFont);
        invalidMemberPhoneLabel.setForeground(Color.RED);
        //min range of the discount
        JLabel minRangeLabel = new JLabel("Min Range of discount");
        Font minRangeLabelFont = minRangeLabel.getFont();
        Font minRangeLabelNewFont = minRangeLabelFont.deriveFont(Font.BOLD, 12);
        minRangeLabel.setFont(minRangeLabelNewFont);

        JTextField minRangeLabelTextField = new JTextField("0");
        minRangeLabelTextField.setEditable(false);
        Font minRangeLabelTextFieldFont = minRangeLabelTextField.getFont();
        Font minRangeTextFieldNewFont = minRangeLabelTextFieldFont.deriveFont(Font.BOLD, 12);
        minRangeLabelTextField.setFont(minRangeTextFieldNewFont);

        JButton submitDiscount = new JButton("Add Discount");

//top range of the discount
        JLabel maxRangeLabel = new JLabel("Max Range of discount");
        Font maxRangeLabelFont = maxRangeLabel.getFont();
        Font maxRangeLabelNewFont = maxRangeLabelFont.deriveFont(Font.BOLD, 12);
        maxRangeLabel.setFont(maxRangeLabelNewFont);

        JTextField maxRangeLabelTextField = new JTextField();
        Font maxRangeLabelTextFieldFont = maxRangeLabelTextField.getFont();
        Font maxRangeTextFieldNewFont = maxRangeLabelTextFieldFont.deriveFont(Font.BOLD, 12);
        maxRangeLabelTextField.setFont(maxRangeTextFieldNewFont);

        JLabel invalidmaxRangeLabel = new JLabel("Invalid Max amount");
        Font invalidmaxRangeLabelFont = invalidmaxRangeLabel.getFont();
        Font invalidmaxRangeLabelNewFont = invalidmaxRangeLabelFont.deriveFont(Font.PLAIN, 12);
        invalidmaxRangeLabel.setFont(invalidmaxRangeLabelNewFont);
        invalidmaxRangeLabel.setForeground(Color.RED);
        //discount
        JLabel discountLabel = new JLabel("Discount");
        Font dixcountLabelFont = discountLabel.getFont();
        Font discountLabelNewFont = dixcountLabelFont.deriveFont(Font.BOLD, 12);
        discountLabel.setFont(discountLabelNewFont);

        JTextField discountLabelTextField = new JTextField();
        Font discountLabelTextFieldFont = discountLabelTextField.getFont();
        Font discountTextFieldNewFont = discountLabelTextFieldFont.deriveFont(Font.BOLD, 12);
        discountLabelTextField.setFont(discountTextFieldNewFont);

        JLabel invalidDiscountLabel = new JLabel("Invalid Discount");
        Font invalidDiscountLabelFont = invalidDiscountLabel.getFont();
        Font invalidDiscountLabelNewFont = invalidDiscountLabelFont.deriveFont(Font.PLAIN, 12);
        invalidDiscountLabel.setFont(invalidDiscountLabelNewFont);
        invalidDiscountLabel.setForeground(Color.RED);

        //Supplier Method
        JLabel supplyMethod = new JLabel("Supply Method");
        Font SupplyMethodLabelFont = supplyMethod.getFont();
        Font SupplyMethodLabelNewFont = SupplyMethodLabelFont.deriveFont(Font.BOLD, 12);
        supplyMethod.setFont(SupplyMethodLabelNewFont);

        String[] supplyItems = {"Fixed Days", "Super Lee Transport", "Supply Days"};
        JComboBox<String> SupplyComboBox = new JComboBox<>(supplyItems);
        JButton supplyButton = new JButton("submit supply method");


        //if FixDays
        JLabel FixDays = new JLabel("Select Supply Days");
        Font FixDaysLabelFont = FixDays.getFont();
        Font FixDaysLabelNewFont = FixDaysLabelFont.deriveFont(Font.BOLD, 12);
        FixDays.setFont(FixDaysLabelNewFont);

        String[] daysOfTheWeek = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
        JComboBox<String> daysComboBox = new JComboBox<>(daysOfTheWeek);
        JButton daysButton = new JButton("Add Day");

        //if Supply days
        JLabel SupplyDays = new JLabel("Number of Supply days");
        Font SupplyDaysLabelFont = SupplyDays.getFont();
        Font SupplyDaysLabelNewFont = SupplyDaysLabelFont.deriveFont(Font.BOLD, 12);
        SupplyDays.setFont(SupplyDaysLabelNewFont);

        JTextField SupplyDaysTextField = new JTextField();
        Font SupplyDaysTextFieldFont = SupplyDaysTextField.getFont();
        Font SupplyDaysTextFieldNewFont = SupplyDaysTextFieldFont.deriveFont(Font.BOLD, 12);
        SupplyDaysTextField.setFont(SupplyDaysTextFieldNewFont);

        JLabel invalidSupplyDaysLabel = new JLabel("Invalid number of days");
        Font invalidSupplyDaysLabelFont = invalidSupplyDaysLabel.getFont();
        Font invalidSupplyDaysLabelNewFont = invalidSupplyDaysLabelFont.deriveFont(Font.BOLD, 12);
        invalidSupplyDaysLabel.setFont(invalidSupplyDaysLabelNewFont);
        invalidSupplyDaysLabel.setForeground(Color.RED);





        JPanel inputPanel = new JPanel();
        int verticalGap = 7; // Set the desired vertical gap between rows
        int horizontalGap = 10;
        inputPanel.setLayout(new GridLayout(14, 3, horizontalGap, verticalGap));
        //add name line
        inputPanel.add(categoryLabel);
        inputPanel.add(categoryTextField);
        inputPanel.add(invalidCategoryLabel);
        invalidCategoryLabel.setVisible(false);
        //add address line
        inputPanel.add(subCategoryLabel);
        inputPanel.add(subCategoryTextField);
        inputPanel.add(invalidSubCategoryLabel);
        invalidSubCategoryLabel.setVisible(false);
        //add id line
        inputPanel.add(subSubCategoryLabel);
        inputPanel.add(subSubCategoryTextField);
        inputPanel.add(invalidSubSubCategoryLabel);
        invalidSubSubCategoryLabel.setVisible(false);
        //add bank line
        inputPanel.add(bankLabel);
        inputPanel.add(bankTextField);
        inputPanel.add(invalidBankLabel);
        invalidBankLabel.setVisible(false);
        //add payment method line
        inputPanel.add(paymentLabel);
        inputPanel.add(PaymentComboBox);
        inputPanel.add(invalidPaymentLabel);
        invalidPaymentLabel.setVisible(false);
        //add contact name line
        inputPanel.add(ContactName);
        inputPanel.add(memberNameTextField);
        inputPanel.add(invalidMemberNameLabel);
        invalidMemberNameLabel.setVisible(false);
        //add contact mail line
        inputPanel.add(ContactMail);
        inputPanel.add(memberMailTextField);
        inputPanel.add(invalidMemberMailLabel);
        invalidMemberMailLabel.setVisible(false);
        //add contact phone line
        inputPanel.add(ContactPhone);
        inputPanel.add(memberPhoneTextField);
        inputPanel.add(invalidMemberPhoneLabel);
        invalidMemberPhoneLabel.setVisible(false);
        inputPanel.add(minRangeLabel);
        inputPanel.add(minRangeLabelTextField);
        inputPanel.add(submitDiscount);
        //top range discount
        inputPanel.add(maxRangeLabel);
        inputPanel.add(maxRangeLabelTextField);
        inputPanel.add(invalidmaxRangeLabel);
        invalidmaxRangeLabel.setVisible(false);
        //discount number
        inputPanel.add(discountLabel);
        inputPanel.add(discountLabelTextField);
        inputPanel.add(invalidDiscountLabel);
        invalidDiscountLabel.setVisible(false);
        //add supply method line
        inputPanel.add(supplyMethod);
        inputPanel.add(SupplyComboBox);
        inputPanel.add(supplyButton);

        inputPanel.add(FixDays);
        inputPanel.add(daysComboBox);
        inputPanel.add(daysButton);
        FixDays.setVisible(false);
        daysComboBox.setVisible(false);
        daysButton.setVisible(false);

        inputPanel.add(SupplyDays);
        inputPanel.add(SupplyDaysTextField);
        inputPanel.add(invalidSupplyDaysLabel);
        SupplyDays.setVisible(false);
        SupplyDaysTextField.setVisible(false);
        invalidSupplyDaysLabel.setVisible(false);

        submitDiscount.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                invalidmaxRangeLabel.setVisible(false);
                invalidDiscountLabel.setVisible(false);
                String max_r = maxRangeLabelTextField.getText();
                String discount = discountLabelTextField.getText();
//                String amount = amountLabelTextField.getText();
//
                int min_range = Integer.parseInt(minRangeLabelTextField.getText());
                if(!checkIfPositiveIntNumber(max_r) || Integer.parseInt(max_r) <= min_range ){
                    invalidmaxRangeLabel.setVisible(true);
                }
                else if(!checkIfPositiveDoubleNumber(discount)){
                    invalidDiscountLabel.setVisible(true);
                }
                else{
                    int max_range  = Integer.parseInt(max_r);
                    double disc = Double.parseDouble(discount);
                    Range range = new Range (min_range,max_range);
                    PrecentageDiscount discount1 = new PrecentageDiscount(range,disc);
                    discounts.add(discount1);
                    int new_min_range = max_range+1;
                    minRangeLabelTextField.setText(String.valueOf(new_min_range));
                    JOptionPane.showMessageDialog(null, "Discount added successfully! ");




                }
            }
        });
//        invalidMemberPhoneLabel.setVisible(false);
        supplyButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                if(SupplyComboBox.getSelectedItem().equals("Fixed Days")){
                    FixDays.setVisible(true);
                    daysComboBox.setVisible(true);
                    daysButton.setVisible(true);
                    SupplyDays.setVisible(false);
                    SupplyDaysTextField.setVisible(false);

                }
                else if(SupplyComboBox.getSelectedItem().equals("Super Lee Transport")){
                    SupplyDays.setVisible(false);
                    SupplyDaysTextField.setVisible(false);
                    FixDays.setVisible(false);
                    daysComboBox.setVisible(false);
                    daysButton.setVisible(false);
                }
                else{
                    SupplyDays.setVisible(true);
                    SupplyDaysTextField.setVisible(true);
                    FixDays.setVisible(false);
                    daysComboBox.setVisible(false);
                    daysButton.setVisible(false);

                }
            }
        });

        daysButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                String day = (String) daysComboBox.getSelectedItem();
                if(day.equals("Sunday"))
                    daysWeek[0] =1;
                if(day.equals("Monday"))
                    daysWeek[1]=1;
                if(day.equals("Tuesday"))
                    daysWeek[2]=1;
                if(day.equals("Wednesday"))
                    daysWeek[3]=1;
                if(day.equals("Thursday"))
                    daysWeek[4] =1;
                if(day.equals("Friday"))
                    daysWeek[5] =1;
                if(day.equals("Saturday"))
                    daysWeek[6] =1;
                JOptionPane.showMessageDialog(null,  day + " added as supply day");

            }
        });

        inputPanel.setOpaque(false);

//        mainPanel.add(inputPanel, BorderLayout.CENTER);
        JPanel inputWrapperPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        inputWrapperPanel.add(inputPanel);
        mainPanel.add(inputWrapperPanel, BorderLayout.WEST);

        inputWrapperPanel.setOpaque(false);

        // Create button panel
        JButton submitButton = new JButton("Submit");
        //JButton submitButton = createButton("Submit", "/GUI/pictures/stock-manager.jpg");
        JButton backButton = new JButton("Back");

        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonsPanel.add(submitButton);
        buttonsPanel.add(backButton);

        buttonsPanel.setOpaque(false);

        mainPanel.add(buttonsPanel, BorderLayout.SOUTH);

        add(mainPanel, BorderLayout.CENTER);

        submitButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                invalidCategoryLabel.setVisible(false);
                invalidSubCategoryLabel.setVisible(false);
                invalidSubSubCategoryLabel.setVisible(false);
                invalidBankLabel.setVisible(false);
                invalidPaymentLabel.setVisible(false);
                invalidMemberNameLabel.setVisible(false);
                invalidMemberMailLabel.setVisible(false);
                invalidMemberPhoneLabel.setVisible(false);
                FixDays.setVisible(false);
                daysComboBox.setVisible(false);
                daysButton.setVisible(false);
                SupplyDays.setVisible(false);
                SupplyDaysTextField.setVisible(false);
                invalidSupplyDaysLabel.setVisible(false);
//                 Handle submit button action
                mistake_counter = 0;
                Payment_method paymentMethod= null;
                String name = categoryTextField.getText();
                String address = subCategoryTextField.getText();
                String id = subSubCategoryTextField.getText();
                String bank  = bankTextField.getText();
                String payment = (String) PaymentComboBox.getSelectedItem();
                String contact_name = memberNameTextField.getText();
                String contact_mail = memberMailTextField.getText();
                String contact_phone = memberPhoneTextField.getText();
                String Supply_Method = (String) SupplyComboBox.getSelectedItem();

                if(Supply_Method.equals("Fixed Days")){
                    methodSupply = new ByFixedDays("By Fixed Days", daysWeek);
                }
                else if(Supply_Method.equals("Super Lee Transport")){
                    methodSupply = new BySuperLee("By Supper Lee");
                }
                else{
                    if(!checkIfPositiveInteger(SupplyDaysTextField.getText())){
                        mistake_counter++;
                        invalidSupplyDaysLabel.setVisible(true);

                    }
                    else{
                        supplyDays = Integer.parseInt(SupplyDaysTextField.getText());
                        methodSupply = new BySupplyDays("By Supply Days", supplyDays);
                    }
                }
                if(payment.equals("bit")){
                    paymentMethod = Payment_method.bit;
                }
                if(payment.equals("cash")){
                    paymentMethod = Payment_method.cash;
                }
                if(payment.equals("credit card")){
                    paymentMethod = Payment_method.credit_card;
                }
                if(!checkIfOnlyLetters(name)){
                    mistake_counter++;
                    invalidCategoryLabel.setVisible(true);
                }
                if(!checkIfOnlyLetters(address)){
                    mistake_counter++;
                    invalidSubCategoryLabel.setVisible(true);
                }
                if(!checkIfPositiveInteger(id)){
                    mistake_counter++;
                    invalidSubSubCategoryLabel.setVisible(true);
                }
                if(checkIfPositiveInteger(id) && SupplierDAO.getInstance().getSupplier(Integer.parseInt(id))!=null) {
                    mistake_counter++;
                    invalidSubSubCategoryLabel.setVisible(true);
                }
                if(!checkIfPositiveInteger(bank)){
                    mistake_counter++;
                    invalidBankLabel.setVisible(true);
                }
                if(!checkIfOnlyLetters(contact_name)){
                    mistake_counter++;
                    invalidMemberNameLabel.setVisible(true);
                }
                if(!contact_mail.contains("@")){
                    mistake_counter++;
                    invalidMemberMailLabel.setVisible(true);
                }
                if(!checkIfPositiveInteger(contact_phone)){
                    mistake_counter++;
                    invalidMemberPhoneLabel.setVisible(true);
                }
                if(mistake_counter== 0){
                    int ID = Integer.parseInt(id);
                    int BANK = Integer.parseInt(bank);
                    ContactMember contactMember = new ContactMember(contact_phone,contact_name,contact_mail,ID);
                    LinkedList<ContactMember> contact_list = new LinkedList<>();
                    contact_list.add(contactMember);
                    int company = 1111;
                    LinkedList<String> category = new LinkedList<>();
                    SupplierCard supplierCard = new SupplierCard(name, ID, company, BANK,address,paymentMethod,contact_list,category );
                    Agreement agreement = new Agreement(ID, methodSupply);
                    agreement.setTotal_orderDiscount(discounts);
                    Supplier s = SupplyManager.getSupply_manager().CreateSupplier(supplierCard,agreement);
                    JOptionPane.showMessageDialog(null, name  + " added successfully!");

                }
            }
        });

                backButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        supplierGUI.showDefaultPanelFromChild();
                    }
                });

            }
            private JPanel createTextFieldPanel(JLabel label, JTextField textField) {
                JPanel panel = new JPanel(new BorderLayout());
                panel.add(label, BorderLayout.WEST);
                panel.add(textField, BorderLayout.CENTER);
                return panel;
            }

            // All the functions that check if the input is valid
            Boolean checkIfOnlyLetters(String str) {
                if (str.equals("")) {
                    return false;
                }
                return str.matches("[a-zA-Z' ]+");
            }

            boolean checkIfPositiveInteger(String number) {
                try {
                    if (number.equals("")) {
                        return false;
                    }
                    int d = Integer.parseInt(number);
                    return d > 0;
                } catch (NumberFormatException e) {
                    return false;
                }
            }
    public boolean isValidString(String input) {
        // Regular expression pattern
        String pattern = "^[a-zA-Z0-9\\.]+$";

        // Check if the input matches the pattern
        return input.matches(pattern);
    }



    boolean checkIfPositiveDoubleNumber(String number) {
        try {
            if (number.equals("")) {
                return false;
            }
            double d = Double.parseDouble(number);
            return d > 0.0;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    boolean checkIfPositiveIntNumber(String number) {
        try {
            if (number.equals("")) {
                return false;
            }
            int d = Integer.parseInt(number);
            return d > 0.0;
        } catch (NumberFormatException e) {
            return false;
        }
    }
        }

