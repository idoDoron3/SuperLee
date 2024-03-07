package Supplier_Module.DAO;
import Supplier_Module.Business.Agreement.Agreement;
import Supplier_Module.Business.Agreement.MethodSupply.ByFixedDays;
import Supplier_Module.Business.Agreement.MethodSupply.BySuperLee;
import Supplier_Module.Business.Agreement.MethodSupply.BySupplyDays;
import Supplier_Module.Business.Agreement.MethodSupply.MethodSupply;
import Supplier_Module.Business.Agreement.SupplierProduct;
import Supplier_Module.Business.Card.ContactMember;
import Supplier_Module.Business.Card.SupplierCard;
import Supplier_Module.Business.Defs.EOM;
import Supplier_Module.Business.Defs.Payment_method;
import Supplier_Module.Business.Discount.PrecentageDiscount;
import Supplier_Module.Business.Discount.Range;
import Supplier_Module.Business.Supplier;
import java.sql.*;
import java.text.MessageFormat;
import java.text.ParseException;
import java.util.*;

public class SupplierDAO extends DAO {
    public static final String ID = "ID";
    public static final String SUP_NAME = "SUP_NAME";
    public static final String ADDRESS = "ADDRESS";
    public static final String PAYMENT_METHOD = "PAYMENT_METHOD";
    public static final String BANK_ACCOUNT = "BANK_ACCOUNT";
    public static final String SUPPLY_METHOD = "SUPPLY_METHOD";
    public static final String DELIVERY_DAYS = "DELIVERY_DAYS";
    public static final String DISCOUNT_BY_AMOUNT = "DISCOUNT_BY_AMOUNT";
    public static final String CATEGORIES = "CATEGORIES";
    public static final String EOM = "EOM";


    private HashMap<Integer, Supplier> supplierHashMap;
    private HashMap<String, Map<Integer,Supplier>> supplierByProductMap; // Map<supID, sup>
    private static SupplierDAO instance = null;

    public static SupplierDAO getInstance(){
        if(instance == null)
            instance = new SupplierDAO();
        return instance;
    }
    private static String TableName="Suppliers";

    private SupplierDAO() {
        super(TableName);
        supplierHashMap = new HashMap<>();
        supplierByProductMap = new HashMap<>();
    }

    public HashMap<Integer, Supplier> getSupplierHashMap() {
        return supplierHashMap;
    }

    public HashMap<String, Map<Integer,Supplier>> getSupplierByProductMap() {
        return supplierByProductMap;
    }

    @Override
    public boolean Insert(Object supplierObj) {
        Supplier supplier = (Supplier) supplierObj;
        SupplierCard supplierCard = supplier.getCard();
        Agreement agreement = supplier.getAgreement();
        boolean res = true;
        List<Integer> daysList = agreement.getMethodSupply().GetSupplyDays();

        String sql = "INSERT INTO " + _tableName + " (ID, SUP_NAME, ADDRESS, PAYMENT_METHOD, BANK_ACCOUNT, SUPPLY_METHOD, DELIVERY_DAYS,DISCOUNT_BY_AMOUNT , CATEGORIES, EOM) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (
                PreparedStatement pstmt = connection.prepareStatement(sql)) {
            //Connection connection=open
            //pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, supplierCard.getSupplier_number());
            pstmt.setString(2, supplierCard.getSupplier_name());
            pstmt.setString(3, supplierCard.getAddress());
            pstmt.setString(4, supplierCard.getPayment_method().toString());
            pstmt.setInt(5, supplierCard.getBank_account());
            pstmt.setString(6, agreement.getMethodSupply().methodType());
            pstmt.setString(7,(supplierCard.listIntTostring(daysList)));
            pstmt.setString(8,(agreement.allDiscountToString()));
            pstmt.setString(9,(supplierCard.listTostring(supplierCard.getProduct_categories())));
            pstmt.setString(10,(agreement.getEom().toString()));
            pstmt.executeUpdate();

            supplierHashMap.put(supplier.getCard().getSupplier_number(), supplier);
            addSuplierByProduct(supplier);
            for(ContactMember c: supplier.getCard().getContact_members())
            {
                ContactMemberDAO.getInstance().Insert(c);
            }

        } catch (SQLException e) {
            System.out.println("Got Exception:");
            System.out.println(e.getMessage());
            System.out.println(sql);
            res = false;
        }
        return res;
    }
    //add the supplier to each of his products
    public void addSuplierByProduct(Supplier sup){
        for(SupplierProduct sp : sup.getAgreement().getProductList()){
            if(supplierByProductMap.containsKey(sp.getProduct_name())){
                supplierByProductMap.get(sp.getProduct_name()).put(sup.getAgreement().getSupplier_number(),sup);
            }
            else{
                Map<Integer,Supplier> temp =new HashMap<>();
                temp.put(sup.getAgreement().getSupplier_number(),sup);
                supplierByProductMap.put(sp.getProduct_name(),temp);
            }
            SupplierProductDAO.getInstance().Insert(sp);
        }
    }

    public void addSuplierByProductToHashMap(Supplier sup){
        for(SupplierProduct sp : sup.getAgreement().getProductList()){
            if(supplierByProductMap.containsKey(sp.getProduct_name())){
                supplierByProductMap.get(sp.getProduct_name()).put(sup.getAgreement().getSupplier_number(),sup);
            }
            else{
                Map<Integer,Supplier> temp=new HashMap<>();
                temp.put(sup.getAgreement().getSupplier_number(),sup);
                supplierByProductMap.put(sp.getProduct_name(),temp);
            }
        }
    }
//remove all the products from supplier
    public void removeSuplierByProduct(Supplier sup){
        for(SupplierProduct sp : sup.getAgreement().getProductList()){
            if(supplierByProductMap.containsKey(sp.getProduct_name())){
                if(supplierByProductMap.get(sp.getProduct_name()).size() == 1){
                    supplierByProductMap.remove(sp.getProduct_name());
                }
                else
                    supplierByProductMap.get(sp.getProduct_name()).remove(sup);
            }
        }
    }

    @Override
    public boolean Delete(Object supplierObj) {
        Supplier supplier = (Supplier) supplierObj;
        boolean res = true;
        String sql = MessageFormat.format("DELETE FROM {0} WHERE {1} = ? "
                , _tableName, ID);

        try (
                PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, supplier.getCard().getSupplier_number());
            pstmt.executeUpdate();
            if(supplierHashMap.containsKey(supplier.getCard().getSupplier_number()))
            {
                removeSuplierByProduct(supplier);
                for(SupplierProduct sp: supplier.getAgreement().getProductList())
                {
                    SupplierProductDAO.getInstance().Delete(sp);
                }
                for(ContactMember cm: supplier.getCard().getContact_members()){
                    ContactMemberDAO.getInstance().Delete(cm);
                }
                supplierHashMap.remove(supplier.getCard().getSupplier_number());
            }


        } catch (SQLException e) {
            System.out.println("Got Exception:");
            System.out.println(e.getMessage());
            System.out.println(sql);
            res = false;
        }
        return res;
    }


    public List<Supplier> SelectAllSuppliers() {
        List<Supplier> sup = new LinkedList<>();
        try {
            Connection c = DBTables.getInstance().open();
            Statement stmt;
            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM " + DBTables.SUPPLIERS + ";");
            while (rs.next()) {
                Supplier supplier = convertReaderToObject(rs);
                sup.add(supplier);
            }
            rs.close();
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sup;
    }

    @Override
    public Supplier convertReaderToObject(ResultSet rs) throws SQLException, ParseException {
        Supplier supplier = null;
        int compnumber = 100;
        int supplierID = rs.getInt(ID);
        String name = rs.getString(SUP_NAME);
        int bankAccount = Integer.parseInt(rs.getString(BANK_ACCOUNT));
        Payment_method paymentMethod = Payment_method.valueOf(rs.getString(PAYMENT_METHOD));
        String address = rs.getString(ADDRESS);
        EOM eom  = Supplier_Module.Business.Defs.EOM.valueOf(rs.getString(EOM));
        MethodSupply methodSupply=null;
        if(rs.getString(SUPPLY_METHOD).equals("By Fixed Days")){
            String str = rs.getString(DELIVERY_DAYS);
            String[] numberStrings = str.split(", ");
            List<Integer> days = new ArrayList<>();
            for (String numberString : numberStrings) {
                int number = Integer.parseInt(numberString);
                days.add(number);
            }
            int[] arr = new int[7];
            for (int i=0; i<7;i++)
            {
                arr[i]=0;
            }
            for(int day: days)
            {
                arr[day-1]++;
            }

            methodSupply=new ByFixedDays("By Fixed Days", arr);

        }
        else if(rs.getString(SUPPLY_METHOD).equals("By Supper Lee")){
            methodSupply=new BySuperLee("By Supper Lee");

        }
        else if(rs.getString(SUPPLY_METHOD).equals("By Supply Days")){
            String str = rs.getString(DELIVERY_DAYS);
            int x= Integer.parseInt(str);
            methodSupply=new BySupplyDays("By Supply Days",x);
        }
        String categories = rs.getString(CATEGORIES);
        String[] strings = categories.split(", ");
        LinkedList<String> categ = new LinkedList<>();
        for (String str : strings) {
            categ.add(str);
        }
        String DisByAmount = rs.getString(DISCOUNT_BY_AMOUNT);
        LinkedList<PrecentageDiscount> discounts = new LinkedList<>();
        String[] triples = DisByAmount.split(",");
        for (int i = 0; i < triples.length - 1; i++) {
            String[] triple = triples[i].split("[-:]");
            int min=Integer.parseInt(triple[0]);
            int max=Integer.parseInt(triple[1]);
            Range range = new Range(min,max);
            double percentage=Double.parseDouble(triple[2]);
            PrecentageDiscount p1 = new PrecentageDiscount(range,percentage);
            discounts.add(p1);
        }

        LinkedList<SupplierProduct> supplierProducts = SupplierProductDAO.getInstance().getProductsBySupplier(supplierID);
        LinkedList<ContactMember> contactMembers = ContactMemberDAO.getInstance().getContactsBySupplierID(supplierID);

        SupplierCard supplierCard = new SupplierCard(name,supplierID,compnumber,bankAccount,address, paymentMethod,contactMembers,categ);
        Agreement agreement = new Agreement(supplierProducts,discounts,methodSupply ,eom, supplierID);

        supplier = new Supplier(supplierCard,agreement);
        supplierHashMap.put(supplier.getCard().getSupplier_number(), supplier);
        addSuplierByProductToHashMap(supplier);
        return supplier;
    }

    public boolean UpdateBankAccount(int supplierID,String bankAccount){
        boolean res = true;

        String sql = MessageFormat.format("UPDATE {0} SET {1} = ? WHERE {2} = ? "
                , _tableName,BANK_ACCOUNT,ID);
        try (
                PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1,bankAccount);
            pstmt.setInt(2, supplierID);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Got Exception:");
            System.out.println(e.getMessage());
            System.out.println(sql);
            res = false;
        }
        return res;
    }

    public boolean UpdateCategory(int supplierID,String allCategories){
        boolean res = true;
        String sql = MessageFormat.format("UPDATE {0} SET {1} = ? WHERE {2} = ? "
                , _tableName,CATEGORIES,ID);
        try (
                PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1,allCategories);
            pstmt.setInt(2, supplierID);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Got Exception:");
            System.out.println(e.getMessage());
            System.out.println(sql);
            res = false;
        }
        return res;
    }

    public boolean UpdatePaymentMethod(int supplierID,String paymentMethod){
        boolean res = true;
        String sql = MessageFormat.format("UPDATE {0} SET {1} = ? WHERE {2} = ? "
                , _tableName,PAYMENT_METHOD,ID);
        try (
                PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1,paymentMethod);
            pstmt.setInt(2, supplierID);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Got Exception:");
            System.out.println(e.getMessage());
            System.out.println(sql);
            res = false;
        }
        return res;
    }

    public boolean UpdateAddress(int supplierID, String address) {
        boolean res = true;
        String sql = MessageFormat.format("UPDATE {0} SET {1} = ? WHERE {2} = ? "
                , _tableName,ADDRESS,ID);
        try (
                PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1,address);
            pstmt.setInt(2, supplierID);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Got Exception:");
            System.out.println(e.getMessage());
            System.out.println(sql);
            res = false;
        }
        return res;
    }

    public boolean UpdateEOM(int supplierID, String eom) {
        boolean res = true;
        String sql = MessageFormat.format("UPDATE {0} SET {1} = ? WHERE {2} = ? "
                , _tableName,EOM,ID);
        try (
                PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1,eom);
            pstmt.setInt(2, supplierID);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Got Exception:");
            System.out.println(e.getMessage());
            System.out.println(sql);
            res = false;
        }
        return res;
    }

    public Supplier getSupplier(int supplierID) {
        if (supplierHashMap.containsKey(supplierID))
            return supplierHashMap.get(supplierID);
        Supplier supplier = null;
        try {
            Connection c = DBTables.getInstance().open();
            Statement stmt;
            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM " + DBTables.SUPPLIERS + " WHERE " + ID + " = " + supplierID + ";");
            if (rs.next()) {
                supplier = convertReaderToObject(rs);
                supplierHashMap.put(supplierID, supplier);
                addSuplierByProductToHashMap(supplier);

            }
            rs.close();
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return supplier;
    }
}

