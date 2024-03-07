package Supplier_Module.DAO;


import DBConnect.Connect;

import java.sql.*;
import java.text.MessageFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public abstract class DAO {

    public final String _tableName;
    protected Connection connection;
    {
        try {
            //connection = DBTables.getInstance().open(); // TODO
            connection = Connect.getConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //constructor
    public DAO(String tableName) {
        this._tableName = tableName;
    }

    public boolean deleteTable(){
        boolean res = true;
        String sql = MessageFormat.format("DROP TABLE {0}"
                , _tableName);

        try (
                PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Got Exception:");
            System.out.println(e.getMessage());
            System.out.println(sql);
            res = false;
        }
        return res;
    }

    public boolean Delete() {
        boolean res = true;
        String sql = MessageFormat.format("DELETE FROM {0}"
                , _tableName);

        try (
                PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Got Exception:");
            System.out.println(e.getMessage());
            System.out.println(sql);
            res = false;
        }
        return res;
    }
    public  boolean Update(String ColumnName, int value,int key) {
        boolean res = true;
        String sql = MessageFormat.format("UPDATE {0} SET {1} = ? WHERE id = ? "
                , _tableName,ColumnName);
        try (
                PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, value);
            pstmt.setInt(2, key);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Got Exception:");
            System.out.println(e.getMessage());
            System.out.println(sql);
            res = false;
        }
        return res;
    }
    public  boolean Update(String ColumnName, double value,int key) {
        boolean res = true;
        String sql = MessageFormat.format("UPDATE {0} SET {1} = ? WHERE id = ? "
                , _tableName,ColumnName);
        try (
                PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setDouble(1, value);
            pstmt.setInt(2, key);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Got Exception:");
            System.out.println(e.getMessage());
            System.out.println(sql);
            res = false;
        }
        return res;
    }


    public  boolean Update(String ColumnName, String value,int key) {
        boolean res = true;
        String sql = MessageFormat.format("UPDATE {0} SET {1} = ? WHERE id = ? "
                , _tableName,ColumnName);
        try (
                PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, value);
            pstmt.setInt(2, key);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Got Exception:");
            System.out.println(e.getMessage());
            System.out.println(sql);
            res = false;
        }
        return res;
    }



    protected String keysQuery(List<String> Columnkeys){
        String keysQuery="";
        for(String key: Columnkeys){
            keysQuery+=" "+key+" = ? AND";
        }
        keysQuery=keysQuery.substring(0,keysQuery.length()-4);

        return keysQuery;
    }

    protected List Select(){
        List list=new ArrayList<>();
        String sql = MessageFormat.format("SELECT * From {0}", _tableName);
        try (
                PreparedStatement pstmt = connection.prepareStatement(sql)) {
            ResultSet resultSet=pstmt.executeQuery();
            while (resultSet.next()) {
                // Fetch each row from the result set
                list.add(convertReaderToObject(resultSet));
            }

        } catch (SQLException | ParseException e) {
            System.out.println("Got Exception:");
            System.out.println(e.getMessage());
            System.out.println(sql);
        }
        return list;
    }

    //abstract functions
    public abstract boolean Insert(Object obj);

    public abstract boolean Delete(Object obj);

    public abstract Object convertReaderToObject(ResultSet res) throws SQLException, ParseException;







}
