package LoginRegister.Business;

import LoginRegister.DataAccess.StockManagerDAO;
import LoginRegister.DataAccess.SupplierManagerDAO;

public class LoginManager {

    private static LoginManager instance = null;

    private static final StockManagerDAO stockManagerDAO = StockManagerDAO.getInstance();

    private static final SupplierManagerDAO supplierManagerDAO = SupplierManagerDAO.getInstance();


    private LoginManager(){
        //private constructor
    }

    public static LoginManager getInstance() {
        if (instance == null) {
            instance = new LoginManager();
        }
        return instance;
    }

    public String login(String username, String password, String role) {
        role = role.toLowerCase();
        // return an empty string if the login is successful or an error message if not
        if (role.equals("store manager")) {
            // Check if store manager is not existing in one relation at least (stock manager and supplier manager)
            if (stockManagerDAO.getUserPassword(username) == null ||
                    supplierManagerDAO.getUserPassword(username) == null) {
                return "This username is not known as store manager";
            }
            else {
                // Check if the username matching to the password
                if (stockManagerDAO.getUserPassword(username).equals(password) &&
                        supplierManagerDAO.getUserPassword(username).equals(password)) {
                    return "";
                }
                return "Incorrect password";
            }
        }
        else if (role.equals("stock manager")) {
            // Check if the username is not known as stock manager
            if (stockManagerDAO.getUserPassword(username) == null ||
                    supplierManagerDAO.getUserPassword(username) != null) {
                return "This username is not known as stock manager";
            }
            else {
                // Check if the username matching to the password
                if (stockManagerDAO.getUserPassword(username).equals(password)) {
                    return "";
                }
                return "Incorrect password";
            }
        }
        else {
            // Check if the username is not known as supplier manager
            if (supplierManagerDAO.getUserPassword(username) == null ||
                    stockManagerDAO.getUserPassword(username) != null) {
                return "This username is not known as supplier manager";
            } else {
                // Check if the username matching to the password
                if (supplierManagerDAO.getUserPassword(username).equals(password)) {
                    return "";
                }
                return "Incorrect password";
            }
        }
    }
}
