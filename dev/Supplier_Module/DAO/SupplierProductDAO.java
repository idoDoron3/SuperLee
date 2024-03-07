package Supplier_Module.DAO;

import java.sql.*;
import java.text.MessageFormat;
import java.text.ParseException;
import java.util.*;

import Supplier_Module.Business.Agreement.SupplierProduct;
import Supplier_Module.Business.Discount.PrecentageDiscount;
import Supplier_Module.Business.Discount.Range;
import Supplier_Module.Business.Supplier;

public class SupplierProductDAO extends DAO {
    private static final String PRODUCT_ID = "PRODUCT_ID";
    private static final String SUPPLIER_ID = "SUPPLIER_ID";
    private static final String P_NAME = "P_NAME";
    private static final String PRICE = "PRICE";
    private static final String WEIGHT = "WEIGHT";
    private static final String AMOUNT = "AMOUNT";
    private static final String DISCOUNT_BY_AMOUNT = "DISCOUNT_BY_AMOUNT";
    private HashMap<Pair<String, Integer>, SupplierProduct> supplierProductHashMap;

    private static SupplierProductDAO instance = null;

    public SupplierProductDAO() {
        super("SUPPLIER_PRODUCTS");
        supplierProductHashMap = new HashMap<>();
    }

    public static SupplierProductDAO getInstance() {
        if (instance == null)
            instance = new SupplierProductDAO();
        return instance;
    }

    @Override
    public boolean Insert(Object productObj) {
        SupplierProduct sProduct = (SupplierProduct) productObj;
        boolean res = true;
        String sql = MessageFormat.format("INSERT INTO {0} ({1}, {2}, {3}, {4}, {5}, {6}, {7} ) VALUES(?, ?, ?, ?, ?, ?, ?) "
                , _tableName, PRODUCT_ID, SUPPLIER_ID, P_NAME, PRICE, WEIGHT, AMOUNT, DISCOUNT_BY_AMOUNT);
        try (
                PreparedStatement pstmt = connection.prepareStatement(sql)) {
            //Connection connection=open
            //pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, sProduct.getLocal_key());
            pstmt.setInt(2, sProduct.getSupplierID());
            pstmt.setString(3, sProduct.getProduct_name());
            pstmt.setDouble(4, sProduct.getUnit_price());
            pstmt.setDouble(5, sProduct.getUnit_weight());
            pstmt.setInt(6, sProduct.getAmount_available());
            pstmt.setString(7, sProduct.allDiscountToString());
            pstmt.executeUpdate();
            if (!isInSupplierProductsMap(sProduct))
                supplierProductHashMap.put(new Pair<>(sProduct.getProduct_name(), sProduct.getSupplierID()), sProduct);

        } catch (SQLException e) {
            System.out.println("Got Exception:");
            System.out.println(e.getMessage());
            System.out.println(sql);
            res = false;
        }
        return res;
    }

    public boolean isInSupplierProductsMap(SupplierProduct supplierProduct) {
        for (Map.Entry<Pair<String, Integer>, SupplierProduct> entry : supplierProductHashMap.entrySet()) {
            String entryname = entry.getKey().getFirst();
            int entryId = entry.getKey().getSecond();
            String pName = supplierProduct.getProduct_name();
            int PsupId = supplierProduct.getSupplierID();
            if ((entryname.compareTo(pName) > 0) && entryId == PsupId) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean Delete(Object obj) {
        SupplierProduct supplierProduct = (SupplierProduct) obj;
        boolean succeed = true;
        String sql = MessageFormat.format("DELETE FROM {0} WHERE {1} = ? AND {2} = ?"
                , _tableName, P_NAME, SUPPLIER_ID);

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, supplierProduct.getProduct_name());
            pstmt.setInt(2, supplierProduct.getSupplierID());
            pstmt.executeUpdate();
            for (Map.Entry<Pair<String, Integer>, SupplierProduct> entry : supplierProductHashMap.entrySet()) {
                String entryname = entry.getKey().getFirst();
                int entryId = entry.getKey().getSecond();
                String pName = supplierProduct.getProduct_name();
                int PsupId = supplierProduct.getSupplierID();
                if ((entryname.equals(pName)) && entryId == PsupId) {
                    supplierProductHashMap.remove(entry.getKey());
                    if (SupplierDAO.getInstance().getSupplierByProductMap().get(pName) == null) {
                        break;
                    } else {
                        List<Supplier> suppliers = new ArrayList<>(SupplierDAO.getInstance().getSupplierByProductMap().get(pName).values());
                        if (suppliers.size() == 1) {
                            SupplierDAO.getInstance().getSupplierByProductMap().remove(pName);
                        } else {
                            SupplierDAO.getInstance().getSupplierByProductMap().get(pName).remove(SupplierDAO.getInstance().getSupplier(PsupId));
                        }
                        break;

                    }
//                    if(suppliers==null){
//                        break;
//                    }

                }

            }

        } catch (SQLException e) {
            System.out.println("Got Exception:");
            System.out.println(e.getMessage());
            System.out.println(sql);
            succeed = false;
        }
        return succeed;
    }

    @Override
    public SupplierProduct convertReaderToObject(ResultSet rs) throws SQLException, ParseException {
        int localKey = rs.getInt(PRODUCT_ID);
        int SupplierID = rs.getInt(SUPPLIER_ID);
        String Name = rs.getString(P_NAME);
        double weight = rs.getDouble(WEIGHT);
        double price = rs.getDouble(PRICE);
        int amount = rs.getInt(AMOUNT);
        String DisByAmount = rs.getString(DISCOUNT_BY_AMOUNT);
        LinkedList<PrecentageDiscount> discounts = new LinkedList<>();
        String[] triples = DisByAmount.split(",");
        for (int i = 0; i < triples.length - 1; i++) {
            String[] triple = triples[i].split("[-:]");
            int min = Integer.parseInt(triple[0]);
            int max = Integer.parseInt(triple[1]);
            Range range = new Range(min, max);
            double percentage = Double.parseDouble(triple[2]);
            PrecentageDiscount p1 = new PrecentageDiscount(range, percentage);
            discounts.add(p1);
        }
        SupplierProduct supplierProduct = new SupplierProduct(Name, localKey, weight, price, amount, discounts, SupplierID);
        return supplierProduct;
    }


    public SupplierProduct getSupplierProductById(String Pname, int SuppId) {
        for (Map.Entry<Pair<String, Integer>, SupplierProduct> entry : supplierProductHashMap.entrySet()) {
            if (entry.getKey().getFirst().equals(Pname) && entry.getKey().getSecond() == SuppId)
                return entry.getValue();
        }
        SupplierProduct sProduct = null;
        try {

            Connection c = DBTables.getInstance().open();
            String sql = "SELECT * FROM " + DBTables.SUPPLIER_PRODUCT + " WHERE " + P_NAME +
                    " = ? AND " + SUPPLIER_ID + " = ?;";

            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, Pname);
            stmt.setInt(2, SuppId);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                sProduct = convertReaderToObject(rs);
                if (!isInSupplierProductsMap(sProduct))
                    supplierProductHashMap.put(new Pair<>(sProduct.getProduct_name(), sProduct.getSupplierID()), sProduct);
            }
            rs.close();
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sProduct;
    }

    public boolean UpdatePrice(int supplierID, String name, double price) {
        boolean res = true;
        String sql = MessageFormat.format("UPDATE {0} SET {1} = ? WHERE {2} = ? and {3} = ? "
                , _tableName, PRICE, SUPPLIER_ID, P_NAME);
        try (
                PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setDouble(1, price);
            pstmt.setInt(2, supplierID);
            pstmt.setString(3, name);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Got Exception:");
            System.out.println(e.getMessage());
            System.out.println(sql);
            res = false;
        }
        return res;
    }


    public boolean UpdateAmount(int supplierID, String name, int amount) {
        boolean res = true;
        String sql = MessageFormat.format("UPDATE {0} SET {1} = ? WHERE {2} = ? and {3} = ? "
                , _tableName, AMOUNT, SUPPLIER_ID, P_NAME);
        try (
                PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setDouble(1, amount);
            pstmt.setInt(2, supplierID);
            pstmt.setString(3, name);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Got Exception:");
            System.out.println(e.getMessage());
            System.out.println(sql);
            res = false;
        }
        return res;
    }


    public LinkedList<SupplierProduct> getProductsBySupplier(int supplierID) {
        LinkedList<SupplierProduct> prod = new LinkedList<>();
        try {
            Connection c = DBTables.getInstance().open();
            Statement stmt;
            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM " + DBTables.SUPPLIER_PRODUCT + " WHERE " + SUPPLIER_ID + " = " + supplierID + ";");
            while (rs.next()) {
                SupplierProduct supplierProduct = convertReaderToObject(rs);
                Pair<String, Integer> temp = new Pair<>(supplierProduct.getProduct_name(), supplierID);
                this.supplierProductHashMap.put(temp, supplierProduct);
                prod.add(supplierProduct);
            }
            rs.close();
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return prod;
    }

    public List<SupplierProduct> getProductsMapper() {
        List<SupplierProduct> prod = new LinkedList<>();
        try {
            Connection c = DBTables.getInstance().open();
            Statement stmt;
            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM " + DBTables.SUPPLIER_PRODUCT +";");
            while (rs.next()) {
                SupplierProduct supplierProduct = convertReaderToObject(rs);
                prod.add(supplierProduct);
            }
            rs.close();
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return prod;
    }

}
