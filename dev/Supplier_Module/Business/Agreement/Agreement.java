package Supplier_Module.Business.Agreement;
import Supplier_Module.Business.Agreement.MethodSupply.MethodSupply;
import Supplier_Module.Business.Defs.EOM;
import Supplier_Module.Business.Discount.PrecentageDiscount;

import java.util.LinkedList;
import java.util.Scanner;

public class Agreement {
    private LinkedList<SupplierProduct> supplierProductList;

    private LinkedList<PrecentageDiscount> total_orderDiscount;
    private MethodSupply methodSupply;
    private EOM eom;
    private int supplier_number;


    private Scanner input= new Scanner(System.in);


    /**constructor
     *
     */
    public Agreement(int supplier_number) {
        this.supplierProductList = new LinkedList<>();
        this.total_orderDiscount= new LinkedList<>();
        this.methodSupply=null;
        this.eom=null;
        this.supplier_number=supplier_number;

    }
    public Agreement(int supplier_number, MethodSupply methodSupply1){
        this.supplierProductList = new LinkedList<>();
        this.total_orderDiscount= new LinkedList<>();
        this.methodSupply=methodSupply1;
        this.eom = EOM.Plus_0;
        this.supplier_number=supplier_number;
    }


    /**constructor
     *
     */
    public Agreement(LinkedList<SupplierProduct> supplierProductList, LinkedList<PrecentageDiscount> total_orderDiscount, MethodSupply methodSupply, EOM eom, int supplier_number) {
        this.supplierProductList = supplierProductList;
        this.total_orderDiscount = total_orderDiscount;
        this.methodSupply = methodSupply;
        this.eom = eom;
        this.supplier_number=supplier_number;
    }

    /**
     * Getter and Setter
     */
    public int getSupplier_number() {
        return supplier_number;
    }

    public MethodSupply getMethodSupply() {
        return methodSupply;
    }

    public void setMethodSupply(MethodSupply methodSupply) {
        this.methodSupply = methodSupply;
    }
    public LinkedList<SupplierProduct> getProductList() {
        return supplierProductList;
    }

    public LinkedList<PrecentageDiscount> getTotal_orderDiscount() {
        return total_orderDiscount;
    }

    public EOM getEom() {
        return eom;
    }

    public void setEom(int x) {
        if(x==1)
        {
            this.eom=EOM.Plus_0;
        }
        else if (x==2)
        {
            this.eom=EOM.Plus_30;

        }
        else
            this.eom=EOM.Plus_60;

    }

    public SupplierProduct getProduct(String productName)
    {
        for(SupplierProduct i:this.getProductList())
        {
            if(i.getProduct_name().equals(productName))
            {
                return i;
            }
        }
        return null;
    }
    public void setTotal_orderDiscount(LinkedList<PrecentageDiscount> total_orderDiscount) {
        this.total_orderDiscount = total_orderDiscount;
    }

    /**function get Product argument and add it to this Agreement
     * update the agreement with the new product
     */
    public void addProductToAgreement(SupplierProduct x)
    {
        supplierProductList.add(x);
        System.out.println("Product added successfully\n");
    }

    public void addTotal_orderDiscount (PrecentageDiscount x)
    {
        this.total_orderDiscount.add(x);
    }



    public void addProductMenu(SupplierProduct x)
    {
        if(isExist(x)==false)
            addProductToAgreement(x);
        else
        {
            System.out.println("This product already exist");
        }
    }



    /**function get product name (String) and check if product name is already exist in the system
     * @return true or false
     */
    public boolean isExistName(String x)
    {
        for (SupplierProduct p : supplierProductList)
        {
            if (p.getProduct_name().equals(x))
            {
                return true;
            }
        }
        return false;
    }



    /**function get product (Product) and check if the product is already exist in the system
     * @return true or false
     */
    public boolean isExist (SupplierProduct x)
    {
        boolean isExist=false;
        for (SupplierProduct p : supplierProductList) {
            if (p.getProduct_name().equals(x.getProduct_name())) {
                isExist=true;
            }
        }
        return isExist;
    }


    /** function get a string, name of the product and remove it from agreement if it exists in the system
     * @return the name of the product that has been removed, or "notExist" if the product is not exists
     */
    public String removeProductFromAgreement(String name)
    {
        if(isExistName(name)==true)
        {
            for (SupplierProduct p : supplierProductList) {
                if (p.getProduct_name().equals(name))
                {
                    this.supplierProductList.remove(p);
                    break;
                }
            }
            return name;
        }
        else
        {
            System.out.println(name+" is not exist in the product list");
            return "notExist";
        }
    }


    /**
     * function that print the total discounts in the agreement
     */
    public void printTotalDiscounts()
    {
        System.out.println("---Total Discounts---");
        if(total_orderDiscount.isEmpty())
            System.out.println("Total discounts list is empty");
        else
        {
            for(PrecentageDiscount p:total_orderDiscount)
            {
                p.printDiscount();
            }
            System.out.println();
        }
    }

    public void printAgreement(){
        System.out.println("----Agreement---");
        System.out.println("EOM: " + this.eom);
        System.out.println("Supply Method: " + this.methodSupply.methodType());
        System.out.println("---Products---");
        for (SupplierProduct supplierProduct : this.supplierProductList) {
            supplierProduct.printProduct();
        }
        printTotalDiscounts();
    }


    public String allDiscountToString(){
        String ans = "";
        for(PrecentageDiscount discount : this.getTotal_orderDiscount()){
            ans+= discount.toString() + ",";
        }
        return ans;
    }
}