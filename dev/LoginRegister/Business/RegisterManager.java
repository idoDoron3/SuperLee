package LoginRegister.Business;

import LoginRegister.DataAccess.StockManagerDAO;
import LoginRegister.DataAccess.SupplierManagerDAO;

public class RegisterManager {

    private static RegisterManager instance = null;

    private static final StockManagerDAO stockManagerDAO = StockManagerDAO.getInstance();

    private static final SupplierManagerDAO supplierManagerDAO = SupplierManagerDAO.getInstance();


    private RegisterManager(){
        //private constructor
    }

    public static RegisterManager getInstance() {
        if (instance == null) {
            instance = new RegisterManager();
        }
        return instance;
    }

    public boolean register(String username, String password, String role) {
        if (role.equals("store manager")) {
            if (stockManagerDAO.getUserPassword(username) != null ||
                    supplierManagerDAO.getUserPassword(username) != null) {
                return false;
            }
            stockManagerDAO.register(username, password);
            supplierManagerDAO.register(username, password);
            return true;
        }
        else if (role.equals("stock manager")) {
            // Check if this username is not known as supplier manager
            if (supplierManagerDAO.getUserPassword(username) == null) {
                return stockManagerDAO.register(username, password);
            }
        }
        else {
            // Check if this username is not known as stock manager
            if (stockManagerDAO.getUserPassword(username) == null) {
                return supplierManagerDAO.register(username, password);
            }
        }
        return false;
    }
}
