package Supplier_Module.Business.Managers;

import Supplier_Module.Business.Agreement.Agreement;
import Supplier_Module.Business.Agreement.MethodSupply.ByFixedDays;
import Supplier_Module.Business.Agreement.MethodSupply.BySuperLee;
import Supplier_Module.Business.Agreement.MethodSupply.BySupplyDays;
import Supplier_Module.Business.Agreement.MethodSupply.MethodSupply;
import Supplier_Module.Business.Agreement.SupplierProduct;
import Supplier_Module.Business.Card.ContactMember;
import Supplier_Module.Business.Card.SupplierCard;
import Supplier_Module.Business.Discount.PrecentageDiscount;
import Supplier_Module.Business.Discount.Range;
import Supplier_Module.Business.Order;
import Supplier_Module.Business.Supplier;
import Supplier_Module.DAO.*;
import Supplier_Module.DAO.ContactMemberDAO;
import Supplier_Module.DAO.OrderDAO;
import Supplier_Module.DAO.SupplierDAO;
import Supplier_Module.DAO.SupplierProductDAO;

import java.util.*;

public class SupplyManager {
    private static SupplyManager supply_manager = null;
    private SupplierDAO supplierDAO;
    private SupplierProductDAO supplierProductDAO;
    private ContactMemberDAO contactMemberDAO;


    private Scanner input = new Scanner(System.in);



    /**
     * constructor
     * private constractor for singleton
     */
    private SupplyManager() {
        supplierDAO = SupplierDAO.getInstance();
        supplierProductDAO = SupplierProductDAO.getInstance();
        contactMemberDAO = ContactMemberDAO.getInstance();
    }

    /**
     * constructor
     * constractor for singleton
     * I use here singleton design because i want only one appereance of this instance
     */
    public static SupplyManager getSupply_manager() {
        if (supply_manager == null) {
            supply_manager = new SupplyManager();

        }
        return supply_manager;
    }

    /**
     * Getters
     */
    public Map<Integer, Supplier> getSuppliers_list() {return supplierDAO.getSupplierHashMap();
    }

    public Map<String, Map<Integer,Supplier>> getSupplierByProduct() {
        return supplierDAO.getSupplierByProductMap();
    }

    public SupplierCard CreateBasicCard(String n, int supplier_number, int company_number, int bank_account, String address) {
        SupplierCard newSupplier = new SupplierCard(n, supplier_number, company_number, bank_account, address);
        return newSupplier;
    }

    public void addSupplierByProduct(String productName, Supplier supplier, boolean isThisProductAlreadyInSystem) {
        if (isThisProductAlreadyInSystem) {
//            SupplierByProduct.get(productName).add(supplier);
            supplierDAO.getSupplierByProductMap().get(productName).put(supplier.getAgreement().getSupplier_number(),supplier);
        }
        else {
            Map<Integer,Supplier> temp=new HashMap<>();
            temp.put(supplier.getAgreement().getSupplier_number(),supplier);
            supplierDAO.getSupplierByProductMap().put(productName, temp);

        }
    }


    public boolean isExistName(Agreement agreement, String product_name) {
        SupplierProduct sp  = supplierProductDAO.getSupplierProductById(product_name,agreement.getSupplier_number());
        if(sp==null){
            return false;
        }
        return true;
//        return agreement.isExistName(product_name);
    }

    public boolean isExistID(Agreement agreement, int id) {
        LinkedList<SupplierProduct> sp = supplierProductDAO.getProductsBySupplier(agreement.getSupplier_number());
        for (SupplierProduct supplierProduct : sp) {
            if (supplierProduct.getLocal_key() == id) {
                return true;
            }
        }
//        return agreement.isExistID(id);
        return false;
    }

    public SupplierProduct createProduct(String product_name, int product_id, double weight, double price, int amount,int supplierID) {
        SupplierProduct p = new SupplierProduct(product_name, product_id, weight, price, amount, supplierID);
        return p;
    }

    public Range createRange(int min, int max) {
        Range range = new Range(min, max);
        return range;
    }

    public PrecentageDiscount createPrecentageDiscount(int min, int max, int percentage) {
        Range range = createRange(min, max);
        PrecentageDiscount discount = new PrecentageDiscount(range, percentage);
        return discount;
    }

    public void addDiscountToProduct(SupplierProduct supplierProduct, PrecentageDiscount discount) {
        supplierProduct.getDiscounts().add(discount); // adding the discount to the list (range and percentage)
    }

    public void addProductToAgreement(Agreement agreement, SupplierProduct x, int data) {
        agreement.addProductToAgreement(x);
        if(data == 0) {
            SupplierProductDAO.getInstance().Insert(x);
        }

    }

    public void addTotal_orderDiscount(Agreement agreement, PrecentageDiscount discount) {
        agreement.addTotal_orderDiscount(discount);
    }

    //add to agreement supplier id
    public void setMethodSupply(Agreement agreement, MethodSupply methodSupply) {
        agreement.setMethodSupply(methodSupply);
    }

    public MethodSupply createByFixedDays(String type, int[] supplydays) {
        ByFixedDays method = new ByFixedDays("By fixed days", supplydays);
        return method;
    }

    public MethodSupply createBySuperLee(String type) {
        BySuperLee method = new BySuperLee("By Supper Lee");
        return method;
    }

    public MethodSupply createBySupplyDays(String type, int x) {
        BySupplyDays method = new BySupplyDays("By supply days",x);
        return method;
    }

    public void setEom(Agreement agreement, int x) {
        agreement.setEom(x);
    }

    public void setPayment_method(SupplierCard x, int y) {
                String str = "";
        if(y ==1)
        {
            str="cash";

        }
        if(y==2)
        {
            str="bit";
        }
        if(y==3)
        {
            str="credit_card";
        }
        supplierDAO.UpdatePaymentMethod(x.getSupplier_number(),str);
        x.setPayment_method(y);
    }

    public void setAddress(SupplierCard x, String address) {
        supplierDAO.UpdateAddress(x.getSupplier_number(),address);
        x.setAddress(address);
    }


    /**
     * function get SupplierCard, Agreement
     * function create a new supplier
     */
    public Supplier CreateSupplier(SupplierCard card, Agreement agreement) {
        Supplier s = new Supplier(card, agreement);
        supplierDAO.Insert(s);
        return s;
    }


    public void printTotalDiscounts(Agreement agreement) {
        agreement.printTotalDiscounts();
    }


    /**
     * function get Supplier
     * after creating a supplier we want to update the products we can buy from all suppliers
     * and add to the list of the product the supplier that can provide it
     */
    public void UpdateSupplierByProduct(Supplier s) {
        for (int i = 0; i < s.getAgreement().getProductList().size(); i++) {
            String temp = s.getAgreement().getProductList().get(i).getProduct_name();
            if (getSupplierByProduct().containsKey(temp)) {
                getSupplierByProduct().get(temp).put(s.getAgreement().getSupplier_number(),s);
            } else {

                Map<Integer,Supplier> temp2=new HashMap<>();
                temp2.put(s.getAgreement().getSupplier_number(),s);
                getSupplierByProduct().put(temp, temp2);
            }
        }


    }


    public Supplier getSupplier(int supplierNum){return this.supplierDAO.getSupplier(supplierNum);
    }
    public boolean isSupplierNumberExist(int id) {
        return supplierDAO.getSupplier(id) != null;
    }


    public void setCompany_number(SupplierCard x, int company_number) {
        x.setCompany_number(company_number);
//        supplierDAO.UpdateBankAccount(x.getSupplier_number(),String.valueOf(bank_account));
    }

    public void setBank_account(SupplierCard x, int bank_account) {
        x.setBank_account(bank_account);
        supplierDAO.UpdateBankAccount(x.getSupplier_number(),String.valueOf(bank_account));
    }

    public boolean isExsitContactMember(SupplierCard x, String phone) {
        if(ContactMemberDAO.getInstance().getContactByPhoneNumber(phone)==null){
            return false;
        }
        return true;
//        return x.isExsitContactMember(phone);
    }

    public void addContact_membersToData(SupplierCard supplierCard, String phone, String name, String email) {
        ContactMember x = new ContactMember(phone, name, email,supplierCard.getSupplier_number());
        supplierCard.addContact_members(x);
        ContactMemberDAO.getInstance().Insert(x);
    }

    public void addContact_membersToCardNoData(SupplierCard supplierCard, String phone, String name, String email) {
        ContactMember x = new ContactMember(phone, name, email,supplierCard.getSupplier_number());
        supplierCard.addContact_members(x);
//        ContactMemberDAO.getInstance().Insert(x);
    }

    public void addCategory_ToSupplier(SupplierCard x, String category) {

        x.addCategory_ToSupplier(category);
    }

    public void printContacts(SupplierCard supplierCard) {
        supplierCard.printContacts();
    }

    public boolean removeContact_members(SupplierCard supplierCard, String pNum) {
//        Pair<Integer, String> temp = new Pair<>(supplierCard.getSupplier_number(), pNum);
        if (ContactMemberDAO.getInstance().getContactByPhoneNumber(pNum) == null) {
            return false;
        } else {
            ContactMember cm = ContactMemberDAO.getInstance().getContactByPhoneNumber(pNum);
            boolean ans =  supplierCard.removeContact_members(pNum);
            ContactMemberDAO.getInstance().Delete(cm);
            return ans;
        }
    }

    public void editContact_members(SupplierCard supplierCard, String pNum, int option, String x) {
        switch (option){
            case 1: {
                contactMemberDAO.UpdateContactName(supplierCard.getSupplier_number(), pNum, x);
                supplierCard.editContact_members(pNum,1,x);
                break;
            }
            case 2: {
                contactMemberDAO.UpdateContactEmail(supplierCard.getSupplier_number(), pNum, x);
                supplierCard.editContact_members(pNum,2,x);
                break;
            }

        }
        supplierCard.editContact_members(pNum, option, x);
    }

    public void printCategories(SupplierCard supplierCard) {
        supplierCard.printCategories();
    }

    public void EditCategories(SupplierCard supplierCard, String category, int option) {
        supplierCard.EditCategories(category, option);
    }



    /**
     * function prints the orders of specific supplier
     */
    public void PrintSupplierOrders(int id) {
        if (supplierDAO.getSupplier(id)==null) {
            System.out.println("This is not valid id!");
        } else {
            supplierDAO.getSupplier(id).PrintOrders();
        }
    }

    /**
     * function get Supplier
     * function prints all products of the supplier
     */
    public void printAllProducts(Supplier x) {
        if (x.getAgreement().getProductList().isEmpty()) {
            System.out.println("There is no products in the list");
        } else {
            System.out.println("---Product list---");
            for (SupplierProduct p : x.getAgreement().getProductList()) {
                p.printProductInfo();
            }
        }
    }
    public void PrintSupplierDetailes(int id) {
        supplierDAO.getSupplier(id);
        if (!supplierDAO.getSupplierHashMap().containsKey(id))
        {
            System.out.println("This is not valid id!");
        }
        else
        {
            supplierDAO.getSupplier(id).PrintSupplierDetailes();
        }
    }
//check if there is a key in the hash map for this product name
    public boolean isThisProductAlreadyInSystem(String productName) {
        supplierDAO.SelectAllSuppliers();
        if (supplierDAO.getSupplierByProductMap().containsKey(productName))
            return true;
        else
            return false;
    }

    public SupplierProduct getProduct(Agreement agreement, String productName) {
        return supplierProductDAO.getSupplierProductById(productName,agreement.getSupplier_number());
    }
    public void printProductInfo(SupplierProduct supplierProduct) {
        supplierProduct.printProductInfo();
    }

    public void printProductDiscounts(SupplierProduct supplierProduct) {
        supplierProduct.printProductDiscounts();
    }

    public void setUnit_price(SupplierProduct supplierProduct, double unit_price) {
        supplierProductDAO.UpdatePrice(supplierProduct.getSupplierID(),supplierProduct.getProduct_name(),unit_price);
        supplierProduct.setUnit_price(unit_price);
    }

    public void setAmount_available(SupplierProduct supplierProduct, int amount_available) {
        supplierProduct.setAmount_available(amount_available);
        supplierProductDAO.UpdateAmount(supplierProduct.getSupplierID(),supplierProduct.getProduct_name(),amount_available);


    }


    /**
     * function asks from user supplier id
     * function remove from system this supplier
     */
    public void removeSupplierByUser(int sup_id) {
        if(supplierDAO.getSupplier(sup_id)==null){
            System.out.println("This supplier is not supply products to Super Lee!");
        } else {
            if(isHasPeriodicOrders(sup_id))
                System.out.println("This supplier has previous commitments and therefore cannot be deleted !");
            else {
                removeSupplier(supplierDAO.getSupplier(sup_id));
                System.out.println("Supplier " + sup_id + " deleted successfully");
            }

        }
    }

    public boolean isHasPeriodicOrders(int supId)
    {
        Map<Integer,Order> orders = OrderDAO.getInstance().getAllOrdersByKind(2);
        if(orders == null){
            return false;
        }
            for (Order order : orders.values()) {
                if(order.getSupplier().getCard().getSupplier_number()==supId)
                    return true;
            }
        return false;
    }


    /**
     * function get Supplier
     * function remove the Supplier from all products
     */
    public void removeSupplier(Supplier s1) {
        supplierDAO.Delete(s1);
//        for (SupplierProduct p : s1.getAgreement().getProductList()) {
//            this.SupplierByProduct.get(p.getProduct_name()).remove(s1);
//            if (this.SupplierByProduct.get(p.getProduct_name()).isEmpty()) // if s1 is the only supplier of the product remove the product
//            {
//                this.SupplierByProduct.remove(p.getProduct_name());
//            }
//
//        }
//
//        this.suppliers_list.remove(s1.getCard().getSupplier_number()); // remove the supplier from supplier list
    }

    /**
     * function get String
     * return true or false if the string represent a valid int
     */
    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            int d = Integer.parseInt(strNum);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    /**
     * function get String
     * return true or false if the string represent a valid int
     */
    public boolean isExist(int supNum) {
        if(!(supplierDAO.getSupplier(supNum)==null))
        {
//        if(supplierDAO.getSupplierHashMap().containsKey(supNum)){
            return true;
        }
//        if (supply_manager.suppliers_list.containsKey(supNum))
//            return true;
        return false;
    }


    /**
     * function get int supplier id, name of product
     * the function remove the product from the supplier
     */
    public void deleteProductFromSupplier(int sup, String pro) {
        if (pro == "notExist") {
            return;
        }

        SupplierProduct sp = SupplierProductDAO.getInstance().getSupplierProductById(pro,sup);
        SupplierProductDAO.getInstance().Delete(sp);
        /////////////////////////////////////
        System.out.println("Product: " + pro + " was deleted from Supplier: " + sup + " Successfully!");


    }


    ////////////////////////////////////////////////////////////////////////
    /**the function get map of string representing products id, and their amount
     *the function take all the suppliers can supply products from this order and save their relevant products in a map
     * the function return map of supplier as key, and his relevant products as value
     */
    public Map<Supplier, Map<String,Integer>> fullProductslist(Map<String,Integer> ans1) {
        //save all the products provide by one supplier and their suppliers
        Map<Supplier,Map<String,Integer>> goodSup = new HashMap<>();
        //check for each product from the map
        for (Map.Entry<String, Integer> iter : ans1.entrySet()) {
            //find this product in our data
//            for (Map.Entry<String, LinkedList<Supplier>> iter2 : this.getSupplierByProduct().entrySet()) {
            for (Map.Entry<String, Map<Integer,Supplier>> iter2 : supplierDAO.getSupplierByProductMap().entrySet()) {
                //find match in our data
                if (iter.getKey().compareTo(iter2.getKey()) == 0) {
                    //check if there is supplier with enough amount
                    for(Supplier k : iter2.getValue().values())
                    {
                        //run all the supplier products
                        for (int p = 0; p < k.getAgreement().getProductList().size(); p++) {
                            //the correct product and enough amount
                            if ((k.getAgreement().getProductList().get(p).getProduct_name().compareTo(iter.getKey()) == 0) && (k.getAgreement().getProductList().get(p).getAmount_available() >= iter.getValue())) {
                                //if its good supplier and product and the supplier already in the map
                                if (goodSup.containsKey(k)) {
                                    goodSup.get(k).put(iter.getKey(), iter.getValue());
                                } else {
                                    Map<String, Integer> temp = new HashMap<>();
                                    temp.put(iter.getKey(), iter.getValue());
                                    goodSup.put(k, temp);
                                }
                            }
                        }
                    }
                }
            }
        }
        return goodSup;
    }


    /**the function get map of suppliers as key, and list of products and amount as value
     *the function calculate which of the suppliers contain the most products, and the cheapest onr
     * the function return the best current supplier
     */
    public Supplier bestSupplier(Map<Supplier,Map<String,Integer>> ans){
        //reset to the first arguments in the map
        int num_ofProduct =  ans.get(ans.keySet().toArray()[0]).size();
        Supplier s1 = (Supplier) ans.keySet().toArray()[0];
        double best = fullPricreOrder(s1, (Map<String, Integer>) ans.values().toArray()[0]);
        for(Map.Entry<Supplier,Map<String,Integer>> iter : ans.entrySet()) {
            //if there is more optional products and if it cheaper
            if (iter.getValue().size() > num_ofProduct) {
                num_ofProduct = iter.getValue().size();
                s1 = iter.getKey();
                best = fullPricreOrder(iter.getKey(), iter.getValue());
                //if they have the same amount of optional products
            } else if (iter.getValue().size() == num_ofProduct) {
                if (fullPricreOrder(iter.getKey(), iter.getValue()) < best) {
                    s1 = iter.getKey();
                    best = fullPricreOrder(iter.getKey(), iter.getValue());
                }
            }
        }
        return s1;
    }

    /**the function get supplier, and map of string and int
     *the function calculate the price for this supplier and map od products using the function productPriceBySupplier.
     * the function return the price it calculates by double
     */

    public double fullPricreOrder(Supplier supplier, Map<String,Integer> ans1){
        double total =0;
        int amount =0;
        for (Map.Entry<String, Integer> iter : ans1.entrySet()) {
            total += productPriceBySupplier(supplier, iter.getKey(), iter.getValue());
            amount+=iter.getValue();
        }
        double total_discount = 0;
        for(int i = 0; i< supplier.getAgreement().getTotal_orderDiscount().size(); i++){
            if(supplier.getAgreement().getTotal_orderDiscount().get(i).getAmountRange().getMin() <= amount && supplier.getAgreement().getTotal_orderDiscount().get(i).getAmountRange().getMax() >= amount){
                total_discount = supplier.getAgreement().getTotal_orderDiscount().get(i).getPercentage();
                break;
            }
        }
        return total*(1-(total_discount*0.01));
    }

    /**the function get supplier, string and int
     *the function calculate the price for this product and amount for this supplier
     * the function return the price it calculates by double
     */
    public double productPriceBySupplier(Supplier supplier, String product_name, int amount){
        SupplierProduct p1 = null;
        int id = supplier.getCard().getSupplier_number();
//        for(int i = 0; i < supplier.getAgreement().getProductList().size(); i++) {
        for(int i = 0; i < SupplierProductDAO.getInstance().getProductsBySupplier(id).size(); i++) {
//            if (supplier.getAgreement().getProductList().get(i).getProduct_name().compareTo(product_name) == 0) {
            if (SupplierProductDAO.getInstance().getProductsBySupplier(id).get(i).getProduct_name().compareTo(product_name) == 0) {
                p1 = SupplierProductDAO.getInstance().getProductsBySupplier(id).get(i);
                break;
            }
        }
        double discount = 0;
        for(int j =0; j < p1.getDiscounts().size(); j++){
            if(p1.getDiscounts().get(j).getAmountRange().getMax()>= amount && p1.getDiscounts().get(j).getAmountRange().getMin()<= amount){
                discount = p1.getDiscounts().get(j).getPercentage();
                break;
            }
        }
        double unitPrice = p1.getUnit_price();
        return amount*unitPrice*(1-(discount*0.01));

    }

    /**the function get supplier, and map of string and int
     *the function create for each key value in the new object and return it.
     */

    public Map<SupplierProduct,Integer> fromStringsToProducts(Supplier supplier, Map<String,Integer> ans1){
        Map<SupplierProduct,Integer> ans = new HashMap<>();
        for(Map.Entry<String, Integer> iter : ans1.entrySet()){
            ans.put(fromStringToProduct(supplier, iter.getKey()), iter.getValue());
        }
        return ans;
    }
    /**the function get supplier, string and int
     *the function create new object and return it .
     */
    public SupplierProduct fromStringToProduct(Supplier supplier, String name){
//        SupplierProduct p2 = null;
//        double dis =0;
//        for(int i = 0; i< supplier.getAgreement().getProductList().size(); i++) {
//            if (supplier.getAgreement().getProductList().get(i).getProduct_name().compareTo(name) == 0) {
//                p2 = supplier.getAgreement().getProductList().get(i);
//            }
//        }
//        return p2;
        return SupplierProductDAO.getInstance().getSupplierProductById(name,supplier.getCard().getSupplier_number());
    }

    /**the function get map of string and int
     *the function use all the functions above to crate new map representing preparation to orders.
     * the function works iterative until the input map is empty, and calculate every time the best current selection.
     */

    public  Map<Supplier,Map<SupplierProduct,Integer>> allFullProducts(Map<String,Integer> ans) {
        Map<Supplier, Map<SupplierProduct, Integer>> allFullProducts = new HashMap<>();
        while (!ans.isEmpty()) {
            //save all the suppliers and the current products
            Map<Supplier, Map<String, Integer>> temp1 = fullProductslist(ans);
            //chose the supplier with most products. if there is more than one, chose the cheapest.
            Supplier s1 = bestSupplier(temp1);
            //get his list of product order details
            Map<SupplierProduct, Integer> mapProAmount1 = fromStringsToProducts(s1, temp1.get(s1));
            //add the supplier and his list to the map
            allFullProducts.put(s1, mapProAmount1);
            //remove the product from ans - run all the product the chosen supplier and remove them
            for (Map.Entry<String, Integer> iter : temp1.get(s1).entrySet()) {
                ans.remove(iter.getKey());
            }
        }
        return allFullProducts;
    }

    /**the function get 2 maps, one is the products cant provided by one supplier and one is the output above.
     *the function use iterative the next function and split the product to suppliers.
     * the function return map of suppliers as keys, and list of products to order by it as their value.
     */
    public Map<Supplier,Map<SupplierProduct,Integer>> allHalfProduct(Map<String,Integer> products, Map<Supplier,Map<SupplierProduct,Integer>> ans){

        for (Map.Entry<String, Integer> iter : products.entrySet()){
            if(halfProduct(iter.getKey(), iter.getValue(), ans)==null){
                System.out.println("The order canceled! the product "+ iter.getKey() + " cant provide with the amount of "+ iter.getValue() + "!.");
                return null;
            }
        }
        return ans;

    }

    /**the function get map, and string and int representing product and amount.
     *the function act iterative and split the product to minimum suppliers as possible.
     * the function return map of suppliers as keys, and list of products to order by it as their value.
     */
    public Map<Supplier,Map<SupplierProduct,Integer>> halfProduct(String name, int amount, Map<Supplier,Map<SupplierProduct,Integer>> ans) {
        if (!isEnough(name, amount)) {
            return null;
        } else {
            while (amount != 0) {

//                LinkedList<Supplier> suppliers = this.getSupplierByProduct().get(name);
                List<Supplier> suppliers= new ArrayList<>(supplierDAO.getSupplierByProductMap().get(name).values());
                Supplier s1 = suppliers.get(0);
                int max_amount = 0;
                for (int i = 0; i < suppliers.get(0).getAgreement().getProductList().size(); i++) {
                    if (suppliers.get(0).getAgreement().getProductList().get(i).getProduct_name().compareTo(name) == 0) {
                        max_amount = suppliers.get(0).getAgreement().getProductList().get(i).getAmount_available();
                    }
                }
                //find the current best supplier that can provide most of the product
                for (Supplier supplier : suppliers) {
                    for (int i = 0; i < supplier.getAgreement().getProductList().size(); i++) {
                        if (supplier.getAgreement().getProductList().get(i).getProduct_name().compareTo(name) == 0) {
                            if (supplier.getAgreement().getProductList().get(i).getAmount_available() > max_amount) {
                                s1 = supplier;
                                max_amount = supplier.getAgreement().getProductList().get(i).getAmount_available();
                            }
                            //if both can provide the same amount check who is cheaper
                            else if (supplier.getAgreement().getProductList().get(i).getAmount_available() == max_amount) {
                                if (productPriceBySupplier(supplier, name, amount) < productPriceBySupplier(s1, name, amount)) {
                                    s1 = supplier;
                                    max_amount = supplier.getAgreement().getProductList().get(i).getAmount_available();
                                }

                            }
                        }
                    }
                }
                //now we have the supplier can provide most ot the product in this time
                //check if we sub from the amount to much - for not provide more than requested
                if(max_amount > amount){
                    max_amount = amount;
                }
                //create product order detail from the product
                SupplierProduct p1 = fromStringToProduct(s1, name);
                //check if the supplier already in the list if yes just add the product to his list
                if(ans.containsKey(s1)){
                    ans.get(s1).put(p1,max_amount);
                }
                //if the supplier is not exist in the list
                else{
                    Map<SupplierProduct,Integer> temp = new HashMap<>();
                    temp.put(p1, max_amount);
                    ans.put(s1,temp);
                }
                amount = amount-max_amount;
                suppliers.remove(s1);
            }
            return ans;
        }
    }


    /**the function get  string and int representing product and amount.
     *the function check if the product can be provided in some way.
     * the function return boolean value.
     */
    public boolean isEnough(String name, int amount){
        supplierDAO.SelectAllSuppliers();
        List<Supplier> suppliers= new ArrayList<>(supplierDAO.getSupplierByProductMap().get(name).values());
        boolean success = false;
        int count =0;
        //for each supplier check the amount he can provide from this product
        for (Supplier s1 : suppliers) {
//            for (int j = 0; j < s1.getAgreement().getProductList().size(); j++) {
            LinkedList<SupplierProduct> splist = SupplierProductDAO.getInstance().getProductsBySupplier(s1.getCard().getSupplier_number());
            for (int j = 0; j < splist.size(); j++) {
                SupplierProduct p1 = splist.get(j);
                if (p1.getProduct_name().compareTo(name) == 0) {
                    count += p1.getAmount_available();
                }
            }
        }
        if(count >= amount){
            success =true;
        }
        return success;
    }
    /**the function get  string and int representing product and amount.
     *the function check if the product can be provided in by one supplier.
     * the function return boolean value.
     */
    public boolean oneSupplier(String name,int amount){
//        supplierDAO.SelectAllSuppliers();
        List<Supplier> suppliers= new ArrayList<>(supplierDAO.getSupplierByProductMap().get(name).values());
        boolean success = false;
        //for each supplier check the amount he can provide from this product
        for (Supplier s1 : suppliers) {
            //don't count this supplier because he is not valid case
            LinkedList<SupplierProduct> splist = SupplierProductDAO.getInstance().getProductsBySupplier(s1.getCard().getSupplier_number());
//            for (int j = 0; j < s1.getAgreement().getProductList().size(); j++) {
            for (int j = 0; j < splist.size(); j++) {
                SupplierProduct p1 = splist.get(j);
                if ((p1.getProduct_name().compareTo(name) == 0) && (p1.getAmount_available() >= amount)) {
                    success = true;
                    break;
                }
            }
        }
        return success;
    }

    public void deleteAllData()
    {
        List<Supplier> allsup= SupplierDAO.getInstance().SelectAllSuppliers();
        for(Supplier supplier : allsup)
        {
            supplierDAO.Delete(supplier);
        }
    }
    public String[] productsNamesOfSupplier(int id)
    {
        Supplier supplier= this.getSupplier(id);
        LinkedList<SupplierProduct> productList= supplier.getAgreement().getProductList();
        String[] ans= new String[productList.size()];
        int index=0;
        for(SupplierProduct p: productList)
        {
            ans[index]=p.getProduct_name();
            index++;
        }
        return ans;
    }

}




