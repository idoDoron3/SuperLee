import DBConnect.Connect;
import Stock.Business.*;
import Stock.DataAccess.ProductDAO;
import Stock.Service.ProductService;
import Supplier_Module.Business.Agreement.SupplierProduct;
import Supplier_Module.Business.Order;
import Supplier_Module.DAO.OrderDAO;
import Supplier_Module.DAO.SupplierProductDAO;
import Supplier_Module.Service.SupplierService;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

public class SuplyStockTests {

//check if there is periodic order for each product
    @Test
    void checkUpdatePeriodNumOfOrders() {
        Connect.loadDataToDatabase();
        Map<String, Integer> stockReport = ProductService.getInstance().sendToSupplierAllProductsQuantity();
        int size = stockReport.size();
        SupplierService.getSupplierService().updatePeriodOrders(stockReport, LocalDate.now());
        Map<Integer, Order> temp = OrderDAO.getInstance().getAllOrdersByKind(2);
        List<String> flag = new ArrayList<>();
        for (Order order : temp.values()) {
            for (SupplierProduct sp : order.getProducts_list_order().keySet()) {
                if (!flag.contains(sp.getProduct_name())) {
                    flag.add(sp.getProduct_name());
                }
            }
        }
        assertEquals(size, flag.size());
    }
    //by all burgers and check if there is lack product for it
    @Test
    void checklackOrder() {
        ProductManager.setShortages(new Shortages());
        Connect.loadDataToDatabase();
        ProductService.getInstance().sellProductsByUniqueCode(ProductDAO.getInstance().getProduct("QnVyZ2VycyAxLjAga2c="), 2, LocalDate.now());
        Map<Integer, Order> temp = OrderDAO.getInstance().getAllOrdersByKind(1);
        boolean flag = false;
        for (Order order : temp.values()) {
            for (SupplierProduct sp : order.getProducts_list_order().keySet()) {
                if (order.getStartDate().equals(LocalDate.now()) && sp.getProduct_name().equals("Burgers 1.0 kg")) {
                    flag = true;
                    break;
                }
            }
        }
        assertTrue(flag);

    }
//check if the confirm period orders is good - cancel all the bad products
    @Test
    void addProductToSupperLee() {
        Connect.loadDataToDatabase();
        SupplierService.getSupplierService().updatePeriodOrders(ProductService.getInstance().sendToSupplierAllProductsQuantity(), LocalDate.now());
        List<String> lst = new ArrayList<>();
        lst.add("Apples 500.0 gr");
        lst.add("Burgers 1.0 kg");
        SupplierService.getSupplierService().ConfirmPeriodOrders(lst,LocalDate.now());
        Map<Integer,Order> map = OrderDAO.getInstance().getAllOrdersBySupplyDate(LocalDate.now().plusDays(1));
        assertEquals(map.size(), 0);
        Connect.loadDataToDatabase();
    }

//check if all the products can be supply
    @Test
    void checkSupplierProductsExist(){
        Connect.loadDataToDatabase();
        int correct = ProductDAO.getInstance().getAllProducts().size();
        int cnt =0;
        List<SupplierProduct> temp = SupplierProductDAO.getInstance().getProductsMapper();
        Set<String> set = new HashSet<>();
        for(SupplierProduct sp: temp){
            set.add(sp.getProduct_name());
        }
        for(String sp : set){
            String[] words = sp.split(" ");
            String subsub = words[words.length-2] + " " + words[words.length-1];
            StringBuilder stringBuilder = new StringBuilder();
            for(int i = 0 ; i < words.length - 2; i++){
                stringBuilder.append(words[i]).append(" ");
            }
            String  sub = stringBuilder.toString().substring(0,stringBuilder.length()-1);

            if(ProductService.getInstance().getProductByCategories(sub,subsub) != null){
                cnt++;
            }



        }
        assertEquals(cnt,correct);


    }
}


