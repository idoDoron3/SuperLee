package LoginRegister.DataAccess;

import DBConnect.Connect;
import Stock.Business.AProductCategory;
import Stock.DataAccess.CategoryDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class StockManagerDAO {

    private static StockManagerDAO instance  = null;
    private static Map<String, String> userToPassword;
    private static Connection connection;

    private StockManagerDAO(){
        connection = Connect.getConnection();
        userToPassword = new HashMap<>();
    }

    public static StockManagerDAO getInstance() {
        if(instance == null){
            instance = new StockManagerDAO();
        }
        return instance;
    }

//    public boolean connect(String givenUserName,String givenPassword){
//        boolean found;
//        String pass = getUserPassword(givenUserName);
//        found = pass != null && pass.equals(givenPassword);
//        return  found;
//    }

    public String getUserPassword(String username){
        if(userToPassword.get(username) == null){
            // read username from db
            readUser(username);
        }
        return userToPassword.get(username);
    }

    private void readUser(String username){
        String password;
        try{
            java.sql.Statement statement = connection.createStatement();
            String sql = "SELECT * FROM StockManagerUsers WHERE username = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();

            while(resultSet.next()){
                password = resultSet.getString("password");
                userToPassword.put(username,password);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public boolean register(String userName, String password){
        if(getUserPassword(userName) != null){
            return false;
        }
        userToPassword.put(userName,password);
        // write to db
        writeStockManager();
        return true;
    }

    private void writeStockManager(){
        for(String user: userToPassword.keySet()){
            try{
                java.sql.Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT * FROM StockManagerUsers WHERE username ='" + user + "'");
                if(!resultSet.next()){
                    PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO StockManagerUsers (username, password) VALUES (?, ?)");
                    preparedStatement.setString(1, user);
                    preparedStatement.setString(2, userToPassword.get(user));
                    preparedStatement.executeUpdate();

                }
//                else{
//                    statement.executeUpdate("UPDATE Category SET password ="  + userToPassword.get(user)+ " WHERE username ='" + user + "'");
//                }
            }
            catch (SQLException e) {
                System.out.println(e.getMessage());
            }

        }
    }
}
