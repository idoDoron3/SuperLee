package Stock.Presentation;
import Stock.Business.Product;
import Stock.Business.ProductManager;
import Stock.Business.UniqueStringGenerator;
import Stock.DataAccess.ProductDAO;
import Stock.Service.ProductService;
import Supplier_Module.Service.SupplierService;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.Scanner;

public class ProductUI {

    private static final ProductService productService = ProductService.getInstance();

    String categoryStr, subCategoryStr, subSubCategoryStr, manufacturer, quantity, minQuantity, weight,
            productCatalogNumber, uniqueCode, reason, quantityStr;
    Date expirationDate;
    boolean flag;
    Scanner input = new Scanner(System.in);

    public void startMenu(String numOfMarketToManagement, LocalDate localDate) {

        boolean running = true;
        while (running) {
            System.out.println("-------- Welcome to the Product menu of market number " + Integer.parseInt(numOfMarketToManagement) + " --------");
            // case 1 at MainUI
            System.out.println("1) Add a new product ");
            // case 2 and Case 3 at MainUI
            System.out.println("2) Update quantity of existing product ");
            // case 5 at MainUI
            System.out.println("3) Inform on a defected/ expired product ");
            // case 7 at MainUI
            System.out.println("4) Get information on selected product ");
            // case 10 at MainUI
            System.out.println("5) Change min quantity to product ");
            // Exit from product's menu to stock's menu
            System.out.println("6) Go back to Stock menu ");

            System.out.println("Select the number you would like to access ");
            System.out.println("-----------------------");
            String selection = input.nextLine();
            switch (selection) {
                case "1":
                    addNewProductCase1(localDate);
                    break;

                case "2":
                    flag = true;
                    while (flag) {
                        System.out.println("How do you want to change the quantity of existing product? ");
                        System.out.println("1) Increase the quantity for single product (would be Supplier's function) ");
                        System.out.println("2) Sell/remove from stock a single product ");
                        System.out.println("3) return to menu.");
                        char option = input.next().charAt(0);
                        input.nextLine();
                        switch (option) {
                            case '1':
                                addMoreItemsToProductCase2Point1();
                                flag = false;
                                break;
                            case '2':
                                sellMoreItemsFromProductCase2Point2(localDate);
                                flag = false;
                                break;
                            case '3':
                                flag = false;
                                break;
                            default:
                                System.out.println("Wrong input");
                                break;
                        }
                    }
                    break;

                case "3":
                    markAsDamagedCase3();
                    break;

                case "4":
                    flag = true;
                    char check;
                    while (flag) {
                        System.out.println("How do you want to receive the information about product? ");
                        System.out.println("1) Information by product's categories.");
                        System.out.println("2)  Information by product's catalog number.");
                        System.out.println("3) return to menu.");
                        check = input.next().charAt(0);
                        input.nextLine();
                        switch (check) {
                            case '1':
                                printProductInformationCase4Point1();
                                flag = false;
                                break;
                            case '2':
                                printProductInformationCase4Point2();
                                flag = false;
                                break;
                            case 3:
                                flag = false;
                                break;
                            default:
                                System.out.println("Wrong input");
                                break;
                        }
                    }
                    break;

                case "5":
                    flag = true;
                    while (flag) {
                        System.out.println("which way you want to Update minimum amount? ");
                        System.out.println("1) by product's categories.");
                        System.out.println("2) by product's ID.");
                        System.out.println("3) return to menu.");
                        check = input.next().charAt(0);
                        input.nextLine();
                        switch (check) {
                            case '1':
                                setMinimumQuantityCase5Point1(localDate);
                                flag = false;
                                break;
                            case '2':
                                setMinimumQuantityCase5Point2(localDate);
                                flag = false;
                                break;
                            case 3:
                                flag = false;
                                break;
                            default:
                                System.out.println("Wrong input");
                                break;
                        }
                    }
                    break;

                case "6":
                    running = false;
                    break;

                default:
                    System.out.println("Wrong input");
                    break;
            }
        }
    }

    // Case 1 in product's menu
    void addNewProductCase1(LocalDate localDate) {
        System.out.println("Whats is your product's category? ");
        categoryStr = input.nextLine();
        if (!checkIfOnlyLetters(categoryStr)) {
            System.out.println("your product's category is not a valid string ");
            return;
        }
        System.out.println("Whats is your product's sub-category? ");
        subCategoryStr = input.nextLine();
        if (!checkSubCategory(subCategoryStr)) {
            System.out.println("your product's subCategory is not a valid string ");
            return;
        }
        System.out.println("Whats is your product's sub-sub-category, in <double string> format? ");
        subSubCategoryStr = input.nextLine();
        if (!checkSubSubCategory(subSubCategoryStr)) {
            return;
        }

        System.out.println("Whats is your product's manufacturer? ");
        manufacturer = input.nextLine();
        if (!checkIfOnlyLetters(manufacturer)) {
            System.out.println("your product's manufacturer is not a valid string ");
            return;
        }

        System.out.println("Whats is your product's quantity? ");
        quantity = input.nextLine();
        if (!checkIfPositiveIntegerNumber(quantity)) {
            System.out.println("your product's quantity is not a positive number ");
            return;
        }

        System.out.println("Whats is your product's weight? ");
        weight = input.nextLine();
        if (!checkIfPositiveDoubleNumber(weight)) {
            System.out.println("your product's weight is not a positive number ");
            return;
        }

        System.out.println("Whats is your product's minimum quantity? ");
        minQuantity = input.nextLine();
        if (!checkIfPositiveIntegerNumber(minQuantity)) {
            System.out.println("your product's minimum quantity is not a positive number ");
            return;
        }

        expirationDate = dateInput();
        if (expirationDate == null) {
            return;
        }
        if (!productService.addNewProduct(categoryStr, subCategoryStr, subSubCategoryStr, manufacturer,
                Integer.parseInt(quantity), Integer.parseInt(minQuantity), Double.parseDouble(weight), expirationDate,localDate)) {
            System.out.println("The product already exist in stock! ");
        }
        else {
            SupplierService.getSupplierService().updatePeriodOrders(ProductService.getInstance().sendToSupplierAllProductsQuantity(),localDate);
            System.out.println("Product added! ");
        }
    }

    // Case 2.1 in product's menu
    void addMoreItemsToProductCase2Point1() {
        System.out.println("Whats is your product's category? ");
        categoryStr = input.nextLine();
        if (!checkIfOnlyLetters(categoryStr)) {
            System.out.println("your product's category is not a valid string ");
            return;
        }
        System.out.println("Whats is your product's sub-category? ");
        subCategoryStr = input.nextLine();
        if (!checkSubCategory(subCategoryStr)) {
            System.out.println("your product's subCategory is not a valid string ");
            return;
        }
        System.out.println("What is your product's sub-sub-category? ");
        subSubCategoryStr = input.nextLine();
        if (!checkSubSubCategory(subSubCategoryStr)) {
            return;
        }
//        product = market.getProductByCategories(categoryStr, subCategoryStr, subSubCategoryStr);
        Product product = productService.getProductByCategories(subCategoryStr, subSubCategoryStr);
        if (product == null) {
            System.out.println("Product was not found ");
            return;
        }
        System.out.println("How many " + product.getSubCategoryName().getName() + " " + product.getSubSubCategory().getName() + " do you want to add? ");
        quantity = input.nextLine();
        if (!checkIfPositiveIntegerNumber(quantity)) {
            System.out.println("You have to add a positive number for quantity ");
            return;
        }
        expirationDate = dateInput();
        if (expirationDate == null) {
            return;
        }
        //product.addMoreItemsToProduct(Integer.parseInt(quantity), expirationDate);
        // ---------- already exists in ProductManager ----------
        //product.setCatalogNumber();
        productService.addMoreItemsToProduct(product, expirationDate, Integer.parseInt(quantity) );
        //ProductManager.getInstance().addMoreItemsToProduct(product,expirationDate,Integer.parseInt(quantity));
        System.out.println("Product's quantity updated! ");
    }

    // Case 2.2 in product's menu
    void sellMoreItemsFromProductCase2Point2(LocalDate localDate) {
        System.out.println("What is the catalog number of the product you sell/remove? ");
        String productCatalogNumber = input.nextLine();
        Product soldProduct = productService.getProductByUniqueCode(productCatalogNumber);
        //Product soldProduct = market.getByProductID(productID);

        // looks for product if found return it else return null
        if (soldProduct == null) {
            System.out.println("The product was not found!");
            return;
        }
        System.out.println("What is the  product  quantity you sell/remove? ");
        quantity = input.nextLine();
        if (!checkIfPositiveIntegerNumber(quantity) || Integer.parseInt(quantity) >= 31) {
            System.out.println("The quantity value was invalid! you can by up to 30 products at once");
            return;
        }
        // ----------- here -----------
        productService.sellProductsByUniqueCode(soldProduct, Integer.parseInt(quantity),localDate);
        //market.sellProductsByID(productID, Integer.parseInt(quantity));

        // New
        if(soldProduct.getStoreQuantity() + soldProduct.getStorageQuantity() == 0){
            System.out.println("ALERT!!!!\nthe product: " + soldProduct.getName()+" is in shortage");
        }
        else if(soldProduct.getStoreQuantity() + soldProduct.getStorageQuantity() < soldProduct.getMinimumQuantity()){
            System.out.println("ALERT!!!!\nthe product: " + soldProduct.getName()+" is under the minimum quantity");
        }
        // Old
//        if(sold.getStoreQuantity() + sold.getStorageQuantity() < market.stock.getBlackLine(sold)){
//            System.out.println("ALERT!!!!\nthe product: " + sold.getName()+" is under the minimum quantity");
//        }
    }

    void markAsDamagedCase3() {
        System.out.println("What is the catalog number of the defected product? ");
        productCatalogNumber = input.nextLine();
        Product defectedProduct = productService.getProductByUniqueCode(productCatalogNumber);
        //Product defected = market.getByProductID(productCatalogNumber);

        // looks for product by ID if found return it else return null
        if (defectedProduct == null) {
            System.out.println("The product was not found!");
            return;
        }
        System.out.println("What is the unique code (barcode) of the product? ");
        uniqueCode = input.nextLine();
        if (!checkIfPositiveIntegerNumber(uniqueCode)) {
            System.out.println("You have to enter a positive number of barcode ");
            return;
        }
        if (defectedProduct.getUniqueProduct(Integer.parseInt(uniqueCode))) {
            System.out.println("What is the Problem with the product? ");
            reason = input.nextLine();
            if (!checkReason(reason)) {
                System.out.println("The Problem with the product is not a valid string ");
                return;
            }
            productService.markAsDamaged(defectedProduct, Integer.parseInt(uniqueCode), reason);
            System.out.println("Product mark as Damaged! ");
            //defectedProduct.markAsDamaged(Integer.parseInt(uniqueCode), reason);
        }
        else {
            System.out.println("The unique code (barcode) is invalid!");
        }
    }


    // Case 4.1 in product's menu
    void printProductInformationCase4Point1() {
        System.out.println("Whats is your product's category? ");
        categoryStr = input.nextLine();
        if (!checkIfOnlyLetters(categoryStr)) {
            System.out.println("your product's category is not a valid string ");
            return;
        }
        System.out.println("Whats is your product's sub-category? ");
        subCategoryStr = input.nextLine();
        if (!checkSubCategory(subCategoryStr)) {
            System.out.println("your product's subCategory is not a valid string ");
            return;
        }
        System.out.println("Whats is your product's sub-sub-category, in <double string> format? ");
        subSubCategoryStr = input.nextLine();
        if (!checkSubSubCategory(subSubCategoryStr)) {
            return;
        }
        Product product = productService.getProductByCategories(subCategoryStr, subSubCategoryStr);
        if (product == null) {
            System.out.println("Product was not found ");
            return;
        }
        productService.printProductInformation(1, product);
        //market.printProductInformation(1, product);
    }

    // Case 4.2 in product's menu
    void printProductInformationCase4Point2() {
        System.out.println("What is the catalog number of the product? ");
        productCatalogNumber = input.nextLine();
        Product product = productService.getProductByUniqueCode(productCatalogNumber);
        // looks for product by ID if found return it else return null
        if (product == null) {
            System.out.println("Product was not found ");
            return;
        }
        productService.printProductInformation(2, product);
        //market.printProductInformation(2, product);
    }

    // Case 5.1 in product's menu
    void setMinimumQuantityCase5Point1(LocalDate localDate) {
        System.out.println("Whats is your product's category? ");
        categoryStr = input.nextLine();
        if (!checkIfOnlyLetters(categoryStr)) {
            System.out.println("your product's category is not a valid string ");
            return;
        }
        System.out.println("Whats is your product's sub-category? ");
        subCategoryStr = input.nextLine();
        if (!checkSubCategory(subCategoryStr)) {
            System.out.println("your product's subCategory is not a valid string ");
            return;
        }
        System.out.println("Whats is your product's sub-sub-category, in <double string> format? ");
        subSubCategoryStr = input.nextLine();
        if (!checkSubSubCategory(subSubCategoryStr)) {
            return;
        }
        Product product = productService.getProductByCategories(subCategoryStr, subSubCategoryStr);
        if (product == null) {
            System.out.println("Product was not found ");
            return;
        }
        System.out.println("What is the new minimum quantity? ");
        quantityStr = input.nextLine();
        if (!checkIfPositiveIntegerNumber(quantityStr)) {
            System.out.println("You have to add a positive number to minimum quantity ");
            return;
        }
        productService.setMinimumQuantity(product, Integer.parseInt(quantityStr),localDate);
        //product.setMinimumQuantity(Integer.parseInt(quantityStr));
        //System.out.println("The new minimum quantity of " + product.getName() + " is " + quantityStr);
    }

    // Case 5.2 in product's menu
    void setMinimumQuantityCase5Point2(LocalDate localDate) {
        System.out.println("What is the catalog number of the product? ");
        productCatalogNumber = input.nextLine();
        Product product = productService.getProductByUniqueCode(productCatalogNumber);
        // looks for product by ID if found return it else return null
        if (product == null) {
            System.out.println("Product was not found ");
            return;
        }
        System.out.println("What is the new minimum quantity? ");
        quantityStr = input.nextLine();
        if (!checkIfPositiveIntegerNumber(quantityStr)) {
            System.out.println("You have to add a positive number to minimum quantity ");
            return;
        }
        productService.setMinimumQuantity(product, Integer.parseInt(quantityStr),localDate);
        //product.setMinimumQuantity(Integer.parseInt(quantityStr));
        //System.out.println("The new minimum quantity of " + product.getName() + " is " + quantityStr);
    }


    Boolean checkIfPositiveIntegerNumber(String number) {
        return number.matches("[0-9]+") && Integer.parseInt(number) > 0;
    }

    boolean checkIfPositiveDoubleNumber(String number) {
        try {
            double d = Double.parseDouble(number);
            return d > 0.0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    Boolean checkIfOnlyLetters(String str) {
        return str.matches("[a-zA-Z' ]+");
    }

    Boolean checkSubCategory(String subCategoryStr) {
        return subCategoryStr.matches("[a-zA-Z0-9% ]+");
    }

    Boolean checkSubSubCategory(String subSubCategoryStr) {
        /**
         * Checks if a given string matches the format of a sub-sub category.
         * A sub-sub category should consist of a number followed by a space and a word.
         * Example: "5 g", "1 l", "10 p".
         *
         * @param subSubCategoryStr the sub-sub category string to be checked
         * @return true if the string matches the format of a sub-sub category, false otherwise
         */
        String[] parts = subSubCategoryStr.split(" ");
        double number;
        if (parts.length == 2) {
            try {
                number = Double.parseDouble(parts[0]);
                String word = parts[1];
                if (!word.matches("[a-zA-Z]+")) {
                    System.out.println("your product's subSubCategory does not match the format.");
                    return false;
                }
            } catch (NumberFormatException e) {
                System.out.println("your product's subSubCategory does not match the format.");
                return false;
            }
        } else {
            System.out.println("your product's subSubCategory does not match the format.");
            return false;
        }
        return true;
    }

    Boolean checkReason(String reason) {
        return reason.matches("[a-zA-Z0-9/ ]+");
    }

    Date dateInput() {
        /**
         * Prompts the user to enter a date in the format "dd/MM/yyyy" and returns it as a Date object.
         * If the date is invalid or in the past, it prints an error message and returns null.
         *
         * @return the date entered by the user as a Date object, or null if it is invalid or in the past.
         */
        Scanner input = new Scanner(System.in);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        System.out.println("Enter a expiration date in dd/MM/yyyy format:");
        String dateStr = input.nextLine();
        String[] parts = dateStr.split("/");
        int day = Integer.parseInt(parts[0]);
        int month = Integer.parseInt(parts[1]);
        int year = Integer.parseInt(parts[2]);
        if (month < 1 || month > 12) {
            System.out.println("Invalid month");
            return null;
        }
        if (day < 1 || day > 31) {
            System.out.println("Invalid day");
            return null;
        }
        LocalDate date = LocalDate.of(year, month, day);
        if (date.isBefore(LocalDate.now())) {
            System.out.println("The date is not in the future");
            return null;
        }
        Date dateToReturn = Date.from(date.atStartOfDay().atZone(java.time.ZoneId.systemDefault()).toInstant());
        return dateToReturn;
    }



}
