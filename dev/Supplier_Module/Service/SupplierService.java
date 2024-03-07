package Supplier_Module.Service;

import Supplier_Module.Business.Agreement.SupplierProduct;
import Supplier_Module.Business.Managers.Order_Manager;
import Supplier_Module.Business.Order;
import Supplier_Module.DAO.ItemOrderDAO;
import Supplier_Module.DAO.OrderDAO;
import Supplier_Module.DAO.Pair;

import java.time.LocalDate;
import java.util.*;

public class SupplierService {
    private static SupplierService supplierService = null;

    private Order_Manager order_manager;


    /**
     * constructor
     * constractor for singleton
     * I use here singleton design because i want only one appereance of this instance
     */
    public static SupplierService getSupplierService() {
        if (supplierService == null) {
            supplierService = new SupplierService();
        }
        return supplierService;
    }

    /**
     * constructor
     * private constractor for singleton
     */
    public SupplierService() {
        this.order_manager = Order_Manager.getOrder_Manager();
    }

    public void lackReport(Map<String, Integer> report, LocalDate localDate) {
        //stock creater lack report of string,int name report--now it is argument but it should be empty
        order_manager.lackReport(report, localDate);
    }

    //when the stock add new product or delete product and the defult report change - need to create period orders from the begining and delete the old one
    public void updatePeriodOrders(Map<String, Integer> report, LocalDate localDate) {
        order_manager.updatePeriodOrders(report, localDate);
    }

    public HashMap<String, Integer> SupplyNextDay(LocalDate localDate) {
        return Order_Manager.getOrder_Manager().SupplyNextDay(localDate);
    }


//
public void ConfirmPeriodOrders(List<String> underminimum, LocalDate localDate) {
        order_manager.ConfirmPeriodOrders(underminimum,localDate);
}





}
