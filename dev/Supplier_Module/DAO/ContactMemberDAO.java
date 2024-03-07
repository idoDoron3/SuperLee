package Supplier_Module.DAO;

import Supplier_Module.Business.Card.ContactMember;

import java.sql.*;
import java.text.MessageFormat;
import java.text.ParseException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class ContactMemberDAO extends DAO{
    private static final String SUPPLIER_ID = "SUPPLIER_ID";
    private static final String C_NAME = "C_NAME";
    private static final String EMAIL = "EMAIL";
    private static final String PHONE_NUMBER = "PHONE_NUMBER";
    private HashMap<Pair<Integer, String>, ContactMember> contactsHashMap; //suppID, Phone numbre - contact
    private static ContactMemberDAO instance = null;

    public static ContactMemberDAO getInstance(){
        if(instance == null)
            instance = new ContactMemberDAO();
        return instance;
    }
    private ContactMemberDAO() {
        super("CONTACTS");
        contactsHashMap = new HashMap<>();
    }

    @Override
    public boolean Insert(Object contactObj) {
        ContactMember contact = (ContactMember) contactObj;
        boolean res = true;
        String sql = MessageFormat.format("INSERT INTO {0} ({1}, {2}, {3}, {4}) VALUES(?, ?, ?, ?) "
                , _tableName, SUPPLIER_ID, C_NAME, EMAIL, PHONE_NUMBER
        );
        try (
                PreparedStatement pstmt = connection.prepareStatement(sql)) {
            //Connection connection=open
            //pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, contact.getSupplierID());
            pstmt.setString(2, contact.getName());
            pstmt.setString(3, contact.getEmail());
            pstmt.setString(4, contact.getPhone_number());
            pstmt.executeUpdate();
            if (!isInContactsMap(contact))
                contactsHashMap.put(new Pair<>(contact.getSupplierID(), contact.getPhone_number()), contact);


        } catch (SQLException e) {
            System.out.println("Got Exception:");
            System.out.println(e.getMessage());
            System.out.println(sql);
            res = false;
        }
        return res;
    }

    private boolean isInContactsMap(ContactMember c) {
        for (HashMap.Entry<Pair<Integer, String>, ContactMember> entry : contactsHashMap.entrySet()) {
            if (entry.getKey().getFirst() == c.getSupplierID() && entry.getKey().getSecond().equals(c.getName()))
                return true;
        }
        return false;
    }

    @Override
    public boolean Delete(Object contactObj) {
        ContactMember contact = (ContactMember) contactObj;
        boolean res = true;
        String sql = MessageFormat.format("DELETE FROM {0} WHERE {1} = ? AND {2} = ? AND {3} = ? AND {4} = ?"
                , _tableName, SUPPLIER_ID, C_NAME, EMAIL, PHONE_NUMBER);

        try (
                PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, contact.getSupplierID());
            pstmt.setString(2, contact.getName());
            pstmt.setString(3, contact.getEmail());
            pstmt.setString(4, contact.getPhone_number());
            pstmt.executeUpdate();
            for (HashMap.Entry<Pair<Integer, String>, ContactMember> entry : contactsHashMap.entrySet()) {
                if (entry.getKey().getFirst() == contact.getSupplierID() && entry.getKey().getSecond().equals(contact.getPhone_number())){
                    contactsHashMap.remove(entry.getKey());
                    break;
                }
            }
        } catch (SQLException e) {
            System.out.println("Got Exception:");
            System.out.println(e.getMessage());
            System.out.println(sql);
            res = false;
        }
        return res;
    }

    @Override
    public Object convertReaderToObject(ResultSet rs) throws SQLException, ParseException {
            ContactMember contact = new ContactMember(rs.getString(PHONE_NUMBER), rs.getString(C_NAME), rs.getString(EMAIL), rs.getInt(SUPPLIER_ID));
            return contact;
    }


    public ContactMember getContactByPhoneNumber(String phoneNum) {
        for (HashMap.Entry<Pair<Integer, String>, ContactMember> entry : contactsHashMap.entrySet()) {
            if (entry.getKey().getSecond().equals(phoneNum))
                return contactsHashMap.get(entry.getKey());
        }
        ContactMember contact = null;
        try {
            Connection c = DBTables.getInstance().open();
            Statement stmt;
            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM " + DBTables.CONTACTS + " WHERE " + PHONE_NUMBER + " = " + phoneNum + ";");
            if (rs.next()) {
                {
                    contact = (ContactMember) this.convertReaderToObject(rs);
                    contactsHashMap.put(new Pair<>(contact.getSupplierID(), contact.getPhone_number()), contact);
                    return contact;
                }
            }
            rs.close();
            stmt.close();
        }
        catch(Exception e){
                e.printStackTrace();
            }
        return contact;
    }


    public List<ContactMember> getAllContacts() {
        List<ContactMember> contList = new LinkedList<>();
        try {
            Connection c = DBTables.getInstance().open();
            Statement stmt;
            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM " + DBTables.CONTACTS + ";");
            while (rs.next()) {
                ContactMember contact = (ContactMember)convertReaderToObject(rs);
                contList.add(contact);
                if (!isInContactsMap(contact))
                    contactsHashMap.put(new Pair<>(contact.getSupplierID(), contact.getPhone_number()), contact);
            }
            rs.close();
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return contList;
    }
    public LinkedList<ContactMember> getContactsBySupplierID(int supplierID) {
        LinkedList<ContactMember> contList = new LinkedList<>();
        try {
            Connection c = DBTables.getInstance().open();
            Statement stmt;
            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM " + DBTables.CONTACTS + " WHERE " + SUPPLIER_ID + " = " + supplierID + ";");
            while (rs.next()) {
                ContactMember contact = (ContactMember)convertReaderToObject(rs);
                contList.add(contact);
                if (!isInContactsMap(contact))
                    contactsHashMap.put(new Pair<>(contact.getSupplierID(), contact.getPhone_number()), contact);;
            }
            rs.close();
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return contList;
    }

    public boolean UpdateContactEmail(int supplierID,String phone, String email){
        boolean res = true;
        String sql = MessageFormat.format("UPDATE {0} SET {1} = ? WHERE {2} = ? and {3} = ? "
                , _tableName,EMAIL,SUPPLIER_ID,PHONE_NUMBER);
        try (
                PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, email);
            pstmt.setInt(2, supplierID);
            pstmt.setString(3, phone);
            pstmt.executeUpdate();
//            Pair<Integer,String> temp = new Pair(supplierID,phone);
//            contactsHashMap.get(temp).setEmail(email);

        } catch (SQLException e) {
            System.out.println("Got Exception:");
            System.out.println(e.getMessage());
            System.out.println(sql);
            res = false;
        }
        return res;
    }
    public boolean UpdateContactName(int supplierID,String phone, String name){
        boolean res = true;
        String sql = MessageFormat.format("UPDATE {0} SET {1} = ? WHERE {2} = ? and {3} = ? "
                , _tableName,C_NAME,SUPPLIER_ID,PHONE_NUMBER);
        try (
                PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setInt(2, supplierID);
            pstmt.setString(3, phone);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Got Exception:");
            System.out.println(e.getMessage());
            System.out.println(sql);
            res = false;
        }
        return res;
    }

    public int getNumOfContacts() {
        int counter = 0;
        try {
            Connection c = DBTables.getInstance().open();
            Statement stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM " + DBTables.CONTACTS + ";");
            while (rs.next()) {
                counter++;
            }
            rs.close();
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return counter;
    }
}
