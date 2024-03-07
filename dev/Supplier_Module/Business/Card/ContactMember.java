package Supplier_Module.Business.Card;

import java.util.LinkedList;

public class ContactMember {

    private String phone_number;
    private String name;
    private String email;
    private int supplierID;

    /**constructor
     *
     */
    public ContactMember( String phone_number, String name, String email, int supplierID) {
        this.phone_number = phone_number;
        this.name = name;
        this.email = email;
        this.supplierID=supplierID;
    }


    /**
     *Getter and Setters
     */
    public String getPhone_number() {
        return phone_number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getSupplierID() {
        return supplierID;
    }


    /**
     * function that print the contact member info
     */
    public void printContactMember()
    {
        System.out.println("Name: "+this.name+" Email: "+this.email+ " Phone Number: "+this.phone_number );
    }

    public LinkedList<String> getContactReport()
    {
        LinkedList<String> temp=new LinkedList<>();
        temp.add("Name:" + this.name);
        temp.add("Phone: "+this.phone_number);
        temp.add("Mail: "+this.email);

        return temp;
    }

}

