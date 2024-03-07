package Supplier_Module.DAO;

import Supplier_Module.Business.Agreement.SupplierProduct;
import Supplier_Module.Business.Order;
import Supplier_Module.Business.Supplier;

import java.sql.*;
import java.text.MessageFormat;
import java.time.LocalDate;
import java.util.*;

public class OrderDAO extends DAO {
    private static final String ORDER_ID = "ORDER_ID";
    private static final String SUPPLIER_ID = "SUPPLIER_ID";
    private static final String ORDER_START_DATE = "ORDER_START_DATE";
    private static final String ORDER_SUPPLY_DATE = "ORDER_SUPPLY_DATE";
    private static final String ORDER_KIND = "ORDER_KIND";
    private static final String DISCOUNT = "DISCOUNT";
    private static final String TOTAL_PRICE = "TOTAL_PRICE";
    private Map<Integer,Order> orders_history;
    private Map<Integer, Order> waiting_orders;
    private Map<Integer,Order> periodic_orders;
    private ItemOrderDAO itemOrderDAO;
    private static OrderDAO instance = null;

    private OrderDAO() {
        super("ORDERS");
        this.periodic_orders = new HashMap<>();
        this.waiting_orders = new HashMap<>();
        this.orders_history = new HashMap<>();
        this.itemOrderDAO= ItemOrderDAO.getInstance();
    }

    public static OrderDAO getInstance() {
        if (instance == null)
            instance = new OrderDAO();
        return instance;
    }

    public Map<Integer, Order> getOrders_history() {
        return orders_history;
    }

    public Map<Integer, Order> getWaiting_orders() {
        return waiting_orders;
    }

    public Map<Integer, Order> getPeriodic_orders() {
        return periodic_orders;
    }

    @Override
    public boolean Insert(Object orderObj) {
        Order order = (Order) orderObj;
        boolean res = true;
//        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String sql = MessageFormat.format("INSERT INTO {0} ({1}, {2}, {3},{4},{5},{6},{7}) VALUES(?, ?, ?, ?,?,?,?)"
                , _tableName, ORDER_ID, SUPPLIER_ID, ORDER_START_DATE, ORDER_SUPPLY_DATE, ORDER_KIND, DISCOUNT, TOTAL_PRICE);
        try (
                PreparedStatement pstmt = connection.prepareStatement(sql)) {
            //Connection connection=open
            //pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, order.getOrder_id());
            pstmt.setInt(2,order.getSupplier_id());
            pstmt.setString(3, order.getStartDate().toString());
            pstmt.setString(4, order.getSupplyDate().toString());
            pstmt.setInt(5, order.getKind());//0= done, 1= waiting, 2 = period
            pstmt.setDouble(6, order.totalDiscountBytotalAmount());
            pstmt.setDouble(7, order.fullOrderPrice());
            pstmt.executeUpdate();
            int k = order.getKind();
            switch (k){
                case 0 :
                {
                    orders_history.put(order.getOrder_id(), order);
                    break;
                }
                case 1 :
                {
                    waiting_orders.put(order.getOrder_id(), order);
                    break;
                }
                case 2 :
                {
                    periodic_orders.put(order.getOrder_id(), order);
                    break;
                }
            }
            for(Map.Entry<SupplierProduct,Integer> temp : order.getProducts_list_order().entrySet()){
                Pair<SupplierProduct,Integer> product = new Pair<>(temp.getKey(),temp.getValue());
                Pair<Pair,Integer> ans = new Pair<>(product,order.getOrder_id());
                //ItemOrderDAO.getInstance().Insert(ans);
                this.itemOrderDAO.Insert(ans);
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
    public boolean Delete(Object orderObj) {
        Order order = (Order) orderObj;
        boolean res = true;
        String sql = MessageFormat.format("DELETE FROM {0} WHERE {1} = ?"
                , _tableName, ORDER_ID);

        try (
                PreparedStatement pstmt = connection.prepareStatement(sql)) {

            pstmt.setInt(1, order.getOrder_id());
            pstmt.executeUpdate();
            for(Map.Entry<SupplierProduct,Integer> temp : order.getProducts_list_order().entrySet()){
                Pair<String,Integer> ans = new Pair<>(temp.getKey().getProduct_name(), order.getOrder_id());
                this.itemOrderDAO.Delete(ans);
            }
            int k = order.getKind();
            switch (k){
                case 0 :
                {
                    orders_history.remove(order.getOrder_id());
                    break;
                }
                case 1 :
                {
                    waiting_orders.remove(order.getOrder_id());
                    break;
                }
                case 2 :
                {
                    periodic_orders.remove(order.getOrder_id());
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
    public Order convertReaderToObject(ResultSet rs) throws SQLException {
        Map<String, Integer> itemOrderMap = this.itemOrderDAO.getItemsByOrderID(rs.getInt(ORDER_ID));
        Map<SupplierProduct,Integer> order_products = new HashMap<>();
        for(Map.Entry<String, Integer> iter : itemOrderMap.entrySet()){
            int sup_id = rs.getInt(SUPPLIER_ID);
            SupplierProduct sp= SupplierProductDAO.getInstance().getSupplierProductById(iter.getKey(),sup_id);
            order_products.put(sp,iter.getValue());
        }
        LocalDate start= LocalDate.parse(rs.getString(ORDER_START_DATE));
        LocalDate end= LocalDate.parse(rs.getString(ORDER_SUPPLY_DATE));
        Supplier s1 = SupplierDAO.getInstance().getSupplier(rs.getInt(SUPPLIER_ID));
        int kind = Integer.parseInt(rs.getString(ORDER_KIND));

        Order order = new Order(s1,order_products, start, end, rs.getInt(ORDER_ID) ,kind);
        return order;
    }

    ///when we do promote day use it to period orders and for waiting orders
    public boolean UpdateOrderForDone(Object orderObj) {
        Order order = (Order) orderObj;
        boolean res = true;
        String sql = MessageFormat.format("UPDATE {0} SET {1} = ? WHERE {2} = ? "
                , _tableName, ORDER_KIND, ORDER_ID
        );
        try (
                PreparedStatement pstmt = connection.prepareStatement(sql)) {

            orders_history.put(order.getOrder_id(),order);
            int k = order.getKind();
            switch (k) {
                case 1: {
                    waiting_orders.remove(order.getOrder_id());
                    break;
                }
                case 2: {
                    periodic_orders.remove(order.getOrder_id());
                    break;
                }
            }
            order.setKind(0);
            pstmt.setInt(1, 0);
            pstmt.setInt(2,order.getOrder_id());
            pstmt.executeUpdate();


        } catch (SQLException e) {
            System.out.println("Got Exception:");
            System.out.println(e.getMessage());
            System.out.println(sql);
            res = false;
        }
        return res;
    }

//when we want to delete product from period order so calculate the new total price
    public boolean updateAndGetOrderTotalPriceById(int orderId) {
        Order order = getOrderById(orderId);
        double total_price = this.itemOrderDAO.getOrderTotalPriceByIdNOMultiDiscount(orderId);
        double discount = this.getOrderById(orderId).totalDiscountBytotalAmount();
        double final_price = (100-discount)*0.01*total_price;
        boolean res = true;
        String sql = MessageFormat.format("UPDATE {0} SET {1} = ?  WHERE {2} = ? "
                , _tableName, TOTAL_PRICE, ORDER_ID
        );
        try (
                PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setDouble(1, final_price);
            pstmt.executeUpdate();
            order.setTotal_price(final_price);

        } catch (SQLException e) {
            System.out.println("Got Exception:");
            System.out.println(e.getMessage());
            System.out.println(sql);
            res = false;
        }
        return res;
    }

    public Order getOrderById(int orderId) {
        if (orders_history.containsKey(orderId))
        {
            return orders_history.get(orderId);
        }
        if (periodic_orders.containsKey(orderId))
        {
            return periodic_orders.get(orderId);
        }
        if ( waiting_orders.containsKey(orderId))
        {
            return waiting_orders.get(orderId);
        }

        Order order = null;
        try {
            Connection c = DBTables.getInstance().open();
            Statement stmt;
            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM " + DBTables.ORDERS + " WHERE " + ORDER_ID + " = " + orderId + ";");

            if (rs.next()) {
                order = convertReaderToObject(rs);
                int kind= order.getKind();
                if (kind==0)
                {
                    orders_history.put(orderId,order);
                }
                if (kind==1)
                {
                    waiting_orders.put(orderId,order);
                }
                if ( kind==2)
                {
                    periodic_orders.put(orderId,order);
                }
            }
            rs.close();
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return order;
    }

    public Map<Integer,Order> getAllOrdersByKind(int kind) {
        try {
            Connection c = DBTables.getInstance().open();
            Statement stmt;
            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM " + DBTables.ORDERS +" WHERE " + ORDER_KIND + " = " + kind + ";");
            while (rs.next()) {
                Order order = convertReaderToObject(rs);
                if (kind==0 && !orders_history.containsKey(order.getOrder_id()))
                {
                    orders_history.put(order.getOrder_id(),order);
                }
                if (kind==1 && !waiting_orders.containsKey(order.getOrder_id()))
                {
                    waiting_orders.put(order.getOrder_id(),order);
                }
                if ( kind==2 && !periodic_orders.containsKey(order.getOrder_id()))
                {
                    periodic_orders.put(order.getOrder_id(),order);
                }
            }
            rs.close();
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (kind==0)
        {
            return orders_history;
        }
        else if (kind==1)
        {
            return waiting_orders;
        }
        else
        {
           return periodic_orders;
        }
    }

    public List<Order> getAllHistoricOrdersBySupplierID(int sup_id){
        List<Order> orderList = new ArrayList<>();
        try {
            int kind =0;
            Connection c = DBTables.getInstance().open();
            String sql = "SELECT * FROM " + DBTables.ORDERS + " WHERE " + SUPPLIER_ID +  " = ? AND " + ORDER_KIND + " = ?;";

            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, sup_id);
            stmt.setInt(2, kind);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Order order = convertReaderToObject(rs);
                orderList.add(order);
            }
            rs.close();
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return orderList;
    }

    public Map<Integer,Order> getAllOrdersBySupplyDate(LocalDate date) {
        String today = date.toString();
        Map<Integer,Order> today_order = new HashMap<>();
        try {
            Connection c = DBTables.getInstance().open();
            String formattedDate = today.toString();
            String sql = "SELECT * FROM " + DBTables.ORDERS + " WHERE " + ORDER_SUPPLY_DATE + " = ?;";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, formattedDate);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Order order = convertReaderToObject(rs);
                if(order.getKind()==1 && !(waiting_orders.containsKey(order.getOrder_id()))) {
                    waiting_orders.put(order.getOrder_id(), order);
                }
                if(order.getKind()==2 && !(periodic_orders.containsKey(order.getOrder_id()))){
                    periodic_orders.put(order.getOrder_id(),order);
                }
                today_order.put(order.getOrder_id(),order);
            }
            rs.close();
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
            return today_order;
    }

    //return list of all the products names that are on the way right now
    public List<String> onTheWayProducts(){
        Map<Integer,Order> temp = getAllOrdersByKind(1);
        LinkedList<String>  ans = new LinkedList<>();
        for(Order order: temp.values()){
            for(SupplierProduct sp: order.getProducts_list_order().keySet()){
                if(!ans.contains(sp.getProduct_name())){
                    ans.add(sp.getProduct_name());
                }
            }
        }
        return ans;
    }

    public boolean isProductInPeriodOrder(String name, int sup_id){
        for(Order order : this.getAllOrdersByKind(2).values()){
            if(order.getSupplier().getCard().getSupplier_number()==sup_id){
                for(SupplierProduct sp: order.getProducts_list_order().keySet()){
                    if(sp.getProduct_name().equals(name)){
                        return true;
                    }
                }
            }
        }
        return false;
    }


    public int getMaxID() {
        int MaxId = 0;
        try {
            Connection c = DBTables.getInstance().open();
            Statement stmt;
            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM " + DBTables.ORDERS + ";");
            while (rs.next()) {
                int cId = rs.getInt(ORDER_ID);
                if (cId > MaxId) {
                    MaxId = cId;
                }
            }
            rs.close();
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return MaxId;
    }

    public Map<SupplierProduct, Integer> getProductsByOrderID(int orderId)
    {
        Map<String, Integer> itemOrderMap = this.itemOrderDAO.getItemsByOrderID(orderId);
        Map<SupplierProduct,Integer> order_products = new HashMap<>();
        for(Map.Entry<String, Integer> iter : itemOrderMap.entrySet()){
            int sup_id = this.getOrderById(orderId).getSupplier_id();
            SupplierProduct sp= SupplierProductDAO.getInstance().getSupplierProductById(iter.getKey(),sup_id);
            order_products.put(sp,iter.getValue());
        }
       return  order_products;
    }





}