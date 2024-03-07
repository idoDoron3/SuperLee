package Supplier_Module.Presentation;
import Supplier_Module.Business.Agreement.Agreement;
import Supplier_Module.Business.Agreement.MethodSupply.ByFixedDays;
import Supplier_Module.Business.Agreement.MethodSupply.BySuperLee;
import Supplier_Module.Business.Agreement.MethodSupply.BySupplyDays;
import Supplier_Module.Business.Agreement.MethodSupply.MethodSupply;
import Supplier_Module.Business.Agreement.SupplierProduct;
import Supplier_Module.Business.Card.ContactMember;
import Supplier_Module.Business.Card.SupplierCard;
import Supplier_Module.Business.Defs.EOM;
import Supplier_Module.Business.Defs.Payment_method;
import Supplier_Module.Business.Discount.PrecentageDiscount;
import Supplier_Module.Business.Discount.Range;
import Supplier_Module.Business.Managers.SupplyManager;
import Supplier_Module.Business.Supplier;


import java.util.LinkedList;

public class  Data {

    private static Data loadData = null;
    private SupplyManager sup_manager;

    /**constructor
     * private constractor for singleton
     */
    private Data() {
        this.sup_manager=SupplyManager.getSupply_manager();
    }
    public static Data getLoadData() {
        if (loadData == null) {
            loadData = new Data();
        }
        return loadData;
    }

    /**
     *load Data Base
     */
    public void loadData()
    {
        //-------------------------------supplier1-------------------------------------------------
        //supplierCard
        //contacts
        ContactMember c1_1=new ContactMember("0521234567","Moshe","Moshe123@gmail.com",10);
        ContactMember c1_2=new ContactMember("0522255889","Avraham","Avraham23@gmail.com",10);
        LinkedList<ContactMember> contactList1=new LinkedList<>();
        contactList1.add(c1_1);
        contactList1.add(c1_2);

        //product_categories
        LinkedList<String> categoriesList1=new LinkedList<>();
        categoriesList1.add("Diary");
        categoriesList1.add("Meat");

        SupplierCard sCard1=new SupplierCard("Matan",10,100,59833,"Rehovot", Payment_method.cash,contactList1,categoriesList1);

        //Agreement
        LinkedList<SupplierProduct> supplierProductList1 =new LinkedList<>();
        LinkedList<PrecentageDiscount> total_orderDiscount1=new LinkedList<>();

        //products
        //p1
        LinkedList<PrecentageDiscount> p1_1_discounts=new LinkedList<>();

        Range range1_1_1=new Range(0,100);
        Range range1_1_2=new Range(101,200);
        Range range1_1_3=new Range(201,300);
        Range range1_1_4=new Range(301,500);

        PrecentageDiscount discount1_1_1=new PrecentageDiscount(range1_1_1,5);
        PrecentageDiscount discount1_1_2=new PrecentageDiscount(range1_1_2,7);
        PrecentageDiscount discount1_1_3=new PrecentageDiscount(range1_1_3,10);
        PrecentageDiscount discount1_1_4=new PrecentageDiscount(range1_1_4,15);

        p1_1_discounts.add(discount1_1_1);
        p1_1_discounts.add(discount1_1_2);
        p1_1_discounts.add(discount1_1_3);
        p1_1_discounts.add(discount1_1_4);

        SupplierProduct p1=new SupplierProduct("Burgers 1.0 kg",1000,1,70,500,p1_1_discounts,10);

        //p2

        LinkedList<PrecentageDiscount> p1_2_discounts=new LinkedList<>();

        Range range1_2_1=new Range(0,300);
        Range range1_2_2=new Range(301,600);
        Range range1_2_3=new Range(601,1000);

        PrecentageDiscount discount1_2_1=new PrecentageDiscount(range1_2_1,0);
        PrecentageDiscount discount1_2_2=new PrecentageDiscount(range1_2_2,10);
        PrecentageDiscount discount1_2_3=new PrecentageDiscount(range1_2_3,15);

        p1_2_discounts.add(discount1_2_1);
        p1_2_discounts.add(discount1_2_2);
        p1_2_discounts.add(discount1_2_3);

        SupplierProduct p2=new SupplierProduct("Apples 500.0 gr",1001,0.5,20,1000,p1_2_discounts,10);

        supplierProductList1.add(p1);
        supplierProductList1.add(p2);

        Range rangeTotal1=new Range(0,700);
        Range rangeTotal2=new Range(701,1000);
        Range rangeTotal3=new Range(1001,3000);

        PrecentageDiscount discountTotal1_1=new PrecentageDiscount(rangeTotal1,5);
        PrecentageDiscount discountTotal1_2=new PrecentageDiscount(rangeTotal2,8);
        PrecentageDiscount discountTotal1_3=new PrecentageDiscount(rangeTotal3,12);

        total_orderDiscount1.add(discountTotal1_1);
        total_orderDiscount1.add(discountTotal1_2);
        total_orderDiscount1.add(discountTotal1_3);

        MethodSupply methoodSupply1= new BySupplyDays("By Supply Days", 2);

        Agreement agreement1=new Agreement(supplierProductList1,total_orderDiscount1,methoodSupply1, EOM.Plus_30, sCard1.getSupplier_number());

        Supplier s1= this.sup_manager.CreateSupplier(sCard1,agreement1); // The supplier will add to the supplier list and to the list of products


        //-------------------------------supplier2-------------------------------------------------
        //supplierCard
        //contacts

        ContactMember c2_1=new ContactMember("0521236357","Idan","Idanosh19@gmail.com",11);
        LinkedList<ContactMember> contactList2=new LinkedList<>();
        contactList2.add(c2_1);

        //product_categories
        LinkedList<String> categoriesList2=new LinkedList<>();
        categoriesList2.add("Cereal");
        categoriesList2.add("Snacks");

        SupplierCard sCard2=new SupplierCard("Ido",11,105,68323,"Ness-Ziona",Payment_method.bit,contactList2,categoriesList2);

        //Agreement
        LinkedList<SupplierProduct> supplierProductList2 =new LinkedList<>();
        LinkedList<PrecentageDiscount> total_orderDiscount2=new LinkedList<>();

        //products
        //p2_1 p(supplier)_(productIndex)
        LinkedList<PrecentageDiscount> p2_1_discounts=new LinkedList<>();

        Range range2_1_1=new Range(0,500);
        Range range2_1_2=new Range(501,1000);
        Range range2_1_3=new Range(1001,1500);

        //discount(supplier)_(product)_(index)
        PrecentageDiscount discount2_1_1=new PrecentageDiscount(range2_1_1,2);
        PrecentageDiscount discount2_1_2=new PrecentageDiscount(range2_1_2,5);
        PrecentageDiscount discount2_1_3=new PrecentageDiscount(range2_1_3,7);


        p2_1_discounts.add(discount2_1_1);
        p2_1_discounts.add(discount2_1_2);
        p2_1_discounts.add(discount2_1_3);

        SupplierProduct p2_1=new SupplierProduct("Milk 3% 1.0 l",2000,1.1,5,1500,p2_1_discounts,11);

        //p2_2

        LinkedList<PrecentageDiscount> p2_2_discounts=new LinkedList<>();

        Range range2_2_1=new Range(0,400);
        Range range2_2_2=new Range(401,1000);
        Range range2_2_3=new Range(1001,1600);
        Range range2_2_4=new Range(1601,2000);


        //discount(supplier)_(product)_(index)
        PrecentageDiscount discount2_2_1=new PrecentageDiscount(range2_2_1,2);
        PrecentageDiscount discount2_2_2=new PrecentageDiscount(range2_2_2,5);
        PrecentageDiscount discount2_2_3=new PrecentageDiscount(range2_2_3,7);
        PrecentageDiscount discount2_2_4=new PrecentageDiscount(range2_2_4,7);


        p2_2_discounts.add(discount2_2_1);
        p2_2_discounts.add(discount2_2_2);
        p2_2_discounts.add(discount2_2_3);
        p2_2_discounts.add(discount2_2_4);

        SupplierProduct p2_2=new SupplierProduct("Yogurt 250.0 ml",2001,0.25,7,2000,p2_2_discounts,11);

        supplierProductList2.add(p2_1);
        supplierProductList2.add(p2_2);

        Range rangeTotal2_1=new Range(0,1200);
        Range rangeTotal2_2=new Range(1201,1600);
        Range rangeTotal2_3=new Range(1601,3500);

        PrecentageDiscount discountTotal2_1=new PrecentageDiscount(rangeTotal2_1,5);
        PrecentageDiscount discountTotal2_2=new PrecentageDiscount(rangeTotal2_2,8);
        PrecentageDiscount discountTotal2_3=new PrecentageDiscount(rangeTotal2_3,12);

        total_orderDiscount2.add(discountTotal2_1);
        total_orderDiscount2.add(discountTotal2_2);
        total_orderDiscount2.add(discountTotal2_3);

        int[] days=new int[]{0,1,0,0,0,1,0};
        MethodSupply methoodSupply2= new ByFixedDays("By Fixed Days",days );

        Agreement agreement2=new Agreement(supplierProductList2,total_orderDiscount2,methoodSupply2,EOM.Plus_0, sCard2.getSupplier_number());

        Supplier s2=s1= this.sup_manager.CreateSupplier(sCard2,agreement2); // The supplier will add to the supplier list and to the list of products

        //-------------------------------supplier3-------------------------------------------------
        //supplierCard
        //contacts

        ContactMember c3_1=new ContactMember("0521298523","Lior","Lior@gmail.com",12);
        LinkedList<ContactMember> contactList3=new LinkedList<>();
        contactList3.add(c3_1);

        //product_categories
        LinkedList<String> categoriesList3=new LinkedList<>();
        categoriesList3.add("Cereal");
        categoriesList3.add("Milk");

        SupplierCard sCard3=new SupplierCard("Kim",12,189,37519,"Yiron",Payment_method.bit,contactList3,categoriesList3);

        //Agreement
        LinkedList<SupplierProduct> supplierProductList3 =new LinkedList<>();
        LinkedList<PrecentageDiscount> total_orderDiscount3=new LinkedList<>();

        //products
        //p3_1 p(supplier)_(productIndex)
        LinkedList<PrecentageDiscount> p3_1_discounts=new LinkedList<>();

        Range range3_1_1=new Range(0,500);
        Range range3_1_2=new Range(501,700);
        Range range3_1_3=new Range(701,1000);

        //discount(supplier)_(product)_(index)
        PrecentageDiscount discount3_1_1=new PrecentageDiscount(range3_1_1,3);
        PrecentageDiscount discount3_1_2=new PrecentageDiscount(range3_1_2,6);
        PrecentageDiscount discount3_1_3=new PrecentageDiscount(range3_1_3,9);


        p3_1_discounts.add(discount3_1_1);
        p3_1_discounts.add(discount3_1_2);
        p3_1_discounts.add(discount3_1_3);

        SupplierProduct p3_1=new SupplierProduct("Milk 3% 1.0 l",3000,1.1,6,1000,p3_1_discounts,12);

        //p3_2

        LinkedList<PrecentageDiscount> p3_2_discounts=new LinkedList<>();

        Range range3_2_1=new Range(0,400);
        Range range3_2_2=new Range(401,1000);
        Range range3_2_3=new Range(1001,1600);
        Range range3_2_4=new Range(1601,2000);


        //discount(supplier)_(product)_(index)
        PrecentageDiscount discount3_2_1=new PrecentageDiscount(range3_2_1,2);
        PrecentageDiscount discount3_2_2=new PrecentageDiscount(range3_2_2,3);
        PrecentageDiscount discount3_2_3=new PrecentageDiscount(range3_2_3,6);
        PrecentageDiscount discount3_2_4=new PrecentageDiscount(range3_2_4,8);


        p3_2_discounts.add(discount3_2_1);
        p3_2_discounts.add(discount3_2_2);
        p3_2_discounts.add(discount3_2_3);
        p3_2_discounts.add(discount3_2_4);

        SupplierProduct p3_2=new SupplierProduct("Yogurt 250.0 ml",2001,0.25,6,2000,p3_2_discounts,12);

        supplierProductList3.add(p3_1);
        supplierProductList3.add(p3_2);

        Range rangeTotal3_1=new Range(0,1200);
        Range rangeTotal3_2=new Range(1201,1600);
        Range rangeTotal3_3=new Range(1601,3500);

        PrecentageDiscount discountTotal3_1=new PrecentageDiscount(rangeTotal3_1,5);
        PrecentageDiscount discountTotal3_2=new PrecentageDiscount(rangeTotal3_2,8);
        PrecentageDiscount discountTotal3_3=new PrecentageDiscount(rangeTotal3_3,12);

        total_orderDiscount3.add(discountTotal3_1);
        total_orderDiscount3.add(discountTotal3_2);
        total_orderDiscount3.add(discountTotal3_3);

        MethodSupply methoodSupply3= new BySuperLee("By Supper Lee" );

        Agreement agreement3=new Agreement(supplierProductList3,total_orderDiscount3,methoodSupply3,EOM.Plus_0, sCard3.getSupplier_number());

        Supplier s3 = this.sup_manager.CreateSupplier(sCard3,agreement3); // The supplier will add to the supplier list and to the list of products
/////////////////

        //-------------------------------supplier4-------------------------------------------------
        //supplierCard
        //contacts

        ContactMember c4_1=new ContactMember("0521123456","Lidor","Lidori@gmail.com",13);
        LinkedList<ContactMember> contactList4=new LinkedList<>();
        contactList4.add(c4_1);

        //product_categories
        LinkedList<String> categoriesList4=new LinkedList<>();
        categoriesList4.add("Cereal");
        categoriesList4.add("Bread");

        SupplierCard sCard4=new SupplierCard("Yaron",13,953,96453,"Beer-Sheva",Payment_method.bit,contactList4,categoriesList4);

        //Agreement
        LinkedList<SupplierProduct> supplierProductList4 =new LinkedList<>();
        LinkedList<PrecentageDiscount> total_orderDiscount4=new LinkedList<>();

        //products
        //p4_1 p(supplier)_(productIndex)
        LinkedList<PrecentageDiscount> p4_1_discounts=new LinkedList<>();

        Range range4_1_1=new Range(0,600);
        Range range4_1_2=new Range(601,1000);
        Range range4_1_3=new Range(1001,1200);

        //discount(supplier)_(product)_(index)
        PrecentageDiscount discount4_1_1=new PrecentageDiscount(range4_1_1,2);
        PrecentageDiscount discount4_1_2=new PrecentageDiscount(range4_1_2,8);
        PrecentageDiscount discount4_1_3=new PrecentageDiscount(range4_1_3,13);


        p4_1_discounts.add(discount4_1_1);
        p4_1_discounts.add(discount4_1_2);
        p4_1_discounts.add(discount4_1_3);

        SupplierProduct p4_1=new SupplierProduct("Oranges 1.0 kg",4000,1,15,1200,p4_1_discounts,13);

        //p4_2

        LinkedList<PrecentageDiscount> p4_2_discounts=new LinkedList<>();

        Range range4_2_1=new Range(0,350);
        Range range4_2_2=new Range(351,780);
        Range range4_2_3=new Range(781,1200);
        Range range4_2_4=new Range(1201,1500);


        //discount(supplier)_(product)_(index)
        PrecentageDiscount discount4_2_1=new PrecentageDiscount(range4_2_1,4);
        PrecentageDiscount discount4_2_2=new PrecentageDiscount(range4_2_2,5);
        PrecentageDiscount discount4_2_3=new PrecentageDiscount(range4_2_3,6);
        PrecentageDiscount discount4_2_4=new PrecentageDiscount(range4_2_4,7);


        p4_2_discounts.add(discount4_2_1);
        p4_2_discounts.add(discount4_2_2);
        p4_2_discounts.add(discount4_2_3);
        p4_2_discounts.add(discount4_2_4);

        SupplierProduct p4_2=new SupplierProduct("Milk 3% 1.0 l",4001,1.1,7,1500,p4_2_discounts,13);

        //p4_3 p(supplier)_(productIndex)
        LinkedList<PrecentageDiscount> p4_3_discounts=new LinkedList<>();

        Range range4_3_1=new Range(0,600);
        Range range4_3_2=new Range(601,1000);
        Range range4_3_3=new Range(1001,1200);

        //discount(supplier)_(product)_(index)
        PrecentageDiscount discount4_3_1=new PrecentageDiscount(range4_3_1,2);
        PrecentageDiscount discount4_3_2=new PrecentageDiscount(range4_3_2,8);
        PrecentageDiscount discount4_3_3=new PrecentageDiscount(range4_3_3,13);


        p4_3_discounts.add(discount4_3_1);
        p4_3_discounts.add(discount4_3_2);
        p4_3_discounts.add(discount4_3_3);

        SupplierProduct p4_3=new SupplierProduct("Yogurt 250.0 ml",4002,0.25,6.8,1200,p4_3_discounts,13);
//
        supplierProductList4.add(p4_1);
        supplierProductList4.add(p4_2);
        supplierProductList4.add(p4_3);


        Range rangeTotal4_1=new Range(0,1200);
        Range rangeTotal4_2=new Range(1201,1600);
        Range rangeTotal4_3=new Range(1601,3500);

        PrecentageDiscount discountTotal4_1=new PrecentageDiscount(rangeTotal4_1,4);
        PrecentageDiscount discountTotal4_2=new PrecentageDiscount(rangeTotal4_2,6);
        PrecentageDiscount discountTotal4_3=new PrecentageDiscount(rangeTotal4_3,13);

        total_orderDiscount4.add(discountTotal4_1);
        total_orderDiscount4.add(discountTotal4_2);
        total_orderDiscount4.add(discountTotal4_3);

        MethodSupply methoodSupply4= new BySuperLee("By Supper Lee" );

        Agreement agreement4=new Agreement(supplierProductList4,total_orderDiscount4,methoodSupply4,EOM.Plus_30,sCard4.getSupplier_number());

        Supplier s4=this.sup_manager.CreateSupplier(sCard4,agreement4); // The supplier will add to the supplier list and to the list of products
/////////////////
        //-------------------------------supplier5-------------------------------------------------
        //supplierCard
        //contacts

        ContactMember c5_1=new ContactMember("0529512456","Aharon","Aharon357@gmail.com",14);
        ContactMember c5_2=new ContactMember("0529826456","Yossi","Yossi7@gmail.com",14);
        LinkedList<ContactMember> contactList5=new LinkedList<>();
        contactList5.add(c5_1);
        contactList5.add(c5_2);


        //product_categories
        LinkedList<String> categoriesList5 =new LinkedList<>();
        categoriesList5.add("Cleaning");
        categoriesList5.add("Drink");

        SupplierCard sCard5=new SupplierCard("Don",14,933,92763,"Haifa",Payment_method.bit,contactList5,categoriesList5);

        //Agreement
        LinkedList<SupplierProduct> supplierProductList5 =new LinkedList<>();
        LinkedList<PrecentageDiscount> total_orderDiscount5 =new LinkedList<>();

        //products
        //p4_1 p(supplier)_(productIndex)
        LinkedList<PrecentageDiscount> p5_1_discounts=new LinkedList<>();

        Range range5_1_1=new Range(0,800);
        Range range5_1_2=new Range(801,1100);
        Range range5_1_3=new Range(1101,1200);

        //discount(supplier)_(product)_(index)
        PrecentageDiscount discount5_1_1=new PrecentageDiscount(range5_1_1,0);
        PrecentageDiscount discount5_1_2=new PrecentageDiscount(range5_1_2,3);
        PrecentageDiscount discount5_1_3=new PrecentageDiscount(range5_1_3,7);


        p5_1_discounts.add(discount5_1_1);
        p5_1_discounts.add(discount5_1_2);
        p5_1_discounts.add(discount5_1_3);

        SupplierProduct p5_1=new SupplierProduct("Beef 500.0 gr",5000,0.5,35,1200,p5_1_discounts,14);

        //p4_2

        LinkedList<PrecentageDiscount> p5_2_discounts=new LinkedList<>();

        Range range5_2_1=new Range(0,960);
        Range range5_2_2=new Range(961,1200);
        Range range5_2_3=new Range(1201,1400);
        Range range5_2_4=new Range(1401,1500);


        //discount(supplier)_(product)_(index)
        PrecentageDiscount discount5_2_1=new PrecentageDiscount(range5_2_1,4);
        PrecentageDiscount discount5_2_2=new PrecentageDiscount(range5_2_2,5);
        PrecentageDiscount discount5_2_3=new PrecentageDiscount(range5_2_3,6);
        PrecentageDiscount discount5_2_4=new PrecentageDiscount(range5_2_4,7);


        p5_2_discounts.add(discount5_2_1);
        p5_2_discounts.add(discount5_2_2);
        p5_2_discounts.add(discount5_2_3);
        p5_2_discounts.add(discount5_2_4);

        SupplierProduct p5_2=new SupplierProduct("Cheese 200.0 gr",5001,0.2,20,1500,p5_2_discounts,14);

        //p4_3 p(supplier)_(productIndex)
        LinkedList<PrecentageDiscount> p5_3_discounts=new LinkedList<>();

        Range range5_3_1=new Range(0,600);
        Range range5_3_2=new Range(601,1000);
        Range range5_3_3=new Range(1001,1200);

        //discount(supplier)_(product)_(index)
        PrecentageDiscount discount5_3_1=new PrecentageDiscount(range5_3_1,2);
        PrecentageDiscount discount5_3_2=new PrecentageDiscount(range5_3_2,8);
        PrecentageDiscount discount5_3_3=new PrecentageDiscount(range5_3_3,13);


        p5_3_discounts.add(discount5_3_1);
        p5_3_discounts.add(discount5_3_2);
        p5_3_discounts.add(discount5_3_3);

        SupplierProduct p5_3=new SupplierProduct("Tomatoes 1.0 kg",5002,1,6.8,1200,p5_3_discounts,14);
//
        supplierProductList5.add(p5_1);
        supplierProductList5.add(p5_2);
        supplierProductList5.add(p5_3);


        Range rangeTotal5_1=new Range(0,1200);
        Range rangeTotal5_2=new Range(1201,1600);
        Range rangeTotal5_3=new Range(1601,3500);

        PrecentageDiscount discountTotal5_1=new PrecentageDiscount(rangeTotal4_1,4);
        PrecentageDiscount discountTotal5_2=new PrecentageDiscount(rangeTotal4_2,6);
        PrecentageDiscount discountTotal5_3=new PrecentageDiscount(rangeTotal4_3,13);

        total_orderDiscount5.add(discountTotal5_1);
        total_orderDiscount5.add(discountTotal5_2);
        total_orderDiscount5.add(discountTotal5_3);

        MethodSupply methoodSupply5= new BySuperLee("By Supper Lee" );

        Agreement agreement5=new Agreement(supplierProductList5,total_orderDiscount5,methoodSupply5,EOM.Plus_30,sCard5.getSupplier_number());

        Supplier s5=this.sup_manager.CreateSupplier(sCard5,agreement5); // The supplier will add to the supplier list and to the list of products
/////////////////
        //-------------------------------supplier6-------------------------------------------------
        //supplierCard
        //contacts

        ContactMember c6_1=new ContactMember("0595145256","Mohamad","Mohamad@gmail.com",15);
        LinkedList<ContactMember> contactList6=new LinkedList<>();
        contactList6.add(c6_1);


        //product_categories
        LinkedList<String> categoriesList6 =new LinkedList<>();
        categoriesList6.add("Cleaning");
        categoriesList6.add("Drink");
        categoriesList6.add("Fruit");


        SupplierCard sCard6=new SupplierCard("Dana",15,383,42313,"Eilat",Payment_method.bit,contactList6,categoriesList6);

        //Agreement
        LinkedList<SupplierProduct> supplierProductList6 =new LinkedList<>();
        LinkedList<PrecentageDiscount> total_orderDiscount6 =new LinkedList<>();

        //products
        //p4_1 p(supplier)_(productIndex)
        LinkedList<PrecentageDiscount> p6_1_discounts=new LinkedList<>();

        Range range6_1_1=new Range(0,800);
        Range range6_1_2=new Range(801,1100);
        Range range6_1_3=new Range(1101,1200);

        //discount(supplier)_(product)_(index)
        PrecentageDiscount discount6_1_1=new PrecentageDiscount(range6_1_1,0);
        PrecentageDiscount discount6_1_2=new PrecentageDiscount(range6_1_2,3);
        PrecentageDiscount discount6_1_3=new PrecentageDiscount(range6_1_3,7);


        p6_1_discounts.add(discount6_1_1);
        p6_1_discounts.add(discount6_1_2);
        p6_1_discounts.add(discount6_1_3);

        SupplierProduct p6_1=new SupplierProduct("Beef 500.0 gr",6000,0.5,31,1500,p6_1_discounts,15);

        //p4_2

        LinkedList<PrecentageDiscount> p6_2_discounts=new LinkedList<>();

        Range range6_2_1=new Range(0,960);
        Range range6_2_2=new Range(961,1200);
        Range range6_2_3=new Range(1201,1400);
        Range range6_2_4=new Range(1401,1500);


        //discount(supplier)_(product)_(index)
        PrecentageDiscount discount6_2_1=new PrecentageDiscount(range6_2_1,2);
        PrecentageDiscount discount6_2_2=new PrecentageDiscount(range6_2_2,3);
        PrecentageDiscount discount6_2_3=new PrecentageDiscount(range6_2_3,7);
        PrecentageDiscount discount6_2_4=new PrecentageDiscount(range6_2_4,8);


        p6_2_discounts.add(discount6_2_1);
        p6_2_discounts.add(discount6_2_2);
        p6_2_discounts.add(discount6_2_3);
        p6_2_discounts.add(discount6_2_4);

        SupplierProduct p6_2=new SupplierProduct("Cheese 200.0 gr",6001,0.2,18.5,1200,p6_2_discounts,15);

        //p4_3 p(supplier)_(productIndex)
        LinkedList<PrecentageDiscount> p6_3_discounts=new LinkedList<>();

        Range range6_3_1=new Range(0,600);
        Range range6_3_2=new Range(601,1000);
        Range range6_3_3=new Range(1001,1200);

        //discount(supplier)_(product)_(index)
        PrecentageDiscount discount6_3_1=new PrecentageDiscount(range6_3_1,2);
        PrecentageDiscount discount6_3_2=new PrecentageDiscount(range6_3_2,8);
        PrecentageDiscount discount6_3_3=new PrecentageDiscount(range6_3_3,13);


        p6_3_discounts.add(discount6_3_1);
        p6_3_discounts.add(discount6_3_2);
        p6_3_discounts.add(discount6_3_3);

        SupplierProduct p6_3=new SupplierProduct("Steaks 2.0 kg",6002,2,180,1200,p6_3_discounts,15);
//
        supplierProductList6.add(p6_1);
        supplierProductList6.add(p6_2);
        supplierProductList6.add(p6_3);


        Range rangeTotal6_1=new Range(0,1200);
        Range rangeTotal6_2=new Range(1201,1600);
        Range rangeTotal6_3=new Range(1601,3500);

        PrecentageDiscount discountTotal6_1=new PrecentageDiscount(rangeTotal6_1,4);
        PrecentageDiscount discountTotal6_2=new PrecentageDiscount(rangeTotal6_2,6);
        PrecentageDiscount discountTotal6_3=new PrecentageDiscount(rangeTotal6_3,13);

        total_orderDiscount6.add(discountTotal6_1);
        total_orderDiscount6.add(discountTotal6_2);
        total_orderDiscount6.add(discountTotal6_3);

        MethodSupply methoodSupply6= new BySuperLee("By Supper Lee" );

        Agreement agreement6=new Agreement(supplierProductList6,total_orderDiscount6,methoodSupply6,EOM.Plus_30,sCard6.getSupplier_number());

        Supplier s6=this.sup_manager.CreateSupplier(sCard6,agreement6); // The supplier will add to the supplier list and to the list of products
    }
}
