package Supplier_Module.Business.Managers;


import Supplier_Module.Business.Agreement.SupplierProduct;
import Supplier_Module.Business.Order;
import Supplier_Module.Business.Supplier;
import Supplier_Module.DAO.ItemOrderDAO;
import Supplier_Module.DAO.OrderDAO;
import Supplier_Module.DAO.Pair;
import Supplier_Module.DAO.SupplierDAO;

import java.time.LocalDate;
import java.util.*;

public class Order_Manager {
    private static Order_Manager order_manager = null;
    private OrderDAO orderDAO;
    private SupplyManager sup_manager;
    static int counter = 0;

    /**constructor
     *  constractor for singleton
     * I use here singleton design because i want only one appereance of this instance
     */
    public static Order_Manager getOrder_Manager() {
        if (order_manager == null) {
            order_manager = new Order_Manager();
        }
        return order_manager;
    }

    /**constructor
     * private constractor for singleton
     */
    private Order_Manager()
    {
        this.sup_manager = SupplyManager.getSupply_manager();
        this.orderDAO = OrderDAO.getInstance();
        counter = orderDAO.getMaxID();
    }

    public Map<Integer,Order> getOrders() {
        return orderDAO.getAllOrdersByKind(0);
    }

    public Map<Integer,Order> getWaiting_orders() {return orderDAO.getAllOrdersByKind(1); }

    public Map<Integer,Order> getPeriodic_orders() {return orderDAO.getAllOrdersByKind(2); }


    /**the function get ,map of string and int representing products and amount.
     *the function split the map to 2 maps - one with products can be provided by one supplier, and one with products cant.
     * the function return map od suppliers as key and lists of products to order as values.
     */
    public Map<Supplier,Map<SupplierProduct,Integer>> allProductsOrder(Map<String,Integer> ans){
        if(ans.isEmpty()){
            return null;
        }
        //charge all the suppliers to objects
        SupplierDAO.getInstance().SelectAllSuppliers();
        //save all the products provide by one supplier
        Map<String,Integer> oneSup = new HashMap<>();
        //save all the products provide by more than one supplier
        Map<String,Integer> moreThanOne = new HashMap<>();
        //check for each product from the map
        for (Map.Entry<String, Integer> iter : ans.entrySet()) {
            if(this.sup_manager.oneSupplier(iter.getKey(), iter.getValue())){
                oneSup.put(iter.getKey(),iter.getValue());
            }
            else {
                moreThanOne.put(iter.getKey(),iter.getValue());
            }
        }
        Map<Supplier,Map<SupplierProduct,Integer>> ret = this.sup_manager.allFullProducts(oneSup);
        Map<Supplier,Map<SupplierProduct,Integer>> ret12 =  this.sup_manager.allHalfProduct(moreThanOne, ret);
        return ret12;
    }


    /**the function get   map od suppliers as key and lists of products to order as values.
     *the function create order for each key value in the map.
     * the function return list of orders.
     */
    public LinkedList<Order> current_orders( Map<Supplier,Map<SupplierProduct,Integer>> ToOrder, LocalDate localDate){
        LinkedList<Order> current = new LinkedList<>();
        for (Map.Entry<Supplier, Map<SupplierProduct,Integer>> iter : ToOrder.entrySet()) {
            counter++;
            int or_id = counter;
            Order or1 = new Order(iter.getKey(),iter.getValue(),localDate, or_id);
            or1.setKind(2);
            current.add(or1);
            orderDAO.Insert(or1);
        }
        return current;
    }

    /**the function get map od suppliers as key and lists of products to order as values.
     *the function wrap the function above and check if there is orders.
     * the function return list of orders.
     */
    public LinkedList<Order> OrdersByRequest(Map<String,Integer> ans,LocalDate localDate){
        Map<Supplier,Map<SupplierProduct,Integer>> ToOrder = allProductsOrder(ans);
        if(ToOrder==null){
            return null;
        }
        LinkedList<Order> orders = current_orders(ToOrder,localDate);

        return orders;
    }

    /**
     *
     * @param order
     * this function get a period order and copy it, update the date and the id and add it to the periodic_orders list
     */
    public Order UpdatePeriodOrder (Order order) {
        LocalDate date= order.getSupplyDate();
        counter++;
        Order updateOrder= new Order(order.getSupplier(),order.getProducts_list_order(), date,date.plusDays(7),counter,2 );
        return updateOrder;
    }


    /**
     //create the waiting orders for the lack report and add them
     */
    public void lackProductsOrder(Map<String,Integer> request, LocalDate date){
        SupplierDAO.getInstance().SelectAllSuppliers();
        List<String> onTheWay = orderDAO.onTheWayProducts();
        Map<String,Integer> to_order = new HashMap<>();
        for(Map.Entry<String ,Integer> temp: request.entrySet()){
            if(!onTheWay.contains(temp.getKey())){
                to_order.put(temp.getKey(),temp.getValue());
            }
        }
        Map<Supplier,Map<SupplierProduct,Integer>> waiting = waitingOrderByRequest(to_order, date);
        LinkedList<Order> lackOrders = lackProducts(waiting, date);
        for(Order order : lackOrders ){
            order.setKind(1);
            orderDAO.Insert(order);
        }
    }

    /**
     //create the waiting orders for the lack report
     */
    public LinkedList<Order> lackProducts( Map<Supplier,Map<SupplierProduct,Integer>> ToOrder, LocalDate localDate){
        LinkedList<Order> lack = new LinkedList<>();
        for (Map.Entry<Supplier, Map<SupplierProduct,Integer>> iter : ToOrder.entrySet()) {
            counter++;
            int or_id = counter;
            Order or1 = new Order(iter.getKey(),iter.getValue(),localDate, or_id);
            lack.add(or1);
        }
        return lack;
    }




    /**
    //create the best time order for every product
     */
    public Map<Supplier,Map<SupplierProduct,Integer>> waitingOrderByRequest(Map<String,Integer> request, LocalDate date){
        Map<Supplier,Map<SupplierProduct,Integer>> waitingOrder = new HashMap<>();
        for (Map.Entry <String,Integer> iter : request.entrySet()) {
            waitingOrder = waitingOrderBysingleProduct(iter.getKey(),iter.getValue(), waitingOrder, date);
        }
        return waitingOrder;
    }

    /**
     //create the best time order for single product
     */
    public Map<Supplier,Map<SupplierProduct,Integer>> waitingOrderBysingleProduct(String name, int amount, Map<Supplier,Map<SupplierProduct,Integer>> ans, LocalDate date) {
        if(!this.sup_manager.isEnough(name, amount)) {
            return null;
        } else {
            while (amount != 0) {
                List<Supplier> suppliers= new ArrayList<>(SupplierDAO.getInstance().getSupplierByProductMap().get(name).values());
                int curr_amount =0;
                //find the current best supplier that can provide fastest of the product
                Supplier sup = bestTimeSupplier(name,amount,date);
                for(SupplierProduct p : sup.getAgreement().getProductList()){
                    if(p.getProduct_name().equals(name)){
                        curr_amount = p.getAmount_available();
                        break;
                    }
                }
                //now we have the supplier can provide the fastest ot the product in this time
                //check if we sub from the amount to much - for not provide more than requested
                if( curr_amount > amount){
                    curr_amount = amount;
                }
                //create product order detail from the product
                SupplierProduct p1 = this.sup_manager.fromStringToProduct(sup, name);
                //check if the supplier already in the list if yes just add the product to his list
                if(ans.containsKey(sup)){
                    ans.get(sup).put(p1,curr_amount);
                }
                //if the supplier is not exist in the list
                else{
                    Map<SupplierProduct,Integer> temp = new HashMap<>();
                    temp.put(p1, curr_amount);
                    ans.put(sup,temp);
                }
                amount = amount-curr_amount;
                suppliers.remove(sup);
            }
            return ans;
        }
    }

    public Supplier bestTimeSupplier(String name, int amount, LocalDate date){
        Supplier s1 = null;
        //set best time very big number
        int time = 10000;
        List<Supplier> suppliers= new ArrayList<>(SupplierDAO.getInstance().getSupplierByProductMap().get(name).values());
        for(Supplier supplier : suppliers){
            if((supplier).getAgreement().getMethodSupply().GetSupplyDate(date) < time){
                s1 = supplier;
                time = s1.getAgreement().getMethodSupply().GetSupplyDate(date);
            }
            else if(supplier.getAgreement().getMethodSupply().GetSupplyDate(date) == time){
                if(sup_manager.productPriceBySupplier(supplier,name,amount) < sup_manager.productPriceBySupplier(s1,name,amount)){
                    s1 = supplier;
                }
            }
        }
        return s1;
    }




    /**
     //move all the waiting orders in this day to done orders
     */
    public void promoteOrders(LocalDate date){
        for(Order order: this.getWaiting_orders().values()){
            if(order.getSupplyDate().isEqual(date)){
                orderDAO.getOrders_history().put(order.getOrder_id(),order);
                orderDAO.getWaiting_orders().remove(order);
                orderDAO.UpdateOrderForDone(order);
            }
        }
    }

    public void deletPeriodic(){
        if(orderDAO.getAllOrdersByKind(2)!= null) {
            List<Order> orders = new ArrayList<>(orderDAO.getAllOrdersByKind(2).values());
            for(Order order : orders){
                List<SupplierProduct> productListToRemove= new ArrayList<>(order.getProducts_list_order().keySet());
                for(SupplierProduct sp: productListToRemove)
                {
                    Pair<String,Integer> temp= new Pair<>(sp.getProduct_name(),order.getOrder_id());
                    ItemOrderDAO.getInstance().Delete(temp);
                    order.getProducts_list_order().remove(sp);
                }
                orderDAO.Delete(order);
            }
        }
    }

    /**
     * this function get the date and send all the orders of this date,
     * return map of the products name and the amount that sent
     * @param localDate
     * @return
     */
    public HashMap<String,Integer> SupplyNextDay(LocalDate localDate){
//        localDate=localDate.plusDays(1);
        HashMap<String, Integer> stock = new HashMap<>();
        List<Order> ans= new ArrayList<>(OrderDAO.getInstance().getAllOrdersBySupplyDate(localDate).values());
        for (Order order : ans) {
            Map<SupplierProduct,Integer> ans2= new HashMap<>(order.getProducts_list_order());
            for (Map.Entry<SupplierProduct, Integer> temp : ans2.entrySet()) {
                if (stock.containsKey(temp.getKey().getProduct_name())) {
                    int curr = stock.get(temp.getKey().getProduct_name()) + temp.getValue();
                    stock.put(temp.getKey().getProduct_name(), curr);
                } else {
                    stock.put(temp.getKey().getProduct_name(), temp.getValue());
                }
            }
            OrderDAO.getInstance().UpdateOrderForDone(order);
        }
        return stock;
        //return the stock map to freshi and yuvi

    }

    /**
     * input: list of the products names that is under the red line of the amount, today date
     * the function edit all the period orders for tomorrow- delete all the products that not necessary
     * @param underminimum
     * @param localDate
     */
    public void ConfirmPeriodOrders(List<String> underminimum, LocalDate localDate) {
        //Map for all the products that under the minimum line but not empty
        //run all the periodic orders
        List<Order> ordersList  = new ArrayList<>(OrderDAO.getInstance().getAllOrdersByKind(2).values());
        for (Order order : ordersList) {
            //run only the periodic orders for tomorrow
            if (order.getSupplyDate().equals(localDate.plusDays(1))) {
                if (underminimum.isEmpty()) {
                    OrderDAO.getInstance().Insert(order_manager.UpdatePeriodOrder(order));
                    OrderDAO.getInstance().Delete(order);
                } else {
                    //copy it before change and create new one for next week
                    OrderDAO.getInstance().Insert(order_manager.UpdatePeriodOrder(order));
                    //run all the products in the order
                    List<SupplierProduct> spList = new ArrayList<>(order.getProducts_list_order().keySet());
                    for (SupplierProduct sp : spList) {
                        //check if the product is good - if not delete it
                        if (!underminimum.contains(sp.getProduct_name())) {
                            order.getProducts_list_order().remove(sp);
                            Pair<String, Integer> ans = new Pair<>(sp.getProduct_name(), order.getOrder_id());
                            ItemOrderDAO.getInstance().Delete(ans);
                        }
                    }
                    //if all the products delete from the order
                    if (order.getProducts_list_order().size() == 0) {
                        OrderDAO.getInstance().Delete(order);
                    }
                }
            }
        }
    }


    /**
     * input: map with product name and requested amount- the product is empty in the stock
     * the function create the best time order for each product
     * @param report
     * @param localDate
     */
    public void lackReport(Map<String,Integer> report, LocalDate localDate){
        //stock creater lack report of string,int name report--now it is argument but it should be empty
        LocalDate date = localDate;
        order_manager.lackProductsOrder(report, date);
    }

    /**
     * when the stock add new product or delete product and the defult report change -
     * need to create period orders from the begining and delete the old one
     * @param report
     * @param localDate
     */
    public void updatePeriodOrders(Map<String,Integer> report, LocalDate localDate){
        order_manager.deletPeriodic();
        LinkedList<Order> ans = order_manager.OrdersByRequest(report,localDate);
    }

//    public void editPeriodicOrder(LocalDate localDate, Order order1){
//        System.out.println("This is the order you want to edit");
//        order1.PrintOrder();
//        System.out.println("Please select action:");


//    }

    public void deleteAllData()
    {
        Map<Integer,Order> allorders_0= OrderDAO.getInstance().getAllOrdersByKind(0);
        Map<Integer,Order> allorders_1= OrderDAO.getInstance().getAllOrdersByKind(1);
        Map<Integer,Order> allorders_2= OrderDAO.getInstance().getAllOrdersByKind(2);

        List<Order>temp0 =new ArrayList<>(allorders_0.values());
        for(Order order:temp0)
        {
            OrderDAO.getInstance().Delete(order);
        }
        List<Order>temp1 =new ArrayList<>(allorders_1.values());
        for(Order order:temp1)
        {
            OrderDAO.getInstance().Delete(order);
        }
        List<Order>temp2 =new ArrayList<>(allorders_2.values());
        for(Order order:temp2)
        {
            OrderDAO.getInstance().Delete(order);
        }
        ItemOrderDAO.getInstance().deleteAll();



    }
    public boolean deleteProductFromOrder(Order order, SupplierProduct sp){
        order.deleteProductFromOrder(sp);//delete product from map
        Pair<String,Integer> pair = new Pair<>(sp.getProduct_name(),order.getOrder_id());
        ItemOrderDAO.getInstance().Delete(pair);//delete the product from table
        this.orderDAO.updateAndGetOrderTotalPriceById(order.getOrder_id());//update the new price in the table
        System.out.println("Product "+ sp.getProduct_name() + " delete successfully!");

        if(order.getProducts_list_order().isEmpty())
        {
            OrderDAO.getInstance().Delete(order);
            System.out.println("There is no products in this order anymore");
            System.out.println("The order deleted!");
            return false;
        }
        return true;
    }
    public void editProductAmount(Order order, SupplierProduct sp, int amount){
        order.editProductInOrder(sp,amount);
        ItemOrderDAO.getInstance().changeAmountOfProduct(sp.getProduct_name(),order.getOrder_id(), amount);
        this.orderDAO.updateAndGetOrderTotalPriceById(order.getOrder_id());//update the new price in the table
        System.out.println("Product "+ sp.getProduct_name() + " amount updated successfully!");
    }
    public boolean isExistOrder(int id)
    {
        Map<Integer,Order> orders2=getPeriodic_orders();
        for(int temp: orders2.keySet())
        {
            if(temp==id)
                return true;
        }
        Map<Integer,Order> orders0=getOrders();
        for(int temp: orders0.keySet())
        {
            if(temp==id)
                return true;
        }
        Map<Integer,Order> orders1=getWaiting_orders();
        for(int temp: orders1.keySet())
        {
            if(temp==id)
                return true;
        }
        return false;
    }
    public Order getOrderById(int id)
    {
        Map<Integer,Order> orders=this.getPeriodic_orders();
        for(int temp: orders.keySet())
        {
            if(temp==id)
                return orders.get(id);
        }
        Map<Integer,Order> orders1=this.getOrders();
        for(int temp: orders1.keySet())
        {
            if(temp==id)
                return orders1.get(id);
        }
        Map<Integer,Order> orders2=this.getWaiting_orders();
        for(int temp: orders2.keySet())
        {
            if(temp==id)
                return orders2.get(id);
        }
        return null;
    }


    public String[] getAllProductsNameOfOrder(int id)
    {
        Order temp= this.getPeriodOrderById(id);
        Map<SupplierProduct,Integer> allProductsMap= temp.getProducts_list_order();
        String[] ans= new String[allProductsMap.size()];
        int index=0;
        for(SupplierProduct p: allProductsMap.keySet())
        {
            ans[index]= p.getProduct_name();
            index++;
        }
        return ans;
    }

    public Order getPeriodOrderById(int id)
    {
        Map<Integer,Order> orders=this.getPeriodic_orders();
        for(int temp: orders.keySet())
        {
            if(temp==id)
                return orders.get(id);
        }
        return null;
    }

}

