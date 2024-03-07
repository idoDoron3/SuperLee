package Stock.DataAccess;

import DBConnect.Connect;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ProductDetailsDAO {
    // the table of the details :
    //  ID        ,Value
    //  1           ProductIDCounter
    //  2           StorageLocation: ShelfNumber
    //  3           StorageLocation: IndexInShelf
    //  4           StoreLocation: ShelfNumber
    //  5           numberOfMarketsInChain
    //  6           ManagedMarket
    //  7           number of shelves

    private static ProductDetailsDAO instance = null;
    private static int curr;
    private static int storageShelfNumber;
    private static int IndexInShelf;
    private static int storeShelfNumber;
    private static int numberOfMarketsInChain;
    private static int managedMarket;
    private static int numOfShelves;
    private static Connection connection;

    private ProductDetailsDAO(){
        connection = Connect.getConnection();

        try{
            java.sql.Statement statement = connection.createStatement();
            java.sql.ResultSet resultSet = statement.executeQuery("SELECT * FROM ProductID WHERE ID =="  + "1" );
            if(resultSet.next()){
                curr = resultSet.getInt("Value");
            }
            resultSet = statement.executeQuery("SELECT * FROM ProductID WHERE ID =="  + "2" );
            if(resultSet.next()){
                storageShelfNumber = resultSet.getInt("Value");
            }
            resultSet = statement.executeQuery("SELECT * FROM ProductID WHERE ID =="  + "3" );
            if(resultSet.next()){
                IndexInShelf = resultSet.getInt("Value");
            }
            resultSet = statement.executeQuery("SELECT * FROM ProductID WHERE ID =="  + "4" );
            if(resultSet.next()){
                storeShelfNumber = resultSet.getInt("Value");
            }
            resultSet = statement.executeQuery("SELECT * FROM ProductID WHERE ID =="  + "5" );
            if(resultSet.next()){
                numberOfMarketsInChain = resultSet.getInt("Value");
            }
            resultSet = statement.executeQuery("SELECT * FROM ProductID WHERE ID =="  + "6" );
            if(resultSet.next()){
                managedMarket = resultSet.getInt("Value");
            }
            resultSet = statement.executeQuery("SELECT * FROM ProductID WHERE ID =="  + "7" );
            if(resultSet.next()){
                numOfShelves = resultSet.getInt("Value");
            }

        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    public static ProductDetailsDAO getInstance() {
        if (instance == null) {
            instance = new ProductDetailsDAO();
        }
        return instance;
    }

    public static void setNumberOfMarketsInChain(int numberOfMarketsInChain) {
        ProductDetailsDAO.numberOfMarketsInChain = numberOfMarketsInChain;
    }

    public static void setManagedMarket(int managedMarket) {
        ProductDetailsDAO.managedMarket = managedMarket;
    }

    public static void setNumOfShelves(int numOfShelves) {
        ProductDetailsDAO.numOfShelves = numOfShelves;
    }

    public static int getNumberOfMarketsInChain() {
        return numberOfMarketsInChain;
    }

    public static int getManagedMarket() {
        return managedMarket;
    }

    public static int getNumOfShelves() {
        return numOfShelves;
    }

    //    public static void saveDetails(){
//        try{
//            java.sql.Statement statement = connection.createStatement();
//            java.sql.ResultSet resultSet = statement.executeQuery("SELECT * FROM ProductID WHERE ID =="  + "1" );
//            if(!resultSet.next()){
//                statement.executeUpdate("INSERT INTO ProductID (ID, VALUES ) VALUES (" + "1"  + "," + Integer.toString(curr) + ")");
//            }
//            else{
//                statement.executeUpdate("UPDATE ProductID SET Value ="  + Integer.toString(curr) + " WHERE ID = "+ "1");
//            }
//            resultSet = statement.executeQuery("SELECT * FROM ProductID WHERE ID =="  + "2" );
//            if(!resultSet.next()){
//                statement.executeUpdate("INSERT INTO ProductID (ID, VALUES ) VALUES (" + "2"  + "," + Integer.toString(storageShelfNumber) + ")");
//            }
//            else{
//                statement.executeUpdate("UPDATE ProductID SET Value ="  + Integer.toString(storageShelfNumber) + " WHERE ID = "+ "2");
//            }
//            resultSet = statement.executeQuery("SELECT * FROM ProductID WHERE ID =="  + "3" );
//            if(!resultSet.next()){
//                statement.executeUpdate("INSERT INTO ProductID (ID, VALUES ) VALUES (" + "3"  + "," + Integer.toString(storageIndexInShelf) + ")");
//            }
//            else{
//                statement.executeUpdate("UPDATE ProductID SET Value ="  + Integer.toString(storageIndexInShelf) + " WHERE ID = "+ "3");
//            }
//            resultSet = statement.executeQuery("SELECT * FROM ProductID WHERE ID =="  + "4" );
//            if(!resultSet.next()){
//                statement.executeUpdate("INSERT INTO ProductID (ID, VALUES ) VALUES (" + "4"  + "," + Integer.toString(storeShelfNumber) + ")");
//            }
//            else{
//                statement.executeUpdate("UPDATE ProductID SET Value ="  + Integer.toString(storeShelfNumber) + " WHERE ID = "+ "4");
//            }
//            resultSet = statement.executeQuery("SELECT * FROM ProductID WHERE ID =="  + "5" );
//            if(!resultSet.next()){
//                statement.executeUpdate("INSERT INTO ProductID (ID, VALUES ) VALUES (" + "5"  + "," + Integer.toString(storeIndexInShelf) + ")");
//            }
//            else{
//                statement.executeUpdate("UPDATE ProductID SET Value ="  + Integer.toString(storeIndexInShelf) + " WHERE ID = "+ "5");
//            }
//
//
//
//        }catch (SQLException e){
//            System.out.println("there is a problem with the database");
//        }
//    }
public void saveDetails(){
    try{
        java.sql.Statement statement = connection.createStatement();

        // Use prepared statements to prevent SQL injection attacks
        PreparedStatement insertStmt = connection.prepareStatement("INSERT INTO ProductID (ID, Value) VALUES (?, ?)");
        PreparedStatement updateStmt = connection.prepareStatement("UPDATE ProductID SET Value = ? WHERE ID = ?");

        // Update or insert values for each ID in ProductID table
        updateOrInsertValue(statement, insertStmt, updateStmt, 1, curr);
        updateOrInsertValue(statement, insertStmt, updateStmt, 2, storageShelfNumber);
        updateOrInsertValue(statement, insertStmt, updateStmt, 3, IndexInShelf);
        updateOrInsertValue(statement, insertStmt, updateStmt, 4, storeShelfNumber);
        updateOrInsertValue(statement, insertStmt, updateStmt, 5, numberOfMarketsInChain);
        updateOrInsertValue(statement, insertStmt, updateStmt, 6, managedMarket);
        updateOrInsertValue(statement, insertStmt, updateStmt, 7, numOfShelves);

    }catch (SQLException e){
        System.out.println(e.getMessage());
    }
}

    private void updateOrInsertValue(java.sql.Statement statement, PreparedStatement insertStmt, PreparedStatement updateStmt, int id, int value) throws SQLException {
        java.sql.ResultSet resultSet = statement.executeQuery("SELECT * FROM ProductID WHERE ID = " + id);
        if(!resultSet.next()){
            insertStmt.setInt(1, id);
            insertStmt.setInt(2, value);
            insertStmt.executeUpdate();
        }
        else{
            updateStmt.setInt(1, value);
            updateStmt.setInt(2, id);
            updateStmt.executeUpdate();
        }
    }


    public  int getProductId() {
        return ++curr;
    }
    public  int getProductIdNoUpdate(){
        return curr;
    }

    public int getStorageShelfNumber() {
        return storageShelfNumber;
    }

    public int getStorageIndexInShelf() {
        return IndexInShelf /2;           // always add 1 from store and one from storage
    }
    public void updateStorageIndexInShelf(){
        IndexInShelf++;
    }
    public void resetIndexInShelf(){
        IndexInShelf = 0;
    }
    public int getStoreShelfNumber() {
        return storeShelfNumber;
    }
    public void updateStoreShelfNumber(){
        storeShelfNumber++;
    }
    public void updateStorageShelfNumber(){
        storageShelfNumber++;
    }

    public int getStoreIndexInShelf() {
        return numberOfMarketsInChain;
    }

}
