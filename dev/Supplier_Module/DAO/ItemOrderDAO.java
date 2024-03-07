package Supplier_Module.DAO;

import Supplier_Module.Business.Agreement.SupplierProduct;
import Supplier_Module.Business.Order;

import java.sql.*;
import java.text.MessageFormat;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;


public class ItemOrderDAO extends DAO{
    private static ItemOrderDAO instance = null;
    private static final String PRODUCT_NAME = "PRODUCT_NAME";
    private static final String ORDER_ID = "ORDER_ID";
    private static final String PRODUCT_ID = "PRODUCT_ID";
    private static final String UNIT_PRICE = "UNIT_PRICE";
    private static final String AMOUNT = "AMOUNT";
    private static final String DISCOUNT = "DISCOUNT";
    private static final String TOTAL_PRICE = "TOTAL_PRICE";


    private ItemOrderDAO() {
        super("ITEM_ORDERS");
    }

    public static ItemOrderDAO getInstance() {
        if (instance == null) {
            instance = new ItemOrderDAO();
        }
        return instance;
    }


    public boolean Insert(Object oPair) { //Map<name,orderid>, amount
        boolean res = true;
        Pair<Pair<SupplierProduct,Integer>, Integer> pair= (Pair<Pair<SupplierProduct,Integer>, Integer>) oPair;
        SupplierProduct sp=pair.getFirst().getFirst();
        int amount = pair.getFirst().getSecond();
        int orderID = pair.getSecond();
        String sql = MessageFormat.format("INSERT INTO {0} ({1}, {2}, {3}, {4}, {5} ,{6}, {7} ) VALUES(?, ?, ?, ?, ?, ?, ? ) "
                , _tableName, PRODUCT_NAME, ORDER_ID, PRODUCT_ID, UNIT_PRICE, AMOUNT, DISCOUNT,TOTAL_PRICE
        );
        try (
                PreparedStatement pstmt = connection.prepareStatement(sql)) {
            double discount = sp.discountByAmount(amount);
            double t_price = (100-discount)*0.01*amount*sp.getUnit_price();

            pstmt.setString(1, sp.getProduct_name());
            pstmt.setInt(2, orderID);
            pstmt.setInt(3, sp.getLocal_key());
            pstmt.setDouble(4,sp.getUnit_price());
            pstmt.setInt(5, amount);
            pstmt.setDouble(6, discount);
            pstmt.setDouble(7, t_price);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Got Exception:");
            System.out.println(e.getMessage());
            System.out.println(sql);
            res = false;
        }
        return res;
    }


    @Override
    public boolean Delete(Object oPair) {//list of pairs, each pari is <productName ,orderID>
        Pair<String,Integer> pair = (Pair<String,Integer>)oPair;
        boolean res = true;
        String p_name=pair.getFirst();
        int order_id=pair.getSecond();
        String sql = MessageFormat.format("DELETE FROM {0} WHERE {1} = ? AND {2} = ?"
                , _tableName, PRODUCT_NAME, ORDER_ID);

        try (
                PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, p_name);
            pstmt.setInt(2, order_id);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Got Exception:");
            System.out.println(e.getMessage());
            System.out.println(sql);
            res = false;
        }
        return res;
    }

    @Override
    public Order convertReaderToObject(ResultSet res) throws SQLException, ParseException { // there is no instance of it
        return null;
    }

    public Map<String, Integer> getItemsByOrderID(int orderID) {
        Map<String ,Integer> map = new HashMap<>();
        try {
            Connection c = DBTables.getInstance().open();
            Statement stmt;
            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM " + DBTables.ITEM_ORDERS + " WHERE " + ORDER_ID + " = " + orderID + ";");

            while (rs.next()) {
                String p_name = rs.getString(PRODUCT_NAME);
                int amount = rs.getInt(AMOUNT);
                map.put(p_name,amount);
            }
            rs.close();
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }


    public int getOrderTotalPriceByIdNOMultiDiscount(int orderID) {
        int price = -1;
        try {
            Connection c = DBTables.getInstance().open();
            Statement stmt;
            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM " + DBTables.ITEM_ORDERS + " WHERE " + ORDER_ID + " = " + orderID + ";");
            price = 0;
            while (rs.next()) {
                price += (rs.getInt(TOTAL_PRICE));

            }
            rs.close();
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return price;
    }

    public int getOrderTotalAmountOfProducts(int orderID) {
        int amount = -1;
        try {
            Connection c = DBTables.getInstance().open();
            Statement stmt;
            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM " + DBTables.ITEM_ORDERS + " WHERE " + ORDER_ID + " = " + orderID + ";");
            amount = 0;
            while (rs.next()) {
                amount += (rs.getInt(AMOUNT));

            }
            rs.close();
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return amount;
    }
    ///////////////////////////////
    public boolean changeAmountOfProduct(String p_name, int order_id, int amount){
        boolean res = true;
        String sql = MessageFormat.format("UPDATE {0} SET {1} = ? WHERE {2} = ? and {3} = ? "
                , _tableName, AMOUNT, PRODUCT_NAME, ORDER_ID);
        try (
                PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, amount);
            pstmt.setString(2, p_name);
            pstmt.setInt(3, order_id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Got Exception:");
            System.out.println(e.getMessage());
            System.out.println(sql);
            res = false;
        }
        return res;
    }
    ///////////////////////////////////////////

    public void deleteAll(){
        try {
            Connection conn = DBTables.getInstance().open();
            Statement stmt = conn.createStatement();
            stmt.executeUpdate("DELETE  FROM " + DBTables.ITEM_ORDERS);
        } catch (Exception e){
            System.out.println(e.getMessage());
        }

    }
}
