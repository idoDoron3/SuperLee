
import Supplier_Module.Business.Agreement.Agreement;
import Supplier_Module.Business.Agreement.MethodSupply.ByFixedDays;
import Supplier_Module.Business.Agreement.MethodSupply.MethodSupply;
import Supplier_Module.Business.Agreement.SupplierProduct;
import Supplier_Module.Business.Card.ContactMember;
import Supplier_Module.Business.Card.SupplierCard;
import Supplier_Module.Business.Defs.EOM;
import Supplier_Module.Business.Defs.Payment_method;
import Supplier_Module.Business.Discount.PrecentageDiscount;
import Supplier_Module.Business.Discount.Range;
import Supplier_Module.Business.Managers.Order_Manager;
import Supplier_Module.Business.Managers.SupplyManager;
import Supplier_Module.Business.Supplier;
import Supplier_Module.DAO.ContactMemberDAO;
import Supplier_Module.DAO.SupplierDAO;
import Supplier_Module.Presentation.Data;
import Supplier_Module.Presentation.UI;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class SupplyTest {


    public void Before_After() {
        Order_Manager.getOrder_Manager().deleteAllData();
        SupplyManager.getSupply_manager().deleteAllData();
        UI.getUser();
        Data.getLoadData().loadData();
    }


    @Test
    public void updateBankAccount() //1
    {
        Before_After();

        Supplier supplier = SupplierDAO.getInstance().getSupplier(10);
        int previousBankAccount = supplier.getCard().getBank_account();
        int newBankAccount = 11111;

        SupplyManager.getSupply_manager().setBank_account(supplier.getCard(), newBankAccount);

        // Verify that the bank account was updated
        Supplier updatedSupplier = SupplierDAO.getInstance().getSupplier(10);
        int updatedBankAccount = updatedSupplier.getCard().getBank_account();
        assertEquals(updatedBankAccount, newBankAccount);


        Before_After();


    }


    @Test
    public void updateAddress() //2
    {
        Before_After();

        Supplier supplier = SupplierDAO.getInstance().getSupplier(10);
        String prevAddress = supplier.getCard().getAddress();
        String currAddress = "new_address";


        SupplyManager.getSupply_manager().setAddress(supplier.getCard(), currAddress);

        // Verify that the bank account was updated
        Supplier updatedSupplier = SupplierDAO.getInstance().getSupplier(10);
        String updatedAddress = updatedSupplier.getCard().getAddress();

        assertEquals(updatedAddress, currAddress);


        Before_After();


    }


    @Test
    public void addContactMember() //3
    {
        Before_After();

        ContactMember c = new ContactMember("77777777777", "NewContactMember", "NewContactMember.com", 10);
        Supplier supplier = SupplierDAO.getInstance().getSupplier(10);

        supplier.getCard().addContact_members(c);

        // Verify that the bank account was updated
        boolean flag = SupplyManager.getSupply_manager().isExsitContactMember(supplier.getCard(), "77777777777");

        assertTrue(flag);


        Before_After();


    }

//    @Test
//    public void deleteContactMember() //4
//    {
//        Before_After();
//
//        Supplier supplier = SupplierDAO.getInstance().getSupplier(10);
//        String existPhoneNumber = "0522255889";
//
//
//        boolean flag = SupplyManager.getSupply_manager().isExsitContactMember(supplier.getCard(), existPhoneNumber);
//
//
//        supplier.getCard().removeContact_members(existPhoneNumber);
//        boolean flag2 = SupplyManager.getSupply_manager().isExsitContactMember(supplier.getCard(), existPhoneNumber);
//
//        assertTrue(!flag2);
//
//
//        Before_After();
//
//    }


    @Test
    public void editContactMemberMail()
    {
        Before_After();

        Supplier supplier = SupplierDAO.getInstance().getSupplier(10);
        String existMailAddress = "Avraham23@gmail.com";
        String newMailAddress = "newMail.com";
        String phoneNum = "0522255889";


        SupplyManager.getSupply_manager().editContact_members(supplier.getCard(), phoneNum, 2, newMailAddress);
        ContactMember c = ContactMemberDAO.getInstance().getContactByPhoneNumber(phoneNum);


        assertEquals(c.getEmail(), newMailAddress);


        Before_After();


    }

//    @Test
//    public void editContactMemberName() //6
//    {
//        Before_After();
//
//        Supplier supplier = SupplierDAO.getInstance().getSupplier(10);
//        String existMailAddress = "Avraham";
//        String newMailAddress = "NEW";
//        String phoneNum = "0522255889";
//
//
//        SupplyManager.getSupply_manager().editContact_members(supplier.getCard(), phoneNum, 1, newMailAddress);
//        ContactMember c = ContactMemberDAO.getInstance().getContactByPhoneNumber(phoneNum);
//
//        assertEquals(c.getName(), newMailAddress);
//
//
//        Before_After();
//
//
//    }


    @Test
    public void removeSupplier() //7
    {
        Before_After();

        Supplier supplier = SupplierDAO.getInstance().getSupplier(10);
        int x = supplier.getCard().getSupplier_number();


        SupplyManager.getSupply_manager().removeSupplier(supplier);
        boolean flag = SupplyManager.getSupply_manager().isExist(x);
        assertTrue(!flag);

        Before_After();


    }

    @Test
    public void addSupplier() //8
    {
        Before_After();

        //-------------------------------supplier2-------------------------------------------------
        //supplierCard
        //contacts

        ContactMember c2_1 = new ContactMember("31546464", "Idan", "Idanosh19@gmail.com", 20);
        LinkedList<ContactMember> contactList2 = new LinkedList<>();
        contactList2.add(c2_1);

        //product_categories
        LinkedList<String> categoriesList2 = new LinkedList<>();
        categoriesList2.add("Cereal");
        categoriesList2.add("Snacks");

        SupplierCard sCard2 = new SupplierCard("newSup", 20, 105, 68323, "Ness-Ziona", Payment_method.bit, contactList2, categoriesList2);

        //Agreement
        LinkedList<SupplierProduct> supplierProductList2 = new LinkedList<>();
        LinkedList<PrecentageDiscount> total_orderDiscount2 = new LinkedList<>();

        //products
        //p2_1 p(supplier)_(productIndex)
        LinkedList<PrecentageDiscount> p2_1_discounts = new LinkedList<>();

        Range range2_1_1 = new Range(0, 500);
        Range range2_1_2 = new Range(501, 1000);
        Range range2_1_3 = new Range(1001, 1500);

        //discount(supplier)_(product)_(index)
        PrecentageDiscount discount2_1_1 = new PrecentageDiscount(range2_1_1, 2);
        PrecentageDiscount discount2_1_2 = new PrecentageDiscount(range2_1_2, 5);
        PrecentageDiscount discount2_1_3 = new PrecentageDiscount(range2_1_3, 7);


        p2_1_discounts.add(discount2_1_1);
        p2_1_discounts.add(discount2_1_2);
        p2_1_discounts.add(discount2_1_3);

        SupplierProduct p2_1 = new SupplierProduct("Captain-Crunch", 2000, 0.75, 25, 1500, p2_1_discounts, 20);

        //p2_2

        LinkedList<PrecentageDiscount> p2_2_discounts = new LinkedList<>();

        Range range2_2_1 = new Range(0, 400);
        Range range2_2_2 = new Range(401, 1000);
        Range range2_2_3 = new Range(1001, 1600);
        Range range2_2_4 = new Range(1601, 2000);


        //discount(supplier)_(product)_(index)
        PrecentageDiscount discount2_2_1 = new PrecentageDiscount(range2_2_1, 2);
        PrecentageDiscount discount2_2_2 = new PrecentageDiscount(range2_2_2, 5);
        PrecentageDiscount discount2_2_3 = new PrecentageDiscount(range2_2_3, 7);
        PrecentageDiscount discount2_2_4 = new PrecentageDiscount(range2_2_4, 7);


        p2_2_discounts.add(discount2_2_1);
        p2_2_discounts.add(discount2_2_2);
        p2_2_discounts.add(discount2_2_3);
        p2_2_discounts.add(discount2_2_4);

        SupplierProduct p2_2 = new SupplierProduct("Lays-chips", 2001, 0.5, 7, 2000, p2_2_discounts, 20);

        supplierProductList2.add(p2_1);
        supplierProductList2.add(p2_2);

        Range rangeTotal2_1 = new Range(800, 1200);
        Range rangeTotal2_2 = new Range(1201, 1600);
        Range rangeTotal2_3 = new Range(1601, 3500);

        PrecentageDiscount discountTotal2_1 = new PrecentageDiscount(rangeTotal2_1, 5);
        PrecentageDiscount discountTotal2_2 = new PrecentageDiscount(rangeTotal2_2, 8);
        PrecentageDiscount discountTotal2_3 = new PrecentageDiscount(rangeTotal2_3, 12);

        total_orderDiscount2.add(discountTotal2_1);
        total_orderDiscount2.add(discountTotal2_2);
        total_orderDiscount2.add(discountTotal2_3);

        int[] days = new int[]{0, 1, 0, 0, 0, 1, 0};
        MethodSupply methoodSupply2 = new ByFixedDays("By Fixed Days", days);

        Agreement agreement2 = new Agreement(supplierProductList2, total_orderDiscount2, methoodSupply2, EOM.Plus_0, sCard2.getSupplier_number());

        Supplier s2 = SupplyManager.getSupply_manager().CreateSupplier(sCard2, agreement2); // The supplier will add to the supplier list and to the list of products

        Supplier supplier = SupplierDAO.getInstance().getSupplier(20);
        int x = supplier.getCard().getSupplier_number();

        boolean flag = SupplyManager.getSupply_manager().isExist(x);
        assertTrue(flag);


        Before_After();


    }


    @Test
    public void addProductToSupplier() //9
    {
        Before_After();

        Supplier supplier = SupplierDAO.getInstance().getSupplier(10);

        //products
        //p1
        LinkedList<PrecentageDiscount> p1_1_discounts = new LinkedList<>();

        Range range1_1_1 = new Range(0, 100);
        Range range1_1_2 = new Range(101, 200);
        Range range1_1_3 = new Range(201, 300);
        Range range1_1_4 = new Range(301, 500);

        PrecentageDiscount discount1_1_1 = new PrecentageDiscount(range1_1_1, 5);
        PrecentageDiscount discount1_1_2 = new PrecentageDiscount(range1_1_2, 7);
        PrecentageDiscount discount1_1_3 = new PrecentageDiscount(range1_1_3, 10);
        PrecentageDiscount discount1_1_4 = new PrecentageDiscount(range1_1_4, 15);

        p1_1_discounts.add(discount1_1_1);
        p1_1_discounts.add(discount1_1_2);
        p1_1_discounts.add(discount1_1_3);
        p1_1_discounts.add(discount1_1_4);

        SupplierProduct p1 = new SupplierProduct("Candy", 1000, 5, 70, 500, p1_1_discounts, 10);


        SupplyManager.getSupply_manager().addProductToAgreement(supplier.getAgreement(), p1, 0);
        boolean flag = SupplyManager.getSupply_manager().isThisProductAlreadyInSystem("Candy");

        assertTrue(flag);


        Before_After();


    }

    @Test
    public void deleteProductFromSupplier() //10
    {
        Before_After();

        Supplier supplier = SupplierDAO.getInstance().getSupplier(15);
        String pName = "Steaks 2.0 kg";

        SupplyManager.getSupply_manager().deleteProductFromSupplier(supplier.getCard().getSupplier_number(), pName);
        boolean flag = SupplyManager.getSupply_manager().isThisProductAlreadyInSystem(pName);

        assertTrue(!flag);


        Before_After();


    }

    @Test
    public void removeAll() //11
    {
        Before_After();

        SupplyManager.getSupply_manager().deleteAllData();
        Order_Manager.getOrder_Manager().deleteAllData();

        boolean flag = true;
        for (int i = 10; i <= 15; i++) {
            if (SupplyManager.getSupply_manager().isExist(i)) {
                System.out.println("Fail test_11");
                flag = false;
            }

        }

        assertTrue(flag);


        Before_After();


    }


    @Test
    public void exactPriceAfterDiscount() //12
    {
        Before_After();

        Supplier supplier = SupplierDAO.getInstance().getSupplier(15);
        String pName = "Steaks 2.0 kg";
        double realPrice = 17640.0;

        double price = SupplyManager.getSupply_manager().productPriceBySupplier(supplier, pName, 100);

        assertEquals(price, realPrice);


        Before_After();


    }


}
