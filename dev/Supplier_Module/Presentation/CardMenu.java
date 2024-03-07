package Supplier_Module.Presentation;

import Supplier_Module.Business.Card.SupplierCard;
import Supplier_Module.Business.Managers.SupplyManager;

import java.util.Scanner;

public class CardMenu {
    private static CardMenu cardMenu = null;
    private SupplyManager sup_manager;

    /**
     * constructor
     * private constractor for singleton
     */
    private CardMenu() {
        this.sup_manager = SupplyManager.getSupply_manager();
    }


    public static CardMenu getCardMenu() {
        if (cardMenu == null) {
            cardMenu = new CardMenu();
        }
        return cardMenu;
    }


    //----------------Adding functions-------------------------------
    public SupplierCard CreateSupplierCard()
    {
        Scanner input= new Scanner(System.in);
        int supplier_number=0;
        int company_number=0;
        int bank_account=0;
        boolean validSupplierNum=false;
        String supplierNum;
//        System.out.println("Enter supplier number: ");
//        supplierNum = input.nextLine();
//        validSupplierNum= isNumeric(supplierNum);

        while (validSupplierNum==false) {
            System.out.println("Enter supplier number: ");
            supplierNum = input.nextLine();
            validSupplierNum = isNumeric(supplierNum);

            if(validSupplierNum==true) // check if the input is valid number
            {
                supplier_number=Integer.parseInt(supplierNum);
                // check if the input is not already exist in the system
                if(sup_manager.isExist(supplier_number)==true)
                {
                    validSupplierNum=false;
                    System.out.print("This supplier number already exist, enter a new one: ");
                }
            }
        }



        // checking if company number is a vaild number
        boolean validCompanyNum=false;
        String companyNum;
        while (validCompanyNum==false)
        {
            System.out.println("Enter company number: ");
            companyNum=input.nextLine();
            validCompanyNum= isNumeric(companyNum);
            if(validCompanyNum==true)
            {
                company_number=Integer.parseInt(companyNum);
            }
        }

        // checking if the bank account is a vaild number
        boolean validBankAccount=false;
        String bankAccount;
        while (validBankAccount==false)
        {
            System.out.println("Enter bank account: ");
            bankAccount=input.nextLine();
            validBankAccount= isNumeric(bankAccount);
            if(validBankAccount==true)
            {
                bank_account=Integer.parseInt(bankAccount);
            }
        }
        Scanner input2= new Scanner(System.in);
        String n;
        System.out.println("Enter your name: ");
        n= input2.nextLine();
        String address;
        Scanner input3= new Scanner(System.in);
        System.out.println("Enter address");
        address=input3.nextLine();

        // creation of a new suppliercard
        SupplierCard newSupplier = this.sup_manager.CreateBasicCard(n,supplier_number,company_number,bank_account,address);

        //enter payment methood
        boolean validPaymentMethood=false;
        while(validPaymentMethood==false)
        {
            System.out.println("Choose the payment methood you want:\n 1.Cash \n 2.Bit \n 3.Credit card");
            int num = input.nextInt();
            if(num==1 || num==2 || num==3)
            {
                this.sup_manager.setPayment_method(newSupplier,num);
                validPaymentMethood=true;
            }
            else
            {
                System.out.println("You insert incorrect option!");
            }
        }

        //enter contact members
        System.out.println("Enter contact members detials:");
        int choice=1;
        while(choice==1){
            this.addContact_membersNewSupplier(newSupplier);

            System.out.println("If you want to add another contact member press 1, else press something else");
            choice=input.nextInt();
        }

        //enter products categories
        int choice2=1;
        while(choice2==1){
            Scanner reader=new Scanner(System.in);
            System.out.println("Enter products category:");
            String ans= reader.nextLine();
            this.sup_manager.addCategory_ToSupplier(newSupplier,ans);
            System.out.println("If you want to add another products category press 1, else press something else");
            choice2=reader.nextInt();
        }

        return newSupplier;

    }


    public void addContact_membersNewSupplier(SupplierCard supplierCard) {
        Scanner input= new Scanner(System.in);
        System.out.println("Enter the name:");
        String name = input.nextLine();
        System.out.println("Enter the email:");
        String email = input.nextLine();
        System.out.println("Enter the phone number:");
        String phone = input.nextLine();

        if(this.sup_manager.isExsitContactMember(supplierCard,phone))
        {
            System.out.println("This phone number is invalid!");
        }
        else
        {
            this.sup_manager.addContact_membersToCardNoData(supplierCard,phone,name,email);
        }

    }

    public void addContact_members(SupplierCard supplierCard) {
        Scanner input= new Scanner(System.in);
        System.out.println("Enter the name:");
        String name = input.nextLine();
        System.out.println("Enter the email:");
        String email = input.nextLine();
        System.out.println("Enter the phone number:");
        String phone = input.nextLine();

        if(this.sup_manager.isExsitContactMember(supplierCard,phone))
        {
            System.out.println("This phone number is invalid!");
        }
        else
        {
            this.sup_manager.addContact_membersToData(supplierCard,phone,name,email);
        }

    }


    public static boolean isNumeric(String strNum) { // check who use it
        if (strNum == null) {
            return false;
        }
        try
        {
            int d = Integer.parseInt(strNum);
        }
        catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    //---------------------- Edit functions--------------

    /**
     * function get SupplierCard
     * function asks user what he wants to edit in the SupplierCard
     */
    public void EditCard(SupplierCard x) {
        Scanner input = new Scanner(System.in);
        int choice = 9;
        while (choice != 0) {
            System.out.println("1. Change bank account.");
            System.out.println("2. Change payment methood.");
            System.out.println("3. Change the address");
            System.out.println("4. Edit contact members.");
            System.out.println("5. Edit categories of product");
            System.out.println("0. Back to main menu");
            choice = input.nextInt();
            switch (choice) {

                case 1: {
                    System.out.println("Enter a new bank account");
                    int y = input.nextInt();
                    this.sup_manager.setBank_account(x, y);
                    break;

                }

                case 2: //Change payment methood
                {
                    boolean valid = false;
                    while (valid == false) {
                        System.out.println("Choose a new payment methood:");
                        System.out.println("1. cash");
                        System.out.println("2. bit");
                        System.out.println("3. credit card");
                        int y = input.nextInt();
                        if (y == 1 || y == 2 || y == 3) {
                            valid = true;
                            this.sup_manager.setPayment_method(x, y);
                        }

                    }
                    break;

                }

                case 3: {
                    Scanner input1 = new Scanner(System.in);
                    System.out.println("Enter a new address");
                    String address = input1.nextLine();
                    this.sup_manager.setAddress(x, address);
                    break;

                }
                case 4: {
                    this.EditContactsMembers(x);
                    break;
                }

                case 5: {
                    this.EditCategories(x);
                    break;
                }

                default: {
                    if (choice != 0)
                        System.out.println("invalid choice, choose again");
                    break;
                }
            }
        }
    }

    /**
     * the function ask from the user to chose between the 3 options and use
     * the function above
     */
    public void EditContactsMembers(SupplierCard supplierCard) {
        Scanner input = new Scanner(System.in);
        int choice = 9;
        while (choice != 0) {
            System.out.println("1. Add new contact member");
            System.out.println("2. Remove a contact member");
            System.out.println("3. Edit an exist contact member");
            System.out.println("0. Quit");
            choice = input.nextInt();
            switch (choice) {
                case 1: {
                    this.addContact_members(supplierCard);
                    this.sup_manager.printContacts(supplierCard);
                    break;
                }
                case 2: {
                    this.removeContact_members(supplierCard);
                    this.sup_manager.printContacts(supplierCard);
                    break;
                }
                case 3: {
                    this.editContact_members(supplierCard);
                    this.sup_manager.printContacts(supplierCard);
                    break;
                }

                default: {
                    if (choice != 0)
                        System.out.println("invalid choice, choose again");
                    break;
                }

            }
        }
    }

    public void removeContact_members(SupplierCard supplierCard) {
        Scanner input = new Scanner(System.in);
        System.out.println("Enter the phone number of the member");
        String pNum = input.nextLine();
        if (this.sup_manager.removeContact_members(supplierCard, pNum))
            System.out.println("Remove Successfully");
        else
            System.out.println("There is not contact member with this phone number");

    }

    public void editContact_members(SupplierCard supplierCard) {
        Scanner input = new Scanner(System.in);
        boolean exist = false;
        System.out.println("Enter the phone number of the member");
        String pNum = input.nextLine();
        boolean isExsit = this.sup_manager.isExsitContactMember(supplierCard, pNum);
        if (isExsit) {
            int choice = 9;
            while (choice != 0) {
                System.out.println("1. Edit name");
                System.out.println("2. Edit mail address");
                System.out.println("0. Quit");
                choice = input.nextInt();

                switch (choice) {
                    case 1: {
                        Scanner input2 = new Scanner(System.in);
                        System.out.println("Enter the new name");
                        String name = input2.nextLine();
                        this.sup_manager.editContact_members(supplierCard, pNum, 1, name);
                        break;
                    }
                    case 2: {
                        Scanner input2 = new Scanner(System.in);
                        System.out.println("Enter the new email");
                        String m = input2.nextLine();
                        this.sup_manager.editContact_members(supplierCard, pNum, 2, m);
                        break;
                    }
                    default: {
                        if (choice != 0)
                            System.out.println("invalid choice, choose again");
                        break;
                    }

                }
            }

        } else
            System.out.println("There is not contact member with this phone number");
    }

    public void EditCategories(SupplierCard supplierCard) {
        Scanner input = new Scanner(System.in);
        int choice = 9;
        while (choice != 0) {
            System.out.println("1. Add category");
            System.out.println("2. Remove category");
            System.out.println("0. Quit");
            choice = input.nextInt();
            switch (choice) {
                case 1: {
                    Scanner input2 = new Scanner(System.in);
                    System.out.println("Enter the new category");
                    String x = input2.nextLine();
                    this.sup_manager.EditCategories(supplierCard,x,1);
                    //this.sup_manager.printCategories(supplierCard);
                    break;
                }

                case 2: {
                    Scanner input2 = new Scanner(System.in);
                    System.out.println("Enter the category you want to remove");
                    String x = input2.nextLine();
                    this.sup_manager.EditCategories(supplierCard,x,2);
                    //this.sup_manager.printCategories(supplierCard);
                    break;
                }

                default: {
                    if (choice != 0)
                        System.out.println("invalid choice, choose again");
                    break;
                }
            }
        }
    }

}
