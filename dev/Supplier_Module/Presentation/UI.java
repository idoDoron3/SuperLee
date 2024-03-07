package Supplier_Module.Presentation;

import Stock.Service.ProductService;
import Supplier_Module.Business.Agreement.Agreement;
import Supplier_Module.Business.Agreement.SupplierProduct;
import Supplier_Module.Business.Card.SupplierCard;
import Supplier_Module.Business.Managers.Order_Manager;
import Supplier_Module.Business.Order;
import Supplier_Module.Business.Supplier;
import Supplier_Module.Business.Managers.SupplyManager;
import Supplier_Module.DAO.DBTables;
import Supplier_Module.DAO.OrderDAO;
import Supplier_Module.Service.SupplierService;

import java.time.LocalDate;
import java.util.*;

public class UI {

    private static UI user = null;

    //singleton
    SupplyManager sup_manager;
    Order_Manager order_manager;
    Data Data;
    AgreementMenu agreementMenu;
    CardMenu cardMenu;
    Scanner input = new Scanner(System.in);


    private UI() {
        this.sup_manager = SupplyManager.getSupply_manager();
        this.order_manager = Order_Manager.getOrder_Manager();
        this.Data= Data.getLoadData();
        this.cardMenu=CardMenu.getCardMenu();
        this.agreementMenu = agreementMenu.getAgreementMenu();
        DBTables.getInstance();
    }

    public static UI getUser() {
        if (user == null) {
            user = new UI();
        }
        return user;
    }

    public void beginOrderMenu(LocalDate localDate) {
        int choice0 = 0;
        while (choice0 != 5) {
            System.out.println("Welcome to Orders Management! \nDo you want to edit periodic order ,create new periodic, delete periodic order, order or load default periodic? \n1.Edit order  \n2.Add new periodic order \n3.Delete periodic order \n4.Load best periodic for each product \n5.Exit");
            choice0 = input.nextInt();
            switch (choice0) {
                case 1: {
                    editPeriodicOrder(localDate);
                    break;

                }
                case 2: {
                    //add new periodic order
                    addPeriodicOrder(localDate);
                    break;
                }
                case 3:{
                    deletePeriodOrder(localDate);
                    break;
                }
                case 4:{
                    SupplierService.getSupplierService().updatePeriodOrders(ProductService.getInstance().sendToSupplierAllProductsQuantity(),localDate);
                    System.out.println("Each product updated with the best periodic for it. ");
                    break;
                }
                case 5:
                    break;//return to
                default:
                    System.out.println("This is not valid option!");
            }
        }
    }
    public void beginSupplierMenu(LocalDate localDate){
        int choice1 = 0;
        while (choice1!=2) {
            System.out.println("Welcome to Suppliers system! \nDo you want to edit current data,create new data or start over? \n1.Edit existing data  \n2.Exit");
            choice1 = input.nextInt();
            switch (choice1)
            {
                case 1:
                {
                    System.out.println("Please select one of the following options:");
                    mainMenu();
                    break;

                }
                case 2:{
                    break;
                }
                default:
                    System.out.println("This is not valid option!");
            }
        }
    }


    public void mainMenu(){
        int choice = 9;
        while (choice != 0)
        {
            System.out.println("1. Add new supplier.");
            System.out.println("2. Edit existing supplier details.");
            System.out.println("3. Delete an existing supplier.");
            System.out.println("4. View order history of supplier");
            System.out.println("5. View Supplier details");
            System.out.println("0. Back");
            choice = input.nextInt();

            switch (choice)
            {
                case 0:
                {
                    break;
                }
                case 1:
                {
                    this.CreateSupplier();
                    break;
                }

                case 2:
                {
                    this.EditSupplier();
                    break;
                }

                case 3:
                {
                    System.out.println("Please enter supplier id:");
                    int sup_id = input.nextInt();
                    this.sup_manager.removeSupplierByUser(sup_id);
                    break;
                }
                case 4:
                {
                    System.out.println("Please enter supplier ID:");
                    int id = input.nextInt();
                    this.sup_manager.PrintSupplierOrders(id);
                    break;
                }
                case 5:
                {
                    System.out.println("Please enter supplier ID:");
                    int id = input.nextInt();
                    this.sup_manager.PrintSupplierDetailes(id);
                    break;
                }


                default:
                {
                    System.out.println("This option is not valid!");
                    break;
                }

            }
        }


    }

    public void CreateSupplier()
    {
        SupplierCard card=this.cardMenu.CreateSupplierCard();
        Agreement agreement=this.agreementMenu.CreateAgreement(card.getSupplier_number());
        this.sup_manager.CreateSupplier(card,agreement);


    }

    /**
     * function asks from user supplier number and let him edit the supplier details
     */
    public void EditSupplier() {
        Scanner input = new Scanner(System.in);
        System.out.println("Enter the supplier number you want to edit:");
        int num = input.nextInt();
        if (this.sup_manager.isSupplierNumberExist(num)) // the number is exist
        {
            Supplier supplier=this.sup_manager.getSupplier(num);
            int choice = 9;
            while (choice != 0) {
                System.out.println("1. Edit supplier card");
                System.out.println("2. Edit agreement");
                System.out.println("0. Quit");
                choice = input.nextInt();
                if (choice == 1) {
                    this.cardMenu.EditCard(supplier.getCard());
                    //EditCard(suppliers_list.get(num).getCard());
                } else if (choice == 2) {
                    this.agreementMenu.EditAgreement(supplier);
                }
            }
        } else
            System.out.println("This supplier number is invalid");
    }

    public void editPeriodicOrder(LocalDate localDate){
        Scanner input = new Scanner(System.in);
        System.out.println("Enter the Order number you want to edit:");
        int num = input.nextInt();
        if(this.order_manager.getPeriodic_orders().containsKey(num)){
            Order order = this.order_manager.getPeriodic_orders().get(num);
            if(order.getSupplyDate().minusDays(1).equals(localDate))//if the supply date id tomorrow don't allow to edit the order
            {
                System.out.println("This order will provided in less then 24 hours and cant be edit!");
            }
            else
            {
                editRealOrder(order,localDate);
            }
        }
        else {
            System.out.println("There is no periodic order with this number!");
        }
    }
    public void editRealOrder(Order order, LocalDate localDate){
        System.out.println("This is the order you want to edit");
        order.PrintOrder();
        int choice0 = 0;
        while (choice0 != 3) {
            System.out.println("Please select action:  \n1. Delete product from order \n2.Edit amount of product \n3. Return to Order management menu");
            choice0 = input.nextInt();
            switch (choice0) {
                case 1: {
                    deleteProductFromOrder(order, localDate);
                    break;
                }
                case 2: {
                    editProductAmountInOrder(order);
                    break;
                }
                case 3:{
                    break;
                }
                default:
                    System.out.println("This is not valid option!");
            }
        }
    }
    public void deletePeriodOrder(LocalDate localDate){
        Scanner input = new Scanner(System.in);
        System.out.println("Enter the Order number you want to delete:");
        int num = input.nextInt();
        if(this.order_manager.getPeriodic_orders().containsKey(num)){
            Order order = this.order_manager.getPeriodic_orders().get(num);
            if(order.getSupplyDate().minusDays(1).equals(localDate))//if the supply date id tomorrow don't allow to edit the order
            {
                System.out.println("This order will provided in less then 24 hours and cant be delete!");
            }
            else
            {
                OrderDAO.getInstance().Delete(OrderDAO.getInstance().getOrderById(num));
                System.out.println("Order number " + num + " has been deleted");
            }
        }
        else {
            System.out.println("There is no periodic order with this number!");
        }
    }

    public void deleteProductFromOrder(Order order1 , LocalDate localDate){
        Scanner input123 = new Scanner(System.in);
        String name = "";
        System.out.println("Please enter the name of the product you want to delete");
        name = input123.nextLine();
        if(order1.isProductInOrder(name)!= null)
        {
            boolean flag=this.order_manager.deleteProductFromOrder(order1,order1.isProductInOrder(name));
            if(!flag)
            {
                beginOrderMenu(localDate);
            }
        }
        else{
            System.out.println("this product is not in the order!");
        }

    }
    public void editProductAmountInOrder(Order order1){
        Scanner input1234 = new Scanner(System.in);
        String name = "";
        int amount =0;

        System.out.println("Please enter the name of the product you want to edit");
        name = input1234.nextLine();
        if(order1.isProductInOrder(name)!= null)
        {
            SupplierProduct sp = order1.isProductInOrder(name);
            System.out.println("Please enter the new amount of the product you want to update");
            amount = input1234.nextInt();
            if(amount > sp.getAmount_available() || amount <1 ){
                System.out.println("Supplier number " + sp.getSupplierID() + " can't provide this amount");
            }
            else
            {
                this.order_manager.editProductAmount(order1,sp,amount);
            }
        }
        else{
            System.out.println("this product is not in the order!");
        }
    }
    public void addPeriodicOrder(LocalDate date){
        Scanner input = new Scanner(System.in);
        System.out.println("Enter the Supplier number you want to order from:");
        int num = input.nextInt();
        if(this.sup_manager.getSuppliers_list().containsKey(num)){
            createPeriodicOrder(this.sup_manager.getSuppliers_list().get(num), date);
        }
        else {
            System.out.println("There is no supplier with this number!");
        }
    }
    public void createPeriodicOrder(Supplier supplier, LocalDate date){
        Scanner input2 = new Scanner(System.in);
        String name = "";
        int choice = 0;
        int amount = 0;
        Map<SupplierProduct, Integer> temp = new HashMap<>();
        while (choice!=2) {
            System.out.println("Please select your action? \n1.Add product to order  \n2.finish creating the order");
            choice = input.nextInt();
            switch (choice)
            {
                case 1:
                {
                    System.out.println("Please enter the product name you want to add:");
                    name = input2.nextLine();
                    boolean found = false;
                    List<SupplierProduct> sp  = new ArrayList<>(temp.keySet());
                    for(SupplierProduct supplierProduct : sp){
                        if(supplierProduct.getProduct_name().equals(name)){
                            found = true;
                            break;
                        }
                    }
                    if(found){
                        System.out.println("This product is already in the order");
                        break;
                    }
                    else if(!supplier.getAgreement().isExistName(name)){
                        System.out.println("Supplier " + supplier.getCard().getSupplier_name() + " can't supply this product!");
                        break;
                    }
                    else{
                        System.out.println("Please enter the amount of product you want to add to the order:");
                        amount = input.nextInt();
                        if(amount > supplier.getAgreement().getProduct(name).getAmount_available()){
                            System.out.println("Supplier "  +supplier.getCard().getSupplier_name() + " can't provide this amount");
                        }
                        else{
                            temp.put(supplier.getAgreement().getProduct(name), amount);
                            System.out.println("Product added successfully!");
                        }
                        break;
                    }
                }

                case 2:{

                    Map<Supplier, Map<SupplierProduct,Integer>> ans = new HashMap<>();
                    ans.put(supplier,temp);
                    this.order_manager.current_orders(ans, date);
                    System.out.println("The order added successfully!");
                    break;
                }
                default:
                    System.out.println("This is not valid option!");
            }

        }
    }
}