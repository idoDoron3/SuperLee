package LoginRegister.Presentation;//import Business.Managers.Order_Manager;
//import Business.Managers.SupplyManager;
//import DataAccessLayer.DAO.DBTables;

import Stock.Presentation.StockMainUI;
import Stock.Presentation.StockManagerUI;
import Stock.Service.ProductService;
import Supplier_Module.Presentation.UI;
import Supplier_Module.Service.SupplierService;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Scanner;

public class StoreManagerMenu {
    private static StoreManagerMenu storeManagerMenu = null;
    static LocalDate localDate;

    static int dayDiff = 0;
    private UI ui;
    StockMainUI stockMainUi = new StockMainUI();
    Scanner input = new Scanner(System.in);


    private StoreManagerMenu() {
        localDate = LocalDate.now();
        ui= UI.getUser();
    }

    public static StoreManagerMenu getLoginMenu() {
        if (storeManagerMenu == null) {
            storeManagerMenu = new StoreManagerMenu();
        }
        return storeManagerMenu;
    }

    public void begin(){
        //SupplierService.getSupplierService().updatePeriodOrders(ProductService.getInstance().sendToSupplierAllProductsQuantity(),localDate);
        int choice1 = 0;
        while (choice1!=5) {
            System.out.println("Choose which option you want to enter");
            System.out.println("1. Suppliers");
            System.out.println("2. Stock");
//            System.out.println("3. Manage orders");
            System.out.println("3. Promote day");
            System.out.println("4. Exit");
            choice1 = input.nextInt();
            switch (choice1) {
                case 1: {
                    ui.beginSupplierMenu(localDate.plusDays(dayDiff));
                    break;
                }

                case 2:{
                    StockManagerUI stockManagerUi = new StockManagerUI(localDate);
                    stockManagerUi.startMenu();
                    break;
                }
                case 3:
                {
                    stockMainUi.updateForNextDay(localDate);
                    executeNextDay();
                    break;
                }
                case 4:
                {
                    System.out.println("Thanks for using our system! Bye Bye");
                    System.exit(0);
                    break;
                }
                default:
                    System.out.println("This is not valid option!");
            }
        }
    }

    private void executeNextDay(){
        localDate = LocalDate.now().plusDays(++dayDiff);
        HashMap<String,Integer> orderArrived = SupplierService.getSupplierService().SupplyNextDay(localDate);
        ProductService.getInstance().addMoreItemsToProductsFromSupplier(orderArrived);
        System.out.println("today: " + localDate.toString());
    }

}
