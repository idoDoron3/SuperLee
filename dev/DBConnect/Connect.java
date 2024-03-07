package DBConnect;
import Stock.Business.AProductCategory;
import Stock.Business.AProductSubCategory;
import Stock.Business.Location;
import Stock.Business.Product;
import Stock.DataAccess.CategoryDAO;
import Stock.DataAccess.ProductDAO;
import Supplier_Module.Business.Managers.Order_Manager;
import Supplier_Module.Business.Managers.SupplyManager;
import Supplier_Module.Presentation.Data;
import Supplier_Module.Presentation.UI;

import java.util.Calendar;
import java.util.Date;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.*;
import java.util.Arrays;


public class Connect {
    private static Connection connection ;

    private Connect(){
        connect();

    }
    public static void connect(){
        try {
//            String url = "jdbc:sqlite:dev/Resource/MarketDB.db";  // change for jar

            String url = "jdbc:sqlite:MarketDB.db";  // change for jar
            connection = DriverManager.getConnection(url);
        }
        catch (SQLException e){
            System.out.println(e.getMessage());
            System.out.println(Arrays.toString(e.getStackTrace()));
        }
    }
    public static void disconnect(){
        try{
            connection.close();
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    public static Connection getConnection(){
        if(connection == null){
            connect();
        }
        return connection;
    }


    public static void loadSupplier(){
        Order_Manager.getOrder_Manager().deleteAllData();
        SupplyManager.getSupply_manager().deleteAllData();
        UI.getUser();
        Data.getLoadData().loadData();
    }
    public static void emptyData(){
        try {
//            Connect.disconnect();
            Connection conn = getConnection();
            Statement stmt = conn.createStatement();
            stmt.executeUpdate("DELETE  FROM ProductID");
            stmt.executeUpdate("DELETE FROM Products");
            stmt.executeUpdate("DELETE  FROM ExpDates");
            stmt.executeUpdate("DELETE  FROM Damaged");
            stmt.executeUpdate("DELETE FROM Shortages");
            stmt.executeUpdate("DELETE FROM Category");
            stmt.executeUpdate("INSERT INTO ProductID (ID, VALUE ) VALUES (" + "1" + "," + 0 + ")");
            stmt.executeUpdate("INSERT INTO ProductID (ID, VALUE ) VALUES (" + "2" + "," + 0 + ")");
            stmt.executeUpdate("INSERT INTO ProductID (ID, VALUE ) VALUES (" + "3" + "," + 0 + ")");
            stmt.executeUpdate("INSERT INTO ProductID (ID, VALUE ) VALUES (" + "4" + "," + 0 + ")");
            stmt.executeUpdate("INSERT INTO ProductID (ID, VALUE ) VALUES (" + "5" + "," + 0 + ")");
            stmt.executeUpdate("INSERT INTO ProductID (ID, VALUE ) VALUES (" + "6" + "," + 0 + ")");
            stmt.executeUpdate("INSERT INTO ProductID (ID, VALUE ) VALUES (" + "7" + "," + 0 + ")");

        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
//
    }


    public static void loadDataToDatabase(){
        try {
            loadSupplier();
//            Connect.disconnect();
            String createTableQuery = "CREATE TABLE SupplierManagerUsers ("
                    + "username VARCHAR(255) PRIMARY KEY NOT NULL,"
                    + "password VARCHAR(255) NOT NULL)";

            String createTableQuery1 = "CREATE TABLE StockManagerUsers ("
                    + "username VARCHAR(255) PRIMARY KEY NOT NULL,"
                    + "password VARCHAR(255) NOT NULL)";

            Connection conn = getConnection();
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(createTableQuery);
            stmt.executeUpdate(createTableQuery1);

//            String createTableQuery = "CREATE TABLE SupplierManagerUsers ("
//                    + "username VARCHAR(255) PRIMARY KEY NOT NULL,"
//                    + "password VARCHAR(255) NOT NULL)";
//            stmt.executeUpdate(createTableQuery);
//
//            createTableQuery = "CREATE TABLE StockManagerUsers ("
//                    + "username VARCHAR(255) PRIMARY KEY NOT NULL,"
//                    + "password VARCHAR(255) NOT NULL)";
//            stmt.executeUpdate(createTableQuery);


//            stmt.executeUpdate(createTableQuery);
            stmt.executeUpdate("DELETE  FROM ProductID");
            stmt.executeUpdate("DELETE FROM Products");
            stmt.executeUpdate("DELETE  FROM ExpDates");
            stmt.executeUpdate("DELETE  FROM Damaged");
            stmt.executeUpdate("DELETE FROM Shortages");
            stmt.executeUpdate("DELETE FROM Category");

            stmt.executeUpdate("DELETE FROM SupplierManagerUsers");
            stmt.executeUpdate("DELETE FROM StockManagerUsers");


            stmt.executeUpdate("INSERT INTO ProductID (ID, VALUE ) VALUES (" + "1" + "," + 0 + ")");
            stmt.executeUpdate("INSERT INTO ProductID (ID, VALUE ) VALUES (" + "2" + "," + 0 + ")");
            stmt.executeUpdate("INSERT INTO ProductID (ID, VALUE ) VALUES (" + "3" + "," + 0 + ")");
            stmt.executeUpdate("INSERT INTO ProductID (ID, VALUE ) VALUES (" + "4" + "," + 0 + ")");
            stmt.executeUpdate("INSERT INTO ProductID (ID, VALUE ) VALUES (" + "5" + "," + 0 + ")");
            stmt.executeUpdate("INSERT INTO ProductID (ID, VALUE ) VALUES (" + "6" + "," + 0 + ")");
            stmt.executeUpdate("INSERT INTO ProductID (ID, VALUE ) VALUES (" + "7" + "," + 0 + ")");

            AProductCategory Dairy = new AProductCategory("Dairy");
            AProductCategory Meat = new AProductCategory("Meat");
            AProductCategory Fruits = new AProductCategory("Fruits");
            AProductCategory vegetables = new AProductCategory("Vegetables");
            CategoryDAO.getInstance().writeNewCategory(Dairy.getName(),0);
            CategoryDAO.getInstance().writeNewCategory(Meat.getName(),0);
            CategoryDAO.getInstance().writeNewCategory(Fruits.getName(),0);
            CategoryDAO.getInstance().writeNewCategory(vegetables.getName(),0);


            AProductCategory milk = new AProductCategory("Milk 3%");
            AProductCategory Burgers = new AProductCategory("Burgers");
            AProductCategory Apples = new AProductCategory("Apples");
            AProductCategory Yogurt = new AProductCategory("Yogurt");
            AProductCategory Oranges = new AProductCategory("Oranges");
            AProductCategory Beef = new AProductCategory("Beef");
            AProductCategory Cheese = new AProductCategory("Cheese");
            AProductCategory Tomatos = new AProductCategory("Tomatoes");
            AProductCategory Steaks = new AProductCategory("Steaks");


            AProductSubCategory ssc_1l = new AProductSubCategory(1,"l");
            AProductSubCategory ssc_1kg = new AProductSubCategory(1,"kg");
            AProductSubCategory ssc_500gr = new AProductSubCategory(500,"gr");
            AProductSubCategory ssc_250ml = new AProductSubCategory(250,"ml");
            AProductSubCategory ssc_200gr = new AProductSubCategory(200,"gr");
            AProductSubCategory ssc_2kg = new AProductSubCategory(2,"kg");


            Calendar calendar = Calendar.getInstance();
            calendar.set(2023, Calendar.MAY, 23);
            Date expDate1 =  calendar.getTime();

            calendar.set(2023, Calendar.JULY,10);
            Date expDate2 = calendar.getTime();

            calendar.set(2023, Calendar.JUNE,12);
            Date expDate3 = calendar.getTime();

            calendar.set(2023, Calendar.SEPTEMBER,15);
            Date expDate4 = calendar.getTime();

            calendar.set(2023, Calendar.OCTOBER,18);
            Date expDate5 = calendar.getTime();

            calendar.set(2023, Calendar.AUGUST,30);
            Date expDate6 = calendar.getTime();

            calendar.set(2023, Calendar.JULY,25);;
            Date expDate7 = calendar.getTime();

            calendar.set(2024, Calendar.JANUARY,17);
            Date expDate8 = calendar.getTime();

            calendar.set(2024, Calendar.MARCH,3);
            Date expDate9 = calendar.getTime();




            Product product1 = new Product(Dairy,milk,ssc_1l,new Location(0,0),new Location(0,0), "Yotvata",7,5,1.1,expDate1);
            Product product2 = new Product(Meat,Burgers,ssc_1kg,new Location(0,1),new Location(0,1), "BBB",2,1,1.0,expDate2);
            Product product3 = new Product(Fruits,Apples,ssc_500gr,new Location(0,2),new Location(0,2), "Sade",5,4,0.5,expDate3);
            Product product4 = new Product(Dairy,Yogurt,ssc_250ml,new Location(0,3),new Location(0,3), "Tnuva",8,8,0.25,expDate4);
            Product product5 = new Product(Fruits,Oranges,ssc_1kg,new Location(0,4),new Location(0,4), "Sade",12,10,1.0,expDate5);
            Product product6 = new Product(Meat,Beef,ssc_500gr,new Location(0,5),new Location(0,5), "Tivol",9,4,0.5,expDate6);
            Product product7 = new Product(Dairy,Cheese,ssc_200gr,new Location(0,6),new Location(0,6), "Yotvata",12,9,0.2,expDate7);
            Product product8 = new Product(vegetables,Tomatos,ssc_1kg,new Location(0,7),new Location(0,7), "Sade",11,10,1.0,expDate8);
            Product product9 = new Product(Meat,Steaks,ssc_2kg,new Location(0,8),new Location(0,8), "BBB",17,4,2.0,expDate9);

                        // curr product 406
            Product[] p = {product1,product2,product3,product4,product5,product6,product7,product8,product9};
            for(Product product: p){
                product.setCatalogNumber();
            }
            ProductDAO.getInstance().addNewProductToProducts(product1);
            ProductDAO.getInstance().addNewProductToProducts(product2);
            ProductDAO.getInstance().addNewProductToProducts(product3);
            ProductDAO.getInstance().addNewProductToProducts(product4);
            ProductDAO.getInstance().addNewProductToProducts(product5);
            ProductDAO.getInstance().addNewProductToProducts(product6);
            ProductDAO.getInstance().addNewProductToProducts(product7);
            ProductDAO.getInstance().addNewProductToProducts(product8);
            ProductDAO.getInstance().addNewProductToProducts(product9);
            ProductDAO.getInstance().writeProducts();
//            stmt.executeUpdate("INSERT INTO ProductID (ID, VALUE ) VALUES (" + "3" + "," + 0 + ")");
            stmt.executeUpdate("UPDATE ProductID SET VALUE = 18 WHERE ID = 3");
//
//            String createTableQuery = "CREATE TABLE SupplierManagerUsers ("
//                    + "username VARCHAR(255) PRIMARY KEY NOT NULL,"
//                    + "password VARCHAR(255) NOT NULL)";
//
//            stmt.executeUpdate(createTableQuery);





        }catch (SQLException e){
            System.out.println(e.getMessage());
        }

//
    }
    public static void main(String[] args) {
        loadDataToDatabase();

    }
}


