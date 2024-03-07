package Stock.DataAccess;

import DBConnect.Connect;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class ShortageDAO {
    private static Connection connection;
    private static ArrayList<String> shortageMap;
    private static ShortageDAO instance = null;

    private ShortageDAO(){
        connection = Connect.getConnection();
        shortageMap = new ArrayList<>();
        loadToShortageMap();
    }

    public static ShortageDAO getInstance() {
        if (instance == null) {
            instance = new ShortageDAO();
        }
        return instance;
    }

    public void loadToShortageMap(){
        try{
            java.sql.Statement statement = connection.createStatement();
            java.sql.ResultSet resultSet = statement.executeQuery("SELECT * FROM Shortages ");
            while(resultSet.next()){
                String catalogNumber = resultSet.getString("CatalogNumber");
                shortageMap.add(catalogNumber);
            }
        }
        catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    public void deleteFromShortages(String catalogNumber){
        while(shortageMap.contains(catalogNumber)) {
            shortageMap.remove(catalogNumber);
        }
    }

    public void resetShortages(){
        shortageMap.clear();
    }

//    public void writeShortages(){
//        try{
//            java.sql.Statement statement = connection.createStatement();
//            java.sql.ResultSet resultSet = statement.executeQuery("DELETE FROM Shortages");
//            for(String catalogNumber: shortageMap){
//                PreparedStatement stmt = connection.prepareStatement("INSERT INTO Shortages (CatalogNumber) VALUES (?)");
//                stmt.setString(1, catalogNumber);
//                stmt.executeUpdate();
//            }
//        }
//        catch (SQLException e){
//            System.out.println("theres a problem with the database");
//        }
//    }


    public void writeShortages() {
        try (PreparedStatement deleteStatement = connection.prepareStatement("DELETE FROM Shortages");
             PreparedStatement insertStatement = connection.prepareStatement("INSERT INTO Shortages (CatalogNumber) VALUES (?)")) {
            deleteStatement.executeUpdate();
            Set<String> uniqe = new HashSet<>(shortageMap);
            for (String catalogNumber : uniqe) {
                insertStatement.setString(1, catalogNumber);
                insertStatement.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public ArrayList<String> getShortageMap(){
        return shortageMap;
    }

    public void addToShortages(String catalogNumber){
        shortageMap.add(catalogNumber);
    }

    public Boolean isInShortage(String catalogNumber){
        return shortageMap.contains(catalogNumber);
    }


}


