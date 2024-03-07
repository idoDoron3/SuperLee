package Supplier_Module.Presentation;

import Supplier_Module.Business.Agreement.Agreement;
import Supplier_Module.Business.Agreement.MethodSupply.MethodSupply;
import Supplier_Module.Business.Agreement.SupplierProduct;
import Supplier_Module.Business.Discount.PrecentageDiscount;
import Supplier_Module.Business.Managers.SupplyManager;
import Supplier_Module.Business.Supplier;
import Supplier_Module.DAO.OrderDAO;

import java.util.Scanner;

public class AgreementMenu {
    private static AgreementMenu agreementMenu = null;
    private SupplyManager sup_manager;

    /**
     * constructor
     * private constractor for singleton
     */
    private AgreementMenu() {
        this.sup_manager = SupplyManager.getSupply_manager();
    }

    public static AgreementMenu getAgreementMenu() {
        if (agreementMenu == null) {
            agreementMenu = new AgreementMenu();
        }
        return agreementMenu;
    }

    public Agreement CreateAgreement(int supplierID)
    {
        Scanner input= new Scanner(System.in);

        Agreement a=new Agreement(supplierID);
        System.out.println("--Create Agreement Step--");
        int moreItems=1;
        System.out.println("--Adding Products--");
        while(moreItems==1)
        {
            CreateProduct(a,supplierID, 1); // creating the product and adding it to the agreement
            System.out.println("For adding another product press 1, else press something else");
            moreItems=input.nextInt();
        }

        System.out.println("--Adding Discount by total amount of products by percentage--");
        int moreD=1;
        while(moreD==1)
        {
            PrecentageDiscount pd= CreateDiscount();
            this.sup_manager.addTotal_orderDiscount(a,pd);
            System.out.println("For adding another discount press 1, else press something else");
            moreD=input.nextInt();
        }

        System.out.println("Pick your supply method:");
        this.MethodSupplyChoose(a);

        int eom=10;
        while (eom!=1&& eom!=2 &&eom!=3)
        {
            System.out.println("Choose EOM you want");
            System.out.println("1. Plus 0");
            System.out.println("2. Plus 30");
            System.out.println("3. Plus 60");
            eom=input.nextInt();
        }

        this.sup_manager.setEom(a,eom);

        return a;
    }


    /**help function for choosing for a new supplier a supply method
     * there are 3 options: 1.By fixed days 2. By Supper Lee 3. By supply days
     */
    public void MethodSupplyChoose(Agreement agreement)
    {
        Scanner input= new Scanner(System.in);
        int choice=4;
        while (choice!=1&&choice!=2&&choice!=3)
        {
            System.out.println("1. By Fixed Days");
            System.out.println("2. By Supper Lee");
            System.out.println("3. By Supply Days");
            choice=input.nextInt();
            switch (choice)
            {
                case 1:
                {
                    Scanner scanner= new Scanner(System.in);
                    int[] supplydays = new int[]{0,0,0,0,0,0,0};
                    int day=1;
                    while (day!=0)
                    {
                        System.out.println("1.Sunday");
                        System.out.println("2.Monday");
                        System.out.println("3.Tuesday");
                        System.out.println("4.Wednesday");
                        System.out.println("5.Thursday");
                        System.out.println("6.Friday");
                        System.out.println("7.Saturday");
                        System.out.println("0.Quit");
                        day=scanner.nextInt();

                        if(day>0 && day<8)
                        {
                            supplydays[day-1]=1;
                        }
                        else if (day!=0)
                        {
                            System.out.println("Invalid input");
                        }
                    }

                    MethodSupply method=this.sup_manager.createByFixedDays("By Fixed Days",supplydays);
                    this.sup_manager.setMethodSupply(agreement,method);
                    break;
                }

                case 2: // empty case (its ok)
                {
                    MethodSupply method=this.sup_manager.createBySuperLee("By Supper Lee");
                    this.sup_manager.setMethodSupply(agreement,method);
                    break;
                }

                case 3:
                {
                    Scanner scanner= new Scanner(System.in);
                    System.out.println("Enter how many days is required for you to supply");
                    int x=scanner.nextInt();
                    MethodSupply method=this.sup_manager.createBySupplyDays("By Supply Days",x);
                    this.sup_manager.setMethodSupply(agreement,method);
                    break;

                }

                default:
                {
                    System.out.println("Enter valid number");
                    break;
                }

            }
        }

    }


    /**
     * @param agreement
     * functin that creating a product and add it to the agreement
     */
    public String CreateProduct(Agreement agreement, int supplierID, int data)
    {
        Scanner input= new Scanner(System.in);
        String product_name="";
        int product_id=0;
        boolean check=true;
        while(check==true)
        {
            System.out.println("Please enter the product name");
            product_name= input.nextLine();
            check=this.sup_manager.isExistName(agreement,product_name);
            if(check==true)
                System.out.println("This product name is already exist, please enter again");
        }

        check=true;
        while(check==true)
        {
            System.out.println("Please enter product local id");
            product_id=input.nextInt();
            check=this.sup_manager.isExistID(agreement,product_id);
            if(check==true)
                System.out.println("This product id is already exist, please enter again");
        }


        System.out.println("Please enter product weight");
        double weight =input.nextDouble();

        System.out.println("Please enter product price");
        double price = input.nextDouble();

        System.out.println("Please enter the amount of goods in units you have: ");
        int amount = input.nextInt();

        SupplierProduct p= sup_manager.createProduct(product_name,product_id,weight,price,amount,supplierID);
        System.out.println("Enter disconts:");
        addDiscount(p);
        this.sup_manager.addProductToAgreement(agreement,p, data); // adding the product to the agreement
        return  product_name;
    }

    /**
     * function that add discount to this product
     */
    public void addDiscount(SupplierProduct supplierProduct)
    {
        Scanner input = new Scanner(System.in);
        int moreDiscount=1;
        while(moreDiscount==1)
        {
            PrecentageDiscount x=CreateDiscount();
            this.sup_manager.addDiscountToProduct(supplierProduct,x); // adding the discount to the list (range and percentage)
            System.out.println("If you want to enter another discount press 1, else press something else");
            moreDiscount=input.nextInt();
        }
    }

    public PrecentageDiscount CreateDiscount()
    {
        Scanner input = new Scanner(System.in);
        System.out.println("Enter a lower limit of the quantity :");
        int min=input.nextInt();
        System.out.println("Enter a upper limit of the quantity :");
        int max=input.nextInt();
        System.out.println("Enter the discount in percentage :");
        int percentage=input.nextInt();
        PrecentageDiscount discount=sup_manager.createPrecentageDiscount(min,max,percentage);
        return discount;
    }

    public void EditAgreement(Supplier x)
    {
        Scanner input1= new Scanner(System.in);
        int choice=9;
        while(choice!=0) {
            System.out.println("1. Add product");
            System.out.println("2. Remove product");
            System.out.println("3. Edit product");
            System.out.println("4. View total discounts that you offer the client");
            System.out.println("0. Back to main menu");
            choice = input1.nextInt();


            switch (choice)
            {
                case 1:
                {
                    String productName=this.CreateProduct(x.getAgreement(),x.getCard().getSupplier_number(),0);
                    if(this.sup_manager.isThisProductAlreadyInSystem(productName))
                    {
                        this.sup_manager.addSupplierByProduct(productName,x,true);
                    }
                    else
                    {
                        this.sup_manager.addSupplierByProduct(productName,x,false);
                    }

                    this.sup_manager.printAllProducts(x);
                    break;
                }

                case 2:
                {
                    Scanner input2 = new Scanner(System.in);
                    System.out.println("Please enter the name of the product you want delete");
                    String temp=input2.nextLine();
                    if(OrderDAO.getInstance().isProductInPeriodOrder(temp,x.getAgreement().getSupplier_number())){
                        System.out.println("this product is already include in "+ x.getCard().getSupplier_name() + " agreement to super lee period order");
                        break;
                    }
                    String temp2=x.getAgreement().removeProductFromAgreement(temp);
                    this.sup_manager.deleteProductFromSupplier(x.getCard().getSupplier_number(),temp2);
                    this.sup_manager.printAllProducts(x);
                    break;
                }

                case 3:
                {
                    Scanner input2 = new Scanner(System.in);
                    System.out.println("Please enter the name of the product you want edit");
                    String productName=input2.nextLine();
                    boolean isExist=this.sup_manager.isExistName(x.getAgreement(),productName);
                    if(isExist)
                    {
                        if(OrderDAO.getInstance().isProductInPeriodOrder(productName,x.getAgreement().getSupplier_number())) {
                            System.out.println("this product is already include in " + x.getCard().getSupplier_name() + " agreement to super lee period order");
                            break;
                        }
                        SupplierProduct supplierProduct = this.sup_manager.getProduct(x.getAgreement(),productName);
                        this.editProduct(supplierProduct);

                    }
                    else
                    {
                        System.out.println("There is no product calls: "+productName);
                    }

                    break;
                }

                case 4:
                {
                    System.out.println("Total discount (about all the purchase)");
                    this.sup_manager.printTotalDiscounts(x.getAgreement());

                    break;
                }

//                case 5:
//                {
//                    Scanner input2 = new Scanner(System.in);
//                    System.out.println("The current total discount (about all the purchase):");
//                    this.sup_manager.printTotalDiscounts(x.getAgreement());
//
//                    int choice2=2;
//                    while (choice2!=0 && choice2!=1)
//                    {
//                        System.out.println("0.Keep current discounts ");
//                        System.out.println("1.Delete current discounts and enter new discounts ");
//                        choice2=input2.nextInt();
//
//                        switch (choice2)
//                        {
//                            case 0:
//                            {
//                                System.out.println("No change will be made");
//                                break;
//                            }
//                            case 1:
//                            {
//                                Scanner input = new Scanner(System.in);
//                                x.getAgreement().getTotal_orderDiscount().clear();
//                                int moreD=1;
//                                while(moreD==1)
//                                {
//                                    PrecentageDiscount pd=this.CreateDiscount();
//                                    this.sup_manager.addTotal_orderDiscount(x.getAgreement(),pd);
//                                    System.out.println("For adding another discount press 1, else press something else");
//                                    moreD=input.nextInt();
//                                }
//                                break;
//                            }
//                            default :
//                            {
//                                System.out.println("Invalid input");
//                                break;
//                            }
//                        }
//                    }
//
//                    break;
//                }

                default:
                {
                    if(choice!=0)
                        System.out.println("Invalid input");
                    break;
                }
            }
        }

    }

    /**function get Product and edit it
     * there is option to change: price, amount, discounts
     */
    public void editProduct(SupplierProduct p)
    {
        int choice=9;
        while (choice!=0)
        {
            Scanner input= new Scanner(System.in);
            System.out.println("1. Edit price");
            System.out.println("2. Edit amount in stock");
            System.out.println("0. Quit");
            choice=input.nextInt();

            switch (choice)
            {
                case 1:
                {
                    System.out.println("Enter the new price");
                    double priceNew=input.nextInt();
                    this.sup_manager.setUnit_price(p,priceNew);
                    this.sup_manager.printProductInfo(p);
                    break;
                }
                case 2:
                {
                    System.out.println("Enter the new amount");
                    int amountNew=input.nextInt();
                    this.sup_manager.setAmount_available(p,amountNew);
                    this.sup_manager.printProductInfo(p);
                    break;
                }

                default:
                {
                    System.out.println("Invalid input");
                    break;
                }
            }
        }
    }



}
