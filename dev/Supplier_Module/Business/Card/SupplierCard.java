package Supplier_Module.Business.Card;

import Stock.DataAccess.CategoryDAO;
import Supplier_Module.Business.Defs.Payment_method;
import Supplier_Module.DAO.ContactMemberDAO;
import Supplier_Module.DAO.SupplierDAO;

import java.lang.reflect.Parameter;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class SupplierCard {
    private String supplier_name;
    private String address;
    private int supplier_number;
    private int company_number;
    private int bank_account;
    private Payment_method payment_method;
    private LinkedList<ContactMember> contact_members;
    private LinkedList<String> product_categories;


    Scanner input = new Scanner(System.in);

    /**
     * basic constructor
     */
    public SupplierCard(String name, int supplier_number, int company_number, int bank_account, String address) {
        this.supplier_name = name;
        this.supplier_number = supplier_number;
        this.company_number = company_number;
        this.bank_account = bank_account;
        this.address = address;
        this.payment_method = null;
        this.contact_members = new LinkedList<>();
        this.product_categories = new LinkedList<>();

    }

    /**
     * full constructor
     */
    public SupplierCard(String supplier_name, int supplier_number, int company_number, int bank_account, String address, Payment_method payment_method, LinkedList<ContactMember> contact_members, LinkedList<String> product_categories) {
        this.supplier_name = supplier_name;
        this.address = address;
        this.supplier_number = supplier_number;
        this.company_number = company_number;
        this.bank_account = bank_account;
        this.payment_method = payment_method;
        this.contact_members = contact_members;
        this.product_categories = product_categories;
    }

    public void printCard(){
        System.out.println("----Supplier Card---");
        System.out.println("Supplier name: " + this.supplier_name);
        System.out.println("Supplier number: " + this.supplier_number);
        System.out.println("Address: " + this.address);
        System.out.println("Bank Account: " + this.bank_account);
        System.out.println("Payment Method " + this.payment_method);
        System.out.println("---Categories---");
        printCategories();
        System.out.println("---Contact Members---");
        printContacts();
    }

    /**
     * the function add to
     * this supplier categories list
     */
    public void addCategory_ToSupplier(String category) {
        if (this.product_categories.contains(category)) {
            System.out.println("This category already exist!");
        } else {
            this.product_categories.add(category);
            String str = listTostring(this.getProduct_categories());
            SupplierDAO.getInstance().UpdateCategory(this.getSupplier_number(),str);
            System.out.println("Category add successfully\n");

        }
    }
    public String listTostring(List<String> str){
        if(str.size()==0){
            return "";
        }
        String ans = str.get(0);
        for(int i = 1; i < str.size(); i++ ){
            ans+= ", "+ str.get(i);
        }
        return ans;
    }
    public String listIntTostring(List<Integer> str){
        if(str.size()==0){
            return "";
        }
        String ans = str.get(0).toString();
        for(int i =1; i< str.size(); i++){
            ans+= ", "+str.get(i).toString();
        }
        return ans;
    }


    /**
     * the function print the current supplier categories
     */
    public void printCategories()
    {
        int i = 1;
        if (product_categories.isEmpty() == false)
            System.out.println("---Categories of supplier: " + supplier_number + " ---");
        else {
            System.out.println("Category list is empty!");
        }
        for (String s : product_categories) {
            System.out.println(i + ". " + s);
            i++;
        }
        System.out.println();
    }
    /**
     * the function ask from the user details to print contact member from his card
     */
    public void printContacts() {
        if (contact_members.isEmpty())
            System.out.println("Contact member list is empty");
        else {
            System.out.println("Contact member INFO:");
        }
        for (ContactMember c : contact_members) {
            c.printContactMember();
        }
    }

    /**
     * the function ask from the user details remove contact member from his card
     */
    public boolean removeContact_members(String pNum) {
        for (ContactMember member : contact_members) {
            if (member.getPhone_number().equals(pNum)) {
                ContactMemberDAO.getInstance().Delete(member);
                contact_members.remove(member);
                return true;
            }
        }
        return false;
    }

    /**
     * the function ask from the user details to edit contact member from his card
     */
    public void editContact_members(String pNum, int option, String x) {
        for (ContactMember member : contact_members) {
            if (member.getPhone_number().equals(pNum)) {
                switch (option) {
                    case 1: {
                        member.setName(x);
                        break;
                    }
                    case 2: {
                        member.setEmail(x);
                        break;
                    }
                }
            }
        }
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getSupplier_number() {
        return supplier_number;
    }

    public String getSupplier_name() {
        return supplier_name;
    }


    /**
     * Getters
     */
    public void setCompany_number(int company_number) {
        this.company_number = company_number;
    }

    public int getBank_account() {
        return bank_account;
    }

    public void setBank_account(int bank_account) {
        this.bank_account = bank_account;
    }

    public Payment_method getPayment_method() {
        return payment_method;
    }

    /**
     * set payment method
     */
    public void setPayment_method(int x) {
        if (x == 1)
            this.payment_method = Payment_method.cash;
        else if (x == 2) {
            this.payment_method = Payment_method.bit;
        } else
            this.payment_method = Payment_method.credit_card;
    }

    public List<ContactMember> getContact_members() {
        return contact_members;
    }

    /**
     * the function ask from the user details to new contact member for his card
     */
    public void addContact_members(ContactMember x) {
        this.contact_members.add(x);
        ContactMemberDAO.getInstance().Insert(x);
        System.out.println(x.getName() + " added successfully!");
    }


    public void EditCategories(String category ,int option)
    {
        switch (option) {
            case 1: {
                addCategory_ToSupplier(category);
                printCategories();
                break;
            }

            case 2: {
                removeCategory_ToSupplier(category);
                printCategories();
                break;
            }
        }
    }

    public List<String> getProduct_categories() {
        return product_categories;
    }

    /**
     * the function remove from this supplier categories list
     */
    public void removeCategory_ToSupplier(String Category) {
        if (!this.product_categories.contains(Category)) {
            System.out.println("This category is not exist!");
        } else {
            this.product_categories.remove(Category);
            String str = listTostring(this.getProduct_categories());
            SupplierDAO.getInstance().UpdateCategory(this.supplier_number,str);
            System.out.println("Category removed successfully\n");

        }
    }


}

