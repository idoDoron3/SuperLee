package Stock.DataAccess;

import DBConnect.Connect;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

public class DamagedProductDAO {
    private static DamagedProductDAO instance = null;
    private static Map<Integer,String> damagedMap;
    private static  Map<Integer,String> qrToCatalogNumber;
    private static Map<String, HashMap<Integer,String>> catalogDamagedMap;
    private static Connection connection;

    private DamagedProductDAO(){
        damagedMap = new HashMap<>();
        qrToCatalogNumber = new HashMap<>();
        catalogDamagedMap = new HashMap<>();
        connection = Connect.getConnection();

    }

    public static DamagedProductDAO getInstance() {
        if (instance == null) {
            instance = new DamagedProductDAO();
        }
        return instance;
    }

    public String getQrDamagedReason(int qr){
        if(damagedMap.get(qr) == null){
            //read from database
            lookForDamagedProduct(qr);
        }
        return damagedMap.get(qr);
    }

    private void lookForDamagedProduct(int qr){
        int barcode;
        String catalogNumber, reason;
        try{
            java.sql.Statement statement = connection.createStatement();
            java.sql.ResultSet resultSet = statement.executeQuery("SELECT * FROM Damaged WHERE QRCode ==" + qr);
            while (resultSet.next()){
                barcode = resultSet.getInt("QRCode");
                catalogNumber = resultSet.getString("catalog_number");
                reason = resultSet.getString("reason");
                damagedMap.put(barcode,reason);
                qrToCatalogNumber.put(barcode,catalogNumber);
            }

        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    public HashMap<Integer,String> readDamagedForCatalogNumber(String catalogNumber){
        if(catalogDamagedMap.get(catalogNumber) == null){
            //read from database
            getDamagedForCatalogNumber(catalogNumber);
        }
        return catalogDamagedMap.get(catalogNumber);
    }

    private void getDamagedForCatalogNumber(String catalogNumber){
        int barcode;
        String reason;
        HashMap<Integer,String> catalogDamaged = new HashMap<>();
        try{
            java.sql.Statement statement = connection.createStatement();
            java.sql.ResultSet resultSet = statement.executeQuery("SELECT * FROM Damaged WHERE catalog_number ==" + "'" + catalogNumber+ "'");
            while(resultSet.next()){
                barcode = resultSet.getInt("QRCode");
                reason = resultSet.getString("reason");
                catalogDamaged.put(barcode,reason);
                damagedMap.putIfAbsent(barcode,reason);
                qrToCatalogNumber.putIfAbsent(barcode,catalogNumber);
            }
            catalogDamagedMap.put(catalogNumber,catalogDamaged);
        }catch (SQLException e)
        {
            System.out.println(e.getMessage());
        }
    }
    public void writeDamagedProducts(){
        for(Integer qr: damagedMap.keySet()) {
            String[] words = damagedMap.get(qr).split(" ");
            try {
                    java.sql.Statement statement = connection.createStatement();
                    java.sql.ResultSet resultSet = statement.executeQuery("SELECT * FROM Damaged WHERE QRCode ==" + qr);
                    if (!resultSet.next()) {
//                    statement.executeUpdate("INSERT INTO Damaged (QRCode, catalog_number, Date) VALUES (" + qr  + ","+"'" + qrToCatalogNumber.get(qr) +"'"+","+"'" + damagedMap.get(qr) +"'" +")");
                        PreparedStatement insertDamaged = connection.prepareStatement("INSERT INTO Damaged (QRCode, catalog_number, Reason) VALUES (?, ?, ?)");
                        insertDamaged.setInt(1, qr);
                        insertDamaged.setString(2, qrToCatalogNumber.get(qr));
                        insertDamaged.setString(3, damagedMap.get(qr));


                        insertDamaged.executeUpdate();

                    } else {
//                    statement.executeUpdate("UPDATE ExpDates SET Date ="  + damagedMap.get(qr)+ " WHERE QRCode = "+ Integer.toString(qr));
//                    statement.executeUpdate("UPDATE ExpDates SET catalog_number ="  + qrToCatalogNumber.get(qr)+ " WHERE QRCode = "+ Integer.toString(qr));
                        PreparedStatement updateDamaged = connection.prepareStatement("UPDATE Damaged SET Reason = ?, catalog_number = ? WHERE QRCode = ?");
                        updateDamaged.setString(1, damagedMap.get(qr));
                        updateDamaged.setString(2, qrToCatalogNumber.get(qr));
                        updateDamaged.setInt(3, qr);

                        updateDamaged.executeUpdate();

                    }
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }

            }
        }


    public void writeDamagedProduct(int QR,String catalogNumber, String reason){
        if(!damagedMap.containsKey(QR)){
            damagedMap.put(QR,reason);
            qrToCatalogNumber.put(QR,catalogNumber);
            // should be added to the database ?
        }
//        else{System.out.println("this qr is already a damaged product");}
    }

    public void deleteDamaged(int qr){
        damagedMap.remove(qr);
        catalogDamagedMap.get(qrToCatalogNumber.get(qr)).remove(qr);
        qrToCatalogNumber.remove(qr);
        try{
            java.sql.Statement statement = connection.createStatement();
            java.sql.ResultSet resultSet = statement.executeQuery("DELETE  FROM Damaged WHERE QRCode ==" + Integer.toString(qr));

        }catch (SQLException e){
            System.out.println(e.getMessage());
        }

    }



}
