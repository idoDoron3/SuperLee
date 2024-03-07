package Supplier_Module.DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class DBTables {
//    private static final String DB_URL = "jdbc:sqlite:dev/Resource/MarketDB.db";
    private static final String DB_URL = "jdbc:sqlite:MarketDB.db";
    public final static String SUPPLIERS = "SUPPLIERS";
        public final static String ITEM_ORDERS = "ITEM_ORDERS";
        public final static String CONTACTS = "CONTACTS";
        public final static String ORDERS = "ORDERS";
        public final static String SUPPLIER_PRODUCT = "SUPPLIER_PRODUCTS";

        private static DBTables dbtables = null;

        public static Connection c = null;

        private DBTables() {

        }

        public static DBTables getInstance() {
            if (dbtables == null) {
                return createDBTables();
            }
            return dbtables;
        }

        private static DBTables createDBTables() {
            dbtables = new DBTables();
            try {
                dbtables.open();
                dbtables.init();
            } catch (Exception e) {
            }
            return dbtables;
        }

        public Connection open() throws Exception {
            if (c != null && !c.isClosed())
                return c;
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection(DB_URL);
            return c;
        }

        public void close() throws Exception {
            if (c != null)
                c.close();
        }

        public void init() throws Exception {
            try {
                createSupplierTable();
            } catch (Exception e) {
            }
            try {
                createItemOrderTable();
            } catch (Exception e) {
            }
            try {
                createContactsTable();
            } catch (Exception e) {
            }

            try {
                createOrderTable();
            } catch (Exception e) {
            }
            try {
                createSupplierProductTable();
            } catch (Exception e) {
            }
        }

        private void createOrderTable() throws Exception {
            Statement stmt = null;
            stmt = c.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS ORDERS " +
                    "(ORDER_ID INT NOT NULL," +
                    " SUPPLIER_ID INT NOT NULL," +
                    " ORDER_START_DATE VARCHAR(30) NOT NULL, " +
                    " ORDER_SUPPLY_DATE VARCHAR(30) NOT NULL," +
                    " ORDER_KIND INT NOT NULL, " +
                    " DISCOUNT DOUBLE NOT NULL, " +
                    " TOTAL_PRICE DOUBLE NOT NULL, " +
                    " PRIMARY KEY (ORDER_ID) )";
            stmt.executeUpdate(sql);
            c.commit();
            stmt.close();
        }

        private void createSupplierProductTable() throws Exception {
            Statement stmt = null;
            stmt = c.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS SUPPLIER_PRODUCTS " +
                    "(PRODUCT_ID INT  NOT NULL," +
                    " SUPPLIER_ID INT   NOT NULL, " +
                    " P_NAME VARCHAR(30) NOT NULL, " +
                    " AMOUNT INT NOT NULL, " +
                    " WEIGHT DOUBLE NOT NULL, " +
                    " PRICE INT NOT NULL, " +
                    " DISCOUNT_BY_AMOUNT VARCHAR(50) NOT NULL," +
                    " PRIMARY KEY (P_NAME, SUPPLIER_ID) )";
            stmt.executeUpdate(sql);
            c.commit();
            stmt.close();
        }

        private void createSupplierTable() throws Exception {
            Statement stmt = null;
            stmt = c.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS SUPPLIERS " +
                    "(ID INT   NOT NULL," +
                    " SUP_NAME VARCHAR(30)    NOT NULL, " +
                    " ADDRESS VARCHAR(30)    NOT NULL, " +
                    " PAYMENT_METHOD VARCHAR(30)    NOT NULL, " +
                    " BANK_ACCOUNT VARCHAR(30)    NOT NULL, " +
                    " SUPPLY_METHOD VARCHAR(30)  NOT NULL , " +
                    " DELIVERY_DAYS VARCHAR(30)," +
                    " DISCOUNT_BY_AMOUNT VARCHAR(50) NOT NULL,"+
                    " CATEGORIES VARCHAR(30) NOT NULL,"+
                    " EOM VARCHAR(30) NOT NULL , "+
                    " PRIMARY KEY (ID)) ";
            stmt.executeUpdate(sql);
            c.commit();
            stmt.close();
        }

        private void createItemOrderTable() throws Exception {
            Statement stmt = null;
            stmt = c.createStatement();
            String sql =
                    "CREATE TABLE IF NOT EXISTS ITEM_ORDERS " +
                            "(PRODUCT_NAME VARCHAR(30) NOT NULL, " +
                            " ORDER_ID INT  NOT NULL, " +
                            " PRODUCT_ID INT  NOT NULL, " +
                            " UNIT_PRICE DOUBLE NOT NULL, " +
                            " AMOUNT INT NOT NULL, " +
                            " DISCOUNT INT NOT NULL, " +
                            " TOTAL_PRICE DOUBLE NOT NULL, " +
                            " PRIMARY KEY (PRODUCT_NAME, ORDER_ID) )";
            stmt.executeUpdate(sql);
            c.commit();
            stmt.close();
        }

        private void createContactsTable() throws Exception {
            Statement stmt = null;
            stmt = c.createStatement();
            String sql =
                    "CREATE TABLE IF NOT EXISTS CONTACTS " +
                            "(SUPPLIER_ID INT  NOT NULL, " +
                            " C_NAME VARCHAR(30) NOT NULL, " +
                            " EMAIL VARCHAR(30) NOT NULL, " +
                            " PHONE_NUMBER VARCHAR(30) NOT NULL, " +
                            " FOREIGN KEY (SUPPLIER_ID) REFERENCES SUPPLIER(ID)," +
                            " PRIMARY KEY (PHONE_NUMBER) )";
            stmt.executeUpdate(sql);
            c.commit();
            stmt.close();
        }
}
