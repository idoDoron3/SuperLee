package Stock.DataAccess;

import DBConnect.Connect;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.*;
import java.util.*;

public class ExpDateDAO {
    private static ExpDateDAO instance = null;
    private  static Map<Integer, Date> ExpDateMap;
    private static  Map<String, HashMap<Integer,Date>> catalogExpDate;
    private static  Map<Integer,String> qrToCatalogNumber;
    private static Connection connection;

    private ExpDateDAO(){
        connection = Connect.getConnection();
        ExpDateMap = new HashMap<>();
        qrToCatalogNumber = new HashMap<>();
        catalogExpDate = new HashMap<>();
    }

    public static ExpDateDAO getInstance() {
        if (instance == null) {
            instance = new ExpDateDAO();
        }
        return instance;
    }

    public Date getQrExpDate(int qr){

        if (ExpDateMap.get(qr) == null) {
            // read from the db
            lookForExpDate(qr);
        }
        return ExpDateMap.get(qr);
    }

    private void lookForExpDate(int qr){
        int barcode;
        String catalogNumber;
        Date expDate;

        try{
            java.sql.Statement statement = connection.createStatement();
            java.sql.ResultSet resultSet = statement.executeQuery("SELECT * FROM ExpDates WHERE QRCode ==" + qr);
            while(resultSet.next()){
                barcode = resultSet.getInt("QRCode");
                catalogNumber = resultSet.getString("catalog_number");
                expDate = resultSet.getDate("Date");
                ExpDateMap.put(barcode,expDate);
                qrToCatalogNumber.put(barcode,catalogNumber);

            }

        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    public HashMap<Integer,Date> readExpForCatalogNumber(String catalogNumber){
        if(catalogExpDate.get(catalogNumber) == null){
            getExpForCatalogNumber(catalogNumber);
        }
        return catalogExpDate.get(catalogNumber);
    }

    private void getExpForCatalogNumber(String catalogNumber){
        HashMap<Integer,Date> productExpDates = new HashMap<>();
        int barcode;
        Date expDate;
        try{
            java.sql.Statement statement = connection.createStatement();
//            java.sql.ResultSet resultSet = statement.executeQuery("SELECT * FROM ExpDates WHERE catalog_number ==" + catalogNumber);
            String sql = "SELECT * FROM ExpDates WHERE catalog_number = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, catalogNumber);
            java.sql.ResultSet resultSet = preparedStatement.executeQuery();

            while(resultSet.next()){
                barcode = resultSet.getInt("QRCode");
                expDate = resultSet.getDate("Date");
                productExpDates.put(barcode,expDate);
                ExpDateMap.putIfAbsent(barcode, expDate);
                qrToCatalogNumber.putIfAbsent(barcode,catalogNumber);
            }
            catalogExpDate.put(catalogNumber,productExpDates);
        }
        catch (SQLException e){
            System.out.println(e.getMessage());
        }

    }


    public void writeExpDates(){
        for(Integer qr: ExpDateMap.keySet()){
            try{
                java.sql.Statement statement = connection.createStatement();
                java.sql.ResultSet resultSet = statement.executeQuery("SELECT * FROM ExpDates WHERE QRCode ==" + qr);
                if(!resultSet.next()){
//                    statement.executeUpdate("INSERT INTO ExpDates (QRCode, catalog_number, Date) VALUES (" + qr  + ","+"'" + qrToCatalogNumber.get(qr) +"'"+","+ExpDateMap.get(qr)+ ")");
                    PreparedStatement pstmt = connection.prepareStatement("INSERT INTO ExpDates (QRCode, catalog_number, Date) VALUES (?, ?, ?)");
                    pstmt.setInt(1, qr);
                    pstmt.setString(2, qrToCatalogNumber.get(qr));
                    pstmt.setDate(3, new java.sql.Date(ExpDateMap.get(qr).getTime()));
                    pstmt.executeUpdate();
                }
                else{
                    String updateExpDatesQuery = "UPDATE ExpDates SET Date = ?, catalog_number = ? WHERE QRCode = ?";
                    PreparedStatement updateExpDatesStmt = connection.prepareStatement(updateExpDatesQuery);
                    updateExpDatesStmt.setDate(1, new java.sql.Date(ExpDateMap.get(qr).getTime()));
                    updateExpDatesStmt.setString(2, qrToCatalogNumber.get(qr));
                    updateExpDatesStmt.setInt(3, qr);
                    updateExpDatesStmt.executeUpdate();
                }
            }catch (SQLException e){
                System.out.println(e.getMessage());
            }
        }
    }

    public void writeExpDateForQR(int QR,String catalogNumber, Date date){
        if(!ExpDateMap.containsKey(QR)){
            ExpDateMap.put(QR,date);
            qrToCatalogNumber.put(QR,catalogNumber);
            // should be added to the database ?
        }
       // else{System.out.println("the qr is not new");}
    }
    public void updateExpDate(String catalogNumber, HashMap<Integer,Date> updatedExpDates){

        for(Integer qr: updatedExpDates.keySet()){
            ExpDateMap.putIfAbsent(qr,updatedExpDates.get(qr));
            qrToCatalogNumber.putIfAbsent(qr,catalogNumber);
        }
        writeExpDates();
    }


    // func bfor sale
       public void updateExpDateAfterSale(String catalogNumber, HashMap<Integer,Date> updatedExpDates){

        try{
            java.sql.Statement statement = connection.createStatement();
            statement.executeUpdate("DELETE FROM ExpDates WHERE catalog_number = '" + catalogNumber + "'");

        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        for(Integer qr: updatedExpDates.keySet()){
            writeExpDateForQR(qr,catalogNumber,updatedExpDates.get(qr));
        }
    }

    public void deleteExpDate(int qr){
        ExpDateMap.remove(qr);
        if(catalogExpDate.containsKey(qrToCatalogNumber.get(qr))) {
            catalogExpDate.get(qrToCatalogNumber.get(qr)).remove(qr);
        }
        qrToCatalogNumber.remove(qr);
        try{
            PreparedStatement statement = connection.prepareStatement("DELETE FROM ExpDates WHERE QRCode = ?");
            statement.setInt(1, qr);
            statement.executeUpdate();
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        writeExpDates();

    }
    public Boolean isQRfromCatalogNumber(String catalogNumber, int qr){
        lookForExpDate(qr);
        return Objects.equals(qrToCatalogNumber.get(qr), catalogNumber);
    }

    private void loadAllDataToCache(){
        int barcode;
        String catalogNumber;
        Date expDate;

        try{
            java.sql.Statement statement = connection.createStatement();
            java.sql.ResultSet resultSet = statement.executeQuery("SELECT * FROM ExpDates");
            while(resultSet.next()){
                barcode = resultSet.getInt("QRCode");
                catalogNumber = resultSet.getString("catalog_number");
                expDate = resultSet.getDate("Date");
                ExpDateMap.putIfAbsent(barcode,expDate);
                qrToCatalogNumber.putIfAbsent(barcode,catalogNumber);

            }

        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    public HashMap<String, ArrayList<Integer>> expirationForDate(LocalDate futureDate){
        loadAllDataToCache();
        HashMap<String, ArrayList<Integer>> map = new HashMap<>();
        for(Integer qr: ExpDateMap.keySet()){
            Date date = Date.from(futureDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
//            int compareator = ExpDateMap.get(qr).compareTo(date);
            if(ExpDateMap.get(qr).compareTo(date) <= 0) {
                String catalogNumber = qrToCatalogNumber.get(qr);
                if (map.containsKey(catalogNumber)) {
                    map.get(catalogNumber).add(qr);
                } else {
                    map.put(catalogNumber, new ArrayList<>());
                    map.get(catalogNumber).add(qr);
                }
                DamagedProductDAO.getInstance().writeDamagedProduct(qr, catalogNumber,"Expired in " + futureDate.toString());
            }

        }
        return map;
    }


}